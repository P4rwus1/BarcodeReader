<?php
header('Content-Type: application/json');

$host = "localhost";
$user = "root";
$password = "";
$db = "urunler";

$conn = new mysqli($host, $user, $password, $db);

if ($conn->connect_error) {
    echo json_encode(["error" => "Connection failed: " . $conn->connect_error]);
    exit;
}

$barcode = $_GET['barcode'];

$sql = "SELECT * FROM products WHERE barcode='$barcode'";
$result = $conn->query($sql);

$products = [];

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $products[] = $row;
    }
}

echo json_encode($products);
$conn->close();
?>
