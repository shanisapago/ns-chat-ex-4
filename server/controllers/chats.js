import chatsModel from '../models/chats.js'

async function addChat(req, res) {
    const chats = await chatsModel.createChat(req.body.username, req.headers.authorization.split(" ")[1]);

   
    if (chats == false) {
        res.status(400)
        res.end("No such user");

    }
    else {  
    res.end(chats)
}

   
}

async function getChats(req, res) {


    const chats = await chatsModel.readChats(req.headers.authorization.split(" ")[1]);
   
    res.end(JSON.stringify(chats))
    
}
async function getChatsByid(req, res) {
    var id = req.params.id 
    const chatById = await chatsModel.getChatsByid(id)
    if (chatById == false) {
        res.status(401)
        res.end()
    }
    else {
        res.end(JSON.stringify(chatById))
    }
 
}
async function deleteChatByid(req, res) {
    var id = req.params.id
    const resDelete = await chatsModel.deleteChatByid(id)
    if (!resDelete)
        res.status(404)
    else
        res.status(204)
    res.end()
  
}

export {
    addChat, getChats, getChatsByid, deleteChatByid
}