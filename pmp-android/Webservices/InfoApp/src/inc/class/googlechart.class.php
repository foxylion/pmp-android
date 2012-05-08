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

/**
 * A GColumn stores the definition of one column of the data table.
 * For each column of the data table, a GColumn has to be defined
 *
 * @author Patrick Strobel
 */
class GColumn {

        /** @var String */
        private $type;
        /** @var String */
        private $id;
        /** @var String */
        private $label;
        /** @var String */
        private $pattern;
        /** @var String */
        private $property;

        /**
         * Creates a new columns.
         * @see https://google-developers.appspot.com/chart/interactive/docs/reference#dataparam for details
         * @param String $type
         * @param String $id
         * @param String $label
         * @param String $pattern
         * @param String $p
         */
        public function __construct($type, $id = null, $label = null, $pattern = null, $property = null) {
            $this->type = $type;
            $this->id = $id;
            $this->label = $label;
            $this->pattern = $pattern;
            $this->property = $property;
        }

        public function getType() {
            return $this->type;
        }

        public function getId() {
            return $this->id;
        }

        public function getLabel() {
            return $this->label;
        }

        public function toJsonString() {
            $string = "{\"type\": \"" . $this->type . "\"";
            if ($this->id != null) {
                $string .= ", \"id\": \"" . $this->id . "\"";
            }

            if ($this->label != null) {
                $string .= ", \"label\": \"" . $this->label . "\"";
            }

            if ($this->pattern != null) {
                $string .= ", \"pattern\": \"" . $this->pattern . "\"";
            }

            if ($this->property != null) {
                $string .= ", \"p\": " . $this->property;
            }
            $string .= "}";

            return $string;
        }
}

/**
 * A GCell stores the data from one cell of the data table.
 * Various cells are grouped together in a GRow
 *
 * @author Patrick Strobel
 */
class GCell {

    /** @var String */
    private $value;
    /** @var String */
    private $formatted;
    /** @var String */
    private $poperty;

    public function __construct($value, $formatted = null, $property = null) {
        $this->value = $value;
        $this->formatted = $formatted;
        $this->property = $property;
    }

    public function getJsonString($valueInBrackets = true) {
        if ($valueInBrackets) {
            $b = "\"";
        } else {
            $b = "";
        }
        $string = "{\"v\": ";
        $string .= $b . $this->value . $b;


        if ($this->formatted != null) {
            $string .= ", " . $b . $this->formatted . $b;
        }

        if ($this->poperty != null) {
            $string .= ", " . $b . $this->poperty . $b;
        }


        $string .= "}";
        return $string;
    }
}

/**
 * A GRow represents a data entry (a row) in the data table.
 * Each row contains one or more cells that stores the actual data of this row
 *
 * @author Patrick Strobel
 */
class GRow {

    /** @var GCell[] */
    private $cells = array();

    public function addCell(GCell $cell) {
        $this->cells[] = $cell;
    }

    /**
     *
     * @param GColumn[] $cols
     * @return string
     */
    public function toJsonString($cols) {
        $string = "{\"c\": [";

        $first = true;
        foreach ($this->cells as $i => $cell) {
            if ($first) {
                $first = false;
            } else {
                $string .= ", ";
            }


            if ($cols[$i]->getType() == "string") {
                $brackets = true;
            } else {
                $brackets = false;
            }
            $string .= $cell->getJsonString($brackets);
        }

        $string .= "]}";
        return $string;
    }

}

/**
 * A GDataTable represents the data that should be visualized in a chart
 *
 * @author Patrick Strobel
 */
class GDataTable {

    /** @var GColumn[] */
    private $cols = array();

    /** @var GRow[] */
    private $rows = array();

    /**
     * Adds a new column to the table.
     * @param GColumn $col Columns that should be added
     */
    public function addColumn(GColumn $col) {
        $this->cols[] = $col;
    }

    /**
     * Adds a new row to the table
     * @param GRow $row Row that schould be added
     */
    public function addRow(GRow $row) {
        $this->rows[] = $row;
    }

    /**
     * Adds a two cell row by reading an associative array.
     * The array's keys are used for the first column, the array's value for the second column
     * @param String[] $array Associative array
     */
    public function addRowsAssocArray($array) {
        foreach ($array as $key => $value) {
            $row = new GRow();
            $row->addCell(new GCell($key));
            $row->addCell(new GCell($value));
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
     * @return GColumn[]    The data table's columns
     */
    public function getColumns() {
        return $this->cols;
    }

    /**
     * Gets all rows that have been added to the data table
     * @return GRow[]   The data table's rows
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
