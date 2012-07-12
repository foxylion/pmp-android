<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="description" content="" />
<meta name="keywords" content="" />
<meta name="author" content="" />
<link rel="stylesheet" type="text/css" href="style.css" media="screen" />
<?php  
error_reporting(0);
include ("update.php");
include ("checkuser.php");
include ("connect_to_db.php");

?>  
<html>  
<head>  
  <title>Interne Seite</title>  
</head>  
<body> 

<div id="wrapper">
			<div id="header">
				<?php include('includes/header.php'); ?>
			</div> <!-- end #header -->

			<div id="nav">
				<?php include('includes/nav.php'); ?>
			</div> <!-- end #nav -->

			<div id="content">
<h1>Observationsmodus</h1>
<hr align='left' width=40%>

  Benutzername: <?php echo $_SESSION["user_name"]; ?><br>
  BenutzerId: <?php echo $_SESSION["user_id"]; ?><br>  
  Bewertung: <?php echo $_SESSION["user_rating"]; ?><br>
  OBS_Nr: <?php echo $_SESSION["obs_nr"]; ?><br>  
  <hr>  
  <table width=100%>
    <tr>
	<td>
		Letzte bekannte Position: 
	</td>
	<td>
		<?php echo $_SESSION["pos_lat"].",<br>".$_SESSION["pos_lon"]; ?>
	</td>
	<td>
		<a href="http://maps.google.com/maps?q=<?php echo $_SESSION["pos_lat"].",".$_SESSION["pos_lon"] ; ?>">Auf Googlemaps öffnen</a>
	</td>
    </tr>
    <tr>
	<td>
		Gesamte Fahrten:
	</td>
	<td>
		<?php echo $_SESSION["#_all_rides"]; ?>
	</td>
	<td>
		<a href="history.php">Verlauf aufrufen</a>
	</td>
    </tr>
    <tr>
	<td colspan=3>
		<?php if ($_SESSION["is_in_trip"] == true){ echo "<p>Dieser Benutzer befindet sich in einer Fahrt!<br>"; 
																								echo "FahrerID: ".$_SESSION['driver']."<br>";
																								echo "Ziel: ".$_SESSION['destination']."<br>";
																								echo "Erstellungsdatum: ".$_SESSION['creation']."<br>";
																								echo "Abgeholt :"; 
																								if($_SESSION['is_picked']){
																									echo "ja"; 
																								}else{
																									echo"nein";
																								}
																								echo "<br>";
																								echo "Position des Fahrers: <a href='http://maps.google.com/maps?q=".$_SESSION["driver_pos_lat"].",".$_SESSION["driver_pos_lon"]."'>Auf Googlemaps öffnen</a>";

																								echo "</p>";
																								 } else{echo "Dieser Benutzer befindet sich in keiner Fahrt!";} ?>
	</td>
    </tr>
  </table>
  
  </div> <!-- end #content -->

			<div id="sidebar">
				<?php include('includes/sidebar.php'); ?>
			</div> <!-- end #sidebar -->

			<div id="footer">
				<?php include('includes/footer.php'); ?>
			</div> <!-- end #footer -->
		</div> <!-- End #wrapper -->
</body>  
</html>
