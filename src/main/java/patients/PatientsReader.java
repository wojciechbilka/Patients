package patients;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PatientsReader {

    public static List<Patient> readFile(String path, String sheetName) {
        XSSFWorkbook workbook;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        List<Patient> list = new ArrayList<>();
        XSSFSheet sheet = workbook.getSheet(sheetName);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            list.add(new Patient(row.getCell(0).toString(),
                    row.getCell(1).toString(),
                    row.getCell(2).toString()));
        }
        return list;
    }
}
