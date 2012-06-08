<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Trip</title>
	<style type="text/css">
		body, td, th {
			font-family: Tahoma, Geneva, sans-serif;
			font-size: small;
		}

		body {
			background-color: #EEE;
		}

		.auto-style1 {
			font-family: Tahoma;
			text-decoration: line-through;
		}
	</style>
</head>

<body>
<h1>Driver Tools</h1>

<h2>Announce trip</h2>

<form action="../trip_announce.php" method="post" target="r">
	<table>
		<tr>
			<td>Destination</td>
			<td><input name="destination" type="text" value="Berlin"/></td>
		</tr>
		<tr>
			<td>Current lat.:</td>
			<td><input name="current_lat" type="text" value="48.782954"/></td>
		</tr>
		<tr>
			<td>Current lon.:</td>
			<td><input name="current_lon" type="text" value="9.179733"/></td>
		</tr>
		<tr>
			<td>Available seats:</td>
			<td><input name="avail_seats" type="text" value="3"/></td>
		</tr>
		<tr>
			<td>Buttons:</td>
			<td colspan="2"><input type="submit"/>
				<input type="reset"/></td>
		</tr>
	</table>
</form>

<h2>Announce trip with date and time</h2>

<form action="../trip_announce.php" method="post" target="r">
	<table>
		<tr>
			<td>Destination</td>
			<td><input name="destination" type="text" value="Berlin"/></td>
		</tr>
		<tr>
			<td>Current lat.:</td>
			<td><input name="current_lat" type="text" value="48.782954"/></td>
		</tr>
		<tr>
			<td>Current lon.:</td>
			<td><input name="current_lon" type="text" value="9.179733"/></td>
		</tr>
		<tr>
			<td>Available seats:</td>
			<td><input name="avail_seats" type="text" value="3"/></td>
		</tr>
		<tr>
			<td>Date:</td>
			<td><input name="date" type="text" value="<?php echo (time() + 3600) * 1000; ?>"/> (<?php
				date_default_timezone_set("Europe/Berlin");
				echo strftime("%c",
					time() + 3600); ?>
				)
			</td>
		</tr>
		<tr>
			<td>Buttons:</td>
			<td colspan="2"><input type="submit"/>
				<input type="reset"/></td>
		</tr>
	</table>
</form>


<h2>Update data</h2>

<form action="../trip_update_data.php" method="post" target="r">
	<table>
		<tr>
			<td>Trip-ID:</td>
			<td><input name="id" type="text" value="0"/></td>
		</tr>
		<tr>
			<td>Avail. seats:</td>
			<td><input name="avail_seats" type="text" value="1"/></td>
		</tr>
		<tr>
			<td>Buttons:</td>
			<td colspan="2"><input type="submit"/>
				<input type="reset"/></td>
		</tr>
	</table>
</form>

<h2>Get trip's overview</h2>

<form action="../trip_overview.php" method="post" target="r">
	<table>
		<tr>
			<td>Trip-ID:</td>
			<td><input name="tripId" type="text" value="0"/></td>
		</tr>
		<tr>
			<td>Buttons:</td>
			<td><input type="submit"/>
				<input type="reset"/></td>
		</tr>
	</table>
</form>


<h2><a href="../trip_get_open.php" target="r">Get current trip</a></h2>

<h2><a href="../trip_get_all.php" target="r">Get current and future trips</a></h2>

<h2><a href="../trip_get_all.php?compact" target="r">Get current and future trips (compact)</a></h2>

<h2><a href="../trip_get_all.php?all" target="r">Get all trips</a></h2>

<h2>End/Close trip</h2>

<form action="../trip_end.php" method="post" target="r">
	<table>
		<tr>
			<td>Trip-ID:</td>
			<td><input name="id" type="text"/></td>
		</tr>
		<tr>
			<td>Buttons:</td>
			<td colspan="2"><input type="submit"/>
				<input type="reset"/></td>
		</tr>
	</table>
</form>


<h2><a href="../trip_end.php" target="r">End/Close trip without ID</a></h2>


<h2>Search for requests</h2>

<form action="../query_search.php" method="post" target="r">
	<table>
		<tr>
			<td>Current lat.</td>
			<td><input name="lat" type="text" value="48.783173"/></td>
		</tr>
		<tr>
			<td>Current lon.</td>
			<td><input name="lon" type="text" value="9.181051"/></td>
		</tr>
		<tr>
			<td>Distance</td>
			<td><input name="distance" type="text" value="5000"/></td>
		</tr>
		<tr>
			<td>Buttons</td>
			<td colspan="2"><input type="submit"/>
				<input type="reset"/></td>
		</tr>
	</table>
</form>

<h2>Pick up user</h2>

<form action="../pick_up.php" method="post" target="r">
	<table>
		<tr>
			<td>User that should be picked up</td>
			<td><input name="user_id" type="text" value="0"/></td>
		</tr>
		<tr>
			<td>Buttons</td>
			<td colspan="2"><input type="submit"/>
				<input type="reset"/></td>
		</tr>
	</table>
</form>

<h2>Get my lifts</h2>

<form action="../load_my_lifts.php" method="post" target="r">
	<table>
		<tr>
			<td>My Id:</td>
			<td><input name="uid" type="text" value="0"/></td>
		</tr>
		<tr>
			<td>Buttons</td>
			<td colspan="2"><input type="submit"/>
				<input type="reset"/></td>
		</tr>
	</table>
</form>


<h2 class="auto-style1">Update position</h2>

<div>Deprecated.
	<a href="user.html#updatepos">Use this instead</a>!
</div>
<form action="../trip_update_pos.php" method="post" target="r">
	<table>
		<tr>
			<td>Trip-ID:</td>
			<td><input name="id" type="text" value="0"/></td>
		</tr>
		<tr>
			<td>Current lat.:</td>
			<td><input name="current_lat" type="text" value="48.782409"/></td>
		</tr>
		<tr>
			<td>Current lon.:</td>
			<td><input name="current_lon" type="text" value="9.183648"/></td>
		</tr>
		<tr>
			<td>Buttons:</td>
			<td colspan="2"><input type="submit"/>
				<input type="reset"/></td>
		</tr>
	</table>
</form>
</body>
</html>
