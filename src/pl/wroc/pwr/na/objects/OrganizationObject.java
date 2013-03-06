package pl.wroc.pwr.na.objects;

import java.io.Serializable;

public class OrganizationObject implements Serializable {
	private static final long serialVersionUID = 2199900062677829827L;
	
	public String name;

	public OrganizationObject(String name) {
		super();
		this.name = name;
	}

}
