package ambovombe.kombarika.configuration.mapping;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RepositoryProperty {
    String name;
    String classSyntax;
    String fieldSyntax;

    @Override
    public String toString() {
        return "RepositoryProperty{" +
                "name='" + name + '\'' +
                ", classSyntax='" + classSyntax + '\'' +
                ", fieldSyntax='" + fieldSyntax + '\'' +
                '}';
    }
}
