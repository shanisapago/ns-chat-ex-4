import bodyParser from 'body-parser';
import express from 'express';
import http from 'http';
import routerUser from './routes/User.js'
import routerToken from './routes/Tokens.js'
import routerChats from './routes/Chats.js'
import routerFireBase from './routes/AppToken.js'
import cors from 'cors'
import { Server } from "socket.io";

const app = express()
const server = http.createServer(app);
const io = new Server(server);
app.use(bodyParser());
app.use(express.static('public'))
app.use('/api/Tokens',routerToken);
app.use('/api/Users', routerUser);
app.use('/api/Chats',routerChats);
app.use('/api/AppToken',routerFireBase);

app.use(cors()); 
io.on("connection", (socket) => {
    socket.on("join_room", (data) => {
        socket.join(data);
    });

    socket.on("send_message", (data) => {
        socket.to(data.room).emit("receive_message", data);
    });

    socket.on("add_contact", (data) => {
        io.emit("update_contact", data);
    });

});
server.listen(3000);