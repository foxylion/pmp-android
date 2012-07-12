<?php
include("connect_to_db.php");

class RidesPerX
{
	// Returns an array of 12 elements [0] = january, [11] = december
  static function  getRidesPerMonth(){
    
		$sql = "SELECT creation FROM dev_trip WHERE creation!='0000-00-00 00:00:00'";  
		$result = mysql_query ($sql);  
		$counts = 0;
		$array_month = array();
		$array_year = array();
		$array_result_month = array(0,0,0,0,0,0,0,0,0,0,0,0);
		while($row = mysql_fetch_object($result))
		{
			$string = $row->creation;
			$month_string = substr($string,-14,2);
			$year_string = substr($string, -19,4);
			array_push($array_month, $month_string);			
			array_push($array_year, $year_string);
			
		}	
			
			for($i=0; $i < sizeof($array_month); $i++){
				$array_result_month[intval($array_month[$i])-1]++;
			}
			
			return $array_result_month;
	}
	
	//Returns an array of 10 elements [0] = 2010, [9] = 2020
	static function  getRidesPerYear(){
    
		$sql = "SELECT creation FROM dev_trip WHERE creation!='0000-00-00 00:00:00'";  
		$result = mysql_query ($sql);  
		$counts = 0;
		$array_month = array();
		$array_year = array();
		$array_result_year = array(0,0,0,0,0,0,0,0,0,0);
		while($row = mysql_fetch_object($result))
		{
			$string = $row->creation;
			$month_string = substr($string,-14,2);
			$year_string = substr($string, -19,4);
			array_push($array_month, $month_string);			
			array_push($array_year, $year_string);
			
		}	
			
			for($i=0; $i < sizeof($array_year); $i++){
				$array_result_year[intval($array_year[$i])-2010]++;
			}
			
			return $array_result_year;
	}
	static function getAvgRideTime(){
		$sql = "SELECT creation, ending FROM dev_trip WHERE ending!='0000-00-00 00:00:00'";  
		$result = mysql_query ($sql);  
		$sum_time = new DateInterval("P0Y0DT0H0M0S");
		$format = 'Y-m-d H:i:s';
		$stamp_result=0;
		$count = 0;
		$array = array();
		$sum=0;
		while($row = mysql_fetch_object($result))
		{
			$string_creation = $row->creation;
			$string_ending = $row->ending;
		  $date_creation = DateTime::createFromFormat($format, $string_creation);
		  $date_ending = DateTime::createFromFormat($format, $string_ending);
			$stamp = 				strtotime($string_ending) - strtotime( $string_creation);
			$stamp_result = $stamp_result + $stamp;
			$count ++;
			$sum_time = date_diff($date_ending, $date_creation);
			$string = var_export($sum_time, true);
			$new_str = substr($string, 33,-4);
			$array = explode(",", $new_str);
			for($i=0; $i < sizeof($array); $i++){
				
				$array[$i] = explode("=>", $array[$i]);
			}
			$sum = $sum + RidesPerX::ArrayToMinutes($array);

		}
			 return RidesPerX::MinutesToInt($sum/$count);
	}
	
	private static function ArrayToMinutes($date){
		$const_mins_in_year = 525600;//365 *24*60 
		$const_mins_in_month= 43200;//30*24*60
		$const_mins_in_day = 1440;//24*60
		$const_mins_in_hour= 60;
		
		$int = array();
		$result = 0;
		for($i=0; $i < 6; $i++){
				array_push($int,$date[$i][1]);
		}
		$result = $int[0]*$const_mins_in_year + $result;
		$result = $int[1]*$const_mins_in_month + $result;
		$result = $int[2]*$const_mins_in_day + $result;
		$result = $int[3]*$const_mins_in_hour + $result;
		$result = $int[4] + $result;
		if($int[5]>30){
			$result = $result + 1;
		}
		return $result;
	}
	
	private static function MinutesToInt($minutes){

		
		$const_mins_in_year = 525600;//365 *24*60 
		$const_mins_in_month= 43200;//30*24*60
		$const_mins_in_day = 1440;//24*60
		$const_mins_in_hour= 60;
		$years = intval($minutes/$const_mins_in_year);
		$months = intval(($minutes-$years*$const_mins_in_year)/$const_mins_in_month);
		$days = intval(($minutes-$years*$const_mins_in_year-$months*$const_mins_in_month)/$const_mins_in_day);
		$hours = intval(($minutes-$years*$const_mins_in_year-$months*$const_mins_in_month-$days*$const_mins_in_day)/$const_mins_in_hour);
		$minutes = intval($minutes-$years*$const_mins_in_year-$months*$const_mins_in_month-$days*$const_mins_in_day-$hours*$const_mins_in_hour);

		$array = array();
		array_push($array, $days);
		array_push($array, $months);
		array_push($array, $years);
		array_push($array, $hours);
		array_push($array, $minutes);
		//echo intval($days)." Tage ".intval($months)." Monate ".intval($years)." Jahre ".intval($hours)." Stunden ".$minutes." Minuten";
		return $array;
		
	}
}
