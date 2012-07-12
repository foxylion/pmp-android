<?php
if (isset ($_REQUEST["fehler"])) {
    echo "<br><b style='color: red'>Die Zugangsdaten waren ungültig.</b><br><br>";
}
?>
<form action="login.php" method="post">
    Username: <input type="text" name="username" size="20"><br>
    ObserverNr: <input type="text" name="obs_nr" size="20"><br>
    <input type="submit" value="Login">
</form>