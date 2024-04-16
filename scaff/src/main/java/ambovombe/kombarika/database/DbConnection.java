/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ambovombe.kombarika.database;

import ambovombe.kombarika.Test;
import ambovombe.kombarika.utils.Misc;
import ambovombe.kombarika.generator.parser.JsonUtility;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;

/**
 *
 * @author Mamisoa
 */
public class DbConnection {
    @Getter
    final static String confPath = "database.json";
    @Getter @Setter
    String defaultConnection;
    @Getter
    String inUseConnection;
    @Getter @Setter
    public boolean init = false;
    @Setter
    public Connection connection = null;
    @Getter
    private HashMap<String, DbProperties> listConnection;
    public static String database="{\n" +
            "    \"defaultConnection\" : \"DefaultConnection\",\n" +
            "    \"listConnection\": {\n" +
            "        \"DefaultConnection\": {\n" +
            "            \"datasource\":\"jdbc:postgresql://localhost:5432/#database#\",\n" +
            "            \"username\":\"#username#\",\n" +
            "            \"password\":\"#pass#\",\n" +
            "            \"databaseType\":\"POSTGRESQL\"\n" +
            "        },\n" +
            "        \"OtherConnection\": {\n" +
            "            \"datasource\":\"jdbc:postgresql://localhost:5432/#database#\",\n" +
            "            \"username\":\"#username#\",\n" +
            "            \"password\":\"#pass#\",\n" +
            "            \"databaseType\":\"POSTGRESQL\"\n" +
            "        }\n" +
            "    }\n" +
            "}";
    //SETTERS & GETTERS

    public void setListConnection(HashMap<String, DbProperties> listConnection) {
        this.listConnection = listConnection;
    }

    //METHODS
    public void read()throws Exception{
        String separator = File.separator;

//
        String confFile = Misc.getConnectionConfLocation() + separator + getConfPath();
        DbConnection temp = JsonUtility.parseStringToGson(DbConnection.database, this.getClass());
        this.setListConnection(temp.getListConnection());
        this.setDefaultConnection(temp.getDefaultConnection());
        this.setInUseConnection(temp.getDefaultConnection());
    }

    public void setInUseConnection(String inUseConnection){
        if(getListConnection().get(inUseConnection) != null)
            this.inUseConnection = inUseConnection;
        else throw new IllegalArgumentException("There is no such connection : "+inUseConnection);
    }

    public Connection createConnection(String connection)throws Exception{
        if(!isInit()) init();
        DbProperties prop = this.getListConnection().get(connection);
        System.out.println("connexion"+prop);
        return prop.connect();
    }

    /**
     * connect to the database by changing
     * the connection property
     * @author rakharrs
     * @return
     * @throws Exception
     */
    public Connection connect()throws Exception{
        if(!isInit()) init();
        setConnection(createConnection(getInUseConnection()));
        return getConnection();
    }

    public void init() throws Exception{
        read();
        setInit(true);
    }

    public Connection connect(String connection)throws Exception{
        if(!isInit()) init();
        setConnection(createConnection(connection));
        return getConnection();
    }

    /**
     * Check if the connection property is null or not
     * @return
     * @throws Exception
     */
    public boolean isConnected() throws Exception {
        return getConnection() != null;
    }

    /**
     * get the connection property
     * if it is undefined -> create the property
     * by connecting to the database
     * @author rakharrs
     * @return - connection property
     * @throws Exception
     */
    public Connection getConnection() throws Exception{
        if(this.connection == null) this.connect();
        return this.connection;
    }

    /**
     * @author rakharrs
     */
    public void close() throws Exception {
        getConnection().close();
    }

    /**
     * @author rakharrs
     */
    public void commit() throws Exception {
        getConnection().commit();
    }

    @Override
    public String toString() {
        return "DbConnection{" +
                "defaultConnection='" + defaultConnection + '\'' +
                ", inUseConnection='" + inUseConnection + '\'' +
                ", init=" + init +
                ", connection=" + connection +
                ", listConnection=" + listConnection +
                '}';
    }
}
