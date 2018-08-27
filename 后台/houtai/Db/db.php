<?php
class Db {
	
	public function connect(){
		$link = @mysql_connect('localhost','root','root');
		if (!$link) {
			echo"连接失败";
		} else {
	
		}
		
		mysql_query("set names UTF8",$link);
		/*连接成功后立即调用mysql_select_db()选中需要连接的数据库*/
		mysql_select_db('jk',$link);
		return $link;
	}
}
