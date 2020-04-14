package patients;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PatientsWriter {

    public static void creatingFile(String filePath, String sheetName) {
        int rowIndex = 0;
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        Row row = sheet.createRow(rowIndex);

        row.createCell(0).setCellValue("Name");
        row.createCell(1).setCellValue("Surname");
        row.createCell(2).setCellValue("Personal Number");
        row.createCell(3).setCellValue("Wallet");
        row.createCell(4).setCellValue("TestStatus");

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean addRecord(Patient patient, String filePath, String sheetName) {
        XSSFWorkbook workbook;
        XSSFSheet sheet;
        boolean flag = false;

        try {
            workbook = new XSSFWorkbook(new FileInputStream(filePath));
            sheet = workbook.getSheet(sheetName);
        } catch (IOException e) {
            e.printStackTrace();
            return flag;
        }

        if (sheet.getLastRowNum() >= 0 && PatientsReader.isUnique(patient, filePath, sheetName)) {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(patient.getName());
            row.createCell(1).setCellValue(patient.getSurname());
            row.createCell(2).setCellValue(patient.getPersonalNumber());
            row.createCell(3).setCellValue(patient.getBalance().toString());
            row.createCell(4).setCellValue(patient.getTestResult().name());
            flag = true;
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean updateRecord(Patient patientToUpdate, Patient updatedPatient, String filePath, String sheetName) {
        XSSFWorkbook workbook;
        XSSFSheet sheet;
        boolean flag = false;

        try {
            workbook = new XSSFWorkbook(new FileInputStream(filePath));
            sheet = workbook.getSheet(sheetName);
        } catch (IOException e) {
            e.printStackTrace();
            return flag;
        }

        Row row;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            if (rowCorrespondsToPatient(row, patientToUpdate)) {
                row.getCell(0).setCellValue(updatedPatient.getName());
                row.getCell(1).setCellValue(updatedPatient.getSurname());
                row.getCell(2).setCellValue(updatedPatient.getPersonalNumber());
                row.getCell(3).setCellValue(updatedPatient.getBalance().toString());
                row.getCell(4).setCellValue(updatedPatient.getTestResult().name());
                flag = true;
            }
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    private static boolean rowCorrespondsToPatient(Row row, Patient patient) {
        if (    row.getCell(0).toString().equals(patient.getName()) &&
                row.getCell(1).toString().equals(patient.getSurname()) &&
                row.getCell(2).toString().equals(patient.getPersonalNumber()) &&
                Double.valueOf(row.getCell(3).toString()).equals(patient.getBalance()) &&
                TestResult.valueOf(row.getCell(4).toString()).equals(patient.getTestResult())) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean removeRecord(Patient patient, String filePath, String sheetName) {
        XSSFWorkbook workbook;
        XSSFSheet sheet;
        boolean flag = false;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(filePath));
            sheet = workbook.getSheet(sheetName);
        } catch (IOException e) {
            e.printStackTrace();
            return flag;
        }

        int rowNum = -1;
        for (Row row : sheet) {
            if ((row.getCell(2).toString().equals(patient.getPersonalNumber()))) {
                rowNum = row.getRowNum();
            }
        }
        if (rowNum != -1) {
            removeRow(sheet, rowNum);
            flag = true;
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    private static void removeRow(XSSFSheet sheet, int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
        if (rowIndex == lastRowNum) {
            XSSFRow removingRow = sheet.getRow(rowIndex);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }
    }

}
