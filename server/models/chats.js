import { MongoClient } from "mongodb";
import jwt from 'jsonwebtoken';
async function createChat(usernameToAdd, token) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const key = "secret key"
        const data = jwt.verify(token, key);
        const usernamebyToken=data.username;
        const db = client.db('NSChat');
        const chats = db.collection('Chat');
        //check if the user exists in the database
        const users = db.collection('User');
        var userToAdd = await users.find({ username: usernameToAdd }).toArray()
        var mainUser = await users.find({ username: String(usernamebyToken) }).toArray()
        if (userToAdd.length == 1) {

            var chatAll = await chats.find({}).toArray()
            for (let i = 0; i < chatAll.length; i++) {
                if ((chatAll[i].users[0].username == userToAdd[0].username && chatAll[i].users[1].username == mainUser[0].username) || (chatAll[i].users[1].username == userToAdd[0].username && chatAll[i].users[0].username == mainUser[0].username))
                    return JSON.stringify(chatAll[i]);
            }
            var countChats
            var chatLen = chatAll.length;
            if (chatLen == 0)
                countChats = 0
            else {
                var lastChatId = chatAll[chatLen - 1].id
                countChats = lastChatId;

            }
           
            await chats.insertOne({ id: countChats + 1, users: [userToAdd[0], mainUser[0]], messages: [] })
            const res = {
                "id": countChats + 1,
                "user": {
                    "username": userToAdd[0].username,
                    "displayName": userToAdd[0].displayName,
                    "profilePic": userToAdd[0].profilePic


                }
            }
            return JSON.stringify(res);
        }
        else {

            return false;
        }
    }
    finally {
        await client.close();

    }
}

async function readChats(token) {
    
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const key = "secret key"
        const data = jwt.verify(token, key);
        const usernamebyToken=data.username;
        const db = client.db('NSChat');
        const chats = db.collection('Chat');
        var chatsUserArray = await chats.find().toArray()
        var resArray = [];
        var resjson;
        var index;      
        for (let i = 0; i < chatsUserArray.length; i++) {
            index = 1;
            if (chatsUserArray[i].users[0].username == usernamebyToken || chatsUserArray[i].users[1].username == usernamebyToken) {
                if (chatsUserArray[i].users[1].username == usernamebyToken)
                    index = 0;
                if (chatsUserArray[i].messages.length == 0) {
                    resjson = {
                        "id": chatsUserArray[i].id,
                        "user": {
                            "username": chatsUserArray[i].users[index].username,
                            "displayName": chatsUserArray[i].users[index].displayName,
                            "profilePic": chatsUserArray[i].users[index].profilePic
                        },
                        "lastMessage": null


                    }
                    resArray.push(resjson);
                }
                else {
                    resjson = {
                        "id": chatsUserArray[i].id,
                        "user": {
                            "username": chatsUserArray[i].users[index].username,
                            "displayName": chatsUserArray[i].users[index].displayName,
                            "profilePic": chatsUserArray[i].users[index].profilePic
                        },
                        "lastMessage": chatsUserArray[i].messages[chatsUserArray[i].messages.length - 1]


                    }
                    resArray.push(resjson);
                }
            }
        }
        return resArray;
        }

    finally {
        await client.close();

    }
}


async function getChatsByid(id) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('NSChat');
        const chats = db.collection('Chat');
        var chatsByidArray = await chats.find({ id: parseInt(id) }).toArray()
        if (chatsByidArray.length == 1) {
            const res = {
                "id": chatsByidArray[0].id,
                "users": chatsByidArray[0].users,
                "messages": chatsByidArray[0].messages
            }
            return res;
        }
        else {
            return false
        }
    }
    finally {
        
        await client.close();
    }
}
async function deleteChatByid(id) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        
        const db = client.db('NSChat');
        const chats = db.collection('Chat');
        const messages = db.collection('Message')
        var chat = await chats.find({ id: parseInt(id) }).toArray()
    
        for (let i = 0; i < chat[0].messages.length; i++) {
            await messages.deleteOne({ id: chat[0].messages[i].id })
        }
        var del=await chats.deleteOne({ id: parseInt(id) })
        if (del.deletedCount == 0) {
            return false
        }

        else {
            return true

        }
    }
    finally {

        await client.close();
    }
}

export default {
    createChat, readChats, getChatsByid, deleteChatByid
}
