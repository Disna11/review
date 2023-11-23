<?php

require_once 'dbconnection.php';


if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    $review_id= $_POST['review_id'];
    $user_id= $_POST['user_id'];
    $messages= $_POST['message'];

    $query = "UPDATE reviews SET messages = :messages WHERE reviewid = :review_id AND user_id = :user_id";
    $statement = $pdo->prepare($query);
    $statement->bindValue(':messages', $messages);
    $statement->bindValue(':review_id', $review_id);
    $statement->bindValue(':user_id', $user_id);
    $statement->execute();

    $result =  $statement->rowCount();

    if ($result > 0){
        $response['error'] = false;
        $response['message'] = 'replay posted';
        $response['data'] = null;
    }

    echo json_encode($response);


}

// Close the PDO connection
$pdo = null;
?>