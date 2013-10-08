package pl.wroc.pwr.na.objects;

import java.io.Serializable;

/**
 * Class to represent adres of event
 * 
 * @author horodysk
 */
public class AddressObject implements Serializable {
    private static final long serialVersionUID = -1130811590154913844L;

    /***/
    public String _address;

    /***/
    public AddressObject(String address) {
        super();
        _address = address;
    }
}
