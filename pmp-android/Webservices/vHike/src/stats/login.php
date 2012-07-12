<?php
// Session starten 
// session_start ();

include("connect_to_db.php");

$username = mysql_real_escape_string($_POST['username']);
$observation_number = mysql_real_escape_string($_POST['obs_nr']);

$sql = "SELECT observation.*\n" .
    "FROM dev_observation AS observation\n" .
    "INNER JOIN dev_user AS `user` ON `user`.id = observation.user_id\n" .
    "WHERE `user`.username = '$username' AND observation.obs_nr = $observation_number";

$result = mysql_query($sql);

if (mysql_num_rows($result) > 0) {
    // Benutzerdaten in ein Array auslesen.
    $data = mysql_fetch_array($result);

    session_start();

    // Sessionvariablen erstellen und registrieren
    $_SESSION['user_id'] = $data['user_id'];
    $_SESSION['obs_nr'] = $data['obs_nr'];

    // Position ermitteln
    $sql = "SELECT latitude, longitude, last_update FROM dev_position WHERE user=" . $_SESSION['user_id'] . "";
    $result = mysql_query($sql);

    $data = mysql_fetch_array($result);

    $_SESSION['pos_lat'] = $data['latitude'];
    $_SESSION['pos_lon'] = $data['longitude'];
    $_SESSION['last_update'] = $data['last_update'];

    // Benutzerdaten ermitteln
    $sql = "SELECT username FROM dev_user WHERE id=" . $_SESSION['user_id'] . "";
    $result = mysql_query($sql);

    $data = mysql_fetch_array($result);

    $_SESSION['user_name'] = $data['username'];

    // Anzahl der Fahrten eines Users ermitteln
    $sql = "SELECT dev_trip.driver, dev_trip.creation, dev_trip.ending, dev_trip.destination, dev_ride.trip, dev_trip.id  FROM dev_ride INNER JOIN dev_trip WHERE dev_ride.trip=dev_trip.id AND (dev_ride.passenger = " . $_SESSION['user_id'] . " OR dev_trip.driver = " . $_SESSION['user_id'] . ")";
    $result = mysql_query($sql);
    if (mysql_num_rows($result) > 0) {
        $_SESSION['#_all_rides'] = 0;
        while ($data = mysql_fetch_array($result)) {
            $_SESSION['#_all_rides']++;
        }
    } else {
        $_SESSION['#_all_rides'] = 0;
    }

    // Bewertung ermitteln
    $sql = "SELECT SUM(rating) FROM dev_rating WHERE recipient=" . $_SESSION['user_id'] . "";
    $result = mysql_query($sql);

    $data = mysql_fetch_array($result);

    $_SESSION['sum_ratings'] = $data[0];

    // Anzahl der Bewertungen
    $sql = "SELECT COUNT(*) FROM dev_rating WHERE recipient=" . $_SESSION['user_id'] . "";
    $result = mysql_query($sql);

    $data = mysql_fetch_array($result);

    $_SESSION['#_ratings'] = $data[0];

    // User Bewertung ermitteln
    $_SESSION['user_rating'] = $_SESSION['#_ratings'] == 0 ? 0 : $_SESSION['sum_ratings'] / $_SESSION['#_ratings'];

    $sql = "SELECT dev_ride.picked_up, dev_trip.driver, dev_trip.creation, dev_trip.destination, dev_ride.trip, dev_trip.id  FROM dev_ride INNER JOIN dev_trip WHERE dev_ride.trip=dev_trip.id AND dev_trip.ending='0000-00-00 00:00:00' AND dev_ride.passenger= " . $_SESSION['user_id'] . "";
    $result = mysql_query($sql);

    if (mysql_num_rows($result) > 0) {
        $data = mysql_fetch_array($result);
        $_SESSION['trip_id'] = $data['id'];
        $_SESSION['driver'] = $data['driver'];
        $_SESSION['is_picked'] = $data['picked_up'];
        $_SESSION['creation'] = $data['creation'];
        $_SESSION['destination'] = $data['destination'];
        $_SESSION['is_in_trip'] = true;
    } else {
        $_SESSION['is_in_trip'] = false;
    }

//  echo $_SESSION['user_name'];
//  echo $_SESSION['user_id'];
//  echo $_SESSION['obs_nr'];

//  echo $_SESSION['pos_lat'];
//  echo $_SESSION['pos_lon'];
//  echo $_SESSION['last_update'];


//  echo $_SESSION['#_all_rides'];
    header("Location: intern.php");
}
else {
    header("Location: statistics.php?fehler=1");
}

// EOF