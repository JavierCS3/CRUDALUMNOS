/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import dtos.AlumnoEliminarDTO;
import dtos.AlumnoGuardarDTO;
import dtos.AlumnoLecturaDTO;
import entidad.AlumnoEntidad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import presentacion.frmEjercicio;

/**
 *
 * @author PC Gamer
 */
public class AlumnoDAO implements IAlumnoDAO{

    private IConexionBD conexionBD;
    private frmEjercicio frm;
    public AlumnoDAO(IConexionBD conexionBD){
        this.conexionBD=conexionBD;
    }
    
    
    @Override
    public List<AlumnoEntidad> buscarAlumnosTabla() throws PersistenciaException {
        try{
            List<AlumnoEntidad> alumnosLista=null;
            Connection conexion=this.conexionBD.crearConexion();
            String codigoSQL="SELECT idAlumno, nombres, apellidoPaterno, apellidoMaterno, eliminado, activo FROM alumnos";
            Statement comandoSQL = conexion.createStatement();
            ResultSet resultado=comandoSQL.executeQuery(codigoSQL);
            while(resultado.next()){
                if(alumnosLista==null){
                    alumnosLista=new ArrayList<>();
                }
                AlumnoEntidad alumno=this.convertirAEntidad(resultado);
                alumnosLista.add(alumno);
            }
            conexion.close();
            return alumnosLista;
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
            throw new PersistenciaException("Ocurrio un error al leer la base de datos, intentelo de nuevo y si el error persiste");
        }
    }
    
    private AlumnoEntidad convertirAEntidad(ResultSet resultado) throws SQLException{
        int id=resultado.getInt("idAlumno");
        String nombre=resultado.getString("nombres");
        String paterno=resultado.getString("apellidoPaterno");
        String materno=resultado.getString("apellidoMaterno");
        boolean eliminado=resultado.getBoolean("eliminado");
        boolean activo=resultado.getBoolean("activo");
        return new AlumnoEntidad(id,nombre,paterno,materno,eliminado,activo);
    }

    @Override
    public AlumnoGuardarDTO insertarAlumno(AlumnoGuardarDTO alumno) throws PersistenciaException {
            Connection conexion = null;
        try {
            conexion = this.conexionBD.crearConexion();
            conexion.setAutoCommit(false);

            String codigoSQL = "SELECT idAlumno, nombres, apellidoPaterno, apellidoMaterno, eliminado, activo FROM alumnos WHERE nombres = ? AND apellidoPaterno = ? AND apellidoMaterno = ?";
            PreparedStatement comandoSQL = conexion.prepareStatement(codigoSQL);
            comandoSQL.setString(1, alumno.getNombres());
            comandoSQL.setString(2, alumno.getApellidoPaterno());
            comandoSQL.setString(3, alumno.getApellidoMaterno());
            ResultSet resultado = comandoSQL.executeQuery();

            while (resultado.next()) {
            AlumnoEntidad alumnoExistente = this.convertirAEntidad(resultado);
            if (alumno.equals(alumnoExistente)) {
                conexion.rollback();
                throw new PersistenciaException("Usuario no valido");
            }
            }

            codigoSQL = "INSERT INTO alumnos (nombres, apellidoPaterno, apellidoMaterno, activo) VALUES (?, ?, ?, ?)";
            PreparedStatement insertCommand = conexion.prepareStatement(codigoSQL, Statement.RETURN_GENERATED_KEYS);
            insertCommand.setString(1, alumno.getNombres());
            insertCommand.setString(2, alumno.getApellidoPaterno());
            insertCommand.setString(3, alumno.getApellidoMaterno());
            insertCommand.setBoolean(4, alumno.isActivo());
            insertCommand.executeUpdate();

            ResultSet generatedKeys = insertCommand.getGeneratedKeys();
            if (generatedKeys.next()) {
                alumno.setIdAlumno(generatedKeys.getInt(1));
            }
  
            conexion.commit();
            } catch (SQLException ex) {
                
                if (conexion != null) {
                    try {
                        conexion.rollback();
                    } catch (SQLException rollbackEx) {
                        System.out.println("Error al revertir la transacción: " + rollbackEx.getMessage());
                    }
                }
                
                System.out.println(ex.getMessage());
                throw new PersistenciaException("Ocurrio un error al leer la base de datos, intentelo de nuevo y si el error persiste");
                
            }
        
                if (conexion != null) {
                    try {
                        conexion.setAutoCommit(true);
                        conexion.close();
                    } catch (SQLException e) {
                        System.out.println("Error al cerrar la conexión: " + e.getMessage());
                    }
                }
            return alumno;
            }

    
    @Override
    public AlumnoLecturaDTO editarAlumno(AlumnoLecturaDTO alumno) throws PersistenciaException {
        try (Connection conexion = this.conexionBD.crearConexion();
         PreparedStatement preparedStatement = conexion.prepareStatement("UPDATE alumnos SET nombres = ?, apellidoPaterno = ?, apellidoMaterno = ?, activo=? WHERE idAlumno = ?")) {

        preparedStatement.setString(1, alumno.getNombres());
        preparedStatement.setString(2, alumno.getApellidoPaterno());
        preparedStatement.setString(3, alumno.getApellidoMaterno());
        preparedStatement.setBoolean(4, alumno.isActivo());
        preparedStatement.setInt(5, alumno.getIdAlumno());

        int filasAfectadas = preparedStatement.executeUpdate();

        if (filasAfectadas == 0) {
            throw new PersistenciaException("No se encontró el alumno con el ID especificado");
        }

        return alumno;
    } catch (SQLException ex) {
        // Log the exception
        throw new PersistenciaException("Error al actualizar el alumno: " + ex.getMessage());
    }
    }

    @Override
    public AlumnoLecturaDTO obtenerAlumnoPorId(int idAlumno) throws PersistenciaException {
    try {
        Connection conexion = this.conexionBD.crearConexion();
        String codigoSQL = "SELECT idAlumno, nombres, apellidoPaterno, apellidoMaterno, activo FROM alumnos WHERE idAlumno = ?";
        PreparedStatement preparedStatement = conexion.prepareStatement(codigoSQL);
        
        preparedStatement.setInt(1, idAlumno);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return new AlumnoLecturaDTO(
                resultSet.getInt("idAlumno"),
                resultSet.getString("nombres"),
                resultSet.getString("apellidoPaterno"),
                resultSet.getString("apellidoMaterno"),
                resultSet.getBoolean("activo")
            );
        } else {
            return null;
        }
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
        throw new PersistenciaException("Error al obtener el alumno");
    }
    }

    @Override
    public AlumnoEliminarDTO eliminarAlumnoPorId(int idAlumno) throws PersistenciaException {
        try {
        Connection conexion = this.conexionBD.crearConexion();
        String codigoSQL = "DELETE FROM alumnos WHERE idAlumno = ?";
        PreparedStatement preparedStatement = conexion.prepareStatement(codigoSQL);

        preparedStatement.setInt(1, idAlumno);

        int filasAfectadas = preparedStatement.executeUpdate();

        preparedStatement.close();
        conexion.close();

        if (filasAfectadas == 0) {
            throw new PersistenciaException("No se encontró el alumno con el ID especificado");
        }

        AlumnoEliminarDTO alumnoEliminado = new AlumnoEliminarDTO();
        alumnoEliminado.setIdAlumno(idAlumno);
        alumnoEliminado.setEliminado(true);

        return alumnoEliminado;
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
        throw new PersistenciaException("Error al eliminar el alumno");
    }    
    }
    
}
