/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ambovombe.generator;

import com.ambovombe.configuration.main.LanguageDetails;
import com.ambovombe.configuration.main.TypeProperties;
import com.ambovombe.configuration.mapping.*;
import com.ambovombe.utils.Misc;
import com.ambovombe.database.DbConnection;
import com.ambovombe.generator.parser.FileUtility;
import com.ambovombe.generator.service.GeneratorService;
import com.ambovombe.generator.service.controller.ControllerService;
import com.ambovombe.generator.service.repository.RepositoryService;
import lombok.Getter;
import lombok.Setter;

import java.io.*;

/**
 * @author Mamisoa
 * @author rakharrs
 */
@Getter @Setter
public class CodeGenerator {
    DbConnection dbConnection;
    LanguageDetails languageDetails;
    TypeProperties typeProperties;
    //FrameworkDetails frameworkDetails;

    public CodeGenerator() throws Exception {
        this.dbConnection = new DbConnection();
        this.dbConnection.init();
        this.languageDetails = new LanguageDetails();
        this.languageDetails.init();
        this.typeProperties = new TypeProperties();
        this.typeProperties.init();
    }

    public  void generateEntity(String path, String table, String packageName, String lang) throws Exception{
        String[] splittedLang = lang.split(":");
        String language = splittedLang[0]; String framework = splittedLang[1];
        String entity = buildEntity(table, packageName, language, framework);
        generateEntityFile(path, table, packageName, language, framework);
    }

    public void generateController(String path, String table, String packageName, String repository, String entity, String lang) throws Exception{
        String[] splittedLang = lang.split(":");
        String language = splittedLang[0]; String framework = splittedLang[1];
        String controller = buildController(table, packageName, repository, entity, language, framework);
        generateFile(path, table, packageName, language, framework, controller);
    }

    public String buildController(String table, String packageName, String repository, String entity, String language, String framework) throws Exception{
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        FrameworkProperties frameworkProperties = languageProperties.getFrameworks().get(framework);
        String template = frameworkProperties.getTemplate();
        return ControllerService.generateController(
                frameworkProperties.getCrudMethod(),
                template,
                table,
                packageName,
                repository,
                entity,
                frameworkProperties.getControllerProperty(),
                languageProperties,
                frameworkProperties.getImports(),
                frameworkProperties.getAnnotationProperty()
        );
    }
    
    public void generateRepository(String path, String table, String packageName, String entityPackage, String lang) throws Exception{
        String[] splittedLang = lang.split(":");
        String language = splittedLang[0]; String framework = splittedLang[1];
        String repository = buildRepository(table, packageName, entityPackage, language, framework);
        generateRepositoryFile(path, table, packageName, language, framework, repository);
    }

    public String buildRepository(String table, String packageName, String entityPackage, String language, String framework) throws Exception{
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        FrameworkProperties frameworkProperties = languageProperties.getFrameworks().get(framework);
        return RepositoryService.generateRepository(
                table,
                languageProperties,
                frameworkProperties,
                packageName,
                entityPackage
        );
    }


    /**
     * eg : generate -p path -t table1, table2, table3 -package name -l java:spring-boot
     * @author rakharrs
     */
    public String buildEntity(String table, String packageName, String language, String framework) throws Exception {
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        FrameworkProperties frameworkProperties = languageProperties.getFrameworks().get(framework);
        String template = frameworkProperties.getTemplate();
        return GeneratorService
                .generateEntity(
                        getDbConnection(),
                        template,
                        table,
                        packageName,
                        languageProperties,
                        frameworkProperties,
                        getTypeProperties()
                );
    }


    public void generateFile(
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
        FileUtility.generateFile(path, GeneratorService.getFileName(table+"Controller", languageProperties), content);
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
        if (languageProperties.getFrameworks().get(framework).getRepository().equals("")) return;
        String directory = packageName.replace(".", File.separator);
        FileUtility.createDirectory(directory,path);
        path = path + File.separator + directory;
        FileUtility.generateFile(path, GeneratorService.getFileName(table+"Repository", languageProperties), content);
    }

    public void generateEntityFile(String path, String table, String packageName, String language, String framework) throws Exception{
        String entity = buildEntity(table, packageName, language, framework);
        LanguageProperties languageProperties = getLanguageDetails().getLanguages().get(language);
        String directory = packageName.replace(".", File.separator);
        FileUtility.createDirectory(directory,path);
        path = path + File.separator + directory;
        FileUtility.generateFile(path, GeneratorService.getFileName(table, languageProperties), entity);
    }

    public static String getTemplate() throws Exception{
        String path = Misc.getSourceTemplateLocation() + File.separator + "Template.code";
        return FileUtility.readOneFile(path);
    }
    
    public  void generateAll(String path, String packageName, String[] tables, String framework) throws Exception{
        for (String table : tables) {
            generateEntity(path, table, packageName + "." + "entity", framework);
            generateRepository(path, table, packageName + "." + "repository", packageName + "." + "entity", framework);
            generateController(path, table, packageName + "." + "controller", packageName + "." + "repository", packageName + "." + "entity", framework);   
        }
    }


}
