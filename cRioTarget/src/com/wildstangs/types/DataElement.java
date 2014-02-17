package com.wildstangs.types;

/**
 *
 * @author Nathan
 */
public class DataElement {

    private Object dkey;
    private Object dvalue;

    /**
     * A generic dataElement
     *
     * @param key The key.
     * @param value The value
     */
    public DataElement(Object key, Object value) {
        dkey = key;
        dvalue = value;
    }

    /**
     * Retrieve the key
     *
     * @return the key
     */
    public Object getKey() {
        return dkey;
    }
    
    public void setKey(Object key)
    {
        this.dkey = key;
    }

    /**
     * Retrieve the value Object.
     *
     * @return the Object stored as value
     */
    public Object getValue() {
        return dvalue;
    }
    
    public void setValue(Object value)
    {
        this.dvalue = value;
    }
}