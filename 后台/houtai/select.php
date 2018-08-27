<?php 
header("Content-type:text/html; charset=utf-8");
	require_once'./Db/db.php';
	$db = new Db();
	$link = $db->connect();

	$user = $_POST['user'];
	$biao = $_GET['biao'];

	require_once'./User/select.php';
	$select = new Select();
	$fun = 'select_'.$biao;
	
	$result = $select->$fun($user,$link);
	require_once'./Response/response.php';
	$response = new Response();
	if ($result != "" ) {
		$response->json(801,'查找成功',$result);
	} else {
		
		$response->json(800,"查找失败",$result);
	}
	
 ?>