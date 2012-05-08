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

?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>InfoApp - <?php echo $tmplt["pageTitle"] ?></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link rel="stylesheet" type="text/css" href="./../res/main.css" media="screen"/>
        <?php if (isset($tmplt["jsFunctDrawChart"])) {?>
        <!--Load the AJAX API-->
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript">
            // Load the Visualization API library and the piechart library.

            // Set a callback to run when the Google Visualization API is loaded.
            google.setOnLoadCallback(drawChart);

            google.load('visualization', '1.0', {'packages':['corechart']});

            // Callback that creates and populates a data table,
            // instantiates the pie chart, passes in the data and
            // draws it.
            function drawChart() {
                <?php echo $tmplt["jsFunctDrawChart"] ?>

            }

            <?php echo $tmplt["jsDrawFunctions"] ?>


        </script>
        <?php } ?>
    </head>
    <body>
        <div id="leftarea">
            <h1>Device Charts:</h1>
            <?php $tmplt["navGetParams"] = $tmplt["dateGetParams"] . "&" . $tmplt["scaleGetParam"] . "&" . $tmplt["deviceGetParam"] ?>
            <ul id="navigation">
                <li><a href="./battery.php?<?php echo $tmplt["navGetParams"] ?>">Battery</a></li>
                <li><a href="./connectioncellular.php?<?php echo $tmplt["navGetParams"] ?>">Cellular Connection</a></li>
                <li><a href="./connection.php?<?php echo $tmplt["navGetParams"] ?>">Connection</a></li>
            </ul>

            <h1>Date:</h1>
            <?php echo $calendar->getHtml() ?>

            <h1>Scale:</h1>
            <?php $tmplt["scaleGetParams"] = $tmplt["dateGetParams"] . "&" . $tmplt["deviceGetParam"] ?>
            <ul id="scale">
                <li <?php if($tmplt["scaleDay"]) echo "class=\"selected\""; ?>>
                    <a href="./<?php echo $tmplt["filename"] ?>.php?<?php echo $tmplt["scaleGetParams"] ?>&scale=day">Day</a>
                </li>
                <li <?php if($tmplt["scaleWeek"]) echo "class=\"selected\""; ?>>
                    <a href="./<?php echo $tmplt["filename"] ?>.php?<?php echo $tmplt["scaleGetParams"] ?>&scale=week">Week</a>
                </li>
                <li <?php if($tmplt["scaleMonth"]) echo "class=\"selected\""; ?>>
                    <a href="./<?php echo $tmplt["filename"] ?>.php?<?php echo $tmplt["scaleGetParams"] ?>&scale=month">Month</a>
                </li>
                <li <?php if($tmplt["scaleYear"]) echo "class=\"selected\""; ?>>
                    <a href="./<?php echo $tmplt["filename"] ?>.php?<?php echo $tmplt["scaleGetParams"] ?>&scale=year">Year</a>
                </li>
            </ul>
        </div>
        <div id="contentarea">
            <?php echo $tmplt["content"] ?>
        </div>
    </body>
</html>