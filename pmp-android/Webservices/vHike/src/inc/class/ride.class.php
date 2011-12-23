<?php
if (!defined("INCLUDE")) {
    exit;
}

class Ride {
    
    private $id = -1;
    private $passengerId = -1;
    /**
     *
     * @var Trip 
     */
    private $trip = null;
    
    /**
     *
     * @param int $passenger
     * @return boolean 
     */
    public function setPassengerId($passenger) {
        if (General::validId($passenger)) {
            $this->passengerId = $passenger;
            return true;
        } else {
            return false;
        }
    }
}

class Rides {
    /**
     *
     * @var Trip 
     */
    private $trip;
    
    /**
     *
     * @var Ride[] 
     */
    private $rides;
}
?>
