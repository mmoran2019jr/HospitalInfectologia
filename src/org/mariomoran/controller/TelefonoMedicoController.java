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
import org.mariomoran.bean.Medico;
import org.mariomoran.bean.TelefonoMedico;
import org.mariomoran.db.Conexion;
import org.mariomoran.report.GenerarReporte;
import org.mariomoran.sistema.Principal;


public class TelefonoMedicoController implements Initializable {
    
    private enum operaciones{AGREGAR, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    
    @FXML private TextField txtCodigoTelefonoMedico;
    @FXML private ComboBox cmbCodigoMedico;
    @FXML private TextField txtTelefonoPersonal;
    @FXML private TextField txtTelefonoTrabajo;
    @FXML private TableView tblTelefonoMedico;
    @FXML private TableColumn colCodigoTelMed;
    @FXML private TableColumn colCodigoMedico;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colTelefonoTrabajo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
            
    private Principal escenarioPrincipal;
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<TelefonoMedico> listaTelefonoMedico;
    
    private ObservableList<Medico> listaMedico;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
     cargarDatos();
     cmbCodigoMedico.setItems(getMedicos());
    }
    
    public void cargarDatos(){
        tblTelefonoMedico.setItems(getTelefonoMedicos());
        colCodigoTelMed.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("codigoTelefonoMedico"));
        colCodigoMedico.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("codigoMedico"));
        colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoPersonal"));
        colTelefonoTrabajo.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoTrabajo"));
    }
    
    
    public ObservableList<Medico>getMedicos(){
      ArrayList<Medico> lista = new ArrayList<Medico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarMedico");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medico(resultado.getInt("codigoMedico"),
                           resultado.getInt("licenciaMedica"),
                           resultado.getString("nombres"),
                           resultado.getString("apellidos"),
                           resultado.getString("horaEntrada"),
                           resultado.getString("horaSalida"),
                           resultado.getInt("turnoMaximo"),
                           resultado.getString("sexo")));               
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaMedico = FXCollections.observableList(lista);
       
    }
    
    
    public ObservableList<TelefonoMedico> getTelefonoMedicos(){
        ArrayList<TelefonoMedico> lista = new ArrayList<TelefonoMedico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarTelefonoMedico");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TelefonoMedico(resultado.getInt("codigoTelefonoMedico"),
                          resultado.getString("telefonoPersonal"),
                          resultado.getString("telefonoTrabajo"),
                          resultado.getInt("codigoMedico")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTelefonoMedico = FXCollections.observableList(lista);
    }
    
    
    public void seleccionarElemento(){
        txtTelefonoPersonal.setText(((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        txtTelefonoTrabajo.setText(((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getTelefonoTrabajo());
        cmbCodigoMedico.getSelectionModel().select(buscarMedico(((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico()));
    }
    
    public Medico buscarMedico(int codigoMedico){
        Medico resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedico(?)}");
            procedimiento.setInt(1, (codigoMedico));
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medico(registro.getInt("codigoMedico"),
                           registro.getInt("licenciaMedica"),
                           registro.getString("nombres"),
                           registro.getString("apellidos"),
                           registro.getString("horaEntrada"),
                           registro.getString("horaSalida"),
                           registro.getInt("turnoMaximo"),
                           registro.getString("sexo"));           
            }
        }catch(Exception e){
          e.printStackTrace();
        }
    return resultado;
        
    }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ModificarTelefonoMedico(?,?,?,?)");
            TelefonoMedico registro = (TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem();
            registro.setCodigoTelefonoMedico(((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico());
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
         // registro.setCodigoMedico(Integer.parseInt(cmbCodigoMedico.getSelectionModel().getSelectedItem().toString()));
            registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
            
            procedimiento.setInt(1, registro.getCodigoTelefonoMedico());
            procedimiento.setString(2, registro.getTelefonoPersonal());
            procedimiento.setString(3, registro.getTelefonoTrabajo());
            procedimiento.setInt(4, registro.getCodigoMedico());
            procedimiento.execute();
        }catch(Exception e){
        e.printStackTrace();
     }
    }
    
    
     public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
               if(tblTelefonoMedico.getSelectionModel().getSelectedItem() != null){
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
                if(tblTelefonoMedico.getSelectionModel().getSelectedItem() != null){
                   int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Telefono de Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                   if(respuesta == JOptionPane.YES_OPTION ){
                     try{
                         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoMedico(?)}");
                         procedimiento.setInt(1, ((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico());
                         procedimiento.execute();
                         listaTelefonoMedico.remove(tblTelefonoMedico.getSelectionModel().getSelectedIndex());
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
        TelefonoMedico registro = new TelefonoMedico();
        registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
        registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
        registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonoMedico(?,?,?)}");
            procedimiento.setString(1, registro.getTelefonoPersonal());
            procedimiento.setString(2, registro.getTelefonoTrabajo());
            procedimiento.setInt(3, registro.getCodigoMedico());
            procedimiento.execute();
            listaTelefonoMedico.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void desactivarControles(){
        txtCodigoTelefonoMedico.setEditable(false);
        txtTelefonoPersonal.setEditable(false);
        txtTelefonoTrabajo.setEditable(false);
        cmbCodigoMedico.setDisable(true);
    }
    
    
    public void activarControles(){
        txtCodigoTelefonoMedico.setEditable(false);
        txtTelefonoPersonal.setEditable(true);
        txtTelefonoTrabajo.setEditable(true);
        cmbCodigoMedico.setDisable(false);
    }
    
    
    public void limpiarControles(){
        txtCodigoTelefonoMedico.setText("");
        txtTelefonoPersonal.setText("");
        txtTelefonoTrabajo.setText("");
        cmbCodigoMedico.getSelectionModel().clearSelection();
    }
    
    public void imprimirReporte(){
        if(tblTelefonoMedico.getSelectionModel().getSelectedItem() != null){
            int codigoTelefonoMedico = ((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico();
            Map parametro = new HashMap();
            parametro.put("p_codigoTelefonoMedico", codigoTelefonoMedico);
            GenerarReporte.mostrarReporte("ReporteBuscarTelefonosMedico.jasper", "Reporte de Telefonos Medico", parametro);
            
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
