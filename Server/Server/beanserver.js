var express = require('express');
var app = express();
var server = require('http').createServer(app);
var mysql = require('mysql');
var io = require('socket.io')(server);
var port = process.env.PORT || 3000;

var getUsersInRoomNumber = function(roomName, namespace) {
    if (!namespace) namespace = '/';
    var room = io.nsps[namespace].adapter.rooms[roomName];
    if (!room) return null;
    var num = 0;
    for (var i in room) num++;
    return num;
}

var db_config = {
	host: 'localhost',
    user: 'root',
	port: '3306',
    password : 'password',
    database : 'game_bean'
};

var connection;

function handleDisconnect() {
	connection = mysql.createConnection(db_config);

	connection.connect(function(err) {
		if(err) {
			console.log('error when connecting to db:', err);
			setTimeout(handleDisconnect, 2000);
		}
	});
	
	connection.on('error', function(err) {
		console.log('db error', err);
		if(err.code === 'PROTOCOL_CONNECTION_LOST') {
			handleDisconnect();
		} else {
			throw err;
		}
	});
}

handleDisconnect();

server.listen(port, function () {
	console.log('Server listening at port %d', port);
});

app.use(express.static(__dirname + '/public'));


io.on('connection', function (socket) {

	socket.on('ready change', function (data) {
		console.log('ready change arrived');
		io.to(socket.roomnum).emit('change ready', {
			position: data
		});
		console.log('change ready sent');
	});

	socket.on('new chat', function (data) {
		console.log('new chat arrived');
		io.to(socket.roomnum).emit('user chat', {
			username: socket.username,
			message: data
		});
		console.log('user chat sent %d',socket.roomnum);
	});

	socket.on('new start', function (usernum) {
		console.log('new game started');
		io.to(socket.roomnum).emit('game start');
	});

	socket.on('add user', function (username) {
		console.log('add user arrived');
		socket.username = username;
	});

	socket.on('in channel', function (channel) {
		console.log('in channel arrived');
		socket.channel = channel;
	});

	socket.on('new kick', function (usernum) {
		console.log('new kick arrived');
		io.to(socket.roomnum).emit('new kick', {
			kicknum: usernum
		});
	});

	socket.on('start timing', function (usernum) {
		console.log('start tick arrived');
		io.to(socket.roomnum).emit('start tick');
	});

	socket.on('round ready', function (usernum) {
		console.log('round ready arrived');
		io.to(socket.roomnum).emit('new ready', {
			usernum: usernum
		});
	});

	socket.on('cancel ready', function (usernum) {
		console.log('cancel ready arrived');
		io.to(socket.roomnum).emit('cancel ready', {
			usernum: usernum
		});
	});

	socket.on('new open', function (usernum) {
		console.log('new open arrived');
		io.to(socket.roomnum).emit('new openbid', {
			usernum: usernum
		});
	});

	socket.on('subscribe', function (roomnum) {
		console.log('subscribe arrived');
		socket.roomnum = roomnum;
		socket.join(socket.roomnum);
		socket.broadcast.to(roomnum).emit('user joined');
		console.log('user joined : %s',socket.username);
		console.log('user subscribe %d',socket.roomnum);
	});
	
	socket.on('disconnect', function () {
		console.log('disconnect arrived');
		socket.broadcast.to(socket.roomnum).emit('user left');
		socket.number = getUsersInRoomNumber(socket.roomnum);
		socket.leave(socket.roomnum);
		console.log('user quit : %s , left : %d , channel : %d',socket.username, socket.number, socket.channel);
		if (socket.number < 1 && socket.channel == 1)
		{
			console.log('deleting room in channel 1');
			connection.query('DELETE FROM bean_room_i WHERE `bean_room_i`.`room_num` = '+socket.roomnum);
		}
		if (socket.number < 1 && socket.channel == 2)
		{
			console.log('deleting room in channel 2');
			connection.query('DELETE FROM bean_room_ii WHERE `bean_room_ii`.`room_num` = '+socket.roomnum);
		}
	});
});