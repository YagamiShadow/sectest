<?php
header('Content-type: application/json');
header("x-content-type-options: nosniff");
require_once 'core.php';


$valid['success'] = array('success' => false, 'messages' => array());

$brandId = $_POST['brandId'];

if($brandId) { 

 $sql = "UPDATE brands SET brand_status = 2 WHERE brand_id = {$brandId}";

 if(mysqli_query($conn, $sql) === TRUE) {
 	$valid['success'] = true;
	$valid['messages'] = "Successfully Removed";		
 } else {
 	$valid['success'] = false;
 	$valid['messages'] = "Error while remove the brand";
 }
 
 mysqli_close($conn);

 echo json_encode($valid);
 
} // /if $_POST