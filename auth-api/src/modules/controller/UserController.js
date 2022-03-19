import UserException from "../exception/UserException.js"
import UserService from "../service/UserService.js"

class UserController {

    async findById(req, res) {
        let user = await UserService.findById(req)
        return res.status(user.status).json(user)
    }

    async findByEmail(req, res) {
        let user = await UserService.findByEmail(req)
        return res.status(user.status).json(user)
    }

    async getAccessToken(req, res) {
        console.log(req.body)
        let auth = await UserService.getAccessToken(req)
        return res.status(auth.status).json(auth)
    }
}

export default new UserController