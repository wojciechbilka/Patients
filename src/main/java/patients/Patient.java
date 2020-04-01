package patients;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Patient {

    private String name;
    private String surname;
    private String pesel;
}
