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

$stat = DeviceProperties::getStatistic();
$countColumn = new GColumn("number", "c", "Count");

// Display distribution
$displayData = new GDataTable();
$displayData->addColumn(new GColumn("string", "r", "Resolution"));
$displayData->addColumn($countColumn);

foreach ($stat->getDisplayDist() as $res) {
    $row = new GRow();
    $row->addCell(new GCell($res["x"] . "x" . $res["y"]));
    $row->addCell(new GCell($res["count"]));
    $displayData->addRow($row);
}

// Average free space ratio
$intMemoryData = new GDataTable();
$intMemoryData->addColumn(new GColumn("string", "i", "Internal Memory"));
$intMemoryData->addColumn(new GColumn("number", "a", "Space"));

$intMemoryRowF = new GRow();
$intMemoryRowF->addCell(new GCell("Free (MB)"));
$intMemoryRowF->addCell(new GCell($stat->getInternalMemoryFreeAvg()));
$intMemoryData->addRow($intMemoryRowF);
$intMemoryRowU = new GRow();
$used = $stat->getInternalMemoryAvg() - $stat->getInternalMemoryFreeAvg();
$intMemoryRowU->addCell(new GCell("Used (MB)"));
$intMemoryRowU->addCell(new GCell($used));
$intMemoryData->addRow($intMemoryRowU);


$extMemoryData = new GDataTable();
$extMemoryData->addColumn(new GColumn("string", "e", "External Memory"));
$extMemoryData->addColumn(new GColumn("number", "a", "Space"));

$extMemoryRowF = new GRow();
$extMemoryRowF->addCell(new GCell("Free (MB)"));
$extMemoryRowF->addCell(new GCell($stat->getExternalMemoryFreeAvg()));
$extMemoryData->addRow($extMemoryRowF);
$extMemoryRowU = new GRow();
$used = $stat->getExternalMemoryAvg() - $stat->getExternalMemoryFreeAvg();
$extMemoryRowU->addCell(new GCell("Used (MB)"));
$extMemoryRowU->addCell(new GCell($used));
$extMemoryData->addRow($extMemoryRowU);

// Sensor distribution
$sensorData = new GDataTable();
$sensorData->addColumn(new GColumn("string", "s", "Sensor"));
$sensorData->addColumn($countColumn);

foreach ($stat->getSensorDist() as $sensor => $count) {
    $row = new GRow();
    $row->addCell(new GCell($sensor));
    $row->addCell(new GCell($count));
    $sensorData->addRow($row);
}

$tmplt["pageTitle"] = "Connection";
$tmplt["jsFunctDrawChart"] = "drawDisplay();
        drawIntMemoryRatio();
        drawExtMemoryRatio();
        drawSensors();";


$tmplt["jsDrawFunctions"] = "
    function drawDisplay() {
        var data = new google.visualization.DataTable(" . $displayData->getJsonObject() . ");


        var options = {
            title: 'Display distribution',
            'width':800,
            'height':400
        };


        var chart = new google.visualization.PieChart(document.getElementById('display'));
        chart.draw(data, options);
    }

    function drawIntMemoryRatio() {
        var data = new google.visualization.DataTable(" . $intMemoryData->getJsonObject() . ");

        var options = {
            title: 'Average internal memory usage (Avr memory size: " . $stat->getInternalMemoryAvg() . " MB)',
            'width':800,
            'height':400
        };


        var chart = new google.visualization.PieChart(document.getElementById('intmemoryratio'));
        chart.draw(data, options);
    }

    function drawExtMemoryRatio() {
        var data = new google.visualization.DataTable(" . $extMemoryData->getJsonObject() . ");

        var options = {
            title: 'Average external memory usage (Avr memory size: " . $stat->getExternalMemoryAvg() . " MB)',
            'width':800,
            'height':400
        };


        var chart = new google.visualization.PieChart(document.getElementById('extmemoryratio'));
        chart.draw(data, options);
    }

    function drawSensors() {
        var data = new google.visualization.DataTable(" . $sensorData->getJsonObject() . ");

        var options = {
            title: 'Sensor distribution',
            'width':700,
            'height':300,
            hAxis: {title: 'Sensors'}
        };


        var chart = new google.visualization.ColumnChart(document.getElementById('sensors'));
        chart.draw(data, options);
    }
";

$tmplt["content"] = "
            <h1>Hardware Statistics</h1>
            <div id=\"display\" style=\"width:800; height:400\"></div>
            <div id=\"intmemoryratio\" style=\"width:800; height:400\"></div>
            <div id=\"extmemoryratio\" style=\"width:800; height:400\"></div>
            <div id=\"sensors\" style=\"width:700; height:300\"></div>
            <p><b>Average CPU frequency:</b> " . $stat->getCpuAvg() . " MHz</p>
            <p><b>Average camera resolution:</b> " . $stat->getCameraAvg() . " MP</p>
            <p><b>Average runtime:</b> " . $stat->getRuntimeAvg() . " Min</p>";

include ("template.php");
?>
<?php

Database::getInstance()->disconnect();
?>

