//package ula;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import java.util.concurrent.TimeoutException;

/**
    This class acts as a Sender to send the created logs to a RabbitMQ server
    * @author Jaime Alvarez
    * @version 1.0
*/
public class Sender {
    
    private final static String QUEUE_NAME = "hello";
    
    protected Sender() {
        
    }
    
    /**
     * Sends a log to a message queue.
     * @param record
     * @throws java.io.IOException
     * @throws TimeoutException 
     */
    protected void send(String record) throws java.io.IOException, TimeoutException {
        
        /* Create a connection to the server
           The connection abstracts the socket connection, and takes care of 
           protocol version negotiation and authentication and so on for us.
        */        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // Specify IP or hostname if connecting to remote machine
        Connection connection = factory.newConnection();
        
        /*  Create a channel, which is where most of the API for getting things done resides. */
        Channel channel = connection.createChannel();
                        
        /*  To send, we must declare a queue for us to send to,
            then we can publish a message to the queue.
        
            Declaring a queue is idempotent - it will only be created if it doesn't exist already. 
            The message content is a byte array, so you can encode whatever you like there.
        */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        
        channel.basicPublish("", QUEUE_NAME, null, record.getBytes());
        System.out.println(" [x] Sent '" + record + "'");
        
        /* Lastly, we close the channel and the connection */
        channel.close();
        connection.close();
        
    }
        
}
