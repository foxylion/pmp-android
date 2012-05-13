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

// Manufacturer distribution
$manufacturerData = new GDataTable();
$manufacturerData->addColumn(new GColumn("string", "m", "Manufacturer"));
$manufacturerData->addColumn($countColumn);

foreach ($stat->getManufacturerDist() as $man => $count) {
    $row = new GRow();
    $row->addCell(new GCell($man));
    $row->addCell(new GCell($count));
    $manufacturerData->addRow($row);
}

// Model and UI distribution
$selectedManufacturer = "Not selected";
$modelData = new GDataTable();
$modelData->addColumn(new GColumn("string", "m", "Model"));
$modelData->addColumn($countColumn);

$uiData = new GDataTable();
$uiData->addColumn(new GColumn("string", "u", "UI"));
$uiData->addColumn($countColumn);

if (isset($_GET["manufacturer"])) {
    $selectedManufacturer = $_GET["manufacturer"];
    $modelDist = $stat->getModelDist();
    $uiDist = $stat->getUiDist();

    if (key_exists($selectedManufacturer, $modelDist)) {
        $rawData = $modelDist[$_GET["manufacturer"]];

        foreach ($rawData as $model => $count) {
            $row = new GRow();
            $row->addCell(new GCell($model));
            $row->addCell(new GCell($count));
            $modelData->addRow($row);
        }
    }

    if (key_exists($selectedManufacturer, $uiDist)) {
        $rawData = $uiDist[$_GET["manufacturer"]];

        foreach ($rawData as $ui => $count) {
            $row = new GRow();
            $row->addCell(new GCell($ui));
            $row->addCell(new GCell($count));
            $uiData->addRow($row);
        }
    }
}

// API distribution
$apiData = new GDataTable();
$apiData->addColumn(new GColumn("string", "a", "API"));
$apiData->addColumn($countColumn);

foreach ($stat->getApiDist() as $api => $count) {
    $row = new GRow();
    $row->addCell(new GCell($api));
    $row->addCell(new GCell($count));
    $apiData->addRow($row);
}

// Kernel distribution
$kernelData = new GDataTable();
$kernelData->addColumn(new GColumn("string", "k", "Kernel"));
$kernelData->addColumn($countColumn);

foreach ($stat->getKernelDist() as $kernel => $count) {
    $row = new GRow();
    $row->addCell(new GCell($kernel));
    $row->addCell(new GCell($count));
    $kernelData->addRow($row);
}




$tmplt["pageTitle"] = "Connection";
$tmplt["jsFunctDrawChart"] = "drawManufacturer();
        drawModel();
        drawUi();
        drawApi();
        drawKernel();";


$tmplt["jsDrawFunctions"] = "
    var manufacturerChart;
    var manufacturerData;
    function drawManufacturer() {
        manufacturerData = new google.visualization.DataTable(" . $manufacturerData->getJsonObject() . ");


        var options = {
            title: 'Manufacturer distribution (Click to select)',
            'width':800,
            'height':400
        };


        manufacturerChart = new google.visualization.PieChart(document.getElementById('manufacturer'));
        google.visualization.events.addListener(manufacturerChart, 'select', selectHandler);
        manufacturerChart.draw(manufacturerData, options);
    }
    function selectHandler(e) {
        if (manufacturerChart.getSelection().length >= 1) {
            var item = manufacturerChart.getSelection()[0];
            var manufacturer = manufacturerData.getFormattedValue(item.row, 0);
            window.location.href=document.URL + '&manufacturer=' + manufacturer;
        }
    }

    function drawModel() {
        var data = new google.visualization.DataTable(" . $modelData->getJsonObject() . ");


        var options = {
            title: 'Model distribution (Manufacturer: " . $selectedManufacturer . ")',
            'width':800,
            'height':400
        };


        var chart = new google.visualization.PieChart(document.getElementById('model'));
        chart.draw(data, options);
    }

    function drawUi() {
        var data = new google.visualization.DataTable(" . $uiData->getJsonObject() . ");


        var options = {
            title: 'UI distribution (Manufacturer: " . $selectedManufacturer . ")',
            'width':800,
            'height':400
        };


        var chart = new google.visualization.PieChart(document.getElementById('ui'));
        chart.draw(data, options);
    }

    function drawApi() {
        var data = new google.visualization.DataTable(" . $apiData->getJsonObject() . ");


        var options = {
            title: 'API distribution',
            'width':800,
            'height':400
        };


        var chart = new google.visualization.PieChart(document.getElementById('api'));
        chart.draw(data, options);
    }

    function drawKernel() {
        var data = new google.visualization.DataTable(" . $kernelData->getJsonObject() . ");


        var options = {
            title: 'Kernel distribution',
            'width':800,
            'height':400
        };


        var chart = new google.visualization.PieChart(document.getElementById('kernel'));
        chart.draw(data, options);
    }";

$tmplt["content"] = "
            <h1>Software Statistics</h1>
            <p>Select a manufacturer to view the model- and UI-chart.</p>
            <div id=\"manufacturer\" style=\"width:800; height:400\"></div>
            <div id=\"model\" style=\"width:800; height:400\"></div>
            <div id=\"ui\" style=\"width:800; height:400\"></div>
            <div id=\"api\" style=\"width:800; height:400\"></div>
            <div id=\"kernel\" style=\"width:800; height:400\"></div>";

include ("template.php");
?>
<?php

Database::getInstance()->disconnect();
?>

