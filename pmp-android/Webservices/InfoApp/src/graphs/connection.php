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
require("./../inc/framework.inc.php");

$device = Device::getInstance("f2305a2fbef51bd82008c7cf3788250f");
// TODO: device per get und zeitspanne
$events = $device->getConnectionEventManager()->getEventsOneDay(1335909600312);

// Count connections per city
$citiesBluetoothColumns = array(array("string", "Cities"),array("number", "Paired devices"));
$citiesWifiColumns = array(array("string", "Cities"),array("number", "Paired devices"));
$citiesConnectionColumns = array(array("datetime","Date"),array("number","Enabled"),array("number","Connected"),"{type:'string',role:'annotation'}","{type:'string',role:'annotationText'}");
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
            $citiesBluetoothConnectionRows[] = array("new Date(".$event->getTimestamp().")", (int)$event->isEnabled(),(int)$event->isConnected(), "i", $event->getCity());
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
            $citiesWifiConnectionRows[] = array("new Date(".$event->getTimestamp().")", (int)$event->isEnabled(),(int)$event->isConnected(), "i", $event->getCity());
            break;

    }

}

?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Connection</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <!--Load the AJAX API-->
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript">
            // Load the Visualization API library and the piechart library.

            // Set a callback to run when the Google Visualization API is loaded.
            google.setOnLoadCallback(drawChart);

            google.load('visualization', '1.0', {'packages':['corechart']});

            // Callback that creates and populates a data table,
            // instantiates the pie chart, passes in the data and
            // draws it.
            function drawChart() {
                drawBluetoothConnection();
                drawWifiConnection();
                drawBluetoothChart();
                drawWifiChart();
            }

            function drawBluetoothConnection() {
                <?php
                echo Chart::getDataObject($citiesConnectionColumns, $citiesBluetoothConnectionRows);
                ?>
                var options = {
                    title: 'Bluetooth adapter status',
                    'width':800,
                    'height':450
                };


                var chart = new google.visualization.AreaChart(document.getElementById('connectionBluetooth'));
                chart.draw(data, options);
            }

            function drawWifiConnection() {
                <?php
                echo Chart::getDataObject($citiesConnectionColumns, $citiesWifiConnectionRows);
                ?>
                var options = {
                    title: 'Wifi adapter status',
                    'width':800,
                    'height':450
                };


                var chart = new google.visualization.AreaChart(document.getElementById('connectionWifi'));
                chart.draw(data, options);
            }


            function drawBluetoothChart() {

                // Create the data table.
                <?php
                echo Chart::getDataObject($citiesBluetoothColumns, $citiesBluetoothRows);
                ?>

                // Set chart options
                var options = {'title':'Number of paired bluetooth devices',
                    'width':800,
                    'height':450};

                // Instantiate and draw our chart, passing in some options.
                var chart = new google.visualization.PieChart(document.getElementById('countBluetooth'));
                chart.draw(data, options);
            }

            function drawWifiChart() {

                // Create the data table.
                <?php
                echo Chart::getDataObject($citiesWifiColumns, $citiesWifiRows);
                ?>

                // Set chart options
                var options = {'title':'Number of WIFI networks the device has been connected to',
                    'width':800,
                    'height':450};

                // Instantiate and draw our chart, passing in some options.
                var chart = new google.visualization.PieChart(document.getElementById('countWifi'));
                chart.draw(data, options);
            }
        </script>
    </head>
    <body>
        <h1>Connection Events</h1>
        <div id="connectionBluetooth" style="width:800; height:450"></div>
        <div id="connectionWifi" style="width:800; height:450"></div>
        <div id="countBluetooth" style="width:800; height:450"></div>
        <div id="countWifi" style="width:800; height:450"></div>
    </body>
</html>
<?php
Database::getInstance()->disconnect();
?>

