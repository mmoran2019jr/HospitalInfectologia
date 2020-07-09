/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mariomoran.bean;


public class Paciente {
    private int codigoPaciente;
    private String DPI;
    private String nombres;
    private String apellidos;
    private String fechaNacimiento;
    private int edad;
    private String direccion;
    private String ocupacion;
    private String sexo;

    public Paciente() {
    }

    public Paciente(int codigoPaciente, String DPI, String nombres, String apellidos, String fechaNacimiento, int edad, String direccion, String ocupacion, String sexo) {
        this.codigoPaciente = codigoPaciente;
        this.DPI = DPI;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.direccion = direccion;
        this.ocupacion = ocupacion;
        this.sexo = sexo;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public String getDPI() {
        return DPI;
    }

    public void setDPI(String DPI) {
        this.DPI = DPI;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    
    public String toString(){
        return getCodigoPaciente() + " | " + getNombres() + " , " +getApellidos();
    }
    
    
}
