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
        String viewPath="/Users/priscafehiarisoadama/IdeaProjects/learnIonicAngular/reciclica-app";
        String url = "http://localhost:8080/";
        String separator = File.separator;
        String confFile = Misc.getConnectionConfLocation() + separator + "database.json";

        String confDatabaseJson=IonicProjectCreator.readFileToString(confFile);
        confDatabaseJson=Test.setupDatabase(confDatabaseJson,args[5],args[6],args[7]);
//        code jeddy
        IonicProjectCreator.clearFileContent(confFile);
        IonicProjectCreator.writeToFile(confFile,confDatabaseJson);
//        code jeddy fin
        CodeGenerator codeGenerator = new CodeGenerator();

        try{

            String[] tables = DbService.getAllTablesArrays(codeGenerator.getDbConnection());
            codeGenerator.generateAll(path,viewPath, packageName, entity, controller, repository, view, viewType, url, tables, framework);

          }catch(Exception e){
            e.printStackTrace();
        }finally{
            codeGenerator.getDbConnection().close();
        }
    }

    public static String setupDatabase(String config,String username,String password,String database){
        return config.replace("#database#",database).replace("#username#",username).replace("#pass#",password);
    }
}
