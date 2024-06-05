/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author PC Gamer
 */
public class ConexionBD implements IConexionBD {
    
    final String SERVER = "localhost"; // Servidor de la base de datos
    final String BASE_DATOS = "ejercicio1"; // Nombre de la base de datos
    final String CADENA_CONEXION = "jdbc:mysql://" + SERVER + "/" + BASE_DATOS; // URL de conexión a la base de datos
    final String USUARIO = "root"; // Usuario de la base de datos
    final String CONTRASEÑA ="" ;
    
    @Override
    public Connection crearConexion() throws SQLException {
        Connection conexion = DriverManager.getConnection(CADENA_CONEXION, USUARIO, CONTRASEÑA);
        return conexion;   
    }
}
