/*
package com.example.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectsType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgenerator")
    @Column
    private Long id;

    @Column
    @NotNull
    private String type;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    @NotNull
    private Subject subject;
}
*/
