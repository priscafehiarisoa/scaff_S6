package ambovombe.kombarika.configuration.mapping;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CrudMethod {
    String findAll;
    String findById;
    String delete;
    String update;
    String save;

    @Override
    public String toString() {
        return "CrudMethod{" +
                "findAll='" + findAll + '\'' +
                ", findById='" + findById + '\'' +
                ", delete='" + delete + '\'' +
                ", update='" + update + '\'' +
                ", save='" + save + '\'' +
                '}';
    }
}
