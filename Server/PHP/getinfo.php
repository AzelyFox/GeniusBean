<?php

//header("Content-Type: text/html; charset=UTF-8");

$usernum = $_REQUEST["user_num"];

if (!$usernum){
echo "ERROR";
exit;
}

$DB['host'] = 'localhost';
$DB['db'] = 'game_bean';
$DB['id'] = 'game_php';
$DB['pw'] = 'password';

$getsqli = new mysqli($DB['host'], $DB['id'], $DB['pw'], $DB['db']);
mysqli_query($getsqli,"set names utf8");

$q = "select user_class,user_lv,user_exp,user_money,user_cash,user_played,user_win,user_lose,user_grade,user_rating,user_skin,user_guild,user_hello from bean_user where user_num = '".$usernum."' limit 1";

$result = mysqli_query($getsqli, $q) or die("ERROR");

$rows = array();
while($r = mysqli_fetch_assoc($result)) {
    $rows[] = $r;
}


echo json_encode($rows, JSON_UNESCAPED_UNICODE);

$getsqli->close();

?>