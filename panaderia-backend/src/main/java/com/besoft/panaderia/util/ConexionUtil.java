package com.besoft.panaderia.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class ConexionUtil {

	private Connection conexion = null;
	private String url = "";
	Properties properties = new Properties();
	
	public ConexionUtil() {
	}

	public Connection getConexion() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			url = "jdbc:oracle:thin:@192.168.8.115:1521:orcl";
			conexion = DriverManager.getConnection(url, "ppos", "1234");
			System.out.println("Conexion a Base de Datos " + url + " . . . . .Ok");
		} catch (SQLException | ClassNotFoundException ex) {
			System.out.println(ex);
		}
		return conexion;
	}

	public Connection cerrarConexion() {
		try {
			conexion.close();
			System.out.println("Cerrando conexion a " + url + " . . . . . Ok");
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		conexion = null;
		return conexion;
	}
}
