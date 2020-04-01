package patients;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PatientsService {

    private String path;
    private String sheetName;


    public void register(Patient patient) {
        List<Patient> patients = PatientsReader.readFile(path, sheetName);
        if (patients.contains(patient)) {
            System.out.println("Patient is already registered.");
        } else {
            patients.add(patient);
            PatientsWriter.creatingFile(patients, path, sheetName);
        }
    }

    public Patient findPatient(String pesel) {
        List<Patient> patients = PatientsReader.readFile(path, sheetName);
        for (Patient p : patients) {
            if (p.getPesel().equals(pesel)) {
                return p;
            }
        }
        System.out.println("There is no patient in list");
        return null;
    }

    public Patient findPatient(String name, String surname) {
        List<Patient> patients = PatientsReader.readFile(path, sheetName);
        for (Patient p : patients) {
            if (p.getName().equals(name) || p.getSurname().equals(surname)) {
                return p;
            }
        }
        System.out.println("There is no patient in list");
        return null;
    }
}
