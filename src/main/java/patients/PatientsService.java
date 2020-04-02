package patients;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PatientsService {

    private String path;
    private String sheetName;


    public boolean register(Patient patient) {
        boolean flag = false;
        if (PatientsReader.isUnique(patient, path, sheetName) && !patient.getPesel().isEmpty()) {
            PatientsWriter.addRecord(patient, path, sheetName);
            flag = true;
        }
        return flag;
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

    public boolean patientExist(String pesel) {
        List<Patient> patients = PatientsReader.readFile(path, sheetName);
        for (Patient p : patients) {
            if (p.getPesel().equals(pesel)) {
                return true;
            }
        }
        System.out.println("There is no such patient in list");
        return false;
    }

    public boolean patientExist(String name, String surname) {
        List<Patient> patients = PatientsReader.readFile(path, sheetName);

        for (Patient p : patients) {
            if (p.getName().equals(name) || p.getSurname().equals(surname)) {
                return true;
            }
        }
        System.out.println("There is no such patient in list");
        return false;
    }

    public List<Patient> getPatients() {
        return PatientsReader.readFile(path, sheetName);
    }

    public boolean removePatient(Patient patient) {
        return PatientsWriter.removeRecord(patient, path, sheetName);
    }

}
