package patients;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Patient {

    private String name;
    private String surname;
    private String pesel;
    private Double wallet;

    public boolean isSolvent(double price) {
        return wallet > price;
    }

    public void payForTest(double value) {
        wallet = wallet - value;
    }
}
