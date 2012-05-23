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

/**
  This bridges can be used as a converter between a {@link DataTable} and
 * a {@link gChart} object.
 *
 * It reads the data from the data table and writes it automatically into the
 * gChart chrat
 *
 * @version 4.1.0
 * @package infoapp
 * @subpackage googlecharttools
 */
class GChartPhpBridge {
    /**
     * Typically for pie charts
     */

    const LEGEND = 0;
    /**
     * Typically for line charts
     */
    const Y_COORDS = 1;
    /**
     * Typically for bar charts
     */
    const AXIS_LABEL = 2;
    /**
     * Typically for scatter charts
     */
    const IN_TURN = 3;

    private $data;

    public function __construct(DataTable $data) {
        $this->data = $data;
    }

    /**
     * Pushs the data stored in the DataTable into a gGraph.
     * @param gchart\gChart $gChart Graph in which the data should be pushed
     * @param enum $firstColumnRole The data in the first column of the DataTable
     *                  is used for different purposes, depending on the chart-type it
     *                  is used for (in Pie-Chart, for example, the first column
     *                  represents the secments' label, whereas in line-charts,
     *                  this column is used for the y-axis). If this parameter
     *                  is set to <code>LEGEND</CODE>, the data in the first column
     *                  will be "pushed" into the legend (through <code>$gChart->setLegend</code>),
     *                  if set to <code>Y_COORDS</code>, it will be "pushed" into
     *                  a data row (through <code>$gChart->addDataSet()</code>,
     *                  if set to <code>AXIS_LABEL</code>, it will be "pushed" into
     *                  an axis label.
     *                  <code>IN_TURN</code> is like <code>Y_COORDS</code>, however
     *                  the coordinates for the different datasets are "pushed" into
     *                  the chart in turn.
     *
     * @param int $xRange If <code>$firstColumnRole</code> is set to <code>Y_COORDS</code>,
     *                  this parameter will be used to define the rightes value
     *                  that's drawn on the y-axis. That is, the values
     *                  on the y-axis will run from 0 to $scale
     * @return \gchart\gChart
     */
    public function pushData(\gchart\gChart $gChart, $firstColumnRole, $xRange = 0, $xRangeLabel = 24, $xOffset = 0) {
        if ($this->data->getRowsCount() == 0) {
            return;
        }

        $columns = $this->data->getColumns();
        $legend = array();
        // Data is stored in a 2D-Array where the first key is the column and
        // the second key is the row
        $data2D = array();

        foreach ($this->data->getRows() as $rowNum => $row) {
            // Use a seperate counter as some columns will be ignored
            // (and there would be empty columns in the array if we would use $rawCellNum)
            $cellNum = 0;
            foreach ($row->getCells() as $rawCellNum => $cell) {
                // Ignore cells that have a role/property (like annotations) since we cannot
                // represent them in static graphs
                if ($columns[$rawCellNum]->getProperty() == null) {
                    $data2D[$cellNum][$rowNum] = $cell->getValue();
                    $legend[$cellNum] = $columns[$rawCellNum]->getLabel();
                    $cellNum++;
                }
            }
        }


        // Counts how many different datasets have to be drawn (=> number of different colors)
        $datasets = count($data2D) - 1;


        switch ($firstColumnRole) {
            case self::LEGEND:
                $datasets = count($data2D[0]);

                $gChart->addDataSet($data2D[1]);
                $gChart->setLegend($data2D[0]);

                // Add the values as label if it's a pie charts
                // (wouldn't be vissible otherwise) and set
                // start-point to the same as in Google's chart tools
                if ($gChart instanceof \gchart\gPieChart) {
                    $gChart->setLabels($data2D[1]);
                    $gChart->setRotation(-90, true);
                }
                break;
            case self::AXIS_LABEL:

                $gChart->addAxisLabel(0, $data2D[0]);
                for ($i = 1; $i < count($data2D); $i++) {
                    $gChart->addDataSet($data2D[$i]);
                }
                $max = (int) \gchart\utility::getMaxOfArray($data2D);
                $max += $max / 10;

                $gChart->setLegend(array_slice($legend, 1));
                $gChart->setDataRange(0, $max);
                $gChart->addAxisRange(1, 0, $max);

                $len = pow(10, strlen((int) $max));
                $gChart->setGridLines(0, (10 / $max) * $len);

                break;
            case self::Y_COORDS:

                $minMax = $this->normalize($data2D, $xRange, $xOffset);

                // Write data into chart
                for ($i = 1; $i < count($data2D); $i++) {
                    $gChart->addDataSet($data2D[0]);
                    $gChart->addDataSet($data2D[$i]);
                }

                // Set axis range and legend
                $gChart->setDataRange(0, 100);
                if ($xRange != 0) {
                    $gChart->addAxisRange(0, 0, $xRangeLabel);
                }
                $gChart->addAxisRange(1, $minMax["yMin"], $minMax["yMax"]);
                $gChart->setLegend(array_slice($legend, 1));
                break;

            case self::IN_TURN:

                $minMax = $this->normalize($data2D, $xRange, $xOffset);

                $mixedX = array();
                $mixedY = array();
                $mixedIndex = 0;

                // Stores the y-index of the array's element that has been inserted
                // in the destination array the last time
                $counters = array();
                for ($i = 0; $i < count($data2D) - 1; $i++) {
                    $counters[$i] = 0;
                }

                $lastAddedIndex = count($counters) - 1;
                $loop = true;
                while ($loop) {
                    // Insert data in turn
                    for ($i = 0; $i < count($counters); $i++) {
                        // Skip if no data is set or end is reached
                        $rows = count($data2D[$i + 1]);
                        if ($counters[$i] >= $rows) {
                            $loop = false;
                            break;
                        }
                        $loop = true;

                        // Get values

                        $xValue = $data2D[0][$counters[$i]];
                        $yValue = $data2D[$i + 1][$counters[$i]];

                        // Add this row if it's not an empty row
                        if ($yValue != "null") {
                            // Add empty entries if neccessarry
                            $nextIndex = ($i + 1) % count($counters);
                            while ($lastAddedIndex != $nextIndex) {
                                $lastAddedIndex = ($lastAddedIndex + 1) % count($counters);
                                $mixedX[$mixedIndex] = -1;
                                $mixedY[$mixedIndex] = -1;
                                $mixedIndex++;
                            }

                            $mixedX[$mixedIndex] = $xValue;
                            $mixedY[$mixedIndex] = $yValue;
                            $lastAddedIndex = $i;
                            $mixedIndex++;
                        }
                        $counters[$i]++;
                    }
                }

                $gChart->setDataRange(0, 1);
                $gChart->addDataSet($mixedX);
                $gChart->addDataSet($mixedY);

                // Set axis range and legend
                $gChart->setDataRange(0, 100);
                if ($xRange != 0) {
                    $gChart->addAxisRange(0, 0, $xRangeLabel);
                }
                $gChart->addAxisRange(1, $minMax["yMin"], $minMax["yMax"]);
                $gChart->setLegend(array_slice($legend, 1));
                break;
        }

        $gChart->setEncodingType("t");

        // Use the same colors as the default colors as in Google's chart tools
        $colors = array("3366CC", "DC3912", "FF9900", "109618", "990099", "0099C6",
            "DD4477", "66AA00", "B82E2E", "316395", "994499", "22AA99");

        $gChart->setColors(array_slice($colors, 0, $datasets));
        return $gChart;
    }


    private function normalize(&$data2D, $xRange = 0, $xOffset = 0) {
        // Data range has to be scalled to a 0 to 100 range
        // since x- and y-axis have a different range which will
        // not be handled correctly by the gChart API
        // Normalize x-axis (first column) so that all x-values will go from 0 to 100
        $xMin = $data2D[0][0];
        $xMax = $data2D[0][count($data2D[0]) - 1];
        if ($xOffset == 0) {
            $xOffset = $xMin;
        }
        $xDelta = $xMax - $xMin;
        if ($xRange == 0) {
            $xScaleFactor = $xDelta / 100;
        } else {
            $xScaleFactor = $xRange / 100;
        }

        // Happens if, for example, only one row exists
        if ($xScaleFactor == 0) {
            $xScaleFactor = 1;
        }

        for ($i = 0; $i < count($data2D[0]); $i++) {
            // Skip null entries
            if ($data2D[0][$i] != "null") {
                $data2D[0][$i] = (int) (($data2D[0][$i] - $xOffset) / $xScaleFactor);
            }
        }

        // Normalize y-axis (first column) so that all y-values will go from 0 to 100
        $yMin = 100;
        $yMax = -100;

        for ($i = 1; $i < count($data2D); $i++) {
            $yMin = min(min($data2D[$i]), $yMin);
            $yMax = max(max($data2D[$i]), $yMax);
        }
        $yDelta = $yMax - $yMin;
        $yScaleFactor = $yDelta / 100;

        // Happens if, for example, only one row exists
        if ($yScaleFactor == 0) {
            $yScaleFactor = 1;
        }

        // Add some empty space between lowest point and x-axis
        $yMin -= 10 * $yScaleFactor;

        for ($i = 1; $i < count($data2D); $i++) {
            foreach ($data2D[$i] as $key => $value) {
                if ($data2D[$i][$key] != "null") {
                    $data2D[$i][$key] = (int) (($value - $yMin) / $yScaleFactor);
                }
            }
        }
        return array("xMax" => $xMax, "xMin" => $xMin, "yMax" => $yMax, "yMin" => $yMin);
    }

}

?>
