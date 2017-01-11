/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.serviceDAO;

import bean.Usuario;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import interfaces.ServiceIF;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author mbernedo
 */
public class ServiceDAO implements ServiceIF {

    MongoClient mongoClient = null;
    DB db = null;
    private final String url = "jdbc:mysql://192.168.130.23:3307/paquetes";

    private Connection getConnectionMySql() {
        Connection con = null;
        Properties prop = new Properties();
        // Cargar las credenciales
        prop.put("user", "root");
        prop.put("password", "root");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, prop);
        } catch (ClassNotFoundException e) {
            System.out.println("===> Revisa tu classpath <===");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("===> Revisa tus parametros de conexion <===");
            e.printStackTrace();
        }
        return con;
    }

    private DB getConnectionMongo() {
        try {
            // OJO: aqui los mongoD no estan con seguridad
            mongoClient = new MongoClient(
                    new MongoClientURI("mongodb://userRegistro:R3masa$1@192.168.120.165:27017/registro"));
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        // USar una Base de datos
        db = mongoClient.getDB("registro");
        return db;
    }

    @Override
    public List<Usuario> getMongo() {
        List<Usuario> usuario = new ArrayList<>();
        Usuario user;
        db = getConnectionMongo();
        boolean rpta = false;
        DBCollection coll = db.getCollection("usuarios");
        // En mongoDb el equivalente de ResultSet es DBCursor
        // Recupear TODA la coleccion
        DBCursor cursor = coll.find();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            user = new Usuario();
            user.setApellido((String) obj.get("apellido"));
            user.setCodigo((String) obj.get("codigo"));
            user.setCompa((String) obj.get("compa"));
            user.setContacto((String) obj.get("contacto"));
            user.setCorreo((String) obj.get("correo"));
            user.setDate_act((Date) obj.get("date_act"));
            user.setDate_reg((Date) obj.get("date_reg"));
            user.setDesc((String) obj.get("desc"));
            user.setDireccion((String) obj.get("direccion"));
            user.setEstado((int) obj.get("estado"));
            user.setIndus((String) obj.get("indus"));
            user.setInfo((String) obj.get("info"));
            user.setNombre((String) obj.get("nombre"));
            user.setPais((String) obj.get("pais"));
            user.setPassword((String) obj.get("password"));
            user.setTelefono((String) obj.get("telefono"));
            user.setTitulo((String) obj.get("titulo"));
            user.setUsuario((String) obj.get("usuario"));
            usuario.add(user);
        }
        return usuario;
    }

    @Override
    public void subirMysql(Usuario usuario) {
        String sql = "INSERT INTO usuario "
                + "(nombre, apellido, usuario, pass, correo, compa, titulo, contacto, descarga, info, telefono, pais, direccion, indus, estado, codigo, date_act, date_reg) "
                + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = getConnectionMySql();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getUsuario());
            stmt.setString(4, usuario.getPassword());
            stmt.setString(5, usuario.getCorreo());
            stmt.setString(6, usuario.getCompa());
            stmt.setString(7, usuario.getTitulo());
            stmt.setString(8, usuario.getContacto());
            stmt.setString(9, usuario.getDesc());
            stmt.setString(10, usuario.getInfo());
            stmt.setString(11, usuario.getTelefono());
            stmt.setString(12, usuario.getPais());
            stmt.setString(13, usuario.getDireccion());
            stmt.setString(14, usuario.getIndus());
            stmt.setInt(15, usuario.getEstado());
            stmt.setString(16, usuario.getCodigo());
            if (usuario.getDate_act() == null || usuario.getDate_reg() == null) {
                stmt.setDate(17, (java.sql.Date) usuario.getDate_act());
                stmt.setDate(18, (java.sql.Date) usuario.getDate_reg());
            } else {
                stmt.setDate(17, fechas(usuario.getDate_act()));
                stmt.setDate(18, fechas(usuario.getDate_reg()));
            }
            int rc = stmt.executeUpdate();
            System.out.println("Cantidad de registros " + rc);
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("casi");
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    @Override
    public List<Usuario> getUser() {
        List<Usuario> lista = new ArrayList<>();
        Usuario user;
        String sql = "SELECT nombre FROM usuario";
        PreparedStatement stmt = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = getConnectionMySql();
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                user = new Usuario();
                user.setNombre(rs.getString(1));
                lista.add(user);
            }
        } catch (Exception e) {
        }
        return lista;
    }

    public java.sql.Date fechas(Date fecha) throws ParseException {
        java.sql.Date fechasql = new java.sql.Date(fecha.getTime());
        return fechasql;
    }

}
