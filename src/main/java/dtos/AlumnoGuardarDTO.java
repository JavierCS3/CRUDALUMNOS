/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

import java.util.Objects;

/**
 *
 * @author PC Gamer
 */
public class AlumnoGuardarDTO {
    private int idAlumno;
    String nombres;
    String apellidoPaterno;
    String apellidoMaterno;
    private boolean eliminado;
    private boolean activo;

    public AlumnoGuardarDTO() {
    }
    
    public AlumnoGuardarDTO(AlumnoGuardarDTO a) {
        this.nombres=a.nombres;
        this.apellidoMaterno=a.apellidoMaterno;
        this.apellidoPaterno=a.apellidoPaterno;
        this.activo=a.activo;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public AlumnoGuardarDTO( String nombre, String paterno, String materno, boolean eliminado, boolean activo) {
        this.nombres=nombre;
        this.apellidoPaterno=paterno;
        this.apellidoMaterno=materno;
        this.eliminado=eliminado;
        this.activo=activo;
    }
    
    public AlumnoGuardarDTO( String nombre, String paterno, String materno, boolean activo) {
        this.nombres=nombre;
        this.apellidoPaterno=paterno;
        this.apellidoMaterno=materno;
        this.eliminado=eliminado;
        this.activo=activo;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlumnoGuardarDTO that = (AlumnoGuardarDTO) o;
        return nombres.equals(that.nombres) &&
               apellidoPaterno.equals(that.apellidoPaterno) &&
               apellidoMaterno.equals(that.apellidoMaterno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombres, apellidoPaterno, apellidoMaterno);
    }
    
}
