<?php
/**
 * This script is used to fix errors in the database by inserting missing 
 * database entries and removing unused entries
 * @author Patrick Strobel
 * @version 1.0.0
 */

define("INCLUDE", true);
require("./../inc/framework.inc.php");

$db = Database::getInstance();


/**
 * Adds missing entries to the position-table
 */
function addPositionEntries() {
    global $db;
    
    // Get all entries from user table that have no entry in the position-table
    $result = $db->query("SELECT u.`id` AS `uid`
                          FROM `" . DB_PREFIX . "_user` u
                          LEFT JOIN `" . DB_PREFIX . "_position` p ON (u.`id` = p.`user`)
                          WHERE p.`id` IS NULL");
    
    // Add a entry for every user
    while (($row = $db->fetch($result)) != null) {
        $db->query("INSERT INTO `" . DB_PREFIX . "_position` (
                        `user`
                     ) VALUES (
                        " . $row["uid"] . "
                     )");
    }
}

/**
 *Removes unused entries from the position-table
 */
function removePositionEntries() {
    global $db;
    
    // Get all entries from position table that have no entry in the user-table
    $result = $db->query("SELECT p.`id` AS `pid`
                          FROM `" . DB_PREFIX . "_position` p
                          LEFT JOIN `" . DB_PREFIX . "_user` u ON (p.`user` = u.`id`)
                          WHERE u.`id` IS NULL");
    
    // Remove entries
    while (($row = $db->fetch($result)) != null) {
        $db->query("DELETE FROM `" . DB_PREFIX . "_position` 
                    WHERE `id` = ".$row["pid"]);
    }
    
}

/****************************
 *          MAIN            *
 ****************************/

?>
<html>
    <head>
        <title>Fix'n'Cleanup</title>
    </head>
    <body>
        <h1>Fix'n'Cleanup</h1>
        <ul>
        
<?php
echo "<li>Add missing entries to position-table... ";
addPositionEntries();
echo "done!</li>\n";

echo "<li>Remove unused entries from position-table... ";
removePositionEntries();
echo "done!</li>\n";

$db->disconnect();
?>
        </ul>
    </body>
</html>
