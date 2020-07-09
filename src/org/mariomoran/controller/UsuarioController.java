/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mariomoran.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.mariomoran.bean.TipoUsuario;
import org.mariomoran.bean.Usuario;
import org.mariomoran.db.Conexion;
import org.mariomoran.sistema.Principal;

/**
 *
 * @author programacion
 */
public class UsuarioController implements Initializable {
    
     private enum operaciones{AGREGAR, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    
    @FXML private TextField txtLogin;
    @FXML private TextField txtContrasena;
    @FXML private CheckBox chbEstado;
    private DatePicker dpdFecha;
    @FXML private GridPane grpFecha;
    @FXML private TextField txtHora;
    @FXML private ComboBox cmbCodigoTipoUsuario;
    @FXML private TableView tblUsuarios;
    @FXML private TableColumn colCodigoUsuario;
    @FXML private TableColumn colLogin;
    @FXML private TableColumn colContrasena;
    @FXML private TableColumn colEstado;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colCodigoTipoUsuario;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnCancelar;
    
    private Principal escenarioPrincipal;
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    private ObservableList<Usuario> listaUsuario;
    
    private ObservableList<TipoUsuario> listaTipoUsuario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoUsuario.setItems(getTipoUsuario());
        
        dpdFecha = new DatePicker(Locale.ENGLISH);
        dpdFecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dpdFecha.getCalendarView().todayButtonTextProperty().set("Today");
        dpdFecha.getCalendarView().setShowWeeks(false);
        grpFecha.add(dpdFecha,0, 0);
    }
    
    public void cargarDatos(){
        tblUsuarios.setItems(getUsuarios());
        colCodigoUsuario.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("codigoUsuario"));
        colLogin.setCellValueFactory(new PropertyValueFactory<Usuario, String>("usuarioLogin"));
        colContrasena.setCellValueFactory(new PropertyValueFactory<Usuario, String>("usuarioContrasena"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Usuario, Boolean>("usuarioEstado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Usuario, Date>("usuarioFecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<Usuario, String>("usuarioHora"));
        colCodigoTipoUsuario.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("codigoTipoUsuario"));
        
    }
    
    public ObservableList<TipoUsuario>getTipoUsuario(){
        ArrayList<TipoUsuario> lista = new ArrayList<TipoUsuario>();
          try{
              PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarTipoUsuario");
              ResultSet resultado = procedimiento.executeQuery();
              while(resultado.next()){
                  lista.add(new TipoUsuario(resultado.getInt("codigoTipoUsuario"),
                                            resultado.getString("nombre"),
                                            resultado.getString("descripcion")));
              }
              
          }catch(Exception e){
              e.printStackTrace();
          }
          
          return listaTipoUsuario = FXCollections.observableList(lista);
        
    }
    
    public ObservableList<Usuario>getUsuarios(){
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarUsuario");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Usuario(resultado.getInt("codigoUsuario"),
                                      resultado.getString("usuarioLogin"),
                                      resultado.getString("usuarioContrasena"),
                                      resultado.getBoolean("usuarioEstado"),
                                      resultado.getDate("usuarioFecha"),
                                      resultado.getString("usuarioHora"),
                                      resultado.getInt("codigoTipoUsuario")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaUsuario = FXCollections.observableList(lista);
        
    }
    
    public void seleccionarElemento(){
        txtLogin.setText(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getUsuarioLogin());
        txtContrasena.setText(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getUsuarioContrasena());
        txtHora.setText(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getUsuarioHora());
        cmbCodigoTipoUsuario.getSelectionModel().select(buscarTipoUsuario(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario()));
        
    }
    
    public TipoUsuario buscarTipoUsuario(int codigoTipoUsuario){
        TipoUsuario resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTipoUsuario(?)}");
            procedimiento.setInt(1, (codigoTipoUsuario));
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
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ModificarUsuario(?,?,?,?,?,?,?)");
            Usuario registro = (Usuario)tblUsuarios.getSelectionModel().getSelectedItem();
            registro.setCodigoUsuario(((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getCodigoUsuario());
            registro.setUsuarioLogin(txtLogin.getText());
            registro.setUsuarioContrasena(txtContrasena.getText());
            registro.setUsuarioEstado(chbEstado.isSelected());
            registro.setUsuarioFecha(dpdFecha.getSelectedDate());
            registro.setUsuarioHora(txtHora.getText());
            registro.setCodigoTipoUsuario(((TipoUsuario)cmbCodigoTipoUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario());
            
            procedimiento.setInt(1, registro.getCodigoUsuario());
            procedimiento.setString(2, registro.getUsuarioLogin());
            procedimiento.setString(3, registro.getUsuarioContrasena());
            procedimiento.setBoolean(4, registro.getUsuarioEstado());
            procedimiento.setDate(5, new java.sql.Date(registro.getUsuarioFecha().getTime()));
            procedimiento.setString(6, registro.getUsuarioHora());
            procedimiento.setInt(7, registro.getCodigoTipoUsuario());
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void editar(){
         switch(tipoDeOperacion){
            case NINGUNO:
               if(tblUsuarios.getSelectionModel().getSelectedItem() != null){
                   btnEditar.setText("Actualizar");
                   btnCancelar.setText("Cancelar");
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
                btnCancelar.setText("Cancelar");
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
                btnCancelar.setDisable(false);
                tipoDeOperacion = operaciones.GUARDAR;
            break;
            
            default:
                if(tblUsuarios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Usuario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarUsuario(?)}");
                            procedimiento.setInt(1, ((Usuario)tblUsuarios.getSelectionModel().getSelectedItem()).getCodigoUsuario());
                            procedimiento.execute();
                            listaUsuario.remove(tblUsuarios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            
                            
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        
                        
                    }else{
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
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
              btnCancelar.setDisable(true);
              limpiarControles();
              tipoDeOperacion = operaciones.GUARDAR;
            break;
             
            case GUARDAR:
                agregar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnCancelar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
    public void agregar(){
        Usuario registro = new Usuario();
        registro.setUsuarioLogin(txtLogin.getText());
        registro.setUsuarioContrasena(txtContrasena.getText());
        registro.setUsuarioEstado(chbEstado.isSelected());
        registro.setUsuarioFecha(dpdFecha.getSelectedDate());
        registro.setUsuarioFecha(dpdFecha.getSelectedDate());
        registro.setUsuarioHora(txtHora.getText());
        registro.setCodigoTipoUsuario(((TipoUsuario)cmbCodigoTipoUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getUsuarioLogin());
            procedimiento.setString(2, registro.getUsuarioContrasena());
            procedimiento.setBoolean(3, registro.getUsuarioEstado());
            procedimiento.setDate(4, new java.sql.Date (registro.getUsuarioFecha().getTime()));
            procedimiento.setString(5, registro.getUsuarioHora());
            procedimiento.setInt(6, registro.getCodigoTipoUsuario());
            procedimiento.execute();
            listaUsuario.add(registro);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
                
    }
    
    public void desactivarControles(){
        txtLogin.setDisable(false);
        txtContrasena.setDisable(false);
        chbEstado.setDisable(true);
        txtHora.setDisable(false);
        cmbCodigoTipoUsuario.setDisable(true);
    }
    
    public void activarControles(){
        txtLogin.setEditable(true);
        txtContrasena.setEditable(true);
        chbEstado.setDisable(false);
        txtHora.setEditable(true);
        cmbCodigoTipoUsuario.setDisable(false);
    }
    
    public void limpiarControles(){
        txtLogin.setText("");
        txtContrasena.setText("");
        txtHora.setText("");
        cmbCodigoTipoUsuario.getSelectionModel().clearSelection();
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
    
    public void ventanaLogin(){
        this.escenarioPrincipal.ventanaLogin();
    }
    
    
    
    
}
