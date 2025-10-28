package com.trabalho.crud.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class TipoQuartoDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String nome;
    private String descricao;
    private Integer capacidadeMaxima;
    private Double tarifaPadrao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(Integer capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public Double getTarifaPadrao() {
        return tarifaPadrao;
    }

    public void setTarifaPadrao(Double tarifaPadrao) {
        this.tarifaPadrao = tarifaPadrao;
    }
}