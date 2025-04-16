package com.example.ressourcesrelationnelles.models.entities;

import java.io.Serializable;
import java.sql.Date;

public class User  implements Serializable {
	public Long id;
	public String email;
	public String password;
	public String firstName;
	public String lastName;
	public String phoneNumber;
	public Character gender;
	public Date birthday;
	public boolean isActived;
	public Date creationDate;
	public Date lastLoginDate;
	public Adresse adresse;

	public User(){}
}
