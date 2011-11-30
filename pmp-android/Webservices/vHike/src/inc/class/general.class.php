<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * 
 */

class General {

    /**
     * Generates a random string.
     * Usefull for greating passwords or activation strings  
     *   
     * @param    $length   Length of the generated string
     * @return   Generated string 
     */
    public static function randomString($length) {
        $chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        mt_srand((double) microtime() * 1000000);
        $string = null;
        while (strlen($string) < $length) {
            $string .= substr($chars, mt_rand(0, strlen($chars) - 1), 1);
        }
        return $string;
    }

}

?>
