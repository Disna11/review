<?php
require_once 'dbconnection.php';

if(isset($_GET['review_id'])&&isset($_GET['user_id'])){
                $review_id= $_GET['review_id'];
                $user_id= $_GET['user_id'];

                // perform delete operation
            $query = "DELETE FROM  reviews WHERE reviewid = :review_id AND user_id= :user_id ";
            $statement = $pdo->prepare($query);
            $statement->bindValue(':review_id', $review_id);
            $statement->bindValue(':user_id', $user_id);
            $statement->execute();

            $result =  $statement->rowCount();
            if ($result > 0){
                $response['error'] = false;
                $response['message'] = 'Deleted';
                $response['data'] = null;
            }else{
                $response['error'] = true;
                $response['message'] = 'Only auther can delete a review';
                $response['data'] = null;
            }
            

            echo json_encode($response);

}

            
?>