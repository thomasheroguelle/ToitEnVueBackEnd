package com.masterpiece.ToitEnVueBackEnd.model.housing;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.masterpiece.ToitEnVueBackEnd.model.booking.Booking;
import com.masterpiece.ToitEnVueBackEnd.model.file.File;
import com.masterpiece.ToitEnVueBackEnd.model.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "housing")
public class Housing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "housing_id", nullable = false)
    private int housing_id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "zipcode", nullable = false)
    @Min(value = 10000, message = "Le code postal doit contenir exactement 5 chiffres")
    @Max(value = 99999, message = "Le code postal doit contenir exactement 5 chiffres")
    private int zipcode;
    @Column(name = "description", nullable = false, length = 2500)
    private String description;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;
    @Min(value = 1)
    @Column(name = "rooms", nullable = false)
    private int rooms;
    @Min(value = 1)
    @Column(name = "bedrooms", nullable = false)
    private int bedrooms;
    @Min(value = 1)
    @Column(name = "bathrooms", nullable = false)
    private int bathrooms;
    @Column(name = "living_space", nullable = false)
    private int living_space;
    @Column(name = "highlights", nullable = false)
    private Set<String> highlights;
    @Column(name = "year_of_construction", nullable = false)
    private int year_of_construction;
    @Column(name = "housing_condition", nullable = false)
    @Enumerated(EnumType.STRING)
    private HousingConditionEnum housingCondition;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "housing", cascade = CascadeType.ALL)
    @JsonDeserialize
    private List<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "housing", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
}
