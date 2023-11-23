<?php

require_once 'dbconnection.php';


if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // if the method is post fetch all data from the table reviews and store it in result
    $query = "SELECT * FROM reviews"; 
    $statement = $pdo->prepare($query);
    $statement->execute();

    $result = $statement->fetchAll(PDO::FETCH_ASSOC);
// if result is not false the set error false and message else set error true
    if ($result !== false) {
        $response['error'] = false;
        $response['message'] = 'all reviews displayed';
        $response['data'] = $result;
        echo json_encode($response);
    }  else {
        $response['error'] = true;
        $response['message'] = 'something went wrong';
        $response['data'] = null;
        echo json_encode($response);
    }
}  else {
    $response['error'] = true;
    $response['message'] = 'Invalid request method';
    echo json_encode($response);
}

   


// Close the PDO connection
$pdo = null;
?>