package pl.wroc.pwr.na.objects;

import java.io.Serializable;

/**
 * Class to represent organization
 * 
 * @author horodysk
 */
public class OrganizationObject implements Serializable {
    private static final long serialVersionUID = 2199900062677829827L;

    /***/
    public String _name;

    /***/
    public OrganizationObject(String name) {
        super();
        _name = name;
    }

}
