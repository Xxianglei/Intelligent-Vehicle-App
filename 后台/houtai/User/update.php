<?php
	/**
	 * 
	 */
	class Update
	{
		
		public function update_user($userName,$sex,$height,$weight,$age,$img,$user,$link){
			$sql = "UPDATE user set userName = '{$userName}',sex = '{$sex}',height = {$height},weight = {$weight},age = {$age},img = '{$img}',user = '{$user}' where user='{$user}'";

			$result = mysql_query($sql,$link);

			$row = mysql_fetch_row($result);

			if ($row) {
				return 901;
			}else{
				return 900;
			}
		}

		public function update_xinxi($name,$shengao,$tizhong,$bingshi,$bron,$xuexing,$guoming,$xiguan,$user,$link){
			$sql = "UPDATE xinxi set name='{$name}',shenggao={$shengao},tizhong={$tizhong},bingshi='{$bingshi}',bron='{$bron}',xuexing='{$xuexing}',guoming='{$guoming}',xiguan='{$xiguan}',user='{$user}' where user='{$user}'";
			$result = mysql_query($sql,$link);

			$row = mysql_fetch_row($result);

			if ($row) {
				return 902;
			}else{
				return 900;
			}

		}
	}
?>