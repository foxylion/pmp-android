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

use infoapp\Chart;
use infoapp\Database;
use infoapp\Device;
use infoapp\HtmlCalendar;

if (!defined("INCLUDE")) {
    exit;
}

// Load config file and classloader
require_once ("./../inc/config.inc.php");
require_once ("./../inc/class/ClassLoader.class.php");

InfoApp\ClassLoader::register();


// Connect to database
Database::getInstance()->connect();

$filename = basename($_SERVER["SCRIPT_NAME"], ".php");

// Decide if static charts should be used
$svgCharts = !(isset($_GET["view"]) && $_GET["view"] == "static");
if (!$svgCharts) {
    require ("./../inc/class/gchart/gChartInit.php");
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

if (isset($_GET["annotations"]) && $_GET["annotations"] == "hide") {
    $chart->setShowAnntotations(false);
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


if ($chart->showAnnotations()) {
    $annotationParam = "show";
} else {
    $annotationParam = "hide";
}
$restOfGetParam = "&day=" . $calendar->getDay() . "&scale=" . $chart->getScale() . "&annotations=" . $annotationParam . "&device=" . $deviceId;
if (!$svgCharts) {
    $restOfGetParam .= "&view=static";
}

$calendar->urlPrevMonth = $filename . ".php?year=" . $calendar->getYearOfPrevMonth() .
        "&month=" . $calendar->getPrevMonth() . $restOfGetParam;
$calendar->urlNextMonth = $filename . ".php?year=" . $calendar->getYearOfNextMonth() .
        "&month=" . $calendar->getNextMonth() . $restOfGetParam;
$calendar->urlPrevYear = $filename . ".php?year=" . $calendar->getPrevYear() .
        "&month=" . $calendar->getMonth() . $restOfGetParam;
$calendar->urlNextYear = $filename . ".php?year=" . $calendar->getNextYear() .
        "&month=" . $calendar->getMonth() . $restOfGetParam;
$calendar->urlSelectDay = $filename . ".php?year=" . $calendar->getYear() .
        "&month=" . $calendar->getMonth() . "&day=%d&scale=" . $chart->getScale() . "&annotations=" . $annotationParam . "&device=" . $deviceId;
if (!$svgCharts) {
    $calendar->urlSelectDay .= "&view=static";
}

$timeMs = Chart::timestampToMillis($calendar->getTimestamp());


// Prepare global template vars
// ----------------------------
$tmplt["hideNavigation"] = (isset($_COOKIE["navigation"]) && $_COOKIE["navigation"] == "hide");
$tmplt["dateGetParams"] = "year=" . $calendar->getYear() . "&month=" . $calendar->getMonth() . "&day=" . $calendar->getDay();
$tmplt["scaleGetParam"] = "scale=" . $chart->getScale();
$tmplt["deviceGetParam"] = "device=" . $deviceId;
if ($svgCharts) {
    $tmplt["viewGetParam"] = "view=dynamic";
    $tmplt["svgView"] = true;
} else {
    $tmplt["viewGetParam"] = "view=static";
    $tmplt["svgView"] = false;
}
if ($chart->showAnnotations()) {
    $tmplt["annotationGetParam"] = "annotations=show";
} else {
    $tmplt["annotationGetParam"] = "annotations=hide";
}

$tmplt["filename"] = $filename;

// Chart scale
$tmplt["scaleDay"] = $tmplt["scaleWeek"] = $tmplt["scaleMonth"] = $tmplt["scaleYear"] = false;

switch ($chart->getScale()) {
    case Chart::DAY:
        $tmplt["scaleDay"] = true;
        break;
    case Chart::WEEK:
        $tmplt["scaleWeek"] = true;
        break;
    case Chart::MONTH:
        $tmplt["scaleMonth"] = true;
        break;
    case Chart::YEAR:
        $tmplt["scaleYear"] = true;
        break;
}

// Annotations
$tmplt["showAnnotations"] = $chart->showAnnotations();

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