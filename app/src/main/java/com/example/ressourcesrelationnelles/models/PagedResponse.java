package com.example.ressourcesrelationnelles.models;

import java.util.List;

public class PagedResponse<T> {
    public List<T> content;
    public int number;
    public int totalPages;
    public int totalElements;
    public int size;
}

