/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.configmanager;

import com.wildstangs.configmanager.impl.ConfigManagerImpl;
import com.wildstangs.configmanager.impl.ConfigManagerImplException;
import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.logger.Logger;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.subsystems.base.SubsystemContainer;
import com.wildstangs.types.DataElement;
import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author Nathan
 */
public class ConfigManager {

    static String myClassName = "WsConfigFacade";
    private static ConfigManager instance = null;
    private static String configFileName = "/ws_config.txt";
    private static String updateFileName = "/reloadConfigFile.cmd";
    private static List config = new List();

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
        if (ConfigManagerImpl.checkCreateFile(filename)) {
            configFileName = filename;
        } else {
            throw new ConfigManagerException("Problem setting config file name");
        }
    }

    /**
     * Sets the filename to check for updates. Overrides the default
     * /reloadConfigFile.cmd. The filename will only be set if it exists and is
     * readable.
     *
     * @param filename The new filename to use for checking for updates.
     * @throws ConfigManagerException
     */
    public void setUpdateFileName(String filename) throws ConfigManagerException {
        if (ConfigManagerImpl.checkCreateFile(filename)) {
            updateFileName = filename;
        } else {
            throw new ConfigManagerException("Problem setting update file name");
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
     * @throws ConfigManagerException
     */
    public void readConfig() throws ConfigManagerException {
        try {
            config = (List) ConfigManagerImpl.readConfig(configFileName);
        } catch (ConfigManagerImplException e) {
            throw new ConfigManagerException(e.toString());
        }
        Logger.getLogger().always(this.getClass().getName(), "readConfig", "Read config File: " + configFileName);
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
     * @throws ConfigManagerException if the key cannot be found.
     */
    public String getConfigParamByName(String name) throws ConfigManagerException {
        for (int i = 0; i < config.size(); i++) {
            if ((((String) ((DataElement) config.get(i)).getKey())).equals(name)) {
                return (String) ((DataElement) config.get(i)).getValue();
            }
        }
        throw new ConfigManagerException("Config Param " + name + " not found");

    }

    public String dumpConfigData() {
        System.out.println("Dumping config data...");
        for (int i = 0; i < config.size(); i++) {
            String name = ((String) ((DataElement) config.get(i)).getKey());
            String value = ((String) ((DataElement) config.get(i)).getValue().toString());
            System.out.println(name + "=" + value);
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
        return ConfigManagerImpl.getConfigItemName(configItem);
    }

    /**
     * If an updated config file is available, read it
     *
     * @throws ConfigManagerException
     */
    public void readConfigIfUpdateAvailable() throws ConfigManagerException {
        if (ConfigManagerImpl.isUpdateAvailable(updateFileName)) {
            Logger.getLogger().always(this.getClass().getName(), "readConfigIfUpdateAvailable", "New config file found!");
            try {
                readConfig();
                ConfigManagerImpl.deleteUpdateFile(updateFileName);
            } catch (ConfigManagerException e) {
                throw new ConfigManagerException(e.toString());
            }
        }
    }
}
