class AuthException extends Error {

    constructor(status, message) {
        
        super(message)

        this.status = status
        this.name = "AuthException"

        Error.captureStackTrace(this.constructor)
    }
}

export default AuthException;
