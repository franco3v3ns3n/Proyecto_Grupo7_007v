package com.hotel.habitacion.DTO;

public class HabitacionDTO {

    private Long id_habitacion;
    private String numero_habitacion;
    private String tipo_habitacion;
    private Integer capacidad_personas;
    private Integer cantidad_camas;
    private Integer cantidad_banos;
    private String estado_habitacion;

    public HabitacionDTO() {
    }

    public HabitacionDTO(Long id_habitacion, String numero_habitacion, String tipo_habitacion,
            Integer capacidad_personas, Integer cantidad_camas, Integer cantidad_banos,
            String estado_habitacion) {
        this.id_habitacion = id_habitacion;
        this.numero_habitacion = numero_habitacion;
        this.tipo_habitacion = tipo_habitacion;
        this.capacidad_personas = capacidad_personas;
        this.cantidad_camas = cantidad_camas;
        this.cantidad_banos = cantidad_banos;
        this.estado_habitacion = estado_habitacion;
    }

    // Getters y Setters
    public Long getId_habitacion() {
        return id_habitacion;
    }

    public void setId_habitacion(Long id_habitacion) {
        this.id_habitacion = id_habitacion;
    }

    public String getNumero_habitacion() {
        return numero_habitacion;
    }

    public void setNumero_habitacion(String numero_habitacion) {
        this.numero_habitacion = numero_habitacion;
    }

    public String getTipo_habitacion() {
        return tipo_habitacion;
    }

    public void setTipo_habitacion(String tipo_habitacion) {
        this.tipo_habitacion = tipo_habitacion;
    }

    public Integer getCapacidad_personas() {
        return capacidad_personas;
    }

    public void setCapacidad_personas(Integer capacidad_personas) {
        this.capacidad_personas = capacidad_personas;
    }

    public Integer getCantidad_camas() {
        return cantidad_camas;
    }

    public void setCantidad_camas(Integer cantidad_camas) {
        this.cantidad_camas = cantidad_camas;
    }

    public Integer getCantidad_banos() {
        return cantidad_banos;
    }

    public void setCantidad_banos(Integer cantidad_banos) {
        this.cantidad_banos = cantidad_banos;
    }

    public String getEstado_habitacion() {
        return estado_habitacion;
    }

    public void setEstado_habitacion(String estado_habitacion) {
        this.estado_habitacion = estado_habitacion;
    }
}