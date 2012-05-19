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

class GChartPhpBridge {

    const LEGEND = 0;
    const Y_COORDS = 1;
    const AXIS_LABEL = 2;

    private $data;

    public function __construct(GDataTable $data) {
        $this->data = $data;
    }

    /**
     * Pushs the data stored in the GDataTable into a gGraph.
     * @param gchart\gChart $gChart Graph in which the data should be pushed
     * @param enum $firstColumnRole The data in the first column of the GDataTable
     *                  is used for different purposes, depending on the chart-type it
     *                  is used for (in Pie-Chart, for example, the first column
     *                  represents the secments' label, whereas in line-charts,
     *                  this column is used for the y-axis. If this parameter
     *                  is set to <code>LEGEND</CODE>, the data in the first column
     *                  will be "pushed" into the legend (through <code>$gChart->setLegend</code>),
     *                  if set to <code>Y_COORDS</code>, it will be "pushed" into
     *                  a data row (through <code>$gChart->addDataSet()</code>
     *
     * @param int $scale If <code>$firstColumnRole</code> is set to <code>Y_COORDS</code>,
     *                  this parameter will be used to define the rightes value
     *                  that's drawn on the y-axis. That is, the values
     *                  on the y-axis will run from 0 to $scale
     * @return \gchart\gChart
     */
    public function pushData(gchart\gChart $gChart, $firstColumnRole, $scale = 0) {
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
        $datasets = 0;


        switch ($firstColumnRole) {
            case self::LEGEND:
                $datasets = count($data2D[0]);

                $gChart->addDataSet($data2D[1]);
                $gChart->setLegend($data2D[0]);

                // Add the values as label if it's a pie charts
                // (wouldn't be vissible otherwise) and set
                // start-point to the same as in Google's chart tools
                if ($gChart instanceof gchart\gPieChart) {
                    $gChart->setLabels($data2D[1]);
                    $gChart->setRotation(-90,true);
                }
                break;
            case self::AXIS_LABEL:
                $datasets = count($data2D) - 1;

                $gChart->addAxisLabel(0, $data2D[0]);
                for ($i = 1; $i < count($data2D); $i++) {
                    $gChart->addDataSet($data2D[$i]);
                }
                $max = (int) gchart\utility::getMaxOfArray($data2D);
                $max += $max/10;

                $gChart->setLegend(array_slice($legend, 1));
                $gChart->setDataRange(0, $max);
                $gChart->addAxisRange(1, 0, $max);

                $len = pow(10, strlen((int)$max));
                $gChart->setGridLines(0, (10 / $max) * $len);

                break;
            case self::Y_COORDS:
                $datasets = count($data2D) - 1;

                // Data range has to be scalled to a 0 to 100 range
                // since x- and y-axis have a different range which will
                // not be handled correctly by the gChart API
                // Normalize x-axis (first column) so that all x-values will go from 0 to 100
                $xMin = $data2D[0][0];
                $xMax = $data2D[0][count($data2D[0]) - 1];
                $xDelta = $xMax - $xMin;
                $xScaleFactor = $xDelta / 100;

                // Happens if, for example, only one row exists
                if ($xScaleFactor == 0) {
                    $xScaleFactor = 1;
                }

                for ($i = 0; $i < count($data2D[0]); $i++) {
                    $data2D[0][$i] = (int) (($data2D[0][$i] - $xMin) / $xScaleFactor);
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

                // Write data into chart
                for ($i = 1; $i < count($data2D); $i++) {
                    $gChart->addDataSet($data2D[0]);

                    foreach ($data2D[$i] as $key => $value) {
                        $data2D[$i][$key] = (int) (($value - $yMin) / $yScaleFactor);
                    }
                    $gChart->addDataSet($data2D[$i]);
                }

                // Set axis range and legend
                $gChart->setEncodingType("t");
                $gChart->setDataRange(0, 100);
                if ($scale != 0) {
                    $gChart->addAxisRange(0, 0, $scale);
                }
                $gChart->addAxisRange(1, $yMin, $yMax);
                $gChart->setLegend(array_slice($legend, 1));
                break;
        }


        // Use the same colors as the default colors as in Google's chart tools
        $colors = array("3366CC", "DC3912" , "FF9900", "109618", "990099", "0099C6",
            "DD4477", "66AA00", "B82E2E", "316395", "994499", "22AA99");

        $gChart->setColors(array_slice($colors,0,$datasets));
        return $gChart;
    }

}

?>
