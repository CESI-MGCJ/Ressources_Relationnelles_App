package com.example.ressourcesrelationnelles.models.entities;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class Ressource implements Serializable {
	public Long id;
	public String title;
	public String headerImagePath;
	public String filePath;
	public String content;
	public Date publicationDate;
	public Date updateDate;
	public String description;
	public String status = "Public";
	public Boolean isPublished = false;
	public Boolean isActived = true;
	public Statistic statistic;
	public Category category;
	public RessourceType ressourceType;
	public User user;
	public List<HaveRelationType> listRelationTypes;

	public Ressource(){

	}
}
