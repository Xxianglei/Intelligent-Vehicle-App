<?php	
header("Content-type:text/html;charset=utf-8");
// 测试PHP执行python代码


//$a = '1  1  1  1  1  1  1  1  1  0  0  0  0  0  0  0';

$a = $_POST['shuju'];



$a = json_encode($a);

$c = exec("C:\phpstudy\PHPTutorial\WWW\houtai/sj-model/sj/write.py {$a}" ,$out,$res);

$result = exec("python result.py",$array,$ret);


if ($result == 1) {
	echo "健康";
}elseif ($result == 2) {
	echo "亚健康";
}elseif ($result == 3) {
	echo "不健康";
}else {
	echo "添加失败";
}
?>