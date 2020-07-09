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
import org.mariomoran.bean.ContactoUrgencia;
import org.mariomoran.bean.Paciente;
import org.mariomoran.db.Conexion;
import org.mariomoran.report.GenerarReporte;
import org.mariomoran.sistema.Principal;


public class ContactoUrgenciaController implements Initializable {
    
    private enum operaciones{AGREGAR, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    
    
    @FXML private TextField txtCodigoContactoUrgencia;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNumeroContacto;
    @FXML private TableView tblContactoUrgencia;
    @FXML private TableColumn colCodigoContactoUrg;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colNumeroContacto;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
   
    private Principal escenarioPrincipal;
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<ContactoUrgencia> listaContactoUrgencia;
    
    private ObservableList<Paciente> listaPaciente;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPaciente.setItems(getPacientes());
    }
    
    
    public void cargarDatos(){
        tblContactoUrgencia.setItems(getContactoUrgencia());
        colCodigoContactoUrg.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codigoContactoUrgencia"));
        colNombres.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("apellidos"));
        colNumeroContacto.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("numeroContacto"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codigoPaciente"));
    }
    
    
    public ObservableList<Paciente> getPacientes(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarPaciente");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente (resultado.getInt("codigoPaciente"),
                          resultado.getString("DPI"),
                          resultado.getString("nombres"),
                          resultado.getString("apellidos"),
                          resultado.getString("fechaNacimiento"),
                          resultado.getInt("edad"),
                          resultado.getString("direccion"),
                          resultado.getString("ocupacion"),
                          resultado.getString("sexo")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPaciente = FXCollections.observableList(lista);
            
    }
    
    
    public ObservableList<ContactoUrgencia>getContactoUrgencia(){
        ArrayList<ContactoUrgencia> lista = new ArrayList<ContactoUrgencia>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarContactoUrgencia");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ContactoUrgencia(resultado.getInt("codigoContactoUrgencia"),
                                         resultado.getString("nombres"),
                                         resultado.getString("apellidos"),
                                         resultado.getString("numeroContacto"),
                                         resultado.getInt("codigoPaciente")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaContactoUrgencia = FXCollections.observableList(lista);
    }
    
    
    public void seleccionarElemento(){
       txtNombres.setText(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getNombres());
       txtApellidos.setText(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getApellidos());
       txtNumeroContacto.setText(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getNumeroContacto());
       cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
    }
    
    
    public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1,(codigoPaciente));
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
               resultado = new Paciente(registro.getInt("codigoPaciente"),
                                            registro.getString("DPI"),
                                            registro.getString("nombres"),
                                            registro.getString("apellidos"),
                                            registro.getString("fechaNacimiento"),
                                            registro.getInt("edad"),
                                            registro.getString("direccion"),
                                            registro.getString("ocupacion"),
                                            registro.getString("sexo"));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
        
        
    }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ModificarContactoUrgencia(?,?,?,?,?)");
            ContactoUrgencia registro = (ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem();
            registro.setCodigoContactoUrgencia(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia());
            registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setNumeroContacto(txtNumeroContacto.getText());
            //registro.setCodigoPaciente(Integer.parseInt(cmbCodigoPaciente.getSelectionModel().getSelectedItem().toString()));
            
            procedimiento.setInt(1, registro.getCodigoContactoUrgencia());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getNumeroContacto());
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
               if(tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
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
                if(tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
                   int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Contacto de Urgencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                   if(respuesta == JOptionPane.YES_OPTION ){
                     try{
                         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarContactoUrgencia(?)}");
                         procedimiento.setInt(1, ((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                         procedimiento.execute();
                         listaContactoUrgencia.remove(tblContactoUrgencia.getSelectionModel().getSelectedIndex());
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
        ContactoUrgencia registro = new ContactoUrgencia();
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setNumeroContacto(txtNumeroContacto.getText());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarContactoUrgencia(?,?,?,?)}");
            procedimiento.setString(1, registro.getNombres());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNumeroContacto());
            procedimiento.setInt(4, registro.getCodigoPaciente());
            procedimiento.execute();
            listaContactoUrgencia.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void desactivarControles(){
        txtCodigoContactoUrgencia.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtNumeroContacto.setEditable(false);
        cmbCodigoPaciente.setDisable(true);
    }
    
    
    public void activarControles(){
        txtCodigoContactoUrgencia.setEditable(false);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtNumeroContacto.setEditable(true);
        cmbCodigoPaciente.setDisable(false);
    }
    
    
    public void limpiarControles(){
        txtCodigoContactoUrgencia.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtNumeroContacto.setText("");
        cmbCodigoPaciente.getSelectionModel().clearSelection();
    }
    
    public void imprimirReporte(){
        if(tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
            int codigoContactoUrgencia = ((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia();
            Map parametro = new HashMap();
            parametro.put("p_codigoContactoUrgencia", codigoContactoUrgencia);
            GenerarReporte.mostrarReporte("ReporteBuscarContactosUrgencia.jasper", "Reporte de Contactos Urgencia", parametro);
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
