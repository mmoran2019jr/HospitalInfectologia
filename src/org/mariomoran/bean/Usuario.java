/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mariomoran.bean;

import java.util.Date;


/**
 *
 * @author programacion
 */
public class Usuario {
    private int codigoUsuario;
    private String usuarioLogin;
    private String usuarioContrasena;
    private Boolean usuarioEstado;
    private Date usuarioFecha;
    private String usuarioHora;
    private int codigoTipoUsuario;

    public Usuario() {
    }

    public Usuario(int codigoUsuario, String usuarioLogin, String usuarioContrasena, Boolean usuarioEstado, Date usuarioFecha, String usuarioHora, int codigoTipoUsuario) {
        this.codigoUsuario = codigoUsuario;
        this.usuarioLogin = usuarioLogin;
        this.usuarioContrasena = usuarioContrasena;
        this.usuarioEstado = usuarioEstado;
        this.usuarioFecha = usuarioFecha;
        this.usuarioHora = usuarioHora;
        this.codigoTipoUsuario = codigoTipoUsuario;
    }

    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(String usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public String getUsuarioContrasena() {
        return usuarioContrasena;
    }

    public void setUsuarioContrasena(String usuarioContrasena) {
        this.usuarioContrasena = usuarioContrasena;
    }

    public Boolean getUsuarioEstado() {
        return usuarioEstado;
    }

    public void setUsuarioEstado(Boolean usuarioEstado) {
        this.usuarioEstado = usuarioEstado;
    }

    public Date getUsuarioFecha() {
        return usuarioFecha;
    }

    public void setUsuarioFecha(Date usuarioFecha) {
        this.usuarioFecha = usuarioFecha;
    }

    public String getUsuarioHora() {
        return usuarioHora;
    }

    public void setUsuarioHora(String usuarioHora) {
        this.usuarioHora = usuarioHora;
    }

    public int getCodigoTipoUsuario() {
        return codigoTipoUsuario;
    }

    public void setCodigoTipoUsuario(int codigoTipoUsuario) {
        this.codigoTipoUsuario = codigoTipoUsuario;
    }

   
    public String toString(){
        return getCodigoUsuario() + " | " + getUsuarioEstado();
    }
    
    

    
    
    
    
    
}




    
    
    


   
    
    
    


    
    
    
    

