import jwt from "jsonwebtoken";
import UserRepository from "../repository/UserRepository.js"
import * as bcrypt from "bcrypt";
import * as httpStatus from "../../config/constants/httpStatus.js"
import * as secrets from "../../config/constants/secrets.js"
import UserException from "../exception/UserException.js"
import User from "../model/User.js"

class UserService {

    // Busca usuario pelo Id
    async findById(req) {

        try {
            const { id } = req.params
            this.validateRequestId(id)

            let user = await UserRepository.findById(id)
            this.validateUserNotFound(user)

            if (user){
                return {
                    status: httpStatus.SUCCESS,
                    user: {
                        id: user.id,
                        name: user.name,
                        email: user.email
                    }
                }
            }
        }
        catch(ex) {
            return {
                status: ex.status ? ex.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: ex.status
            }
        }
    }

    // Busca usuario pelo email
    async findByEmail(req) {

        try {
            const { email } = req.params
            const { authUser } = req

            this.validateRequestEmail(email)

            let user = await UserRepository.findByEmail(email)
            this.validateUserNotFound(user)

            // Verifica se o usuario autenticado está acessando os dados dele
            if(user.id !== authUser.id) {
                throw new UserException(httpStatus.FORBIDDEN, "Inválid token")
            }

            if (user){
                return {
                    status: httpStatus.SUCCESS,
                    user: {
                        id: user.id,
                        name: user.name,
                        email: user.email
                    }
                }
            }
        }
        catch(ex) {
            return {
                status: ex.status ? ex.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: ex.message
            }
        }
    }

    // Obtem access token
    async getAccessToken(req) {
        
        // Obtem email e password
        const { email, password } = req.body

        try {
            // Verifica parametros
            if (!email || !password) {
                throw new UserException(httpStatus.UNAUTHORIZED, "Email and Password are required")
            }

            const user = await UserRepository.findByEmail(email)
            
            if (!user) {
                throw new UserException(httpStatus.BAD_REQUEST, "User not found")
            }

            // Valida o password
            if (!await this.validatePassword(password, user.password)){
                throw new UserException(httpStatus.BAD_REQUEST, "Password doesn't match")
            }

            // Objeto do usuario autenticado
            const authUser = {
                id: user.id,
                name: user.name,
                email: user.email
            }
            const accessToken = jwt.sign({authUser}, secrets.apiSecret, {expiresIn: "1d"})

            return {
                status: httpStatus.SUCCESS,
                accessToken: accessToken
            }
        }
        catch(ex){
            return {
                status: ex.status || httpStatus.INTERNAL_SERVER_ERROR,
                message: ex.message
            }
        }
    }

    async validatePassword(password, hashPassword) {
        return await bcrypt.compare(password, hashPassword);
    }

    validateAccessTokenParam(email, password) {

        if (!email || !password) {

            console.log('vai sair pela exception')

            throw new UserException(httpStatus.UNAUTHORIZED, "Email and Password must be informed")
        }
    }

    validateRequestId(id) {
        if (!id)
            throw new UserException(httpStatus.BAD_REQUEST, "User Id was not informed")
    }
    
    validateRequestEmail(email) {
        if (!email)
            throw new UserException(httpStatus.BAD_REQUEST, "User email was not informed")
    }


    validateUserNotFound(user) {
        if (!user)
            throw new UserException(httpStatus.BAD_REQUEST, "User not found")
    }
}

export default new UserService()