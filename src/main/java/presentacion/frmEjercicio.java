/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentacion;

import dtos.AlumnoLecturaDTO;
import dtos.AlumnoTablaDTO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import negocio.IAlumnoNegocio;
import negocio.NegocioException;
import utilerias.JButtonCellEditor;
import utilerias.JButtonRenderer;

/**
 *
 * @author PC Gamer
 */
public class frmEjercicio extends javax.swing.JFrame {
    
    private int pagina=1;
    private int LIMITE=1;
    private IAlumnoNegocio alumnoNegocio;
    private int code=0;
    
    
    /**
     * Creates new form frmEjercicio
     */
    public frmEjercicio(IAlumnoNegocio alumnoNegocio) {
       initComponents();

        this.alumnoNegocio = alumnoNegocio;
        this.cargarMetodosIniciales();
        BotonCancelar.setVisible(false);
        BotonAceptar.setVisible(false);
    }

    
    
    private int getIdSeleccionadoTablaAlumnos() {
        int indiceFilaSeleccionada = this.tblAlumnos.getSelectedRow();
        if (indiceFilaSeleccionada != -1) {
            DefaultTableModel modelo = (DefaultTableModel) this.tblAlumnos.getModel();
            int indiceColumnaId = 0;
            int idSocioSeleccionado = (int) modelo.getValueAt(indiceFilaSeleccionada,
                    indiceColumnaId);
            return idSocioSeleccionado;
        } else {
            return 0;
        }
    }
    
    private void cargarMetodosIniciales() {
        this.cargarConfiguracionInicialTablaAlumnos();
        this.cargarAlumnosEnTabla();
    }
    
    private void cargarConfiguracionInicialPantalla(){
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    private void cargarConfiguracionInicialTablaAlumnos() {
        ActionListener onEditarClickListener = new ActionListener() {
            final int columnaId = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                //Metodo para editar un alumno
                editar();
            }
        };
        int indiceColumnaEditar = 5;
        TableColumnModel modeloColumnas = this.tblAlumnos.getColumnModel();
        modeloColumnas.getColumn(indiceColumnaEditar)
                .setCellRenderer(new JButtonRenderer("Editar"));
        modeloColumnas.getColumn(indiceColumnaEditar)
                .setCellEditor(new JButtonCellEditor("Editar",
                        onEditarClickListener));

        ActionListener onEliminarClickListener = new ActionListener() {
            final int columnaId = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                //Metodo para eliminar un alumno
                eliminar();
            }
        };
        int indiceColumnaEliminar = 6;
        modeloColumnas = this.tblAlumnos.getColumnModel();
        modeloColumnas.getColumn(indiceColumnaEliminar)
                .setCellRenderer(new JButtonRenderer("Eliminar"));
        modeloColumnas.getColumn(indiceColumnaEliminar)
                .setCellEditor(new JButtonCellEditor("Eliminar",
                        onEliminarClickListener));
    }


    //ERROR EN EDITAR
    private void editar(){
    int id = this.getIdSeleccionadoTablaAlumnos(); 
    System.out.println("ID seleccionado: " + id);

    if (id == 0) {
        JOptionPane.showMessageDialog(this, "No se ha seleccionado ningún alumno.", "Error", JOptionPane.ERROR_MESSAGE);
        return; 
    }

    try {
        AlumnoLecturaDTO alumno = alumnoNegocio.obtenerAlumnoPorId(id);

        if (alumno == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el alumno con el ID especificado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TextoNombres.setText(alumno.getNombres());
        TextoApellidoPaterno.setText(alumno.getApellidoPaterno());
        TextoApellidoMaterno1.setText(alumno.getApellidoMaterno());
        Activo.setState(alumno.isActivo());

        code = 2; 
        BotonCancelar.setEnabled(true);
        BotonAceptar.setEnabled(true);
        BotonCancelar.setVisible(true);
        BotonAceptar.setVisible(true);
    } catch (NegocioException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace(); 
    }
    }
    
    
    
    
    private void eliminar() {
    try {
        int idAlumno = this.getIdSeleccionadoTablaAlumnos();
        
        alumnoNegocio.eliminarAlumnoPorId(idAlumno);
        
        System.out.println("Alumno con ID " + idAlumno + " eliminado correctamente.");
        this.cargarConfiguracionInicialTablaAlumnos();
        this.cargarAlumnosEnTabla();
    } catch (NegocioException ex) {
        System.out.println("Error al eliminar el alumno: " + ex.getMessage());
        ex.printStackTrace(); 
    }
    }
    
    
    private void llenarTablaAlumnos(List<AlumnoTablaDTO> alumnosLista) {
        DefaultTableModel modeloTabla = (DefaultTableModel) this.tblAlumnos.getModel();

        if (modeloTabla.getRowCount() > 0) {
            for (int i = modeloTabla.getRowCount() - 1; i > -1; i--) {
                modeloTabla.removeRow(i);
            }
        }

        if (alumnosLista != null) {
            alumnosLista.forEach(row -> {
                Object[] fila = new Object[5];
                fila[0] = row.getIdAlumno();
                fila[1] = row.getNombres();
                fila[2] = row.getApellidoPaterno();
                fila[3] = row.getApellidoMaterno();
                fila[4] = row.getEstatus();

                modeloTabla.addRow(fila);
            });
        }
    }
     
    private void cargarAlumnosEnTabla() {
    try {
        int indiceInicio = (pagina - 1) * LIMITE;
        List<AlumnoTablaDTO> todosLosAlumnos = alumnoNegocio.buscarAlumnosTabla();
        int indiceFin = Math.min(indiceInicio + LIMITE, todosLosAlumnos.size());

        List<AlumnoTablaDTO> alumnosPagina = obtenerAlumnosPagina(indiceInicio, indiceFin);

        llenarTablaAlumnos(alumnosPagina);

        actualizarNumeroDePagina();
    } catch (NegocioException ex) {
        ex.printStackTrace();
    }
    }
    
        private List<AlumnoTablaDTO> obtenerAlumnosPagina(int indiceInicio, int indiceFin) {
            try {
        List<AlumnoTablaDTO> todosLosAlumnos = alumnoNegocio.buscarAlumnosTabla();
        List<AlumnoTablaDTO> alumnosPagina = new ArrayList<>();

        indiceFin = Math.min(indiceFin, todosLosAlumnos.size());

        for (int i = indiceInicio; i < indiceFin; i++) {
            alumnosPagina.add(todosLosAlumnos.get(i));
        }
        
        return alumnosPagina;
            } catch (NegocioException ex) {
 
                ex.printStackTrace();
                return Collections.emptyList();
            }
        }
        
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BotonAtras = new javax.swing.JButton();
        BotonSiguiente = new javax.swing.JButton();
        NumeroDePagina = new javax.swing.JTextField();
        BotonAgregarAlumno = new javax.swing.JButton();
        Activo = new java.awt.Checkbox();
        TextoApellidoMaterno = new javax.swing.JScrollPane();
        TextoApellidoMaterno1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        TextoApellidoPaterno = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        TextoNombres = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblAlumnos = new javax.swing.JTable();
        BotonCancelar = new javax.swing.JButton();
        BotonAceptar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ADMINISTRADOR DE ALUMNOS");
        setBackground(new java.awt.Color(0, 0, 0));

        BotonAtras.setText("Atrás");
        BotonAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAtrasActionPerformed(evt);
            }
        });

        BotonSiguiente.setText("Siguiente");
        BotonSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonSiguienteActionPerformed(evt);
            }
        });

        NumeroDePagina.setEditable(false);
        NumeroDePagina.setBackground(new java.awt.Color(215, 215, 215));
        NumeroDePagina.setText("Pagina 1");
        NumeroDePagina.setAutoscrolls(false);
        NumeroDePagina.setFocusable(false);
        NumeroDePagina.setRequestFocusEnabled(false);
        NumeroDePagina.setVerifyInputWhenFocusTarget(false);
        NumeroDePagina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NumeroDePaginaActionPerformed(evt);
            }
        });

        BotonAgregarAlumno.setText("Nuevo registro");
        BotonAgregarAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAgregarAlumnoActionPerformed(evt);
            }
        });

        Activo.setLabel("Activo");

        TextoApellidoMaterno.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        TextoApellidoMaterno.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        TextoApellidoMaterno.setPreferredSize(new java.awt.Dimension(222, 86));

        TextoApellidoMaterno1.setColumns(20);
        TextoApellidoMaterno1.setRows(5);
        TextoApellidoMaterno1.setPreferredSize(new java.awt.Dimension(222, 84));
        TextoApellidoMaterno.setViewportView(TextoApellidoMaterno1);

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TextoApellidoPaterno.setColumns(20);
        TextoApellidoPaterno.setRows(5);
        TextoApellidoPaterno.setPreferredSize(new java.awt.Dimension(222, 84));
        jScrollPane3.setViewportView(TextoApellidoPaterno);

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        TextoNombres.setColumns(20);
        TextoNombres.setRows(5);
        TextoNombres.setPreferredSize(new java.awt.Dimension(222, 84));
        jScrollPane4.setViewportView(TextoNombres);

        jLabel1.setText("Nombres");

        jLabel2.setText("Apellido Materno");

        jLabel3.setText("Apellido Paterno");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setText("Administrador de Alumnos");

        tblAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombres", "A.Paterno", "A.Materno", "Estatus", "Editar", "Eliminar"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Byte.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane7.setViewportView(tblAlumnos);

        BotonCancelar.setText("Cancelar");
        BotonCancelar.setEnabled(false);
        BotonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonCancelarActionPerformed(evt);
            }
        });

        BotonAceptar.setText("Aceptar");
        BotonAceptar.setEnabled(false);
        BotonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAceptarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane7)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(BotonAtras)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(NumeroDePagina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(360, 360, 360)
                                .addComponent(BotonSiguiente))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(6, 6, 6)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(TextoApellidoMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(Activo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(36, 36, 36)
                                            .addComponent(BotonCancelar)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(BotonAceptar))))
                                .addComponent(jLabel4)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(BotonAgregarAlumno)
                        .addGap(6, 6, 6))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BotonAgregarAlumno)
                        .addGap(295, 295, 295)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BotonAtras)
                            .addComponent(NumeroDePagina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BotonSiguiente))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(BotonCancelar)
                                    .addComponent(BotonAceptar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(Activo, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(TextoApellidoMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(43, 43, 43)))
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonSiguienteActionPerformed
        // TODO add your handling code here:
        try {
        List<AlumnoTablaDTO> todosLosAlumnos = alumnoNegocio.buscarAlumnosTabla();

        int totalPaginas = (int) Math.ceil((double) todosLosAlumnos.size() / LIMITE);

        if (pagina < totalPaginas) {
            pagina++;
            cargarAlumnosEnTabla();
            actualizarNumeroDePagina();
        } else {

            JOptionPane.showMessageDialog(this, "No hay más páginas disponibles", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (NegocioException ex) {
        ex.printStackTrace();
    }
    }//GEN-LAST:event_BotonSiguienteActionPerformed

    
    private void NumeroDePaginaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NumeroDePaginaActionPerformed
        // TODO add your handling code here:
        try {
        List<AlumnoTablaDTO> todosLosAlumnos = alumnoNegocio.buscarAlumnosTabla();

        int totalPaginas = (int) Math.ceil((double) todosLosAlumnos.size() / LIMITE);

        int nuevaPagina = Integer.parseInt(NumeroDePagina.getText());

        if (nuevaPagina >= 1 && nuevaPagina <= totalPaginas) {
            pagina = nuevaPagina;

            cargarAlumnosEnTabla();

            actualizarNumeroDePagina();
        } else {
            JOptionPane.showMessageDialog(this, "Número de página inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Ingrese un número válido para la página", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NegocioException ex) {
        ex.printStackTrace();
    }
    }//GEN-LAST:event_NumeroDePaginaActionPerformed

    private void actualizarNumeroDePagina() {
    NumeroDePagina.setText("Página " + pagina);
    }
    
    private void BotonAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAtrasActionPerformed
        // TODO add your handling code here:
        if (pagina > 1) {
        pagina--;
        cargarAlumnosEnTabla();
        actualizarNumeroDePagina();
    }
    }//GEN-LAST:event_BotonAtrasActionPerformed

    private void BotonAgregarAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAgregarAlumnoActionPerformed
        // TODO add your handling code here:
        code=1;
        BotonCancelar.setEnabled(true);
        BotonAceptar.setEnabled(true);
        BotonCancelar.setVisible(true);
        BotonAceptar.setVisible(true);
    }//GEN-LAST:event_BotonAgregarAlumnoActionPerformed

    private void BotonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonCancelarActionPerformed
        // TODO add your handling code here:
       if(code==1){
           TextoNombres.setText("");
           TextoApellidoPaterno.setText("");
           TextoApellidoMaterno1.setText("");
           code=0;
           BotonCancelar.setEnabled(false);
           BotonAceptar.setEnabled(false);
           BotonCancelar.setVisible(false);
           BotonAceptar.setVisible(false);
           Activo.setState(false);
       }else if(code==2){
           BotonCancelar.setEnabled(false);
           BotonAceptar.setEnabled(false);
           BotonCancelar.setVisible(false);
           BotonAceptar.setVisible(false);
           TextoNombres.setText("");
           TextoApellidoPaterno.setText("");
           TextoApellidoMaterno1.setText("");
           code=0;
           this.cargarConfiguracionInicialTablaAlumnos();
           this.cargarAlumnosEnTabla();
       }
    }//GEN-LAST:event_BotonCancelarActionPerformed

    private void BotonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAceptarActionPerformed
        // TODO add your handling code here:
        if(code==1){
            try {
        int id=alumnoNegocio.insertarAlumno(TextoNombres.getText(), TextoApellidoPaterno.getText(), TextoApellidoMaterno1.getText(),Activo.getState()).getIdAlumno();
        JOptionPane.showMessageDialog(this, "id: " + id  +" El alumno " + TextoNombres.getText() + " " + TextoApellidoPaterno.getText() + " ha sido agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        this.cargarConfiguracionInicialTablaAlumnos();
        this.cargarAlumnosEnTabla();
        TextoNombres.setText("");
        TextoApellidoPaterno.setText("");
        TextoApellidoMaterno1.setText("");
        Activo.setState(false);
        BotonCancelar.setEnabled(false);
           BotonAceptar.setEnabled(false);
           BotonCancelar.setVisible(false);
           BotonAceptar.setVisible(false);
           Activo.setState(false);
        }catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.ERROR_MESSAGE);
            
        }
        }else if(code==2){
            try {
            int id = this.getIdSeleccionadoTablaAlumnos();
            alumnoNegocio.editarAlumno(id, TextoNombres.getText(), TextoApellidoPaterno.getText(), TextoApellidoMaterno1.getText(), Activo.getState());
            JOptionPane.showMessageDialog(this, "El alumno " + TextoNombres.getText() + " " + TextoApellidoPaterno.getText() + " ha sido editado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.cargarConfiguracionInicialTablaAlumnos();
            this.cargarAlumnosEnTabla();
            TextoNombres.setText("");
            TextoApellidoPaterno.setText("");
            TextoApellidoMaterno1.setText("");
            Activo.setState(false);
            BotonCancelar.setEnabled(false);
           BotonAceptar.setEnabled(false);
           BotonCancelar.setVisible(false);
           BotonAceptar.setVisible(false);
           Activo.setState(false);
             code = 0;
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.ERROR_MESSAGE);
        }
        }

    }//GEN-LAST:event_BotonAceptarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Checkbox Activo;
    private javax.swing.JButton BotonAceptar;
    private javax.swing.JButton BotonAgregarAlumno;
    private javax.swing.JButton BotonAtras;
    private javax.swing.JButton BotonCancelar;
    private javax.swing.JButton BotonSiguiente;
    private javax.swing.JTextField NumeroDePagina;
    private javax.swing.JScrollPane TextoApellidoMaterno;
    private javax.swing.JTextArea TextoApellidoMaterno1;
    private javax.swing.JTextArea TextoApellidoPaterno;
    private javax.swing.JTextArea TextoNombres;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable tblAlumnos;
    // End of variables declaration//GEN-END:variables


    
}
