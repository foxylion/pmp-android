<html>
    <head>
        <title>vHike - E-Mail Verification</title>
    </head>
    <body>
        <h1>E-Mail Verification</h1>
        <p>
        
        
<?php
define("INCLUDE", true);

// Load config-file
require ("./inc/config.inc.php");

// Load class-files
require ("./inc/class/database.class.php");
require ("./inc/class/general.class.php");
require ("./inc/class/user.class.php");

// Connect to database
try {
    Database::getInstance()->connect();
    if (isset($_GET["user"]) && isset($_GET["key"]) && 
            User::verifyUser($_GET["user"], $_GET["key"])) {
        echo "User account acctivated successfully. You can now log in.";
    } else {
        echo "The given activation key is invalid or the user has already been activated";
    }
} catch (DatabaseException $de) {
    echo "Internal database-error:<br />$de";
}
?>
        </p>
    </body>
</html>