/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mariomoran.controller;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.mariomoran.bean.TipoUsuario;
import org.mariomoran.db.Conexion;
import org.mariomoran.report.GenerarReporte;
import org.mariomoran.sistema.Principal;


public class LoginController implements Initializable{
    private Principal escenarioPrincipal;
    
    @FXML private TextField txtUsuario;
    @FXML private PasswordField pswContrasena;
    @FXML private ComboBox cmbTipoUsuario;
    @FXML private Button btnLogin;
    @FXML private Button btnCancelar;

    public void Logear(){
        login();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cmbTipoUsuario.setItems(getTipoUsuarios());
    }
    
    public ObservableList<TipoUsuario>getTipoUsuarios(){
        ArrayList<TipoUsuario> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTipoUsuario}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
        
            lista.add(new TipoUsuario(resultado.getInt("codigoTipoUsuario"),
                                                    resultado.getString("nombre"),
                                                    resultado.getString("descripcion")));
            }
        }catch(SQLException e){
        e.getMessage();
        e.printStackTrace();
        }
        ObservableList<TipoUsuario> listarTipoUsuarios;
            return listarTipoUsuarios = FXCollections.observableList(lista);
    }
    
     public TipoUsuario buscarTipoUsuario(int codigoTipoUsuario){
         TipoUsuario resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTipoUsuario(?)}");
            procedimiento.setInt(1, codigoTipoUsuario);
            ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
                resultado = new TipoUsuario(registro.getInt("codigoTipoUsuario"),
                                                registro.getString("nombre"),
                                                registro.getString("descripcion"));
            }
       }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public LoginController(){
        con = Conexion.getInstancia().getConexion();
    }
    
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    
    private void login(){
        String usuarioLogin = txtUsuario.getText().toString();
        String usuarioContrasena = pswContrasena.getText().toString();
        Integer codigoTipoUsuario = (((TipoUsuario) cmbTipoUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario());

        String sql = "SELECT  usuarioLogin, usuarioContrasena FROM usuarios WHERE usuarioLogin = ? and usuarioContrasena = ? and codigoTipoUsuario = ?";
        
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, usuarioLogin);
            preparedStatement.setString(2, usuarioContrasena);
            preparedStatement.setInt(3, codigoTipoUsuario);
            resultSet = preparedStatement.executeQuery();
            if (! resultSet.next()){
                 JOptionPane.showMessageDialog(null, "Usuario y/o contraseña incorrectos.");
                 ventanaLogin();
            }  else{
                 JOptionPane.showMessageDialog(null, "Inicio de sesión correcto!.");
                menuPrincipal();              
                    }   
        } catch (Exception e) {
           e.printStackTrace();
        }       
    }      
    
    
    
    public void cancelar(){
    limpiarBotones();
    }
    
    public void limpiarBotones(){
        txtUsuario.setText("");
        pswContrasena.setText("");
        cmbTipoUsuario.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal EscenarioPrincipal) {
        this.escenarioPrincipal = EscenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
    
 public void imprimirReporteCargos(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteGeneralUsuarios.jasper", "Reporte genenal de usuarios", parametros);
    }
    
    
}
