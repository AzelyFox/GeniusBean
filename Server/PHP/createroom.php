<?php

//header("Content-Type: text/html; charset=UTF-8");

$user_num = $_REQUEST["user_num"];
$user_channel = $_REQUEST["user_channel"];
$room_name = $_REQUEST["user_room_name"];
$room_pw = $_REQUEST["user_room_pw"];
$room_bat = $_REQUEST["user_room_bat"];

if (!$user_num | !$user_channel | !$room_name){
echo "ERROR";
exit;
}

if ($room_pw == null | !$room_pw){
	$room_pw = 0;
}
if (!$room_bat){
	$room_bat = 0;
}

$DB['host'] = 'localhost';
$DB['db'] = 'game_bean';
$DB['id'] = 'game_php';
$DB['pw'] = 'password';

$makesqli = new mysqli($DB['host'], $DB['id'], $DB['pw'], $DB['db']);
mysqli_query($makesqli,"set names utf8");

$q1 = "";
$q2 = "UPDATE bean_channel SET channel_user = channel_user + 1 WHERE channel_num = $user_channel";
$q3 = "";

if ($user_channel == 1){
	$q1 = "INSERT INTO bean_room_i ( room_name, room_pw, room_bat, room_user_num_i ) VALUES ( '$room_name', $room_pw, $room_bat, $user_num )";
	$q3 = "select room_num from bean_room_i WHERE room_name = '$room_name' LIMIT 1";
}
if ($user_channel == 2){
	$q1 = "INSERT INTO bean_room_ii ( room_name, room_pw, room_bat, room_user_num_i ) VALUES ( '$room_name', $room_pw, $room_bat, $user_num )";
	$q3 = "select room_num from bean_room_ii WHERE room_name = '$room_name' LIMIT 1";
}

$result = mysqli_query($makesqli, $q1) or die(mysqli_error($makesqli));
$result = mysqli_query($makesqli, $q2) or die(mysqli_error($makesqli));
$result = mysqli_query($makesqli, $q3) or die(mysqli_error($makesqli));

$rowroom = $result->fetch_array(MYSQLI_NUM);

echo "&SUCCESS&";
echo $rowroom[0];

$makesqli->close();

?>