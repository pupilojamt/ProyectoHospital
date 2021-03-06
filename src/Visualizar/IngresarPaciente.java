/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visualizar;

import ConexionSQL.ConexionSQL;
import java.awt.Color;
import java.sql.Connection;
import java.sql.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.util.Date;

/**
 *
 * @author jamt_
 */
public class IngresarPaciente extends javax.swing.JInternalFrame {

    /**
     * Creates new form IngresarPaciente
    */
    DefaultTableModel model;
    
    
    public IngresarPaciente() {
        initComponents();
        cargarComboFinanciador();
        cargarComboSexo();
        cargar2("");
        bloquear();
        btnActualizar.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
    }

    void cargarComboFinanciador(){
        String SQL = "SELECT nombreFinanciador FROM Financiador";
        try {
            PreparedStatement pst = cn.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery();
            cboFinanciador.addItem("Seleccione una Opcion");
            
            while(rs.next()){
                cboFinanciador.addItem(rs.getString("nombreFinanciador"));
            }
        } catch (Exception e) {
            System.out.println("Error en combo financiador: " + e);
        }
    }
    
    void cargarComboSexo(){
        cboSexo.addItem("M");
        cboSexo.addItem("F");
    }
    
    void limpiar(){
        txtDNI.setText("");
        txtNombre.setText("");
        txtPaterno.setText("");
        txtMaterno.setText("");
        txtHisClinica.setText("");
        txtDireccion.setText("");
        dateNacimiento.setDate(null);
        cboSexo.setSelectedIndex(-1);
        cboFinanciador.setSelectedIndex(0);
        txtDNI.requestFocus();
    }
    
    void bloquear(){
        txtDNI.setEnabled(false);
        txtNombre.setEnabled(false);
        txtPaterno.setEnabled(false);
        txtMaterno.setEnabled(false);
        txtHisClinica.setEnabled(false);
        txtDireccion.setEnabled(false);
        dateNacimiento.setEnabled(false);
        cboSexo.setEnabled(false);
        cboFinanciador.setEnabled(false);
    }
    
    void desbloquear(){
        txtDNI.setEnabled(true);
        txtNombre.setEnabled(true);
        txtPaterno.setEnabled(true);
        txtMaterno.setEnabled(true);
        txtHisClinica.setEnabled(true);
        txtDireccion.setEnabled(true);
        dateNacimiento.setEnabled(true);
        cboSexo.setEnabled(true);
        cboFinanciador.setEnabled(true);
    }
    
    void btnNuevo(){
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(true);
    }
   
    void btnGuardar(){
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);
    }
    
    void btnModificar(){
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);
    }
    
    void btnModificar2(){
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(true);
    }
    
    void btnCancelar(){
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);
    }
    
    void cargar2(String valor){
        
        String mostrar="SELECT dni,nombre,apellidoPaterno,apellidoMaterno,edad,sexo,fechaNacimiento,historiaClinica,nombreFinanciador FROM Paciente INNER JOIN Financiador ON Paciente.idFinanciador=Financiador.idFinanciador WHERE Paciente.dni LIKE '%"+valor+"%' OR Paciente.nombre LIKE '%"+valor+"%'";
        String []titulos={"DNI","Nombre","Apellidos","Edad","Sexo","Fecha de Nacimiento","Historia Clinica","Financiador"};
        String []Registros=new String[8];
        model= new DefaultTableModel(null, titulos);
        String apellido="";
        try {
              Statement st = cn.createStatement();
              ResultSet rs = st.executeQuery(mostrar);
              while(rs.next())
              {
                  Registros[0]= rs.getString("dni");
                  Registros[1]= rs.getString("nombre");
                  apellido = rs.getString("apellidoPaterno") + " "+ rs.getString("apellidoMaterno");
                  Registros[2]= apellido;
                  Registros[3]= rs.getString("edad");
                  Registros[4]= rs.getString("sexo");
                  Registros[5]= rs.getString("fechaNacimiento");
                  Registros[6]= rs.getString("historiaClinica");
                  Registros[7]= rs.getString("nombreFinanciador");       
                  model.addRow(Registros);
              }
              tablaMostrarPaciente.setModel(model);
        } catch (SQLException ex) {
            System.out.println("Error en la tabla paciente: " + ex);
        }
    }
    
    public String detalleDNI(){
        String dni=txtDNI.getText();
        String dniRepetido = null;
            try {
                     String ConsultaSQL="SELECT * FROM Paciente WHERE dni='"+dni+"'";

                     Statement st = cn.createStatement();
                     ResultSet rs = st.executeQuery(ConsultaSQL);                    
                     
                     if(rs.next()){
                       dniRepetido = rs.getString("dni");     
                     } 
                        
             } catch (SQLException ex) {
                 
             }
        return dniRepetido;
    }
    
    boolean vr = false;
    boolean repetidoDNI = false;
    boolean valida(){
        if(txtDNI.getText().equals("") || txtNombre.getText().equals("")
                || txtPaterno.getText().equals("") || txtMaterno.getText().equals("")
                || txtHisClinica.getText().equals("") || txtDNI.getText().equals("")
                || dateNacimiento.getDate()==null || txtDireccion.getText().equals("")
                || cboSexo.getSelectedIndex()==-1 || cboFinanciador.getSelectedIndex()==0){
            return false;
        }
        if( txtDNI.getText().length()<8){
            vr = true;
            return false;
        }
        if(txtDNI.getText().equals(detalleDNI())){
            repetidoDNI = true;
            return false;
        }
        return true;
    }
    
    void ingresar(){
        if (!valida()){
            if(vr==true){
                JOptionPane.showMessageDialog(null, "Campo de DNI imcompleto");
            }else if(repetidoDNI==true){
                JOptionPane.showMessageDialog(null, "El DNI ingresado ya existe.");
            }else{
                JOptionPane.showMessageDialog(null, "Falta ingresar campos.");
            }
            
        }
        else{
            String sql="INSERT INTO Paciente (dni,nombre,apellidoPaterno,apellidoMaterno,edad,sexo,fechaNacimiento,direccion,historiaClinica,idFinanciador) VALUES (?,?,?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement pst  = cn.prepareStatement(sql);
                pst.setString(1, txtDNI.getText());
                pst.setString(2, txtNombre.getText());
                pst.setString(3, txtPaterno.getText());
                pst.setString(4, txtMaterno.getText());

                    int anio = dateNacimiento.getCalendar().get(Calendar.YEAR);
                    int dia = dateNacimiento.getCalendar().get(Calendar.DAY_OF_MONTH);
                    int mes = dateNacimiento.getCalendar().get(Calendar.MARCH)+1;
                    String fecha = anio+"-"+mes+"-"+dia;

                    int anionace = dateNacimiento.getDate().getYear()+1900;
                    int mesanace= dateNacimiento.getDate().getMonth() + 1;
                    int dianace = dateNacimiento.getDate().getDay();

                    Date fechaActual = new Date();
                    int anioactual = fechaActual.getYear()+1900;
                    int mesactual = fechaActual.getMonth()+1;
                    int diaactual = fechaActual.getDate();                       

                    int edad = 0;
                    if(mesactual > mesanace){
                        edad = anioactual - anionace;
                    }
                    else if(mesactual == mesanace){
                        if(diaactual >= dianace){
                            edad = anioactual - anionace;
                        }
                        else{
                            edad = (anioactual - anionace) -1;
                        }
                    }
                    else if(mesactual < mesanace){
                        edad = (anioactual - anionace) -1;
                    }                       

                pst.setInt(5, edad);
                pst.setString(6, cboSexo.getSelectedItem().toString());   
                pst.setString(7,fecha);
                pst.setString(8, txtDireccion.getText());
                pst.setInt(9, Integer.parseInt(txtHisClinica.getText()));
                pst.setInt(10, cboFinanciador.getSelectedIndex());

                int n=pst.executeUpdate();
                if(n>0){
                    JOptionPane.showMessageDialog(null, "Registro Guardado con Exito");
                }

                cargar2("");
                
                limpiar();
                bloquear();
                btnGuardar();
            } catch (SQLException ex) {
                System.out.println("Error al ingresar datos: " + ex);
            }
        }
    }
    
    void modificar1() {
            try {
                String dnii = txtDNI.getText();
                String nombree = txtNombre.getText();
                String paternoo = txtPaterno.getText();
                String maternoo = txtMaterno.getText();
                String generoo = cboSexo.getSelectedItem().toString();
                
                int año = dateNacimiento.getCalendar().get(Calendar.YEAR);
                int dia = dateNacimiento.getCalendar().get(Calendar.DAY_OF_MONTH);
                int mes = dateNacimiento.getCalendar().get(Calendar.MARCH)+1;
                String fecha = año+"-"+mes+"-"+dia;
                
                int anionace = dateNacimiento.getDate().getYear()+1900;
                int mesanace= dateNacimiento.getDate().getMonth() + 1;
                int dianace = dateNacimiento.getDate().getDay();

                Date fechaActual = new Date();
                int anioactual = fechaActual.getYear()+1900;
                int mesactual = fechaActual.getMonth()+1;
                int diaactual = fechaActual.getDate();


                int edad = 0;
                if(mesactual > mesanace){
                    edad = anioactual - anionace;
                }
                else if(mesactual == mesanace){
                    if(diaactual >= dianace){
                        edad = anioactual - anionace;
                    }
                    else{
                        edad = (anioactual - anionace) -1;
                    }
                }
                else if(mesactual < mesanace){
                    edad = (anioactual - anionace) -1;
                }         
                
                String direcionn = txtDireccion.getText();
                int historiall = Integer.parseInt(txtHisClinica.getText());
                int financiador = cboFinanciador.getSelectedIndex();
                
                    String insertar = "UPDATE Paciente SET "
                    +"nombre='"+nombree+"', "
                    +"apellidoPaterno='"+paternoo+"', "
                    +"apellidoMaterno='"+maternoo+"', "
                    +"edad="+edad+", "
                    +"sexo='"+generoo+"', "
                    +"fechaNacimiento='"+fecha+"', "
                    +"direccion='"+direcionn+"', "
                    +"historiaClinica="+historiall+", "
                    +"idFinanciador="+financiador+" "
                    +"WHERE dni='"+dnii+"'";
                    PreparedStatement pst = cn.prepareStatement(insertar);

                    pst.executeUpdate();
                    
                    cargar2("");
            } catch (Exception e) {
                System.out.println("error al cargar los datos: "+e);
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

        jPanel1 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaMostrarPaciente = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtDNI = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtPaterno = new javax.swing.JTextField();
        txtMaterno = new javax.swing.JTextField();
        cboSexo = new javax.swing.JComboBox<>();
        dateNacimiento = new com.toedter.calendar.JDateChooser();
        txtDireccion = new javax.swing.JTextField();
        txtHisClinica = new javax.swing.JTextField();
        cboFinanciador = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        txtBuscarDNI = new javax.swing.JTextField();
        btnMostrarTodos = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Registro de Paciente");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Botones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        btnSalir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSalir.setText("SALIR");
        btnSalir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSalir.setBorderPainted(false);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnActualizar.setBackground(new java.awt.Color(204, 204, 204));
        btnActualizar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnActualizar.setBorderPainted(false);
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnCancelar.setText("CANCELAR");
        btnCancelar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnCancelar.setBorderPainted(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(204, 204, 204));
        btnGuardar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnGuardar.setBorderPainted(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnNuevo.setBackground(new java.awt.Color(204, 204, 204));
        btnNuevo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNuevo.setText("NUEVO");
        btnNuevo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnNuevo.setBorderPainted(false);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tablaMostrarPaciente.setBackground(new java.awt.Color(204, 204, 204));
        tablaMostrarPaciente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tablaMostrarPaciente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaMostrarPaciente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tablaMostrarPaciente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMostrarPacienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaMostrarPaciente);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Detalle Paciente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("DNI:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("NOMBRES:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("APELLIDO PATERNO:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("APELLIDO MATERNO:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("SEXO:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("FEC. NACIMIENTO:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("DIRECCIÓN:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("HISTORIA CLÍNICA:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("FINANCIADOR:");

        txtDNI.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDNI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDNIActionPerformed(evt);
            }
        });
        txtDNI.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDNIKeyTyped(evt);
            }
        });

        txtNombre.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtPaterno.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtMaterno.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cboSexo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        dateNacimiento.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtDireccion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtHisClinica.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtHisClinica.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHisClinicaKeyTyped(evt);
            }
        });

        cboFinanciador.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtHisClinica, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboFinanciador, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDireccion, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cboSexo, 0, 200, Short.MAX_VALUE)
                                .addGap(114, 114, 114)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtNombre))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(txtPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(82, 82, 82))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(cboSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtHisClinica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(cboFinanciador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(dateNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("BUSCAR:");

        txtBuscarDNI.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtBuscarDNI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarDNIActionPerformed(evt);
            }
        });
        txtBuscarDNI.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarDNIKeyReleased(evt);
            }
        });

        btnMostrarTodos.setBackground(new java.awt.Color(204, 204, 204));
        btnMostrarTodos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnMostrarTodos.setText("MOSTRAR TODOS");
        btnMostrarTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarTodosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscarDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMostrarTodos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addGap(4, 4, 4))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtBuscarDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMostrarTodos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtBuscarDNIKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarDNIKeyReleased
        // TODO add your handling code here:
        cargar2(txtBuscarDNI.getText());
    }//GEN-LAST:event_txtBuscarDNIKeyReleased

    private void btnMostrarTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarTodosActionPerformed
        // TODO add your handling code here:
        txtBuscarDNI.setText("");
        cargar2("");
    }//GEN-LAST:event_btnMostrarTodosActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        //System.out.println("hola: " + cboFinanciador.getSelectedIndex());
        ingresar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        limpiar();
        desbloquear();
        btnNuevo();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        modificar1();
        limpiar();
        bloquear();
        btnModificar();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        bloquear();
        limpiar();
        btnCancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void tablaMostrarPacienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMostrarPacienteMouseClicked
        // TODO add your handling code here:
        String dni=(String) tablaMostrarPaciente.getValueAt(tablaMostrarPaciente.getSelectedRow(),0);
        try {
            String ConsultaSQL="SELECT * FROM Paciente WHERE dni='"+dni+"'";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(ConsultaSQL);                    

            if(rs.next()){
                txtDNI.setText(rs.getString("dni"));
                txtNombre.setText(rs.getString("nombre"));
                txtPaterno.setText(rs.getString("apellidoPaterno"));
                txtMaterno.setText(rs.getString("apellidoMaterno"));
                dateNacimiento.setDate(rs.getDate("fechaNacimiento"));
                txtDireccion.setText(rs.getString("direccion"));
                cboSexo.setSelectedItem(rs.getString("sexo"));
                txtHisClinica.setText(rs.getString("historiaClinica"));
                cboFinanciador.setSelectedIndex(rs.getInt("idFinanciador"));
            } 
            desbloquear();
            btnModificar2();

        } catch (Exception e) {
            System.out.println("ERROR seleccionar datos: "+e.getMessage());
        }
    }//GEN-LAST:event_tablaMostrarPacienteMouseClicked

    private void txtBuscarDNIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarDNIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarDNIActionPerformed

    private void txtDNIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDNIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDNIActionPerformed

    private void txtDNIKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDNIKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        
        if(c<'0' || c>'9') evt.consume();
        if (txtDNI.getText().length()== 8) evt.consume(); 
    }//GEN-LAST:event_txtDNIKeyTyped

    private void txtHisClinicaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHisClinicaKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        
        if(c<'0' || c>'9') evt.consume();
        if (txtHisClinica.getText().length()== 5) evt.consume(); 
    }//GEN-LAST:event_txtHisClinicaKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnMostrarTodos;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cboFinanciador;
    private javax.swing.JComboBox<String> cboSexo;
    private com.toedter.calendar.JDateChooser dateNacimiento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaMostrarPaciente;
    private javax.swing.JTextField txtBuscarDNI;
    private javax.swing.JTextField txtDNI;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtHisClinica;
    private javax.swing.JTextField txtMaterno;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPaterno;
    // End of variables declaration//GEN-END:variables
ConexionSQL cc = new ConexionSQL();
Connection cn= ConexionSQL.conexionn();
}
