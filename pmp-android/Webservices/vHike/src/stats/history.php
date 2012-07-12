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
include ("checkuser.php");
include ("connect_to_db.php");
include ("update.php");

	$sql = "SELECT dev_trip.driver, dev_trip.creation, dev_trip.ending, dev_trip.destination, dev_ride.trip, dev_trip.id  FROM dev_ride INNER JOIN dev_trip WHERE dev_ride.trip=dev_trip.id AND (dev_ride.passenger = ".$_SESSION['user_id']." OR dev_trip.driver = ".$_SESSION['user_id'].")";
	$result = mysql_query($sql);
?>  
<html>  
<head>  
  <title>Verlauf</title>  
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
<h1>Verlauf</h1>
<hr align='left' width=40%>

  Benutzername: <?php echo $_SESSION["user_name"]; ?><br>
  BenutzerId: <?php echo $_SESSION["user_id"]; ?><br>  
  Bewertung: <?php echo $_SESSION["user_rating"]; ?><br>
  OBS_Nr: <?php echo $_SESSION["obs_nr"]; ?><br>  
  <hr>  
  <table width=100%>
  <th>FahrtID</th>
  <th>FahrerID</th>
  <th>Start</th>
  <th>Ende</th>
  <th>Ziel</th>
  <?php 
  	if (mysql_num_rows ($result) > 0) {
		while($data = mysql_fetch_array($result) ){
			echo"<tr align = center><td>".$data['id']." </td><td>".$data['driver'] ." </td><td> ".$data['creation']." </td><td>". $data['ending']." </td><td>". $data['destination']." </td></tr>";
		}
		
	}
  	
  ?>
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
