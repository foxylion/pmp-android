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
$citiesBluetooth = array();
$citiesWifi = array();
$connectionBluetoothRows = array();
foreach ($events as $event) {
    // City counter
    $city = $event->getCity();
    switch ($event->getMedium()) {
        case ConnectionEvent::BLUETOOTH:
            if (key_exists($city, $citiesBluetooth)) {
                $citiesBluetooth[$city]++;
            } else {
                $citiesBluetooth[$city] = 1;
            }
            break;

        case ConnectionEvent::WIFI:
            if (key_exists($city, $citiesWifi)) {
                $citiesWifi[$city]++;
            } else {
                $citiesWifi[$city] = 1;
            }
            break;
    }

    // Connection status
    $timeString = Chart::timeMillisToString("Y-m-d, H:i:s", $event->getTimestamp());
    $status = array(array("v" => $timeString), array("v" => (int)$event->isEnabled()),
    array("v" => (int)$event->isConnected()));
    $connectionBluetoothRows[] = array("c" => $status);
}

$connectionCols = array(array("id" => "time", "label" => "Date", "type" => "string"),
    array("id" => "enabled", "label" => "Enabled", "type" => "number"),
    array("id" => "connected", "label" => "Connected", "type" => "number"));



$connectionBluetooth = array("cols" => $connectionCols, "rows" => $connectionBluetoothRows);
?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Connection</title>
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
                drawBluetoothChart();
                drawWifiChart();
            }

            function drawBluetoothConnection() {
                var data = new google.visualization.DataTable(<?php echo Json::arrayToJson($connectionBluetooth) ?>);

                var options = {
                    title: 'Bluetooth adapter status',
                    'width':800,
                    'height':450
                };


                var chart = new google.visualization.AreaChart(document.getElementById('connectionBluetooth'));
                chart.draw(data, options);

            }

            function drawBluetoothChart() {

                // Create the data table.
                var data = new google.visualization.DataTable();
                data.addColumn('string', 'Cities');
                data.addColumn('number', 'Paired devices');
                data.addRows(<?php echo Chart::getJsDataObject($citiesBluetooth) ?>);

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
                var data = new google.visualization.DataTable();
                data.addColumn('string', 'Cities');
                data.addColumn('number', 'WIFI connections');
                data.addRows(<?php echo Chart::getJsDataObject($citiesWifi) ?>);

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
        <div id="countBluetooth" style="width:800; height:450"></div>
        <div id="countWifi" style="width:800; height:450"></div>
    </body>
</html>
<?php
Database::getInstance()->disconnect();
?>

