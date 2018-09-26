package com.dev.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.dev.entity.system.LoginUser;

@Component
public class ExportUtil<T> {

	@Autowired
	private Environment environment;

	public File exportReleaseHistoryToExcel(List<Map<String,String>> list) throws Exception {
		String fileName="release_history.xlsx";
    	InputStream stream = getClass().getClassLoader().getResourceAsStream("static"+File.separator+fileName);
    	File newFile = new File(fileName.substring(0, fileName.lastIndexOf("."))+"_"+System.currentTimeMillis()+".xlsx");
    	FileUtils.copyInputStreamToFile(stream, newFile);
		
		InputStream in = new FileInputStream(newFile);

		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
		XSSFSheet sheet = xssfWorkbook.getSheetAt(0);

		Map<String,String> paramMap = null;
		// sheet 对应一个工作页

		for (int i = 0; i <list.size(); i++) {
			//创建一行
			XSSFRow row = sheet.createRow(i+1);  //加1不然覆盖掉上面的行数
			paramMap = list.get(i);
			row.createCell(0).setCellValue(paramMap.get("rls_no")); 
			row.createCell(1).setCellValue(paramMap.get("product_name")); 
			row.createCell(2).setCellValue(paramMap.get("rls_version")); 
			row.createCell(3).setCellValue(paramMap.get("rls_mode_name")); 
			row.createCell(4).setCellValue(paramMap.get("is_script_name")); 
			row.createCell(5).setCellValue(paramMap.get("dev_manager_uname")); 
			row.createCell(6).setCellValue(paramMap.get("test_manager_uname")); 
			row.createCell(7).setCellValue(paramMap.get("rls_date")); 
			row.createCell(8).setCellValue(paramMap.get("is_gray_name"));
			row.createCell(9).setCellValue(paramMap.get("gray_scale")); 
			row.createCell(10).setCellValue(paramMap.get("rls_type_name")); 
			row.createCell(11).setCellValue(paramMap.get("rls_type_ext")); 
			row.createCell(12).setCellValue(paramMap.get("rollback_plan_name")); 
			row.createCell(13).setCellValue(paramMap.get("rollback_plan_ext")); 
			row.createCell(14).setCellValue(paramMap.get("rls_theme")); 
			row.createCell(15).setCellValue(paramMap.get("rls_content")); 
			row.createCell(16).setCellValue(paramMap.get("rls_remark")); 
			
			row.createCell(17).setCellValue(paramMap.get("applicant_uname")); 
			row.createCell(18).setCellValue(paramMap.get("applicant_date")); 
			row.createCell(19).setCellValue(paramMap.get("release_one")); 
			row.createCell(20).setCellValue(paramMap.get("release_two")); 
			row.createCell(21).setCellValue(paramMap.get("release_three")); 
			row.createCell(22).setCellValue(paramMap.get("release_four")); 
			row.createCell(23).setCellValue(paramMap.get("release_five")); 
			row.createCell(24).setCellValue(paramMap.get("config_result"));
			row.createCell(25).setCellValue(paramMap.get("dba_result")); 
			row.createCell(26).setCellValue(paramMap.get("test_result")); 
		}

		OutputStream out = new FileOutputStream(newFile);
		xssfWorkbook.write(out);

		// 读取此文件的流数据
		return newFile;
	}
	    public File exportListExcelWithHeaders(String[][] exportInfo,List list,String fileName,ServletOutputStream outputStream) throws Exception {
		// 第一步，创建一个webbook，对应一个Excel文件
		// HSSFWorkbook wb = new HSSFWorkbook();
		InputStream stream = getClass().getClassLoader().getResourceAsStream("static" + File.separator + fileName);
		File newFile = new File(fileName.substring(0, fileName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + ".xlsx");
		try {
			FileUtils.copyInputStreamToFile(stream, newFile);
			InputStream in = new FileInputStream(newFile);
			XSSFWorkbook wb = new XSSFWorkbook(in);

			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			// XSSFSheet sheet = wb.createSheet(sheetName);
			XSSFSheet sheet = wb.getSheetAt(0);
			
		   	for (short i = 0; i < exportInfo[0].length; i++) {
		   		sheet.setColumnWidth(i, 3000);
			}
		   	
			XSSFRow row = sheet.createRow(0);
			// 第四步，创建单元格，并设置值表头 设置表头居中
			XSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//			style.setFillForegroundColor(HSSFColor.TURQUOISE.index);
//			style.setFillPattern(CellStyle.SOLID_FOREGROUND);

			XSSFCell cell;
			XSSFRichTextString text = null;
			for (int i = 0; i < exportInfo[0].length; i++) {
				cell = row.createCell(i);
				text = new XSSFRichTextString(exportInfo[0][i]);
				cell.setCellValue(text);
				cell.setCellStyle(style);
			}
			Object t = new Object();//根据实际的对象取值
			Field[] fields =null;
			Field field =null;
			String fieldName =null;
			String methodName =null;
			Method getMethod =null;
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow((int) i + 1);
				t = list.get(i);
				// 第四步，创建单元格，并设置值
				fields = t.getClass().getDeclaredFields();
				for (int j = 0; j < exportInfo[0].length; j++) {
					for (int k = 0; k < fields.length; k++) {
						field = fields[k];
						fieldName = field.getName();
						if (StringUtil.equals(fieldName, exportInfo[1][j])) {
							cell = row.createCell(j);
							cell.setCellStyle(style);// 设置值和格式
							methodName = "get"+ fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
							getMethod = t.getClass().getMethod(methodName, new Class[] {});
							if ("measure,deposit,cash_pledge,agency_fee,related_measure,related_deposit,related_cash_pledge,related_agency_fee".contains(fieldName)) {
								if (getMethod.invoke(t, new Object[] {})!=null) {
									cell.setCellValue(StringUtil.handleDouble(Double.parseDouble(StringUtil.getNotNullStr(getMethod.invoke(t, new Object[] {})))));
								}else{
									cell.setCellValue("");
								}
							
								break;
							}else if ("department".equals(fieldName)) {
								cell.setCellValue(StringUtil.processDept(StringUtil.getNotNullStr(getMethod.invoke(t, new Object[] {}))));
							} else {
								cell.setCellValue(StringUtil.getNotNullStr(getMethod.invoke(t, new Object[] {})));
								break;	
							}

						} else {
							continue;
						}
					}
				}
			}
			// 第六步，将文件存到指定位置
				wb.write(outputStream);
				outputStream.flush();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return newFile;
	    }

}
