package testCases;

import org.hamcrest.Matcher;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
public class UpdateOneProduct {

	String baseURI;
	SoftAssert softAssert;
	String createPayloadPath;
	HashMap<String,String> createPayload;
	HashMap<String,String> updatePayload;
	String firstProductId;
	
	public UpdateOneProduct()
	{
		baseURI="https://techfios.com/api-prod/api/product";
		softAssert=new SoftAssert();
		createPayloadPath="src\\main\\java\\data\\CreatePayload.json";
		createPayload=new HashMap<String,String>();
		updatePayload=new HashMap<String,String>();
	}
	public Map<String,String> createPayloadMap()
	{
		createPayload.put("name","Amazing Headset 8.0");
		createPayload.put("description","The super Headset for amazing programmers.");
		createPayload.put("price","299");
		createPayload.put("category_id","2");
		createPayload.put("category_name","Electronics");
		return createPayload;
	}
		
	public Map<String,String> updatePayloadMap()
	{
		updatePayload.put("id","6875");
        updatePayload.put("name","Amazing Headset 5.0");
        updatePayload.put("description","The best Headset for amazing programmers.");
        updatePayload.put("price","499");
        updatePayload.put("category_id","2");
        updatePayload.put("category_name","Electronics");
		return updatePayload;
	}
	@Test(priority=1)
	public void createOneProduct() {
		//System.out.println("create payload map: " + createPayloadMap());

		Response response =
			given()
				// .log().all()
				.baseUri(baseURI)
				.header("Content-Type", "application/json; charset=UTF-8")
				.auth().preemptive().basic("demo@techfios.com", "abc123")
				.body(createPayloadMap()).
			when()
				// .log().all()
				.post("/create.php").
			then()
				// .log().all()
				.extract().response();

		/*long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time: " + responseTime);

		if (responseTime <= 2500) {
			System.out.println("Response time is within range");
		} else {
			System.out.println("Response time is out of range");
		}*/

		int responseStatusCode = response.getStatusCode();
		//	Assert.assertEquals(responseStatusCode, 200);
		softAssert.assertEquals(responseStatusCode, 201,"Response status code is not matching");
		System.out.println("Response Status code: " + responseStatusCode);

		String responseHeaderContentType = response.getHeader("Content-Type");
	//	softAssert.assertEquals(responseHeaderContentType, "application/json","Response header content type is not matching");
		softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8","Response header content type is not matching");
		System.out.println("Response header content type: " + responseHeaderContentType);
				  
		String responseBody = response.getBody().asString();
		System.out.println("Response Body: " + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		
		String productMessage = jp.getString("message");
		softAssert.assertEquals(productMessage,"Product was created.","Product message is not matching");
		System.out.println("Product Message:  " + productMessage);
		
		softAssert.assertAll();
	}
	
	@Test(priority=2)
	public void readAllProducts() {

		Response response= given()
		      //  .log().all()
				.baseUri(baseURI)
				.header("Content-Type","application/json; charset=UTF-8")
				.auth().preemptive().basic("demo@techfios.com","abc123").
		when()
				//.log().all()	
				.get("/read.php").
		then()
		 		//.log().all()
				.extract().response();
		
		String responseBody=response.getBody().asString();
		System.out.println("Response Body: " +responseBody);
		
		JsonPath jp=new JsonPath(responseBody);	
	    firstProductId=jp.getString("records[0].id");
		System.out.println("First Product Id " +firstProductId);
		
		
	}
	
	//@Test(priority=3)
	public void readOneProduct() {

		
		Response response =
			given()
				// .log().all()
				.baseUri(baseURI)
				.header("Content-Type", "application/json")
				.auth().preemptive().basic("demo@techfios.com", "abc123")
				
				.queryParam("id",firstProductId).
			when()
				// .log().all()
				.get("/read_one.php").
			then()
				// .log().all()
				.extract().response();

	
		String ActualresponseBody = response.getBody().asString();
		System.out.println("Actual Response Body: " + ActualresponseBody);
		
		JsonPath jp = new JsonPath(ActualresponseBody);
		
		String Actualproductname = jp.getString("name");
		String Expectedproductname=createPayloadMap().get("name");
		softAssert.assertEquals(Actualproductname,Expectedproductname,"Product Names are not matching");
		System.out.println("Actual Product Name:  " + Actualproductname);

		
		String Actualproductdescription = jp.getString("description");
		String Expectedproductdescription=createPayloadMap().get("description");
		softAssert.assertEquals(Actualproductdescription,Expectedproductdescription,"Product description is not matching");
		System.out.println("Actual Product description:  " + Actualproductdescription);
		
		String ActualproductPrice = jp.getString("price");
		String Expectedproductprice=createPayloadMap().get("price");
		softAssert.assertEquals(ActualproductPrice,Expectedproductprice,"Product Price is not matching");
		System.out.println("Actual Product Price: " + ActualproductPrice);
		
		softAssert.assertAll();
	}
	
	@Test(priority=4)
	public void updateOneProduct() {
		//System.out.println("create payload map: " + createPayloadMap());

		Response response =
			given()
				// .log().all()
				.baseUri(baseURI)
				.header("Content-Type", "application/json; charset=UTF-8")
				.auth().preemptive().basic("demo@techfios.com", "abc123")
				.body(updatePayloadMap()).
			when()
				// .log().all()
				.put("/update.php").
			then()
				// .log().all()
				.extract().response();

		int responseStatusCode = response.getStatusCode();
		//	Assert.assertEquals(responseStatusCode, 200);
		softAssert.assertEquals(responseStatusCode, 200,"Response status code is not matching");
		System.out.println("Response Status code: " + responseStatusCode);

		String responseHeaderContentType = response.getHeader("Content-Type");
	//	softAssert.assertEquals(responseHeaderContentType, "application/json","Response header content type is not matching");
		softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8","Response header content type is not matching");
		System.out.println("Response header content type: " + responseHeaderContentType);
				  
		String responseBody = response.getBody().asString();
		System.out.println("Response Body: " + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		
		String productMessage = jp.getString("message");
		softAssert.assertEquals(productMessage,"Product was updated.","Product message is not matching");
		System.out.println("Product Message:  " + productMessage);
		
		softAssert.assertAll();
	}
	
	@Test(priority=5)
		public void readOneUpdatedProduct() {

			
			Response response =
				given()
					// .log().all()
					.baseUri(baseURI)
					.header("Content-Type", "application/json")
					.auth().preemptive().basic("demo@techfios.com", "abc123")
					
					.queryParam("id",updatePayloadMap().get("id")).
				when()
					// .log().all()
					.get("/read_one.php").
				then()
					// .log().all()
					.extract().response();

		
			String ActualresponseBody = response.getBody().asString();
			System.out.println("Actual Response Body: " + ActualresponseBody);
			
			JsonPath jp = new JsonPath(ActualresponseBody);
			
			String Actualproductname = jp.getString("name");
			String Expectedproductname=updatePayloadMap().get("name");
			softAssert.assertEquals(Actualproductname,Expectedproductname,"Product Names are not matching");
			System.out.println("Actual Product Name:  " + Actualproductname);

			
			String Actualproductdescription = jp.getString("description");
			String Expectedproductdescription=updatePayloadMap().get("description");
			softAssert.assertEquals(Actualproductdescription,Expectedproductdescription,"Product description is not matching");
			System.out.println("Actual Product description:  " + Actualproductdescription);
			
			String ActualproductPrice = jp.getString("price");
			String Expectedproductprice=updatePayloadMap().get("price");
			softAssert.assertEquals(ActualproductPrice,Expectedproductprice,"Product Price is not matching");
			System.out.println("Actual Product Price: " + ActualproductPrice);
			
			softAssert.assertAll();
		}
	
	
	
}
