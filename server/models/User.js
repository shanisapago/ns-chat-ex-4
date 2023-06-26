
import {MongoClient} from 'mongodb';
import jwt from 'jsonwebtoken';
async function addUser(username, password, displayName, profilePic){
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('NSChat');
        const usersPassName = db.collection('UserPassName');
        let result = await usersPassName.find({username : username}).toArray();
        if(result.length===1){
            return false;
        }
        await usersPassName.insertOne({ username : username, password : password, displayName : displayName, profilePic : profilePic})
        const usersPass = db.collection('UserPass');
        await usersPass.insertOne({ username : username, password : password})
        const users = db.collection('User');
        await users.insertOne({ username : username, displayName : displayName, profilePic : profilePic})
    }
    finally {
        await client.close();
    }
    return true;
}

async function userDetails(token, username){
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('NSChat');
        const user = db.collection('User');
        try{
            const key = "secret key"
            const data = jwt.verify(token, key);
            if(data.username==username){
                let result = await user.find({username : username}).toArray();
                const data={
                    username: result[0].username,
                    displayName: result[0].displayName,
                    profilePic:result[0].profilePic
                }
                return JSON.stringify(data);
            }
        }
        catch(err){
            return 0;
        }
    }
    finally {
        await client.close();
    }
}

export default {
    addUser,
    userDetails
}
