<?php

$user_channel = $_REQUEST["user_channel"];
$room_num = $_REQUEST["room_num"];
$user_location = $_REQUEST["user_location"];
$ready_state = $_REQUEST["ready_state"];

if (!$room_num | !$user_channel | !$user_location){
echo "ERROR";
exit;
}

$DB['host'] = 'localhost';
$DB['db'] = 'game_bean';
$DB['id'] = 'game_php';
$DB['pw'] = 'password';

$msqli = new mysqli($DB['host'], $DB['id'], $DB['pw'], $DB['db']);
mysqli_query($msqli,"set names utf8");

$q = "";

if ($user_channel == 1) {
	if ($user_location == 2){
		$q = "UPDATE bean_room_i SET room_ready_ii = $ready_state WHERE room_num = $room_num";
	}
	if ($user_location == 3) {
		$q = "UPDATE bean_room_i SET room_ready_iii = $ready_state WHERE room_num = $room_num";
	}
	if ($user_location == 4) {
		$q = "UPDATE bean_room_i SET room_ready_iv = $ready_state WHERE room_num = $room_num";
	}
	if ($user_location == 5) {
		$q = "UPDATE bean_room_i SET room_ready_v = $ready_state WHERE room_num = $room_num";
	}
	if ($user_location == 6) {
		$q = "UPDATE bean_room_i SET room_ready_vi = $ready_state WHERE room_num = $room_num";
	}
}
if ($user_channel == 2) {
	if ($user_location == 2){
		$q = "UPDATE bean_room_ii SET room_ready_ii = $ready_state WHERE room_num = $room_num";
	}
	if ($user_location == 3) {
		$q = "UPDATE bean_room_ii SET room_ready_iii = $ready_state WHERE room_num = $room_num";
	}
	if ($user_location == 4) {
		$q = "UPDATE bean_room_ii SET room_ready_iv = $ready_state WHERE room_num = $room_num";
	}
	if ($user_location == 5) {
		$q = "UPDATE bean_room_ii SET room_ready_v = $ready_state WHERE room_num = $room_num";
	}
	if ($user_location == 6) {
		$q = "UPDATE bean_room_ii SET room_ready_vi = $ready_state WHERE room_num = $room_num";
	}
}


$result = mysqli_query($msqli, $q) or die(mysqli_error($msqli));

echo "&SUCCESS&";
$msqli->close();

?>