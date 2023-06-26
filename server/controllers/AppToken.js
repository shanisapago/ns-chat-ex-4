import AppTokenModal from '../models/AppToken.js'

async function addAppToken(req,res){
    const result = await AppTokenModal.addAppToken(req.body.username,req.body.token);
    res.status(200);
    res.end();
   
}
async function deleteAppToken(req,res){
    const result = await AppTokenModal.deleteAppToken(req.params.id);
    res.status(200);
    res.end();
}
export{
    addAppToken,deleteAppToken
}