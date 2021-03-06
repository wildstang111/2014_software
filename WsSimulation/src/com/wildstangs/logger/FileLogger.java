package com.wildstangs.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author smitty
 */
public class FileLogger {
    
    private static final String LOG_FILE = "/snapshot-log.txt";
    private static FileLogger instance = null;

    FileManager fileMan;
    Thread t;
    boolean started = false;
    
    private FileLogger() {
        started = false;
    }
    
    
    /**
     * Gets the instance of the Logger Singleton.
     *
     * @return The instance of the Logger.
     */
    public static FileLogger getFileLogger() {
        if (instance == null) {
            instance = new FileLogger();
        }
        return instance;
    }
    
    public void logData(String myLog) {
        fileMan.setWriteData(myLog);
        synchronized(fileMan) {
            fileMan.notify();
        }
    }
    
    public void killLogger() {
        fileMan.stop();
    }
    
    public void startLogger() {
        if (!started) {
            t = new Thread(fileMan = new FileManager(LOG_FILE));
            //This is safe because there is only one instance of the subsystem in the subsystem container.
            t.start();
            started = true;
        }
    }
    public void flushLogger() {
        fileMan.flush();
    }
 
    
    
    private static class FileManager implements Runnable {
        //Designed to only have one single threaded controller. (LED)
        //Offload to a thread avoid blocking main thread with LED sends.
        File configFile; 
        BufferedWriter bw;
        String path;
        boolean dataToWrite = false;
        String outputString;
        boolean running = true;
        
        public FileManager(String logPath)
        {
            path = System.getProperty("user.dir"); 
            path += logPath;
           try {
              configFile  = new File(path);
              if (!configFile.exists()){
                  configFile.createNewFile();
              }
              FileWriter out = new FileWriter(configFile);
              bw = new BufferedWriter(out);
              
            } catch (IOException e) { 
                System.out.println("Unable to open output file." + e.toString());
                e.printStackTrace();
            }
        }

        public void run() {
            while (running) {
                synchronized (this) {
                    try {
                        //blocking sleep until someone calls notify.
                        this.wait();
                        if (dataToWrite) {
                            bw.write(outputString);
                            bw.newLine();
                            bw.flush();
                            dataToWrite = false;
                        }
                        
                    } catch (Exception e) {
                        System.out.println("Error writing data " + e.toString());
                        e.printStackTrace();
                               
                                
                   } 
                }
            }
            try {
                bw.flush();
                bw.close();
            }
            catch (IOException e) {}
        }

        public void setWriteData(String sendString) {
            outputString = sendString;
            dataToWrite = true;
        }

        public void stop() {
            running = false;
        }
        
        public void flush() {
            try {
                bw.flush();
            }
            catch (IOException e) { 
                System.out.println("Unable to flush buffered writer: " + e.toString());
            }
        }
    }
    
}
