import express from "express";
import checkToken from "./src/config/auth/checkToken.js";
import { connectMongoDB } from './src/config/db/mongoDBConfig.js';
import { connectRabbitMQ } from './src/amqp/rabbitConfig.js';

const app = express();
const env = process.env;
const PORT = env.PORT || 8082;

// Conecta-se ao MongoDB
connectMongoDB();

// Conecta-se ao RabbitMQ
connectRabbitMQ();

app.use(checkToken)

app.listen(PORT, () => {
    console.info(`Server started succesfully at port ${PORT}`)
})

app.get('/api/status', async (req, res) => {

    return res.status(200).json({
        service: 'Sales-API',
        status: 'up',
        httpStatus: 200
    })
})

app.get('/sales/all', async (req, res) => {

    let dados = await Order.find({});
    return res.status(200).json(dados);
})