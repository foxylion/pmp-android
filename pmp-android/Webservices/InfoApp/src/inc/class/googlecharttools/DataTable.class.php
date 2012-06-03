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

namespace infoapp\googlecharttools;

if (!defined("INCLUDE")) {
    exit;
}

/**
 * A DataTable represents the data that should be visualized in a chart
 *
 * @author Patrick Strobel
 * @version 4.1.1
 * @package infoapp
 * @subpackage googlecharttools
 */
class DataTable {

    /** @var Column[] */
    private $cols = array();

    /** @var Row[] */
    private $rows = array();

    /**
     * Adds a new column to the table.
     * @param Column $col Columns that should be added
     */
    public function addColumn(Column $col) {
        $this->cols[] = $col;
    }

    /**
     * Adds a new row to the table
     * @param Row $row Row that schould be added
     */
    public function addRow(Row $row) {
        $this->rows[] = $row;
    }

    /**
     * Adds a two cell row by reading an associative array.
     * The array's keys are used for the first column, the array's value for the second column
     * @param String[] $array Associative array
     */
    public function addRowsAssocArray($array) {
        foreach ($array as $key => $value) {
            $row = new Row();
            if ($key != null) {
                $row->addCell(new Cell($key));
            } else {
                $row->addCell(new Cell("Unknown"));
            }
            $row->addCell(new Cell($value));
            $this->rows[] = $row;
        }
    }

    /**
     * Gets the number of columns that have been added to the data table
     * @return int  Number of columns added by {@see addColumn()}
     */
    public function getColumnsCount() {
        return count($this->cols);
    }

    /**
     * Gets the number of rows that have been added to the data table
     * @return int  Number of rows added by {@see addRow()}
     */
    public function getRowsCount() {
        return count($this->rows);
    }

    /**
     * Gets all columns that have been added to the data table
     * @return Column[]    The data table's columns
     */
    public function getColumns() {
        return $this->cols;
    }

    /**
     * Gets all rows that have been added to the data table
     * @return Row[]   The data table's rows
     */
    public function getRows() {
        return $this->rows;
    }

    public function getJsonObject() {
        $json = "{\n";

        // Build cols-array
        $json .= "  \"cols\": [\n";
        $first = true;
        foreach ($this->cols as $col) {
            if ($first) {
                $first = false;
            } else {
                $json .= ",\n";
            }
            $json .= "    ".$col->toJsonString();
        }
        $json .= "],\n";


        // Build rows-array
        $json .= "  \"rows\": [\n";
        $first = true;
        foreach ($this->rows as $row) {
            if ($first) {
                $first = false;
            } else {
                $json .= ",\n";
            }
            $json .= "    ".$row->toJsonString($this->cols);
        }
        $json .= "]\n";
        $json .= "}";

        return $json;

    }
}
?>