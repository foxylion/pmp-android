<?php

/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp-Webservice
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

define("INCLUDE", true);
require("./../inc/graphs_framework.inc.php");

if ($deviceIdValid) {
    $eventManager = $device->getCellularConnectionEventManager();
    $events = $chart->getEventsByScale($eventManager, $timeMs, $calendar->getDaysInMonth());

    // cellular connection status
    $dateColumn = new GColumn("datetime", "d", "Date");
    $airplaneColumn = new GColumn("number", "a", "Airplane");
    $roamingColumn = new GColumn("number", "r", "Roaming");

    $connectionData = new GDataTable();
    $connectionData->addColumn($dateColumn);
    $connectionData->addColumn($airplaneColumn);
    $connectionData->addColumn($roamingColumn);

    foreach ($events as $event) {
        $levelRow = new GRow();
        $levelRow->addCell(new GCell("new Date(" . $event->getTimestamp() . ")"));
        $levelRow->addCell(new GCell((int) $event->isAirplane()));
        $levelRow->addCell(new GCell((int) $event->isRoaming()));
        $connectionData->addRow($levelRow);
    }

    $tmplt["pageTitle"] = "Cellular Connection";
    $tmplt["jsFunctDrawChart"] = "drawConnection();";

    $tmplt["jsDrawFunctions"] = "function drawConnection() {
        var data = new google.visualization.DataTable(" . $connectionData->getJsonObject()  . ");

        var options = {
            title: 'Cellular connection status',
            'width':800,
            'height':150
        };


        var chart = new google.visualization.AreaChart(document.getElementById('connection'));
        chart.draw(data, options);
    }";

    $tmplt["content"] = "
            <h1>Cellular Connection Events</h1>
            <div id=\"connection\" style=\"width:800; height:150\"></div>";
}
include ("template.php");
?>
<?php

Database::getInstance()->disconnect();
?>

