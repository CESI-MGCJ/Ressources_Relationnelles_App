package com.example.ressourcesrelationnelles.models.entities;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {
	public Long id;
	public String name;
	public List<Ressource> listRessources;

	public Category() {}
}
