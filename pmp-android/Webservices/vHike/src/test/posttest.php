<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
define("INCLUDE", true);
require("./../inc/json_framework.inc.php");

$output = array("output_pos" => "Hallo ".$_POST['name'].", wie geht's?",  "output_get" => "Mir geht's ".$_GET['mood']);

echo Json::arrayToJson($output);

Database::getInstance()->disconnect();
?>
