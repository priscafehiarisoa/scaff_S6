package ambovombe.kombarika.generator.service.view;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ambovombe.kombarika.configuration.mapping.FrameworkProperties;
import ambovombe.kombarika.configuration.mapping.ViewProperties;
import ambovombe.kombarika.database.DbConnection;
import ambovombe.kombarika.generator.parser.FileUtility;
import ambovombe.kombarika.generator.service.DbService;
import ambovombe.kombarika.generator.utils.ObjectUtility;
import ambovombe.kombarika.utils.Misc;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class View {
    ViewProperties viewProperties;
    FrameworkProperties frameworkProperties;



    public String getOptionUpdate(HashMap<String, String> foreignKeys, String url, String id, String attribute) throws Exception{
        String res = "";
        String [] attributes=attribute.split("/");
        int i =0 ;
        if (foreignKeys.isEmpty()) {
            return "";
        }
        for (Map.Entry<String, String> set : foreignKeys.entrySet()) {
            res += this.getViewProperties().getOptionUpdate()
                .replace("#url#", url)
                .replace("#path#", ObjectUtility.formatToCamelCase(set.getValue()))
                .replace("#Name#", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(set.getValue())))
                .replace("#label#", ObjectUtility.formatToCamelCase(set.getValue()))
                .replace("#id#", ObjectUtility.formatToCamelCase(id))
                .replace("#attribute#", ObjectUtility.formatToCamelCase(attributes[i]))
                ;
            res += "\n";
            i++;
        }
        return Misc.tabulate(res);
    }
    public String getInputInsert(HashMap<String, String> columns, HashMap<String, String> foreignKeys, List<String> primaryKeys, String url, String id, String attribute) throws Exception{
        String res ="";
        String template = this.getViewProperties().getInputInsert();
        String [] attributes=attribute.split("/");
        int i =0 ;
        for (Map.Entry<String, String> set : columns.entrySet()) {
            if (!primaryKeys.contains(set.getKey())) {
                String temp = foreignKeys.get(set.getKey());
                if(temp != null){
                    String option="";
                    try {
                         option = this.getViewProperties().getOption()
                                .replace("#url#", url)
                                .replace("#path#", ObjectUtility.formatToCamelCase(temp))
                                .replace("#id#", ObjectUtility.formatToCamelCase(id))
                                .replace("#name#", ObjectUtility.formatToCamelCase(set.getKey()))
                                .replace("#attribute#", ObjectUtility.formatToCamelCase(attributes[i]));
                        i++;
                    }catch (Exception e){

                    }
                    option = Misc.tabulate(Misc.tabulate(option));
                    res += this.getViewProperties().getSelect()
                            .replace("#name#", ObjectUtility.formatToCamelCase(temp))
                            .replace("#label#", ObjectUtility.formatToCamelCase(temp))
                            .replace("#option#", option);
                    continue;
                }
                res += template
                        .replace("#label#", ObjectUtility.formatToSpacedString(set.getKey()))
                        .replace("#type#", this.getViewProperties().getListMapping().get(set.getValue().split("\\.")[set.getValue().split("\\.").length -1]))
                        .replace("#name#", ObjectUtility.formatToCamelCase(set.getKey())) + "\n";
            }
        }
        return Misc.tabulate(res);
    }
    public String getInputUpdate(HashMap<String, String> columns, HashMap<String, String> foreignKeys, List<String> primaryKeys, String url, String id,String attribute) throws Exception{
        String res ="";
        String template = this.getViewProperties().getInputUpdate();
        String [] attributes=attribute.split("/");
        int i =0 ;
        for (Map.Entry<String, String> set : columns.entrySet()) {
            if (!primaryKeys.contains(set.getKey())) {
                String temp = foreignKeys.get(set.getKey());
                if(temp != null){
                    res += this.getViewProperties().getSelectUpdate()
                    .replace("#name#", ObjectUtility.formatToCamelCase(temp))
                    .replace("#id#", ObjectUtility.formatToCamelCase(id))
                    .replace("#Name#", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(temp)));
                    String option ="";
                    try {
                        option = this.getViewProperties().getOption()
                                .replace("#url#", url)
                                .replace("#path#", ObjectUtility.formatToCamelCase(temp))
                                .replace("#label#", temp)
                                .replace("#id#", ObjectUtility.formatToCamelCase(id))
                                .replace("#attribute#", ObjectUtility.formatToCamelCase(attributes[i]))
                                .replace("#name#", ObjectUtility.formatToCamelCase(set.getKey())) + "\n";

                        i++;
                    }catch (Exception e){

                    }
                    res=res.replace("#optionUpdate#",option).replace("#label#", ObjectUtility.formatToCamelCase(temp));
                    continue;
                }
                res +=  template
                .replace("#label#", ObjectUtility.formatToSpacedString(set.getKey()))
                .replace("#type#", this.getViewProperties().getListMapping().get(set.getValue().split("\\.")[set.getValue().split("\\.").length -1]))
                .replace("#Name#", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(set.getKey())))
                .replace("#name#", ObjectUtility.formatToCamelCase(set.getKey())) + "\n";
            }else{
                res += template
                .replace("#label#", "")
                .replace("#type#", "hidden")
                .replace("#Name#", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(set.getKey())))
                .replace("#name#", ObjectUtility.formatToCamelCase(set.getKey())) + "\n";
            }
        }
        return Misc.tabulate(res);
    }

    public String getHeaders(HashMap<String, String> columns){
        String res ="";
        String template = this.getViewProperties().getTableHeader();
        for (Map.Entry<String, String> set : columns.entrySet()) {
            if(set.getKey().equals("password")){
                continue;
            }
            res += "\t\t" + template
            .replace("#label#", ObjectUtility.formatToSpacedString(set.getKey()).replace("_"," ")) + "\n";
        }
        return res;
    }

    public String getTableValue(HashMap<String, String> columns, HashMap<String, String> foreignKeys, String attribute){
        String res ="";
        String template = this.getViewProperties().getTableValue();
        int i=0 ;
        String [] attributes= attribute.split("/");
        System.out.println("table values : ");
        System.out.println(template);
        System.out.println(attribute);
        for (Map.Entry<String, String> set : columns.entrySet()) {
            if(foreignKeys.get(set.getKey()) != null){
                try {
                    res += "\t\t" + template
                            .replace("#values#", ObjectUtility.formatToCamelCase(foreignKeys.get(set.getKey())) + "." + ObjectUtility.formatToCamelCase(attributes[i])) + "\n";
                    System.out.println(res);
                    i++;
                }catch (Exception e){

                }
            }else{
                if(set.getKey().contains("password")){
                    continue;
                }
                res += "\t\t" + template
                .replace("#values#", ObjectUtility.formatToCamelCase(set.getKey())) + "\n";
            }
        }
        return res;
    }

    public String getHandleInputSelectChange(HashMap<String, String> columns, HashMap<String, String> foreignKeys, List<String> primaryKeys){
        String res = "";
        String template = this.getViewProperties().getHandleInputChange();
        for (Map.Entry<String, String> set : columns.entrySet()) {
            String temp = foreignKeys.get(set.getKey());
            if(temp != null){
                res += this.getViewProperties().getHandleSelectChange()
                .replace("#Name#", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(temp)))
                .replace("#name#", ObjectUtility.formatToCamelCase(temp)) + "\n";
                continue;
            }
            res +=  template
            .replace("#Name#", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(set.getKey())))
            .replace("#name#", ObjectUtility.formatToCamelCase(set.getKey())) + "\n";
        }
        return Misc.tabulate(res);
    }

    public String getValues(HashMap<String, String> columns, HashMap<String, String> foreignKeys, String table){
        String res = "";
        String template = this.getViewProperties().getValues();
        res += template
            .replace("#entity#", table)
            .replace("#Entity#", ObjectUtility.capitalize(table)) + "\n";
        for (Map.Entry<String, String> set : columns.entrySet()) {
            String temp = foreignKeys.get(set.getKey());
            if(temp != null){
                res += this.getViewProperties().getValues()
                .replace("#entity#", ObjectUtility.formatToCamelCase(temp))
                .replace("#Entity#", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(temp))) + "\n";
                continue;
            }
        }
        return Misc.tabulate(res);
    }

    public String getFetcher(HashMap<String, String> columns, HashMap<String, String> foreignKeys, String table){
        String res = "";
        String template = this.getViewProperties().getFetch();
        res += template
            .replace("#entity#", table)
            .replace("#Entity#", ObjectUtility.capitalize(table));
        for (Map.Entry<String, String> set : columns.entrySet()) {
            String temp = foreignKeys.get(set.getKey());
            if(temp != null){
                res += this.getViewProperties().getFetch()
                .replace("#entity#", ObjectUtility.formatToCamelCase(temp))
                .replace("#Entity#", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(temp)));
                continue;
            }
        }
        return Misc.tabulate(res);
    }

    public HashMap<String, String> getIdAndAttribute(DbConnection dbConnection, HashMap<String, String> foreignKeys) throws Exception{
        String attribute = "";
        String id = "";
        HashMap<String,String> map = new HashMap<>();
        System.out.println("foreign keys "+ foreignKeys);
        for (Map.Entry<String, String> set : foreignKeys.entrySet()) {
            List<String> tempPrimaryKey = DbService.getPrimaryKey(dbConnection, set.getValue());
            id = tempPrimaryKey.get(0);
            HashMap<String, String> tempColumns = DbService.getDetailsColumn(dbConnection.getConnection(), set.getValue());
            for (Map.Entry<String, String> set2 : tempColumns.entrySet()) {
                if(set2.getValue().equals("java.lang.String")){
                    if(attribute!="") attribute+="/";
                    attribute += set2.getKey();

                    break;
                }
            }
//            break;
        }
        map.put("attribute", attribute);
        map.put("id", id);
        return map;
    }

    public String generateView(String table, String url, DbConnection dbConnection) throws Exception{
        String res = "";
//        chemin misy ny template an'le fichier ho-générer-na
        String tempPath = Misc.getViewTemplateLocation().concat(File.separator).concat(this.getViewProperties().getTemplate());
        String template = FileUtility.readOneFile(tempPath);
        System.out.println("template path : "+tempPath);
//        System.out.println("view template : "+template);
        List<String> primaryKeys = DbService.getPrimaryKey(dbConnection, table);
        String path =  ObjectUtility.formatToCamelCase(table);
        HashMap<String, String> columns = DbService.getDetailsColumn(dbConnection.getConnection(), table);
        HashMap<String, String> foreignKeys = DbService.getForeignKeys(dbConnection, table);
        HashMap<String, String> idAndAttribute = this.getIdAndAttribute(dbConnection, foreignKeys);
        String id = idAndAttribute.get("id");
        String attribute = idAndAttribute.get("attribute");
        System.out.println("qwerty : => => "+idAndAttribute);
        res = template.replace("#header#", getHeaders( columns))
        .replace("#inputInsert#", getInputInsert(columns, foreignKeys, primaryKeys, url, id, attribute))
        .replace("#inputUpdate#", getInputUpdate(columns, foreignKeys, primaryKeys, url, id,attribute))
//        .replace("#optionUpdate#", getOptionUpdate(foreignKeys, url, id, attribute))
        .replace("#handleInputSelectChange#", getHandleInputSelectChange(columns, foreignKeys, primaryKeys))
        .replace("#getValues#", getFetcher(columns, foreignKeys, table))
        .replace("#values#", getValues(columns, foreignKeys, table))
        .replace("#entity#", ObjectUtility.formatToSpacedString(table))
        .replace("#tableValue#", getTableValue(columns, foreignKeys, attribute))
        .replace("#url#", url)
        .replace("#id#", ObjectUtility.formatToCamelCase(primaryKeys.get(0)))
        .replace("#path#", path)
        .replace("#label#", ObjectUtility.formatToCamelCase(primaryKeys.get(0)));

        System.out.println("path:=> "+path);
//        String res = "";
//        String tempPath = Misc.getViewTemplateLocation().concat(File.separator).concat(this.getViewProperties().getTemplate());
//        String template = FileUtility.readOneFile(tempPath);
//        List<String> primaryKeys = DbService.getPrimaryKey(dbConnection, table);
//        String path =  ObjectUtility.formatToCamelCase(table);
//        path = this.getFrameworkProperties().getControllerProperty().getPathSyntax().replace("?", path);
//        HashMap<String, String> columns = DbService.getDetailsColumn(dbConnection.getConnection(), table);
//        HashMap<String, String> foreignKeys = DbService.getForeignKeys(dbConnection, table);
//        HashMap<String, String> idAndAttribute = this.getIdAndAttribute(dbConnection, foreignKeys);
//        String id = idAndAttribute.get("id");
//        String attribute = idAndAttribute.get("attribute");
//        res = template.replace("#header#", getHeaders( columns))
//                .replace("#inputInsert#", getInputInsert(columns, foreignKeys, primaryKeys, url, id, attribute))
//                .replace("#inputUpdate#", getInputUpdate(columns, foreignKeys, primaryKeys, url, id))
//                .replace("#optionUpdate#", getOptionUpdate(foreignKeys, url, id, attribute))
//                .replace("#handleInputSelectChange#", getHandleInputSelectChange(columns, foreignKeys, primaryKeys))
//                .replace("#getValues#", getFetcher(columns, foreignKeys, table))
//                .replace("#values#", getValues(columns, foreignKeys, table))
//                .replace("#entity#", ObjectUtility.formatToSpacedString(table))
//                .replace("#tableValue#", getTableValue(columns, foreignKeys, attribute))
//                .replace("#url#", url)
//                .replace("#id#", ObjectUtility.formatToCamelCase(primaryKeys.get(0)))
//                .replace("#path#", path)
//                .replace("#label#", ObjectUtility.formatToCamelCase(primaryKeys.get(0)));

        return res;
    }
}
