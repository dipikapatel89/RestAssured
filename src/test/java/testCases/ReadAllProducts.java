package testCases;

import org.hamcrest.Matcher;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.concurrent.TimeUnit;


public class ReadAllProducts {

	/*
	   01.ReadAllProducts http method=GET
	  EndPointURL=https://techfios.com/api-prod/api/product/read.php
	  Authorization:(basic auth) username:demo@techfios.com password:abc123
	  Header/s: Content-Type=application/json; charset=UTF-8
	   http status code=200
	  response Time= <=1500ms
	   
	  given()=all input details
	     (baseUri,header/s,quryParams,authorization,Payload/Body)
	  when()=submit
	     request=httpMethod(endPoint)
	  then()=response validation(statusCode, header/s,responseTime,response Payload/Body)
	 */
	
	@Test
	public void readAllProducts() {

		Response response= given()
		      //  .log().all()
				.baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type","application/json; charset=UTF-8")
				.auth().preemptive().basic("demo@techfios.com","abc123").
		when()
				//.log().all()	
				.get("/read.php").
		then()
		 		//.log().all()
				.extract().response();
		
		long responseTime=response.getTimeIn(TimeUnit.MILLISECONDS);
			System.out.println("Response Time: " +responseTime);	
			
			if(responseTime<=2500)
			{
				System.out.println("Response time is within range");
			}
			else
			{
				System.out.println("Response time is out of range");
			}
			
		int responseStatusCode=response.getStatusCode();
	    System.out.println("Response Status code: " +responseStatusCode);
		Assert.assertEquals(responseStatusCode,200);
		
		String responseHeaderContentType=response.getHeader("Content-Type");
		System.out.println("Response header content type: " +responseHeaderContentType);
		Assert.assertEquals(responseHeaderContentType,"application/json; charset=UTF-8");
		
		String responseBody=response.getBody().asString();
		System.out.println("Response Body: " +responseBody);
		
		JsonPath jp=new JsonPath(responseBody);	
		String firstProductId=jp.getString("records[0].id");
		System.out.println("First Product Id " +firstProductId);
		
		if(firstProductId!=null)
		{
			System.out.println("Products list is not empty");
		}
		else
		{
			System.out.println("Product list is empty");
		}
	}

}
