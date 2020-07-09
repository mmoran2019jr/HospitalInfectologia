    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mariomoran.sistema;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashSet;
import org.mariomoran.db.Conexion;
import org.mariomoran.controller.MenuPrincipalController;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.AnchorPane;
import org.mariomoran.controller.AreaController;
import org.mariomoran.controller.CargoController;
import org.mariomoran.controller.ContactoUrgenciaController;
import org.mariomoran.controller.EspecialidadController;
import org.mariomoran.controller.HorarioController;
import org.mariomoran.controller.LoginController;
import org.mariomoran.controller.MedicoController;
import org.mariomoran.controller.MedicoEspecialidadController;
import org.mariomoran.controller.PacientesController;
import org.mariomoran.controller.ProgramadorController;
import org.mariomoran.controller.ResponsableTurnoController;
import org.mariomoran.controller.TelefonoMedicoController;
import org.mariomoran.controller.TurnoController;
import org.mariomoran.controller.UsuarioController;

/**
 *
 * @author mmora
 */
public class Principal extends Application {
    
    public Stage escenarioPrincipal;
    public Scene escena;
     
    public final String PAQUETE_VISTA = "/org/mariomoran/view/";
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        
       Connection conn = Conexion.getInstancia().getConexion();
        Statement stmt = conn.createStatement();
       ResultSet rs = stmt.executeQuery("Select * from medicos");
       
       /*while(rs.next()){
           System.out.println(rs.getInt("codigoMedico"));
           System.out.println(rs.getString(1));
       } */
       
       
        /*Parent root = FXMLLoader.load(getClass().getResource(PAQUETE_VISTA+"menuPrincipalView.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();*/
        
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("***CONSULTA EXTERNA DE HOSPITALES");
        ventanaLogin();
       // menuPrincipal();
      
        escenarioPrincipal.show();
    }
    
    public void ventanaLogin(){
        try{
        LoginController LoginController =(LoginController) cambiarEscena("VistaLogin.fxml", 500, 350);
        LoginController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }    
    }
    
    public void ventanaUsuario(){
        try{
        UsuarioController UsuarioController = (UsuarioController) cambiarEscena("VistaUsuarios.fxml", 600, 400);
        UsuarioController.setEscenarioPrincipal(this); 
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
     public void menuPrincipal(){
        try{
            MenuPrincipalController menuPrincipal = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml",600,400);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
      
        }
    }
     
        public void ventanaProgramador(){
            try{
                ProgramadorController ProgramadorController = (ProgramadorController) cambiarEscena("VistaProgramadorView.fxml",600,367);
                ProgramadorController.setEscenarioPrincipal(this);                
            }catch (Exception e){
                e.printStackTrace();
            }
         }
        
        public void ventanaMedico(){
            try{
                MedicoController MedicoController = (MedicoController) cambiarEscena("VistaMedicoView.fxml", 745,530);
                MedicoController.setEscenarioPrincipal(this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        
         public void ventanaTelefonoMedico(){
            try{
                TelefonoMedicoController TelefonoMedicoController = (TelefonoMedicoController) cambiarEscena("VistaTelefonosMedico.fxml", 600,400);
                TelefonoMedicoController.setEscenarioPrincipal(this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
         
         public void ventanaPaciente(){
            try{
                PacientesController PacientesController = (PacientesController) cambiarEscena("VistaPacientes.fxml", 700, 530);
                PacientesController.setEscenarioPrincipal(this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
         
          public void ventanaContactoUrgencia(){
            try{
                ContactoUrgenciaController ContactoUrgenciaController = (ContactoUrgenciaController) cambiarEscena("VistaContactoUrgencia.fxml", 600, 400);
                ContactoUrgenciaController.setEscenarioPrincipal(this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
          
          public void ventanaArea(){
            try{
               AreaController AreaController = (AreaController) cambiarEscena("VistaArea.fxml", 600, 400);
               AreaController.setEscenarioPrincipal(this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
          
          public void ventanaCargo(){
            try{
               CargoController CargoController = (CargoController) cambiarEscena("VistaCargo.fxml", 600, 400);
               CargoController.setEscenarioPrincipal(this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
          
          public void ventanaEspecialidad(){
            try{
               EspecialidadController EspecialidadController = (EspecialidadController) cambiarEscena("VistaEspecialidad.fxml", 600, 400);
               EspecialidadController.setEscenarioPrincipal(this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
          
          public void ventanaMedicoEspecialidad(){
            try{
               MedicoEspecialidadController MedicoEspecialidadController = (MedicoEspecialidadController) cambiarEscena("VistaMedicoEspecialidad.fxml", 600, 400);
               MedicoEspecialidadController.setEscenarioPrincipal(this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
          
          public void ventanaResponsableTurno(){
            try{
               ResponsableTurnoController ResponsableTurnoController = (ResponsableTurnoController) cambiarEscena("VistaResponsableTurno.fxml", 600, 400);
               ResponsableTurnoController.setEscenarioPrincipal(this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
          
          public void ventanaHorario(){
            try{
               HorarioController HorarioController = (HorarioController) cambiarEscena("VistaHorarios.fxml", 650, 400);
               HorarioController.setEscenarioPrincipal(this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
          
          public void ventanaTurno(){
            try{
               TurnoController TurnoController = (TurnoController) cambiarEscena("VistaTurno.fxml", 675, 450);
               TurnoController.setEscenarioPrincipal(this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        
        
     public Initializable cambiarEscena(String fxml, int ancho, int alto) throws IOException{
        Initializable resultado = null;
            FXMLLoader cargadorFXML = new FXMLLoader();
            InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA +fxml);
            cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
            cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA +fxml));
            escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
            escenarioPrincipal.setScene(escena);
            escenarioPrincipal.sizeToScene();
            resultado = (Initializable) cargadorFXML.getController();
            return resultado;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}


