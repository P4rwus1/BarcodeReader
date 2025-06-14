<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "urunler"; 

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Bağlantı hatası: " . $conn->connect_error);
}

$barcode = $_POST['barcode'] ?? '';
$name = $_POST['name'] ?? '';
$price = $_POST['price'] ?? '';
$stock = $_POST['stock'] ?? '';
$category = $_POST['category'] ?? '';


$sql = "UPDATE products SET name=?, price=?, stock=?, category=? WHERE barcode=?";
$stmt = $conn->prepare($sql);
if (!$stmt) {
    die("Hazırlama hatası: " . $conn->error);
}

$stmt->bind_param("sdsss", $name, $price, $stock, $category, $barcode);



if ($stmt->execute()) {
    echo "Güncelleme başarılı.";
} else {
    echo "Hata: " . $stmt->error;
}

$stmt->close();
$conn->close();
