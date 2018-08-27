<?php
	require_once '../Db/db.php';
	$db = new Db();
	$conn = $db->connect();
	$phone = $_GET['user'];

	$user_sql = "SELECT * from user  where user='{$phone}'";
	$tijian_sql = "SELECT * from tijian  where user='{$phone}'";
	$xinxi_sql = "SELECT * from xinxi  where user='{$phone}'";
	// $username_sql = "SELECT * from user  where phone='{$phone}'";
	//echo $user_sql;
	$user_data = mysql_fetch_array(mysql_query($user_sql,$conn));
	$tijian_result = mysql_query($tijian_sql,$conn);
	$xinxi_data = mysql_fetch_array(mysql_query($xinxi_sql,$conn));

	$xueya_gao = 0;
	$xueya_di = 0;
	$xueya_zc = 0;

	$xinlv_gao = 0;
	$xinlv_di = 0;
	$xinlv_zc = 0;
	while ( $tijian_data = mysql_fetch_array($tijian_result)) {
	//echo $tijian_data['xinlv'];
	if($tijian_data['xueya_gao']!="" && $tijian_data['xueya_di']!=""){
		if ($tijian_data['xueya_gao']>120) {
			$xueya_gao+=1;
		}elseif ($tijian_data['xueya_di']<80) {
			$xueya_di+=1;
		}else{
			$xueya_zc+=1;
		}
	}
	if($tijian_data['xinlv']!=""){
		if ($tijian_data['xinlv']>100) {
			$xinlv_gao+=1;
		}elseif ($tijian_data['xinlv']<60) {
			$xinlv_di+=1;
		}else{
			$xinlv_zc+=1;
		}
	}
	}
	$num = $xueya_gao+$xueya_di+$xueya_zc;
	if ($xueya_gao>$xueya_di) {
		if ($xueya_zc>$xueya_gao) {
			$xueya_q = "基本正常";
		}else{
			$xueya_q="偏高";
		}
	}else{
		if ($xueya_zc>$xueya_di) {
			$xueya_q = "基本正常";
		}else{
			$xueya_q="偏低";
		}
	}
	$xueya_gao =floor($xueya_gao/$num*10000)/100;
	$xueya_di = floor($xueya_di/$num*10000)/100;
	$xueya_zc = floor($xueya_zc/$num*10000)/100;

	$num2 = $xinlv_gao+$xinlv_di+$xinlv_zc;
	if ($xinlv_gao>$xinlv_di) {
		if ($xinlv_zc>$xinlv_gao) {
			$xinlv_q = "基本正常";
		}else{
			$xinlv_q="偏高";
		}
	}else{
		if ($xinlv_zc>$xinlv_di) {
			$xinlv_q = "基本正常";
		}else{
			$xinlv_q="偏低";
		}
	}
	//echo $xinlv_di;
	$xinlv_gao = floor($xinlv_gao/$num2*10000)/100;
	$xinlv_di = floor($xinlv_di/$num2*10000)/100;
	$xinlv_zc = floor($xinlv_zc/$num2*10000)/100;
?>