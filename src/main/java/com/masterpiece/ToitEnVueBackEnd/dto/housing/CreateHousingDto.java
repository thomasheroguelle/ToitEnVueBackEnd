package com.masterpiece.ToitEnVueBackEnd.dto.housing;

import com.masterpiece.ToitEnVueBackEnd.model.housing.CategoryEnum;
import com.masterpiece.ToitEnVueBackEnd.model.housing.HousingConditionEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.util.Set;

@Getter
@Setter
public class CreateHousingDto {
    @NotBlank(message = "Le titre ne peut pas être vide")
    private String title;

    @NotBlank(message = "L'adresse ne peut pas être vide")
    private String address;

    @NotBlank(message = "La ville ne peut pas être vide")
    private String city;

    @NotNull(message = "Le code postal ne peut pas être vide")
    private Integer zipcode;

    @NotBlank(message = "La description ne peut pas être vide")
    private String description;

    @NotBlank(message = "Le prix ne peut pas être vide")
    private double price;

    @NotBlank(message = "La catégorie ne peut pas être vide")
    private CategoryEnum category;

    @NotNull(message = "Le nombre de pièces ne peut pas être vide")
    private Integer rooms;

    @NotNull(message = "Le nombre de chambres ne peut pas être vide")
    private Integer bedrooms;

    @NotNull(message = "Le nombre de salles de bain ne peut pas être vide")
    private Integer bathrooms;

    @NotBlank(message = "La valeur meublé/ne meublé ne peut pas être vide")
    private boolean furnished;

    @NotNull(message = "La surface habitable ne peut pas être vide")
    private Integer living_space;

    @NotBlank(message = "Veuillez séléctionner au moins un point fort")
    private Set<String> highlights;

    @NotNull(message = "L'année de construction ne peut pas être vide")
    private Integer year_of_construction;

    @NotBlank(message = "La condition du logement ne peut pas être vide")
    private HousingConditionEnum housingCondition;

    @NotNull(message = "L'identifiant de l'utilisateur ne peut pas être vide")
    private Long user_id;

    @NotBlank(message = "Le nom d'utilisateur ne peut pas être vide")
    private String username;
}
