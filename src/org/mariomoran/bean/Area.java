/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mariomoran.bean;


public class Area {
    private int codigoArea;
    private String nombreArea;

    public Area() {
    }

    public Area(int codigoArea, String nombreArea) {
        this.codigoArea = codigoArea;
        this.nombreArea = nombreArea;
    }

    public int getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }
    
    public String toString(){
        return getCodigoArea() + " | " + getNombreArea();
    }
    
    
}
