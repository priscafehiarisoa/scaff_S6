package ambovombe.kombarika.generator.service.controller;

import ambovombe.kombarika.configuration.mapping.LanguageProperties;
import ambovombe.kombarika.configuration.mapping.*;
import ambovombe.kombarika.database.DbConnection;
import ambovombe.kombarika.generator.CodeGenerator;
import ambovombe.kombarika.generator.service.GeneratorService;
import ambovombe.kombarika.generator.utils.ObjectUtility;
import ambovombe.kombarika.utils.Misc;
import lombok.Getter;
import lombok.Setter;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Getter @Setter
public class Controller{
    LanguageProperties languageProperties;
    CrudMethod crudMethod;
    ControllerProperty controllerProperty;
    AnnotationProperty annotationProperty;
    Imports imports;

    /**
     * Generate the function that make the insert to the database
     * @param table
     * @param language
     * @param method
     * @param controllerProperty
     * @return the string form of the function
     */
    public String save(String table){
        String body = "";
        String args = "";
        args += this.getLanguageProperties().getAnnotationSyntax().replace("?", this.getControllerProperty().getAnnotationArgumentParameterFormData()) + " "
                + ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table)) + " "
                + ObjectUtility.formatToCamelCase(table);

        body += Misc.tabulate(this.getCrudMethod().getSave()
            .replace("#object#", ObjectUtility.formatToCamelCase(table))
            .replace("?", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table))));
        String function =  this.getLanguageProperties().getMethodSyntax()
                .replace("#name#", "save")
                .replace("#type#", this.getControllerProperty().getReturnType().replace("?", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table))))
                .replace("#arg#", args)
                .replace("#body#", body);
        return Misc.tabulate(this.getLanguageProperties().getAnnotationSyntax().replace("?", this.getControllerProperty().getPost()) + "\n" + function);
    }

    public String update(String table) throws Exception{
        String body = "";
        String args = "";
        args += this.getLanguageProperties().getAnnotationSyntax().replace("?", this.getControllerProperty().getAnnotationArgumentParameterFormData()) + " "
                + ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table)) + " "
                + ObjectUtility.formatToCamelCase(table);
        body += Misc.tabulate(this.getCrudMethod().getUpdate()
            .replace("#object#", ObjectUtility.formatToCamelCase(table))
            .replace("?", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table))));
        System.out.println("--upd--"+this.getCrudMethod().getUpdate());
        System.out.println("**==**==");
        System.out.println(args);
        String arg=args.split(" ")[2];
        String state="\n_context."+args.split(" ")[1]+".Update("+arg+"); \n";
        String function =  this.getLanguageProperties().getMethodSyntax()
                .replace("#name#", "update")
                .replace("#type#", "async Task<"+ this.getControllerProperty().getReturnType().replace("?", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table)))+">")
                .replace("#arg#", args)
                .replace("#body#", state+ body);
        System.out.println("=func upd="+function);

        return Misc.tabulate(this.getLanguageProperties().getAnnotationSyntax().replace("?", this.getControllerProperty().getPut()) + "\n" + function);
    }

    public static String getPrimaryKeyType(DbConnection dbConnection,String table){
        try {
            Statement statement = dbConnection.connect().createStatement();

            // Replace "your_table" with the name of your table
            String tableName = table;
            ResultSet resultSet = statement.executeQuery("SELECT column_name, data_type FROM information_schema.columns WHERE table_name = '" + tableName + "' AND column_name IN (SELECT column_name FROM information_schema.key_column_usage WHERE table_name = '" + tableName + "' AND constraint_name = (SELECT constraint_name FROM information_schema.table_constraints WHERE table_name = '" + tableName + "' AND constraint_type = 'PRIMARY KEY'))");
            String dataType ="";
            // Iterate through the primary key columns
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                dataType =resultSet.getString("DATA_TYPE");

                System.out.println("Primary key column: " + columnName + ", Data type: " + dataType);

            }
            dbConnection.close();
            return dataType;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String conv(String val){
        if(val.equals("character varying")){
            return "string";
        }if(val.equals("integer")){
            return "int";
        }
        return val;
    }
    public String delete(String table) throws Exception{
        // get the primary key type
        CodeGenerator codeGenerator=new CodeGenerator();
        DbConnection dbConnection=codeGenerator.getDbConnection();
        String primKey= conv(getPrimaryKeyType(dbConnection,table));
        System.out.println("123"+primKey);
        String body = "";
        String args = "";
        args += this.getLanguageProperties().getAnnotationSyntax().replace("?", this.getControllerProperty().getAnnotationArgumentParameterFormData()) + " "
                + ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table)) + " "
                + ObjectUtility.formatToCamelCase(table);
        body += Misc.tabulate(this.getCrudMethod().getDelete()
                .replace("#object#", ObjectUtility.formatToCamelCase(table))
                .replace("?", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table))));
//        body+="\n \t return Ok();";
        System.out.println("--del--"+ args);
//        String state="\n_context."+args.split(" ")[1]+".Find("+args.split(" ")[2]+"); \n";


        String function =  this.getLanguageProperties().getMethodSyntax()
                .replace("#name#", "delete")
                .replace("#type#", this.getControllerProperty().getReturnType().replace("?", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table))))
                .replace("#arg#", primKey + " id")
                .replace("#argument#", args.split(" ")[2])
                .replace("#body#", body);
        System.out.println("=func="+function);
        return Misc.tabulate(this.getLanguageProperties().getAnnotationSyntax().replace("?", this.getControllerProperty().getDelete()) + "\n" + function);
    }

    public String findAll(String table){
        String body = "";
        body += Misc.tabulate(this.getCrudMethod().getFindAll()
            .replace("#object#", ObjectUtility.formatToCamelCase(table))
            .replace("?", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table))));
        String function =  this.getLanguageProperties().getMethodSyntax()
                .replace("#name#", "findAll")
                .replace("#type#", this.getControllerProperty().getReturnType().replace("?", this.getLanguageProperties().getListSyntax().replace("?",ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table)))))
                .replace("#arg#", "")
                .replace("#body#", body);
        return Misc.tabulate(this.getLanguageProperties().getAnnotationSyntax().replace("?", this.getControllerProperty().getGet()) + "\n" + function);
    }



    public String findById(String table) throws Exception{
        String res = "";
        return res;
    }
    public String getCrudMethods(String table) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        String save = save(table);
        String findAll = findAll(table);
        String update = update(table);
        String delete = delete(table);
        stringBuilder.append(save);
        stringBuilder.append("\n");
        stringBuilder.append(update);
        stringBuilder.append("\n");
        stringBuilder.append(delete);
        stringBuilder.append("\n");
        stringBuilder.append(findAll);

        return stringBuilder.toString();
    }

    public String getControllerField(String table){
        String res = "";
        if(!this.getControllerProperty().getField().equals("") && !this.getControllerProperty().getAnnotationField().equals("")){
            res += "\t"
                    + this.getLanguageProperties().getAnnotationSyntax().replace("?", this.getControllerProperty().getAnnotationField()) + "\n"
                    + "\t" + this.getControllerProperty().getField().replace("?", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table))) + "\n";
        }else if (!this.getControllerProperty().getField().equals("")){
            res += "\t" + this.getControllerProperty().getField().replace("?", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table))) + "\n";
        }
        return res;
    }


    public String getControllerClass(String table, String framework){
        String res = "";
        res += this.getLanguageProperties().getAnnotationSyntax()
                .replace("?", this.getAnnotationProperty().getController()) + "\n"
                + this.getLanguageProperties().getAnnotationSyntax()
                .replace("?", this.getControllerProperty().getPath())
                .replace("?", ObjectUtility.formatToCamelCase(table)) + "\n"
                + this.getLanguageProperties().getClassSyntax() + " "
                + ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(
                    this.getLanguageProperties().getFrameworks().get(framework).getControllerProperty().getClassSyntax()).replace("?", ObjectUtility.formatToCamelCase(table))
                );

        return res;
    }

    public String getControllerImport(String repository, String entity, String table) throws Exception{
        String res = "";
        String imp = this.getLanguageProperties().getImportSyntax();
        for(String item : this.getImports().getController()){
            item = item
            .replace("packageName", repository)
            .replace("className", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table)))
            .replace("entity", entity);
            res += imp+ " " + item + "" + this.getLanguageProperties().getEndOfInstruction() + "\n";
        }
        return res;
    }

    public String getConstructor(String table) throws Exception{
        String res = "";
        if(!this.getControllerProperty().getConstructor().equals("")){
            res = this.getControllerProperty().getConstructor()
                .replace("#name#", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table)));
        }
        return res;
    }

    public String generateController(String template, String table, String packageName, String repository, String entity, String framework) throws Exception {
        String res = template.replace("#package#", GeneratorService.getPackage(this.getLanguageProperties(), packageName))
                .replace("#imports#", getControllerImport(repository, entity, table))
                .replace("#class#", getControllerClass(table, framework))
                .replace("#open-bracket#", languageProperties.getOpenBracket())
                .replace("#close-bracket#", languageProperties.getCloseBracket())
                .replace("#fields#", getControllerField(table))
                .replace("#constructors#", getConstructor(table))
                .replace("#methods#", getCrudMethods(table))
                .replace("#encapsulation#", "");
        return res;
    }
}
