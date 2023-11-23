<?php

require_once 'dbconnection.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['full_name']) && isset($_POST['user_name'])  && isset($_POST['phone_no']) && isset($_POST['password']) ){

            $fullname= $_POST['full_name'];
            $phonenumber= $_POST['phone_no'];
            $username = $_POST['user_name'];
            $password = $_POST['password'];
            $userid= $_POST['userid'];


            $query = "UPDATE user SET full_name = :fullname, phone_no = :phonenumber, username = :username, password = :password WHERE id = :userid";
            $statement = $pdo->prepare($query);
            $statement->bindValue(':fullname', $fullname);
            $statement->bindValue(':phonenumber', $phonenumber);
            $statement->bindValue(':username', $username);
            $statement->bindValue(':password', $password);
            $statement->bindValue(':userid', $userid);
            $statement->execute();

            $response['error'] = false;
            $response['message'] = 'updated successfully';
            echo json_encode($response);

    } else {
        $response['error'] = true;
        $response['message'] = 'missing parameters';
        echo json_encode($response);
    }
} else{
    $response['error'] = true;
    $response['message'] = 'invalid request';
    echo json_encode($response);
}

// Close the PDO connection
$pdo = null;
?>