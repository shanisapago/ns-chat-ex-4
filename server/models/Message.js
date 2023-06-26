import {MongoClient} from 'mongodb';
import jwt from 'jsonwebtoken';
async function addMessage(id, token, msg){
    var idNum=parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('NSChat');
        const message = db.collection('Message');
        const chat = db.collection('Chat');
        const user = db.collection('User');
        try{
            const key = "secret key"
            const data = jwt.verify(token, key);
            let chatResult = await chat.find({id:idNum}).toArray();
            let userResult = await user.find({ username: data.username }).toArray();
            let messageResult = await message.find({ }).toArray();
            if ((chatResult.length == 1) && (userResult.length == 1)) {
                var msgId
                var msgLen = messageResult.length;
                if (msgLen == 0)
                    msgId = 1
                else {
                    var lastMessId = messageResult[msgLen - 1].id
                    msgId = lastMessId + 1;
                }
               
                const createdDate=new Date();
                var month=parseInt(createdDate.getMonth()+1);
                const formattedMonth = `00${month}`.slice(-2);
                var day=createdDate.getDate();
                const formattedDay = `00${day}`.slice(-2);
                var hour=createdDate.getHours();
                const formatteHours = `00${hour}`.slice(-2);
                var minutes=createdDate.getMinutes();
                const formatteMinutes = `00${minutes}`.slice(-2);
                var seconds=createdDate.getSeconds();
                const formatteSeconds = `00${seconds}`.slice(-2);
                var date=createdDate.getFullYear()+'-'+formattedMonth+'-'+formattedDay+'T'+formatteHours+':'+formatteMinutes+':'+formatteSeconds+'.'+createdDate.getMilliseconds();
                const userData = {

                    "username": userResult[0].username,
                    "displayName": userResult[0].displayName,
                    "profilePic": userResult[0].profilePic
                }
                await message.insertOne({ id: msgId, created: date, sender: userData, content: msg })
                await chat.updateOne(
                    { id: idNum },
                    { $push: { messages: { id: msgId, created: date, sender: userData, content: msg } } }
                )
                var res = {
                    "content": msg,
                    "created": date,
                    "id": msgId,
                    "sender": userData
                   


                }



                var displaySender;
                var picSender;
                const appToken = db.collection('AppToken');
                if(chatResult[0].users[0].username!=data.username){
                    var sendTo=chatResult[0].users[0].username;
                    displaySender=chatResult[0].users[1].displayName;
                    picSender=chatResult[0].users[1].profilePic;
                }
                else{
                    var sendTo=chatResult[0].users[1].username;
                    displaySender=chatResult[0].users[0].displayName;
                    picSender=chatResult[0].users[0].profilePic;
                }
                var chatId=chatResult[0].id
                let findTokenOfUser = await appToken.find({username:sendTo}).toArray();
                if(findTokenOfUser.length>0)
                {

let bodyMsg={
    message:msg,
    username:data.username,
    displayName:displaySender,
    profilePic:"picSender",
    id:chatId
}


                    let notificationReq={
                        to:findTokenOfUser[0].token,
                        notification:{title:data.username,body:JSON.stringify(bodyMsg)}

                    }
                    let serverkey="AAAAXa9imtY:APA91bEQtJ2QIcMAgfehwpe6xbVFOzD6C2G2LX_GzLzwy0LnIez09TSeVD1T94msXMjfG7hs8FEwGnstQzHG_W5T46RyDnx9Co3AB_IayX_U9P2OtvpR6jXvaMSuAS8Lpf8vCsnI1wW0"
                    await fetch ('https://fcm.googleapis.com/fcm/send',{
                        method:'POST',
                        headers:{
                            'Authorization':`key=${serverkey}`,
                             'Content-Type':'application/json',
                            },
                            body:JSON.stringify(notificationReq),
                        })
                }
                return JSON.stringify(res);
            }
            else{
                return false;
            }
        }
        catch(err){
            return false;
        }
    }
    finally {
        await client.close();
    }
}

async function getMessage(id, token){
    var idNum=parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('NSChat');
        const message = db.collection('Message');
        const chat = db.collection('Chat');
        const user = db.collection('User');
        try{
            const key = "secret key"
            const data = jwt.verify(token, key);
            let chatResult = await chat.find({id:idNum}).toArray();
          
            let userResult = await user.find({username : data.username}).toArray();
            if((chatResult.length==1)&&(userResult.length==1)){
                return chatResult[0].messages.slice(0).reverse();
            }
            else{
                return 1;
            }
        }
        catch(err){
            return 1;
        }
    }
    finally {
        await client.close();
    }
}

export default {
    addMessage,
    getMessage
}