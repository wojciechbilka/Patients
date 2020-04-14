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
        if (PatientsReader.isUnique(patient, path, sheetName) && !patient.getPersonalNumber().isEmpty()) {
            PatientsWriter.addRecord(patient, path, sheetName);
            flag = true;
        }
        return flag;
    }

    public Patient findPatient(String personalNumber) {
        List<Patient> patients = PatientsReader.readFile(path, sheetName);
        for (Patient p : patients) {
            if (p.getPersonalNumber().equals(personalNumber)) {
                return p;
            }
        }
        System.out.println("There is no patient on the list");
        return null;
    }

    public Patient findPatient(Patient patient) {
        List<Patient> patients = PatientsReader.readFile(path, sheetName);
        for (Patient p : patients) {
            if (p.equals(patient)) {
                return p;
            }
        }
        System.out.println("There is no patient on the list");
        return null;
    }

    public boolean patientExist(String personalNumber) {
        List<Patient> patients = PatientsReader.readFile(path, sheetName);
        for (Patient p : patients) {
            if (p.getPersonalNumber().equals(personalNumber)) {
                return true;
            }
        }
        System.out.println("There is no such patient ont the list");
        return false;
    }

    public boolean patientExist(Patient patient) {
        List<Patient> patients = PatientsReader.readFile(path, sheetName);
        for (Patient p : patients) {
            if (p.equals(patient)) {
                return true;
            }
        }
        System.out.println("There is no such patient on the list");
        return false;
    }

    public boolean updatePatientData(Patient patientBeforeUpdate, Patient patientWithUpdatedData) {
        return PatientsWriter.updateRecord(patientBeforeUpdate, patientWithUpdatedData, path, sheetName);
    }

    public List<Patient> getPatients() {
        return PatientsReader.readFile(path, sheetName);
    }

    public boolean removePatient(Patient patient) {
        return PatientsWriter.removeRecord(patient, path, sheetName);
    }

}
