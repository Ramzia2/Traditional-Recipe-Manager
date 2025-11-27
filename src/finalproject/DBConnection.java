/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author YIJIA
 */
package finalproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DBConnection {

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn != null) {
            return conn;
        }

        try {
            // Load properties file from config folder
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("config/db_config.properties");
            props.load(fis);

            String username = props.getProperty("username");
            String password = props.getProperty("password");
            String url = props.getProperty("url");

            conn = DriverManager.getConnection(url, username, password);
            return conn;

        } catch (IOException e) {
            System.out.println("Unable to load db_config.properties: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }

        return null;
    }
}
