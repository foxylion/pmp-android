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
use infoapp\events\AwakeEvent;
use infoapp\events\ScreenEvent;
use infoapp\googlecharttools\Cell;
use infoapp\googlecharttools\Column;
use infoapp\googlecharttools\DataTable;
use infoapp\googlecharttools\GChartPhpBridge;
use infoapp\googlecharttools\Row;

define("INCLUDE", true);
require("./../inc/graphs_framework.inc.php");

if ($deviceIdValid) {
    $awEventManager = $device->getAwakeEventManager();
    $scrEventManager = $device->getScreenEventManager();
    $awEvents = $chart->getEventsByScale($awEventManager, $timeMs, $calendar->getDaysInMonth());
    $scrEvents = $chart->getEventsByScale($scrEventManager, $timeMs, $calendar->getDaysInMonth());


    // Data preparation
    // ----------------
    // Put awake and screen events into one assoc 2D array where the timestamp is used as the key and
    // $rawData[time][0] for awake events and $rawDate[time][1] for screen events
    $rawData = array();

    $awakes = 0;
    $screens = 0;

    // Fill array with awake events and increment counter
    foreach ($awEvents as $event) {
        $rawData[$event->getTimestamp()][0] = $event;
        if ($event->isAwake()) {
            $awakes++;
        }
    }

    // Fill array with screen events
    foreach ($scrEvents as $event) {
        $rawData[$event->getTimestamp()][1] = $event;
        if ($event->isDisplayOn()) {
            $screens++;
        }
    }

    ksort($rawData);

    // Area chart
    // ----------
    $dateColumn = new Column("datetime", "d", "Date");
    $awakeColumn = new Column("number", "a", "Awake");
    $awakeAnColumn = new Column("string", "aa", "Status", null, "{\"role\": \"annotation\"}");
    $awakeAnTextColumn = new Column("string", "aat", "Status Tooltip", null, "{\"role\": \"annotationText\"}");
    $screenColumn = new Column("number", "a", "Screen");
    $screenAnColumn = new Column("string", "aa", "Status", null, "{\"role\": \"annotation\"}");
    $screenAnTextColumn = new Column("string", "aat", "Status Tooltip", null, "{\"role\": \"annotationText\"}");

    $areaChartData = new DataTable();
    $areaChartData->addColumn($dateColumn);
    $areaChartData->addColumn($awakeColumn);
    $areaChartData->addColumn($awakeAnColumn);
    $areaChartData->addColumn($awakeAnTextColumn);
    $areaChartData->addColumn($screenColumn);
    $areaChartData->addColumn($screenAnColumn);
    $areaChartData->addColumn($screenAnTextColumn);

    $lastAwEvent = new AwakeEvent(1, 1, false);
    $lastScrEvent = new ScreenEvent(1, 1, false);
    // Fill chart with data form $rawData
    foreach ($rawData as $timestamp => $raw) {
        $row = new Row();
        $row->addCell(new Cell($timestamp));
        // Values
        if (isset($raw[0])) {
            $row->addCell(new Cell((int) $raw[0]->isAwake()));
            $row->addCell(new Cell(null));
            $row->addCell(new Cell(null));
            $lastAwEvent = $raw[0];
        } else {
            $row->addCell(new Cell((int) $lastAwEvent->isAwake()));
            if ($chart->showAnnotations()) {
                $row->addCell(new Cell("i"));
                $row->addCell(new Cell("Value interpolated"));
            } else {
                $row->addCell(new Cell(null));
                $row->addCell(new Cell(null));
            }
        }


        if (isset($raw[1])) {
            $row->addCell(new Cell((int) $raw[1]->isDisplayOn()));
            $lastScrEvent = $raw[1];
        } else {
            $row->addCell(new Cell((int) $lastScrEvent->isDisplayOn()));
            if ($chart->showAnnotations()) {
                $row->addCell(new Cell("i"));
                $row->addCell(new Cell("Value interpolated"));
            } else {
                $row->addCell(new Cell(null));
                $row->addCell(new Cell(null));
            }
        }

        // Annotations
        $areaChartData->addRow($row);
    }

    // Ratios
    // ------
    $statusColumn = new Column("string", "c", "Status");
    $countColumn = new Column("number", "n", "Count");

    // Awake
    $awakeRatio = new DataTable();
    $awakeRatio->addColumn($statusColumn);
    $awakeRatio->addColumn($countColumn);

    $awakeRowT = new Row();
    $awakeRowT->addCell(new Cell("Awake"));
    $awakeRowT->addCell(new Cell($awakes));
    $awakeRowF = new Row();
    $awakeRowF->addCell(new Cell("Asleep"));
    $awakeRowF->addCell(new Cell(count($awEvents) - $awakes));

    $awakeRatio->addRow($awakeRowT);
    $awakeRatio->addRow($awakeRowF);

    // Screen
    $screenRatio = new DataTable();
    $screenRatio->addColumn($statusColumn);
    $screenRatio->addColumn($countColumn);

    $screenRowT = new Row();
    $screenRowT->addCell(new Cell("On"));
    $screenRowT->addCell(new Cell($screens));
    $screenRowF = new Row();
    $screenRowF->addCell(new Cell("Off"));
    $screenRowF->addCell(new Cell(count($scrEvents) - $screens));

    $screenRatio->addRow($screenRowT);
    $screenRatio->addRow($screenRowF);

    // Awake / Screen ratio
    $awakeScreenRatio = new DataTable();
    $awakeScreenRatio->addColumn($statusColumn);
    $awakeScreenRatio->addColumn($countColumn);

    $awakeScreenRowA = new Row();
    $awakeScreenRowA->addCell(new Cell("Awake"));
    $awakeScreenRowA->addCell(new Cell($awakes));
    $awakeScreenRowS = new Row();
    $awakeScreenRowS->addCell(new Cell("Screen On"));
    $awakeScreenRowO = new Row();
    $awakeScreenRowO->addCell(new Cell("Device asleep (and screen off)"));
    $off = count($awEvents) + count($scrEvents) - $awakes - $screens;
    $awakeScreenRowO->addCell(new Cell($off));
    $awakeScreenRowS->addCell(new Cell($screens));
    $awakeScreenRatio->addRow($awakeScreenRowA);
    $awakeScreenRatio->addRow($awakeScreenRowS);
    $awakeScreenRatio->addRow($awakeScreenRowO);


    $tmplt["pageTitle"] = "Standby";
    if ($svgCharts) {
        // Draw SVG-Charts
        // ---------------
        $tmplt["jsFunctDrawChart"] = "drawAreaChart();
            drawAwakeRatio();
            drawScreenRatio();
            drawAwakeScreenRatio();";

        $tmplt["jsDrawFunctions"] = "function drawAreaChart() {
        var data = new google.visualization.DataTable(" . $areaChartData->getJsonObject() . ");

        var options = {
            title: 'Standby status',
            'width':" . $chart->getAxisChartWidth() . ",
            'height':" . $chart->getAxisChartHeight() . "
        };


        var chart = new google.visualization.AreaChart(document.getElementById('areaChart'));
        chart.draw(data, options);
    }

    function drawAwakeRatio() {
        var data = new google.visualization.DataTable(" . $awakeRatio->getJsonObject() . ");

        var options = {
            title: 'Device active ratio',
            'width':" . $chart->getPieChartWidth() . ",
            'height':" . $chart->getPieChartHeight() . "
        };


        var chart = new google.visualization.PieChart(document.getElementById('awakeRatio'));
        chart.draw(data, options);
    }

    function drawScreenRatio() {
        var data = new google.visualization.DataTable(" . $screenRatio->getJsonObject() . ");

        var options = {
            title: 'Screen on/off ratio',
            'width':" . $chart->getPieChartWidth() . ",
            'height':" . $chart->getPieChartHeight() . "
        };


        var chart = new google.visualization.PieChart(document.getElementById('screenRatio'));
        chart.draw(data, options);
    }

    function drawAwakeScreenRatio() {
        var data = new google.visualization.DataTable(" . $awakeScreenRatio->getJsonObject() . ");

        var options = {
            title: 'Awake/Screen ratio',
            'width':" . $chart->getPieChartWidth() . ",
            'height':" . $chart->getPieChartHeight() . "
        };


        var chart = new google.visualization.PieChart(document.getElementById('awakeScreenRatio'));
        chart.draw(data, options);
    }";
    }

    $tmplt["content"] = "
            <h1>Standby Events</h1>";

    if ($svgCharts) {
        // Draw SVG-Charts
        // ---------------
        $tmplt["content"] .= "
            <div id=\"areaChart\" style=\"width:800; height:150\"></div>
            <div id=\"awakeRatio\" style=\"width:800; height:400\"></div>
            <div id=\"screenRatio\" style=\"width:800; height:400\"></div>
            <div id=\"awakeScreenRatio\" style=\"width:800; height:400\"></div>";
    } else {
        // Draw static/PNG-charts
        // ----------------------
        $scale = $chart->getScaleYAxis($calendar->getDaysInMonth());

        $areaChart = new gchart\gLineChart($chart->getAxisChartWidth(), $chart->getAxisChartHeight());
        $areaChart->setTitle("Standby status");
        $areaChart->setProperty("cht", "lxy");
        $areaChart->setVisibleAxes(array('x', 'y'));
        $bridge = new GChartPhpBridge($areaChartData);
        $bridge->pushData($areaChart, GChartPhpBridge::Y_COORDS, $scale);

        $awakeRatioChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
        $awakeRatioChart->setTitle("Device active ratio");
        $bridge = new GChartPhpBridge($awakeRatio);
        $bridge->pushData($awakeRatioChart, GChartPhpBridge::LEGEND);

        $screenRatioChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
        $screenRatioChart->setTitle("Device active ratio");
        $bridge = new GChartPhpBridge($screenRatio);
        $bridge->pushData($screenRatioChart, GChartPhpBridge::LEGEND);

        $awakeScreenRatioChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
        $awakeScreenRatioChart->setTitle("Awake/Screen ratio");
        $bridge = new GChartPhpBridge($awakeScreenRatio);
        $bridge->pushData($awakeScreenRatioChart, GChartPhpBridge::LEGEND);


        $tmplt["content"] .= "
            <p><img src=\"" . $areaChart->getUrl() . "\" alt=\"Cannot display chart as there is to much data. Please reduce the scale or use the interactive charts.\" /></p>
            <p><img src=\"" . $awakeRatioChart->getUrl() . "\" /></p>
            <p><img src=\"" . $screenRatioChart->getUrl() . "\" /></p>
            <p><img src=\"" . $awakeScreenRatioChart->getUrl() . "\" /></p>";
    }
}
include ("template.php");

Database::getInstance()->disconnect();
?>

