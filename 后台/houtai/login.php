<?php
	header("Content-type:text/html; charset=utf-8");
	require_once'./Db/db.php';
	$db = new Db();
	$link = $db->connect();
	
	$phone = $_POST['phone'];
	$password = $_POST['password'];
	
	require_once'./User/select.php';
	$select = new Select();
	
	$result = $select->login($phone,$password,$link);
	require_once'./Response/response.php';
	$response = new Response();
	$data = array(
		'phone' =>$phone,
		'password' => $password
	);
	if ($result == 0) {
		$response->json(0,"查找失败！请修改",$data);
	}else {
		if ($result == 201) {
			$response->json(201,"登录成功",$data);
		} else {
			$response->json(200,"登录失败",$data);
		}
	}
	
	
	
	
	
?>