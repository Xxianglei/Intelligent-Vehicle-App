<!DOCTYPE html>
<html>
	<head>
		<?php 
			require_once './houtai/houtai.php';
		 ?>
		<title>血压监测周报报告</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0, user-scalable=no"/>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="css/bpw_css4.css" rel="stylesheet">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="css/public_config.css"/>
		<style>
			#i_bpw_banner{
				background-image: url("images/banner_4.png");
			}
		</style>
	</head>
	<body id="i_bpw_body" class="c_bpw_body">
		<div id="i_bpw_banner" class="c_bpw_banner">
			<div id="logion">没有健康就没有乐趣。——博林布鲁克</div>
			<div id="bpControl">健康指数</div>
			<div id="r_bp_judge"></div>
			<div id="i_bpw_date"></div>
		</div>
		<div id="i_bpw_name" class="c_bpw_name_bg_left c_cp_padding">尊敬的<?php $user_data['userName']; ?></div>
		<div id="i_bpw_overview" class="c_bpw_overview c_cp_padding mbotm">您好！</div>
		<div class="c_bpw_overview c_cp_padding">检测数据分析详情</div>
		<div class="c_bpw_blank"><img src="./picture/blank.png" /></div>
		<div id="i_bpw_levelanaly_title" class="c_bpw_levelanaly_title c_cp_padding_left">
			<img src="picture/title1.png" class="c_bpw_icon">
		</div>
		<div id="i_bpw_levelanaly_title" class="c_bpw_levelanaly_title">血压等级分析<div class="c_bpw_levelanaly_small_title">通过每次测量等级分析血压异常占比</div></div>
		<div class="c_bpw_clear"></div>
		<div id="i_bpw_levelanaly_overview" class="c_bpw_levelanaly_overview c_cp_padding">
			您本周血压监测情况：<br>
				高血压比重：<?php echo $xueya_gao ?>%
				低血压比重：<?php echo $xueya_di ?>%
				正常血压比重：<?php echo $xueya_zc ?>%
		</div>
		<div class="c_bpw_load">本周血压监测情况：<?php echo $xueya_q ?></div>

  <div class="c_bpw_load_value1 c_bpw_color_guozhong"></div>
		
		<div class="c_bpw_clear"></div>
		
		<div class="c_bpw_blank"><img src="picture/blank.png" /></div>
		<div class="c_bpw_blank"><img src="picture/blank.png" /></div>
		<div id="i_bpw_kindlyremind_title" class="c_bpw_kindlyremind_title c_cp_padding_left"><img src="picture/title5.png" class="c_bpw_icon">
		</div>
		<div id="i_bpw_kindlyremind_title" class="c_bpw_kindlyremind_title">血压建议
			<div class="c_bpw_levelanaly_small_title">综合控制效果提供的个性化建议</div>
		</div>
		<div class="c_bpw_clear"></div>
		<div id="i_bpw_kindlyremind_1" class="c_bpw_kindlyremind_1 c_cp_padding">研究表明，通过良好的心理调整，保持情绪稳定、心境平和、心情舒畅，会有助于高血压的治疗。结合您的血压控制情况，心理调节方面建议：在用药、饮食、运动等方面积极治疗的同时，也请多重视心理状态的调节，避免心理负担过重，这样才能最大限度的促进血压水平稳步降至理想范围内。</div>
		<div class="c_bpw_blank"><img src="./picture/blank.png" /></div>
		<div id="i_bpw_levelanaly_title" class="c_bpw_levelanaly_title c_cp_padding_left">
			<img src="picture/title1.png" class="c_bpw_icon">
		</div>

		<div id="i_bpw_levelanaly_title" class="c_bpw_levelanaly_title">心率分析<div class="c_bpw_levelanaly_small_title">通过每次测量等级分析心率异常占比</div></div>
		<div class="c_bpw_clear"></div>
		<div id="i_bpw_levelanaly_overview" class="c_bpw_levelanaly_overview c_cp_padding">
			您本周心率监测情况：<br>
				高心率比重：<?php echo $xinlv_gao ?>%
				低心率比重：<?php echo $xinlv_di ?>%
				正常心率比重：<?php echo $xinlv_zc ?>%
		</div>
		<div class="c_bpw_load">心率监测情况：<?php echo $xinlv_q ?></div>

  <div class="c_bpw_load_value1 c_bpw_color_guozhong"></div>
		
		<div class="c_bpw_clear"></div>
		
		<div class="c_bpw_blank"><img src="picture/blank.png" /></div>
		<div class="c_bpw_blank"><img src="picture/blank.png" /></div>
		<div id="i_bpw_kindlyremind_title" class="c_bpw_kindlyremind_title c_cp_padding_left"><img src="picture/title5.png" class="c_bpw_icon">
		</div>
		<div id="i_bpw_kindlyremind_title" class="c_bpw_kindlyremind_title">本周建议
			<div class="c_bpw_levelanaly_small_title">综合控制效果提供的个性化建议</div>
		</div>
		<div class="c_bpw_clear"></div>
		<div id="i_bpw_kindlyremind_1" class="c_bpw_kindlyremind_1 c_cp_padding">1.运动
常参加各种强度适宜的运动，就会使静息心率变慢。虽然运动时心率加快，但运动能使心功能得到锻炼，从而使静息心率减慢。一般适宜的运动心率是“170-年龄”，如一个50岁人，运动心率控制在120次/分钟为宜，过快说明运动量过大，达不到也起不到效果。运动前要自觉舒适、无疲劳感，一般运动不要超过1小时，而且每次最佳时间为30分钟～60分钟，每周至少坚持3次运动。
2.改正不良的生活方式
熬夜、吸烟、饮酒均可使静息心率加快。少喝浓茶，特别是不要在睡前喝，否则容易导致失眠。还应定时大便，保持排便顺畅。
3.保持适当体重
肥胖会使心脏负担加重，心率加快，因此肥胖者要通过健身运动，调节饮食来保持适宜的体重。
4.保持心态平和
生活中心态要平和，不要总着急、生气，如果因为紧张、生气等情况出现心率过快，可以通过听音乐、静心冥想等方式逐渐恢复平静。</div>
	</body>
</html>