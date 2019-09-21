<?php

//header("Content-Type: text/html; charset=UTF-8");

$user_channel = $_REQUEST["user_channel"];
$room_num = $_REQUEST["room_num"];
$user_num = $_REQUEST["user_num"];

if (!$user_channel | !$room_num | !$user_num){
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
	$q = "select room_name,room_pw,room_bat,room_user_num_i,room_user_num_ii,room_user_num_iii,room_user_num_iv,room_user_num_v,room_user_num_vi from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
}
if($user_channel == 2){
	$q = "select room_name,room_pw,room_bat,room_user_num_i,room_user_num_ii,room_user_num_iii,room_user_num_iv,room_user_num_v,room_user_num_vi from bean_room_ii WHERE room_num = $room_num LIMIT 1";
}

$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
$rowroom = $result->fetch_array(MYSQLI_NUM);

$result_json = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
$rows = array();
while($r = mysqli_fetch_assoc($result_json)) {
    $rows[] = $r;
}

if(!$rowroom[0]){
	echo "ERROR &NOROOM&";
	exit;
}

$ADDLOCATION = 0;
for($a = 3;$a <= 8;$a++){
	if($rowroom[$a] == 0){
		$ADDLOCATION = $a-2;
		break;
	}
}

if($ADDLOCATION == 1){
	echo "ERROR &NOUSER&";
	exit;
} else if ($ADDLOCATION == 2){
	if($user_channel == 1){
		$q = "UPDATE bean_room_i SET room_user_num_ii = $user_num , room_current = room_current + 1 WHERE room_num = $room_num";
	}
	if($user_channel == 2){
		$q = "UPDATE bean_room_ii SET room_user_num_ii = $user_num , room_current = room_current + 1 WHERE room_num = $room_num";
	}
} else if ($ADDLOCATION == 3){
	if($user_channel == 1){
		$q = "UPDATE bean_room_i SET room_user_num_iii = $user_num , room_current = room_current + 1 WHERE room_num = $room_num";
	}
	if($user_channel == 2){
		$q = "UPDATE bean_room_ii SET room_user_num_iii = $user_num , room_current = room_current + 1 WHERE room_num = $room_num";
	}
} else if ($ADDLOCATION == 4){
	if($user_channel == 1){
		$q = "UPDATE bean_room_i SET room_user_num_iv = $user_num , room_current = room_current + 1 WHERE room_num = $room_num";
	}
	if($user_channel == 2){
		$q = "UPDATE bean_room_ii SET room_user_num_iv = $user_num , room_current = room_current + 1 WHERE room_num = $room_num";
	}
} else if ($ADDLOCATION == 5){
	if($user_channel == 1){
		$q = "UPDATE bean_room_i SET room_user_num_v = $user_num , room_current = room_current + 1 WHERE room_num = $room_num";
	}
	if($user_channel == 2){
		$q = "UPDATE bean_room_ii SET room_user_num_v = $user_num , room_current = room_current + 1 WHERE room_num = $room_num";
	}
} else if ($ADDLOCATION == 6){
	if($user_channel == 1){
		$q = "UPDATE bean_room_i SET room_user_num_vi = $user_num , room_current = room_current + 1 WHERE room_num = $room_num";
	}
	if($user_channel == 2){
		$q = "UPDATE bean_room_ii SET room_user_num_vi = $user_num , room_current = room_current + 1 WHERE room_num = $room_num";
	}
} else {
	echo "ERROR &FULL&";
	exit;
}

$result = mysqli_query($getsqli, $q) or die(mysqli_error($getsqli));

echo json_encode($rows, JSON_UNESCAPED_UNICODE);

$getsqli->close();

?>