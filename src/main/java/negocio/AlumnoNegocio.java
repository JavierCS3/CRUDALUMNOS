/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import dtos.AlumnoEliminarDTO;
import dtos.AlumnoGuardarDTO;
import dtos.AlumnoLecturaDTO;
import dtos.AlumnoTablaDTO;
import entidad.AlumnoEntidad;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.IAlumnoDAO;
import persistencia.PersistenciaException;

/**
 *
 * @author PC Gamer
 */
public class AlumnoNegocio implements IAlumnoNegocio{

    private IAlumnoDAO alumnoDAO;
    
    public AlumnoNegocio(IAlumnoDAO alumnoDAO)
    {
        this.alumnoDAO=alumnoDAO;
    }
    
    @Override
    public List<AlumnoTablaDTO> buscarAlumnosTabla() throws NegocioException {
        try{
           List<AlumnoEntidad> alumnos=this.alumnoDAO.buscarAlumnosTabla();
           return this.convertirAlumnoTablaDTO(alumnos);
        } catch(PersistenciaException ex){
            // hacer uso de Logger
            System.out.println(ex.getMessage());
            throw new NegocioException(ex.getMessage());
        }
    }

     private List<AlumnoTablaDTO> convertirAlumnoTablaDTO(List<AlumnoEntidad> alumnos) throws NegocioException {
        if (alumnos == null) {
            throw new NegocioException("No se pudieron obtener los alumnos");
        }

        List<AlumnoTablaDTO> alumnosDTO = new ArrayList<>();
        for (AlumnoEntidad alumno : alumnos) {
            AlumnoTablaDTO dto = new AlumnoTablaDTO();
            dto.setIdAlumno(alumno.getIdAlumno());
            dto.setNombres(alumno.getNombres());
            dto.setApellidoPaterno(alumno.getApellidoPaterno());
            dto.setApellidoMaterno(alumno.getApellidoMaterno());
            dto.setEstatus(alumno.isActivo() == true ? "Activo" : "Inactivo");
            alumnosDTO.add(dto);
        }
        return alumnosDTO;
     }

    @Override
    public AlumnoGuardarDTO insertarAlumno(String nombres, String apellidoPaterno, String apellidoMaterno,boolean activo) throws NegocioException {
        if (nombres.isEmpty() || apellidoPaterno.isEmpty()) {
        throw new NegocioException("Nombres y apellido Paterno no puede estar vacío");
        }
        try{
            AlumnoGuardarDTO alumno=new AlumnoGuardarDTO(nombres, apellidoPaterno, apellidoMaterno,activo);
       
        alumno=alumnoDAO.insertarAlumno(alumno);
        return alumno;
        } catch(PersistenciaException ex){
            System.out.println(ex.getMessage());
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public AlumnoLecturaDTO editarAlumno(int idAlumno, String nombres, String apellidoPaterno, String apellidoMaterno, boolean activo) throws NegocioException {
        if (nombres.isEmpty() || apellidoPaterno.isEmpty()) {
        throw new NegocioException("Nombres y apellido Paterno no pueden estar vacíos");
    }
    try {
        AlumnoLecturaDTO alumno = new AlumnoLecturaDTO(idAlumno, nombres, apellidoPaterno, apellidoMaterno, activo);

        alumno = alumnoDAO.editarAlumno(alumno);
        return alumno;
    } catch (PersistenciaException ex) {
        System.out.println(ex.getMessage());
        throw new NegocioException(ex.getMessage());
    }
    }
    
    @Override
    public AlumnoLecturaDTO obtenerAlumnoPorId(int idAlumno) throws NegocioException {
        try {
            return alumnoDAO.obtenerAlumnoPorId(idAlumno);
        } catch (PersistenciaException ex) {
            System.out.println(ex.getMessage());
          throw new NegocioException ("Hubo un error encontrando el usuario");
        }
    }

    @Override
    public AlumnoEliminarDTO eliminarAlumnoPorId(int idAlumno) throws NegocioException {
        try {
        return alumnoDAO.eliminarAlumnoPorId(idAlumno);
    } catch (PersistenciaException ex) {
        System.out.println(ex.getMessage());
        throw new NegocioException("Error al eliminar");
    }
        }
     
     
}
