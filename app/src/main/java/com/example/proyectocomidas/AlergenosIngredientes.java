package com.example.proyectocomidas;

public class AlergenosIngredientes {

    private String idAlergeno;
    private String idIngrediente;

    public AlergenosIngredientes(String idAlergeno, String idIngrediente) {
        this.idAlergeno = idAlergeno;
        this.idIngrediente = idIngrediente;
    }

    public String getIdAlergeno() {
        return idAlergeno;
    }

    public void setIdAlergeno(String idAlergeno) {
        this.idAlergeno = idAlergeno;
    }

    public String getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(String idIngrediente) {
        this.idIngrediente = idIngrediente;
    }
}
