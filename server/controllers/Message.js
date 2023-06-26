import MessageModel from '../models/Message.js'

async function addMessage(req,res){

    const result = await MessageModel.addMessage(req.params.id, req.headers.authorization.split(" ")[1], req.body.msg);
    if (!result) {
        res.status(401)
        res.end();
    }
    else {
        res.end(result);
    }
    
}

async function getMessage(req,res){
    const result = await MessageModel.getMessage(req.params.id, req.headers.authorization.split(" ")[1]);
    if (result == 1) {
        res.status(401)
        res.end();
    }
    else {
        res.end(JSON.stringify(result));
    }
}

export{
    addMessage,
    getMessage
}