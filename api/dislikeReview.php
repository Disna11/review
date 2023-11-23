<?php

require_once 'dbconnection.php';

if(isset($_GET['review_id'])){
    $review_id= $_GET['review_id'];

// Retrieve the current like count from the database
$query = "SELECT `dislike` FROM reviews WHERE reviewid = :review_id";
$statement = $pdo->prepare($query);
$statement->bindValue(':review_id', $review_id);
$statement->execute();
$currentdisLikeCount = $statement->fetchColumn();

// Increment the like count
$newdisLikeCount = $currentdisLikeCount + 1;

// Update the like count in the database
$query = "UPDATE reviews SET `dislike` = :newdisLikeCount WHERE reviewid = :review_id";
$statement = $pdo->prepare($query);
$statement->bindValue(':newdisLikeCount', $newdisLikeCount);
$statement->bindValue(':review_id', $review_id);
$statement->execute();

$response['error'] = false;
$response['message'] = 'Disliked';
$response['data'] =$newdisLikeCount;

echo json_encode($response);
}

// Close the PDO connection
$pdo = null;
?>