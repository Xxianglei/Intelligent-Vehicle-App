<?php
	header("Content-type:text/html; charset=utf-8");
	require_once'./Db/db.php';
	$db = new Db();
	$link = $db->connect();
	$data = array(
		'user' => $_POST['user'],
		'name' =>$_POST['name'],
		'phone' => $_POST['phone'],
	);
	
	require_once'./User/add.php';
	$add = new Add();
	
	$result = $add->link($data['user'],$data['name'],$data['phone'],$link);
	require_once'./Response/response.php';
	$response = new Response();
	if ($result == 601) {
		$response->json(601,'联系人信息录入成功',$data);
	} else {
		
		$response->json(600,"联系人信息录入失败",$data);
	}
	
?>