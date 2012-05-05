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

    // Get data used to display the charts
    $citiesBluetoothColumns = array(array("string", "Cities"), array("number", "Paired devices"));
    $citiesWifiColumns = array(array("string", "Cities"), array("number", "Paired devices"));
    $citiesConnectionColumns = array(array("datetime", "Date"), array("number", "Enabled"), array("number", "Connected"), "{type:'string',role:'annotation'}", "{type:'string',role:'annotationText'}");
    $citiesBluetoothRows = array();
    $citiesWifiRows = array();
    $citiesBluetoothConnectionRows = array();
    $citiesWifiConnectionRows = array();

    foreach ($events as $event) {

        $city = $event->getCity();
        switch ($event->getMedium()) {
            case ConnectionEvent::BLUETOOTH:
                // City counter
                if (key_exists($city, $citiesBluetoothRows)) {
                    $citiesBluetoothRows[$city][1]++;
                } else {
                    $citiesBluetoothRows[$city][1] = 1;
                    $citiesBluetoothRows[$city][0] = $city;
                }

                // Connection status
                $citiesBluetoothConnectionRows[] = array("new Date(" . $event->getTimestamp() . ")", (int) $event->isEnabled(), (int) $event->isConnected(), "i", $event->getCity());
                break;

            case ConnectionEvent::WIFI:
                // City counter
                if (key_exists($city, $citiesWifiRows)) {
                    $citiesWifiRows[$city][1]++;
                } else {
                    $citiesWifiRows[$city][1] = 1;
                    $citiesWifiRows[$city][0] = $city;
                }

                // Connection status
                $citiesWifiConnectionRows[] = array("new Date(" . $event->getTimestamp() . ")", (int) $event->isEnabled(), (int) $event->isConnected(), "i", $event->getCity());
                break;
        }
    }

    $tmplt["pageTitle"] = "Connection";
    $tmplt["jsFunctDrawChart"] = "drawBluetoothConnection();
                drawWifiConnection();
                drawBluetoothChart();
                drawWifiChart();";

    $tmplt["jsDrawFunctions"] = "function drawBluetoothConnection() {
        " . Chart::getDataObject($citiesConnectionColumns, $citiesBluetoothConnectionRows) . "

        var options = {
            title: 'Bluetooth adapter status',
            'width':800,
            'height':150
        };


        var chart = new google.visualization.AreaChart(document.getElementById('connectionBluetooth'));
        chart.draw(data, options);
    }

    function drawWifiConnection() {
        " . Chart::getDataObject($citiesConnectionColumns, $citiesWifiConnectionRows) . "

        var options = {
            title: 'Wifi adapter status',
            'width':800,
            'height':150
        };


        var chart = new google.visualization.AreaChart(document.getElementById('connectionWifi'));
        chart.draw(data, options);
    }


    function drawBluetoothChart() {
        " . Chart::getDataObject($citiesBluetoothColumns, $citiesBluetoothRows) . "

        var options = {'title':'Number of paired bluetooth devices',
            'width':600,
            'height':400};

        var chart = new google.visualization.PieChart(document.getElementById('countBluetooth'));
        chart.draw(data, options);
    }

    function drawWifiChart() {
        " . Chart::getDataObject($citiesWifiColumns, $citiesWifiRows) . "

        var options = {'title':'Number of WIFI networks the device has been connected to',
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

