<?php
/**
 * 
 */
class Select{
	
	public function login($phone,$password,$link) {
		$sql = "SELECT*FROM loginUser where phone = {$phone}";
		
		$result = mysql_query($sql,$link);
		
		if ($result) {
			$data = mysql_fetch_array($result);
			
			if ($password == $data['password']) {
				return 201;
			} else {
				return 200;
			}
		} else {
			return 0;
		}
		
	}
	
	public function select_linkman($phone,$link) {
		$sql = "SELECT*FROM linkman where user = {$phone}";
		
		$result = mysql_query($sql,$link);
		
		if ($result) {
			$data = mysql_fetch_array($result);
			if ($data != '') {
				return $data;
			} else {
				return 200;
			}
		} else {
			return 0;
		}
		
	}
	// // public function select_tijian($phone,$link) {
	// // 	$sql = "SELECT*FROM tijian where user = {$phone}";
		
	// // 	$result = mysql_query($sql,$link);
	// // 	if ($result) {
	// // 		while ( $data = mysql_fetch_array($result)) {
	// // 			$i = 0;
	// // 			$data1 = array();
	// // 			foreach($data as $key=>$value){
	// // 				$k = $key;
	// // 		        for ($j=0; $j < 14 ; $j++) { 
	// // 		        	 $data1[$i][$j] = $data[$key];
	// // 		        }
	// // 		    }
	// // 		    $i++;
	// // 		}
	// // 		if ($data1 != '') {
	// // 			return $data1;
	// // 		} else {
	// // 			return 200;
	// // 		}
	// // 	} else {
	// // 		return 0;
	// // 	}
		
	// }
	public function select_user($phone,$link) {
		$sql = "SELECT*FROM user where user = {$phone}";
		
		$result = mysql_query($sql,$link);
		
		if ($result) {
			$data = mysql_fetch_array($result);
			if ($data != '') {
				return $data;
			} else {
				return 200;
			}
		} else {
			return 0;
		}
		
	}
	public function select_xinxi($phone,$link) {
		$sql = "SELECT*FROM xinxi where user = {$phone}";
		
		$result = mysql_query($sql,$link);
		
		if ($result) {
			$data = mysql_fetch_array($result);

			if ($data != '') {
				return $data;
			} else {
				return 200;
			}
		} else {
			return 0;
		}
		
	}
	public function select_aa(){
		echo "111";
	}
}

?>