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
session_start();
include("connect_to_db.php");
include("bargraph.class.php"); 
include("rides_per_x.class.php");
//include("update.php");
require_once ('src/jpgraph.php');
require_once ('src/jpgraph_bar.php');

$sql = "SELECT COUNT(*) FROM dev_trip";  
$result = mysql_query ($sql);  

if (mysql_num_rows ($result) > 0)  
{  
  //Anzahl der Fahrten
  $data = mysql_fetch_array ($result);  
  $rides = $data[0];

  // Anzahl der offenen Fahrten
  $sql = "SELECT COUNT(*) FROM dev_trip WHERE ending='0000-00-00 00:00:00'";  
  $result = mysql_query ($sql);  
  $data = mysql_fetch_array($result);
  $open_rides = $data[0];
  
  // Anzahl der abgeschloßenen Fahrten
  $sql = "SELECT COUNT(*) FROM dev_trip WHERE ending!='0000-00-00 00:00:00'";
  $result = mysql_query ($sql);  
  $data = mysql_fetch_array($result);  
  $closed_rides = $data[0];

  // Anzahl der Mitglieder
  $sql = "SELECT COUNT(*) FROM dev_user";  
  $result = mysql_query ($sql);  
  $data = mysql_fetch_array($result);
  $users = $data[0];
  
  $array_month_rides = RidesPerx::getRidesPerMonth();
  $array_year_rides =  RidesPerX::getRidesPerYear();
}

$data1y = $array_month_rides;

// Create the graph. These two calls are always required
$graph = new Graph(450,200,'auto');
$graph->SetScale("textlin");

$theme_class=new UniversalTheme;
$graph->SetTheme($theme_class);

$graph->yaxis->SetTickPositions(array(0,1,2,3,4,5,6,7,8,9,10), array(0));
$graph->SetBox(false);

$graph->ygrid->SetFill(false);
$graph->xaxis->SetTickLabels(array('Jan','Feb','Mär','Apr', 'Mai','Jun','Jul','Aug','Sep','Okt','Nov', 'Dez'));
$graph->yaxis->HideLine(false);
$graph->yaxis->HideTicks(false,false);

// Create the bar plots
$b1plot = new BarPlot($data1y);

// ...and add it to the graPH
$graph->Add($b1plot);


$b1plot->SetColor("white");
$b1plot->SetFillColor("#cc1111");

$graph->title->Set("Fahrten pro Monat");

// Display the graph
$gdImgHandler = $graph->Stroke(_IMG_HANDLER);
// Output line
// Default is PNG so use ".png" as suffix
$fileName = "imagefile.png";
$graph->img->Stream($fileName);


//---------------------------------------

$data1y1 = $array_year_rides;

// Create the graph. These two calls are always required
$graph1 = new Graph(450,200,'auto');
$graph1->SetScale("textlin");

$theme_class1=new UniversalTheme;
$graph1->SetTheme($theme_class1);

$graph1->yaxis->SetTickPositions(array(0,1,2,3,4,5,6,7,8,9,10), array(0));
$graph1->SetBox(false);

$graph1->ygrid->SetFill(false);
$graph1->xaxis->SetTickLabels(array('2010','2011','2012','2013', '2014','2015','2016','2017','2018','2019'));
$graph1->yaxis->HideLine(false);
$graph1->yaxis->HideTicks(false,false);

// Create the bar plots
$b1plot1 = new BarPlot($data1y1);

// ...and add it to the graPH
$graph1->Add($b1plot1);


$b1plot1->SetColor("white");
$b1plot1->SetFillColor("#cc1111");

$graph1->title->Set("Fahrten pro Jahr");

// Display the graph
$gdImgHandler1 = $graph1->Stroke(_IMG_HANDLER);
// Output line
// Default is PNG so use ".png" as suffix
$fileName1 = "imagefile1.png";
$graph1->img->Stream($fileName1);

?>

<html>  
<head>  
  <title>Statistik</title>  
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
			<h1>Statistik</h1>
<hr align='left' width = 40%>
	<table align='center'>
	<tr><td>Alle Fahrten: </td><td> <?php echo $rides; ?></td></tr>
	<tr><td>Offene Fahrten: </td><td> <?php echo $open_rides; ?> </td></tr>
	<tr><td>Abgeschloßene Fahrten: </td><td> <?php echo $closed_rides; ?> </td></tr>

	<tr><td>durchschnittliche Fahrtzeit: </td><td> <?php $array = array(); $array = RidesPerX::getAvgRideTime(); 
	echo intval($array[0])." Tage ".intval($array[1])." Monate ".intval($array[2])." Jahre ".intval($array[3])." Stunden ".$array[4]." Minuten";?> </td></tr>
	<tr><td colspan = '2' align = 'center'> <img src="imagefile.png?"/></td></tr>
	<tr><td colspan = '2' align = 'center'> <img src="imagefile1.png?"/></td></tr>
	<tr><td>Anzahl der Mitglieder: </td><td> <?php echo $users; ?> </td></tr>
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
