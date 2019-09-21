<?php

//header("Content-Type: text/html; charset=UTF-8");

$email = $_REQUEST["user_email"];
$nick = $_REQUEST["user_nick"];
$sobid = $_REQUEST["user_sobid"];

if (!$email | !$nick){
echo "ERROR";
exit;
}

$DB['host'] = 'localhost';
$DB['db'] = 'game_bean';
$DB['id'] = 'game_php';
$DB['pw'] = 'password';

$checksqli = new mysqli($DB['host'], $DB['id'], $DB['pw'], $DB['db']);
mysqli_query($checksqli,"set names utf8");
mysqli_set_charset($checksqli, 'utf8');

$q = "select user_num from bean_user where user_nick = '".$nick."' limit 1";

$result = mysqli_query($checksqli, $q) or die(mysql_error());
$row = $result->fetch_array(MYSQLI_NUM);


if($row[0]){
	echo "&DUPLICATE&";
	exit;
}

$q = "INSERT INTO bean_user ( user_nick, user_email, user_sobid ) VALUES ( '$nick', '$email', '$sobid' )";

$result = mysqli_query($checksqli, $q) or die(mysqli_error($checksqli));

echo "&SUCCESS&";

$checksqli->close();

?>