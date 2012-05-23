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

namespace infoapp;
use infoapp\eventmanager\EventManager;

if (!defined("INCLUDE")) {
    exit;
}

/**
 * Helper class for easier creation of Google Charts
 *
 * @author Patrick Strobel
 * @version 4.2.0
 * @package infoapp
 */
class Chart {

    const DAY = "day";
    const WEEK = "week";
    const MONTH = "month";
    const YEAR = "year";

    private $scale = Chart::DAY;
    private $showAnnotations = true;

    public function setScale($scale) {
        if ($scale == Chart::DAY || $scale == Chart::WEEK || $scale == Chart::MONTH || $scale == Chart::YEAR) {
            $this->scale = $scale;
        } else {
            $this->scale = Chart::DAY;
        }
    }

    public function getScale() {
        return $this->scale;
    }

    public function getScaleXAxis(HtmlCalendar $calendar) {
        switch ($this->scale) {
            case self::DAY:
                return 24 * 60 * 60 * 1000;
            case self::WEEK:
                return 7 * 24 * 60 * 60 * 1000;
            case self::MONTH:
                return $calendar->getDaysInMonth() * 24 * 60 * 60 * 1000;
            case self::YEAR:
                return 365 * 24 * 60 * 60 * 1000;
        }
    }

    public function getScaleXAxisLabel(HtmlCalendar $calendar) {
        switch ($this->scale) {
            case self::DAY:
                return 24;
            case self::WEEK:
                return 7;
            case self::MONTH:
                return $calendar->getDaysInMonth();
            case self::YEAR:
                return 365;
        }
    }

    public function getOffsetXAxis(HtmlCalendar $calendar) {
        switch ($this->scale) {
            case self::DAY:
                $date = getdate(mktime(0, 0, 0, $calendar->getMonth(), $calendar->getDay(), $calendar->getYear()));
                return $date[0]*1000;

    }
    }

    public function getAxisChartHeight() {
        return 150;
    }

    public function getAxisChartWidth() {
        return 800;
    }

    public function getPieChartHeight() {
        return 400;
    }

    public function getPieChartWidth() {
        return 600;
    }

    public function getEventsByScale(EventManager $manager, $startTs, $daysInMonth) {
        switch ($this->scale) {
            case Chart::DAY:
                return $manager->getEventsOneDay($startTs);
            case Chart::WEEK:
                return $manager->getEventsMultDays($startTs, 7);
            case Chart::MONTH:
                return $manager->getEventsMultDays($startTs, $daysInMonth);
            case Chart::YEAR:
                return $manager->getEventsMultDays($startTs, 365);
        }
    }

    public function setShowAnntotations($show) {
        if (is_bool($show)) {
            $this->showAnnotations = $show;
        }
    }

    public function showAnnotations() {
        return $this->showAnnotations;
    }

    /**
     *
     * @param String[][] $columns
     * @param string[][] $data
     * @deprecated Use class GDataObject instead
     */
    public static function getDataObject($columns, $rows) {

        $data = "var data = new google.visualization.DataTable();\n";

        // Create column field
        foreach ($columns as $value) {
            if (is_array($value)) {
                $data .= "data.addColumn('" . $value[0] . "', '" . $value[1] . "');\n";
            } else {
                $data .= "data.addColumn(" . $value . ");\n";
            }
        }

        $data .= "data.addRows([\n";

        $outerFirst = true;
        foreach ($rows as $key => $value) {
            if ($outerFirst) {
                $outerFirst = false;
            } else {
                $data .= ", \n";
            }

            $innerFirst = true;
            $data .= "[";

            for ($i = 0; $i < count($value); $i++) {
                if ($innerFirst) {
                    $innerFirst = false;
                } else {
                    $data .= ", ";
                }
                if ($columns[$i][0] == "string" || !is_array($columns[$i])) {
                    $data .= "'";
                }
                $data .= $value[$i];
                if ($columns[$i][0] == "string" || !is_array($columns[$i])) {
                    $data .= "'";
                }
            }
            $data .= "]";
        }

        $data .= "\n]);\n";
        return $data;
    }

    /**
     * Converts a timestamp given in MS into a formated date/time string
     * @param String $format    Format to which the date/time should be converted
     * @param long $millis      Timestamp in MS that needs to be converted
     * @return String           Formated date/time
     */
    public static function timeMillisToString($format, $millis) {
        $sec = $millis / 1000;
        return date($format, $sec);
    }

    /**
     * Converts a php timestamp into a Java MS-timestamp
     * @param long $timestamp   PHP Timestamp
     * @return long Java timestamp
     */
    public static function timestampToMillis($timestamp) {
        return $timestamp * 1000;
    }

}

?>
