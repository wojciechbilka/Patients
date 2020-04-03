package patients;

public class PatientsExample {

    private static final String FILE_PATH = "src/main/resources/PatientsList.xlsx";
    private static final String SHEET_NAME = "patients";

    public static void main(String[] args) {

        String num = "12312.";
        System.out.println(num.matches("([0-9]*)([.,][0-9]{2})?"));
        String stringNumber = String.valueOf(23.3);
        System.out.println(String.format("%.2s", stringNumber));
    }
}
