package com.masterpiece.ToitEnVueBackEnd.model.file;

import com.masterpiece.ToitEnVueBackEnd.model.housing.Housing;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private int file_id;
    @Column(name = "file_name")
    private String file_name;
    @Column(name = "content_type")
    private String content_type;
    @ManyToOne
    @JoinColumn(name = "housing_id", nullable = false)
    private Housing housing;
}
