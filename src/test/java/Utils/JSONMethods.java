package Utils;

import io.restassured.path.json.JsonPath;

public class JSONMethods {
	public static JsonPath JSON(String Response)
	{
		JsonPath js1= new JsonPath(Response);
		return js1;

	}

}
