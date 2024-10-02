import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import Utils.JSONMethods;
import Utils.Payload;

public class BaseClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Validate if Add place API is working fine with Post method
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String response=given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
//		.body("{\r\n"
//				+ "    \"location\": {\r\n"
//				+ "        \"lat\": -38.383494,\r\n"
//				+ "        \"lng\": 33.427362\r\n"
//				+ "    },\r\n"
//				+ "    \"accuracy\": 50,\r\n"
//				+ "    \"name\": \"Frontline house\",\r\n"
//				+ "    \"phone_number\": \"(+91) 983 893 3937\",\r\n"
//				+ "    \"address\": \"29, side layout, cohen 09\",\r\n"
//				+ "    \"types\": [\r\n"
//				+ "        \"shoe park\",\r\n"
//				+ "        \"shop\"\r\n"
//				+ "    ],\r\n"
//				+ "    \"website\": \"http://google.com\",\r\n"
//				+ "    \"language\": \"French-IN\"\r\n"
//				+ "}")
		
		//OR use .body(Payload.Addplace())--> for calling the method
		.body(Payload.AddPlace())
		.when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
		System.out.println(response);
		JsonPath js= new JsonPath(response);
		String Place_id =js.get("place_id");
		System.out.println(Place_id);
		
		//Update place with new address with PUT
		
		String NewAddress= "70 Summer walk, USA";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				//+ "\"place_id\":\"9d015fba514a169f78fa90fa03cb1292\",\r\n" -->we are replacing with Place_id
				+ "\"place_id\":\""+Place_id+"\",\r\n"
				//+ "\"address\":\""+70 Summer walk, USA\",\r\n"--> We are replacing with NewAddress
				+ "\"address\":\""+NewAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		
		//GET API Method
		
		String ResponseValidation=given().log().all().queryParam("key", "qaclick123").queryParam("place_id", Place_id )
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		
//		String ResponseValidation2=given().log().all().queryParam("key", "qaclick123").queryParam("place_id", Place_id )
//				.when().get("maps/api/place/get/json")
//				.then().log().all().assertThat().statusCode(200)
//				.extract().response().asString(); --> Used For Git
	
		JsonPath js1=JSONMethods.JSON(ResponseValidation);
		String Actualaddress=js1.getString("address");
		System.out.println(Actualaddress);
		Assert.assertEquals(Actualaddress,NewAddress);
	
		
		
	}

}
