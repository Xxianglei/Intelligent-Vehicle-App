<?php
	header("Content-type:text/html; charset=utf-8");
	require_once'./Db/db.php';
	$db = new Db();
	$link = $db->connect();
		
		$phone = $_POST['phone'];
		$password = $_POST['password'];
		
		$sql = "INSERT into loginuser(phone,password) values('{$phone}','{$password}')";
		
		$result = mysql_query($sql,$link);
		require_once'./Response/response.php';
		$response = new Response();
		$data = array(
			'phone' => $phone,
			'password' => $password
		);
		
		if ($result) {
			$response->json(101,"注册成功",$data);
		} else {
			$response->json(100,"注册失败",$data);
		}
		
?>