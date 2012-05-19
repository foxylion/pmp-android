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

define("INCLUDE", true);
require("./../inc/graphs_framework.inc.php");

if ($deviceIdValid) {
    $eventManager = $device->getConnectionEventManager();
    $events = $chart->getEventsByScale($eventManager, $timeMs, $calendar->getDaysInMonth());

    // Bluetooth and wifi adapter chart
    $dateColumn = new Column("datetime", "d", "Date");
    $enabledColumn = new Column("number", "e", "Enabled");
    $connectedColumn = new Column("number", "c", "Connected");
    $cityIColumn = new Column("string", "ca", "City Annotation", null, "{\"role\": \"annotation\"}");
    $cityNameColumn = new Column("string", "cn", "City Name", null, "{\"role\": \"annotationText\"}");

    $bluetoothAdapterData = new DataTable();
    $bluetoothAdapterData->addColumn($dateColumn);
    $bluetoothAdapterData->addColumn($enabledColumn);
    $bluetoothAdapterData->addColumn($connectedColumn);
    $bluetoothAdapterData->addColumn($cityIColumn);
    $bluetoothAdapterData->addColumn($cityNameColumn);

    $wifiAdapterData = new DataTable();
    $wifiAdapterData->addColumn($dateColumn);
    $wifiAdapterData->addColumn($enabledColumn);
    $wifiAdapterData->addColumn($connectedColumn);
    $wifiAdapterData->addColumn($cityIColumn);
    $wifiAdapterData->addColumn($cityNameColumn);

    // City chart
    $cityColumn = new Column("string", "c", "Cities");
    $countColumn = new Column("number", "n", "Connections");

    $bluetoothCitiesData = new DataTable();
    $bluetoothCitiesData->addColumn($cityColumn);
    $bluetoothCitiesData->addColumn($countColumn);

    $wifiCitiesData = new DataTable();
    $wifiCitiesData->addColumn($cityColumn);
    $wifiCitiesData->addColumn($countColumn);

    // Get data used to display the charts
    $citiesBluetooth = array();
    $citiesWifi = array();

    foreach ($events as $event) {

        $city = $event->getCity();
        switch ($event->getMedium()) {
            case ConnectionEvent::BLUETOOTH:

                // Count cities if a connection has been established
                if ($event->isConnected()) {
                    if (key_exists($city, $citiesBluetooth)) {
                        $citiesBluetooth[$city]++;
                    } else {
                        $citiesBluetooth[$city] = 1;
                    }
                }

                // Connection status
                $levelRow = new Row();
                $levelRow->addCell(new Cell($event->getTimestamp()));
                $levelRow->addCell(new Cell((int) $event->isEnabled()));
                $levelRow->addCell(new Cell((int) $event->isConnected()));
                if ($chart->showAnnotations()) {
                    $levelRow->addCell(new Cell("i"));
                    $levelRow->addCell(new Cell($event->getCity()));
                }

                $bluetoothAdapterData->addRow($levelRow);

                break;

            case ConnectionEvent::WIFI:

                // Count cities if a connection has been established
                if ($event->isConnected()) {
                    if (key_exists($city, $citiesWifi)) {
                        $citiesWifi[$city]++;
                    } else {
                        $citiesWifi[$city] = 1;
                    }
                }

                // Connection status
                $levelRow = new Row();
                $levelRow->addCell(new Cell($event->getTimestamp()));
                $levelRow->addCell(new Cell((int) $event->isEnabled()));
                $levelRow->addCell(new Cell((int) $event->isConnected()));
                if ($chart->showAnnotations()) {
                    $levelRow->addCell(new Cell("i"));
                    $levelRow->addCell(new Cell($event->getCity()));
                }

                $wifiAdapterData->addRow($levelRow);
                break;
        }
    }

    $bluetoothCitiesData->addRowsAssocArray($citiesBluetooth);
    $wifiCitiesData->addRowsAssocArray($citiesWifi);

    $tmplt["pageTitle"] = "Connection";
    if ($svgCharts) {
        // Draw SVG-Charts
        // ---------------
        $tmplt["jsFunctDrawChart"] = "drawBluetoothConnection();
                drawWifiConnection();
                drawBluetoothChart();
                drawWifiChart();";

        $tmplt["jsDrawFunctions"] = "function drawBluetoothConnection() {
        var data = new google.visualization.DataTable(" . $bluetoothAdapterData->getJsonObject() . ");

        var options = {
            title: 'Bluetooth adapter status',
            'width':" . $chart->getAxisChartWidth() . ",
            'height':" . $chart->getAxisChartHeight() . "
        };


        var chart = new google.visualization.AreaChart(document.getElementById('connectionBluetooth'));
        chart.draw(data, options);
    }

    function drawWifiConnection() {
        var data = new google.visualization.DataTable(" . $wifiAdapterData->getJsonObject() . ");

        var options = {
            title: 'Wi-Fi adapter status',
            'width':" . $chart->getAxisChartWidth() . ",
            'height':" . $chart->getAxisChartHeight() . "
        };


        var chart = new google.visualization.AreaChart(document.getElementById('connectionWifi'));
        chart.draw(data, options);
    }


    function drawBluetoothChart() {
        var data = new google.visualization.DataTable(" . $bluetoothCitiesData->getJsonObject() . ");

        var options = {'title':'Number of bluetooth parings per city',
            'width':" . $chart->getPieChartWidth() . ",
            'height':" . $chart->getPieChartHeight() . "
        };

        var chart = new google.visualization.PieChart(document.getElementById('countBluetooth'));
        chart.draw(data, options);
    }

    function drawWifiChart() {
        var data = new google.visualization.DataTable(" . $wifiCitiesData->getJsonObject() . ");

        var options = {'title':'Number of Wi-Fi connections per city',
            'width':" . $chart->getPieChartWidth() . ",
            'height':" . $chart->getPieChartHeight() . "
         };

        var chart = new google.visualization.PieChart(document.getElementById('countWifi'));
        chart.draw(data, options)
    }";
    }

    $tmplt["content"] = "
            <h1>Connection Events</h1>";
    if ($svgCharts) {
        // Draw SVG-Charts
        // ---------------
        $tmplt["content"] .= "
            <div id=\"connectionBluetooth\" style=\"width:800; height:150\"></div>
            <div id=\"connectionWifi\" style=\"width:800; height:150\"></div>
            <div id=\"countBluetooth\" style=\"width:600; height:400\"></div>
            <div id=\"countWifi\" style=\"width:600; height:400\"></div>";
    } else {
        // Draw static/PNG-charts
        // ----------------------
        $scale = $chart->getScaleYAxis($calendar->getDaysInMonth());

        $connectionBluetoothChart = new gchart\gLineChart($chart->getAxisChartWidth(), $chart->getAxisChartHeight());
        $connectionBluetoothChart->setTitle("Bluetooth adapter status");
        $connectionBluetoothChart->setProperty("cht", "lxy");
        $connectionBluetoothChart->setVisibleAxes(array('x', 'y'));
        $bridge = new GChartPhpBridge($bluetoothAdapterData);
        $bridge->pushData($connectionBluetoothChart, GChartPhpBridge::Y_COORDS, $scale);

        $connectionWifiChart = new gchart\gLineChart($chart->getAxisChartWidth(), $chart->getAxisChartHeight());
        $connectionWifiChart->setTitle("Wi-Fi adapter status");
        $connectionWifiChart->setProperty("cht", "lxy");
        $connectionWifiChart->setVisibleAxes(array('x', 'y'));
        $bridge = new GChartPhpBridge($wifiAdapterData);
        $bridge->pushData($connectionWifiChart, GChartPhpBridge::Y_COORDS, $scale);

        $bluetoothCountChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
        $bluetoothCountChart->setTitle("Number of bluetooth parings per city");
        $bridge = new GChartPhpBridge($bluetoothCitiesData);
        $bridge->pushData($bluetoothCountChart, GChartPhpBridge::LEGEND);

        $wifiCountChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
        $wifiCountChart->setTitle("Number of Wi-Fi connections per city");
        $bridge = new GChartPhpBridge($wifiCitiesData);
        $bridge->pushData($wifiCountChart, GChartPhpBridge::LEGEND);

        $tmplt["content"] .= "
            <p><img src=\"" . $connectionBluetoothChart->getUrl() . "\" alt=\"Cannot display chart as there is to much data. Please reduce the scale or use the interactive charts.\" /></p>
            <p><img src=\"" . $connectionWifiChart->getUrl() . "\" /></p>
            <p><img src=\"" . $bluetoothCountChart->getUrl() . "\" /></p>
            <p><img src=\"" . $wifiCountChart->getUrl() . "\" /></p>";
    }
}
include ("template.php");

Database::getInstance()->disconnect();
?>

