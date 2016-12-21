package com.example.bluetoothconnection;

public class Dispozitiv {

	public String name,address;
	
	public Dispozitiv(String n,String a){
		name=n;
		address=a;
	}

	@Override
	public boolean equals(Object o) {
		Dispozitiv obj = (Dispozitiv) o;
		return obj.name.compareTo(this.name)==0 && obj.address.compareTo(this.address)==0;
	}
	
	
}
