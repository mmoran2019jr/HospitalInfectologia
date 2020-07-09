create database DBHospitalInfectologia2018549;
drop database DBHospitalInfectologia2018549;
use DBHospitalInfectologia2018549;

ALTER USER 'root'@'localHost' IDENTIFIED WITH mysql_native_password BY 'admin';

-- Medicos ------------------------------------------------------------
create table Medicos(
codigoMedico int(10) primary key auto_increment not null,
licenciaMedica integer(10) not null,
nombres varchar(100),
apellidos varchar(100),
horaEntrada varchar(10) not null,
horaSalida varchar(10) not null,
turnoMaximo int default 0,
sexo varchar(15)
);

-- Procedimietos Medicos /-/-/-/-/-/-/-/-/
DELIMITER $$
create procedure sp_AgregarMedico(p_licenciaMedica int, p_nombres varchar(100), p_apellidos varchar(100),
					p_horaEntrada varchar(10), p_horaSalida varchar(10), p_sexo varchar(15))
begin
	insert into Medicos (licenciaMedica, nombres, apellidos, horaEntrada, horaSalida, sexo)
	values(p_licenciaMedica, p_nombres, p_apellidos, p_horaEntrada, p_horaSalida, p_sexo);
end $$


DELIMITER $$
create procedure sp_ModificarMedico(p_codigoMedico int(10), p_licenciaMedica int(10), p_nombres varchar(100),
									p_apellidos varchar(100), p_horaEntrada varchar(10), p_horaSalida varchar(10),
									p_sexo varchar(15))
begin
	update Medicos
    set licenciaMedica = p_licenciaMedica,
    nombres = p_nombres,
    apellidos = p_apellidos,
    horaEntrada = p_horaEntrada,
    horaSalida = p_horaSalida,
    sexo = p_sexo
    where codigoMedico = p_codigoMedico;
end $$

DELIMITER $$
create procedure sp_EliminarMedico(p_codigoMedico int(10))
begin
	delete from Medicos
    where codigoMedico = p_codigoMedico;
end $$

DELIMITER $$
create procedure sp_ListarMedico()
begin
	select C.codigoMedico as codigoMedico, licenciaMedica, nombres, apellidos, horaEntrada, horaSalida, turnoMaximo, sexo
    from Medicos C;
end $$

call sp_ListarMedico();

DELIMITER $$
create procedure sp_BuscarMedico(p_codigoMedico int)
begin
	select codigoMedico, licenciaMedica, nombres, apellidos, horaEntrada, horaSalida, turnoMaximo, sexo
    from Medicos
    where codigoMedico = p_codigoMedico;
end $$

drop procedure sp_BuscarMedico;
-- telefonosMedico -----------------------------------------------------
create table telefonosMedico(
codigoTelefonoMedico int(10) primary key auto_increment, 
telefonoPersonal varchar(15),
telefonoTrabajo varchar(15),
codigoMedico int not null,
foreign key (codigoMedico) references Medicos (codigoMedico)
);

-- Procedimientos telefonosMedico /-/-/-/-/-/-/-/
DELIMITER $$
create procedure sp_AgregarTelefonoMedico(p_telefonoPersonal varchar(15), p_telefonoTrabajo varchar(15), p_codigoMedico int)
begin
	insert into telefonosMedico(telefonoPersonal, telefonoTrabajo, codigoMedico)
    values(p_telefonoPersonal, p_telefonoTrabajo, p_codigoMedico);
end $$


DELIMITER $$
create procedure sp_ModificarTelefonoMedico(p_codigoTelefonoMedico int(10), p_telefonoPersonal varchar(15),
											 p_telefonoTrabajo varchar(15), p_codigoMedico int)
begin
	update telefonosMedico
	set telefonoPersonal = p_telefonoPersonal,
        telefonoTrabajo = p_telefonoTrabajo,
        codigoMedico = p_codigoMedico
	where codigoTelefonoMedico = p_codigoTelefonoMedico;
end $$

DELIMITER $$
create procedure sp_EliminarTelefonoMedico(p_codigoTelefonoMedico int(10))
begin
	delete from telefonosMedico
    where codigoTelefonoMedico = p_codigoTelefonoMedico;
end $$

DELIMITER $$
create procedure sp_ListarTelefonoMedico()
begin
	select C.codigoTelefonoMedico as codigoTelefonoMedico, telefonoPersonal, telefonoTrabajo, codigoMedico
    from telefonosMedico C;
end $$

call sp_ListarTelefonoMedico();

DELIMITER $$
create procedure sp_BuscarTelefonoMedico(p_codigoTelefonoMedico int)
begin
	select codigoTelefonoMedico, telefonoPersonal, telefonoTrabajo, codigoMedico
    from telefonosMedico
    where codigoTelefonoMedico = p_codigoTelefonoMedico;
end $$

drop procedure sp_BuscarTelefonoMedico;

-- Horarios -----------------------------------------------------------
create table Horarios(
codigoHorario int(10) primary key auto_increment,
horarioInicio varchar(25),
horarioSalida varchar(25),
lunes TINYINT,
martes TINYINT,
miercoles TINYINT,
jueves TINYINT,
viernes TINYINT
);

-- Procedimientos Horarios /-/-/-/-/-/-/-/
DELIMITER $$
create procedure sp_AgregarHorario(p_horarioInicio varchar(45), p_horarioSalida varchar(45), p_lunes TINYINT, p_martes TINYINT,
									p_miercoles TINYINT, p_jueves TINYINT, p_viernes TINYINT)
begin
	insert into Horarios(horarioInicio, horarioSalida, lunes, martes, miercoles, jueves, viernes)
    values(p_horarioInicio, p_horarioSalida, p_lunes, p_martes, p_miercoles, p_jueves, p_viernes);
end $$

DELIMITER $$
create procedure sp_ModificarHorario(p_codigoHorario int(10), p_horarioInicio varchar(45), p_horarioSalida varchar(45),
									 p_lunes TINYINT, p_martes TINYINT, p_miercoles TINYINT, p_jueves TINYINT,
									 p_viernes TINYINT)
begin
      update Horarios
	  set horarioInicio = p_horarioInicio,
          horarioSalida = p_horarioSalida,
          lunes = p_lunes,
          martes = p_martes,
          miercoles = p_miercoles,
          jueves = p_jueves,
          viernes = p_viernes
	  where codigoHorario = p_codigoHorario;
end $$

DELIMITER $$
create procedure sp_EliminarHorario(p_codigoHorario int(10))
begin
	delete from Horarios
    where codigoHorario = p_codigoHorario;
end $$

DELIMITER $$
create procedure sp_ListarHorario()
begin
	select C.codigoHorario as codigoHorario, horarioInicio, horarioSalida, lunes, martes, miercoles, jueves, viernes
    from Horarios C;
end $$

call sp_ListarHorario();


DELIMITER $$
create procedure sp_BuscarHorario(p_codigoHorario int)
begin 
	select codigoHorario, horarioInicio, horarioSalida, lunes, martes, miercoles, jueves, viernes
    from Horarios
	where codigoHorario = p_codigoHorario;
end $$


-- Especialidades ----------------------------------------------------
create table Especialidades(
codigoEspecialidad int(10) primary key auto_increment,
nombreEspecialidad varchar(45)
);

DELIMITER $$
create procedure sp_AgregarEspecialidad(p_nombreEspecialidad varchar(45))
begin
	insert into Especialidades(nombreEspecialidad)
    values(p_nombreEspecialidad);
end $$

DELIMITER $$
create procedure sp_ModificarEspecialidad(p_codigoEspecialidad int(10), p_nombreEspecialidad varchar(45))
begin
	update Especialidades
        set nombreEspecialidad = p_nombreEspecialidad
	where codigoEspecialidad = p_codigoEspecialidad;
end $$

DELIMITER $$
create procedure sp_EliminarEspecialidad(p_codigoEspecialidad int(10))
begin
	delete from Especialidades
    where codigoEspecialidad = p_codigoEspecialidad;
end $$

DELIMITER $$
create procedure sp_ListarEspecialidad()
begin
	select C.codigoEspecialidad as codigoEspecialidad, nombreEspecialidad
    from Especialidades C;
end $$

call sp_ListarEspecialidad();

DELIMITER $$
create procedure sp_BuscarEspecialidad(p_codigoEspecialidad int)
begin
	select codigoEspecialidad, nombreEspecialidad
    from Especialidades
    where codigoEspecialidad = p_codigoEspecialidad;
end $$

-- Medico_Especialidad ----------------------------------------------
create table Medico_Especialidad(
codigoMedicoEspecialidad int(10) primary key auto_increment,
codigoMedico int not null,
codigoEspecialidad int not null,
codigoHorario int not null,
foreign key (codigoMedico) references Medicos (codigoMedico),
foreign key (codigoEspecialidad) references Especialidades (codigoEspecialidad),
foreign key (codigoHorario) references Horarios (codigoHorario)
);

DELIMITER $$
create procedure sp_AgregarMedicoEspecialidad(p_codigoMedico int, p_codigoEspecialidad int, p_codigoHorario int)
begin
	insert into Medico_Especialidad(codigoMedico, codigoEspecialidad, codigoHorario)
    values(p_codigoMedico, p_codigoEspecialidad, p_codigoHorario);
end $$


DELIMITER $$
create procedure sp_ModificarMedicoEspecialidad(p_codigoMedicoEspecialidad int(10), p_codigoMedico int, p_codigoEspecialidad int,
												p_codigoHorario int)
begin
	update Medico_Especialidad
    set codigoMedico = p_codigoMedico,
        codigoEspecialidad = p_codigoEspecialidad,
        codigoHorario = p_codigoHorario
	where codigoMedicoEspecialidad = p_codigoMedicoEspecialidad;
end $$


DELIMITER $$
create procedure sp_EliminarMedicoEspecialidad(p_codigoMedicoEspecialidad int(10))
begin
	delete from Medico_Especialidad
    where codigoMedicoEspecialidad = p_codigoMedicoEspeicialidad;
end $$


DELIMITER $$
create procedure sp_ListarMedicoEspecialidad()
begin
	select C.codigoMedicoEspecialidad as codigoMedicoEspecialidad, codigoMedico, codigoEspecialidad, codigoHorario
	from Medico_Especialidad C;
end $$


DELIMITER $$
create procedure sp_BuscarMedicoEspecialidad(p_codigoMedicoEspecialidad int)
begin 
	select codigoMedicoEspecialidad, codigoMedico, codigoEspecialidad, codigoHorario
    from Medico_Especialidad
    where codigoMedicoEspecialidad = p_codigoMedicoEspecialidad;
end $$ 


-- Pacientes -------------------------------------------------------
create table Pacientes(
codigoPaciente int(10) primary key auto_increment,
DPI varchar(20),
nombres varchar(100),
apellidos varchar(100),
fechaNacimiento varchar(20),
edad int,
direccion varchar(150),
ocupacion varchar(150),
sexo varchar(15)
);

DELIMITER $$
create procedure sp_AgregarPaciente(p_DPI varchar(20), p_nombres varchar(100), p_apellidos varchar(100), p_fechaNacimiento varchar(20),
									p_edad int, p_direccion varchar(150), p_ocupacion varchar(150), p_sexo varchar(15))
begin
	insert into Pacientes(DPI, nombres, apellidos, fechaNacimiento, edad, direccion, ocupacion, sexo)
    values(p_DPI, p_nombres, p_apellidos, p_fechaNacimiento, p_edad, p_direccion, p_ocupacion, p_sexo);
end $$


DELIMITER $$
create procedure sp_ModificarPaciente(p_codigoPaciente int(10), p_DPI varchar(20), p_nombres varchar(100), p_apellidos varchar(100),
									  p_fechaNacimiento varchar(20), p_edad int, p_direccion varchar(150), p_ocupacion varchar(150),
                                      p_sexo varchar(15))
begin
	update Pacientes
    set DPI = p_DPI,
        nombres = p_nombres,
        apellidos = p_apellidos,
        fechaNacimiento = p_fechaNacimiento,
        edad = p_edad,
        direccion = p_direccion,
        ocupacion = p_ocupacion,
        sexo = p_sexo
	where codigoPaciente = p_codigoPaciente;
end $$

DELIMITER $$ 
create procedure sp_EliminarPaciente(p_codigoPaciente int(10))
begin
	delete from Pacientes
    where codigoPaciente = p_codigoPaciente;
end $$

DELIMITER $$
create procedure sp_ListarPaciente()
begin
	select C.codigoPaciente as codigoPaciente, DPI, nombres, apellidos, fechaNacimiento, edad, direccion, ocupacion, sexo
    from Pacientes C;
end $$

call sp_ListarPaciente();

DELIMITER $$
create procedure sp_BuscarPaciente(p_codigoPaciente int)
begin	
	select codigoPaciente, DPI, nombres, apellidos, fechaNacimiento, edad, direccion, ocupacion, sexo
    from Pacientes
    where codigoPaciente = p_codigoPaciente;
end $$

-- contactoUrgencia ----------------------------------------------
create table contactoUrgencia(
codigoContactoUrgencia int(10) primary key auto_increment,
nombres varchar(100),
apellidos varchar(100),
numeroContacto varchar(10),
codigoPaciente int not null,
foreign key(codigoPaciente) references Pacientes (codigoPaciente)
);

DELIMITER $$
create procedure sp_AgregarContactoUrgencia(p_nombres varchar(100), p_apellidos varchar(100), p_numeroContacto varchar(10),
											p_codigoPaciente int)
begin
	insert into contactoUrgencia(nombres, apellidos, numeroContacto, codigoPaciente)
    values(p_nombres, p_apellidos, p_numeroContacto, p_codigoPaciente);
end $$

DELIMITER $$
create procedure sp_ModificarContactoUrgencia(p_codigoContactoUrgencia int(10), p_nombres varchar(100), p_apellidos varchar(100),
											  p_numeroContacto varchar(10), p_codigoPaciente int)
begin
	update contactoUrgencia
    set nombres = p_nombres,
        apellidos = p_apellidos,
        numeroContacto = p_numeroContacto,
        codigoPaciente = p_codigoPaciente
	where codigoContactoUrgencia = p_codigoContactoUrgencia;
end $$

DELIMITER $$
create procedure sp_EliminarContactoUrgencia(p_codigoContactoUrgencia int(10))
begin
	delete from contactoUrgencia
    where codigoContactoUrgencia = p_codigoContactoUrgencia;
end $$

DELIMITER $$
create procedure sp_ListarContactoUrgencia()
begin
	select C.codigoContactoUrgencia as codigoContactoUrgencia, nombres, apellidos, numeroContacto, codigoPaciente
    from contactoUrgencia C;
end $$

call sp_ListarContactoUrgencia();

DELIMITER $$
create procedure sp_BuscarContactoUrgencia(p_codigoContactoUrgencia int)
begin
	select codigoContactoUrgencia, nombres, apellidos, numeroContacto, codigoPaciente
    from contactoUrgencia
    where codigoContactoUrgencia = p_codigoContactoUrgencia ;
end $$

-- Areas ---------------------------------------------------
create table Areas(
codigoArea int(10) primary key auto_increment,
nombreArea varchar(45)
);

DELIMITER $$
create procedure sp_AgregarArea(p_nombreArea varchar(45))
begin
	insert into Areas(nombreArea)
    values(p_nombreArea);
end $$

DELIMITER $$
create procedure sp_ModificarArea(p_codigoArea int(10), p_nombreArea varchar(45))
begin
	update Areas
    set nombreArea = p_nombreArea
    where codigoArea = p_codigoArea;
end $$

DELIMITER $$
create procedure sp_EliminarArea(p_codigoArea int(10))
begin
	delete from Areas
    where codigoArea = p_codigoArea;
end $$

DELIMITER $$
create procedure sp_ListarArea()
begin
	select C.codigoArea as codigoArea, nombreArea
    from Areas C;
end $$

call sp_ListarArea();

DELIMITER $$
create procedure sp_BuscarArea(p_codigoArea int)
begin
	select codigoArea, nombreArea
    from Areas
    where codigoArea = p_codigoArea;
end $$


-- Cargos ------------------------------------------------
create table Cargos(
codigoCargo int(10) primary key auto_increment,
nombreCargo varchar(45)
);

DELIMITER $$
create procedure sp_AgregarCargo(p_nombreCargo varchar(45))
begin
	insert into Cargos(nombreCargo)
    values(p_nombreCargo);
end $$

DELIMITER $$
create procedure sp_ModificarCargo(p_codigoCargo int(10), p_nombreCargo varchar(45))
begin
	update Cargos
    set nombreCargo = p_nombreCargo
    where codigoCargo = p_codigoCargo;
end $$

DELIMITER $$
create procedure sp_EliminarCargo(p_codigoCargo int(10))
begin
	delete from Cargos
    where codigoCargo = p_codigoCargo;
end $$

DELIMITER $$
create procedure sp_ListarCargo()
begin
	select C.codigoCargo as codigoCargo, nombreCargo
    from Cargos C;
end $$

DELIMITER $$
create procedure sp_BuscarCargo(p_codigoCargo int)
begin
	select codigoCargo, nombreCargo 
    from Cargos
    where codigoCargo = p_codigoCargo;
end $$

-- ResponsableTurno -------------------------------------
create table ResponsableTurno(
codigoResponsableTurno int(10) primary key auto_increment,
nombreResponsable varchar(75),
apellidoResponsable varchar(45),
telefonoPersonal varchar(10),
codigoArea int not null,
codigoCargo int not null,
foreign key(codigoArea) references Areas (codigoArea),
foreign key(codigoCargo) references Cargos (codigoCargo)
);

DELIMITER $$
create procedure sp_AgregarResponsableTurno(p_nombreResponsable varchar(75), p_apellidoResponsable varchar(45),
											p_telefonoPersonal varchar(10), p_codigoArea int, p_codigoCargo int)
begin
	insert into ResponsableTurno(nombreResponsable, apellidoResponsable, telefonoPersonal, codigoArea, codigoCargo)
    values(p_nombreResponsable, p_apellidoResponsable, p_telefonoPersonal, p_codigoArea, p_codigoCargo);
end $$


call sp_AgregarResponsableTurno('Walter', 'Chinchilla', '35456765', 1, 3);

DELIMITER $$
create procedure sp_ModificarResponsableTurno(p_codigoResponsableTurno int(10), p_nombreResponsable varchar(75),
											  p_apellidoResponsable varchar(45), p_telefonoPersonal varchar(10),
                                              p_codigoArea int, p_codigoCargo int)
begin
	update ResponsableTurno
    set nombreResponsable = p_nombreResponsable,
        apellidoResponsable = p_apellidoResponsable,
        telefonoPersonal = p_telefonoPersonal,
        codigoArea = p_codigoArea,
        codigoCargo = p_codigoCargo
	where codigoResponsableTurno = p_codigoResponsableTurno;
end $$

DELIMITER $$
create procedure sp_EliminarResponsableTurno(p_codigoResponsableTurno int(10))
begin
	delete from ResponsableTurno
    where codigoResponsableTurno = p_codigoResponsableTurno;
end $$

DELIMITER $$
create procedure sp_ListarResponsableTurno()
begin
	select C.codigoResponsableTurno as codigoResponsableTurno, nombreResponsable, apellidoResponsable, telefonoPersonal, codigoArea, codigoCargo
    from ResponsableTurno C;
end $$

drop procedure sp_ListarResponsableTurno

DELIMITER $$
create procedure sp_BuscarResponsableTurno(p_codigoResponsableTurno int)
begin
	select codigoResponsableTurno, nombreResponsable, apellidoResponsable, telefonoPersonal, codigoArea, codigoCargo
    from ResponsableTurno
	where codigoResponsableTurno = p_codigoResponsableTurno;
end $$


-- Turno ---------------------------------------------
create table Turnos(
codigoTurno int(10) primary key auto_increment,
fechaTurno varchar(25),
fechaCita varchar(25),
valorCita decimal(10,2),
codigoMedicoEspecialidad int not null,
codigoResponsableTurno int not null,
codigoPaciente int not null,
foreign key(codigoMedicoEspecialidad) references Medico_Especialidad (codigoMedicoEspecialidad),
foreign key(codigoResponsableTurno) references ResponsableTurno (codigoResponsableTurno),
foreign key(codigoPaciente) references Pacientes (codigoPaciente)
);

DELIMITER $$
create procedure sp_AgregarTurno(p_fechaTurno varchar(25), p_fechaCita varchar(25), p_valorCita decimal(10,2), p_codigoMedicoEspecialidad int,
								 p_codigoResponsableTurno int, p_codigoPaciente int)
begin
	insert into Turnos(fechaTurno, fechaCita, valorCita, codigoMedicoEspecialidad, codigoResponsableTurno, codigoPaciente)
    values(p_fechaTurno, p_fechaCita, p_valorCita, p_codigoMedicoEspecialidad, p_codigoResponsableTurno, p_codigoPaciente);
end $$

DELIMITER $$
create procedure sp_ModificarTurno(p_codigoTurno int(10), p_fechaTurno varchar(25), p_fechaCita varchar(25), p_valorCita decimal(10,2),
								   p_codigoMedicoEspecialidad int, p_codigoResponsableTurno int, p_codigoPaciente int)
begin
	update Turnos
		set fechaTurno = p_fechaTurno,
            fechaCita = p_fechacita,
            valorCita = p_valorCita,
            codigoMedicoEspecialidad = p_codigoMedicoEspecialidad,
            codigoResponsableTurno = p_codigoResponsableTurno,
            codigoPaciente = p_codigoPaciente
		where codigoTurno = p_codigoTurno;
end $$

DELIMITER $$
create procedure sp_EliminarTurno(p_codigoTurno int(10))
begin
	delete from Turnos
    where codigoTurno = p_codigoTurno;
end $$

DELIMITER $$
create procedure sp_ListarTurno()
begin
	select C.codigoTurno as codigoTurno, fechaTurno, fechaCita, valorCita, codigoMedicoEspecialidad, codigoResponsableTurno, codigoPaciente
    from Turnos C;
end $$

call sp_ListarTurno();

DELIMITER $$
create procedure sp_BuscarTurno(p_codigoTurno int)
begin
	select codigoTurno, fechaTurno, fechaCita, valorCita, codigoMedicoEspecialidad, codigoResponsableTurno, codigoPaciente
    from Turnos
    where codigoTurno = p_codigoTurno;
end $$



-- Script para examen III bimestre --//////////////
create table TipoUsuario(
codigoTipoUsuario int(10) not null primary key auto_increment,
nombre varchar(100),
descripcion varchar(150)
);

DELIMITER $$
create procedure sp_AgregarTipoUsuario(p_nombre varchar(100), p_descripcion varchar(150))
begin
	insert into TipoUsuario(nombre, descripcion)
    values(p_nombre, p_descripcion);
end $$

call sp_AgregarTipoUsuario('Administrador', 'N');
call sp_AgregarTipoUsuario('Root', 'N');
call sp_AgregarTipoUsuario('Invitado', 'N');


DELIMITER $$
create procedure sp_ModificarTipoUsuario(p_codigoTipoUsuario int(10), p_nombre varchar(100), p_descripcion varchar(150))
begin
	update TipoUsuario
		set nombre = p_nombre,
			descripcion = p_descripcion
		where codigoTipoUsuario = p_codigoTipoUsuario;
end $$

DELIMITER $$
create procedure sp_EliminarTipoUsuario(p_codigoTipoUsuario int(10))
begin
	delete from TipoUsuario
    where codigoTipoUsuario = p_codigoTipoUsuario;
end $$

call sp_EliminarTipoUsuario(1);

DELIMITER $$
create procedure sp_ListarTipoUsuario()
begin
	select C.codigoTipoUsuario as codigoTipoUsuario, nombre, descripcion
    from TipoUsuario C;
end $$

call sp_ListarTipoUsuario();

DELIMITER $$
create procedure sp_BuscarTipoUsuario(p_codigoTipoUsuario int(10))
begin
	select codigoTipoUsuario, nombre, descripcion
    from TipoUsuario
    where codigoTipoUsuario = p_codigoTipoUsuario;
end $$


create table Usuarios(
codigoUsuario int(10) not null primary key auto_increment,
usuarioLogin varchar(45),
usuarioContrasena varchar(45),
usuarioEstado TINYINT,
usuarioFecha DATE,
usuarioHora varchar(25),
codigoTipoUsuario int(10) not null,
foreign key (codigoTipoUsuario) references TipoUsuario (codigoTipoUsuario)
);

-- Procedimientos -/-/-/-/-/
DELIMITER $$
create procedure sp_AgregarUsuario(p_usuarioLogin varchar(45), p_usuarioContrasena varchar(45), p_usuarioEstado TINYINT, p_usuarioFecha DATE, p_usuarioHora varchar(25), p_codigoTipoUsuario int(10))
begin
	insert into Usuarios(usuarioLogin, usuarioContrasena, usuarioEstado, usuarioFecha, usuarioHora, codigoTipoUsuario)
    values(p_usuarioLogin, p_usuarioContrasena, p_usuarioEstado, p_usuarioFecha, p_usuarioHora, p_codigoTipoUsuario);
end $$

call sp_AgregarUsuario('mmoran', '12345', true, '2019-07-26', '15:35:00', 1);

DELIMITER $$
create procedure sp_ModificarUsuario(p_codigoUsuario int(10), p_usuarioLogin varchar(45), p_usuarioContrasena varchar(45), p_usuarioEstado TINYINT, p_usuarioFecha DATE,
										p_usuarioHora varchar(25), p_codigoTipoUsuario int(10))
begin
	update Usuarios
		set usuarioLogin = p_usuarioLogin,
			usuarioContrasena = p_usuarioContrasena,
			usuarioEstado = p_usuarioEstado,
            usuarioFecha = p_usuarioFecha,
            usuarioHora = p_usuarioHora,
            codigoTipoUsuario = p_codigoTipoUsuario
		where codigoUsuario = p_codigoUsuario;
end $$

DELIMITER $$
create procedure sp_EliminarUsuario(p_codigoUsuario int(10))
begin
	delete from Usuarios
    where codigoUsuario = p_codigoUsuario;
end $$

call sp_EliminarUsuario(4)

DELIMITER $$
create procedure sp_ListarUsuario()
begin
	select C.codigoUsuario as codigousuario, usuarioLogin, usuarioContrasena, usuarioEstado, usuarioFecha, usuarioHora, codigoTipoUsuario
    from Usuarios C;
end $$

call sp_ListarUsuario()
DELIMITER $$
create procedure sp_BuscarUsuario(p_codigoUsuario int(10))
begin
	select codigoUsuario, usuarioLogin, usuarioContrasena, usuarioEstado, usuarioFecha, usuarioHora, codigoTipoUsuario
    from Usuarios
    where codigoUsuario = p_codigoUsuario;
end $$

	
-- Script para reporte general ----
create view vReporteGeneral as 
select m.*, 
	h.lunes, h.martes, h.miercoles, h.jueves, h.viernes, e.nombreEspecialidad, 
	p.DPI, p.nombres as nombresPaciente, p.apellidos as apellidosPaciente, p.fechaNacimiento, p.edad, p.direccion,
    p.ocupacion, p.sexo as sexoPaciente, rt.nombreResponsable, rt.apellidoResponsable, rt.telefonoPersonal, c.nombreCargo, a.nombreArea
    
		from Medicos m
		inner join medico_especialidad me 
		on me.codigoMedico = m.codigoMedico
		inner join especialidades e
		on e.codigoEspecialidad = me.codigoEspecialidad
		inner join horarios h
		on h.codigoHorario = me.codigoHorario
		left join turnos t
		on t.codigoMedicoEspecialidad = me.codigoMedicoEspecialidad
		left join pacientes p
		on p.codigoPaciente = t.codigoPaciente
		left join responsableturno rt
		on rt.codigoResponsableTurno = t.codigoResponsableTurno
		left join cargos c
		on c.codigoCargo = rt.codigoCargo
		left join areas a 
		on a.codigoArea = rt.codigoArea;
        

-- Script para reporte General examen III bimestre -----
create view ReporteUsuarioLogin as
select u.codigoUsuario, u.usuarioLogin, u.usuarioContrasena, u.usuarioEstado, u.usuarioFecha, u.usuarioHora, tu.codigoTipoUsuario, tu.nombre,  tu.descripcion
from Usuarios u inner join TipoUsuario tu on tu.codigoTipoUsuario = u.codigoTipoUsuario;

select * from ReporteUsuarioLogin