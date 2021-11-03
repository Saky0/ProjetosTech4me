package com.example.projetoprovabackend01v2.view.model;

public class MusicaResponse {
    
    private String id;
    private String titulo;
    private String artista;
    private String album;
    private String genero;
    private Integer anoLançamento;
    private String compositor;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
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
