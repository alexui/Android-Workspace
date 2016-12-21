package com.example.battleship;

public class BDevice {

	public String name,address;
	
	public BDevice(String n,String a){
		name=n;
		address=a;
	}

	@Override
	public boolean equals(Object o) {
		BDevice obj = (BDevice)o;
		return (obj.name.compareTo(this.name)==0) && (obj.address.compareTo(this.address)==0); 
	}
	
	
}
