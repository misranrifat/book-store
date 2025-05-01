package com.bookstore.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthorDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String biography;

    public AuthorDto() {
    }

    public AuthorDto(Long id, String name, String biography) {
        this.id = id;
        this.name = name;
        this.biography = biography;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}