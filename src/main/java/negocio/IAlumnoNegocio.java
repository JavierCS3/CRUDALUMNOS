/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import dtos.AlumnoEliminarDTO;
import dtos.AlumnoGuardarDTO;
import dtos.AlumnoLecturaDTO;
import dtos.AlumnoTablaDTO;
import java.util.List;

/**
 *
 * @author PC Gamer
 */
public interface IAlumnoNegocio {
    
    public AlumnoGuardarDTO insertarAlumno(String nombres, String apellidoPaterno, String apellidoMaterno,boolean activo) throws NegocioException;
    public AlumnoLecturaDTO editarAlumno(int id, String nombres, String apellidoPaterno, String apellidoMaterno,boolean activo) throws NegocioException;
    public AlumnoLecturaDTO obtenerAlumnoPorId(int idAlumno) throws NegocioException ;
    public AlumnoEliminarDTO eliminarAlumnoPorId(int idAlumno) throws NegocioException;
    public List<AlumnoTablaDTO> buscarAlumnosTabla() throws NegocioException;
}
