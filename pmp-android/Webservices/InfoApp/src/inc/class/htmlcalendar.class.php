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

/**
 * Stored calendary data and creates a html calendar using this data
 *
 * @author Patrick Strobel
 * @version 4.0.0
 */
class HtmlCalendar {

    private $year;
    private $month;
    private $day;
    public $cssClassCurrentDay = "currentDay";
    public $cssClassSelectedDay = "selectedDay";
    public $cssClassCalendar = "calendar";
    public $imgBack = "<img src=\"../res/arrow_down.gif\" alt=\"Prev.\" />";
    public $imgForw = "<img src=\"../res/arrow_up.gif\" alt=\"Next\" />";
    public $urlPrevMonth = "";
    public $urlNextMonth = "";
    public $urlPrevYear = "";
    public $urlNextYear = "";
    public $urlSelectDay = "";

    /**
     * Defines the day that should be used as first day of the week (0 = Sunday, 1 = Monday, ...)
     * @var int
     */
    private $firstDay = 1;
    private $weekDays = array("S", "M", "T", "W", "T", "F", "S");

    public function __construct($year = 0, $month = 0, $day = 0) {
        $currentDate = getdate();

        // Set current date value for field that are set to zero
        if ($year == 0) {
            $this->year = $currentDate["year"];
            $this->month = $currentDate["mon"];
            $this->day = $currentDate["mday"];
        } elseif ($month == 0) {
            $this->year = $year;
            $this->month = $currentDate["mon"];
            $this->day = $currentDate["mday"];
        } elseif ($day == 0) {
            $this->year = $year;
            $this->month = $month;
            $this->day = $currentDate["mday"];
        } else {
            $this->year = $year;
            $this->month = $month;
            $this->day = $day;
        }

        // Avoid setting the calendar to illegal values
        if ($this->year < 1) {
            $this->year = 1;
        }

        if ($this->month < 1) {
            $this->month = 1;
        }elseif ($this->month > 12) {
            $this->month = 12;
        }
    }

    public function getDay() {
        return $this->day;
    }

    public function getMonth() {
        return $this->month;
    }

    public function getPrevMonth() {
        if ($this->month <= 1) {
            return 12;
        }
        return $this->month - 1;
    }

    public function getNextMonth() {
        if ($this->month >= 12) {
            return 1;
        }
        return $this->month + 1;
    }

    public function getYear() {
        return $this->year;
    }

    public function getYearOfPrevMonth() {
        if ($this->month <= 1) {
            return $this->getPrevYear();
        }
        return $this->year;
    }

    public function getYearOfNextMonth() {
        if ($this->month >= 12) {
            return $this->getNextYear();
        }
        return $this->year;
    }

    public function getPrevYear() {
        if ($this->year <= 1) {
            return 1;
        }
        return $this->year - 1;
    }

    public function getNextYear() {
        return $this->year + 1;
    }

    public function getDaysInMonth() {
        return cal_days_in_month(CAL_GREGORIAN, $this->month, $this->year);
    }

    /**
     * Gets the selected date as timetamp
     * @return long Timestamp in seconds. Hours, minutes and secondes are set to zero
     */
    public function getTimestamp() {
        return mktime(0, 0, 0, $this->month, $this->day, $this->year);
    }

    public function getHtml() {

        // Current date used to highlight the entry.
        // 0 if current month is not the month selected in the calendar
        $currentDate = getdate();
        if ($currentDate ["mon"] == $this->month && $currentDate["year"] == $this->year) {
            $currentDay = $currentDate["mday"];
        } else {
            $currentDay = 0;
        }

        // Determine the first week-day and the number of days in this month
        $firstOfMonth = getdate(mktime(0, 0, 0, $this->month, 1, $this->year));
        $daysInMonth = cal_days_in_month(CAL_GREGORIAN, $this->month, $this->year);


        $html = "<table class=\"" . $this->cssClassCalendar . "\">\n";
        $monthLabel = "<a href=\"" . $this->urlPrevMonth . "\">" .
                $this->imgBack . "</a>" . $firstOfMonth["month"] .
                "<a href=\"" . $this->urlNextMonth . "\">" . $this->imgForw . "</a>";

        $yearLabel = "<a href=\"" . $this->urlPrevYear . "\">"
                . $this->imgBack . "</a>" . $this->year .
                "<a href=\"" . $this->urlNextYear . "\">" . $this->imgForw . "</a>";
        $html .= "<tr><th colspan=\"7\">" . $monthLabel . " " . $yearLabel . "</th></tr>\n";

        // Make table header (Name of the day)
        $html .= "<tr>";
        $key = $this->firstDay;
        for ($i = 0; $i < count($this->weekDays); $i++) {
            $html .= "<th>" . $this->weekDays[$key] . "</th>";
            $key = ($key + 1) % 7;
        }
        $html .= "";



        $dayOfWeek = 0;

        // Fill first empty days if necessary
        if ($this->firstDay != $firstOfMonth["wday"]) {
            $html .= "</tr>\n<tr>";
            for ($i = $this->firstDay; $i < $firstOfMonth["wday"]; $i++) {
                $html .= "<td>&nbsp;</td>";
                $dayOfWeek++;
            }
        }

        // Fill with days in month
        for ($i = 1; $i <= $daysInMonth; $i++) {
            if ($dayOfWeek == 0) {
                $html .= "</tr>\n<tr>";
            }

            // Build label (link etc)
            $dayUrl = sprintf($this->urlSelectDay, $i);
            $dayLabel = "<a href=\"" . $dayUrl . "\">" . $i . "</a>";

            // Mark current and selected day
            $class = "";
            if ($i == $currentDay) {
                $class .= $this->cssClassCurrentDay;
            }
            if ($i == $this->day) {
                if ($class != "") {
                    $class .= " ";
                }
                $class .= $this->cssClassSelectedDay;
            }

            if ($class != "") {
                $html .= "<td class=\"" . $class . "\">" . $dayLabel . "</td>";
            } else {
                $html .= "<td>" . $dayLabel . "</td>";
            }

            $dayOfWeek = ($dayOfWeek + 1) % 7;
        }

        // Fill last empty days if necessary
        if ($dayOfWeek != 0) {
            for ($i = $dayOfWeek; $i < 7; $i++) {
                $html .= "<td>&nbsp;</td>";
            }
        }

        $html .= "</tr>\n</table>";
        echo $html;
    }

    public function getFistDayOfMonth() {
        $date = new DateTime();
        $date->setDate($this->year, $this->month, 1);
        return (int) $date->format("w");
    }

}

?>
