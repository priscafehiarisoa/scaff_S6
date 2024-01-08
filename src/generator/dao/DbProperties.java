/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package generator.dao;

import generator.parser.FileUtility;
import generator.parser.JsonUtility;
import java.util.List;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import properties.DatabaseType;
/**
 *
 * @author Mamisoa
 */
public class DbProperties {
    String database;
    String datasource;
    String username;
    String password;
    DatabaseType databaseType;

    //SETTERS and GETTERS

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    //CONSTRUCTOR
    public DbProperties(){}
    
    //FUNCTION 
}