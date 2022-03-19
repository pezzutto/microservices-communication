import express from "express";

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

app.listen(PORT, () => {
    console.info(`Server started succesfully at port ${PORT}`)
})

app.get('/api/status', (req, res) => {

    return res.status(200).json({
        service: 'Sales-API',
        status: 'up',
        httpStatus: 200
    })
})