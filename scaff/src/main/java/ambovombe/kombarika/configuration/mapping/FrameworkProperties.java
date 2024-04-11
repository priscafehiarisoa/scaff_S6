package ambovombe.kombarika.configuration.mapping;

import ambovombe.kombarika.utils.Misc;
import ambovombe.kombarika.generator.parser.FileUtility;
import lombok.Getter;
import lombok.Setter;

import java.io.File;


@Getter @Setter
public class FrameworkProperties {
    String template;
    String pageList;
    String pageparameters;
    String authTemplate;
    String repository;
    boolean isOneRepository;
    Imports imports;
    AnnotationProperty annotationProperty;
    CrudMethod crudMethod;
    ControllerProperty controllerProperty;
    RepositoryProperty repositoryProperty;
    boolean init = false;

    public FrameworkProperties(){}

    public String getTemplatePath(){
        return Misc.getSourceTemplateLocation() + File.separator + this.template;
    }

    public String getPageListTemplate(){
        return Misc.getSourceTemplateLocation() + File.separator + this.pageList;
    }
    public String getPageParamTemplate(){
        return Misc.getSourceTemplateLocation() + File.separator + this.pageparameters;
    }

    public String getTemplate(){
        if(!init){
            String path = getTemplatePath();
            try {
                setTemplate(FileUtility.readOneFile(path));
                setInit(true);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                throw new RuntimeException(e);
            }
        }
        return this.template;
    }

    public String getAuthTemplate() {
         return Misc.getSourceTemplateLocation() + File.separator + this.authTemplate;
    }

    public String getAuthentificationTemplate(){
        String temp="";
        String path = getAuthTemplate();
        try {
            temp=(FileUtility.readOneFile(path));
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RuntimeException(e);
        }
        return temp;
    }

    public String getPageListTemp() {
        String temp="";
            String path = getPageListTemplate();
            try {
                temp=(FileUtility.readOneFile(path));
            } catch (Exception e) {
                e.printStackTrace(System.out);
                throw new RuntimeException(e);
            }
        return temp;
    }
    public String getPageParameterTemp() {
        String temp="";

            String path = getPageParamTemplate();
            try {
                temp=(FileUtility.readOneFile(path));
            } catch (Exception e) {
                e.printStackTrace(System.out);
                throw new RuntimeException(e);
            }
        return temp;
    }

    @Override
    public String toString() {
        return "FrameworkProperties{" +
                "template='" + template + '\'' +
                ", pageList='" + pageList + '\'' +
                ", pageparameters='" + pageparameters + '\'' +
                ", repository='" + repository + '\'' +
                ", isOneRepository=" + isOneRepository +
                ", imports=" + imports +
                ", annotationProperty=" + annotationProperty +
                ", crudMethod=" + crudMethod +
                ", controllerProperty=" + controllerProperty +
                ", repositoryProperty=" + repositoryProperty +
                ", init=" + init +
                '}';
    }

}
