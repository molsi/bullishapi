package bullishtest;

import io.cucumber.java.en.*;

import java.util.*;
import org.json.simple.*;
import static org.junit.jupiter.api.Assertions.*;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.exception.JsonPathException;

public class StepDefinitions {
	
	private static final String BASE_URI = "http://localhost";
	private static final String BASE_PATH = "/studentmgmt";
	private static final int PORT = 9080;
	private static final String[] NATIONALITIES = {"American", "Chinese", "Indian", "South African", "Brazilian"};
	private static final String[] CLASSES = {"English", "French", "Math", "Chemistry", "Social Media"};
	private static final String[] FIRST_NAMES = {"Jodie", "Jane", "John", "Seth", "Bart"};
	private static final String[] LAST_NAMES = {"Foster", "Doe", "Smith", "McFarlane", "Simpson"};	
	private static final int UPPERBOUND = 10000;

	private static Response response;
	private static String jsonString;
	private static int STUDENT_ID;
	private static String STUDENT_CLASS;
	private static String STUDENT_NATIONALITY;
	private static String STUDENT_FIRST_NAME;
	private static String STUDENT_LAST_NAME;
	
	// Method to initialize RestAssured
	private void beforeEverything() {
    	RestAssured.baseURI = BASE_URI;
    	RestAssured.basePath= BASE_PATH;
    	RestAssured.port = PORT;   		
	}

	// Method to generate random parameters for RestAssured
    private Map<String, Object> get_parameters() {
    	Random rand = new Random(); 
        int id = rand.nextInt(UPPERBOUND); 
        
    	Random random = new Random(); 
        int index = rand.nextInt(NATIONALITIES.length); 
        
        String nationality = NATIONALITIES[index];
        String studentClass = CLASSES[index];
        String firstName = FIRST_NAMES[index];
        String lastName = LAST_NAMES[index];
        
    	return new HashMap<String, Object>() {{
    		put("firstName", firstName);
    		put("lastName", lastName);
    		put("nationality", nationality);
    		put("studentClass", studentClass);
    		put("id", id);
    	}};
    }
	
    @Given("I have students in the table")
    public void i_have_students_in_the_table() {
    	i_post_with_valid_parameters();
    }
    
    @When("I get the list of students with wrong {string} parameter")
    public void i_get_the_list_of_students_with_wrong_parameter(String parameter) {
    	beforeEverything();
    	
    	RequestSpecification request = RestAssured.given();
    	request.header("Content-Type", "application/json");

    	if(parameter.equals("id")) {
            request.queryParam("id", UPPERBOUND+1);
        } else if (parameter.equals("studentClass")) {
            request.queryParam("studentClass", "NotAClass");
        } else if (parameter.contains("id") && parameter.contains("studentClass")) {
            request.queryParam("id", UPPERBOUND+1);
            request.queryParam("studentClass", "NotAClass");
        }

    	response = request.when().get("/fetchStudents");
    }
    
    @When("I get the list of students with {string} parameter")
    public void i_get_the_list_of_students_with_parameter(String parameter) {
    	beforeEverything();
    	
    	RequestSpecification request = RestAssured.given();
    	request.header("Content-Type", "application/json");
    	
    	if(parameter.equals("id")) {
            request.queryParam("id", STUDENT_ID);
        } else if (parameter.equals("studentClass")) {
            request.queryParam("studentClass", STUDENT_CLASS);
        } else if (parameter.contains("id") && parameter.contains("studentClass")) {
            request.queryParam("id", STUDENT_ID);
            request.queryParam("studentClass", STUDENT_CLASS);
        }
    	
    	response = request.when().get("/fetchStudents");
    }
    
    @Then("I should get no error with get")
    public void i_should_get_no_error_with_get() {
    	jsonString = response.asString();
		List<Map<String, String>> list = JsonPath.from(jsonString).get();
		assertEquals(200, response.getStatusCode());
		assertTrue(list.size() > 0);
		
		for(Map<String, String> entry : list) {
			assertNotNull(entry.get("id"));
			assertTrue(!entry.get("firstName").isEmpty());
			assertTrue(!entry.get("lastName").isEmpty());
			assertTrue(!entry.get("studentClass").isEmpty());
			assertTrue(!entry.get("nationality").isEmpty());
		}
    }
    
    @Then("I should get empty list")
    public void i_should_get_an_empty_list() {
    	jsonString = response.asString();
		List<Map<String, String>> list = JsonPath.from(jsonString).get();
		assertEquals(200, response.getStatusCode());
		assertTrue(list.size() == 0);
    }
    
    @When("I post with valid parameters")
    public void i_post_with_valid_parameters() {
    	beforeEverything();
    	
    	Map<String, Object> map = get_parameters();
        JSONObject requestParams = new JSONObject(); 
        requestParams.put("firstName", map.get("firstName")); 
        requestParams.put("lastName", map.get("lastName")); 
        requestParams.put("nationality", map.get("nationality")); 
        requestParams.put("studentClass", map.get("studentClass")); 
        requestParams.put("id", map.get("id")); 
        
        STUDENT_ID = (int)map.get("id");
        STUDENT_CLASS = (String)map.get("studentClass");
        STUDENT_FIRST_NAME = (String)map.get("firstName");
        STUDENT_LAST_NAME = (String)map.get("lastName");
        STUDENT_NATIONALITY = (String)map.get("nationality");
    	
    	RequestSpecification request = RestAssured.given();
    	request.header("Content-Type", "application/json");
    	request.body(requestParams.toJSONString());
    	response = request.when().post("/addStudent");	
    }    
    
    @When("I call {string} with no parameters")
    public void i_post_put_with_no_parameters(String api) {
    	beforeEverything();
    	
    	RequestSpecification request = RestAssured.given();
    	request.header("Content-Type", "application/json");
    	if(api.equals("post")) {
    		response = request.when().post("/addStudent");	
    	} else if(api.equals("put")){
    		response = request.when().put("/updateStudent");	
    	} else if(api.equals("delete")){
    		response = request.when().delete("/deleteStudent");
    	} else {
    		response = request.when().get("/fetchStudents");
    	}
    }
    
    @When("I call {string} with extra parameters")
    public void i_post_put_with_extra_parameters(String api) {
    	beforeEverything();
    	
    	Map<String, Object> map = get_parameters();
        JSONObject requestParams = new JSONObject(); 
        requestParams.put("firstName", map.get("firstName")); 
        requestParams.put("lastName", map.get("lastName")); 
        requestParams.put("nationality", map.get("nationality")); 
        requestParams.put("studentClass", map.get("studentClass")); 
        if(api.equals("post")) {
        	requestParams.put("id", map.get("id")); 
        } else if(api.equals("put")){
        	STUDENT_FIRST_NAME = (String)map.get("firstName");
        	STUDENT_LAST_NAME = (String)map.get("lastName");
        	STUDENT_NATIONALITY = (String)map.get("nationality");
        	STUDENT_CLASS = (String)map.get("studentClass");
        	requestParams.put("id", STUDENT_ID); 
        } else if(api.equals("delete")) {
        	requestParams.put("id", STUDENT_ID); 
        }
        requestParams.put("extra", "ABC");        
    	
    	RequestSpecification request = RestAssured.given();
    	request.header("Content-Type", "application/json");
    	request.body(requestParams.toJSONString());
    	if(api.equals("post")) {
    		response = request.when().post("/addStudent");	
    	} else {
    		response = request.when().put("/updateStudent");	
    	}
    }
    
    @When("I call {string} with invalid parameters")
    public void i_post_with_invalid_parameters(String api) {
    	beforeEverything();
    	
    	Map<String, Object> map = get_parameters();
        JSONObject requestParams = new JSONObject(); 
        requestParams.put("firstName", map.get("firstName")); 
        requestParams.put("lastName", map.get("lastName")); 
        requestParams.put("nationality", map.get("nationality")); 
        requestParams.put("studentClass", map.get("studentClass")); 
        requestParams.put("idNonexistant", map.get("id")); 
        
    	RequestSpecification request = RestAssured.given();
    	request.header("Content-Type", "application/json");
    	request.body(requestParams.toJSONString());
    	
    	if(api.equals("post")) {
    		response = request.when().post("/addStudent");	
    	} else if(api.equals("put")){
    		response = request.when().put("/updateStudent");	
    	} else {
    		response = request.when().delete("/deleteStudent");	
    	}
    }
    
    
    @When("I post with lesser parameters")
    public void i_post_with_lesser_parameters() {
    	beforeEverything();
    	
    	Map<String, Object> map = get_parameters();
        JSONObject requestParams = new JSONObject(); 
        requestParams.put("firstName", map.get("firstName")); 
        requestParams.put("lastName", map.get("lastName")); 
        requestParams.put("nationality", map.get("nationality")); 
        requestParams.put("studentClass", map.get("studentClass")); 
    	
    	RequestSpecification request = RestAssured.given();
    	request.header("Content-Type", "application/json");
    	request.body(requestParams.toJSONString());
    	response = request.when().post("/addStudent");	
    }
    
    @When("I post with duplicate parameters")
    public void i_post_with_duplicate_parameters() {
    	beforeEverything();
        
    	Map<String, Object> map = get_parameters();
        JSONObject requestParams = new JSONObject(); 
        requestParams.put("firstName", map.get("firstName")); 
        requestParams.put("lastName", map.get("lastName")); 
        requestParams.put("nationality", map.get("nationality")); 
        requestParams.put("studentClass", map.get("studentClass")); 
        requestParams.put("id", map.get("id")); 
    	
    	RequestSpecification request = RestAssured.given();
    	request.header("Content-Type", "application/json");
    	request.body(requestParams.toJSONString());
    	response = request.when().post("/addStudent");	
    	response = request.when().post("/addStudent");	
    }
    
    @Then("I should get expected error code {string} and error message {string}")
    public void i_should_get_expected_error(String errorCode, String expectedError) {
    	jsonString = response.asString();
    	try { 
			Map<String, String> list = JsonPath.from(jsonString).get();
	    	assertEquals(errorCode, response.getStatusCode()+"");
	    	assertTrue(list.get("message").contains(expectedError));
    	} catch (JsonPathException e) {
    		assertTrue(jsonString.contains(expectedError));
    	}
    }
    
    @Then("I should get no error with post")
    public void i_should_get_no_error_with_post() {
    	jsonString = response.asString();
    	assertEquals(200, response.getStatusCode());
    	assertTrue(jsonString.contains("New student enrolled with student id :"));
    }
    
    
    @When("I edit the list of students with all parameters")
    public void i_edit_the_list_of_students_with_all_parameters() {
    	beforeEverything();
        
        JSONObject requestParams = new JSONObject(); 
        STUDENT_FIRST_NAME = STUDENT_FIRST_NAME+"aa";
        STUDENT_LAST_NAME = STUDENT_FIRST_NAME+"bb";
        STUDENT_NATIONALITY = STUDENT_FIRST_NAME+"cc";
        STUDENT_CLASS = STUDENT_FIRST_NAME+"dd";
        requestParams.put("id", STUDENT_ID); 
        requestParams.put("firstName", STUDENT_FIRST_NAME); 
        requestParams.put("lastName", STUDENT_LAST_NAME); 
        requestParams.put("nationality", STUDENT_NATIONALITY); 
        requestParams.put("studentClass", STUDENT_CLASS); 
    	
    	RequestSpecification request = RestAssured.given();
    	request.header("Content-Type", "application/json");
    	request.body(requestParams.toJSONString());
    	response = request.when().put("/updateStudent");	
    }
    
    @When("I call put with less parameters")
    public void i_call_put_with_less_parameters() {
    	beforeEverything();
        
        JSONObject requestParams = new JSONObject(); 
        STUDENT_FIRST_NAME = STUDENT_FIRST_NAME+"aa";
        STUDENT_LAST_NAME = STUDENT_FIRST_NAME+"bb";
        STUDENT_NATIONALITY = STUDENT_FIRST_NAME+"cc";
        requestParams.put("id", STUDENT_ID); 
        requestParams.put("firstName", STUDENT_FIRST_NAME); 
        requestParams.put("lastName", STUDENT_LAST_NAME); 
        requestParams.put("nationality", STUDENT_NATIONALITY); 
    	
    	RequestSpecification request = RestAssured.given();
    	request.header("Content-Type", "application/json");
    	request.body(requestParams.toJSONString());
    	response = request.when().put("/updateStudent");	
    }    
    
    @Then("I should get no error with put")
    public void i_should_get_no_error_with_put() {
    	jsonString = response.asString();
    	assertEquals(200, response.getStatusCode());
    	
		Map<String, String>map = JsonPath.from(jsonString).get();
		assertEquals(map.get("id"), STUDENT_ID);
		assertEquals(map.get("firstName"), STUDENT_FIRST_NAME); 
		assertEquals(map.get("lastName"), STUDENT_LAST_NAME); 
		assertEquals(map.get("nationality"), STUDENT_NATIONALITY); 
		assertEquals(map.get("studentClass"), STUDENT_CLASS); 		
    }  
    
    @When("I delete with correct parameters")
    public void i_delete_with_correct_parameters() {
    	beforeEverything();
        
        JSONObject requestParams = new JSONObject(); 
        requestParams.put("id", STUDENT_ID); 
    	
    	RequestSpecification request = RestAssured.given();
    	request.header("Content-Type", "application/json");
    	request.body(requestParams.toJSONString());
    	response = request.when().delete("/deleteStudent");	
    }    
    
    @Then("I should get no error with delete")
    public void i_should_get_no_error_with_delete() {
    	jsonString = response.asString();
    	assertEquals(200, response.getStatusCode());
    }   
    
}
