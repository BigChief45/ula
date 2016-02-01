import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receiver {
    
    private final static String QUEUE_NAME = "hello";
    
    public static void main(String[] args) throws IOException, TimeoutException, java.io.InterruptedIOException {
        
        /* Create a connection to the server
           The connection abstracts the socket connection, and takes care of 
           protocol version negotiation and authentication and so on for us.
        */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");        
        Connection connection = factory.newConnection();
        
        /*  Create a channel, which is where most of the API for getting things done resides. */
        Channel channel = connection.createChannel();

        /*  Note that we declare the queue here, as well. 
            Because we might start the receiver before the sender, 
            we want to make sure the queue exists before we try to consume messages from it.
        */        
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        
        /*  We're about to tell the server to deliver us the messages from the queue. 
            Since it will push us messages asynchronously, we provide a callback in the form of an object 
            that will buffer the messages until we're ready to use them. 
            That is what a DefaultConsumer subclass does.
        */
        Consumer consumer = new DefaultConsumer(channel) {
            
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
    
}
