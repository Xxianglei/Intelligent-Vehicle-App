<?php
	header("Content-type:text/html; charset=utf-8");
	require_once'./Db/db.php';
	$db = new Db();
	$link = $db->connect();
	$data = array(
		'user' => $_POST['user'],
		'tiwen' =>isset($_POST['tiwen'])?$_POST['tiwen']:"",
		'xinlv' => isset($_POST['xinlv'])?$_POST['xinlv']:"",
		'xueyang' => isset($_POST['xueyang'])?$_POST['xueyang']:"",
		'tizhong' => isset($_POST['tizhong'])?$_POST['tizhong']:"",
		'xueya_gao' => isset($_POST['xueya_gao'])?$_POST['xueya_gao']:"",
		'xueya_di' => isset($_POST['xueya_di'])?$_POST['xueya_di']:"",
		'atime' => $_POST['atime'],
	);
	
	require_once'./User/add.php';
	$add = new Add();
	
	$result = $add->tj($data['user'],$data['tiwen'],$data['xinlv'],$data['xueyang'],$data['tizhong'],$data['xueya_gao'],$data['xueya_di'],$data['atime'],$link);

	require_once'./Response/response.php';
	$response = new Response();

	if ($result == 701) {
		$response->json(701,'体检信息录入成功',$data);
	} else {
		
		$response->json(700,"体检信息录入失败",$data);
	}
	
?>