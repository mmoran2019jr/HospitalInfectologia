/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mariomoran.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.mariomoran.bean.Paciente;
import org.mariomoran.db.Conexion;
import org.mariomoran.report.GenerarReporte;
import org.mariomoran.sistema.Principal;

public class PacientesController implements Initializable {
    
    private enum operaciones{AGREGAR, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    
    @FXML private TextField txtDPI;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtSexo;
    @FXML private TextField txtFechaNacimiento;
    @FXML private TextField txtEdad;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtOcupacion;
    @FXML private TableView tblPacientes;
    @FXML private TableColumn colCodPaciente;
    @FXML private TableColumn colDPI;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colFechaNacimiento;
    @FXML private TableColumn colEdad;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colOcupacion;
    @FXML private TableColumn colSexo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
            
    private Principal escenarioPrincipal;
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<Paciente> listaPaciente;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     cargarDatos();
    }
    
    public void cargarDatos(){
       tblPacientes.setItems(getPacientes());
       colCodPaciente.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("codigoPaciente"));
       colDPI.setCellValueFactory(new PropertyValueFactory<Paciente, String>("DPI"));
       colNombres.setCellValueFactory(new PropertyValueFactory<Paciente, String>("nombres"));
       colApellidos.setCellValueFactory(new PropertyValueFactory<Paciente, String>("apellidos"));
       colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente, String>("fechaNacimiento"));
       colEdad.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("edad"));
       colDireccion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("direccion"));
       colOcupacion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("ocupacion"));
       colSexo.setCellValueFactory(new PropertyValueFactory<Paciente, String>("sexo"));
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
    
    
    public void seleccionarElemento(){
       txtDPI.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDPI());
       txtNombres.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getNombres());
       txtApellidos.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getApellidos());
       txtFechaNacimiento.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
       txtEdad.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getEdad()));
       txtDireccion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDireccion());
       txtOcupacion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getOcupacion());
       txtSexo.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getSexo());
    }
    

    public Paciente buscarPaciente(int codigoPaciente){ 
        Paciente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscarPaciente(?)");
            procedimiento .setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Paciente(
                     registro.getInt("codigoPaciente"),
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
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
               if(tblPacientes.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ModificarPaciente(?,?,?,?,?,?,?,?,?)");
            Paciente registro = (Paciente)tblPacientes.getSelectionModel().getSelectedItem();
            registro.setCodigoPaciente(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
            registro.setDPI(txtDPI.getText());
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setFechaNacimiento(txtFechaNacimiento.getText());
            registro.setEdad(Integer.parseInt(txtEdad.getText()));
            registro.setDireccion(txtDireccion.getText());
            registro.setOcupacion(txtOcupacion.getText());
            registro.setSexo(txtSexo.getText());
            procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getDPI());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setString(4, registro.getApellidos());
            procedimiento.setString(5, registro.getFechaNacimiento());
            procedimiento.setInt(6, registro.getEdad());
            procedimiento.setString(7, registro.getDireccion());
            procedimiento.setString(8, registro.getOcupacion());
            procedimiento.setString(9, registro.getSexo());
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
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
                   int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Paciente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                   if(respuesta == JOptionPane.YES_OPTION ){
                     try{
                         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarPaciente(?)}");
                         procedimiento.setInt(1, ((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                         procedimiento.execute();
                         listaPaciente.remove(tblPacientes.getSelectionModel().getSelectedIndex());
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
        Paciente registro = new Paciente();
        registro.setDPI(txtDPI.getText());
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setFechaNacimiento(txtFechaNacimiento.getText());
        registro.setEdad(Integer.parseInt(txtEdad.getText()));
        registro.setDireccion(txtDireccion.getText());
        registro.setOcupacion(txtOcupacion.getText());
        registro.setSexo(txtSexo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarPaciente(?,?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getDPI());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getFechaNacimiento());
            procedimiento.setInt(5, registro.getEdad());
            procedimiento.setString(6, registro.getDireccion());
            procedimiento.setString(7, registro.getOcupacion());
            procedimiento.setString(8, registro.getSexo());
            procedimiento.execute();
                       
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void desactivarControles(){
        txtDPI.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtFechaNacimiento.setEditable(false);
        txtEdad.setEditable(false);
        txtDireccion.setEditable(false);
        txtOcupacion.setEditable(false);
        txtSexo.setEditable(false);
    }
    
    
    public void activarControles(){
        txtDPI.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtFechaNacimiento.setEditable(true);
        txtEdad.setEditable(true);
        txtDireccion.setEditable(true);
        txtOcupacion.setEditable(true);
        txtSexo.setEditable(true);
    }
    
    
    public void limpiarControles(){
        txtDPI.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtFechaNacimiento.setText("");
        txtEdad.setText("");
        txtDireccion.setText("");
        txtOcupacion.setText("");
        txtSexo.setText("");
    }
    
    public void imprimirReporte(){
        if(tblPacientes.getSelectionModel().getSelectedItem() != null){
            int codigoPaciente = ((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente();
            Map parametro = new HashMap();
            parametro.put("p_codigoPaciente", codigoPaciente);
            GenerarReporte.mostrarReporte("ReporteBuscarPacientes.jasper", "Reporte de Pacientes", parametro);
            
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
        }
    }
    
    
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        this.escenarioPrincipal.menuPrincipal();
    }
    
}
