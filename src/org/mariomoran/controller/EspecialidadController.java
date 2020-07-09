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
import org.mariomoran.bean.Especialidad;
import org.mariomoran.db.Conexion;
import org.mariomoran.report.GenerarReporte;
import org.mariomoran.sistema.Principal;


public class EspecialidadController implements Initializable {
    
    private enum operaciones{AGREGAR, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    
    @FXML private TextField txtNombreEspecialidad;
    @FXML private TableView tblEspecialidad;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colNombreEspecialidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    
    
    private Principal escenarioPrincipal;
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<Especialidad> listaEspecialidad;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    
    public void cargarDatos(){
        tblEspecialidad.setItems(getEspecialidades());
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidad, Integer>("codigoEspecialidad"));
        colNombreEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidad, String>("nombreEspecialidad"));
        
    }
    
    
    public ObservableList<Especialidad> getEspecialidades(){
        ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarEspecialidad");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
                                 resultado.getString("nombreEspecialidad")));
            }
        }catch(Exception e){
          e.printStackTrace();  
        }
        
        return listaEspecialidad = FXCollections.observableList(lista); 
        
    }
    
    
    public void seleccionarElemento(){
        txtNombreEspecialidad.setText(((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getNombreEspecialidad());
    }
    
    
    public Especialidad buscarEspecialidad(int codigoEspecialidad){
        Especialidad resultado = null;
        try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscarEspecialidad(?)");
            procedimiento.setInt(1, codigoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Especialidad(
                        registro.getInt("codigoEspecialidad"),
                        registro.getString("nombreEspecialidad"));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
               if(tblEspecialidad.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ModificarEspecialidad(?,?)");
            Especialidad registro = (Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem();
            registro.setCodigoEspecialidad(((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
            registro.setNombreEspecialidad(txtNombreEspecialidad.getText());
            
            procedimiento.setInt(1, registro.getCodigoEspecialidad());
            procedimiento.setString(2, registro.getNombreEspecialidad());
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
                if(tblEspecialidad.getSelectionModel().getSelectedItem() != null){
                   int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                   if(respuesta == JOptionPane.YES_OPTION ){
                     try{
                         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEspecialidad(?)}");
                         procedimiento.setInt(1, ((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                         procedimiento.execute();
                         listaEspecialidad.remove(tblEspecialidad.getSelectionModel().getSelectedIndex());
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
        Especialidad registro = new Especialidad();
        registro.setNombreEspecialidad(txtNombreEspecialidad.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarEspecialidad(?)");
            procedimiento.setString(1, registro.getNombreEspecialidad());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void imprimirReporte(){
        if(tblEspecialidad.getSelectionModel().getSelectedItem() != null){
            int codigoEspecialidad = ((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad();
            Map parametro = new HashMap();
            parametro.put("p_codigoEspecialidad", codigoEspecialidad);
            GenerarReporte.mostrarReporte("ReporteBuscarEspecialidades.jasper", "Reporte de Especialidades", parametro);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
        }
    }
    
   
    public void desactivarControles(){
        txtNombreEspecialidad.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreEspecialidad.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombreEspecialidad.setText("");
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
