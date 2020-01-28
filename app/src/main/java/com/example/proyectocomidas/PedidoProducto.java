package com.example.proyectocomidas;

public class PedidoProducto {

    private String idPedido;
    private String idProducto;

    public PedidoProducto(String idPedido, String idProducto) {
        this.idPedido = idPedido;
        this.idProducto = idProducto;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public String getIdProducto() {
        return idProducto;
    }

    @Override
    public String toString() {
        return "PedidoProducto{" +
                "idPedido='" + idPedido + '\'' +
                ", idProducto='" + idProducto + '\'' +
                '}';
    }
}
