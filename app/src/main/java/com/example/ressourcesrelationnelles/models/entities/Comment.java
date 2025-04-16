package com.example.ressourcesrelationnelles.models.entities;

import java.io.Serializable;

public class Comment implements Serializable {
	public Long id;
	public String name;
	public String content;
	public boolean isActivated;
	public boolean isReported;
	public Ressource ressource;

	public Comment(){}
}
