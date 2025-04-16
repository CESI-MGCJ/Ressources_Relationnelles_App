package com.example.ressourcesrelationnelles.models.entities;

import java.io.Serializable;

public class Statistic implements Serializable {
	public Long id;
	public int nbConsult;
	public int nbFav;
	public int nbExploit;
	public int nbComment;
	public Ressource ressource;

	public Statistic(){}
}
