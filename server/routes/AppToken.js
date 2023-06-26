import express from 'express';
import { addAppToken,deleteAppToken }from '../controllers/AppToken.js';


const router = express.Router();
router.post('/',addAppToken);
router.delete('/:id',deleteAppToken);


export default router