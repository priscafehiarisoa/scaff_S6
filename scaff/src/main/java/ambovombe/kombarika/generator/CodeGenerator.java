/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ambovombe.kombarika.generator;

import ambovombe.kombarika.configuration.main.LanguageDetails;
import ambovombe.kombarika.configuration.main.TypeProperties;
import ambovombe.kombarika.configuration.main.ViewDetails;
import ambovombe.kombarika.configuration.mapping.*;
import ambovombe.kombarika.database.DbConnection;
import ambovombe.kombarika.generator.parser.FileUtility;
import ambovombe.kombarika.generator.parser.JsonUtility;
import ambovombe.kombarika.generator.service.DbService;
import ambovombe.kombarika.generator.service.GeneratorService;
import ambovombe.kombarika.generator.service.controller.Controller;
import ambovombe.kombarika.generator.service.entity.Entity;
import ambovombe.kombarika.generator.service.repository.Repository;
import ambovombe.kombarika.generator.service.view.View;
import ambovombe.kombarika.generator.utils.ObjectUtility;
import ambovombe.kombarika.utils.Misc;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.HashMap;
import java.util.List;



@Getter @Setter
public class CodeGenerator {
    DbConnection dbConnection;
    LanguageDetails languageDetails;
    TypeProperties typeProperties;
    ViewDetails viewDetails;
    FrameworkProperties frameworkProperties;
    String [] tables;

    public CodeGenerator() throws Exception {
        this.dbConnection = new DbConnection();
        this.dbConnection.init();
        this.languageDetails = new LanguageDetails();
        this.languageDetails.init();
        this.typeProperties = new TypeProperties();
        this.typeProperties.init();
        this.viewDetails = new ViewDetails();
        this.viewDetails.init();
    }

    public void generateEntity(
        String path,
        String table,
        String packageName,
        String lang)
    throws Exception{
        String[] splittedLang = lang.split(":");
        String language = splittedLang[0];
        String framework = splittedLang[1];
        this.setFrameworkProperties(this.getLanguageDetails().getLanguages().get(language).getFrameworks().get(framework));
        generateEntityFile(path, table, packageName, language, framework);
    }

    public void generateController(
        String path,
        String table,
        String packageName,
        String repository,
        String entity,
        String lang,
        String pagination,
        String role,
        String [] tables
    ) throws Exception{
        String[] splittedLang = lang.split(":");
        String language = splittedLang[0]; String framework = splittedLang[1];
        String controller = buildController(table, packageName, repository, entity, language, framework,pagination,role,tables);
        generateControllerFile(path, table, packageName, language, framework, controller);
    }

    public void generateRepositoryFile(
        String path,
        String table,
        String packageName,
        String language,
        String framework,
        String content
    ) throws Exception{
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        if(languageProperties.getFrameworks().get(framework).getRepository().equals("")) return;
        String directory = packageName.replace(".", File.separator);
        FileUtility.createDirectory(directory, path);
        path = path + File.separator + directory;
        FileUtility.generateFile(path, GeneratorService.getFileName(table+languageProperties.getFrameworks().get(framework).getRepositoryProperty().getName(), languageProperties.getExtension()), content);
    }

    public String buildRepository(
        String table,
        String packageName,
        String entityPackage,
        String language,
        String framework
    ) throws Exception{
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        FrameworkProperties frameworkProperties = languageProperties.getFrameworks().get(framework);
        TypeMapping typeMapping = getTypeProperties().getListProperties().get(language);
        List<String> primaryKeysType = DbService.getPrimaryKeyType(dbConnection, table);
        Repository repository = new Repository();
        repository.setFrameworkProperties(frameworkProperties);
        repository.setLanguageProperties(languageProperties);
        repository.setTypeMapping(typeMapping);
        return repository.generateRepository(table, packageName, entityPackage, primaryKeysType);
    }

    public String buildRepository(
        String[] tables,
        String context,
        String packageName,
        String entityPackage,
        String language,
        String framework
    ) throws Exception{
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        FrameworkProperties frameworkProperties = languageProperties.getFrameworks().get(framework);
        TypeMapping typeMapping = getTypeProperties().getListProperties().get(language);
        List<String> primaryKeysType = DbService.getPrimaryKeyType(dbConnection, tables[0]);
        Repository repository = new Repository();
        repository.setFrameworkProperties(frameworkProperties);
        repository.setLanguageProperties(languageProperties);
        repository.setTypeMapping(typeMapping);
        return repository.generateRepository(tables, context, packageName, entityPackage, primaryKeysType);
    }


    public void generateRepository(
        String path,
        String table,
        String packageName,
        String entityPackage,
        String lang
    ) throws Exception{
        String[] splittedLang = lang.split(":");
        String language = splittedLang[0]; String framework = splittedLang[1];
        String repository = buildRepository(table, packageName, entityPackage, language, framework);
        System.out.println(entityPackage);
        generateRepositoryFile(path, table, packageName, language, framework, repository);
    }

    public void generateRepository(
        String path,
        String[] tables,
        String context,
        String packageName,
        String entityPackage,
        String lang
    ) throws Exception{
        String[] splittedLang = lang.split(":");
        String language = splittedLang[0]; String framework = splittedLang[1];
        String repository = buildRepository(tables, context, packageName, entityPackage, language, framework);
        generateRepositoryFile(path, context, packageName, language, framework, repository);
    }

    public void generateView(
        String path,
        String table,
        // view izy ary ambadika ary
        String directory,
        String viewType,
        String url
    ) throws Exception{
        String view = buildView(table, viewType, url);
        FileUtility.createDirectory(directory,path);
        String fileName = GeneratorService.getFileName(table, this.getViewDetails().getViews().get(viewType).getExtension());

//        cas où c'est un projet ionic :
        if(viewType.equals("angular-ionic")){
            // on va créer le truc de ionic ici
            IonicProjectCreator.addIonicPage(path,table);
            path+= File.separator + "src/app" + File.separator +table;
            // delete what's in the component
            String componentName= table+"."+this.getViewDetails().getViews().get(viewType).getExtension();
            // write the file
            FileUtility.writeFile(path+File.separator+componentName,view);
        }
        else{
            FileUtility.createDirectory(table,path+ File.separator + directory);
            path = path + File.separator + directory+ File.separator +table;

        }
//        FileUtility.createDirectory("test",path);
//        FileUtility.generateFile(path+"/test", fileName, view);

        // eto no amboarina raha angular na ionic
    }

    /**
     * eg : generate -p path -t table1, table2, table3 -package name -l java:spring-boot
     * @author rakharrs
     */
    public String buildEntity(String table, String packageName, String language, String framework) throws Exception {
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        TypeMapping typeMapping = getTypeProperties().getListProperties().get(language);
        FrameworkProperties frameworkProperties = languageProperties.getFrameworks().get(framework);
        String template = frameworkProperties.getTemplate();
        Entity entity = new Entity();
        entity.setAnnotationProperty(frameworkProperties.getAnnotationProperty());
        entity.setLanguageProperties(languageProperties);
        entity.setTypeMapping(typeMapping);
        entity.setImports(frameworkProperties.getImports());
        return entity.generateEntity(getDbConnection(), template, table, packageName);
    }

    public void generateEntityFile(
        String path,
        String table,
        String packageName,
        String language,
        String framework
    ) throws Exception{
        String entity = buildEntity(table, packageName, language, framework);
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        String directory = packageName.replace(".", File.separator);
        FileUtility.createDirectory(directory,path);
        path = path + File.separator + directory;
        FileUtility.generateFile(path, GeneratorService.getFileName(table, languageProperties.getExtension()), entity);
    }

    public String buildController(String table, String packageName, String repository, String entity, String language, String framework,String paginations,String role,String [] tables) throws Exception{
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        FrameworkProperties frameworkProperties = languageProperties.getFrameworks().get(framework);
        String template = frameworkProperties.getTemplate();
        Controller controller = new Controller();
        controller.setAnnotationProperty(frameworkProperties.getAnnotationProperty());
        controller.setControllerProperty(frameworkProperties.getControllerProperty());
        controller.setCrudMethod(frameworkProperties.getCrudMethod());
        controller.setImports(frameworkProperties.getImports());
        controller.setLanguageProperties(languageProperties);
        return controller.generateController(template, table, packageName, repository, entity, framework,paginations,role);
    }

    private void generatePaginationClasses(String path,String packageName,String lang) throws Exception {

        String[] splittedLang = lang.split(":");
        String framework = splittedLang[1];
        String language = splittedLang[0];
        String directory=packageName.replace(".", File.separator)+ File.separator+"pagination";
        String directoryPath=path+directory;
        this.setFrameworkProperties(this.getLanguageDetails().getLanguages().get(language).getFrameworks().get(framework));
        FileUtility.createDirectory(packageName.replace(".", File.separator)+ File.separator+"pagination",path);

        String pageListTemplate="";

            pageListTemplate = getFrameworkProperties().getPageListTemp();
        String pageParamtersTemplate=getFrameworkProperties().getPageParameterTemp();
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        FileUtility.generateFile(directoryPath,GeneratorService.getFileName("PageList",languageProperties.getExtension()),pageListTemplate);
        FileUtility.generateFile(directoryPath,GeneratorService.getFileName("PageParameters",languageProperties.getExtension()),pageParamtersTemplate);
    }

    public void generateAuthentificationClass(String path, String packageName,String lang) throws Exception {

        String[] splittedLang = lang.split(":");
        String framework = splittedLang[1];
        String language = splittedLang[0];
        String directory=packageName.replace(".", File.separator)+ File.separator+"controller";
        String directoryPath=path+directory;
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        this.setFrameworkProperties(this.getLanguageDetails().getLanguages().get(language).getFrameworks().get(framework));
        String template = this.getFrameworkProperties().getAuthentificationTemplate();
        FileUtility.generateFile(directoryPath,GeneratorService.getFileName("AuthController",languageProperties.getExtension()),template);

    }

    public void generateControllerFile(
        String path,
        String table,
        String packageName,
        String language,
        String framework,
        String content
    ) throws Exception{
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        String directory = packageName.replace(".", File.separator);
        FileUtility.createDirectory(directory,path);
        path = path + File.separator + directory;
        FileUtility.generateFile(path, GeneratorService.getFileName(table+"Controller", languageProperties.getExtension()), content);
    }

    public String buildView(String table, String viewType, String url) throws Exception{
        View view = new View();
        view.setViewProperties(this.getViewDetails().getViews().get(viewType));
        return view.generateView(table, url, dbConnection);
    }
    public void generateAllEntity(
        String path,
        String[] tables,
        String packageName,
        String entity,
        String framework
    )  throws Exception{
        for (String table : tables) {
            generateEntity(path, table, packageName + "." + entity, framework);
        }
    }

    public void generateAllController(
        String path,
        String[] tables,
        String packageName,
        String entity,
        String controller,
        String repository,
        String framework,
        String role
    )  throws Exception{
        for (String table : tables) {
            generateController(path, table, packageName + "." + controller, packageName + "." + repository, packageName + "." + "entity", framework,packageName+".paginations",role,tables);
        }
    }

    public void generateAllRepository(
        String path,
        String[] tables,
        String packageName,
        String entity,
        String repository,
        String framework
    )  throws Exception{
        if(this.getFrameworkProperties().isOneRepository()){
            generateRepository(path, tables, ObjectUtility.capitalize(repository), packageName + "." + repository, entity, framework);
        }else{
            for (String table : tables) {
                generateRepository(path, table, packageName + "." + repository, packageName + "." + entity, framework);
            }
        }
    }

    public void generateAllView(
        String path,
        String[] tables,
        String view,
        String viewType,
        String url
    )  throws Exception{

        for (String table : tables) {
            generateView(path, table, view, viewType, url);
        }
    }

    public void generateAll(
        String path,
        String viewPath,
        String packageName,
        String entity,
        String controller,
        String repository,
        String view,
        String viewType,
        String url,
        String[] tables,
        String framework,
        String role
    ) throws Exception{
        generateAllEntity(path, tables, packageName ,entity, framework);
        generateAllRepository(path, tables, packageName , entity, repository, framework);
        generateAllController(path, tables, packageName, entity, controller, repository, framework,role);
        generateAuthentificationClass(path,packageName,framework);
        generateViewService(viewPath, viewType);
        generateAllView(viewPath, tables, view, viewType, url);
        generatePaginationClasses(path,packageName,framework);
        generateUserDTO(path,packageName,framework);
        generateAuthentificationPages(viewPath,viewType);
    }

    private void generateAuthentificationPages(String viewPath,String viewType) throws IOException {
        if(viewType.equals("angular-ionic")){
            String directory="auth";
            String path=viewPath+ File.separator + "src/app"+ File.separator +directory;
            String templateLoginpath=Misc.getViewTemplateLocation()+File.separator+viewType+File.separator+"login.txt";
            String templateRegisterpath=Misc.getViewTemplateLocation()+File.separator+viewType+File.separator+"register.txt";
            //            login
            String page="login";
            String loginTemplate=IonicProjectCreator.readFileToString(templateLoginpath);
            IonicProjectCreator.addIonicPage(viewPath,directory+File.separator+page);
            IonicProjectCreator.writeToFile(path+File.separator+page+File.separator+page+".page.ts",loginTemplate);


//          register
            page="register";
            String registerTemplate=IonicProjectCreator.readFileToString(templateRegisterpath);
            IonicProjectCreator.addIonicPage(viewPath,directory+File.separator+page);
            IonicProjectCreator.writeToFile(path+File.separator+page+File.separator+page+".page.ts",registerTemplate);

        }
    }

    private void generateViewService(String path, String viewType) {
        if(viewType.equals("angular-ionic")){
            String tempPath = Misc.getViewTemplateLocation().concat(File.separator).concat(viewType).concat(File.separator).concat(this.getViewDetails().getViews().get(viewType).getServiceTemplate());

            IonicProjectCreator.generateIonicService(path,this.getViewDetails().getViews().get(viewType).getServiceFileName(),tempPath);
        }
    }
    private void generateUserDTO(String path,String packageName, String lang) throws Exception {
        String[] splittedLang = lang.split(":");
        String framework = splittedLang[1];
        String language = splittedLang[0];
        String directory=packageName.replace(".", File.separator)+ File.separator+"entity";
        String directoryPath=path+directory;
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        this.setFrameworkProperties(this.getLanguageDetails().getLanguages().get(language).getFrameworks().get(framework));
        String template = (getFrameworkProperties().getTemplate());
        template=template
                .replace("#package#","namespace com.district.test.entity;")
                .replace("#imports#","")
                .replace("#class#","public class UserDto")
                .replace("#open-bracket#","{")
                .replace("#close-bracket#","}")
                .replace("#fields#","public required string UserName { get; set; }\n   public required string Password { get; set; }")
                .replace("#methods#","")
                .replace("#constructors#","")
                .replace("#encapsulation#","");
        FileUtility.generateFile(directoryPath, GeneratorService.getFileName("UserDto", languageProperties.getExtension()), template);
        System.out.println("package name : "+ packageName);
    }

    public static void main(String[] args) throws Exception {
        CodeGenerator codeGenerator = new CodeGenerator();
        String path="./";

        String packageName="test.newT";
        String lang="csharp:dotnet";
//        codeGenerator.generateUserDTO(path,packageName,lang);
        codeGenerator.generateAuthentificationPages(path,"angular-ionic");
    }
}
