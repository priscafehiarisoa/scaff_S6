package ambovombe.kombarika.utils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Misc {
    public static String currentLocation(String name) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resourceUrl = classLoader.getResource(name);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource not found: " + name);
        }
        return resourceUrl.getPath();
    }

    public static String readResource(String resourcePath) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
        if (inputStream == null) {
            // If the resource is not found in the classpath, try reading it as a file
            File file = new File(resourcePath);
            if (file.exists()) {
                inputStream = new FileInputStream(file);
            } else {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
        }
        return readInputStream(inputStream);
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }
//    public static String currentLocation(String name) {
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        return classLoader.getResource(name).getPath();
//    }

    public static String tabulate(String string){
        string = "\t" + string;
        return string.replace("\n", "\n\t");
    }

    public static String getTemplateLocation(){
        return currentLocation("template");
    }

    public static String getSourceTemplateLocation(){
        return getTemplateLocation() + File.separator + "sourceCode";
    }

    public static String getViewTemplateLocation(){
        return getTemplateLocation() + File.separator + "view";
    }

    public static String getConfigLocation(){
        return currentLocation("conf");
    }

    public static String getConnectionConfLocation(){
        String separator = File.separator;
        return getConfigLocation() + separator + "connection";
    }

    public static String getGeneratorConfLocation(){
        String separator = File.separator;
        return getConfigLocation() + separator + "generator";
    }
}
