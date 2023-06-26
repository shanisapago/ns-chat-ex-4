import UserModel from '../models/User.js'

async function addUser(req,res){
    const result = await UserModel.addUser(req.body.username,req.body.password, req.body.displayName, req.body.profilePic);
    if (!result){
        res.status(409);
    }
    res.end();
}

async function userDetails (req,res){
    const result = await UserModel.userDetails(req.headers.authorization.split(" ")[1], req.params.id);
    if (result==0){
        res.status(401);
        res.end();
    }
    else {
        res.end(result);
    }
}

export{
    addUser,
    userDetails
}