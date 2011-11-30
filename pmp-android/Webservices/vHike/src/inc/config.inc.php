<?php
if (!defined("INCLUDE")) {
    exit;
}
error_reporting(E_ALL);
//error_reporting(E_ERROR | E_PARSE);

define("DEBUG",     true);
define("FORMAT_JSON", true);

define("BASE_URL", "vhike.no-ip.org");
define("ADMIN_EMAIL", "admin@vhike.no-ip.org");

define("DB_HOST",   "localhost");
define("DB_USER",   "root");
define("DB_PASSW",  "");
define("DB_NAME",   "vhike");
define("DB_PREFIX", "dev");

?>
