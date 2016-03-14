/**
 * Author:  Jaime Alvarez - 2016
 */

//package ula;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import java.io.File;

/**
* ULA Base class that provides special logging capability for SaaS.
* @author Jaime Alvarez
* @version 1.0     
*/
public class UlaLogger 
{
    private String tenant_id;
    private String saas_id;
    private String process_id;
    private Logger logger;
    
    /**
     * Create a new ULA Logger instance.
     * @throws IOException 
     */
    public UlaLogger() throws IOException {
        /* Initialize the logger object */
        
        /* Assign Random SaaS Tenant ID and Process ID */
        /* These values should somehow be retrieved by the API from the
           SaaS system.
        */
        tenant_id = "TN1231";
        saas_id = "SAAS456";
        
        process_id = ManagementFactory.getRuntimeMXBean().getName();    // Gets current process name
        
        /* Create a new Logging for this tenant or SaaS instance */
        logger = java.util.logging.Logger.getLogger(tenant_id);
        //logger = Logging.getLogger(ULA.class.getName());        
        
        // Create an instance of our custom Handler
        //UlaHandler handler = new UlaHandler();
        
        // Disable default console handler
        logger.setUseParentHandlers(false);
        
        // Create directory where logs will be written
        // Logs will be written to ~/ula/
        File directory = new File(System.getProperty("user.home"), "/ula");
        
        // Check if directory exists
        if ( !(directory.exists() && directory.isDirectory()) ) {
            // If directory does not exist, create it.
            boolean success = directory.mkdir();
    
            if ( !success )
                System.err.println("Dir creation failed.");
            else
                System.out.println("Directory created");
        }
    
        // Create file handler (Append: true)
        FileHandler handler = new FileHandler(System.getProperty("user.home") + "/ula/" + tenant_id + ".log", true);
                
        // Set our custom formatter to the console handler
        handler.setFormatter(new logFormatter());        
        logger.addHandler(handler);
        
        // Set formatter to the file handler
        //txtFile.setFormatter(new logFormatter());
        //logger.addHandler(txtFile);
    }
    
    /**
     * Obtain the Logger object of this instance.
     * This Logger will be used to log all your logs using additional methods
     * such as <CODE>info</CODE>, <CODE>warning</CODE>, etc.
     * 
     * <p>
     *      Example:
     * 
     *      <CODE>
     *          <p>UlaLogger l = new UlaLogger();</p>
     *          <p>l.getLogger().info("Your log message...");</p>
     *      </CODE>
     * </p>
     * @return the logger
     */
    public Logger getLogger() {
        return logger;
    }

    
    /**
     * Contains the Formatter object that formats the contents of the log.
     * @author Jaime Alvarez
     * @version 1.0
     */
    class logFormatter extends Formatter {
    
        private final String LINE_SEPARATOR = System.getProperty("line.separator");

        @Override
        public String format(LogRecord record) {
            StringBuilder sb = new StringBuilder();

            sb.append(new Date(record.getMillis()))
                .append(" ")
                .append(tenant_id)
                .append(" ")
                .append(process_id)
                .append(" ")
                .append(record.getLevel().getLocalizedName())
                .append(": ")
                .append(formatMessage(record))
                .append(LINE_SEPARATOR);

            return sb.toString();
         }

        private String calculateDate(long milliSecs) {
            SimpleDateFormat date_format = new SimpleDateFormat("MMM dd yyyy HH:mm");

            Date result = new Date(milliSecs);

            return date_format.format(result);
        }
        
    } // End ulaFormatter Class
    
    /**
     * The ULA Handler that sends logs to the RabbitMQ message queue
     * @author Jaime Alvarez
     * @version 1.0
     */
    class UlaHandler extends StreamHandler {
        
        @Override
        public void publish(LogRecord record) {
            /* Message forwarding to RabbitMQ message queue */
            
            /* Obtain a String representation of the log record from the Formatter */
            String log = super.getFormatter().format(record);
            
            /* Initialize a Sender object instance and send the record */            
            //Sender sender = new Sender();
            try {
                //sender.send(log);       
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            
            super.publish(record);          
        }
        
        @Override
        public void flush() {
            super.flush();
        }
        
        @Override
        public void close() throws SecurityException {
            super.close();
        }
    }
}


