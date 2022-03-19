import jwt from "jsonwebtoken"
import { promisify } from "util"
import * as secrets from "../constants/secrets.js"
import * as httpStatus from "../constants/httpStatus.js"
import AuthException from '../../modules/exception/AuthException.js'

const bearer = "bearer "

export default async (req, res, next) => {
    
    try {
        console.log('entrou no middleware')    
        const { authorization } = req.headers
        if (!authorization){
            throw new AuthException(httpStatus.UNAUTHORIZED, "Access token not informed")
        }
        let accessToken = authorization
        if (accessToken.includes(bearer)) {
            accessToken = accessToken.replace(bearer, "").trim()
        }

        const decoded = await promisify(jwt.verify)(accessToken, secrets.apiSecret)
        req.authUser = decoded.authUser
        return next()
    }
    catch(ex) {

        return res.status(ex.status).json({
            status: ex.status ? ex.status : httpStatus.INTERNAL_SERVER_ERROR,
            message: ex.message
        })
    }
}