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
import org.mariomoran.bean.MedicoEspecialidad;
import org.mariomoran.bean.Paciente;
import org.mariomoran.bean.ResponsableTurno;
import org.mariomoran.bean.Turno;
import org.mariomoran.db.Conexion;
import org.mariomoran.report.GenerarReporte;
import org.mariomoran.sistema.Principal;

/**
 *
 * @author mmora
 */
public class TurnoController implements Initializable{
    
    private enum operaciones{AGREGAR, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    
    @FXML private TextField txtFechaTurno;
    @FXML private TextField txtFechaCita;
    @FXML private TextField txtValorCita;
    @FXML private ComboBox cmbCodigoMedicoEspecialidad;
    @FXML private ComboBox cmbCodigoResponsableTurno;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private TableView tblTurno;
    @FXML private TableColumn colCodigoTurno;
    @FXML private TableColumn colFechaTurno;
    @FXML private TableColumn colFechaCita;
    @FXML private TableColumn colValorCita;
    @FXML private TableColumn colCodigoMedicoEspecialidad;
    @FXML private TableColumn colCodigoResponsableTurno;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    private Principal escenarioPrincipal;

    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<Turno> listaTurno;
    
    private ObservableList<MedicoEspecialidad> listaMedicoEspecialidad;
    
    private ObservableList<ResponsableTurno> listaResponsableTurno;
    
    private ObservableList<Paciente> listaPaciente;
            

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoMedicoEspecialidad.setItems(getMedicoEspecialidad());
        cmbCodigoResponsableTurno.setItems(getResponsableTurno());
        cmbCodigoPaciente.setItems(getPacientes());
    }
    
    public ObservableList<MedicoEspecialidad> getMedicoEspecialidad(){
        ArrayList<MedicoEspecialidad> lista = new ArrayList<MedicoEspecialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarMedicoEspecialidad");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new MedicoEspecialidad(resultado.getInt("codigoMedicoEspecialidad"),
                                            resultado.getInt("codigoMedico"),
                                            resultado.getInt("codigoEspecialidad"),
                                            resultado.getInt("codigoHorario")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaMedicoEspecialidad = FXCollections.observableList(lista);
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
    
   public ObservableList<Turno> getTurnos(){
       ArrayList<Turno> lista = new ArrayList<Turno>();
       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarTurno");
           ResultSet resultado = procedimiento.executeQuery();
           while(resultado.next()){
               lista.add(new Turno(resultado.getInt("codigoTurno"),
                                   resultado.getString("fechaTurno"),
                                   resultado.getString("fechaCita"),
                                   resultado.getString("valorCita"),
                                   resultado.getInt("codigoMedicoEspecialidad"),
                                   resultado.getInt("codigoResponsableTurno"),
                                   resultado.getInt("codigoPaciente")));
           }
           
       }catch(Exception e){
           e.printStackTrace();
       }
       
       return listaTurno = FXCollections.observableList(lista);
    }
   
    public void cargarDatos(){
       tblTurno.setItems(getTurnos());
       colCodigoTurno.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoTurno"));
       colFechaTurno.setCellValueFactory(new PropertyValueFactory<Turno, String>("fechaTurno"));
       colFechaCita.setCellValueFactory(new PropertyValueFactory<Turno, String>("fechaCita"));
       colValorCita.setCellValueFactory(new PropertyValueFactory<Turno, String>("valorCita"));
       colCodigoMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoMedicoEspecialidad"));
       colCodigoResponsableTurno.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoResponsableTurno"));
       colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoPaciente"));
    }
    
    public void seleccionarElemento(){
        txtFechaTurno.setText(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getFechaTurno());
        txtFechaCita.setText(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getFechaCita());
        txtValorCita.setText(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getValorCita());
        cmbCodigoMedicoEspecialidad.getSelectionModel().select(buscarMedicoEspecialidad(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoTurno()));
        cmbCodigoResponsableTurno.getSelectionModel().select(buscarResponsableTurno(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoTurno()));
        cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoTurno()));
    }
    
    public MedicoEspecialidad buscarMedicoEspecialidad(int codigoMedicoEspecialidad){
        MedicoEspecialidad resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedicoEspecialidad(?)}");
            procedimiento.setInt(1, (codigoMedicoEspecialidad));
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new MedicoEspecialidad(registro.getInt("codigoMedicoEspecialidad"),
                                                   registro.getInt("codigoMedico"),
                                                   registro.getInt("codigoEspecialidad"),
                                                   registro.getInt("codigoHorario"));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public ResponsableTurno buscarResponsableTurno(int codigoResponsableTurno){
        ResponsableTurno resultado = null;
        try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarResponsableTurno(?)}");
           procedimiento.setInt(1, (codigoResponsableTurno));
           ResultSet registro = procedimiento.executeQuery();
           while(registro.next()){
               resultado = new ResponsableTurno(registro.getInt("codigoResponsableTurno"),
                                                registro.getString("nombreResponsable"),
                                                registro.getString("apellidoResponsable"),
                                                registro.getString("telefonoPersonal"),
                                                registro.getInt("codigoArea"),
                                                registro.getInt("codigocargo"));
           }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1, (codigoPaciente));
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ModificarTurno(?,?,?,?,?,?)");
            Turno registro = (Turno)tblTurno.getSelectionModel().getSelectedItem();
            registro.setCodigoTurno(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoTurno());
            registro.setFechaTurno(txtFechaTurno.getText());
            registro.setFechaCita(txtFechaCita.getText());
            registro.setValorCita(txtValorCita.getText());
            registro.setCodigoMedicoEspecialidad(((MedicoEspecialidad)cmbCodigoMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
            registro.setCodigoResponsableTurno(((ResponsableTurno)cmbCodigoResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
            registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
            
            procedimiento.setInt(1, registro.getCodigoTurno());
            procedimiento.setString(2, registro.getFechaTurno());
            procedimiento.setString(3, registro.getFechaCita());
            procedimiento.setString(4, registro.getValorCita());
            procedimiento.setInt(5, registro.getCodigoMedicoEspecialidad());
            procedimiento.setInt(6, registro.getCodigoResponsableTurno());
            procedimiento.setInt(7, registro.getCodigoPaciente());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTurno.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
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
                if(tblTurno.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Turno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTurno(?)}");
                            procedimiento.setInt(1, ((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoTurno());
                            procedimiento.execute();
                            listaTurno.remove(tblTurno.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
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
        Turno registro = new Turno();
        registro.setFechaTurno(txtFechaTurno.getText());
        registro.setFechaCita(txtFechaCita.getText());
        registro.setValorCita(txtValorCita.getText());
        registro.setCodigoMedicoEspecialidad(((MedicoEspecialidad)cmbCodigoMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
        registro.setCodigoResponsableTurno(((ResponsableTurno)cmbCodigoResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTurno(?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getFechaTurno());
            procedimiento.setString(2, registro.getFechaCita());
            procedimiento.setString(3, registro.getValorCita());
            procedimiento.setInt(4, registro.getCodigoMedicoEspecialidad());
            procedimiento.setInt(5, registro.getCodigoResponsableTurno());
            procedimiento.setInt(6, registro.getCodigoPaciente());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtFechaTurno.setEditable(false);
        txtFechaCita.setEditable(false);
        txtValorCita.setEditable(false);
        cmbCodigoMedicoEspecialidad.setDisable(true);
        cmbCodigoResponsableTurno.setDisable(true);
        cmbCodigoPaciente.setDisable(true);
    }
    
    public void activarControles(){
        txtFechaTurno.setEditable(true);
        txtFechaCita.setEditable(true);
        txtValorCita.setEditable(true);
        cmbCodigoMedicoEspecialidad.setDisable(false);
        cmbCodigoResponsableTurno.setDisable(false);
        cmbCodigoPaciente.setDisable(false);
    }
    
    public void limpiarControles(){
        txtFechaTurno.setText("");
        txtFechaCita.setText("");
        txtValorCita.setText("");
        cmbCodigoMedicoEspecialidad.getSelectionModel().clearSelection();
        cmbCodigoResponsableTurno.getSelectionModel().clearSelection();
        cmbCodigoPaciente.getSelectionModel().clearSelection();
    }
    
    public void imprimirReporte(){
        if(tblTurno.getSelectionModel().getSelectedItem() != null){
            int codigoTurno = ((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoTurno();
            Map parametro = new HashMap();
            parametro.put("p_codigoTurno", codigoTurno);
            GenerarReporte.mostrarReporte("ReporteBuscarTurnos.jasper", "Reporte de Turno", parametro);
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
