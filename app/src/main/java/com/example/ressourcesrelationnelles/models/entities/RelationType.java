package com.example.ressourcesrelationnelles.models.entities;

import java.io.Serializable;
import java.util.List;

public class RelationType implements Serializable {
	public Long id;
	public String name;
	public List<HaveRelationType> listRelationTypes;

	public RelationType(){}
}
