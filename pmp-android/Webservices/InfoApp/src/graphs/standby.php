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
    $dateColumn = new GColumn("datetime", "d", "Date");
    $awakeColumn = new GColumn("number", "a", "Awake");
    $awakeAnColumn = new GColumn("string", "aa", "Status", null, "{\"role\": \"annotation\"}");
    $awakeAnTextColumn = new GColumn("string", "aat", "Status Tooltip", null, "{\"role\": \"annotationText\"}");
    $screenColumn = new GColumn("number", "a", "Screen");
    $screenAnColumn = new GColumn("string", "aa", "Status", null, "{\"role\": \"annotation\"}");
    $screenAnTextColumn = new GColumn("string", "aat", "Status Tooltip", null, "{\"role\": \"annotationText\"}");

    $areaChartData = new GDataTable();
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
        $row = new GRow();
        $row->addCell(new GCell($timestamp));
        // Values
        if (isset($raw[0])) {
            $row->addCell(new GCell((int) $raw[0]->isAwake()));
            $row->addCell(new GCell(null));
            $row->addCell(new GCell(null));
            $lastAwEvent = $raw[0];
        } else {
            $row->addCell(new GCell((int) $lastAwEvent->isAwake()));
            if ($chart->showAnnotations()) {
                $row->addCell(new GCell("i"));
                $row->addCell(new GCell("Value interpolated"));
            } else {
                $row->addCell(new GCell(null));
                $row->addCell(new GCell(null));
            }
        }


        if (isset($raw[1])) {
            $row->addCell(new GCell((int) $raw[1]->isDisplayOn()));
            $lastScrEvent = $raw[1];
        } else {
            $row->addCell(new GCell((int) $lastScrEvent->isDisplayOn()));
            if ($chart->showAnnotations()) {
                $row->addCell(new GCell("i"));
                $row->addCell(new GCell("Value interpolated"));
            } else {
                $row->addCell(new GCell(null));
                $row->addCell(new GCell(null));
            }
        }

        // Annotations
        $areaChartData->addRow($row);
    }

    // Ratios
    // ------
    $statusColumn = new GColumn("string", "c", "Status");
    $countColumn = new GColumn("number", "n", "Count");

    // Awake
    $awakeRatio = new GDataTable();
    $awakeRatio->addColumn($statusColumn);
    $awakeRatio->addColumn($countColumn);

    $awakeRowT = new GRow();
    $awakeRowT->addCell(new GCell("Awake"));
    $awakeRowT->addCell(new GCell($awakes));
    $awakeRowF = new GRow();
    $awakeRowF->addCell(new GCell("Asleep"));
    $awakeRowF->addCell(new GCell(count($awEvents) - $awakes));

    $awakeRatio->addRow($awakeRowT);
    $awakeRatio->addRow($awakeRowF);

    // Screen
    $screenRatio = new GDataTable();
    $screenRatio->addColumn($statusColumn);
    $screenRatio->addColumn($countColumn);

    $screenRowT = new GRow();
    $screenRowT->addCell(new GCell("On"));
    $screenRowT->addCell(new GCell($screens));
    $screenRowF = new GRow();
    $screenRowF->addCell(new GCell("Off"));
    $screenRowF->addCell(new GCell(count($scrEvents) - $screens));

    $screenRatio->addRow($screenRowT);
    $screenRatio->addRow($screenRowF);

    // Awake / Screen ratio
    $awakeScreenRatio = new GDataTable();
    $awakeScreenRatio->addColumn($statusColumn);
    $awakeScreenRatio->addColumn($countColumn);

    $awakeScreenRowA = new GRow();
    $awakeScreenRowA->addCell(new GCell("Awake"));
    $awakeScreenRowA->addCell(new GCell($awakes));
    $awakeScreenRowS = new GRow();
    $awakeScreenRowS->addCell(new GCell("Screen On"));
    $awakeScreenRowO = new GRow();
    $awakeScreenRowO->addCell(new GCell("Device asleep (and screen off)"));
    $off = count($awEvents) + count($scrEvents) - $awakes - $screens;
    $awakeScreenRowO->addCell(new GCell($off));
    $awakeScreenRowS->addCell(new GCell($screens));
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
?>
<?php

Database::getInstance()->disconnect();
?>

