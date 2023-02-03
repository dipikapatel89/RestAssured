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
public class DeleteOneProductAdvanced {

	String baseURI;
	SoftAssert softAssert;
	String createPayloadPath;
	HashMap<String,String> createPayload;
	HashMap<String,String> deletePayload;
	String firstProductId;
	String deleteProductId;
	
	public DeleteOneProductAdvanced()
	{
		baseURI="https://techfios.com/api-prod/api/product";
		softAssert=new SoftAssert();
		createPayloadPath="src\\main\\java\\data\\CreatePayload.json";
		createPayload=new HashMap<String,String>();
		deletePayload=new HashMap<String,String>();
	}
	public Map<String,String> createPayloadMap()
	{
		createPayload.put("name","Amazing Headset 6.0");
		createPayload.put("description","The best Headset for amazing programmers.");
		createPayload.put("price","279");
		createPayload.put("category_id","2");
		createPayload.put("category_name","Electronics");
		return createPayload;
	}
		
	public Map<String,String> deletePayloadMap()
	{
		deletePayload.put("id",deleteProductId);
         
		return deletePayload;
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
		deleteProductId=firstProductId;	
		//softAssert.assertAll();
		
	}
	

	
	@Test(priority=3)
	public void deleteOneProduct() {
		//System.out.println("create payload map: " + createPayloadMap());

		Response response =
			given()
				// .log().all()
				.baseUri(baseURI)
				.header("Content-Type", "application/json; charset=UTF-8")
				.auth().preemptive().basic("demo@techfios.com", "abc123")
				.body(deletePayloadMap()).
			when()
				// .log().all()
				.delete("/delete.php").
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
		softAssert.assertEquals(productMessage,"Product was deleted.","Product message is not matching");
		System.out.println("Product Message:  " + productMessage);
		
		softAssert.assertAll();
	}
	
	@Test(priority=4)
		public void readOneDeletedProduct() {

			
			Response response =
				given()
					// .log().all()
					.baseUri(baseURI)
					.header("Content-Type", "application/json")
					.auth().preemptive().basic("demo@techfios.com", "abc123")
					
					.queryParam("id",deletePayloadMap().get("id")).
				when()
					// .log().all()
					.get("/read_one.php").
				then()
					// .log().all()
					.extract().response();

			int responseStatusCode = response.getStatusCode();
			//	Assert.assertEquals(responseStatusCode, 200);
			softAssert.assertEquals(responseStatusCode, 404,"Response status code is not matching");
			System.out.println("Response Status code: " + responseStatusCode);
		
			String ActualresponseBody = response.getBody().asString();
			System.out.println("Actual Response Body: " + ActualresponseBody);
			
			JsonPath jp = new JsonPath(ActualresponseBody);
			
			String Actualdeletemessage = jp.getString("message");
			String Expecteddeletemessage="Product does not exist.";
			softAssert.assertEquals(Actualdeletemessage,Expecteddeletemessage,"Product messages are not matching");
			System.out.println("Actual Deleted message:  " + Actualdeletemessage);

			
		
			softAssert.assertAll();
		}
	
	
	
}
