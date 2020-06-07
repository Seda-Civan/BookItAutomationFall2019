package com.bookit.step_definitions;

import com.bookit.pojos.Room;
import com.bookit.utilities.APIUtilities;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


/**
 *
 */
public class APIStepDefinitions {

    private RequestSpecification requestSpecification; //this is what we put in given
    private Response response;         //when - this is where we store response data
    private String token;              //this is what we use for authentication
    private JsonPath jsonPath;         //this is where we store JSON body - get body of response
    private ContentType contentType;   //This is what we use to set up content type - we specify in the given

    // Given authorization token is provided for "teacher"
    @Given("authorization token is provided for {string}")
    public void authorization_token_is_provided_for(String role) {
        token = APIUtilities.getToken(role);
        //token has lifespan for security purpose thats why it can expire
        //api key and basit authentication won't expire so they are not secured!
    }

    @Given("user accepts content type as {string}")
    public void user_accepts_content_type_as(String acceptType) {
        if (acceptType.toLowerCase().contains("json")) {
            contentType = ContentType.JSON;
        } else if (acceptType.toLowerCase().contains("xml")) {
            contentType = ContentType.XML;
        } else if (acceptType.toLowerCase().contains("html")) {
            contentType = ContentType.HTML;
        }
    }

    @When("user sends GET request to {string}")
    public void user_sends_GET_request_to(String path) {
        response = given().accept(contentType).auth().oauth2(token).get(path).prettyPeek();
        //auth indicates that you will specify authentication information, oauth2 is type of authentication,
        //this app uses bearer token that is part of oauth2

        //auth - what, oauth2 - how (denis)
    }

    //Then user should be able to see 18 rooms
    @Then("user should be able to see {int} rooms")
    public void user_should_be_able_to_see_rooms(int expectedNumberOfRooms) {
        //List<?> rooms = response.as(List.class);
        List<?> rooms = response.jsonPath().get();
        //how we can make sure that number of rooms is not limited in payload? are we sure response is not limited ?
        //if not in requirement, then not limited
        Assert.assertEquals(expectedNumberOfRooms, rooms.size());
        //if payload of objects have name, will it change the way we save them in list?
        //we need to specify jsonPath in that case,
    }

    @Then("user verifies that response status code is {int}")
    public void user_verifies_that_response_status_code_is(int expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @Then("user should be able to see all room names")
    public void user_should_be_able_to_see_all_room_names() {
        List<Room> rooms = response.jsonPath().getList("", Room.class); //represent collection of JSON object as collection of java object type of object is room
        //path is empty because we do not have collection name
        rooms.forEach(room -> System.out.println(room.getName()));
        //below is same with lambda
//        for (Room room : rooms) {
//            System.out.println(room.getName());
//        }
    }

    @Then("user payload contains following room names:")
    public void user_payload_contains_following_room_names(List<String> allRoomNames) {
        List<String> roomNames = response.jsonPath().getList("name"); //collect specific property or all entire objects we can do whatever we need
        // here we collect all nanes from payload
        Assert.assertTrue(roomNames.containsAll(allRoomNames));
        //above regular assertions or below hamcrest matcher
        MatcherAssert.assertThat(roomNames, hasItem(in(allRoomNames)));
        //or convert to array -containsInAnyOrder works when you convert it to array
        MatcherAssert.assertThat(roomNames, containsInAnyOrder(allRoomNames.toArray()));
    }

    //regular member not supposed to be able to create users, response will be 403 forbidden
//   When user sends POST request to "/api/students/student" with following information:
    @When("user sends POST request to {string} with following information:")
    public void user_sends_POST_request_to_with_following_information(String path, List<Map<String, String>> dataTable) {
        for(Map<String, String> user: dataTable) {
            System.out.println("User to add :: "+user);
            APIUtilities.ensureUserDoesnotExist(user.get("email"),user.get("password"));
            response = given().queryParams(user).contentType(contentType).auth().oauth2(token).when().post(path).prettyPeek();
        }
    }

    /**
     *  Then user deletes previously added students
     *       | first-name | last-name | email               | password | role                | campus-location | batch-number | team-name      |
     *       | Lesly      | SDET      | lesly2020@email.com | 1111     | student-team-member | VA              | 15           | Online_Hackers |
     *
     */
    @Then("user deletes previously added students")
    public void user_deletes_previously_added_students(List<Map<String, String>> dataTable) {
        for (Map<String, String> row: dataTable){
            int userID = APIUtilities.getUserID(row.get("email"), row.get("password"));
            response = APIUtilities.deleteUserByID(userID);
            response.then().statusCode(204);
        }
    }
}

