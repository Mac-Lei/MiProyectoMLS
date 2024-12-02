package com.mac.miproyectomls;

public class Compra {
    private String productoId;
    private String productoNombre;
    private int cantidad;
    private double totalCompra;
    private String fecha;

    // Constructor vacío necesario para Firebase
    public Compra() {}

    // Constructor
    public Compra(String productoId, String productoNombre, int cantidad, double totalCompra, String fecha) {
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.cantidad = cantidad;
        this.totalCompra = totalCompra;
        this.fecha = fecha;
    }

    // Getters y setters
    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
