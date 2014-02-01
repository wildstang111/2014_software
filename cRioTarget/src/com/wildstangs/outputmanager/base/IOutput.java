package com.wildstangs.outputmanager.base;

/**
 *
 * @author Nathan
 */
public interface IOutput {

    /**
     * Method to set the value of the object for the key provided.
     *
     * @param key the key of the value to set.
     * @param value the value to set.
     */
    public void set(IOutputEnum key, Object value);
    
    public void set(Object value);
    
    /**
     * Retrieves the value represented by the key.
     *
     * @param key the key of the value to grab.
     * @return the value for the key.
     */
    public Object get(IOutputEnum key);
    
    public Object get();
    
    /**
     * Method to force the output element to update. Used by the output facade
     * to update all elements.
     */
    public void update();

    /**
     * Method to notify the output element that config values have been updated
     * and should be re-read. Used by the output facade to notify all output
     * elements.
     */
    public void notifyConfigChange();
}
