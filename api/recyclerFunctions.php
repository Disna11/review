<?php

require_once 'dbconnection.php';



if(isset($_GET['op'])){
    
   if(isset($_GET['likeButtonClicked'])){

        $review_id= $_POST['review_id'];

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
    $response['message'] = 'updated';
    $response['data'] =$newLikeCount;

    echo json_encode($response);

  }

  if(isset($_GET['dislikeButtonClicked'])){

            $review_id= $_POST['review_id'];

            // Retrieve the current DISlike count from the database
        $query = "SELECT `dislike` FROM reviews WHERE reviewid = :review_id";
        $statement = $pdo->prepare($query);
        $statement->bindValue(':review_id', $review_id);
        $statement->execute();
        $currentdisLikeCount = $statement->fetchColumn();

        // Increment the dislike count
        $newdisLikeCount = $currentdisLikeCount + 1;

        // Update the dislike count in the database
        $query = "UPDATE reviews SET `dislike` = :newdisLikeCount WHERE reviewid = :review_id";
        $statement = $pdo->prepare($query);
        $statement->bindValue(':newdisLikeCount', $newdisLikeCount);
        $statement->bindValue(':review_id', $review_id);
        $statement->execute();

        $response['error'] = false;
        $response['message'] = 'updated';
        $response['data'] =$newdisLikeCount;

        echo json_encode($response);

    }

    if(isset($_GET['deleteButtonClicked'])){

                $review_id= $_POST['review_id'];
                $user_id= $_POST['user_id'];

                // perform delete operation
            $query = "DELETE FROM  reviews WHERE reviewid = :review_id AND user_id= :user_id ";
            $statement = $pdo->prepare($query);
            $statement->bindValue(':review_id', $review_id);
            $statement->bindValue(':user_id', $user_id);
            $statement->execute();

            $result =  $statement->rowCount();
            if ($result > 0){
                $response['error'] = false;
                $response['message'] = 'deleted';
                $response['data'] = null;
            }else{
                $response['error'] = true;
                $response['message'] = 'only auther can delete a review';
                $response['data'] = null;
            }
            

            echo json_encode($response);

      }

 }

// Close the PDO connection
$pdo = null;
?>