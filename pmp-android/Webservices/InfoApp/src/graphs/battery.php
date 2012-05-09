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
    $eventManager = $device->getBatteryEventManager();
    $events = $chart->getEventsByScale($eventManager, $timeMs, $calendar->getDaysInMonth());

    // Battery level chart
    $dateColumn = new GColumn("datetime", "d", "Date");
    $levelColumn = new GColumn("number", "l", "Level");
    $statusColumn = new GColumn("string", "sa", "Status", null, "{\"role\": \"annotation\"}");
    $statusTextColumn = new GColumn("string", "st", "Status Tooltip", null, "{\"role\": \"annotationText\"}");
    $adapterColumn = new GColumn("string", "aa", "Adapter", null, "{\"role\": \"annotation\"}");
    $adapterTextColumn = new GColumn("string", "at", "Adapter Tooltip", null, "{\"role\": \"annotationText\"}");

    $levelData = new GDataTable();
    $levelData->addColumn($dateColumn);
    $levelData->addColumn($levelColumn);
    $levelData->addColumn($statusColumn);
    $levelData->addColumn($statusTextColumn);
    $levelData->addColumn($adapterColumn);
    $levelData->addColumn($adapterTextColumn);

    // Temperatur chart
    $tempColumn = new GColumn("number", "t", "Temperature");

    $tempData = new GDataTable();
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
        $levelRow = new GRow();
        $levelRow->addCell(new GCell("new Date(" . $event->getTimestamp() . ")"));
        $levelRow->addCell(new GCell($event->getLevel()));

        // Temperature
        $tempRow = new GRow();
        $tempRow->addCell(new GCell("new Date(" . $event->getTimestamp() . ")"));
        $tempRow->addCell(new GCell($event->getTemperature()));


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
                $levelRow->addCell(new GCell($event->getStatus()));
                $levelRow->addCell(new GCell($statusText));
                $tempRow->addCell(new GCell($event->getStatus()));
                $tempRow->addCell(new GCell($statusText));
            }
            $lastStatus = $event->getStatus();
        }

        if ($lastAdapter != $event->getPlugged()) {
            if ($chart->showAnnotations()) {
                $levelRow->addCell(new GCell(($event->getPlugged())));
                $levelRow->addCell(new GCell($adapterText));
                $tempRow->addCell(new GCell(($event->getPlugged())));
                $tempRow->addCell(new GCell($adapterText));
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
    $statusColumn = new GColumn("string", "c", "Status");
    $countColumn = new GColumn("number", "n", "Count");

    $chargingRatioData = new GDataTable();
    $chargingRatioData->addColumn($statusColumn);
    $chargingRatioData->addColumn($countColumn);

    $chargingRatioRowC = new GRow();
    $chargingRatioRowC->addCell(new GCell("Charging"));
    $chargingRatioRowC->addCell(new GCell($chargings));
    $chargingRatioRowD = new GRow();
    $chargingRatioRowD->addCell(new GCell("Discharging"));
    $chargingRatioRowD->addCell(new GCell($dischargings));
    $chargingRatioRowF = new GRow();
    $chargingRatioRowF->addCell(new GCell("Full"));
    $chargingRatioRowF->addCell(new GCell($fulls));
    $chargingRatioRowN = new GRow();
    $chargingRatioRowN->addCell(new GCell("Not charging"));
    $chargingRatioRowN->addCell(new GCell($notChargings));
    $chargingRatioRowU = new GRow();
    $chargingRatioRowU->addCell(new GCell("Unknown"));
    $chargingRatioRowU->addCell(new GCell($unknowns));
    $chargingRatioData->addRow($chargingRatioRowC);
    $chargingRatioData->addRow($chargingRatioRowD);
    $chargingRatioData->addRow($chargingRatioRowF);
    $chargingRatioData->addRow($chargingRatioRowN);
    $chargingRatioData->addRow($chargingRatioRowU);


    // Adapter ratio
    $adapterRatioData = new GDataTable();
    $adapterRatioData->addColumn($statusColumn);
    $adapterRatioData->addColumn($countColumn);

    $adapterRatioRowA = new GRow();
    $adapterRatioRowA->addCell(new GCell("AC-Adapter"));
    $adapterRatioRowA->addCell(new GCell($acPluggeds));
    $adapterRatioRowN = new GRow();
    $adapterRatioRowN->addCell(new GCell("Not plugged"));
    $adapterRatioRowN->addCell(new GCell($notPluggeds));
    $adapterRatioRowU = new GRow();
    $adapterRatioRowU->addCell(new GCell("USB-Adapter"));
    $adapterRatioRowU->addCell(new GCell($usbPluggeds));
    $adapterRatioData->addRow($adapterRatioRowA);
    $adapterRatioData->addRow($adapterRatioRowN);
    $adapterRatioData->addRow($adapterRatioRowU);

    // Battery present chart data
    $presentRationData = new GDataTable();
    $presentRationData->addColumn($statusColumn);
    $presentRationData->addColumn($countColumn);

    $presentRationRowP = new GRow();
    $presentRationRowP->addCell(new GCell("Present"));
    $presentRationRowP->addCell(new GCell($batteryPresents));
    $presentRationRowN = new GRow();
    $presentRationRowN->addCell(new GCell("Not present"));
    $presentRationRowN->addCell(new GCell($batteryNotPresents));
    $presentRationData->addRow($presentRationRowP);
    $presentRationData->addRow($presentRationRowN);


    $tmplt["pageTitle"] = "Connection";
    $tmplt["jsFunctDrawChart"] = "drawLevel();
                drawTemperature();
                drawChargingRatio();
                drawAdapterRatio();
                drawPresentRatio();";

    $tmplt["jsDrawFunctions"] = "function drawLevel() {
        var data = new google.visualization.DataTable(" . $levelData->getJsonObject() . ");

        var options = {
            title: 'Battery level',
            'width':800,
            'height':150
        };


        var chart = new google.visualization.AreaChart(document.getElementById('level'));
        chart.draw(data, options);
    }

    function drawTemperature() {
        var data = new google.visualization.DataTable(" . $tempData->getJsonObject() . ");

        var options = {
            title: 'Battery temperature',
            'width':800,
            'height':150
        };


        var chart = new google.visualization.AreaChart(document.getElementById('temperature'));
        chart.draw(data, options);
    }

    function drawChargingRatio() {
        var data = new google.visualization.DataTable(" . $chargingRatioData->getJsonObject() . ");

        var options = {
            title: 'Charging/Discharging ratio',
            'width':800,
            'height':400
        };


        var chart = new google.visualization.PieChart(document.getElementById('chargingRatio'));
        chart.draw(data, options);
    }

    function drawAdapterRatio() {
        var data = new google.visualization.DataTable(" . $adapterRatioData->getJsonObject() . ");

        var options = {
            title: 'Adapter ratio',
            'width':800,
            'height':400
        };


        var chart = new google.visualization.PieChart(document.getElementById('adapterRatio'));
        chart.draw(data, options);
    }

    function drawPresentRatio() {
        var data = new google.visualization.DataTable(" . $presentRationData->getJsonObject() . ");

        var options = {
            title: 'Present ratio',
            'width':800,
            'height':400
        };


        var chart = new google.visualization.PieChart(document.getElementById('presentRatio'));
        chart.draw(data, options);
    }";

    $tmplt["content"] = "
            <h1>Battery Events</h1>
            <div id=\"level\" style=\"width:800; height:150\"></div>
            <div id=\"temperature\" style=\"width:800; height:150\"></div>
            <div id=\"chargingRatio\" style=\"width:800; height:400\"></div>
            <div id=\"adapterRatio\" style=\"width:800; height:400\"></div>
            <div id=\"presentRatio\" style=\"width:800; height:400\"></div>";
}
include ("template.php");
?>
<?php

Database::getInstance()->disconnect();
?>

