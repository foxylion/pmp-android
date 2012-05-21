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

use infoapp\Database;
use infoapp\googlecharttools\Cell;
use infoapp\googlecharttools\Column;
use infoapp\googlecharttools\DataTable;
use infoapp\googlecharttools\GChartPhpBridge;
use infoapp\googlecharttools\Row;

define("INCLUDE", true);
require("./../inc/graphs_framework.inc.php");

if ($deviceIdValid) {
    $eventManager = $device->getCellularConnectionEventManager();
    $events = $chart->getEventsByScale($eventManager, $timeMs, $calendar->getDaysInMonth());

    // cellular connection status
    $dateColumn = new Column("datetime", "d", "Date");
    $airplaneColumn = new Column("number", "a", "Airplane");

    $connectionData = new DataTable();
    $connectionData->addColumn($dateColumn);
    $connectionData->addColumn($airplaneColumn);

    foreach ($events as $event) {
        $levelRow = new Row();
        $levelRow->addCell(new Cell($event->getTimestamp()));
        $levelRow->addCell(new Cell((int) $event->isAirplane()));
        $connectionData->addRow($levelRow);
    }

    $tmplt["pageTitle"] = "Cellular Connection";
    if ($svgCharts) {
        // Draw SVG-Charts
        // ---------------
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
    }

    $tmplt["content"] = "
            <h1>Cellular Connection Events</h1>";

    if ($svgCharts) {
        // Draw SVG-Charts
        // ---------------
        $tmplt["content"] .= "
            <div id=\"connection\"></div>";
    } else {
        // Draw static/PNG-charts
        // ----------------------
        $scale = $chart->getScaleXAxis($calendar);
        $scaleLabel = $chart->getScaleXAxisLabel($calendar);
        $offset = $chart->getOffsetXAxis($calendar);

        $connectionChart = new gchart\gLineChart($chart->getAxisChartWidth(), $chart->getAxisChartHeight());
        $connectionChart->setTitle("Cellular connection status");
        $connectionChart->setProperty("cht", "lxy");
        $connectionChart->setVisibleAxes(array('x', 'y'));
        $bridge = new GChartPhpBridge($connectionData);
        $bridge->pushData($connectionChart, GChartPhpBridge::Y_COORDS, $scale, $scaleLabel, $offset);

        $tmplt["content"] .= "
            <p><img src=\"" . $connectionChart->getUrl() . "\" alt=\"Cannot display chart as there is to much data. Please reduce the scale or use the interactive charts.\" /></p>";
    }
}
include ("template.php");

Database::getInstance()->disconnect();
?>

