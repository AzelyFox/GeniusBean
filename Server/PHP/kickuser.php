<?php

//header("Content-Type: text/html; charset=UTF-8");

$user_channel = $_REQUEST["user_channel"];
$user_roomnum = $_REQUEST["user_roomnum"];
$user_usernum = $_REQUEST["user_usernum"];
$kick_usernum = $_REQUEST["kick_usernum"];


if (!$user_channel || !$user_roomnum || !$user_usernum || !$kick_usernum){
echo "ERROR";
exit;
}

$DB['host'] = 'localhost';
$DB['db'] = 'game_bean';
$DB['id'] = 'game_php';
$DB['pw'] = 'password';

$getsqli = new mysqli($DB['host'], $DB['id'], $DB['pw'], $DB['db']);
mysqli_query($getsqli,"set names utf8");

$q = "";

if($user_channel == 1){
		$q =  "select room_user_num_i from bean_room_i WHERE room_num = $user_roomnum LIMIT 1";
		$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
		$rowroom = $result->fetch_array(MYSQLI_NUM);
		if ($rowroom[0] != $user_usernum){
			echo 'ERROR:NOT ADMIN';
			exit;
		}
	if($kick_usernum == 2){
		$q =  "UPDATE bean_room_i SET room_user_num_ii = 0 , room_current = room_current - 1 , room_ready_ii = 0 WHERE room_num = $user_roomnum";
	}
	if($kick_usernum == 3){
		$q =  "UPDATE bean_room_i SET room_user_num_iii = 0 , room_current = room_current - 1 , room_ready_iii = 0 WHERE room_num = $user_roomnum";
	}
	if($kick_usernum == 4){
		$q =  "UPDATE bean_room_i SET room_user_num_iv = 0 , room_current = room_current - 1 , room_ready_iv = 0 WHERE room_num = $user_roomnum";
	}
	if($kick_usernum == 5){
		$q =  "UPDATE bean_room_i SET room_user_num_v = 0 , room_current = room_current - 1 , room_ready_v = 0 WHERE room_num = $user_roomnum";
	}
	if($kick_usernum == 6){
		$q =  "UPDATE bean_room_i SET room_user_num_vi = 0 , room_current = room_current - 1 , room_ready_vi = 0 WHERE room_num = $user_roomnum";
	}
}
if($user_channel == 2){
		$q =  "select room_user_num_i from bean_room_ii WHERE room_num = $user_roomnum LIMIT 1";
		$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
		$rowroom = $result->fetch_array(MYSQLI_NUM);
		if ($rowroom[0] != $user_usernum){
			echo 'ERROR:NOT ADMIN';
			exit;
		}
	if($kick_usernum == 2){
		$q =  "UPDATE bean_room_ii SET room_user_num_ii = 0 , room_current = room_current - 1, room_ready_ii = 0 WHERE room_num = $user_roomnum";
	}
	if($kick_usernum == 3){
		$q =  "UPDATE bean_room_ii SET room_user_num_iii = 0 , room_current = room_current - 1 , room_ready_iii = 0 WHERE room_num = $user_roomnum";
	}
	if($kick_usernum == 4){
		$q =  "UPDATE bean_room_ii SET room_user_num_iv = 0 , room_current = room_current - 1 , room_ready_iv = 0 WHERE room_num = $user_roomnum";
	}
	if($kick_usernum == 5){
		$q =  "UPDATE bean_room_ii SET room_user_num_v = 0 , room_current = room_current - 1 , room_ready_v = 0 WHERE room_num = $user_roomnum";
	}
	if($kick_usernum == 6){
		$q =  "UPDATE bean_room_ii SET room_user_num_vi = 0 , room_current = room_current - 1 , room_ready_vi = 0 WHERE room_num = $user_roomnum";
	}
}


$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");


$getsqli->close();

?>