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

$stat = ConnectionProperties::getStatistic();

// Provider distribution
$providerData = new GDataTable();
$providerData->addColumn(new GColumn("string", "p", "Provider"));
$providerData->addColumn(new GColumn("number", "c", "Count"));

foreach ($stat->getProviderDist() as $provider => $count) {
    $row = new GRow();
    $row->addCell(new GCell($provider));
    $row->addCell(new GCell($count));
    $providerData->addRow($row);
}

// Average signal
$signalData = new GDataTable();
$signalData->addColumn(new GColumn("string", "p", "Provider"));
$signalData->addColumn(new GColumn("number", "a", "Average"));
foreach ($stat->getSignalAvg() as $provider => $avg) {
    $row = new GRow();
    $row->addCell(new GCell($provider));
    $row->addCell(new GCell($avg));
    $signalData->addRow($row);
}

$tmplt["pageTitle"] = "Connection";
$tmplt["jsFunctDrawChart"] = "drawProvider();
        drawSignal();";
if ($svgCharts) {
    // Draw SVG-Charts
    // ---------------
    $tmplt["jsDrawFunctions"] = "
    function drawProvider() {
        var data = new google.visualization.DataTable(" . $providerData->getJsonObject() . ");

        var options = {
            title: 'Provider distribution',
            'width':800,
            'height':400
        };


        var chart = new google.visualization.PieChart(document.getElementById('provider'));
        chart.draw(data, options);
    }

    function drawSignal() {
        var data = new google.visualization.DataTable(" . $signalData->getJsonObject() . ");

        var options = {
            title: 'Signal',
            'width':700,
            'height':300,
            hAxis: {title: 'Provider'}
        };


        var chart = new google.visualization.ColumnChart(document.getElementById('signal'));
        chart.draw(data, options);
    }";
}
$tmplt["content"] = "
            <h1>Connection Statistics</h1>";
if ($svgCharts) {
    // Draw SVG-Charts
    // ---------------
    $tmplt["content"] .= "}
            <div id=\"provider\" style=\"width:800; height:400\"></div>
            <div id=\"signal\" style=\"width:700; height:300\"></div>";
} else {
    // Draw static/PNG-charts
    // ----------------------

    $providerChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
    $providerChart->setTitle("Provider distribution");
    $bridge = new GChartPhpBridge($providerData);
    $bridge->pushData($providerChart, GChartPhpBridge::LEGEND);

    $signalChart = new gchart\gBarChart($chart->getAxisChartWidth(), $chart->getAxisChartHeight() + 100);
    $signalChart->setTitle("Provider");
    $signalChart->setVisibleAxes(array('x', 'y'));
    $signalChart->setBarWidth(50, 4, 50);
    $bridge = new GChartPhpBridge($signalData);
    $bridge->pushData($signalChart, GChartPhpBridge::AXIS_LABEL);

    $tmplt["content"] .= "
            <p><img src=\"" . $providerChart->getUrl() . "\" /></p>
            <p><img src=\"" . $signalChart->getUrl() . "\" /></p>";
}

$tmplt["content"] .= "
            <p><b>Active roaming:</b> " . sprintf("%3.2f %%", $stat->getRoamingPerc()) . "</p>
            <p><b>Average bluetooth connections:</b> " . sprintf("%3.2f", $stat->getBluetoothAvg()) . "</p>
            <p><b>Average Wi-Fi connections:</b> " . sprintf("%3.2f", $stat->getWifiAvg()) . "</p>";

include ("template.php");
?>
<?php

Database::getInstance()->disconnect();
?>

