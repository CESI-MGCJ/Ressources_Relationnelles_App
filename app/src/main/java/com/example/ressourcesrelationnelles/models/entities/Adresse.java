package com.example.ressourcesrelationnelles.models.entities;

import java.io.Serializable;

public class Adresse implements Serializable {
	public Long id;
	public String country;
	public String city;
	public Integer numStreet;
	public String street;

	public Adresse(){}
}
