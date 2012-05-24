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
use infoapp\events\BatteryEvent;
use infoapp\googlecharttools\Cell;
use infoapp\googlecharttools\Column;
use infoapp\googlecharttools\DataTable;
use infoapp\googlecharttools\GChartPhpBridge;
use infoapp\googlecharttools\Row;

define("INCLUDE", true);
require("./../inc/graphs_framework.inc.php");

if ($deviceIdValid) {
    $eventManager = $device->getBatteryEventManager();
    $events = $chart->getEventsByScale($eventManager, $timeMs, $calendar->getDaysInMonth());

    // Battery level chart
    $dateColumn = new Column("datetime", "d", "Date");
    $levelColumn = new Column("number", "l", "Level");
    $statusColumn = new Column("string", "sa", "Status", null, "{\"role\": \"annotation\"}");
    $statusTextColumn = new Column("string", "st", "Status Tooltip", null, "{\"role\": \"annotationText\"}");
    $adapterColumn = new Column("string", "aa", "Adapter", null, "{\"role\": \"annotation\"}");
    $adapterTextColumn = new Column("string", "at", "Adapter Tooltip", null, "{\"role\": \"annotationText\"}");

    $levelData = new DataTable();
    $levelData->addColumn($dateColumn);
    $levelData->addColumn($levelColumn);
    $levelData->addColumn($statusColumn);
    $levelData->addColumn($statusTextColumn);
    $levelData->addColumn($adapterColumn);
    $levelData->addColumn($adapterTextColumn);

    // Temperatur chart
    $tempColumn = new Column("number", "t", "Temperature");

    $tempData = new DataTable();
    $tempData->addColumn($dateColumn);
    $tempData->addColumn($tempColumn);
    $tempData->addColumn($statusColumn);
    $tempData->addColumn($statusTextColumn);
    $tempData->addColumn($adapterColumn);
    $tempData->addColumn($adapterTextColumn);

    // Counters used to generate the pie charts
    $chargings = 0;
    $dischargings = 0;
    $fulls = 0;
    $notChargings = 0;
    $unknowns = 0;

    $acPluggeds = 0;
    $notPluggeds = 0;
    $usbPluggeds = 0;

    $batteryPresents = 0;
    $batteryNotPresents = 0;

    // Save last status/adapter, so that the status/adapter is only annotated when it's changed
    $lastStatus = "";
    $lastAdapter = "";

    // Build line/area chart data and fill counters
    foreach ($events as $event) {
        // Level and status
        $levelRow = new Row();
        //$levelRow->addCell(new Cell("new Date(" . $event->getTimestamp() . ")"));
        $levelRow->addCell(new Cell($event->getTimestamp()));
        $levelRow->addCell(new Cell($event->getLevel()));

        // Temperature
        $tempRow = new Row();
        //$tempRow->addCell(new Cell("new Date(" . $event->getTimestamp() . ")"));
        $tempRow->addCell(new Cell($event->getTimestamp()));
        $tempRow->addCell(new Cell($event->getTemperature()));


        // Status
        switch ($event->getStatus()) {
            case BatteryEvent::CHARGING:
                $statusText = "Charging";
                $chargings++;
                break;
            case BatteryEvent::DISCHARGING:
                $statusText = "Discharging";
                $dischargings++;
                break;
            case BatteryEvent::FULL:
                $statusText = "Full";
                $fulls++;
                break;
            case BatteryEvent::NOT_CHARGING:
                $statusText = "Not charging";
                $notChargings++;
                break;
            case BatteryEvent::UNKNOWN:
                $statusText = "Unknown";
                $unknowns++;
                break;
        }

        // Adapter
        switch ($event->getPlugged()) {
            case BatteryEvent::AC_ADAPTER:
                $adapterText = "AC-Adapter";
                $acPluggeds++;
                break;
            case BatteryEvent::ADAPTER_NOT_PLUGGED:
                $adapterText = "Not plugged";
                $notPluggeds++;
                break;
            case BatteryEvent::USB_ADAPTER:
                $adapterText = "USB-Adapter";
                $usbPluggeds++;
                break;
        }

        if ($lastStatus != $event->getStatus()) {
            if ($chart->showAnnotations()) {
                $levelRow->addCell(new Cell($event->getStatus()));
                $levelRow->addCell(new Cell($statusText));
                $tempRow->addCell(new Cell($event->getStatus()));
                $tempRow->addCell(new Cell($statusText));
            }
            $lastStatus = $event->getStatus();
        }

        if ($lastAdapter != $event->getPlugged()) {
            if ($chart->showAnnotations()) {
                $levelRow->addCell(new Cell(($event->getPlugged())));
                $levelRow->addCell(new Cell($adapterText));
                $tempRow->addCell(new Cell(($event->getPlugged())));
                $tempRow->addCell(new Cell($adapterText));
            }
            $lastAdapter = $event->getPlugged();
        }

        $levelData->addRow($levelRow);
        $tempData->addRow($tempRow);

        // Battery present
        if ($event->isPresent()) {
            $batteryPresents++;
        } else {
            $batteryNotPresents++;
        }
    }


    // Chart/discharge ratio
    $statusColumn = new Column("string", "c", "Status");
    $countColumn = new Column("number", "n", "Count");

    $chargingRatioData = new DataTable();
    $chargingRatioData->addColumn($statusColumn);
    $chargingRatioData->addColumn($countColumn);

    $chargingRatioRowC = new Row();
    $chargingRatioRowC->addCell(new Cell("Charging"));
    $chargingRatioRowC->addCell(new Cell($chargings));
    $chargingRatioRowD = new Row();
    $chargingRatioRowD->addCell(new Cell("Discharging"));
    $chargingRatioRowD->addCell(new Cell($dischargings));
    $chargingRatioRowF = new Row();
    $chargingRatioRowF->addCell(new Cell("Full"));
    $chargingRatioRowF->addCell(new Cell($fulls));
    $chargingRatioRowN = new Row();
    $chargingRatioRowN->addCell(new Cell("Not charging"));
    $chargingRatioRowN->addCell(new Cell($notChargings));
    $chargingRatioRowU = new Row();
    $chargingRatioRowU->addCell(new Cell("Unknown"));
    $chargingRatioRowU->addCell(new Cell($unknowns));
    $chargingRatioData->addRow($chargingRatioRowC);
    $chargingRatioData->addRow($chargingRatioRowD);
    $chargingRatioData->addRow($chargingRatioRowF);
    $chargingRatioData->addRow($chargingRatioRowN);
    $chargingRatioData->addRow($chargingRatioRowU);


    // Adapter ratio
    $adapterRatioData = new DataTable();
    $adapterRatioData->addColumn($statusColumn);
    $adapterRatioData->addColumn($countColumn);

    $adapterRatioRowA = new Row();
    $adapterRatioRowA->addCell(new Cell("AC-Adapter"));
    $adapterRatioRowA->addCell(new Cell($acPluggeds));
    $adapterRatioRowN = new Row();
    $adapterRatioRowN->addCell(new Cell("Not plugged"));
    $adapterRatioRowN->addCell(new Cell($notPluggeds));
    $adapterRatioRowU = new Row();
    $adapterRatioRowU->addCell(new Cell("USB-Adapter"));
    $adapterRatioRowU->addCell(new Cell($usbPluggeds));
    $adapterRatioData->addRow($adapterRatioRowA);
    $adapterRatioData->addRow($adapterRatioRowN);
    $adapterRatioData->addRow($adapterRatioRowU);

    // Battery present chart data
    $presentRatioData = new DataTable();
    $presentRatioData->addColumn($statusColumn);
    $presentRatioData->addColumn($countColumn);

    $presentRationRowP = new Row();
    $presentRationRowP->addCell(new Cell("Present"));
    $presentRationRowP->addCell(new Cell($batteryPresents));
    $presentRationRowN = new Row();
    $presentRationRowN->addCell(new Cell("Not present"));
    $presentRationRowN->addCell(new Cell($batteryNotPresents));
    $presentRatioData->addRow($presentRationRowP);
    $presentRatioData->addRow($presentRationRowN);


    $tmplt["pageTitle"] = "Battery";
    if ($svgCharts) {
        // Draw SVG-Charts
        // ---------------
        $tmplt["jsFunctDrawChart"] = "drawLevel();
                drawTemperature();
                drawChargingRatio();
                drawAdapterRatio();
                drawPresentRatio();";

        $tmplt["jsDrawFunctions"] = "function drawLevel() {
        var data = new google.visualization.DataTable(" . $levelData->getJsonObject() . ");

        var options = {
            title: 'Battery level',
            'width':" . $chart->getAxisChartWidth() . ",
            'height':" . $chart->getAxisChartHeight() . "
        };


        var chart = new google.visualization.AreaChart(document.getElementById('level'));
        chart.draw(data, options);
    }

    function drawTemperature() {
        var data = new google.visualization.DataTable(" . $tempData->getJsonObject() . ");

        var options = {
            title: 'Battery temperature',
            'width':" . $chart->getAxisChartWidth() . ",
            'height':" . $chart->getAxisChartHeight() . "
        };


        var chart = new google.visualization.AreaChart(document.getElementById('temperature'));
        chart.draw(data, options);
    }

    function drawChargingRatio() {
        var data = new google.visualization.DataTable(" . $chargingRatioData->getJsonObject() . ");

        var options = {
            title: 'Charging/Discharging ratio',
            'width':" . $chart->getPieChartWidth() . ",
            'height':" . $chart->getPieChartHeight() . "
        };


        var chart = new google.visualization.PieChart(document.getElementById('chargingRatio'));
        chart.draw(data, options);
    }

    function drawAdapterRatio() {
        var data = new google.visualization.DataTable(" . $adapterRatioData->getJsonObject() . ");

        var options = {
            title: 'Adapter ratio',
            'width':" . $chart->getPieChartWidth() . ",
            'height':" . $chart->getPieChartHeight() . "
        };


        var chart = new google.visualization.PieChart(document.getElementById('adapterRatio'));
        chart.draw(data, options);
    }

    function drawPresentRatio() {
        var data = new google.visualization.DataTable(" . $presentRatioData->getJsonObject() . ");

        var options = {
            title: 'Present ratio',
            'width':" . $chart->getPieChartWidth() . ",
            'height':" . $chart->getPieChartHeight() . "
        };


        var chart = new google.visualization.PieChart(document.getElementById('presentRatio'));
        chart.draw(data, options);
    }";
    }

    $tmplt["content"] = "
            <h1>Battery Events</h1>";
    if ($svgCharts) {
        // Draw SVG-Charts
        // ---------------
        $tmplt["content"] .= "
            <div id=\"level\"></div>
            <div id=\"temperature\"></div>
            <div id=\"chargingRatio\"></div>
            <div id=\"adapterRatio\"></div>
            <div id=\"presentRatio\"></div>";
    } else {
        // Draw static/PNG-charts
        // ----------------------
        $scale = $chart->getScaleXAxis($calendar);
        $scaleLabel = $chart->getScaleXAxisLabel($calendar);
        $offset = $chart->getOffsetXAxis($calendar);

        $levelChart = new gchart\gLineChart($chart->getAxisChartWidth(), $chart->getAxisChartHeight());
        $levelChart->setTitle("Battery level");
        $levelChart->setProperty("cht", "lxy");
        $levelChart->setVisibleAxes(array('x', 'y'));
        $bridge = new GChartPhpBridge($levelData);
        $bridge->pushData($levelChart, GChartPhpBridge::Y_COORDS, $scale, $scaleLabel, $offset);

        $tempChart = new gchart\gLineChart($chart->getAxisChartWidth(), $chart->getAxisChartHeight());
        $tempChart->setTitle("Battery temperature");
        $tempChart->setProperty("cht", "lxy");
        $tempChart->setVisibleAxes(array('x', 'y'));
        $bridge = new GChartPhpBridge($tempData);
        $bridge->pushData($tempChart, GChartPhpBridge::Y_COORDS, $scale, $scaleLabel, $offset);

        $chargingRatioChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
        $chargingRatioChart->setTitle("Charging/Discharging ratio");
        $bridge = new GChartPhpBridge($chargingRatioData);
        $bridge->pushData($chargingRatioChart, GChartPhpBridge::LEGEND);

        $adapterRatioChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
        $adapterRatioChart->setTitle("Charging/Discharging ratio");
        $bridge = new GChartPhpBridge($adapterRatioData);
        $bridge->pushData($adapterRatioChart, GChartPhpBridge::LEGEND);

        $presentRatioChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
        $presentRatioChart->setTitle("Charging/Discharging ratio");
        $bridge = new GChartPhpBridge($presentRatioData);
        $bridge->pushData($presentRatioChart, GChartPhpBridge::LEGEND);
        $tmplt["content"] .= "
            <p><img src=\"" . $levelChart->getUrl() . "\" alt=\"Cannot display chart as there is to much data. Please reduce the scale or use the interactive charts.\" /></p>
            <p><img src=\"" . $tempChart->getUrl() . "\" alt=\"Cannot display chart as there is to much data. Please reduce the scale or use the interactive charts.\" /></p>
            <p><img src=\"" . $chargingRatioChart->getUrl() . "\" /></p>
            <p><img src=\"" . $adapterRatioChart->getUrl() . "\" /></p>
            <p><img src=\"" . $presentRatioChart->getUrl() . "\" /></p>";
    }
}
include ("template.php");

Database::getInstance()->disconnect();
?>

