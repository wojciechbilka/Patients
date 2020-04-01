package patients;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PatientsWriter {

    public static void creatingFile(List<Patient> employeeList, String filePath, String sheetName) {
        int rowIndex = 0;
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        Row row = sheet.createRow(rowIndex);

        row.createCell(0).setCellValue("Name:");
        row.createCell(1).setCellValue("Surname:");
        row.createCell(2).setCellValue("Pesel:");

        for (Patient e : employeeList) {
            rowIndex++;
            row = sheet.createRow(rowIndex);
            row.createCell(0).setCellValue(e.getName());
            row.createCell(1).setCellValue(e.getSurname());
            row.createCell(2).setCellValue(e.getPesel());
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addRecord(Patient patient, String filePath, String sheetName) {
        XSSFWorkbook workbook;
        XSSFSheet sheet;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(filePath));
            sheet = workbook.getSheet(sheetName);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (sheet.getLastRowNum() > 0) {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(patient.getName());
            row.createCell(1).setCellValue(patient.getSurname());
            row.createCell(2).setCellValue(patient.getPesel());
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
