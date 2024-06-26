package ambovombe.kombarika.generator.service.repository;

import ambovombe.kombarika.configuration.mapping.FrameworkProperties;
import ambovombe.kombarika.configuration.mapping.LanguageProperties;
import ambovombe.kombarika.configuration.mapping.TypeMapping;
import ambovombe.kombarika.generator.parser.FileUtility;
import ambovombe.kombarika.generator.service.GeneratorService;
import ambovombe.kombarika.generator.utils.ObjectUtility;
import ambovombe.kombarika.utils.Misc;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter @Setter
public class Repository {
    LanguageProperties languageProperties;
    FrameworkProperties frameworkProperties;
    TypeMapping typeMapping;

    public String getRepositoryImport(String packageName, String table) throws Exception{
        String res = "";
        String imp = this.getLanguageProperties().getImportSyntax();
        for(String item : this.getFrameworkProperties().getImports().getRepository())
            res += imp+ " " + item
            .replace("packageName", packageName)
            .replace("className", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table)))
            + "" + this.getLanguageProperties().getEndOfInstruction() + "\n";
        return res;
    }

    public String getRepositoryClass(String table, List<String> primaryKeysType) throws Exception{
        String res = "";
        res += this.getFrameworkProperties().getRepositoryProperty().getClassSyntax().replace("?", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table)));
        res = res.replace("#type#", this.getTypeMapping().getListMapping().get(primaryKeysType.get(0)).getType());
        return res;
    }

    public String generateRepository(
        String table,
        String packageName,
        String entityPackage,
        List<String> primaryKeysType
    ) throws Exception{
        String res = "";
        if(this.getFrameworkProperties().getRepository().equals(""))
            return res;
        String path = Misc.getSourceTemplateLocation().concat(File.separator).concat(this.getFrameworkProperties().getRepository());
        String template = FileUtility.readOneFile(path);
        res = template.replace("#package#", GeneratorService.getPackage(this.getLanguageProperties(), packageName))
                .replace("#imports#", getRepositoryImport(entityPackage, table))
                .replace("#class#", getRepositoryClass(table, primaryKeysType))
                .replace("#open-bracket#", languageProperties.getOpenBracket())
                .replace("#close-bracket#", languageProperties.getCloseBracket())
                .replace("#fields#", this.getFrameworkProperties().getRepositoryProperty().getFieldSyntax().replace("#Field#", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(table))))
                .replace("#constructors#", generateConstructor(getRepositoryClass(table, primaryKeysType)))
                .replace("#methods#", "")
                .replace("#encapsulation#", "");
        System.out.println("see frame ="+this.getFrameworkProperties());
        return res;
    }

    // added by me
    public String generatemethodsForRepository(String table,String classe){
        return " protected override void OnModelCreating(ModelBuilder modelBuilder)\n" +
                "        {\n" +
                "            modelBuilder.Entity<"+classe+">().ToTable(\""+table+"\"); // Optionally, specify table name\n" +
                "        }";
    }
    public String generateConstructor(String classe){
        classe=classe.replace("public class","");
        classe=classe.replace(": ","");
        classe=classe.replace(" DbContext","");
        System.out.println("ememememem : "+classe);
        return "public "+classe+" (DbContextOptions<RepositoryDbContext> options) : base(options)\n" +
                "        {\n" +
                "        }";
    }


    public String generateRepository(
        String[] tables,
        String context,
        String packageName,
        String entityPackage,
        List<String> primaryKeysType
    ) throws Exception{
        String res = "";
        if(this.getFrameworkProperties().getRepository().equals(""))
            return res;
        String path = Misc.getSourceTemplateLocation().concat(File.separator).concat(this.getFrameworkProperties().getRepository());
        String template = FileUtility.readOneFile(path);
        String field = "";
        for (String string : tables) {
            field += this.getFrameworkProperties().getRepositoryProperty().getFieldSyntax()
                .replace("#Field#", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(string))) + "\n";
        }
        res = template.replace("#package#", GeneratorService.getPackage(this.getLanguageProperties(), packageName))
                .replace("#imports#", getRepositoryImport(entityPackage, context))
                .replace("#class#", getRepositoryClass(context, primaryKeysType))
                .replace("#open-bracket#", languageProperties.getOpenBracket())
                .replace("#close-bracket#", languageProperties.getCloseBracket())
                .replace("#fields#", field)
                .replace("#constructors#", generateConstructor(getRepositoryClass(context, primaryKeysType)))
                .replace("#methods#", "")
                .replace("#encapsulation#", "");
        return res;
    }
}
