<?php

header('Content-Type: application/json'); // Set the response header to JSON
header('Access-Control-Allow-Origin: http://127.0.0.1:5501');
$host = 'localhost';
$user_name = 'root';
$pass = '';
$db = 'review';

global $pdo;
try {
    $pdo = new PDO("mysql:host=$host;dbname=$db;", $user_name, $pass);
} catch (PDOException $e) {
    echo json_encode(array('error' => true, 'message' => 'Database connection error'));
    exit;
}

$response = array();
?>