<?php
	header("Content-type:text/html; charset=utf-8");
	require_once'./Db/db.php';
	$db = new Db();
	$link = $db->connect();
	$data = array(
		'userName' => $_POST['userName'],
		'sex' =>$_POST['sex'],
		'height' => $_POST['height'],
		'weight' =>$_POST['weight'],
		'age' => $_POST['age'],
		'img' => $_POST['img'],
		'user'=>$_POST['user']
	);
	
	require_once'./User/add.php';
	$add = new Add();
	
	$result = $add->user($data['userName'],$data['sex'],$data['height'],$data['weight'],$data['age'],$data['img'],$data['user'],$link);
	require_once'./Response/response.php';
	$response = new Response();
	if ($result == 301) {
		$response->json(301,'用户信息录入成功',$data);
	} else {
		
		$response->json(300,"用户信息录入失败",$data);
	}
	
?>