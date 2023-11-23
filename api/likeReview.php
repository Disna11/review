<?php

require_once 'dbconnection.php';

if(isset($_GET['review_id'])){
    $review_id= $_GET['review_id'];

// Retrieve the current like count from the database
$query = "SELECT `like` FROM reviews WHERE reviewid = :review_id";
$statement = $pdo->prepare($query);
$statement->bindValue(':review_id', $review_id);
$statement->execute();
$currentLikeCount = $statement->fetchColumn();

// Increment the like count
$newLikeCount = $currentLikeCount + 1;

// Update the like count in the database
$query = "UPDATE reviews SET `like` = :newLikeCount WHERE reviewid = :review_id";
$statement = $pdo->prepare($query);
$statement->bindValue(':newLikeCount', $newLikeCount);
$statement->bindValue(':review_id', $review_id);
$statement->execute();

$response['error'] = false;
$response['message'] = 'Liked';
$response['data'] =$newLikeCount;

echo json_encode($response);
}

// Close the PDO connection
$pdo = null;
?>