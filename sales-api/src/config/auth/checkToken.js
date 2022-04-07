import jwt from "jsonwebtoken"
import { promisify } from "util"
import { API_SECRET } from '../constants/secrets.js';
import { UNAUTHORIZED, INTERNAL_SERVER_ERROR } from "../constants/httpStatus.js"
import AuthException from '../auth/AuthException.js';

const bearer = "bearer "

export default async (req, res, next) => {
    
    try {
        console.log('entrou no middleware')    
        const { authorization } = req.headers
        if (!authorization){
            throw new AuthException(UNAUTHORIZED, "Access token not informed")
        }
        let accessToken = authorization
        if (accessToken.includes(bearer)) {
            accessToken = accessToken.replace(bearer, "").trim()
        }

        const decoded = await promisify(jwt.verify)(accessToken, API_SECRET)
        req.authUser = decoded.authUser
        return next()
    }
    catch(ex) {

        return res.status(ex.status).json({
            status: ex.status ? ex.status : INTERNAL_SERVER_ERROR,
            message: ex.message
        })
    }
}