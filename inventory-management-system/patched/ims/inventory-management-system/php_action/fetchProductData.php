<?php
header('Content-type: application/json');
header("x-content-type-options: nosniff");
require_once 'core.php';

$sql = "SELECT product_id, product_name FROM product WHERE status = 1 AND active = 1";
$result = mysqli_query($conn, $sql);

$data = mysqli_fetch_all($result);
foreach ($data as $k => $v){
	$data[$k][1] = htmlentities($v[1]);
}

mysqli_close($conn);

echo json_encode($data);