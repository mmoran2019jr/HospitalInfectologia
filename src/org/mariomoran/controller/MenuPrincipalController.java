/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mariomoran.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.mariomoran.report.GenerarReporte;
import org.mariomoran.sistema.Principal;

/**
 *
 * @author mmora
 */
public class MenuPrincipalController implements Initializable{
   public Principal escenarioPrincipal;
    @Override
    
    public void initialize(URL location, ResourceBundle resources) {
       
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaMedico(){
        escenarioPrincipal.ventanaMedico();
    }
    
    public void ventanaTelefonoMedico(){
        escenarioPrincipal.ventanaTelefonoMedico();
    }
    
    public void ventanaPaciente(){
        escenarioPrincipal.ventanaPaciente();
    }
    
    public void ventanaContactoUrgencia(){
        escenarioPrincipal.ventanaContactoUrgencia();
    }
    
    public void ventanaArea(){
        escenarioPrincipal.ventanaArea();
    }
    
    public void ventanaCargo(){
        escenarioPrincipal.ventanaCargo();
    }
    
    public void ventanaEspecialidad(){
        escenarioPrincipal.ventanaEspecialidad();
    }
    
    public void ventanaMedicoEspecialidad(){
        escenarioPrincipal.ventanaMedicoEspecialidad();
    }
    
    public void ventanaResponsableTurno(){
        escenarioPrincipal.ventanaResponsableTurno();
    }
    
    public void ventanaHorario(){
        escenarioPrincipal.ventanaHorario();
    }
    
    public void ventanaTurno(){
        escenarioPrincipal.ventanaTurno();
    }
    
    public void imprimirReporteMedicos(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteListarMedicos.jasper", "Reporte Medicos", parametros);
    }
    
    public void imprimirReportePacientes(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteListarPacientes.jasper", "Reporte Pacientes", parametros);
    }
    
    public void imprimirReporteAreas(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteListarAreas.jasper", "Reporte Areas", parametros);
    }
    
    public void imprimirReporteCargos(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteListarCargos.jasper", "Reporte Cargos", parametros);
    }
    
    public void imprimirReporteTelefonosMedico(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteListarTelefonosMedico.jasper", "Reporte Telefonos Medico", parametros);
    }
    
    public void imprimirReporteContactosUrgencia(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteListarContactosUrgencia.jasper", "Reporte Contactos Urgencia", parametros);
    }
    
    public void imprimirReporteEspecialidades(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteListarEspecialidades.jasper", "Reporte Especialidades", parametros);
    }
    
    public void imprimirReporteMedicoEspecialidad(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteListarMedicosEspecialidad.jasper", "Reporte Medico_Especialidad", parametros);
    }
    
    public void imprimirReporteHorarios(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteListarHorarios.jasper", "Reporte Horarios", parametros);
    }
    
    public void imprimirReporteResponsablesTurno(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteListarResponsablesTurno.jasper", "Reporte Responsables Turno", parametros);
    }
    
    public void imprimirReporteTurnos(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteListarTurnos.jasper", "Reporte Turnos", parametros);
    }
    
 
    public void Salir(){
        System.exit(0);
    }
}
