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
	$q = "select room_name,room_pw,room_bat,room_user_num_i,room_user_num_ii,room_user_num_iii,room_user_num_iv,room_user_num_v,room_user_num_vi,room_ready_ii,room_ready_iii,room_ready_iv,room_ready_v,room_ready_vi from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
}
if($user_channel == 2){
	$q = "select room_name,room_pw,room_bat,room_user_num_i,room_user_num_ii,room_user_num_iii,room_user_num_iv,room_user_num_v,room_user_num_vi,room_ready_ii,room_ready_iii,room_ready_iv,room_ready_v,room_ready_vi from bean_room_ii WHERE room_num = $room_num LIMIT 1";
}

$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
$rowroom = $result->fetch_array(MYSQLI_NUM);


if(!$rowroom[0]){
	echo "ERROR &NOROOM&";
	exit;
}

$result_json = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
$rows = array();
while($r = mysqli_fetch_assoc($result_json)) {
    $rows[] = $r;
}

echo json_encode($rows, JSON_UNESCAPED_UNICODE);

$getsqli->close();

?>