import express from 'express';
import { addMessage, getMessage } from '../controllers/Message.js';
import { addChat, getChats, getChatsByid, deleteChatByid } from '../controllers/chats.js';
const router = express.Router();
router.post('/:id/Messages',addMessage);
router.get('/:id/Messages',getMessage);
router.post('/', addChat);
router.get('/', getChats);
router.get('/:id', getChatsByid)
router.delete('/:id', deleteChatByid)

export default router