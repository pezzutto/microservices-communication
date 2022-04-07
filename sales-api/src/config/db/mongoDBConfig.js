import mongoose from "mongoose";

const env = process.env;
const MONGO_DB_URL = env.MONGO_DB_URL 
                   ? env.MONGO_DB_URL
                   : "mongodb://admin:admin123@127.0.0.1:27017/sales-db";

export function connectMongoDB() {


    mongoose.connect(MONGO_DB_URL, {
        useNewUrlParser: true,
        useUnifiedTopology: true
    });

    mongoose.connection.on('connected', () => {
        console.info('Connected to MongoDB')
    })

    mongoose.connection.on('error', err => {
        console.error(err);
    });
}