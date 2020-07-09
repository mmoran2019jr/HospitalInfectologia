/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mariomoran.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.mariomoran.bean.Area;
import org.mariomoran.db.Conexion;
import org.mariomoran.report.GenerarReporte;
import org.mariomoran.sistema.Principal;

/**
 *
 * @author mmora
 */
public class AreaController implements Initializable {
    
    private enum operaciones{AGREGAR, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    
    @FXML private ComboBox cmbCodigoArea;
    @FXML private TextField txtNombreArea;
    @FXML private TableView tblArea;
    @FXML private TableColumn colCodigoArea;
    @FXML private TableColumn colNombreArea;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    
    private Principal escenarioPrincipal;
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<Area> listaArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    
    public void cargarDatos(){
        tblArea.setItems(getAreas());
        colCodigoArea.setCellValueFactory(new PropertyValueFactory<Area, Integer>("codigoArea"));
        colNombreArea.setCellValueFactory(new PropertyValueFactory<Area, String>("nombreArea"));
        
    }
    
    
    public ObservableList<Area> getAreas(){
        ArrayList<Area> lista = new ArrayList<Area>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarArea");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Area(resultado.getInt("codigoArea"),
                                 resultado.getString("nombreArea")));
            }
        }catch(Exception e){
          e.printStackTrace();  
        }
        
        return listaArea = FXCollections.observableList(lista); 
        
    }
    
    
    public void seleccionarElemento(){
        txtNombreArea.setText(((Area)tblArea.getSelectionModel().getSelectedItem()).getNombreArea());
    }
    
    
    public Area buscarArea(int codigoArea){
        Area resultado = null;
        try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscarArea(?)");
            procedimiento.setInt(1, codigoArea);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Area(
                        registro.getInt("codigoArea"),
                        registro.getString("nombreArea"));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
               if(tblArea.getSelectionModel().getSelectedItem() != null){
                   btnEditar.setText("Actualizar");
                   btnReporte.setText("Cancelar");
                   btnNuevo.setDisable(true);
                   btnEliminar.setDisable(true);                  
                   activarControles();
                   tipoDeOperacion = operaciones.ACTUALIZAR;
                   
                   
               } else {
                   JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                   
               } 
               break;
               
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
                
                
        }
        
        
    }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ModificarArea(?,?)");
            Area registro = (Area)tblArea.getSelectionModel().getSelectedItem();
            registro.setCodigoArea(((Area)tblArea.getSelectionModel().getSelectedItem()).getCodigoArea());
            registro.setNombreArea(txtNombreArea.getText());
            
            procedimiento.setInt(1, registro.getCodigoArea());
            procedimiento.setString(2, registro.getNombreArea());
            procedimiento.execute();
           
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
             desactivarControles();
             limpiarControles();
             btnNuevo.setText("Nuevo");
             btnEliminar.setText("Eliminar");
             btnEditar.setDisable(false);
             btnReporte.setDisable(false);
             tipoDeOperacion = operaciones.GUARDAR;
             break;
            default:
                if(tblArea.getSelectionModel().getSelectedItem() != null){
                   int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Area", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                   if(respuesta == JOptionPane.YES_OPTION ){
                     try{
                         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarArea(?)}");
                         procedimiento.setInt(1, ((Area)tblArea.getSelectionModel().getSelectedItem()).getCodigoArea());
                         procedimiento.execute();
                         listaArea.remove(tblArea.getSelectionModel().getSelectedIndex());
                         limpiarControles();
                     }catch(Exception e){
                         e.printStackTrace();
                     }
                    
                    }
                
             
                } else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
    
                }
        }
    }
    
    
    public void nuevo(){
        
     switch (tipoDeOperacion){
        case NINGUNO:
            activarControles();
            btnNuevo.setText("Guardar");
            btnEliminar.setText("Cancelar");
            btnEditar.setDisable(true);
            btnReporte.setDisable(true);
            
            tipoDeOperacion = operaciones.GUARDAR;
        break;
        
        case GUARDAR:
            guardar();
            desactivarControles();
            limpiarControles();
            btnNuevo.setText("Nuevo");
            btnEliminar.setText("Eliminar");
            btnEditar.setDisable(false);
            btnReporte.setDisable(false);
            tipoDeOperacion = operaciones.NINGUNO;          
            cargarDatos();
        break;    
     }   
     
    }
    
    
    public void guardar(){
        Area registro = new Area();
        registro.setNombreArea(txtNombreArea.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarArea(?)");
            procedimiento.setString(1, registro.getNombreArea());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    
    public void imprimirReporte(){
        if(tblArea.getSelectionModel().getSelectedItem() != null){
            int codigoArea = ((Area)tblArea.getSelectionModel().getSelectedItem()).getCodigoArea();
            Map parametro = new HashMap();
            parametro.put("p_codigoArea", codigoArea);
            GenerarReporte.mostrarReporte("ReporteBuscarAreas.jasper", "Reporte de Area", parametro);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
   
    public void desactivarControles(){
        txtNombreArea.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreArea.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombreArea.setText("");
    }
    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
     public void menuPrincipal(){
        this.escenarioPrincipal.menuPrincipal();
    }
    
}
