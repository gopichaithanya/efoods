package model;

import java.io.FileNotFoundException;

public class Account {
	
	private String AccountID;
	private String AccountName;
	private String Address;
	private String Password;
	
	
	public Account(String ID, String Name, String Add, String Pass) throws FileNotFoundException{
		
		if((ID != null) && (Name != null) && (Add != null) && (Pass != null))
		{
		this.setAccountID(new String(ID));
		this.setAccountName(new String(Name));
		this.setAddress(new String(Add));
		this.setPassword(new String(Pass));
		}
		else{
			System.exit(0);
		}
		
	}
	
	public Account()
	{
		
	}
	
	public String getAccountID(){
		return this.AccountID;
	}
	public String getAccountName(){
		return this.AccountName;
	}
	public String getAddress(){
		return this.Address;
	}
	public void setAccountID(String ID){
		this.AccountID = ID;
	}
	public void setAccountName(String Name){
		this.AccountName = Name;
	}
	public void setAddress(String Address){
		this.Address = Address;
	}
	public void setPassword(String Pass){
		this.Password = Pass;
	}
	public String getPassword()
	{
		return this.Password;
	}
	public String toString()
	{
		return "ID: " + this.getAccountID() + "| Name: " + this.getAccountName() +
				"| Address: " + this.getAddress() + "| Password: " + this.getPassword();
	}
}
