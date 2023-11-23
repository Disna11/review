<?php

require_once 'dbconnection.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    $review_id= $_POST['review_id'];
    $user_id= $_POST['user_id'];
    $newreview= $_POST['update_review'];

    $query = "UPDATE reviews SET review = :newreview WHERE reviewid = :review_id AND user_id = :user_id";
    $statement = $pdo->prepare($query);
    $statement->bindValue(':newreview', $newreview);
    $statement->bindValue(':review_id', $review_id);
    $statement->bindValue(':user_id', $user_id);
    $statement->execute();

    $result =  $statement->rowCount();

    if ($result > 0){
        $response['error'] = false;
        $response['message'] = 'updated';
        $response['data'] = null;
    }else{
        $response['error'] = true;
        $response['message'] = 'something went wrong';
        $response['data'] = null;
    }
    
    echo json_encode($response);

}


// Close the PDO connection
$pdo = null;
?>