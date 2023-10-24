package com.example.rest.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.hibernate.procedure.internal.Util;

import java.sql.*;

public class PoiUtils {
    public static String getStringValueFromCell(Cell cell){
        switch(cell.getCellType()){
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case STRING: return String.valueOf(cell.getStringCellValue());
            case BOOLEAN : return String.valueOf(cell.getBooleanCellValue());
        }
        return "";
    }
}
