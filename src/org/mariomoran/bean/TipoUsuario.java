/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mariomoran.bean;

/**
 *
 * @author programacion
 */
public class TipoUsuario {
    private int codigoTipoUsuario;
    private String nombre;
    private String descripcion;

    public TipoUsuario() {
    }

    public TipoUsuario(int codigoTipoUsuario, String nombre, String descripcion) {
        this.codigoTipoUsuario = codigoTipoUsuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getCodigoTipoUsuario() {
        return codigoTipoUsuario;
    }

    public void setCodigoTipoUsuario(int codigoTipoUsuario) {
        this.codigoTipoUsuario = codigoTipoUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String toString(){
        return getCodigoTipoUsuario() + " | " + getNombre() + " , " +getDescripcion();
    }
    
    
}
