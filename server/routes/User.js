import express from 'express';
import { addUser, userDetails } from '../controllers/User.js';
const router = express.Router();
router.post('/',addUser);
router.get('/:id',userDetails);

export default router