<?php

require_once 'dbconnection.php';


if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    if (isset($_POST['review']) && isset($_POST['user_id'])){

        $review= $_POST['review'];
        $user_id= $_POST['user_id'];

        $query = " INSERT INTO reviews ( user_id, review) VALUES (:user_id, :review)";
        $statement = $pdo->prepare($query);
        $statement->bindValue(':user_id', $user_id);
        $statement->bindValue(':review', $review);
        $statement->execute();

        $response['error'] = false;
        $response['message'] = 'Review Posted successfully';
        echo json_encode($response);


    }else {
        $response['error'] = true;
        $response['message'] = 'missing parameters';
        echo json_encode($response);}


} else {
    $response['error'] = true;
    $response['message'] = 'Invalid request method';
    echo json_encode($response);
}



// Close the PDO connection
$pdo = null;
?>