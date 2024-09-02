package resception.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
public abstract class Person {
    int id;
    String nationalId;
    String name;
    String family;
    String phoneNumber;


}
