package com.example.ressourcesrelationnelles.models.entities;

import java.io.Serializable;
import java.util.List;

public class Role implements Serializable {
	public Long id;
	public String name;
	public String description;
	public List<User> listeDeUser;

	public Role(){}
}
