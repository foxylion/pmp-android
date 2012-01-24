<?php

	if (isset($_GET['q'])) {
		
		// remember inputs might be evil
		$query = ereg_replace('[^A-Za-z0-9,.]', '', $_GET['q']);
		
		$query = explode(",", $query);
		
		// wow, that was some complex coding
		foreach (glob('*.xml') as $rg) {
			$found = false;
			foreach ($query AS $q) {
				if(stripos($rg, $q) !== false) {
					$found = true;
					break;
				}
			}
			
			if($found || ($query[0] == "" && count($query) == 1)) {
				printf("%s\n", basename($rg, '.xml'));
			}
		}
		// in the future we could read all the XMLs and search in there as well
	}