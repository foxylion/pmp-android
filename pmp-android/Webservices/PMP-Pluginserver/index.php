<?php

	if (isset($_GET['q'])) {
		
		// remember inputs might be evil
		$query = ereg_replace('[^A-Za-z0-9]', '', $_GET['q']);
	
		// wow, that was some complex coding
		foreach (glob('*'.$query.'*.apk') as $rg) {
			printf('%s\n', $rg);
		}				
		// in the future we could read all the XMLs and search in there as well
	}