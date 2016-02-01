//package ula_test;

import java.io.IOException;
import ula.UlaLogger;

public class Test {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        /* Create Logger instance */
        UlaLogger ula = new UlaLogger();
        ula.getLogger().info("System is restarting...");        
        ula.getLogger().warning("Device is DOWN!!!");

    }
    
}
