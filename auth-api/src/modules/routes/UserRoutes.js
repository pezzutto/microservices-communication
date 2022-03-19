import { Router } from "express"
import UserController from "../controller/UserController.js"
import checkToken from "../../config/db/checkToken.js"

const userRoutes = new Router()

// Este endpoint não pode passar pelo middleware
userRoutes.post('/api/user/auth', UserController.getAccessToken)

userRoutes.use(checkToken)
userRoutes.get('/api/user/email/:email', UserController.findByEmail)
userRoutes.get('/api/user/id/:id', UserController.findById)

export default userRoutes
