package ambovombe.kombarika;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import ambovombe.kombarika.generator.CodeGenerator;
import ambovombe.kombarika.generator.IonicProjectCreator;
import ambovombe.kombarika.generator.service.DbService;
import ambovombe.kombarika.utils.Misc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.HashMap;

public class Test {

    /**
     * @param args the command line arguments
     * @throws SQLException
     */

    public static void main(String[] args) throws Exception {
        String path = "/Users/priscafehiarisoadama/RiderProjects/WebApplication5/WebApplication5/";
//        String path = "./";
        String framework = "csharp:dotnet";
        String packageName = "com.district.test";
        String entity = "entity";
        String controller = "controller";
        String repository = "repository";
        String view = "viewTest";
        String viewType = "angular-ionic";

//        String viewPath="/Users/priscafehiarisoadama/IdeaProjects/learnIonicAngular/reciclica-app";
//        String viewPath="./";
        String viewPath="/Users/priscafehiarisoadama/IdeaProjects/learnIonicAngular/reciclica-app";
        String url = "http://localhost:8080/";
        String separator = File.separator;
        String confFile = Misc.getConnectionConfLocation() + separator + "database.json";
        System.out.println("conffile:"+confFile);
        String smt = "{\n" +
                "    \"defaultConnection\" : \"DefaultConnection\",\n" +
                "    \"listConnection\": {\n" +
                "        \"DefaultConnection\": {\n" +
                "            \"datasource\":\"jdbc:postgresql://localhost:5432/"+args[7]+"\",\n" +
                "            \"username\":\""+args[5]+"\",\n" +
                "            \"password\":\""+args[6]+" \",\n" +
                "            \"databaseType\":\"POSTGRESQL\"\n" +
                "        },\n" +
                "        \"OtherConnection\": {\n" +
                "            \"datasource\":\"jdbc:postgresql://localhost:5432/"+args[7]+"\",\n" +
                "            \"username\":\""+args[5]+"\",\n" +
                "            \"password\":\""+args[6]+"\",\n" +
                "            \"databaseType\":\"POSTGRESQL\"\n" +
                "        }\n" +
                "    }\n" +
                "}\n";


//        code jeddy
        IonicProjectCreator.clearFileContent(confFile);
        IonicProjectCreator.writeToFile(confFile,smt);
//        code jeddy fin
        CodeGenerator codeGenerator = new CodeGenerator();

        try{
            // String[] tables = {"district","region"};
            // DbConnection dbConnection = codeGenerator.getDbConnection();
            // String str = dbConnection.getListConnection().get(dbConnection.getInUseConnection()).getDatabaseType().getForeignKeyQuery();
            // str = str.replace("?", "commune");
            // System.out.println(str);
            // HashMap<String, String> foreignKeys = DbService.getForeignKeys(dbConnection, "commune");
            // for (Map.Entry<String, String> set : foreignKeys.entrySet()) {
            //     System.out.println(set.getKey() + " " + set.getValue());
            // }
            String[] tables = DbService.getAllTablesArrays(codeGenerator.getDbConnection());
//             for(String table: tables)
//                 System.out.println(table);

            codeGenerator.generateAll(path,viewPath, packageName, entity, controller, repository, view, viewType, url, tables, framework);

            // codeGenerator.generateEntity(path, "car", packageName+".entity", framework);

//            System.out.println(codeGenerator.getFrameworkProperties().getAuthentificationTemplate());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            codeGenerator.getDbConnection().close();
        }
    }
}
