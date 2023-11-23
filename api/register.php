<?php

require_once 'dbconnection.php';


if ($_SERVER['REQUEST_METHOD'] === 'POST') {
   
        if (isset($_POST['fullname']) && isset($_POST['email'])  && isset($_POST['phonenumber']) && isset($_POST['username']) && isset($_POST['password']) ) {
            $fullname= $_POST['fullname'];
            $email= $_POST['email'];
            $phonenumber= $_POST['phonenumber'];
            $username = $_POST['username'];
            $password = $_POST['password'];



            //check if email provided is already exist in the database


            $query = "SELECT * FROM user WHERE email = :email";
            $statement = $pdo->prepare($query);
            $statement->bindValue(':email', $email);
            $statement->execute();
            $result = $statement->fetch(PDO::FETCH_ASSOC);

            //if the given email is not in the database perform registration activity

            if ($result == false) {
               
            // Perform the register operation
            $query = "INSERT INTO user ( full_name, email, phone_no, username, password) VALUES (:fullname, :email, :phonenumber, :username, :password)";
            $statement = $pdo->prepare($query);
            $statement->bindValue(':fullname', $fullname);
            $statement->bindValue(':email', $email);
            $statement->bindValue(':phonenumber', $phonenumber);
            $statement->bindValue(':username', $username);
            $statement->bindValue(':password', $password);
            $statement->execute();
           
            $response['error'] = false;
            $response['message'] = 'Registration Successfull';
            echo json_encode($response);
            
            }

            //if the email given is already in the database set the error true
            else
            {
                $response['error'] = true;
            $response['message'] = 'Email already exist';
            echo json_encode($response);
            }
            
        } else {
            $response['error'] = true;
            $response['message'] = 'Required parameters are missing';
            echo json_encode($response);
        }
} 
 else {
    $response['error'] = true;
    $response['message'] = 'Invalid request method';
    echo json_encode($response);
}

// Close the PDO connection
$pdo = null;
?>
