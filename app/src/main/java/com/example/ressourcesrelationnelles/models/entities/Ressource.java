package com.example.ressourcesrelationnelles.models.entities;

import java.sql.Date;
import java.util.List;

public class Ressource {
	public Long id;
	public String title;
	public String headerImagePath;
	public String filePath;
	public String content;
	public Date publicationDate;
	public Date updateDate;
	public String description;
	public String status;
	public Boolean isPublished;
	public Boolean isActived;
	public Statistic statistic;
	public Category category;
	public RessourceType ressourceType;
	public List<HaveRelationType> listRelationTypes;
}
