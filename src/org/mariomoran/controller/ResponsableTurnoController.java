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
import java.util.HashSet;
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
import org.mariomoran.bean.Cargo;
import org.mariomoran.bean.ResponsableTurno;
import org.mariomoran.db.Conexion;
import org.mariomoran.report.GenerarReporte;
import org.mariomoran.sistema.Principal;


public class ResponsableTurnoController implements Initializable {
    
    private enum operaciones{AGREGAR, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    
    @FXML private TextField txtCodigoResponsableTurno;
    @FXML private TextField txtNombreResponsable;
    @FXML private TextField txtApellidoResponsable;
    @FXML private TextField txtTelefonoPersonal;
    @FXML private ComboBox cmbCodigoArea;
    @FXML private ComboBox cmbCodigoCargo;
    @FXML private TableView tblResponsableTurno;
    @FXML private TableColumn colCodigoResponsableTurno;
    @FXML private TableColumn colNombreResponsable;
    @FXML private TableColumn colApellidoResponsable;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colCodigoArea;
    @FXML private TableColumn colCodigoCargo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    private Principal escenarioPrincipal;
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<ResponsableTurno> listaResponsableTurno;
    
    private ObservableList<Area> listaArea;
    
    private ObservableList<Cargo> listaCargo;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoArea.setItems(getAreas());
        cmbCodigoCargo.setItems(getCargos());
        
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
    
    
    public void cargarDatos(){
        tblResponsableTurno.setItems(getResponsableTurno());
        colCodigoResponsableTurno.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoResponsableTurno"));
        colCodigoArea.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoArea"));
        colCodigoCargo.setCellValueFactory(new PropertyValueFactory<ResponsableTurno,Integer>("codigoCargo"));
        colNombreResponsable.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("nombreResponsable"));
        colApellidoResponsable.setCellValueFactory(new PropertyValueFactory<ResponsableTurno,String>("apellidoResponsable"));
        colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("telefonoPersonal")); 
    }
    
    public ObservableList<ResponsableTurno> getResponsableTurno(){
        ArrayList<ResponsableTurno> lista = new ArrayList<ResponsableTurno>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarResponsableTurno");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ResponsableTurno(resultado.getInt("codigoResponsableTurno"),
                                           resultado.getString("nombreResponsable"),
                                           resultado.getString("apellidoResponsable"),
                                           resultado.getString("telefonoPersonal"),
                                           resultado.getInt("codigoArea"),
                                           resultado.getInt("codigoCargo")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaResponsableTurno = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        txtNombreResponsable.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getNombreResponsable());
        txtApellidoResponsable.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getApellidoResponsable());
        txtTelefonoPersonal.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        cmbCodigoArea.getSelectionModel().select(buscarArea(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno()));
        cmbCodigoCargo.getSelectionModel().select(buscarCargo(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno()));
    }
    
    public Area buscarArea(int codigoArea){
        Area resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarArea(?)}");
            procedimiento.setInt(1, (codigoArea));
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Area(registro.getInt("codigoArea"),
                                     registro.getString("nombreArea"));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
     public Cargo buscarCargo(int codigoCargo){
        Cargo resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCargo(?)}");
            procedimiento.setInt(1, (codigoCargo));
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Cargo(registro.getInt("codigoCargo"),
                                     registro.getString("nombreCargo"));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
     
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ModificarResponsableTurno(?,?,?,?,?,?)");
            ResponsableTurno registro = (ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem();
            registro.setCodigoResponsableTurno(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
            registro.setNombreResponsable(txtNombreResponsable.getText());
            registro.setApellidoResponsable(txtApellidoResponsable.getText());
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setCodigoArea(((Area)cmbCodigoArea.getSelectionModel().getSelectedItem()).getCodigoArea());
            registro.setCodigoCargo(((Cargo)cmbCodigoCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
            
            procedimiento.setInt(1, registro.getCodigoResponsableTurno());
            procedimiento.setString(2, registro.getNombreResponsable());
            procedimiento.setString(3, registro.getApellidoResponsable());
            procedimiento.setString(4, registro.getTelefonoPersonal());
            procedimiento.setInt(5, registro.getCodigoArea());
            procedimiento.setInt(6, registro.getCodigoCargo());
            procedimiento.execute();
                    
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
   public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
               if(tblResponsableTurno.getSelectionModel().getSelectedItem() != null){
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
                if(tblResponsableTurno.getSelectionModel().getSelectedItem() != null){
                   int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Responsable Turno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                   if(respuesta == JOptionPane.YES_OPTION ){
                     try{
                         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarResponsableTurno(?)}");
                         procedimiento.setInt(1, ((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
                         procedimiento.execute();
                         listaResponsableTurno.remove(tblResponsableTurno.getSelectionModel().getSelectedIndex());
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
        switch(tipoDeOperacion){
            case NINGUNO:
            activarControles();
              btnNuevo.setText("Guardar");
              btnEliminar.setText("Cancelar");
              btnEditar.setDisable(true);
              btnReporte.setDisable(true);
              limpiarControles();
              tipoDeOperacion = operaciones.GUARDAR;
            break;
             
            case GUARDAR:
                agregar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
     
     
    public void agregar(){
        ResponsableTurno registro = new ResponsableTurno();
        registro.setNombreResponsable(txtNombreResponsable.getText());
        registro.setApellidoResponsable(txtApellidoResponsable.getText());
        registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
        registro.setCodigoArea(((Area)cmbCodigoArea.getSelectionModel().getSelectedItem()).getCodigoArea());
        registro.setCodigoCargo(((Cargo)cmbCodigoCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarResponsableTurno(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreResponsable());
            procedimiento.setString(2, registro.getApellidoResponsable());
            procedimiento.setString(3, registro.getTelefonoPersonal());
            procedimiento.setInt(4, registro.getCodigoArea());
            procedimiento.setInt(5, registro.getCodigoCargo());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void desactivarControles(){
        txtNombreResponsable.setEditable(false);
        txtApellidoResponsable.setEditable(false);
        txtTelefonoPersonal.setEditable(false);
        cmbCodigoArea.setDisable(true);
        cmbCodigoCargo.setDisable(true);
    }
    
    public void activarControles(){
        txtNombreResponsable.setEditable(true);
        txtApellidoResponsable.setEditable(true);
        txtTelefonoPersonal.setEditable(true);
        cmbCodigoArea.setDisable(false);
        cmbCodigoCargo.setDisable(false);
    }
    
    public void limpiarControles(){
        txtNombreResponsable.setText("");
        txtApellidoResponsable.setText("");
        txtTelefonoPersonal.setText("");
        cmbCodigoArea.getSelectionModel().clearSelection();
        cmbCodigoCargo.getSelectionModel().clearSelection();
    }
    
    public void imprimirReporte(){
        if(tblResponsableTurno.getSelectionModel().getSelectedItem() != null){
            int codigoResponsableTurno = ((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno();
            Map parametro = new HashMap();
            parametro.put("p_codigoResponsableTurno", codigoResponsableTurno);
            GenerarReporte.mostrarReporte("ReporteBuscarResponsablesTurno.jasper", "Reporte de Responsable Turno", parametro);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
        }
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
