package ambovombe.kombarika.configuration.mapping;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter

public class AnnotationProperty {
    String table;
    String autoIncrement;
    String column;
    String entity;
    String controller;
    Constraint constraints;

    @Override
    public String toString() {
        return "AnnotationProperty{" +
                "table='" + table + '\'' +
                ", autoIncrement='" + autoIncrement + '\'' +
                ", column='" + column + '\'' +
                ", entity='" + entity + '\'' +
                ", controller='" + controller + '\'' +
                ", constraints=" + constraints +
                '}';
    }
}
