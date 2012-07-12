<?php  
include("checkuser.php");
include("connect_to_db.php");

$sql = "SELECT * FROM dev_observation WHERE obs_nr=".$_SESSION['obs_nr']."";  
$result = mysql_query ($sql);  

if (mysql_num_rows ($result) > 0)  
{  
  // Benutzerdaten in ein Array auslesen.  
  $data = mysql_fetch_array ($result);  

  // Sessionvariablen erstellen und registrieren  
  $_SESSION['user_id'] = $data['user_id'];  
  $_SESSION['obs_nr'] = $data['obs_nr'];  

  // Position ermitteln
  $sql = "SELECT latitude, longitude, last_update FROM dev_position WHERE user=".$_SESSION['user_id']."";
  $result = mysql_query ($sql); 

  $data = mysql_fetch_array ($result);

  $_SESSION['pos_lat'] = $data['latitude'];
  $_SESSION['pos_lon'] = $data['longitude'];
  $_SESSION['last_update'] = $data['last_update'];

  // Benutzerdaten ermitteln
  $sql = "SELECT username FROM dev_user WHERE id=".$_SESSION['user_id']."";
  $result = mysql_query ($sql); 

  $data = mysql_fetch_array ($result);

  $_SESSION['user_name'] = $data['username'];

  // Anzahl der Fahrten eines Users ermitteln
  $sql = "SELECT dev_trip.driver, dev_trip.creation, dev_trip.ending, dev_trip.destination, dev_ride.trip, dev_trip.id  FROM dev_ride INNER JOIN dev_trip WHERE dev_ride.trip=dev_trip.id AND (dev_ride.passenger = ".$_SESSION['user_id']." OR dev_trip.driver = ".$_SESSION['user_id'].")";
	$result = mysql_query($sql);
	if (mysql_num_rows ($result) > 0) {
  $_SESSION['#_all_rides'] = 0;
		while($data = mysql_fetch_array($result) ){
		  $_SESSION['#_all_rides']++;
		}
	}else{  $_SESSION['#_all_rides'] = 0;}
  
  // Bewertung ermitteln
  $sql = "SELECT SUM(rating) FROM dev_rating WHERE recipient=".$_SESSION['user_id']."";
  $result = mysql_query ($sql); 

  $data = mysql_fetch_array ($result);

  $_SESSION['sum_ratings'] = $data[0];

  // Anzahl der Bewertungen
  $sql = "SELECT COUNT(*) FROM dev_rating WHERE recipient=".$_SESSION['user_id']."";
  $result = mysql_query ($sql); 

  $data = mysql_fetch_array ($result);

  $_SESSION['#_ratings'] = $data[0];

  // User Bewertung ermitteln
  if($_SESSION['#_ratings'] != 0){
  	$_SESSION['user_rating'] = $_SESSION['sum_ratings'] / $_SESSION['#_ratings'];
  }else{
  	$_SESSION['user_rating'] = 0;
  }
  
  

	$sql = "SELECT dev_ride.picked_up, dev_trip.driver, dev_trip.creation, dev_trip.destination, dev_ride.trip, dev_trip.id  FROM dev_ride INNER JOIN dev_trip WHERE dev_ride.trip=dev_trip.id AND dev_trip.ending='0000-00-00 00:00:00' AND dev_ride.passenger= ".$_SESSION['user_id']." OR dev_trip.driver =".$_SESSION['user_id']."";
	$result = mysql_query($sql);
	
	if (mysql_num_rows ($result) > 0) {
		$data = mysql_fetch_array ($result); 
		$_SESSION['trip_id'] = $data['id'];
		$_SESSION['driver'] = $data['driver'];
		$_SESSION['is_picked'] = $data['picked_up'];
		$_SESSION['creation'] = $data['creation'];
		$_SESSION['destination'] = $data['destination'];
		$_SESSION['is_in_trip'] = true;
	 }else{
	 	$_SESSION['is_in_trip'] = false;
	 }

	// Position des Fahrers, falls in einer Fahrt anzeigen
	if($_SESSION['is_in_trip']){
		// Position ermitteln
  	$sql = "SELECT latitude, longitude, last_update FROM dev_position WHERE user=".$_SESSION['driver']."";
  	$result = mysql_query ($sql); 

  	$data = mysql_fetch_array ($result);
	
  	$_SESSION['driver_pos_lat'] = $data['latitude'];
  	$_SESSION['driver_pos_lon'] = $data['longitude'];
	}else{
	}
}
	 ?>
