<?php

if (!defined("INCLUDE")) {
    exit;
}
error_reporting(E_ALL);
//error_reporting(E_ERROR | E_PARSE);

define("DEBUG", true);
define("FORMAT_JSON", true);

define("DB_HOST", "localhost");
define("DB_USER", "root");
define("DB_PASSW", "");
define("DB_NAME", "infoapp");
define("DB_PREFIX", "dev");
?>
