package resception.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString

public abstract class Person {

    private String username;
    private String password;
    private String nationalId;
    private String name;
    private String family;
    private String phoneNumber;
    private boolean active;
    private String accessLevel;

}



