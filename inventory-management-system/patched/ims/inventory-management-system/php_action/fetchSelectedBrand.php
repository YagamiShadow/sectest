<?php
header('Content-type: application/json');
header("x-content-type-options: nosniff");
require_once 'core.php';

$brandId = $_POST['brandId'];

$sql = "SELECT brand_id, brand_name, brand_active, brand_status FROM brands WHERE brand_id = $brandId";
$result = mysqli_query($conn, $sql);

if(mysqli_num_rows($result) > 0) { 
 $row =  mysqli_fetch_array($result);
 $row[1] = htmlentities($row[1]);
 $row["brand_name"] = htmlentities($row["brand_name"]);
} // if num_rows

mysqli_close($conn);

echo json_encode($row);