import amqp from 'amqplib/callback_api.js';
import * as rabbit from '../config/constants/rabbitConstants.js';

export async function connectRabbitMQ() {

    amqp.connect(rabbit.RABBITMQ_URL, (error, connection) => {

        if (error) throw error

        // Conecta a fila de produtos
        createQueue(
            connection,
            rabbit.PRODUCT_STOCK_UPDATE_QUEUE,
            rabbit.PRODUCT_STOCK_UPDATE_ROUTING_KEY,
            rabbit.PRODUCT_TOPIC
        )

        // Conecta a fila de sales
        createQueue(
            connection,
            rabbit.SALES_CONFIRMATION_QUEUE,
            rabbit.SALES_CONFIRMATION_ROUTING_KEY,
            rabbit.PRODUCT_TOPIC
        )

        setTimeout(() => {
            connection.close()
        }, 500)

    })

    function createQueue(connection, queue, routingKey, topic) {

        connection.createChannel((error, channel) => {

            if (error) 
                throw error;
                
            channel.assertExchange(topic, "topic", {durable: true})
            
            channel.assertQueue(queue, {durable: true})
            
            channel.bindQueue(queue, topic, routingKey)
        })

    }

}