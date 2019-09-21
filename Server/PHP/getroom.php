<?php

//header("Content-Type: text/html; charset=UTF-8");

$user_channel = $_REQUEST["user_channel"];

if (!$user_channel){
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
	$q = "select room_num,room_state,room_current,room_name,room_pw,room_bat from bean_room_i";
}
if($user_channel == 2){
	$q = "select room_num,room_state,room_current,room_name,room_pw,room_bat from bean_room_ii";
}

$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
$rows = array();
while($r = mysqli_fetch_assoc($result)) {
    $rows[] = $r;
}


if(!$rows[0]){
	echo "&NOROOM&";
	exit;
}

echo json_encode($rows, JSON_UNESCAPED_UNICODE);

$getsqli->close();

?>