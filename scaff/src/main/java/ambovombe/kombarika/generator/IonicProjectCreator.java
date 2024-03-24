package ambovombe.kombarika.generator;

import ambovombe.kombarika.generator.parser.FileUtility;
import ambovombe.kombarika.generator.service.DbService;

import java.io.*;
import java.nio.file.Files;
import ambovombe.kombarika.generator.CodeGenerator;
import ambovombe.kombarika.generator.service.DbService;
import java.nio.file.Paths;

public class IonicProjectCreator {
    CodeGenerator codeGenerator = new CodeGenerator();

    public IonicProjectCreator() throws Exception {
    }


    public static void createIonicProject(String projectName, String projectPath) {
        try {
            // Création du répertoire pour le projet
            File projectDir = new File(projectPath, projectName);
            if (!projectDir.exists()) {
                projectDir.mkdirs();
            }

            // Exécution des commandes Ionic
            String[] commands = {
                    "ionic",
                    "start",
                    projectName,
                    "blank",
                    "--type=angular"
            };
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.directory(projectDir);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // Lire la sortie de la console
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Attendre que le processus se termine
            process.waitFor();
            System.out.println("Projet Ionic créé avec succès !");

            // Ajout de pages supplémentaires
            addIonicPage(projectDir.getAbsolutePath(), "task-list");
            addIonicPage(projectDir.getAbsolutePath(), "add-task");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void addIonicPage(String projectPath, String pageName) {
        try {
            String[] commands = {
                    "ionic",
                    "generate",
                    "page",
                    pageName
            };
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.directory(new File(projectPath));
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // Lire la sortie de la console
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Attendre que le processus se termine
            process.waitFor();
            System.out.println("Page " + pageName + " créée avec succès !");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    ///////////////

    public static void clearFileContent(String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(""); // Écrire une chaîne vide pour effacer le contenu
            writer.close();
            System.out.println("Contenu du fichier " + filePath + " effacé avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String filePath, String content) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(content); // Écrire le contenu dans le fichier
            writer.close();
            System.out.println("Contenu écrit dans le fichier " + filePath + " avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateIonicService(String projectPath, String serviceName,String filepathcontent) {
        try {
            // Générer le service Ionic
            String[] commands = {
                    "ionic",
                    "generate",
                    "service",
                    serviceName
            };
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.directory(new File(projectPath));
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // Attendre que le processus se termine
            process.waitFor();
            System.out.println("Service " + serviceName + " généré avec succès !");

            // Récupérer le chemin du fichier de service
            String serviceFilePath = projectPath + "/src/app/services/" + serviceName.toLowerCase() + ".service.ts";

            // Effacer le contenu du fichier de service
            clearFileContent(serviceFilePath);

            // Écrire le contenu spécifié par l'utilisateur dans le fichier de service
            String content = readFileToString(filepathcontent);

            writeToFile(serviceFilePath, content);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String readFileToString(String filePath) throws IOException {
        // Lecture du contenu du fichier en utilisant Files.readString() disponible à partir de Java 11
            return FileUtility.getString(filePath);

    }

    ////////////////generations des pages par tables

    public void generateAllIonicPages(
            String path
    )  throws Exception{
        String[] tables = DbService.getAllTablesArrays(codeGenerator.getDbConnection());
        for (String table : tables) {
            addIonicPage(path, table);
        }
    }
    ////////////////
    ///////////////////html to ts

    public static void convertHtmlToTs(String htmlFilePath, String tsFilePath) throws IOException {
        // Lecture du contenu du fichier HTML
        try {
            String htmlContent = FileUtility.readOneFile(String.valueOf(Paths.get(htmlFilePath)));

            // Écriture du contenu dans le fichier TypeScript
//            Files.writeString(Paths.get(tsFilePath), "export const htmlContent = `" + escapeBackticks(htmlContent) + "`;");
            FileUtility.writeFile(String.valueOf(Paths.get(tsFilePath)), "export const htmlContent = `" + escapeBackticks(htmlContent) + "`;");
        }
        catch (Exception e){

        }
        }

    // Fonction pour échapper les backticks dans la chaîne de caractères HTML
    private static String escapeBackticks(String htmlContent) {
        return htmlContent.replace("`", "\\`");
    }


    //////




    public static void main(String[] args) throws IOException {
//        String projectName = "myToDoList";
//        String projectPath1 = "C:\\myToDoList\\box";
//        addIonicPage(projectPath, "huhu");

        String projectPath= "/Users/priscafehiarisoadama/IdeaProjects/learnIonicAngular/reciclica-app";
        String serviceName = "saucisse";
        String content = "/Users/priscafehiarisoadama/me/SCAFFOLDING/scaff/src/main/resources/template/view/angular-ionic/angular.service.txt";
        generateIonicService(projectPath, serviceName, content);
//        clearFileContent("C:\\myToDoList\\box\\src\\app\\haha.service.spec.ts");
//        writeToFile("C:\\myToDoList\\box\\src\\app\\haha.service.spec.ts",content);
        //////////////j ai pas pu tester le code fa tsy nety le ionic tato ducoup j ai fait a la main et essayer une par une les fonction de nety
        /////////////vasy adapte le truc stp desole, ce sont mes chemins de teste je laisse ca la pour que tu comprenne mieux

//
//        String htmlFilePath = "C:\\Users\\Jeddy\\IdeaProjects\\generatenagular\\src\\main\\java\\com\\example\\generatenagular\\page.html"; // Spécifiez le chemin de votre fichier HTML
//        String tsFilePath = "C:\\Users\\Jeddy\\IdeaProjects\\generatenagular\\src\\main\\java\\com\\example\\generatenagular\\page.ts"; // Spécifiez le chemin du fichier TypeScript de sortie
//
//        try {
//            convertHtmlToTs(htmlFilePath, tsFilePath);
//            System.out.println("Conversion terminée avec succès !");
//        } catch (IOException e) {
//            System.err.println("Erreur lors de la conversion du fichier : " + e.getMessage());
//        }
    }
}



