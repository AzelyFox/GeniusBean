<?php

$user_num = $_REQUEST["user_num"];
$room_num = $_REQUEST["room_num"];
$room_channel = $_REQUEST["room_channel"];
$room_round = $_REQUEST["room_round"];

if (!$room_channel | !$room_num | !$user_num | !$room_round){
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
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_usedbean_round_i_user_i,room_usedbean_round_i_user_ii,room_usedbean_round_i_user_iii,room_usedbean_round_i_user_iv,room_usedbean_round_i_user_v,room_usedbean_round_i_user_vi from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 2){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_usedbean_round_ii_user_i,room_usedbean_round_ii_user_ii,room_usedbean_round_ii_user_iii,room_usedbean_round_ii_user_iv,room_usedbean_round_ii_user_v,room_usedbean_round_ii_user_vi from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 3){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_usedbean_round_iii_user_i,room_usedbean_round_iii_user_ii,room_usedbean_round_iii_user_iii,room_usedbean_round_iii_user_iv,room_usedbean_round_iii_user_v,room_usedbean_round_iii_user_vi from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 4){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_usedbean_round_iv_user_i,room_usedbean_round_iv_user_ii,room_usedbean_round_iv_user_iii,room_usedbean_round_iv_user_iv,room_usedbean_round_iv_user_v,room_usedbean_round_iv_user_vi from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 5){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_usedbean_round_v_user_i,room_usedbean_round_v_user_ii,room_usedbean_round_v_user_iii,room_usedbean_round_v_user_iv,room_usedbean_round_v_user_v,room_usedbean_round_v_user_vi from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
}
if($room_channel == 2){
	if ($room_round == 1){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_usedbean_round_i_user_i,room_usedbean_round_i_user_ii,room_usedbean_round_i_user_iii,room_usedbean_round_i_user_iv,room_usedbean_round_i_user_v,room_usedbean_round_i_user_vi from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 2){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_usedbean_round_ii_user_i,room_usedbean_round_ii_user_ii,room_usedbean_round_ii_user_iii,room_usedbean_round_ii_user_iv,room_usedbean_round_ii_user_v,room_usedbean_round_ii_user_vi from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 3){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_usedbean_round_iii_user_i,room_usedbean_round_iii_user_ii,room_usedbean_round_iii_user_iii,room_usedbean_round_iii_user_iv,room_usedbean_round_iii_user_v,room_usedbean_round_iii_user_vi from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 4){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_usedbean_round_iv_user_i,room_usedbean_round_iv_user_ii,room_usedbean_round_iv_user_iii,room_usedbean_round_iv_user_iv,room_usedbean_round_iv_user_v,room_usedbean_round_iv_user_vi from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_round == 5){
		$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_usedbean_round_v_user_i,room_usedbean_round_v_user_ii,room_usedbean_round_v_user_iii,room_usedbean_round_v_user_iv,room_usedbean_round_v_user_v,room_usedbean_round_v_user_vi from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
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
$user_i_bid = $rowroom[6];
$user_ii_bid = $rowroom[7];
$user_iii_bid = $rowroom[8];
$user_iv_bid = $rowroom[9];
$user_v_bid = $rowroom[10];
$user_vi_bid = $rowroom[11];

$bidsort = array();
$bidsort['1'] = $user_i_bid;
$bidsort['2'] = $user_ii_bid;
$bidsort['3'] = $user_iii_bid;
$bidsort['4'] = $user_iv_bid;
$bidsort['5'] = $user_v_bid;
$bidsort['6'] = $user_vi_bid;

$average = ($user_i_bid + $user_ii_bid + $user_iii_bid + $user_iv_bid + $user_v_bid) / 5;
$average = floor($average);


//라운드 계산
//print_r($bidsort);
//echo "and<br/>";
asort($bidsort);
//print_r($bidsort);

$min_value = reset($bidsort);
$current_state = 7;
$current_rank = 7;
$add_point = -4; 
foreach ($bidsort as $key => $value){
	//echo "<br/>deal $value and $key <br/>";
	//echo "currentstate : $current_state , currentrank : $current_rank , addpoint : $add_point , minvalue : $min_value<br/>";
	if ($min_value == $value){
		$current_rank--;
	//	echo "isminvalue , so rank--<br/>";
	//	echo "currentstate : $current_state , currentrank : $current_rank , addpoint : $add_point , minvalue : $min_value<br/>";
	} else {
		$current_rank --;
		$current_state = $current_rank;
		$min_value = $value;
	//	echo "isnotminvalue , so rank-- , state = rank, min = value<br/>";
	//	echo "currentstate : $current_state , currentrank : $current_rank , addpoint : $add_point , minvalue : $min_value<br/>";
	}
	if ($current_state >= 6){
		$add_point = -4;
	//	echo "currentstate == 6 , so addpoint = -4<br/>";
	}
	if ($current_state == 5){
		$add_point = -2;
	//	echo "currentstate == 5 , so addpoint = -2<br/>";
	}
	if ($current_state == 4){
		$add_point = 0;
	//	echo "currentstate == 4 , so addpoint = 0<br/>";
	}
	if ($current_state == 3){
		$add_point = 1;
	//	echo "currentstate == 3 , so addpoint = 1<br/>";
	}
	if ($current_state == 2){
		$add_point = 2;
	//	echo "currentstate == 2 , so addpoint = 2<br/>";
	}
	if ($current_state == 1){
		$add_point = 3;
	//	echo "currentstate == 1 , so addpoint = 3<br/>";
	}
	if ($key == 1){
		$user_i_point = $user_i_point + $add_point;
	//	echo "keyis 1 , so user i point = $user_i_point<br/>";
	}
	if ($key == 2){
		$user_ii_point = $user_ii_point + $add_point;
	//	echo "keyis 2 , so user ii point = $user_ii_point<br/>";
	}
	if ($key == 3){
		$user_iii_point = $user_iii_point + $add_point;
	//	echo "keyis 3 , so user iii point = $user_iii_point<br/>";
	}
	if ($key == 4){
		$user_iv_point = $user_iv_point + $add_point;
	//	echo "keyis 4 , so user iv point = $user_iv_point<br/>";
	}
	if ($key == 5){
		$user_v_point = $user_v_point + $add_point;
	//	echo "keyis 5 , so user v point = $user_v_point<br/>";
	}
	if ($key == 6){
		$user_vi_point = $user_vi_point + $add_point;
	//	echo "keyis 6 , so user vi point = $user_vi_point<br/>";
	}
}
//결과저장


if($room_channel == 1){
	$q = "UPDATE bean_room_i SET room_point_user_i = $user_i_point, room_point_user_ii = $user_ii_point,room_point_user_iii = $user_iii_point, room_point_user_iv = $user_iv_point, room_point_user_v = $user_v_point, room_point_user_vi = $user_vi_point, room_round = $room_round + 1 WHERE room_num = $room_num";
	if ($room_round == 1){
		$qb = "UPDATE bean_room_i SET room_round_i_average = $average WHERE room_num = $room_num" ;
		$result = mysqli_query($getsqli, $qb) or die("ERROR_MYSQLI");
	}
	if ($room_round == 2){
		$qb = "UPDATE bean_room_i SET room_round_ii_average = $average WHERE room_num = $room_num" ;
		$result = mysqli_query($getsqli, $qb) or die("ERROR_MYSQLI");
	}
	if ($room_round == 3){
		$qb = "UPDATE bean_room_i SET room_round_iii_average = $average WHERE room_num = $room_num" ;
		$result = mysqli_query($getsqli, $qb) or die("ERROR_MYSQLI");
	}
	if ($room_round == 4){
		$qb = "UPDATE bean_room_i SET room_round_iv_average = $average WHERE room_num = $room_num" ;
		$result = mysqli_query($getsqli, $qb) or die("ERROR_MYSQLI");
	}
	if ($room_round == 5){
		$qb = "UPDATE bean_room_i SET room_round_v_average = $average WHERE room_num = $room_num" ;
		$result = mysqli_query($getsqli, $qb) or die("ERROR_MYSQLI");
	}
}
if($room_channel == 2){
	$q = "UPDATE bean_room_ii SET room_point_user_i = $user_i_point, room_point_user_ii = $user_ii_point,room_point_user_iii = $user_iii_point, room_point_user_iv = $user_iv_point, room_point_user_v = $user_v_point, room_point_user_vi = $user_vi_point, room_round = $room_round + 1 WHERE room_num = $room_num";
	if ($room_round == 1){
		$qb = "UPDATE bean_room_ii SET room_round_i_average = $average WHERE room_num = $room_num" ;
		$result = mysqli_query($getsqli, $qb) or die("ERROR_MYSQLI");
	}
	if ($room_round == 2){
		$qb = "UPDATE bean_room_ii SET room_round_ii_average = $average WHERE room_num = $room_num" ;
		$result = mysqli_query($getsqli, $qb) or die("ERROR_MYSQLI");
	}
	if ($room_round == 3){
		$qb = "UPDATE bean_room_ii SET room_round_iii_average = $average WHERE room_num = $room_num" ;
		$result = mysqli_query($getsqli, $qb) or die("ERROR_MYSQLI");
	}
	if ($room_round == 4){
		$qb = "UPDATE bean_room_ii SET room_round_iv_average = $average WHERE room_num = $room_num" ;
		$result = mysqli_query($getsqli, $qb) or die("ERROR_MYSQLI");
	}
	if ($room_round == 5){
		$qb = "UPDATE bean_room_ii SET room_round_v_average = $average WHERE room_num = $room_num" ;
		$result = mysqli_query($getsqli, $qb) or die("ERROR_MYSQLI");
	}
}
$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");


if ($room_round == 5){
	//GET LAST BEANS
	$q = "";
	if ($room_channel == 1){
	$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_bean_user_i,room_bean_user_ii,room_bean_user_iii,room_bean_user_iv,room_bean_user_v,room_bean_user_vi,room_user_num_i,room_user_num_ii,room_user_num_iii,room_user_num_iv,room_user_num_v,room_user_num_vi from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($room_channel == 2){
	$q = "select room_point_user_i,room_point_user_ii,room_point_user_iii,room_point_user_iv,room_point_user_v,room_point_user_vi,room_bean_user_i,room_bean_user_ii,room_bean_user_iii,room_bean_user_iv,room_bean_user_v,room_bean_user_vi,room_user_num_i,room_user_num_ii,room_user_num_iii,room_user_num_iv,room_user_num_v,room_user_num_vi from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	
	$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
	$rowroom = $result->fetch_array(MYSQLI_NUM);
	//GET USER POINTS
	$user_point_i = $rowroom[0];
	$user_point_ii = $rowroom[1];
	$user_point_iii = $rowroom[2];
	$user_point_iv = $rowroom[3];
	$user_point_v = $rowroom[4];
	$user_point_vi = $rowroom[5];
	$user_bean_i = $rowroom[6];
	$user_bean_ii = $rowroom[7];
	$user_bean_iii = $rowroom[8];
	$user_bean_iv = $rowroom[9];
	$user_bean_v = $rowroom[10];
	$user_bean_vi = $rowroom[11];
	$user_num_i = $rowroom[12];
	$user_num_ii = $rowroom[13];
	$user_num_iii = $rowroom[14];
	$user_num_iv = $rowroom[15];
	$user_num_v = $rowroom[16];
	$user_num_vi = $rowroom[17];
	//CALCULATE
	$bidsort = array ($user_point_i=>1,$user_point_ii=>2,$user_point_iii=>3,$user_point_iv=>4,$user_point_v=>5,$user_point_vi=>6);

	$bidsort = array();
	$bidsort['1'] = $user_point_i;
	$bidsort['2'] = $user_point_ii;
	$bidsort['3'] = $user_point_iii;
	$bidsort['4'] = $user_point_iv;
	$bidsort['5'] = $user_point_v;
	$bidsort['6'] = $user_point_vi;

	asort($bidsort);
	$min_value = reset($bidsort);
	$current_state = 7;
	$current_rank = 7;
	$add_point = 15;
	$add_exp = 75;
	$add_bonus = 0;
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
	$user_i_grade = 0;
	$user_ii_grade = 0;
	$user_iii_grade = 0;
	$user_iv_grade = 0;
	$user_v_grade = 0;
	$user_vi_grade = 0;
	foreach ($bidsort as $key => $value){
		if ($min_value == $value){
			$current_rank--;
		} else {
			$current_rank --;
			$current_state = $current_rank;
			$min_value = $value;
		}
		if ($current_state == 6){
			$add_point = 15;
			$add_exp = 75;
			$add_bonus = 0;
		}
		if ($current_state == 5){
			$add_point = 20;
			$add_exp = 90;
			$add_bonus = 0;
		}
		if ($current_state == 4){
			$add_point = 25;
			$add_exp = 105;
			$add_bonus = 0;
		}
		if ($current_state == 3){
			$add_point = 30;
			$add_exp = 120;
			$add_bonus = 2;
		}
		if ($current_state == 2){
			$add_point = 40;
			$add_exp = 135;
			$add_bonus = 3;
		}
		if ($current_state == 1){
			$add_point = 50;
			$add_exp = 150;
			$add_bonus = 4;
		}
		if ($key == 1){
			$user_i_exp = $add_exp;
			$user_i_grade = $current_state;
			$user_i_money = $add_point + ($user_bean_i * $add_bonus);
		}
		if ($key == 2){
			$user_ii_exp = $add_exp;
			$user_ii_grade = $current_state;
			$user_ii_money = $add_point + ($user_bean_ii * $add_bonus);
		}
		if ($key == 3){
			$user_iii_exp = $add_exp;
			$user_iii_grade = $current_state;
			$user_iii_money = $add_point + ($user_bean_iii * $add_bonus);
		}
		if ($key == 4){
			$user_iv_exp = $add_exp;
			$user_iv_grade = $current_state;
			$user_iv_money = $add_point + ($user_bean_iv * $add_bonus);
		}
		if ($key == 5){
			$user_v_exp = $add_exp;
			$user_v_grade = $current_state;
			$user_v_money = $add_point + ($user_bean_v * $add_bonus);
		}
		if ($key == 6){
			$user_vi_exp = $add_exp;
			$user_vi_grade = $current_state;
			$user_vi_money = $add_point + ($user_bean_vi * $add_bonus);
		}
	}
	//SAVE EXP MONEY
	$q = "";
	if ($room_channel == 1){
		$q = "UPDATE bean_room_i SET room_exp_user_i = $user_i_exp, room_money_user_i = $user_i_money, room_exp_user_ii = $user_ii_exp, room_money_user_ii = $user_ii_money,room_exp_user_iii = $user_iii_exp, room_money_user_iii = $user_iii_money,room_exp_user_iv = $user_iv_exp, room_money_user_iv = $user_iv_money,room_exp_user_v = $user_v_exp, room_money_user_v = $user_v_money,room_exp_user_vi = $user_vi_exp, room_money_user_vi = $user_vi_money, room_user_i_grade = $user_i_grade, room_user_ii_grade = $user_ii_grade, room_user_iii_grade = $user_iii_grade, room_user_iv_grade = $user_iv_grade, room_user_v_grade = $user_v_grade, room_user_vi_grade = $user_vi_grade WHERE room_num = $room_num";
	}
	if ($room_channel == 2){
		$q = "UPDATE bean_room_ii SET room_exp_user_i = $user_i_exp, room_money_user_i = $user_i_money, room_exp_user_ii = $user_ii_exp, room_money_user_ii = $user_ii_money,room_exp_user_iii = $user_iii_exp, room_money_user_iii = $user_iii_money,room_exp_user_iv = $user_iv_exp, room_money_user_iv = $user_iv_money,room_exp_user_v = $user_v_exp, room_money_user_v = $user_v_money,room_exp_user_vi = $user_vi_exp, room_money_user_vi = $user_vi_money, room_user_i_grade = $user_i_grade, room_user_ii_grade = $user_ii_grade, room_user_iii_grade = $user_iii_grade, room_user_iv_grade = $user_iv_grade, room_user_v_grade = $user_v_grade, room_user_vi_grade = $user_vi_grade WHERE room_num = $room_num";
	}
	$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
	//INCREASE EXP MONEY
	$q = "UPDATE bean_user SET user_exp = user_exp + $user_i_exp, user_money = user_money + $user_i_money, user_played = user_played + 1 WHERE user_num = $user_num_i";
	$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
	$q = "UPDATE bean_user SET user_exp = user_exp + $user_ii_exp, user_money = user_money + $user_ii_money, user_played = user_played + 1 WHERE user_num = $user_num_ii";
	$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
	$q = "UPDATE bean_user SET user_exp = user_exp + $user_iii_exp, user_money = user_money + $user_iii_money, user_played = user_played + 1 WHERE user_num = $user_num_iii";
	$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
	$q = "UPDATE bean_user SET user_exp = user_exp + $user_iv_exp, user_money = user_money + $user_iv_money, user_played = user_played + 1 WHERE user_num = $user_num_iv";
	$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
	$q = "UPDATE bean_user SET user_exp = user_exp + $user_v_exp, user_money = user_money + $user_v_money, user_played = user_played + 1 WHERE user_num = $user_num_v";
	$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
	$q = "UPDATE bean_user SET user_exp = user_exp + $user_vi_exp, user_money = user_money + $user_vi_money, user_played = user_played + 1 WHERE user_num = $user_num_vi";
	$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");

}


echo "SUCCESS";

$getsqli->close();

?>