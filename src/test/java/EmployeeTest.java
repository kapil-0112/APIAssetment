
import org.apache.poi.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.RestAssured;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EmployeeTest {

	String name;
	String salary;
	String age;
	String id;
	
	@BeforeClass
	public void setup() {
		try
        {
            FileInputStream file = new FileInputStream(new File(System.getProperty("user.dir")+"\\testdata.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row = rowIterator.next();
        
            row = rowIterator.next();
                 
            name=row.getCell(0).toString();
            salary=row.getCell(1).toString();
            age=row.getCell(2).toString();
                
            System.out.println(name+salary+age);
        
            file.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            System.out.println("Unable to read excel");
        }

		
		
	}
	
	@Test(enabled=true, priority=1)
	public void createEmployee()
	{   
				
		//String accessToken="8270972c1a2d0d574ff2fbbce6a716839ebffd7be7e9efd9558ecfb53df9e19f";
		String endpoint="http://dummy.restapiexample.com/api/v1/create";
		
			
		HashMap hm=new JSONObject();
		hm.put("name", name );
		hm.put("salary", salary);
		hm.put("age", age);
				
		RequestSpecification rs= RestAssured.given();
		rs.body(hm);
		Response response=rs.post(endpoint);
		System.out.println(response.getBody().asString());
		System.out.println();
		
		JsonPath jp=new JsonPath(response.asString());
		id=jp.getString("data.id");
		String status=jp.getString("status");
		
		if(status.equals("success")) {
			System.out.println("Record Created");
			Assert.assertTrue(true);
		}
		
		
		else {
			System.out.println("Error Occured");
			Assert.assertTrue(false);
		}
	}

	@Test(enabled=true,priority=2)
	public void validateEmployee()
	{   

		String endpoint="http://dummy.restapiexample.com/api/v1/employee/"+id;
		String resName;
		String resSal;
		String resAge;
					
		RequestSpecification rs= RestAssured.given();
		
		Response response=rs.get(endpoint);
		
		JsonPath jp=new JsonPath(response.asString());
		try {
		resName=jp.getString("data.employee_name");
		if(resName.equals(name)) {
			System.out.println("Name is correct");
			Assert.assertTrue(true);
		}
		
		else {
			System.out.println("Name is not correct");
			Assert.assertTrue(false);
		}
		}
		
		catch(Exception e) {
		resName=null;	
		System.out.println("Name is null");
		Assert.assertTrue(false);
		}
		
		try {
			resSal=jp.getString("data.employee_salary");
	
			if(resSal.equals(salary)) {
				System.out.println("Salary is correct");
				Assert.assertTrue(true);
			}
			
			else {
				System.out.println("Salary is not correct");
				Assert.assertTrue(false);
			}
		
			}
			
			catch(Exception e) {
				resSal=null;	
				System.out.println("Salary is null");
				Assert.assertTrue(false);
			}
		
		try {
			resAge=jp.getString("data.employee_age");
			if(resAge.equals(age)) {
				System.out.println("Age is correct");
				Assert.assertTrue(true);
			}
			
			else {
				System.out.println("Age is not correct");
				Assert.assertTrue(false);
			}
			}
			
			catch(Exception e) {
				resAge=null;	
				System.out.println("Age is null");
				Assert.assertTrue(false);
			}
	
	}
	
	
	@Test(enabled=true,priority=3)
	public void deleteEmployee()
	{   
				
		
		String endpoint="http://dummy.restapiexample.com/api/v1/delete/"+id;
		
					
		RequestSpecification rs= RestAssured.given();
		
		Response response=rs.delete(endpoint);
		System.out.println(response.getBody().asString());
		System.out.println();
		
		JsonPath jp=new JsonPath(response.asString());
		String status=jp.getString("status");
		
		if(status.equals("success")) {
			
			System.out.println("Record Deleted");
			Assert.assertTrue(true);
		}
		
		
		else {
			System.out.println("Error Occured");
			Assert.assertTrue(false);
		}
		
	}
	
@AfterClass
public void teardown() {
	
	System.out.println("Test Ended");
}
}