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
use infoapp\events\ConnectionEvent;
use infoapp\googlecharttools\Cell;
use infoapp\googlecharttools\Column;
use infoapp\googlecharttools\DataTable;
use infoapp\googlecharttools\GChartPhpBridge;
use infoapp\googlecharttools\Row;
use infoapp\properties\ConnectionProperties;

define("INCLUDE", true);
require("./../inc/graphs_framework.inc.php");

$stat = ConnectionProperties::getStatistic();

// Provider distribution
$providerData = new DataTable();
$providerData->addColumn(new Column("string", "p", "Provider"));
$providerData->addColumn(new Column("number", "c", "Count"));

foreach ($stat->getProviderDist() as $provider => $count) {
    $row = new Row();
    $row->addCell(new Cell($provider));
    $row->addCell(new Cell($count));
    $providerData->addRow($row);
}

// Network distribution
$networkData = new DataTable();
$networkData->addColumn(new Column("string", "n", "Network-Type"));
$networkData->addColumn(new Column("number", "c", "Count"));

foreach ($stat->getNetworkDist() as $network => $count) {
    $row = new Row();
    switch ($network) {
        case ConnectionProperties::NETWORK_TYPE_GPRS:
            $type = "GPRS";
            break;
        case ConnectionProperties::NETWORK_TYPE_EDGE:
            $type = "EDGE";
            break;
        case ConnectionProperties::NETWORK_TYPE_UMTS:
            $type = "UMTS";
            break;
        case ConnectionProperties::NETWORK_TYPE_HSDPA:
            $type = "HSDPA";
            break;
        case ConnectionProperties::NETWORK_TYPE_HSUPA:
            $type = "HSUPA";
            break;
        case ConnectionProperties::NETWORK_TYPE_HSPA:
            $type = "HSPA";
            break;
        case ConnectionProperties::NETWORK_TYPE_CDMA:
            $type = "CDMA";
            break;
        case ConnectionProperties::NETWORK_TYPE_EVDO_0:
            $type = "EVDO 0";
            break;
        case ConnectionProperties::NETWORK_TYPE_EVDO_A:
            $type = "EVDO A";
            break;
        case ConnectionProperties::NETWORK_TYPE_EVDO_B:
            $type = "EVDO B";
            break;
        case ConnectionProperties::NETWORK_TYPE_1xRTT:
            $type = "1xRTT";
            break;
        case ConnectionProperties::NETWORK_TYPE_IDEN:
            $type = "IDEN";
            break;
        case ConnectionProperties::NETWORK_TYPE_LTE:
            $type = "LTE";
            break;
        case ConnectionProperties::NETWORK_TYPE_EHRPD:
            $type = "EHRPD";
            break;
        case ConnectionProperties::NETWORK_TYPE_HSPAP:
            $type = "HSPAP";
            break;
        default:
            $type = "Unknown";
            break;
    }

    $row->addCell(new Cell($type));
    $row->addCell(new Cell($count));
    $networkData->addRow($row);
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
            'width':" . $chart->getPieChartWidth() . ",
            'height':" . $chart->getPieChartHeight() . "
        };


        var chart = new google.visualization.PieChart(document.getElementById('provider'));
        chart.draw(data, options);
    }

    function drawSignal() {
        var data = new google.visualization.DataTable(" . $networkData->getJsonObject() . ");

        var options = {
            title: 'Network-Type distribution',
            'width':" . $chart->getPieChartWidth() . ",
            'height':" . $chart->getPieChartHeight() . "
        };


        var chart = new google.visualization.PieChart(document.getElementById('network'));
        chart.draw(data, options);
    }";
}
$tmplt["content"] = "
            <h1>Connection Statistics</h1>";
if ($svgCharts) {
    // Draw SVG-Charts
    // ---------------
    $tmplt["content"] .= "
            <div id=\"provider\"></div>
            <div id=\"network\"></div>";
} else {
    // Draw static/PNG-charts
    // ----------------------

    $providerChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
    $providerChart->setTitle("Provider distribution");
    $bridge = new GChartPhpBridge($providerData);
    $bridge->pushData($providerChart, GChartPhpBridge::LEGEND);

    $networkChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
    $networkChart->setTitle("Network-Type distribution");
    $bridge = new GChartPhpBridge($networkData);
    $bridge->pushData($networkChart, GChartPhpBridge::LEGEND);

    $tmplt["content"] .= "
            <p><img src=\"" . $providerChart->getUrl() . "\" /></p>
            <p><img src=\"" . $networkChart->getUrl() . "\" /></p>";
}

$tmplt["content"] .= "
            <p><b>Active roaming:</b> " . sprintf("%3.2f %%", $stat->getRoamingPerc()) . "</p>
            <p><b>Average bluetooth connections:</b> " . sprintf("%3.2f", $stat->getBluetoothAvg()) . "</p>
            <p><b>Average Wi-Fi connections:</b> " . sprintf("%3.2f", $stat->getWifiAvg()) . "</p>";

include ("template.php");

Database::getInstance()->disconnect();
?>

