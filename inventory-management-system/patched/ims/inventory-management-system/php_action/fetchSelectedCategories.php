<?php
header('Content-type: application/json');
header("x-content-type-options: nosniff");
require_once 'core.php';

$categoriesId = $_POST['categoriesId'];

$sql = "SELECT categories_id, categories_name, categories_active, categories_status FROM categories WHERE categories_id = $categoriesId";
$result = mysqli_query($conn, $sql);

if(mysqli_num_rows($result) > 0) { 
 $row =  mysqli_fetch_array($result);
 $row[1] = htmlentities($row[1]);
 $row["categories_name"] = htmlentities($row["categories_name"]);
} // if num_rows

mysqli_close($conn);

echo json_encode($row);