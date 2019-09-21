<?php

//header("Content-Type: text/html; charset=UTF-8");

$email = $_REQUEST["user_email"];
$sobid = $_REQUEST["user_sobid"];

if (!$email){
echo "ERROR";
exit;
}

$DB['host'] = 'localhost';
$DB['db'] = 'game_bean';
$DB['id'] = 'game_php';
$DB['pw'] = 'password';

$logsqli = new mysqli($DB['host'], $DB['id'], $DB['pw'], $DB['db']);
mysqli_query($logsqli,"set names utf8");

$q = "select user_num,user_nick from bean_user where user_email = '".$email."' limit 1";

$result = mysqli_query($logsqli, $q) or die("ERROR");
//$row = $result->fetch_array(MYSQLI_NUM);

$rows = array();
while($r = mysqli_fetch_assoc($result)) {
    $rows[] = $r;
}


if(!$rows[0]){
	echo "&NEW USER&";
	exit;
}

$q = "update bean_user set user_sobid = '".$sobid."' where user_email = '".$email."'";

$result = mysqli_query($logsqli, $q) or die("ERROR");

echo json_encode($rows, JSON_UNESCAPED_UNICODE);

$logsqli->close();

?>