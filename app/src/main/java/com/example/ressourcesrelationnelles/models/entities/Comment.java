package com.example.ressourcesrelationnelles.models.entities;

public class Comment {
	public Long id;
	public String name;
	public String content;
	public boolean isActivated;
	public boolean isReported;
	public Ressource ressource;
}
