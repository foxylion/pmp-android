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

$stat = BatteryProperties::getStatistic();

// Technology distribution
$technologyData = new GDataTable();
$technologyData->addColumn(new GColumn("string", "t", "Technology"));
$technologyData->addColumn(new GColumn("number", "c", "Count"));

foreach ($stat->getTechnologyDist() as $tech => $count) {
    $row = new GRow();
    $row->addCell(new GCell($tech));
    $row->addCell(new GCell($count));
    $technologyData->addRow($row);
}

// Average health
$healthData = new GDataTable();
$healthData->addColumn(new GColumn("string", "t", "Technology"));
$healthData->addColumn(new GColumn("number", "a", "Average"));
foreach ($stat->getHealthAvg() as $tech => $avg) {
    $row = new GRow();
    $row->addCell(new GCell($tech));
    $row->addCell(new GCell($avg));
    $healthData->addRow($row);
}

$tmplt["pageTitle"] = "Battery";

if ($svgCharts) {
    // Draw SVG-Charts
    // ---------------
    $tmplt["jsFunctDrawChart"] = "drawTechnology();
        drawHealth();";

    $tmplt["jsDrawFunctions"] = "
    function drawTechnology() {
        var data = new google.visualization.DataTable(" . $technologyData->getJsonObject() . ");

        var options = {
            title: 'Technology distribution',
            'width':800,
            'height':400
        };


        var chart = new google.visualization.PieChart(document.getElementById('technology'));
        chart.draw(data, options);
    }

    function drawHealth() {
        var data = new google.visualization.DataTable(" . $healthData->getJsonObject() . ");

        var options = {
            title: 'Health',
            'width':700,
            'height':300,
            hAxis: {title: 'Technology'}
        };


        var chart = new google.visualization.ColumnChart(document.getElementById('health'));
        chart.draw(data, options);
    }";
}
$tmplt["content"] = "
            <h1>Battery Statistics</h1>";

if ($svgCharts) {
    // Draw SVG-Charts
    // ---------------
    $tmplt["content"] .= "
            <div id=\"technology\" style=\"width:800; height:400\"></div>
            <div id=\"health\" style=\"width:700; height:300\"></div>";
} else {
    // Draw static/PNG-charts
    // ----------------------

    $technologyChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
    $technologyChart->setTitle("Technology distribution");
    $bridge = new GChartPhpBridge($technologyData);
    $bridge->pushData($technologyChart, GChartPhpBridge::LEGEND);

    $healthChart = new gchart\gBarChart($chart->getAxisChartWidth(), $chart->getAxisChartHeight() + 100);
    $healthChart->setTitle("Health");
    $healthChart->setVisibleAxes(array('x', 'y'));
    $healthChart->setBarWidth(60, 4, 60);
    $bridge = new GChartPhpBridge($healthData);
    $bridge->pushData($healthChart, GChartPhpBridge::AXIS_LABEL);

    $tmplt["content"] .= "
            <p><img src=\"" . $technologyChart->getUrl() . "\" /></p>
            <p><img src=\"" . $healthChart->getUrl() . "\" /></p>";
}

include ("template.php");
?>
<?php

Database::getInstance()->disconnect();
?>

