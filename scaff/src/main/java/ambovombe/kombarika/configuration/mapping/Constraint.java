package ambovombe.kombarika.configuration.mapping;

import lombok.Getter;
import lombok.Setter;

public class Constraint {
    @Getter @Setter
    String primaryKey;
    @Getter @Setter
    ForeignKey foreignKey;

    @Override
    public String toString() {
        return "Constraint{" +
                "primaryKey='" + primaryKey + '\'' +
                ", foreignKey=" + foreignKey +
                '}';
    }
}
