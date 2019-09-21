<?php

header("Content-Type: text/html; charset=UTF-8");

$usernum = $_REQUEST["user_num"];
$userhello = $_REQUEST["user_hello"];

if (!$usernum){
echo "ERROR";
exit;
}

$DB['host'] = 'localhost';
$DB['db'] = 'game_bean';
$DB['id'] = 'game_php';
$DB['pw'] = 'password';

$msqli = new mysqli($DB['host'], $DB['id'], $DB['pw'], $DB['db']);
mysqli_query($msqli,"set names utf8");
mysqli_query($msqli,"set session character_set_connection=utf8;");
mysqli_query($msqli,"set session character_set_results=utf8;");
mysqli_query($msqli,"set session character_set_client=utf8;");

$q = "UPDATE bean_user SET user_hello = '".$userhello."' where user_num = '".$usernum."'";

$result = mysqli_query($msqli, $q) or die("ERROR");

echo "SUCCESS";

$msqli->close();

?>