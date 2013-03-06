package pl.wroc.pwr.na.objects;

import java.io.Serializable;

public class AddressObject implements Serializable{
	private static final long serialVersionUID = -1130811590154913844L;
	
	public String address;

	public AddressObject(String address) {
		super();
		this.address = address;
	}
	

}
