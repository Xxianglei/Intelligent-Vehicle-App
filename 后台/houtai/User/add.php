<?php
/**
 * 
 */
class Add{
	
	public  function zhuce($phone,$pwassword,$link)
	{
		$sql = "INSERT into loginUser(phone,password) values({$phone},'{$pwassword}')";
		$result = mysql_query($sql,$link);
		if ($result) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public function user($userName,$sex,$height,$weight,$age,$img,$user,$link){
		$s_sql="SELECT * from user where user = '{$user}'";
		$s_result = mysql_query($s_sql,$link);
		$s_row = mysql_num_rows($s_result);
		if ($s_row != 1) {
			$sql = "INSERT into user(userName,sex,height,weight,age,img,user) values('{$userName}','{$sex}',{$height},{$weight},{$age},'{$img}','{$user}')";
		} else {
			$sql = "UPDATE  user set userName='{$userName}',sex='{$sex}',height={$height},weight={$weight},age={$age},img='{$img}' where user='{$user}'";
		}
		echo $sql;
		$result = mysql_query($sql,$link);
		if ($result) {
			return 301;
		} else {
			return 300;
		}
	}

	public function xinxi($name,$shengao,$tizhong,$bingshi,$bron,$xuexing,$guoming,$xiguan,$user,$link){
		$s_sql="SELECT * from xinxi where user = '{$user}'";
		$s_result = mysql_query($s_sql,$link);
		$s_row = mysql_num_rows($s_result);
		if ($s_row!=1) {
			$sql = "INSERT into xinxi(name,shengao,tizhong,bingshi,bron,xuexing,guoming,xiguan,user) values('{$name}',{$shengao},{$tizhong},'{$bingshi}','{$bron}','{$xuexing}','{$guoming}','{$xiguan}','{$user}')";
		} else {
			$sql = "UPDATE  xinxi set name='{$name}',shengao='{$shengao}',tizhong='{$tizhong}',bingshi='{$bingshi}',bron='{$bron}',xuexing='{$xuexing}',guoming='{$guoming}',xiguan='{$xiguan}' where user='{$user}'";
		}
		
			$result = mysql_query($sql,$link);
		
		if ($result) {
			return 501;
		} else {
			return 500;
		}
	}
	public function link($user,$name,$phone,$link){
		$s_sql="SELECT * from linkman where user = '{$user}'";
		$s_result = mysql_query($s_sql,$link);
		$s_row = mysql_num_rows($s_result);
		if ($s_row!=1) {
			$sql = "INSERT into linkman(user,name,phone) values('{$user}','{$name}',{$phone})";
		} else {
			$sql = "UPDATE  linkman set name='{$name}',phone='{$phone}' where user='{$user}'";
		}
		
		
		$result = mysql_query($sql,$link);
		
		if ($result) {
			return 601;
		} else {
			return 600;
		}
	}
	public function tj($user,$tiwen,$xinlv,$xueyang,$tizhong,$xueya_gao,$xueya_di,$atime,$link){
		
		$sql = "INSERT into tijian(user,tiwen,xinlv,xueyang,tizhong,xueya_gao,xueya_di,atime) values('{$user}','{$tiwen}','{$xinlv}','{$xueyang}','{$tizhong}','{$xueya_gao}','{$xueya_di}','{$atime}')";
		echo $sql;
			$result = mysql_query($sql,$link);
		
		if ($result) {
			return 701;
		} else {
			return 700;
		}
	}
	
}

?>