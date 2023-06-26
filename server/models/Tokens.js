 import {MongoClient} from 'mongodb';
import jwt from 'jsonwebtoken';
async function createToken(username, password){
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('NSChat');
        const usersPass = db.collection('UserPass');
        let result = await usersPass.find({username : username}).toArray();
        if(result.length===1){
            if(result[0].password===password){
                const key = "secret key"
                const data = { username: username}
                const token = jwt.sign(data, key)
                return token;
            }
        }
    }
    finally {
        await client.close();
    }
    return 0;
}

export default {
    createToken
}
