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

    // Event charts
    $dateColumn = new Column("datetime", "d", "Date");
    $incomingColumn = new Column("number", "i", "Incoming");
    $outgoingColumn = new Column("number", "o", "Outgoing");
    $cityColumn = new Column("string", "c", "City", null, "{\"role\": \"tooltip\"}");

    $callData = new DataTable();
    $callData->addColumn($dateColumn);
    $callData->addColumn($incomingColumn);
    $callData->addColumn($cityColumn);
    $callData->addColumn($outgoingColumn);
    $callData->addColumn($cityColumn);

    $smsData = new DataTable();
    $smsData->addColumn($dateColumn);
    $smsData->addColumn($incomingColumn);
    $smsData->addColumn($cityColumn);
    $smsData->addColumn($outgoingColumn);
    $smsData->addColumn($cityColumn);


    // City chart
    $cityColumn = new Column("string", "c", "Cities");
    $countColumn = new Column("number", "e", "Events");

    $callCitiesData = new DataTable();
    $callCitiesData->addColumn($cityColumn);
    $callCitiesData->addColumn($countColumn);

    $smsCitiesData = new DataTable();
    $smsCitiesData->addColumn($cityColumn);
    $smsCitiesData->addColumn($countColumn);

    // Get data used to display the charts
    $citiesCall = array();
    $citiesSms = array();

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
            // City
            if (array_key_exists($city, $citiesCall)) {
                $citiesCall[$city]++;
            } else {
                $citiesCall[$city] = 1;
            }

            // Event
            if ($event->getDirection() == ProfileEvent::INCOMING) {
                $callRow->addCell(new Cell(1));
                $callRow->addCell(new Cell($cityTooltip));
                $callRow->addCell(new Cell("null"));
                $callRow->addCell(new Cell("null"));
            } else {
                $callRow->addCell(new Cell("null"));
                $callRow->addCell(new Cell("null"));
               $callRow->addCell(new Cell(1));
                $callRow->addCell(new Cell($cityTooltip));
            }
            $callData->addRow($callRow);
        }


        if ($event->getEvent() == ProfileEvent::SMS) {
            // City
            if (array_key_exists($city, $citiesSms)) {
                $citiesSms[$city]++;
            } else {
                $citiesSms[$city] = 1;
            }

            // Event
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
            $smsData->addRow($smsRow);
        }
    }

    $callCitiesData->addRowsAssocArray($citiesCall);
    $smsCitiesData->addRowsAssocArray($citiesSms);

    $tmplt["pageTitle"] = "Profile";
    if ($svgCharts) {
        // Draw SVG-Charts
        // ---------------
        $tmplt["jsFunctDrawChart"] = "drawCallChart();
                drawSmsChart();
                drawCallCitiesChart();
                drawSmsCitiesChart();";

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
    }

    function drawCallCitiesChart() {
        var data = new google.visualization.DataTable(" . $callCitiesData->getJsonObject() . ");

        var options = {'title':'Number of calls per city',
            'width':" . $chart->getPieChartWidth() . ",
            'height':" . $chart->getPieChartHeight() . "
        };

        var chart = new google.visualization.PieChart(document.getElementById('callcities'));
        chart.draw(data, options);
    }

    function drawSmsCitiesChart() {
        var data = new google.visualization.DataTable(" . $smsCitiesData->getJsonObject() . ");

        var options = {'title':'Number of SMS messages per city',
            'width':" . $chart->getPieChartWidth() . ",
            'height':" . $chart->getPieChartHeight() . "
        };

        var chart = new google.visualization.PieChart(document.getElementById('smscities'));
        chart.draw(data, options);
    }";
    }

    $tmplt["content"] = "
            <h1>Profile Events</h1>";
    if ($svgCharts) {
        // Draw SVG-Charts
        // ---------------
        $tmplt["content"] .= "
            <div id=\"call\"></div>
            <div id=\"sms\"></div>
            <div id=\"callcities\"></div>
            <div id=\"smscities\"></div>";
    } else {
        // Draw static/PNG-charts
        // ----------------------
        $scale = $chart->getScaleXAxis($calendar);
        $scaleLabel = $chart->getScaleXAxisLabel($calendar);
        $offset = $chart->getOffsetXAxis($calendar);

        $callChart = new gchart\gScatterChart($chart->getAxisChartWidth(), $chart->getAxisChartHeight());
        $callChart->setTitle("Call events");
        $callChart->setVisibleAxes(array('x', 'y'));
        $bridge = new GChartPhpBridge($callData);
        $bridge->pushData($callChart, GChartPhpBridge::IN_TURN, $scale, $scaleLabel, $offset);

        $smsChart = new gchart\gScatterChart($chart->getAxisChartWidth(), $chart->getAxisChartHeight());
        $smsChart->setTitle("SMS events");
        $smsChart->setVisibleAxes(array('x', 'y'));
        $bridge = new GChartPhpBridge($smsData);
        $bridge->pushData($smsChart, GChartPhpBridge::IN_TURN, $scale);

        $callCitiesChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
        $callCitiesChart->setTitle("Number of calls per city");
        $bridge = new GChartPhpBridge($callCitiesData);
        $bridge->pushData($callCitiesChart, GChartPhpBridge::LEGEND);

        $smsCitiesChart = new gchart\gPieChart($chart->getPieChartWidth() - 100, $chart->getPieChartHeight() - 100);
        $smsCitiesChart->setTitle("Number of SMS messages per city");
        $bridge = new GChartPhpBridge($smsCitiesData);
        $bridge->pushData($smsCitiesChart, GChartPhpBridge::LEGEND);

        $tmplt["content"] .= "
            <p><img src=\"" . $callChart->getUrl() . "\" alt=\"Cannot display chart as there is to much data. Please reduce the scale or use the interactive charts.\" /></p>
            <p><img src=\"" . $smsChart->getUrl() . "\" /></p>
            <p><img src=\"" . $callCitiesChart->getUrl() . "\" /></p>
            <p><img src=\"" . $smsCitiesChart->getUrl() . "\" /></p>";
    }
}
include ("template.php");

Database::getInstance()->disconnect();
?>

