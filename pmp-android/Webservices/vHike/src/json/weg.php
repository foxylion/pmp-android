<?php
/**
 * Author: Kannextic
 * Date: 10.02.12
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
define('INCLUDE', true);
require ('./../inc/json_framework.inc.php');

$db = Database::getInstance();
if (isset($_POST['offer'])) {
	$db->query("TRUNCATE dev_offer");
}
if (isset($_POST['query'])) {
	$db->query('TRUNCATE dev_query');
}
if (isset($_POST['ride'])) {
	$db->query('TRUNCATE dev_ride');
}
if (isset($_POST['trip'])) {
	$db->query("TRUNCATE dev_trip");
}

echo "Gelöscht! Alles weg!";

// EOF