/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejercicio1;

import negocio.AlumnoNegocio;
import negocio.IAlumnoNegocio;
import persistencia.AlumnoDAO;
import persistencia.ConexionBD;
import persistencia.IAlumnoDAO;
import persistencia.IConexionBD;
import presentacion.frmEjercicio;

/**
 *
 * @author PC Gamer
 */
public class Ejercicio1 {

    public static void main(String[] args) {
        //Capa persistencia
        IConexionBD conexionBD=new ConexionBD();
        IAlumnoDAO alumnoDAO=new AlumnoDAO(conexionBD);
        
        //Capa negocio
        IAlumnoNegocio alumnoNegocio=new AlumnoNegocio(alumnoDAO);
        
        //Capa presentacion
        frmEjercicio frmE=new frmEjercicio(alumnoNegocio);
        frmE.show();
    }
}
