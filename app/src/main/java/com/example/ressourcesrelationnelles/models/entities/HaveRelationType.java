package com.example.ressourcesrelationnelles.models.entities;

import java.io.Serializable;

public class HaveRelationType implements Serializable {
	public Long id;
	public Ressource ressource;
	public RelationType relationType;

	public HaveRelationType(){}
}
