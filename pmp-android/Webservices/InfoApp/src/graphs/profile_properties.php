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
use infoapp\properties\ProfileProperties;

define("INCLUDE", true);
require("./../inc/graphs_framework.inc.php");

$stat = ProfileProperties::getStatistic();

// Average signal
// Ring type distribution
$ringData = new DataTable();
$ringData->addColumn(new Column("string", "r", "Ring type"));
$ringData->addColumn(new Column("number", "a", "Average"));

foreach ($stat->getRingDist() as $ring => $count) {
    $row = new Row();
    switch ($ring) {
        case ProfileProperties::BOTH:
            $row->addCell(new Cell("Ringtone and Vibration"));
            break;
        case ProfileProperties::RING:
            $row->addCell(new Cell("Ringtone"));
            break;
        case ProfileProperties::SILENT:
            $row->addCell(new Cell("Silent"));
            break;
        case ProfileProperties::VIBRATION:
            $row->addCell(new Cell("Vibration"));
            break;
    }

    $row->addCell(new Cell($count));
    $ringData->addRow($row);
}

$tmplt["pageTitle"] = "Profile";
$tmplt["jsFunctDrawChart"] = "drawRing();";
if ($svgCharts) {
    // Draw SVG-Charts
    // ---------------
    $tmplt["jsDrawFunctions"] = "
    function drawRing() {
        var data = new google.visualization.DataTable(" . $ringData->getJsonObject() . ");

        var options = {
            title: 'Ring type distribution',
            'width':" . $chart->getPieChartWidth() . ",
            'height':" . $chart->getPieChartHeight() . "
        };


        var chart = new google.visualization.ColumnChart(document.getElementById('ring'));
        chart.draw(data, options);
    }";
}
$tmplt["content"] = "
            <h1>Profile Statistics</h1>";
if ($svgCharts) {
    // Draw SVG-Charts
    // ---------------
    $tmplt["content"] .= "
            <div id=\"ring\"></div>";
} else {
    // Draw static/PNG-charts
    // ----------------------

    $ringChart = new gchart\gBarChart($chart->getAxisChartWidth(), $chart->getAxisChartHeight() + 100);
    $ringChart->setTitle("Ring type distribution");
    $ringChart->setVisibleAxes(array('x', 'y'));
    $ringChart->setBarWidth(50, 4, 50);
    $bridge = new GChartPhpBridge($ringData);
    $bridge->pushData($ringChart, GChartPhpBridge::AXIS_LABEL);

    $tmplt["content"] .= "
            <p><img src=\"" . $ringChart->getUrl() . "\" /></p>";
}

$tmplt["content"] .= "
            <p><b>Average contacts:</b> " . sprintf("%3.2f", $stat->getContactsAvg()) . "</p>
            <p><b>Average apps:</b> " . sprintf("%3.2f", $stat->getAppsAvg()) . "</p>";

include ("template.php");

Database::getInstance()->disconnect();
?>

