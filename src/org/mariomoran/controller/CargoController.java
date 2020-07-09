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
import org.mariomoran.bean.Cargo;
import org.mariomoran.db.Conexion;
import org.mariomoran.report.GenerarReporte;
import org.mariomoran.sistema.Principal;


public class CargoController implements Initializable {
    
    private enum operaciones{AGREGAR, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    
    @FXML private ComboBox cmbCodigoCargo;
    @FXML private TextField txtNombreCargo;
    @FXML private TableView tblCargo;
    @FXML private TableColumn colCodigoCargo;
    @FXML private TableColumn colNombreCargo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    private Principal escenarioPrincipal;
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<Cargo> listaCargo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    
    public void cargarDatos(){
        tblCargo.setItems(getCargos());
        colCodigoCargo.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("codigoCargo"));
        colNombreCargo.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreCargo"));
    }
    
    
    public ObservableList<Cargo> getCargos(){
        ArrayList<Cargo> lista = new ArrayList<Cargo>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarCargo");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cargo(resultado.getInt("codigoCargo"),
                                   resultado.getString("nombreCargo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaCargo = FXCollections.observableList(lista); 
        
    }
    
    
    public void seleccionarElemento(){
        txtNombreCargo.setText(((Cargo)tblCargo.getSelectionModel().getSelectedItem()).getNombreCargo());
    }
    
    
    public Cargo buscarCargo(int codigoCargo){
        Cargo resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscarCargo(?)");
            procedimiento.setInt(1, codigoCargo);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Cargo(
                        registro.getInt("codigoCargo"),
                        registro.getString("nombreCargo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
               if(tblCargo.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ModificarCargo(?,?)");
            Cargo registro = (Cargo)tblCargo.getSelectionModel().getSelectedItem();
            registro.setCodigoCargo(((Cargo)tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
            registro.setNombreCargo(txtNombreCargo.getText());
            
            procedimiento.setInt(1, registro.getCodigoCargo());
            procedimiento.setString(2, registro.getNombreCargo());
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
                if(tblCargo.getSelectionModel().getSelectedItem() != null){
                   int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Cargo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                   if(respuesta == JOptionPane.YES_OPTION ){
                     try{
                         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCargo(?)}");
                         procedimiento.setInt(1, ((Cargo)tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
                         procedimiento.execute();
                         listaCargo.remove(tblCargo.getSelectionModel().getSelectedIndex());
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
        Cargo registro = new  Cargo();
        registro.setNombreCargo(txtNombreCargo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarCargo(?)");
            procedimiento.setString(1, registro.getNombreCargo());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void imprimirReporte(){
        if(tblCargo.getSelectionModel().getSelectedItem() != null){
            int codigoCargo = ((Cargo)tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargo();
            Map parametro = new HashMap();
            parametro.put("p_codigoCargo", codigoCargo);
            GenerarReporte.mostrarReporte("ReporteBuscarCargos.jasper", "Reporte de Cargos", parametro);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
        }
    }
    
   
    public void desactivarControles(){
        txtNombreCargo.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreCargo.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombreCargo.setText("");
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
