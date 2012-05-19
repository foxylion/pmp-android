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
use infoapp\Device;
use infoapp\Json;
use infoapp\exceptions\DatabaseException;

define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

try {
    $device = Device::getInstance($_POST["device"]);
    $prop = $device->getDeviceProperties();

    $prop->setManufacturer($_POST["manufacturer"]);
    $prop->setApiLevel((int) $_POST["apiLevel"]);
    $prop->setKernelVersion($_POST["kernel"]);
    $prop->setModel($_POST["model"]);
    $prop->setUi($_POST["ui"]);
    $prop->setDisplayResolution((int) $_POST["displayResX"], (int) $_POST["displayResY"]);
    $prop->setCpuFrequency((int) $_POST["cpuFrequency"]);
    $prop->setInternalMemory((int) $_POST["memoryInternal"]);
    $prop->setInternalMemoryFree((int) $_POST["memoryInternalFree"]);
    $prop->setExternalMemory((int) $_POST["memoryExternal"]);
    $prop->setExternalMemoryFree((int) $_POST["memoryExternalFree"]);
    $prop->setCameraResolution((float) $_POST["cameraResolution"]);

    $sensors = explode(",", $_POST["sensors"]);
    foreach ($sensors as $index => $sensor) {
        $sensors[$index] = trim($sensor);
    }
    $prop->setSensors($sensors);

    $prop->setRuntime((float) $_POST["runtime"]);

    $prop->writeBack();

    Json::printAsJson(array('successful' => true));
} catch (InvalidArgumentException $iae) {
    Json::printInvalidParameterError($iae);
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}

Database::getInstance()->disconnect();
?>
