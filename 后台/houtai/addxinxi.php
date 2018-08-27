<?php
	header("Content-type:text/html; charset=utf-8");
	require_once'./Db/db.php';
	$db = new Db();
	$link = $db->connect();
	$data = array(
		'name' => $_POST['name'],
		'shengao' =>$_POST['shengao'],
		'tizhong' => $_POST['tizhong'],
		'bingshi' =>$_POST['bingshi'],
		'bron' => $_POST['bron'],
		'xuexing' => $_POST['xuexing'],
		'guoming' => $_POST['guoming'],
		'xiguan' => $_POST['xiguan'],
		'user' =>$_POST['user']
	);
	
	require_once'./User/add.php';
	$add = new Add();
	
	$result = $add->xinxi($data['name'],$data['shengao'],$data['tizhong'],$data['bingshi'],$data['bron'],$data['xuexing'],$data['guoming'],$data['xiguan'],$data['user'],$link);
	require_once'./Response/response.php';
	$response = new Response();
	if ($result == 501) {
		$response->json(501,'用户信息录入成功',$data);
	} else {
		$response->json(500,"用户信息录入失败",$data);
	}
	
?>