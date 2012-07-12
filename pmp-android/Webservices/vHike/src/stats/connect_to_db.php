<?php 
include("config.php");
// Datenbankverbindung aufbauen  
$connectionid = mysql_connect ($dbhost, $dbuser, $dbpass);  
if (!mysql_select_db ($dbname, $connectionid))  
{  
  die ("Keine Verbindung zur Datenbank");  
}

// EOF