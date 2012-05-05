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
    $eventManager = $device->getConnectionEventManager();
    $events = $chart->getEventsByScale($eventManager, $timeMs, $calendar->getDaysInMonth());

    // Bluetooth and wifi adapter chart
    $dateColumn = new GColumn("datetime", "d", "Date");
    $enabledColumn = new GColumn("number", "e", "Enabled");
    $connectedColumn = new GColumn("number", "c", "Connected");
    $cityIColumn = new GColumn("string", "i", "City Tooltip", null, "{\"role\": \"annotation\"}");
    $cityNameColumn = new GColumn("string", "i", "City Name", null, "{\"role\": \"annotationText\"}");

    $bluetoothAdapterData = new GDataTable();
    $bluetoothAdapterData->addColumn($dateColumn);
    $bluetoothAdapterData->addColumn($enabledColumn);
    $bluetoothAdapterData->addColumn($connectedColumn);
    $bluetoothAdapterData->addColumn($cityIColumn);
    $bluetoothAdapterData->addColumn($cityNameColumn);

    $wifiAdapterData = new GDataTable();
    $wifiAdapterData->addColumn($dateColumn);
    $wifiAdapterData->addColumn($enabledColumn);
    $wifiAdapterData->addColumn($connectedColumn);
    $wifiAdapterData->addColumn($cityIColumn);
    $wifiAdapterData->addColumn($cityNameColumn);

    // City chart
    $cityColumn = new GColumn("string", "c", "Cities");
    $countColumn = new GColumn("number", "n", "Connections");

    $bluetoothCitiesData = new GDataTable();
    $bluetoothCitiesData->addColumn($cityColumn);
    $bluetoothCitiesData->addColumn($countColumn);

    $wifiCitiesData = new GDataTable();
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
                $row = new GRow();
                $row->addCell(new GCell("new Date(" .  $event->getTimestamp() . ")"));
                $row->addCell(new GCell((int) $event->isEnabled()));
                $row->addCell(new GCell((int) $event->isConnected()));
                $row->addCell(new GCell("i"));
                $row->addCell(new GCell($event->getCity()));

                $bluetoothAdapterData->addRow($row);

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
                $row = new GRow();
                $row->addCell(new GCell("new Date(" .  $event->getTimestamp() . ")"));
                $row->addCell(new GCell((int) $event->isEnabled()));
                $row->addCell(new GCell((int) $event->isConnected()));
                $row->addCell(new GCell("i"));
                $row->addCell(new GCell($event->getCity()));

                $wifiAdapterData->addRow($row);
                break;
        }
    }

    $bluetoothCitiesData->addRowsAssocArray($citiesBluetooth);
    $wifiCitiesData->addRowsAssocArray($citiesWifi);

    $tmplt["pageTitle"] = "Connection";
    $tmplt["jsFunctDrawChart"] = "drawBluetoothConnection();
                drawWifiConnection();
                drawBluetoothChart();
                drawWifiChart();";

    $tmplt["jsDrawFunctions"] = "function drawBluetoothConnection() {
        var data = new google.visualization.DataTable(" . $bluetoothAdapterData->getJsonObject()  . ");

        var options = {
            title: 'Bluetooth adapter status',
            'width':800,
            'height':150
        };


        var chart = new google.visualization.AreaChart(document.getElementById('connectionBluetooth'));
        chart.draw(data, options);
    }

    function drawWifiConnection() {
        var data = new google.visualization.DataTable(" . $wifiAdapterData->getJsonObject()  . ");

        var options = {
            title: 'Wi-Fi adapter status',
            'width':800,
            'height':150
        };


        var chart = new google.visualization.AreaChart(document.getElementById('connectionWifi'));
        chart.draw(data, options);
    }


    function drawBluetoothChart() {
        var data = new google.visualization.DataTable(" . $bluetoothCitiesData->getJsonObject()  . ");

        var options = {'title':'Number of bluetooth parings per city',
            'width':600,
            'height':400};

        var chart = new google.visualization.PieChart(document.getElementById('countBluetooth'));
        chart.draw(data, options);
    }

    function drawWifiChart() {
        var data = new google.visualization.DataTable(" . $wifiCitiesData->getJsonObject()  . ");

        var options = {'title':'Number of Wi-Fi connections per city',
            'width':600,
            'height':400};

        var chart = new google.visualization.PieChart(document.getElementById('countWifi'));
        chart.draw(data, options)
    }";

    $tmplt["content"] = "
            <h1>Connection Events</h1>
            <div id=\"connectionBluetooth\" style=\"width:800; height:150\"></div>
            <div id=\"connectionWifi\" style=\"width:800; height:150\"></div>
            <div id=\"countBluetooth\" style=\"width:600; height:400\"></div>
            <div id=\"countWifi\" style=\"width:600; height:400\"></div>";
}
include ("template.php");
?>
<?php

Database::getInstance()->disconnect();
?>

