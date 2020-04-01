package patients;

import java.util.Arrays;
import java.util.List;

public class PatientsExample {

    private static final String FILE_PATH = "src/main/resources/PatientsList.xlsx";
    private static final String SHEET_NAME = "patients";

    public static void main(String[] args) {

        List<Patient> patients = Arrays.asList(
                new Patient("John", "Cena", "12312323"),
                new Patient("Alec", "White", "23455679"),
                new Patient("Pablo", "Coelo", "12398467")
        );

        Patient patient = new Patient("George", "Kapeloni", "13398467");

        PatientsService patientsService = new PatientsService(FILE_PATH, SHEET_NAME);

        PatientsWriter.creatingFile(patients, FILE_PATH, SHEET_NAME);
        PatientsWriter.creatingFile(patients, FILE_PATH, SHEET_NAME);

        Patient foundPatient = patientsService.findPatient("12398467");
        System.out.println(patientsService.patientExist("12398467"));
        System.out.println(foundPatient);
        PatientsWriter.addRecord(patient, FILE_PATH, SHEET_NAME);
    }
}
