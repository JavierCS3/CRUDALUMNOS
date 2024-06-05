/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import dtos.AlumnoEliminarDTO;
import dtos.AlumnoGuardarDTO;
import dtos.AlumnoLecturaDTO;
import entidad.AlumnoEntidad;
import java.util.List;

/**
 *
 * @author PC Gamer
 */
public interface IAlumnoDAO {
    
    public AlumnoGuardarDTO insertarAlumno(AlumnoGuardarDTO alumno) throws PersistenciaException;
    public AlumnoLecturaDTO editarAlumno(AlumnoLecturaDTO alumno) throws PersistenciaException;
    public AlumnoLecturaDTO obtenerAlumnoPorId(int idAlumno) throws PersistenciaException;
    public AlumnoEliminarDTO eliminarAlumnoPorId(int idAlumno) throws PersistenciaException;
    public List<AlumnoEntidad> buscarAlumnosTabla() throws PersistenciaException;
}
