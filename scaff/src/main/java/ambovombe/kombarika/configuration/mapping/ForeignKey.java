package ambovombe.kombarika.configuration.mapping;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ForeignKey {
    String annotation;
    String manyToOne;
    String manyToMany;
    String oneToMany;
    String oneToOne;

    @Override
    public String toString() {
        return "ForeignKey{" +
                "annotation='" + annotation + '\'' +
                ", manyToOne='" + manyToOne + '\'' +
                ", manyToMany='" + manyToMany + '\'' +
                ", oneToMany='" + oneToMany + '\'' +
                ", oneToOne='" + oneToOne + '\'' +
                '}';
    }
}
