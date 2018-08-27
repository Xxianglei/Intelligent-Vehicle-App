<?php
/**
	* 按json方式输出通信数据
	* @param integer $code 状态码
	* @param string $message 提示信息
	* @param array $data 数据
	* return string
*/

class Response{
	public static function json($code, $message ='', $data = array()) {

		$result = array(
			'code' => $code,
			'message' => $message,
			'data' => $data
		);

		echo json_encode($result,JSON_UNESCAPED_UNICODE);
	}
}
	
?>