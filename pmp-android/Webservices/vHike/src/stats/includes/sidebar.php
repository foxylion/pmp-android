<div id="sidebar">

    <h3>Navigation</h3>
    <li><a href="statistics.php">Statistics</a></li>
    <li><a href="intern.php">Observation</a></li>

    <h3>Login</h3>
    <li><?php if (!isset ($_SESSION["obs_nr"])) {
        include("formular.php");
    } else {
        echo "<a href='logout.php'>Ausloggen</a> ";
    }  ?></li>

    <h3>News</h3>
    <li>News</li>

</div> <!-- end #sidebar -->