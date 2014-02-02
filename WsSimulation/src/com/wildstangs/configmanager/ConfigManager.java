/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.configmanager;

import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.subsystems.base.SubsystemContainer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nathan
 */
public class ConfigManager {

    private static ConfigManager instance = null;
    private static String configFileName = "/ws_config.txt";
    private static Hashtable config = new Hashtable();
    String path;
    File configFile;
    BufferedWriter bw;
    /**
     * Gets the instance of the ConfigManager Singleton.
     *
     * @return The instance of the ConfigManager.
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    protected ConfigManager() {
    }

    /**
     * Sets the filename to parse. Overrides the default /ws_config.txt. The
     * filename will only be set if it exists and is readable.
     *
     * @param filename The new filename to use for reading.
     * @throws ConfigManagerException
     */
    public void setConfigFileName(String filename) throws ConfigManagerException {
        String path = System.getProperty("user.dir");
        path += filename;
        System.out.println("Path " + path);
        File testFile = new File(path);
        try {
            testFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (testFile.exists()) {
            if (testFile.canRead()) {
                configFileName = path;
            } else {
                throw new ConfigManagerException("File " + path + " is not readable.");
            }
        } else {
            throw new ConfigManagerException("File " + path + " does not exist.");
        }
    }

    /**
     * Reads the config file /ws_config.txt unless setFileName has been used to
     * change the filename. Supports comment lines using // delineator. Comments
     * can be by themselves on a line or at the end of the line.
     *
     * The config file should be on the format key=value Example:
     * com.wildstangs.InputManager.WsDriverJoystick.trim=0.1
     *
     * @throws WsConfigFacadeException
     */
    public void readConfig() throws ConfigManagerException {
        File configFile = new File(configFileName);
        BufferedReader br = null;
        String line;
        StringTokenizer st;
        String value;
        String key;
        String configLine;

        if (configFile.exists()) {
            if (configFile.canRead()) {
                try {
                    br = new BufferedReader(new FileReader(configFile));
                    while ((line = br.readLine()) != null) {
                        if (!line.trim().startsWith("//") && !line.trim().isEmpty()) {
                            // This is not a comment line
                            if (line.contains("//")) {
                                //There is comment in the line
                                st = new StringTokenizer(line.trim(), "//");
                                if (st.countTokens() >= 2) {
                                    configLine = st.nextToken();
                                } else {
                                    configLine = line.substring(0, (line.lastIndexOf("//") - 1));
                                }
                            } else {
                                configLine = line;
                            }

                            st = new StringTokenizer(configLine.trim(), "=");
                            if (st.countTokens() >= 2) {
                                key = st.nextToken();
                                value = st.nextToken();
                                config.put(key, value);
                            } else {
                                throw new ConfigManagerException("Bad line in config file " + line);
                            }
                        }
                    }

                } catch (IOException ioe) {
                    throw new ConfigManagerException(ioe.toString());
                }
            }
        } else {
            throw new ConfigManagerException("File " + configFileName + " does not exist");
        }
        if (br != null) {
            try {
                br.close();
            } catch (IOException ioe) {
                throw new ConfigManagerException("Error closing file.");
            }
        }

        //Update all the facades
        InputManager.getInstance().notifyConfigChange();
        OutputManager.getInstance().notifyConfigChange();
        SubsystemContainer.getInstance().notifyConfigChange();

    }

    /**
     * Return a Config value the matches the key
     *
     * @param name The key value to search for.
     * @return An Object that contains the value.
     * @throws WsConfigFacadeException if the key cannot be found.
     */
    public String getConfigParamByName(String name) throws ConfigManagerException {
        String o = null;
        if (config.get(name) != null) {
            o = (config.get(name)).toString();
        }
        if (o == null) {
            throw new ConfigManagerException("Config Param " + name + " not found");
        }
        return o;
    }
    public void addParameterToConfigFile(String name, String defaultValue) {
           try {
              if(configFile == null) 
              {
                  configFile = new File(configFileName);
              }
              
              if (!configFile.exists()){
                  configFile.createNewFile();
              }
              
              FileWriter out = new FileWriter(configFile, true);
              bw = new BufferedWriter(out);
              bw.newLine();
              bw.append(name + "=" + defaultValue);
              bw.flush();
              bw.close();
              out.close();
              
              File cRioConfigFile = new File(configFile.getParentFile().getParentFile().getParentFile(), "cRioTarget/Config/ws_config.txt");
              
              if(cRioConfigFile.exists())
              {
                  out = new FileWriter(cRioConfigFile, true);
                  BufferedWriter cRioWriter = new BufferedWriter(out);
                  cRioWriter.newLine();
                  cRioWriter.append(name + "=" + defaultValue);
                  cRioWriter.flush();
                  cRioWriter.close();
                  out.close();
              }
              
              config.put(name, defaultValue);
            } catch (IOException e) { 
                System.out.println("Unable to open output file." + e.toString());
                e.printStackTrace();
            }
    
    
    }
    public String dumpConfigData() {
        for (Enumeration e = config.keys(); e.hasMoreElements();) {
            Object name = e.nextElement();
            System.out.println((String) name + "=" + (String) config.get(name));
        }
        return null;
    }

    /**
     * Config Item name parser
     *
     * Example: com.wildstangs.InputManager.WsDriverJoystick.trim will return
     * trim.
     *
     * @return The config Item name or null if the string is unparsable
     * @param configItem A String representing the config item to parse
     */
    public String getConfigItemName(String configItem) {
        StringTokenizer st;
        String configName = null;
        if (configItem != null) {
            st = new StringTokenizer(configItem.trim(), ".");
            while (st.hasMoreTokens()) {
                configName = st.nextToken();
            }
        }
        return configName;
    }
}
