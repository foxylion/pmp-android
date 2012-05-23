<?php

/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp-Webservice
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

namespace infoapp;

if (!defined("INCLUDE")) {
    exit;
}

/**
 * The class loader is used to automatically load classes that are requested
 * by the new-statement. The class' name is used as file name, with the extension
 * ".class.php" added, whereas the namespace is used as the file's path.
 *
 * @author Patrick Strobel
 * @version 4.0.0
 * @package infoapp
 */
class ClassLoader {

    private static $baseDir = __DIR__;

    public static function register() {
        spl_autoload_register(__CLASS__ . "::load");
    }

    public static function load($class) {
        $classpath = explode("\\", $class);
        // Cancel if class has another namespace as classloader
        if (count($classpath) <= 1 || $classpath[0] != __NAMESPACE__) {
            return false;
        }

        // Use namesapces (without the parent namespace) and filename as path
        $path = self::$baseDir . DIRECTORY_SEPARATOR .
                implode(DIRECTORY_SEPARATOR, array_slice($classpath, 1)) . ".class.php";


        // echo "load: ".$class . " path: $path <br>";
        // Cancel if file does not exist
        if (!file_exists($path)) {
            echo "<b>Cannot load class:</b> " . $path . "<br />\n";
            return false;
        }

        require_once($path);
    }

}

?>
