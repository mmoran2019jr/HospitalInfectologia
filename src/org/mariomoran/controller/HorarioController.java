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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.mariomoran.bean.Horario;
import org.mariomoran.db.Conexion;
import org.mariomoran.report.GenerarReporte;
import org.mariomoran.sistema.Principal;

/**
 *
 * @author mmora
 */
public class HorarioController implements Initializable{
    
    private enum operaciones{AGREGAR, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    
    @FXML private TextField txtHorarioInicio;
    @FXML private TextField txtHorarioSalida;
    @FXML private TextField txtLunes;
    @FXML private TextField txtMartes;
    @FXML private TextField txtMiercoles;
    @FXML private TextField txtJueves;
    @FXML private TextField txtViernes;
    @FXML private TableView tblHorarios;
    @FXML private TableColumn colCodigoHorario;
    @FXML private TableColumn colHorarioInicio;
    @FXML private TableColumn colHorarioSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes;
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    
    private Principal escenarioPrincipal;
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<Horario> listaHorario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblHorarios.setItems(getHorarios());
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("codigoHorario"));
        colHorarioInicio.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioInicio"));
        colHorarioSalida.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("viernes"));
    }
    
    public ObservableList<Horario> getHorarios(){
        ArrayList<Horario> lista = new ArrayList<Horario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarHorario");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Horario(resultado.getInt("codigoHorario"),
                                resultado.getString("horarioInicio"),
                                resultado.getString("horarioSalida"),
                                resultado.getInt("lunes"),
                                resultado.getInt("martes"),
                                resultado.getInt("miercoles"),
                                resultado.getInt("jueves"),
                                resultado.getInt("viernes")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaHorario = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        txtHorarioInicio.setText(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioInicio());
        txtHorarioSalida.setText(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida());
        txtLunes.setText(String.valueOf(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getLunes()));
        txtMartes.setText(String.valueOf(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getMartes()));
        txtMiercoles.setText(String.valueOf(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getMiercoles()));
        txtJueves.setText(String.valueOf(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getJueves()));
        txtViernes.setText(String.valueOf(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getViernes()));
    }
    
    public Horario buscarHorario(int codigoHorario){
        Horario resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscarHorario(?)");
            procedimiento.setInt(1, codigoHorario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Horario(
                        registro.getInt("codigoHorario"),
                        registro.getString("horarioInicio"),
                        registro.getString("horarioSalida"),
                        registro.getInt("lunes"),
                        registro.getInt("martes"),
                        registro.getInt("miercoles"),
                        registro.getInt("jueves"),
                        registro.getInt("viernes"));
            
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
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
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ModificarHorario(?,?,?,?,?,?,?,?)");
            Horario registro = (Horario)tblHorarios.getSelectionModel().getSelectedItem();
            registro.setHorarioInicio(txtHorarioInicio.getText());
            registro.setHorarioSalida(txtHorarioSalida.getText());
            registro.setLunes(Integer.parseInt(txtLunes.getText()));
            registro.setMartes(Integer.parseInt(txtMartes.getText()));
            registro.setMiercoles(Integer.parseInt(txtMiercoles.getText()));
            registro.setJueves(Integer.parseInt(txtJueves.getText()));
            registro.setViernes(Integer.parseInt(txtViernes.getText()));
            
            procedimiento.setInt(1, registro.getCodigoHorario());
            procedimiento.setString(2, registro.getHorarioInicio());
            procedimiento.setString(3, registro.getHorarioSalida());
            procedimiento.setInt(4, registro.getLunes());
            procedimiento.setInt(5, registro.getMartes());
            procedimiento.setInt(6, registro.getMiercoles());
            procedimiento.setInt(7, registro.getJueves());
            procedimiento.setInt(8, registro.getViernes());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch (tipoDeOperacion){
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
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro", "Eliminar Horario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarHorario(?)}");
                            procedimiento.setInt(1, ((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
                            procedimiento.execute();
                            listaHorario.remove(tblHorarios.getSelectionModel().getSelectedIndex());
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
        Horario registro = new Horario();
        registro.setHorarioInicio(txtHorarioInicio.getText());
        registro.setHorarioSalida(txtHorarioSalida.getText());
        registro.setLunes(Integer.parseInt(txtLunes.getText()));
        registro.setMartes(Integer.parseInt(txtMartes.getText()));
        registro.setMiercoles(Integer.parseInt(txtMiercoles.getText()));
        registro.setJueves(Integer.parseInt(txtJueves.getText()));
        registro.setViernes(Integer.parseInt(txtViernes.getText()));
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarHorario(?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getHorarioInicio());
            procedimiento.setString(2, registro.getHorarioSalida());
            procedimiento.setInt(3, registro.getLunes());
            procedimiento.setInt(4, registro.getMartes());
            procedimiento.setInt(5, registro.getMiercoles());
            procedimiento.setInt(6, registro.getJueves());
            procedimiento.setInt(7, registro.getViernes());
            procedimiento.execute();
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void desactivarControles(){
        txtHorarioInicio.setEditable(false);
        txtHorarioSalida.setEditable(false);
        txtLunes.setEditable(false);
        txtMartes.setEditable(false);
        txtMiercoles.setEditable(false);
        txtJueves.setEditable(false);
        txtViernes.setEditable(false);
    }
    
    public void activarControles(){
        txtHorarioInicio.setEditable(true);
        txtHorarioSalida.setEditable(true);
        txtLunes.setEditable(true);
        txtMartes.setEditable(true);
        txtMiercoles.setEditable(true);
        txtJueves.setEditable(true);
        txtViernes.setEditable(true);
    }
    
    public void limpiarControles(){
        txtHorarioInicio.setText("");
        txtHorarioSalida.setText("");
        txtLunes.setText("");
        txtMartes.setText("");
        txtMiercoles.setText("");
        txtJueves.setText("");
        txtViernes.setText("");
    }
    
    public void imprimirReporte(){
        if(tblHorarios.getSelectionModel().getSelectedItem() != null){
            int codigoHorario = ((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario();
            Map parametro = new HashMap();
            parametro.put("p_codigoHorario", codigoHorario);
            GenerarReporte.mostrarReporte("ReporteBuscarHorarios.jasper", "Reporte de Horarios", parametro);
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
