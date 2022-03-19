class UserException extends Error {

    constructor(status, message) {
        
        super(message)

        this.status = status
        this.name = "UserException"

        Error.captureStackTrace(this.constructor)
    }
}

export default UserException;
