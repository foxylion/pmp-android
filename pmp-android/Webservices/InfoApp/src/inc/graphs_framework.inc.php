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

if (!defined("INCLUDE")) {
    exit;
}

require ("./../inc/classloader.inc.php");

// Connect to database
try {
    Database::getInstance()->connect();
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}

// Get device data
// ---------------
$deviceIdValid = isset($_GET["device"]) && Device::eventsExists($_GET["device"]);
if ($deviceIdValid) {
    $device = Device::getInstance($_GET["device"]);
    $deviceId = $_GET["device"];
} else {
    $device = null;
    $deviceId = 0;
}


// Prepare chart
// -------------
$chart = new Chart();
if (isset($_GET["scale"])) {
    $chart->setScale($_GET["scale"]);
} else {
    $chart->setScale(Chart::DAY);
}


// Prepare callendar
// -----------------
if (isset($_GET["year"]) && is_numeric($_GET["year"])) {
    $year = $_GET["year"];
} else {
    $year = 0;
}

if (isset($_GET["month"]) && is_numeric($_GET["month"])) {
    $month = $_GET["month"];
} else {
    $month = 0;
}

if (isset($_GET["day"]) && is_numeric($_GET["day"])) {
    $day = $_GET["day"];
} else {
    $day = 0;
}

$calendar = new HtmlCalendar($year, $month, $day);
$restOfGetParam = "&day=" . $calendar->getDay() . "&scale=" . $chart->getScale() . "&device=" . $deviceId;

$calendar->urlPrevMonth = "connection.php?year=" . $calendar->getYearOfPrevMonth() .
        "&month=" . $calendar->getPrevMonth() . $restOfGetParam;
$calendar->urlNextMonth = "connection.php?year=" . $calendar->getYearOfNextMonth() .
        "&month=" . $calendar->getNextMonth() . $restOfGetParam;
$calendar->urlPrevYear = "connection.php?year=" . $calendar->getPrevYear() .
        "&month=" . $calendar->getMonth() . $restOfGetParam;
$calendar->urlNextYear = "connection.php?year=" . $calendar->getNextYear() .
        "&month=" . $calendar->getMonth() . $restOfGetParam;
$calendar->urlSelectDay = "connection.php?year=" . $calendar->getYear() .
        "&month=" . $calendar->getMonth() . "&day=%d&scale=" . $chart->getScale() . "&device=" . $deviceId;

$timeMs = Chart::timestampToMillis($calendar->getTimestamp());


// Prepare global template vars
// ----------------------------
$tmplt["dateGetParams"] = "year=" . $calendar->getYear() . "&month=" . $calendar->getMonth() . "&day=" . $calendar->getDay();
$tmplt["scaleGetParam"] = "scale=" . $chart->getScale();
$tmplt["deviceGetParam"] = "device=" . $deviceId;

$tmplt["filename"] = basename($_SERVER["SCRIPT_NAME"], ".php");

// Chart scale
$tmplt["scaleDay"] = $tmplt["scaleMonth"] = $tmplt["scaleYear"] = false;

switch ($chart->getScale()) {
    case Chart::DAY:
        $tmplt["scaleDay"] = true;
        break;
    case Chart::MONTH:
        $tmplt["scaleMonth"] = true;
        break;
    case Chart::YEAR:
        $tmplt["scaleYear"] = true;
        break;
}

// Set remaining temp-vars to default values used to display the error message
$tmplt["pageTitle"] = "No valid device ID";
$tmplt["content"] = "
            <h1>No valid device ID</h1>
            <p>
                No valid device ID has been given. Please enter a valid ID.

                <form method=\"get\" action=\"" . $tmplt["filename"] . ".php\">
                    <input type=\"text\" name=\"device\" size=\"32\" value=\"f2305a2fbef51bd82008c7cf3788250f\" />
                    <input type=\"submit\">
                </form>
            </p>";
?>
