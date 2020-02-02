<?php
header('Content-type: application/json');
header("x-content-type-options: nosniff");
require_once 'core.php';

$orderId = $_POST['orderId'];

$valid = array('order' => array(), 'order_item' => array());

$sql = "SELECT orders.order_id, orders.order_date, orders.client_name, orders.client_contact, orders.sub_total, orders.vat, orders.total_amount, orders.discount, orders.grand_total, orders.paid, orders.due, orders.payment_type, orders.payment_status FROM orders 	
	WHERE orders.order_id = {$orderId}";

$result = mysqli_query($conn, $sql);
$data = mysqli_fetch_row($result);
foreach (array(2, 3, 4, 5, 6, 7, 8, 9, 10) as $i){
	$data[$i] = htmlentities($data[$i]);
}	
$valid['order'] = $data;


mysqli_close($conn);

echo json_encode($valid);