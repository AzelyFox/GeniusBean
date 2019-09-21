<?php

$user_me = $_REQUEST["user_me"];
$room_num = $_REQUEST["room_num"];
$room_channel = $_REQUEST["room_channel"];
$room_round = $_REQUEST["room_round"];

if (!$room_channel | !$room_num | !$user_me | !$room_round){
echo "ERROR";
exit;
}

$DB['host'] = 'localhost';
$DB['db'] = 'game_bean';
$DB['id'] = 'game_php';
$DB['pw'] = 'password';

$getsqli = new mysqli($DB['host'], $DB['id'], $DB['pw'], $DB['db']);
mysqli_query($getsqli,"set names utf8");

//라운드 계산값들 가져오기
$q = "";

if($room_channel == 1){
	if ($room_round == 1){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_round_i_average from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 2){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_round_ii_average from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 3){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_round_iii_average from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 4){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_round_iv_average from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 5){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_round_v_average,room_exp_user_i,room_exp_user_ii,room_exp_user_iii,room_exp_user_iv,room_exp_user_v,room_exp_user_vi,room_money_user_i,room_money_user_ii,room_money_user_iii,room_money_user_iv,room_money_user_v,room_money_user_vi,room_user_i_grade,room_user_ii_grade,room_user_iii_grade,room_user_iv_grade,room_user_v_grade,room_user_vi_grade from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
}
if($room_channel == 2){
	if ($room_round == 1){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_round_i_average from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 2){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_round_ii_average from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 3){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_round_iii_average from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 4){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_round_iv_average from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 5){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_round_v_average,room_exp_user_i,room_exp_user_ii,room_exp_user_iii,room_exp_user_iv,room_exp_user_v,room_exp_user_vi,room_money_user_i,room_money_user_ii,room_money_user_iii,room_money_user_iv,room_money_user_v,room_money_user_vi,room_user_i_grade,room_user_ii_grade,room_user_iii_grade,room_user_iv_grade,room_user_v_grade,room_user_vi_grade from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
}

$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
$rowroom = $result->fetch_array(MYSQLI_NUM);
$user_i_point = $rowroom[0];
$user_ii_point = $rowroom[1];
$user_iii_point = $rowroom[2];
$user_iv_point = $rowroom[3];
$user_v_point = $rowroom[4];
$user_vi_point = $rowroom[5];
$round_average = $rowroom[6];
$user_i_exp = 0;
$user_ii_exp = 0;
$user_iii_exp = 0;
$user_iv_exp = 0;
$user_v_exp = 0;
$user_vi_exp = 0;
$user_i_money = 0;
$user_ii_money = 0;
$user_iii_money = 0;
$user_iv_money = 0;
$user_v_money = 0;
$user_vi_money = 0;
if ($room_round == 5){
	$user_i_exp = $rowroom[7];
	$user_ii_exp = $rowroom[8];
	$user_iii_exp = $rowroom[9];
	$user_iv_exp = $rowroom[10];
	$user_v_exp = $rowroom[11];
	$user_vi_exp = $rowroom[12];
	$user_i_money = $rowroom[13];
	$user_ii_money = $rowroom[14];
	$user_iii_money = $rowroom[15];
	$user_iv_money = $rowroom[16];
	$user_v_money = $rowroom[17];
	$user_vi_money = $rowroom[18];
	$user_i_grade = $rowroom[19];
	$user_ii_grade = $rowroom[20];
	$user_iii_grade = $rowroom[21];
	$user_iv_grade = $rowroom[22];
	$user_v_grade = $rowroom[23];
	$user_vi_grade = $rowroom[24];
}

$arr = array ('point_i'=>$user_i_point,'point_ii'=>$user_ii_point,'point_iii'=>$user_iii_point,'point_iv'=>$user_iv_point,'point_v'=>$user_v_point,'point_vi'=>$user_vi_point,'round_average'=>$round_average,'exp_i'=>$user_i_exp,'exp_ii'=>$user_ii_exp,'exp_iii'=>$user_iii_exp,'exp_iv'=>$user_iv_exp,'exp_v'=>$user_v_exp,'exp_vi'=>$user_vi_exp,'money_i'=>$user_i_money,'money_ii'=>$user_ii_money,'money_iii'=>$user_iii_money,'money_iv'=>$user_iv_money,'money_v'=>$user_v_money,'money_vi'=>$user_vi_money,'grade_i'=>$user_i_grade,'grade_ii'=>$user_ii_grade,'grade_iii'=>$user_iii_grade,'grade_iv'=>$user_iv_grade,'grade_v'=>$user_v_grade,'grade_vi'=>$user_vi_grade);

echo json_encode($arr);

$getsqli->close();

?>