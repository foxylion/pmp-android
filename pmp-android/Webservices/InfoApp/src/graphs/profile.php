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
use infoapp\Chart;
use infoapp\events\ProfileEvent;
use infoapp\googlecharttools\Cell;
use infoapp\googlecharttools\Column;
use infoapp\googlecharttools\DataTable;
use infoapp\googlecharttools\GChartPhpBridge;
use infoapp\googlecharttools\Row;

define("INCLUDE", true);
require("./../inc/graphs_framework.inc.php");

if ($deviceIdValid) {
    $eventManager = $device->getProfileEventManager();
    $events = $chart->getEventsByScale($eventManager, $timeMs, $calendar->getDaysInMonth());

    // Bluetooth and wifi adapter chart
    $dateColumn = new Column("datetime", "d", "Date");
    $callInColumn = new Column("number", "c", "Call Incoming");
    $callOutColumn = new Column("number", "c", "Call Outgoing");
    $smsInColumn = new Column("number", "s", "SMS Incoming");
    $smsOutColumn = new Column("number", "s", "SMS Outgoing");
    $cityColumn = new Column("string", "c", "City", null, "{\"role\": \"tooltip\"}");

    $callData = new DataTable();
    $callData->addColumn($dateColumn);
    $callData->addColumn($callInColumn);
    $callData->addColumn($cityColumn);
    $callData->addColumn($callOutColumn);
    $callData->addColumn($cityColumn);

    $smsData = new DataTable();
    $smsData->addColumn($dateColumn);
    $smsData->addColumn($smsInColumn);
    $smsData->addColumn($cityColumn);
    $smsData->addColumn($smsOutColumn);
    $smsData->addColumn($cityColumn);


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

        // City
        switch ($event->getDirection()) {
            case ProfileEvent::INCOMING:
                $cityTooltip = $event->getCity();
                break;
            case ProfileEvent::OUTGOING:
                $cityTooltip = $event->getCity();
                break;
        }
        $cityTooltip .= "\\n" . Chart::timeMillisToString("Y/m/d, H:m:s", $event->getTimestamp());

        $callRow = new Row();
        $callRow->addCell(new Cell($event->getTimestamp()));
        $smsRow = new Row();
        $smsRow->addCell(new Cell($event->getTimestamp()));

        $city = $event->getCity();
        if ($event->getEvent() == ProfileEvent::CALL) {
            if ($event->getDirection() == ProfileEvent::INCOMING) {
                $callRow->addCell(new Cell(1));
                $callRow->addCell(new Cell($cityTooltip));
            } else {
                $callRow->addCell(new Cell("null"));
                $callRow->addCell(new Cell("null"));
                $callRow->addCell(new Cell(1));
                $callRow->addCell(new Cell($cityTooltip));
            }
        } else {
            $callRow->addCell(new Cell("null"));
            $callRow->addCell(new Cell("null"));
            $callRow->addCell(new Cell("null"));
            $callRow->addCell(new Cell("null"));
        }

        $callData->addRow($callRow);

        if ($event->getEvent() == ProfileEvent::SMS) {
            if ($event->getDirection() == ProfileEvent::INCOMING) {
                $smsRow->addCell(new Cell(1));
                $smsRow->addCell(new Cell($cityTooltip));
                $smsRow->addCell(new Cell("null"));
                $smsRow->addCell(new Cell("null"));
            } else {
                $smsRow->addCell(new Cell("null"));
                $smsRow->addCell(new Cell("null"));
                $smsRow->addCell(new Cell(1));
                $smsRow->addCell(new Cell($cityTooltip));
            }
        } else {
            $smsRow->addCell(new Cell("null"));
            $smsRow->addCell(new Cell("null"));
            $smsRow->addCell(new Cell("null"));
            $smsRow->addCell(new Cell("null"));
        }

        $smsData->addRow($smsRow);
    }

    $bluetoothCitiesData->addRowsAssocArray($citiesBluetooth);
    $wifiCitiesData->addRowsAssocArray($citiesWifi);

    $tmplt["pageTitle"] = "Connection";
    if ($svgCharts) {
        // Draw SVG-Charts
        // ---------------
        $tmplt["jsFunctDrawChart"] = "drawCallChart();
                drawSmsChart();";

        $tmplt["jsDrawFunctions"] = "function drawCallChart() {
        var data = new google.visualization.DataTable(" . $callData->getJsonObject() . ");

        var options = {
            title: 'Call events',
            'width':" . $chart->getAxisChartWidth() . ",
            'height':" . $chart->getAxisChartHeight() . "
        };


        var chart = new google.visualization.ScatterChart(document.getElementById('call'));
        chart.draw(data, options);
    }

    function drawSmsChart() {
        var data = new google.visualization.DataTable(" . $smsData->getJsonObject() . ");

        var options = {
            title: 'SMS events',
            'width':" . $chart->getAxisChartWidth() . ",
            'height':" . $chart->getAxisChartHeight() . "
        };


        var chart = new google.visualization.ScatterChart(document.getElementById('sms'));
        chart.draw(data, options);
    }";
    }

    $tmplt["content"] = "
            <h1>Connection Events</h1>";
    if ($svgCharts) {
        // Draw SVG-Charts
        // ---------------
        $tmplt["content"] .= "
            <div id=\"call\" style=\"width:800; height:150\"></div>
            <div id=\"sms\" style=\"width:800; height:150\"></div>";
    } else {
        // Draw static/PNG-charts
        // ----------------------
        $scale = $chart->getScaleYAxis($calendar->getDaysInMonth());

        $connectionBluetoothChart = new gchart\gLineChart($chart->getAxisChartWidth(), $chart->getAxisChartHeight());
        $connectionBluetoothChart->setTitle("Bluetooth adapter status");
        $connectionBluetoothChart->setProperty("cht", "lxy");
        $connectionBluetoothChart->setVisibleAxes(array('x', 'y'));
        $bridge = new GChartPhpBridge($callData);
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

