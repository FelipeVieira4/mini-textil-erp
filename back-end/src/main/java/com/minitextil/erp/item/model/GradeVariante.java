package com.minitextil.erp.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "TBL_GRADE_VAR")
public class GradeVariante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGrade;

    @NotBlank(message = "Descrição da grade é obrigatório!")
    private String descricao;
}
