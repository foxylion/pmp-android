<?php
session_start();

if (!isset ($_SESSION["obs_nr"])) {
    header("Location: statistics.php");
}

// EOF