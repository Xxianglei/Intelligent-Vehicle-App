<?php
	header("Content-type:text/html; charset=utf-8");
	require_once'./Db/db.php';
	$db = new Db();
	$link = $db->connect();

	$user = $_POST['user'];
	$biao = $_GET['biao'];

	require_once'./User/update.php';
	$update = new Update();
	$fun = 'update_'.$biao;
	if ($biao == 'xinxi') {
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
		$result = $update->$fun($data['name'],$data['shengao'],$data['tizhong'],$data['bingshi'],$data['bron'],$data['xuexing'],$data['guoming'],$data['xiguan'],$data['user'],$link);
	}elseif ($biao == 'user'){

		$data = array(
		'userName' => $_POST['userName'],
		'sex' =>$_POST['sex'],
		'height' => $_POST['height'],
		'weight' =>$_POST['weight'],
		'age' => $_POST['age'],
		'img' => $_POST['img'],
		'user'=>$_POST['user']
	);
		$result = $update->$fun($data['userName'],$data['sex'],$data['height'],$data['weight'],$data['age'],$data['img'],$data['user'],$link);
	}
	
	require_once'./Response/response.php';
	$response = new Response();
	if ($result !=900 ) {
		$response->json($result,'修改成功',$data);
	} else {
		$response->json(900,"修改失败",$result);
	}
?>