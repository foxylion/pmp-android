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

    // Get data used to display the charts
    $connectionColumns = array(array("datetime", "Date"), array("number", "Airplane"), array("number", "Roaming"));
    $connectionRows = array();

    foreach ($events as $event) {
                // Connection status
                $connectionRows[] = array("new Date(" . $event->getTimestamp() . ")", (int) $event->isAirplane(), (int) $event->isRoaming());

    }

    $tmplt["pageTitle"] = "Connection";
    $tmplt["jsFunctDrawChart"] = "drawConnection();";

    $tmplt["jsDrawFunctions"] = "function drawConnection() {
        " . Chart::getDataObject($connectionColumns, $connectionRows) . "

        var options = {
            title: 'Cellular connection status',
            'width':800,
            'height':150
        };


        var chart = new google.visualization.AreaChart(document.getElementById('connection'));
        chart.draw(data, options);
    }";

    $tmplt["content"] = "
            <h1>Connection Events</h1>
            <div id=\"connection\" style=\"width:800; height:150\"></div>";
}
include ("template.php");
?>
<?php

Database::getInstance()->disconnect();
?>

