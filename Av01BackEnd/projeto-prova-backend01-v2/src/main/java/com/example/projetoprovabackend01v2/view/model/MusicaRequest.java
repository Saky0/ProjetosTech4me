package com.example.projetoprovabackend01v2.view.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class MusicaRequest {
    
    /*
        Somente Alguns atributos são indispensáveis no cadastro de uma musica
    */

    @NotBlank(message = "O Campo titulo não deve conter somente caracteres em branco")
    @NotEmpty(message = "Preencha o campo titulo")
    private String titulo;

    @NotBlank(message = "O Campo artista não deve conter somente caracteres em branco")
    @NotEmpty(message = "Preencha o campo artista")
    private String artista;

    @NotBlank(message = "O Campo album não deve conter somente caracteres em branco")
    @NotEmpty(message = "Preencha o campo album")
    private String album;

    @NotBlank(message = "O Campo genero não deve conter somente caracteres em branco")
    @NotEmpty(message = "Preencha o campo genero")
    private String genero;

    // O valor mínimo é 1000 para permitir que apenas Anos com 4 caracteres ou mais sejam digitados
    @Min(value = 1000, message =  "O Ano deve conter exatamente 4 caracteres")
    private Integer anoLançamento;
    private String compositor;
    
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getArtista() {
        return artista;
    }
    public void setArtista(String artista) {
        this.artista = artista;
    }
    public String getAlbum() {
        return album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public Integer getAnoLançamento() {
        return anoLançamento;
    }
    public void setAnoLançamento(Integer anoLançamento) {
        this.anoLançamento = anoLançamento;
    }
    public String getCompositor() {
        return compositor;
    }
    public void setCompositor(String compositor) {
        this.compositor = compositor;
    }

}
