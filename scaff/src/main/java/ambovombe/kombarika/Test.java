package ambovombe.kombarika;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import ambovombe.kombarika.database.DbConnection;
import ambovombe.kombarika.generator.CodeGenerator;
import ambovombe.kombarika.generator.IonicProjectCreator;
import ambovombe.kombarika.generator.service.DbService;
import ambovombe.kombarika.generator.service.controller.Controller;
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
        String path = args[0];
//        String path = "./";
        String framework = "csharp:dotnet";
        String packageName = args[1];
        String entity = "entity";
        String controller = "controller";
        String repository = "repository";
        String view = "viewTest";
        String viewType = "angular-ionic";
        String viewPath=args[3];
        String url = args[4];
        System.out.println(args[8]);

        String role=args[8];
        String separator = File.separator;
//        String confFile = Misc.getConnectionConfLocation() + separator + "database.json";

//        String confDatabaseJson=IonicProjectCreator.readFileToString(confFile);
        DbConnection.database=Test.setupDatabase(DbConnection.database,args[5],args[6],args[7]);
//        Controller.getTableName().forEach(System.out::println);

//        code jeddy
//        IonicProjectCreator.clearFileContent(confFile);
//        IonicProjectCreator.writeToFile(confFile,confDatabaseJson);
//        code jeddy fin
        CodeGenerator codeGenerator = new CodeGenerator();

        try{

            String[] tables = DbService.getAllTablesArrays(codeGenerator.getDbConnection());
            for (int i = 0; i < tables.length; i++) {
                System.out.println(tables[i]);
            }
            codeGenerator.generateAll(path,viewPath, packageName, entity, controller, repository, view, viewType, url, tables, framework,role);

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
