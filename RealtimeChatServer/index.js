const express = require('express'),
    http = require('http'),
    app = express(),
    server = http.createServer(app),
    io = require('socket.io').listen(server);
app.get('/', (req, res) => {

    res.send('Chat Server sedang berjalan pada port 3000')
});

io.on('connection', (socket) => {
    console.log('pengguna terhubung!')

    socket.on('join', function (senderNickname) {
        console.log(senderNickname + " : telah bergabung ke dalam chat");

        //create a message object 
        let message = {
            "senderNickname": senderNickname,
            "message": " (telah bergabung ke dalam chat)",
        }

        // send the message to all users including the sender  using io.emit() 
        io.emit('message', message)

        // socket.broadcast.emit('userjoinedthechat', userNickname + " : telah bergabung ke dalam chat");
    })


    socket.on('messagedetection', (senderNickname, messageContent) => {
        //log the message in console 
        console.log(senderNickname + " : " + messageContent)

        //create a message object 
        let message = {
            "message": messageContent,
            "senderNickname": senderNickname
        }

        // send the message to all users including the sender  using io.emit() 
        io.emit('message', message)
    })

    socket.on('left', function (senderNickname) {
        console.log(senderNickname + " : telah meninggalkan chat");

        let message = {
            "senderNickname": senderNickname,
            "message": " (telah meninggalkan chat)"
        }

        // send the message to all users including the sender  using io.emit() 
        io.emit('message', message)

        // socket.broadcast.emit("userdisconnect", userNickname + " : telah meninggalkan chat");
    })
})

server.listen(3000, () => {
    console.log('Node app sedang berjalan pada port 3000')
})