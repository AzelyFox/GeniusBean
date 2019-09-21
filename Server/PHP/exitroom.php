<?php

//header("Content-Type: text/html; charset=UTF-8");

$user_channel = $_REQUEST["user_channel"];
$room_num = $_REQUEST["room_num"];
$user_num = $_REQUEST["user_num"];
$user_location = $_REQUEST["user_location"];

if (!$user_channel | !$room_num | !$user_num | !$user_location){
echo "ERROR";
exit;
}

$DB['host'] = 'localhost';
$DB['db'] = 'game_bean';
$DB['id'] = 'game_php';
$DB['pw'] = 'password';

$getsqli = new mysqli($DB['host'], $DB['id'], $DB['pw'], $DB['db']);
mysqli_query($getsqli,"set names utf8");

if($user_location == 1){
	if($user_channel == 1){
		$q = "UPDATE bean_room_i SET room_user_num_i = 0 , room_current = room_current - 1 WHERE room_num = $room_num";
	}
	if($user_channel == 2){
		$q = "UPDATE bean_room_ii SET room_user_num_i = 0 , room_current = room_current - 1 WHERE room_num = $room_num";
	}
} else if ($user_location == 2){
	if($user_channel == 1){
		$q = "UPDATE bean_room_i SET room_user_num_ii = 0 , room_current = room_current - 1 , room_ready_ii = 0 WHERE room_num = $room_num";
	}
	if($user_channel == 2){
		$q = "UPDATE bean_room_ii SET room_user_num_ii = 0 , room_current = room_current - 1 , room_ready_ii = 0 WHERE room_num = $room_num";
	}
} else if ($user_location == 3){
	if($user_channel == 1){
		$q = "UPDATE bean_room_i SET room_user_num_iii = 0 , room_current = room_current - 1 , room_ready_iii = 0 WHERE room_num = $room_num";
	}
	if($user_channel == 2){
		$q = "UPDATE bean_room_ii SET room_user_num_iii = 0 , room_current = room_current - 1 , room_ready_iii = 0 WHERE room_num = $room_num";
	}
} else if ($user_location == 4){
	if($user_channel == 1){
		$q = "UPDATE bean_room_i SET room_user_num_iv = 0 , room_current = room_current - 1 , room_ready_iv = 0 WHERE room_num = $room_num";
	}
	if($user_channel == 2){
		$q = "UPDATE bean_room_ii SET room_user_num_iv = 0 , room_current = room_current - 1 , room_ready_iv = 0 WHERE room_num = $room_num";
	}
} else if ($user_location == 5){
	if($user_channel == 1){
		$q = "UPDATE bean_room_i SET room_user_num_v = 0 , room_current = room_current - 1 , room_ready_v = 0 WHERE room_num = $room_num";
	}
	if($user_channel == 2){
		$q = "UPDATE bean_room_ii SET room_user_num_v = 0 , room_current = room_current - 1 , room_ready_v = 0 WHERE room_num = $room_num";
	}
} else if ($user_location == 6){
	if($user_channel == 1){
		$q = "UPDATE bean_room_i SET room_user_num_vi = 0 , room_current = room_current - 1 , room_ready_vi = 0 WHERE room_num = $room_num";
	}
	if($user_channel == 2){
		$q = "UPDATE bean_room_ii SET room_user_num_vi = 0 , room_current = room_current - 1 , room_ready_vi = 0 WHERE room_num = $room_num";
	}
}

$result = mysqli_query($getsqli, $q) or die(mysqli_error($getsqli));

$q = "";
$qdel = "";
if ($user_channel == 1) {
	$q = "select room_current from bean_room_i WHERE room_num = $room_num LIMIT 1";
	$qdel = "DELETE FROM bean_room_i WHERE `bean_room_i`.`room_num` = $room_num";
}
if ($user_channel == 2) {
	$q = "select room_current from bean_room_ii WHERE room_num = $room_num LIMIT 1";
	$qdel = "DELETE FROM bean_room_ii WHERE `bean_room_ii`.`room_num` = $room_num";
}
$result = mysqli_query($getsqli, $q) or die("ERROR");
$row = $result->fetch_array(MYSQLI_NUM);

if (!$row[0] | $row[0] <= 0){
$result = mysqli_query($getsqli, $qdel) or die("ERROR");
}

$getsqli->close();

?>