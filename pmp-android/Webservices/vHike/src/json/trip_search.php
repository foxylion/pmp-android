<?php
/**
 * Find trips with specific characters
 */
define('INCLUDE', TRUE);
require("./../inc/json_framework.inc.php");

// Stop execution of script and print error message if user is not logged in
Json::printErrorIfNotLoggedIn();

try {
    // Destination
    if (!isset($_POST['destination']))
        throw new InputException('destination');
    $db = Database::getInstance();
    $destination = General::clean_input($_POST['destination']);

    // User id
    $user = Session::getInstance()->getLoggedInUser()->getId();

    // Seat
    $seat = 0;
    if (General::validateId('seat_count'))
        $seat = $_POST['seat_count'];

    // Where date clause
    $where_date = '';
    if (General::validateId('unix_date_1') && General::validateId('unix_date_2'))
        $where_date = "UNIX_TIMESTAMP(trip.creation) BETWEEN {$_POST['unix_date_1']} AND {$_POST['unix_date_2']} AND\n";

    // Where departure clause
    $where_departure = '';
    if (isset($_POST['departure'])) {
        $departure = General::clean_input($_POST['departure']);
        $where_departure = "CONCAT_WS(';', trip.departure, trip.destination) LIKE '%;$departure;%' AND\n";
    }

    // Where username
    $where_username = '';
    if (isset($_POST['username'])) {
        $username = str_replace('*', '%', General::clean_input($_POST['username']));
        $where_username = "`user`.username LIKE '$username' AND\n";
    }

    // User minimal rating
    $where_min_rating = '';
    if (General::validateId('min_rating'))
        $where_min_rating = "user_rating(trip.driver) >= {$_POST['min_rating']} AND\n";

    // Other info
    $search_table = DB_PREFIX . '_trip';
    $select_columns = "trip.id,\n" .
        "trip.driver AS userid,\n" .
        "trip.avail_seats AS seat,\n" .
        "user_rating(trip.driver) as rating,\n" .
        "UNIX_TIMESTAMP(trip.creation)*1000 AS date,\n";
    $where_clause = "trip.ending = 0 AND\n" .
        "trip.driver <> $user AND\n" .
        "trip.avail_seats >= $seat AND\n";
    $join_condition = "trip.driver = `user`.id";
    $where_destination = "trip.destination LIKE '%;$destination;%'";

    // If this is a query search
    if (isset($_POST['search_request'])) {
        $seat = $seat == 0 ? 100 : $seat;
        $search_table = DB_PREFIX . '_query';
        $select_columns = "trip.id,\n" .
            "trip.passenger AS userid,\n" .
            "trip.seats AS seat,\n".
            "user_rating(trip.passenger) as rating,\n" .
            "UNIX_TIMESTAMP(trip.date)*1000 AS date,\n";
        $where_clause = "trip.passenger <> $user AND\n" .
            "trip.seats <= $seat AND\n";
        $join_condition = "trip.passenger = `user`.id";
        $where_destination = "trip.destination LIKE '%$destination%'";
        if (strlen($where_date) > 0)
            $where_date =
                "((UNIX_TIMESTAMP(DATE_ADD(trip.date, INTERVAL trip.tolerance DAY)) BETWEEN {$_POST['unix_date_1']} AND {$_POST['unix_date_2']}) OR\n" .
                "(UNIX_TIMESTAMP(DATE_SUB(trip.date, INTERVAL trip.tolerance DAY)) BETWEEN {$_POST['unix_date_1']} AND {$_POST['unix_date_2']}) OR\n" .
                "(UNIX_TIMESTAMP(DATE_SUB(trip.date, INTERVAL trip.tolerance DAY)) < {$_POST['unix_date_1']} AND UNIX_TIMESTAMP(DATE_ADD(trip.date, INTERVAL trip.tolerance DAY)) > {$_POST['unix_date_2']})) AND\n";
    }

    // Build the query
    $query = "SELECT\n" .
        $select_columns .
        "trip.departure,\n" .
        "trip.destination,\n" .
        "`user`.username\n" .
        "FROM\n" .
        $search_table . " AS trip\n" .
        "INNER JOIN " . DB_PREFIX . "_user AS `user` ON $join_condition\n" .
        "WHERE\n" .
        $where_clause .
        $where_date .
        $where_departure . $where_min_rating . $where_username . $where_destination;

    $output = array();

    $result = $db->query($query);
    while ($row = $db->fetch($result)) {
        settype($row['id'], 'integer');
        settype($row['userid'], 'integer');
        settype($row['seat'], 'integer');
        settype($row['date'], 'integer');
        settype($row['rating'], 'float');
        $output[] = $row;
    }

    echo Json::arrayToJson(array('trips' => $output
    , 'query' => $query // for debug purpose
    ));

} catch (InvalidArgumentException $iae) {
    Json::printInvalidInputError();
} catch (DatabaseException $de) {
    Json::printDatabaseError($de);
}
Database::getInstance()->disconnect();
// EOF