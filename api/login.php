<?php

require_once 'dbconnection.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
   
   
        
            $username = $_POST['username'];
            $password = $_POST['password'];

            // Perform the login operation
            $query = "SELECT * FROM user WHERE username = :username AND password = :password";
            $statement = $pdo->prepare($query);
            $statement->bindValue(':username', $username);
            $statement->bindValue(':password', $password);
            $statement->execute();

            // Fetch the first row from the result
            $result = $statement->fetch(PDO::FETCH_ASSOC);
            
// if result is not false then set error false else set error true
            if ($result !== false) {
                $response['error'] = false;
                $response['message'] = 'Login successful';
                $response['data'] = $result;
            } else {
                $response['error'] = true;
                $response['message'] = 'Invalid username or password';
                $response['data'] = null;
            }
     

} 
 else {
    $response['error'] = true;
    $response['message'] = 'Invalid request method';
}

// Convert the response array to JSON format
echo json_encode($response);

// Close the PDO connection
$pdo = null;
?>
