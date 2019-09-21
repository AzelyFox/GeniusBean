<?php

$user_me = $_REQUEST["user_me"];
$user_num = $_REQUEST["user_num"];
$room_num = $_REQUEST["room_num"];
$room_channel = $_REQUEST["room_channel"];
$room_round = $_REQUEST["room_round"];

if (!$user_me | !$room_num | !$user_num | !$room_channel | !$room_round){
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

if($room_channel == 1){
	if ($user_me == 1){
		$q = "select room_user_num_i,room_bean_user_i,room_round from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($user_me == 2){
		$q = "select room_user_num_ii,room_bean_user_ii,room_round from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($user_me == 3){
		$q = "select room_user_num_iii,room_bean_user_iii,room_round from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($user_me == 4){
		$q = "select room_user_num_iv,room_bean_user_iv,room_round from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($user_me == 5){
		$q = "select room_user_num_v,room_bean_user_vi,room_round from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($user_me == 6){
		$q = "select room_user_num_vi,room_bean_user_vi,room_round from bean_room_i WHERE room_num = $room_num LIMIT 1" ;
	}
	
}
if($room_channel == 2){
	if ($user_me == 1){
		$q = "select room_user_num_i,room_bean_user_i,room_round from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($user_me == 2){
		$q = "select room_user_num_ii,room_bean_user_ii,room_round from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($user_me == 3){
		$q = "select room_user_num_iii,room_bean_user_iii,room_round from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($user_me == 4){
		$q = "select room_user_num_iv,room_bean_user_iv,room_round from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($user_me == 5){
		$q = "select room_user_num_v,room_bean_user_vi,room_round from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
	if ($user_me == 6){
		$q = "select room_user_num_vi,room_bean_user_vi,room_round from bean_room_ii WHERE room_num = $room_num LIMIT 1" ;
	}
}

$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");
$rowroom = $result->fetch_array(MYSQLI_NUM);

if ($rowroom[0] != $user_num){ 
	echo "ERROR : nouser";
	exit;
}

if ($rowroom[1] <= 0){
	echo "ERROR : nobean";
	exit;
}

$room_round = $rowroom[2];

if($room_channel == 1){
	if ($user_me == 1){
		if ($room_round == 1){
			$q = "UPDATE bean_room_i SET room_bean_user_i = room_bean_user_i - 1 , room_usedbean_round_i_user_i = room_usedbean_round_i_user_i + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 2){
			$q = "UPDATE bean_room_i SET room_bean_user_i = room_bean_user_i - 1 , room_usedbean_round_ii_user_i = room_usedbean_round_ii_user_i + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 3){
			$q = "UPDATE bean_room_i SET room_bean_user_i = room_bean_user_i - 1 , room_usedbean_round_iii_user_i = room_usedbean_round_iii_user_i + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 4){
			$q = "UPDATE bean_room_i SET room_bean_user_i = room_bean_user_i - 1 , room_usedbean_round_iv_user_i = room_usedbean_round_iv_user_i + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 5){
			$q = "UPDATE bean_room_i SET room_bean_user_i = room_bean_user_i - 1 , room_usedbean_round_v_user_i = room_usedbean_round_v_user_i + 1 WHERE room_num = $room_num";
		}
	}
	if ($user_me == 2){
		if ($room_round == 1){
			$q = "UPDATE bean_room_i SET room_bean_user_ii = room_bean_user_ii - 1 , room_usedbean_round_i_user_ii = room_usedbean_round_i_user_ii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 2){
			$q = "UPDATE bean_room_i SET room_bean_user_ii = room_bean_user_ii - 1 , room_usedbean_round_ii_user_ii = room_usedbean_round_ii_user_ii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 3){
			$q = "UPDATE bean_room_i SET room_bean_user_ii = room_bean_user_ii - 1 , room_usedbean_round_iii_user_ii = room_usedbean_round_iii_user_ii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 4){
			$q = "UPDATE bean_room_i SET room_bean_user_ii = room_bean_user_ii - 1 , room_usedbean_round_iv_user_ii = room_usedbean_round_iv_user_ii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 5){
			$q = "UPDATE bean_room_i SET room_bean_user_ii = room_bean_user_ii - 1 , room_usedbean_round_v_user_ii = room_usedbean_round_v_user_ii + 1 WHERE room_num = $room_num";
		}
	}
	if ($user_me == 3){
		if ($room_round == 1){
			$q = "UPDATE bean_room_i SET room_bean_user_iii = room_bean_user_iii - 1 , room_usedbean_round_i_user_iii = room_usedbean_round_i_user_iii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 2){
			$q = "UPDATE bean_room_i SET room_bean_user_iii = room_bean_user_iii - 1 , room_usedbean_round_ii_user_iii = room_usedbean_round_ii_user_iii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 3){
			$q = "UPDATE bean_room_i SET room_bean_user_iii = room_bean_user_iii - 1 , room_usedbean_round_iii_user_iii = room_usedbean_round_iii_user_iii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 4){
			$q = "UPDATE bean_room_i SET room_bean_user_iii = room_bean_user_iii - 1 , room_usedbean_round_iv_user_iii = room_usedbean_round_iv_user_iii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 5){
			$q = "UPDATE bean_room_i SET room_bean_user_iii = room_bean_user_iii - 1 , room_usedbean_round_v_user_iii = room_usedbean_round_v_user_iii + 1 WHERE room_num = $room_num";
		}
	}
	if ($user_me == 4){
		if ($room_round == 1){
			$q = "UPDATE bean_room_i SET room_bean_user_iv = room_bean_user_iv - 1 , room_usedbean_round_i_user_iv = room_usedbean_round_i_user_iv + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 2){
			$q = "UPDATE bean_room_i SET room_bean_user_iv = room_bean_user_iv - 1 , room_usedbean_round_ii_user_iv = room_usedbean_round_ii_user_iv + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 3){
			$q = "UPDATE bean_room_i SET room_bean_user_iv = room_bean_user_iv - 1 , room_usedbean_round_iii_user_iv = room_usedbean_round_iii_user_iv + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 4){
			$q = "UPDATE bean_room_i SET room_bean_user_iv = room_bean_user_iv - 1 , room_usedbean_round_iv_user_iv = room_usedbean_round_iv_user_iv + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 5){
			$q = "UPDATE bean_room_i SET room_bean_user_iv = room_bean_user_iv - 1 , room_usedbean_round_v_user_iv = room_usedbean_round_v_user_iv + 1 WHERE room_num = $room_num";
		}
	}
	if ($user_me == 5){
		if ($room_round == 1){
			$q = "UPDATE bean_room_i SET room_bean_user_v = room_bean_user_v - 1 , room_usedbean_round_i_user_v = room_usedbean_round_i_user_v + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 2){
			$q = "UPDATE bean_room_i SET room_bean_user_v = room_bean_user_v - 1 , room_usedbean_round_ii_user_v = room_usedbean_round_ii_user_v + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 3){
			$q = "UPDATE bean_room_i SET room_bean_user_v = room_bean_user_v - 1 , room_usedbean_round_iii_user_v = room_usedbean_round_iii_user_v + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 4){
			$q = "UPDATE bean_room_i SET room_bean_user_v = room_bean_user_v - 1 , room_usedbean_round_iv_user_v = room_usedbean_round_iv_user_v + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 5){
			$q = "UPDATE bean_room_i SET room_bean_user_v = room_bean_user_v - 1 , room_usedbean_round_v_user_v = room_usedbean_round_v_user_v + 1 WHERE room_num = $room_num";
		}
	}
	if ($user_me == 6){
		if ($room_round == 1){
			$q = "UPDATE bean_room_i SET room_bean_user_vi = room_bean_user_vi - 1 , room_usedbean_round_i_user_vi = room_usedbean_round_i_user_vi + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 2){
			$q = "UPDATE bean_room_i SET room_bean_user_vi = room_bean_user_vi - 1 , room_usedbean_round_ii_user_vi = room_usedbean_round_ii_user_vi + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 3){
			$q = "UPDATE bean_room_i SET room_bean_user_vi = room_bean_user_vi - 1 , room_usedbean_round_iii_user_vi = room_usedbean_round_iii_user_vi + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 4){
			$q = "UPDATE bean_room_i SET room_bean_user_vi = room_bean_user_vi - 1 , room_usedbean_round_iv_user_vi = room_usedbean_round_iv_user_vi + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 5){
			$q = "UPDATE bean_room_i SET room_bean_user_vi = room_bean_user_vi - 1 , room_usedbean_round_v_user_vi = room_usedbean_round_v_user_vi + 1 WHERE room_num = $room_num";
		}
	}
	
}
if($room_channel == 2){
	if ($user_me == 1){
		if ($room_round == 1){
			$q = "UPDATE bean_room_ii SET room_bean_user_i = room_bean_user_i - 1 , room_usedbean_round_i_user_i = room_usedbean_round_i_user_i + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 2){
			$q = "UPDATE bean_room_ii SET room_bean_user_i = room_bean_user_i - 1 , room_usedbean_round_ii_user_i = room_usedbean_round_ii_user_i + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 3){
			$q = "UPDATE bean_room_ii SET room_bean_user_i = room_bean_user_i - 1 , room_usedbean_round_iii_user_i = room_usedbean_round_iii_user_i + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 4){
			$q = "UPDATE bean_room_ii SET room_bean_user_i = room_bean_user_i - 1 , room_usedbean_round_iv_user_i = room_usedbean_round_iv_user_i + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 5){
			$q = "UPDATE bean_room_ii SET room_bean_user_i = room_bean_user_i - 1 , room_usedbean_round_v_user_i = room_usedbean_round_v_user_i + 1 WHERE room_num = $room_num";
		}
	}
	if ($user_me == 2){
		if ($room_round == 1){
			$q = "UPDATE bean_room_ii SET room_bean_user_ii = room_bean_user_ii - 1 , room_usedbean_round_i_user_ii = room_usedbean_round_i_user_ii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 2){
			$q = "UPDATE bean_room_ii SET room_bean_user_ii = room_bean_user_ii - 1 , room_usedbean_round_ii_user_ii = room_usedbean_round_ii_user_ii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 3){
			$q = "UPDATE bean_room_ii SET room_bean_user_ii = room_bean_user_ii - 1 , room_usedbean_round_iii_user_ii = room_usedbean_round_iii_user_ii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 4){
			$q = "UPDATE bean_room_ii SET room_bean_user_ii = room_bean_user_ii - 1 , room_usedbean_round_iv_user_ii = room_usedbean_round_iv_user_ii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 5){
			$q = "UPDATE bean_room_ii SET room_bean_user_ii = room_bean_user_ii - 1 , room_usedbean_round_v_user_ii = room_usedbean_round_v_user_ii + 1 WHERE room_num = $room_num";
		}
	}
	if ($user_me == 3){
		if ($room_round == 1){
			$q = "UPDATE bean_room_ii SET room_bean_user_iii = room_bean_user_iii - 1 , room_usedbean_round_i_user_iii = room_usedbean_round_i_user_iii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 2){
			$q = "UPDATE bean_room_ii SET room_bean_user_iii = room_bean_user_iii - 1 , room_usedbean_round_ii_user_iii = room_usedbean_round_ii_user_iii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 3){
			$q = "UPDATE bean_room_ii SET room_bean_user_iii = room_bean_user_iii - 1 , room_usedbean_round_iii_user_iii = room_usedbean_round_iii_user_iii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 4){
			$q = "UPDATE bean_room_ii SET room_bean_user_iii = room_bean_user_iii - 1 , room_usedbean_round_iv_user_iii = room_usedbean_round_iv_user_iii + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 5){
			$q = "UPDATE bean_room_ii SET room_bean_user_iii = room_bean_user_iii - 1 , room_usedbean_round_v_user_iii = room_usedbean_round_v_user_iii + 1 WHERE room_num = $room_num";
		}
	}
	if ($user_me == 4){
		if ($room_round == 1){
			$q = "UPDATE bean_room_ii SET room_bean_user_iv = room_bean_user_iv - 1 , room_usedbean_round_i_user_iv = room_usedbean_round_i_user_iv + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 2){
			$q = "UPDATE bean_room_ii SET room_bean_user_iv = room_bean_user_iv - 1 , room_usedbean_round_ii_user_iv = room_usedbean_round_ii_user_iv + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 3){
			$q = "UPDATE bean_room_ii SET room_bean_user_iv = room_bean_user_iv - 1 , room_usedbean_round_iii_user_iv = room_usedbean_round_iii_user_iv + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 4){
			$q = "UPDATE bean_room_ii SET room_bean_user_iv = room_bean_user_iv - 1 , room_usedbean_round_iv_user_iv = room_usedbean_round_iv_user_iv + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 5){
			$q = "UPDATE bean_room_ii SET room_bean_user_iv = room_bean_user_iv - 1 , room_usedbean_round_v_user_iv = room_usedbean_round_v_user_iv + 1 WHERE room_num = $room_num";
		}
	}
	if ($user_me == 5){
		if ($room_round == 1){
			$q = "UPDATE bean_room_ii SET room_bean_user_v = room_bean_user_v - 1 , room_usedbean_round_i_user_v = room_usedbean_round_i_user_v + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 2){
			$q = "UPDATE bean_room_ii SET room_bean_user_v = room_bean_user_v - 1 , room_usedbean_round_ii_user_v = room_usedbean_round_ii_user_v + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 3){
			$q = "UPDATE bean_room_ii SET room_bean_user_v = room_bean_user_v - 1 , room_usedbean_round_iii_user_v = room_usedbean_round_iii_user_v + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 4){
			$q = "UPDATE bean_room_ii SET room_bean_user_v = room_bean_user_v - 1 , room_usedbean_round_iv_user_v = room_usedbean_round_iv_user_v + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 5){
			$q = "UPDATE bean_room_ii SET room_bean_user_v = room_bean_user_v - 1 , room_usedbean_round_v_user_v = room_usedbean_round_v_user_v + 1 WHERE room_num = $room_num";
		}
	}
	if ($user_me == 6){
		if ($room_round == 1){
			$q = "UPDATE bean_room_ii SET room_bean_user_vi = room_bean_user_vi - 1 , room_usedbean_round_i_user_vi = room_usedbean_round_i_user_vi + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 2){
			$q = "UPDATE bean_room_ii SET room_bean_user_vi = room_bean_user_vi - 1 , room_usedbean_round_ii_user_vi = room_usedbean_round_ii_user_vi + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 3){
			$q = "UPDATE bean_room_ii SET room_bean_user_vi = room_bean_user_vi - 1 , room_usedbean_round_iii_user_vi = room_usedbean_round_iii_user_vi + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 4){
			$q = "UPDATE bean_room_ii SET room_bean_user_vi = room_bean_user_vi - 1 , room_usedbean_round_iv_user_vi = room_usedbean_round_iv_user_vi + 1 WHERE room_num = $room_num";
		}
		if ($room_round == 5){
			$q = "UPDATE bean_room_ii SET room_bean_user_vi = room_bean_user_vi - 1 , room_usedbean_round_v_user_vi = room_usedbean_round_v_user_vi + 1 WHERE room_num = $room_num";
		}
	}
}

$result = mysqli_query($getsqli, $q) or die("ERROR_MYSQLI");

echo "SUCCESS";

$getsqli->close();

?>