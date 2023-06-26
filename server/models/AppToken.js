import {MongoClient} from 'mongodb';
import jwt from 'jsonwebtoken';
async function addAppToken(username, token){
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('NSChat');
        const appToken = db.collection('AppToken');
        const users = db.collection('User');
        let result = await appToken.find({username : username}).toArray();
        //let userResult = await users.find({username:username}).toArray(); 
        if(result.length===1){
            
           await appToken.updateOne({username:username},{$set:{token:token}})     
            
        }
        else{
            await appToken.insertOne({username:username,token:token})
        }
    }
    finally {
        await client.close();
    }
    return 0;
}
async function deleteAppToken(username) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        
        const db = client.db('NSChat');
        
        const appToken = db.collection('AppToken')
        var token = await appToken.find({ username: username }).toArray()
    
        if(token.length==1)
        {
                await appToken.deleteOne({ username: username })
        }
        return 0;
     
    }
    finally {

        await client.close();
    }
}

export default {
    addAppToken,deleteAppToken
}
