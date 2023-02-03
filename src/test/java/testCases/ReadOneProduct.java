package testCases;

import org.hamcrest.Matcher;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
public class ReadOneProduct {

	String baseURI;
	SoftAssert softAssert;
	
	
	public ReadOneProduct()
	{
		baseURI="https://techfios.com/api-prod/api/product";
		softAssert=new SoftAssert();
		
	}
	
	
	
	@Test
	public void readOneProduct() {

		
		Response response =
			given()
				// .log().all()
				.baseUri(baseURI)
				.header("Content-Type", "application/json")
				.auth().preemptive().basic("demo@techfios.com", "abc123")
				
				.queryParam("id","6209").
			when()
				// .log().all()
				.get("/read_one.php").
			then()
				// .log().all()
				.extract().response();
		
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time: " + responseTime);

		if (responseTime <= 2500) {
			System.out.println("Response time is within range");
		} else {
			System.out.println("Response time is out of range");
		}

		int responseStatusCode = response.getStatusCode();
		//	Assert.assertEquals(responseStatusCode, 200);
		softAssert.assertEquals(responseStatusCode, 200,"Response status code is not matching");
		System.out.println("Response Status code: " + responseStatusCode);

		String responseHeaderContentType = response.getHeader("Content-Type");
	//	softAssert.assertEquals(responseHeaderContentType, "application/json","Response header content type is not matching");
		softAssert.assertEquals(responseHeaderContentType, "application/json","Response header content type is not matching");
		System.out.println("Response header content type: " + responseHeaderContentType);
				  
			  
		String responseBody = response.getBody().asString();
		System.out.println("Response Body: " + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		
		String productname = jp.getString("name");
		softAssert.assertEquals(productname,"Amazing Headset 1.0 By MD","Product Name is not matching");
		System.out.println("Product Name:  " + productname);

		
		String productdescription = jp.getString("description");
		//softAssert.assertEquals(productdescription,"The best Headset for amazing programmers.","Product description is not matching");
		softAssert.assertEquals(productdescription,"The best Headset for amazing programmers.","Product description is not matching");
		System.out.println("Product description:  " + productdescription);
		
		String productPrice = jp.getString("price");
		softAssert.assertEquals(productPrice,"199","Product Price is not matching");
		System.out.println("Product Price: " + productPrice);
		
		softAssert.assertAll();
		
		
	}

}
