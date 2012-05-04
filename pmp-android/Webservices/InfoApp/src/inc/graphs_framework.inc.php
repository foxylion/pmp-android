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

// Prepare callendar
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
$calendar->urlPrevMonth = "connection.php?year=" . $calendar->getYearOfPrevMonth() . "&month=" .$calendar->getPrevMonth();
$calendar->urlNextMonth = "connection.php?year=" . $calendar->getYearOfNextMonth() . "&month=" .$calendar->getNextMonth();
$calendar->urlPrevYear = "connection.php?year=" . $calendar->getPrevYear() . "&month=" . $calendar->getMonth();
$calendar->urlNextYear = "connection.php?year=" . $calendar->getNextYear() . "&month=" . $calendar->getMonth();
$calendar->urlSelectDay = "connection.php?year=" . $calendar->getYear() . "&month=" . $calendar->getMonth() . "&day=%d";

$timeMs =  Chart::timestampToMillis($calendar->getTimestamp());
// Gather event data
$device = Device::getInstance("f2305a2fbef51bd82008c7cf3788250f");

$tmplt["getParams"] = "year=" . $calendar->getYear() . "&month=" . $calendar->getMonth() . "&day=" . $calendar->getDay();
?>
