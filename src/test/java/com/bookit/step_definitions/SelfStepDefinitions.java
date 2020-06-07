package com.bookit.step_definitions;

import com.bookit.database.UserDB;
import com.bookit.pages.SelfPage;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class SelfStepDefinitions {

    SelfPage selfPage = new SelfPage();
    UserDB userDB = new UserDB();

    /**
     *        | first-name | last-name | email                  | password | role                | campus-location | batch-number | team-name      |
     *  Row 0 | Lesly      | SDET      | whoisfromb15@email.com | 1111     | student-team-member | VA              | 15           | Online_Hackers |
     *
     */
    @Then("user verifies that information displays correctly:")
    public void user_verifies_that_information_displays_correctly(List<Map<String, String>> dataTable) {
        System.out.println("********** [UI VALIDATIONS] ***********");
        //in ui page, first and last names displays together as full name
        //get(0) == Row(0)
        String fullName = dataTable.get(0).get("first-name") + " " +dataTable.get(0).get("last-name");
        Assert.assertEquals(fullName, selfPage.getUserInfo("name"));
        Assert.assertEquals(dataTable.get(0).get("role"), selfPage.getUserInfo("role"));
        Assert.assertEquals(dataTable.get(0).get("campus-location"), selfPage.getUserInfo("campus"));
        Assert.assertEquals(dataTable.get(0).get("batch-number"), selfPage.getUserInfo("batch").replace("#",""));
        Assert.assertEquals(dataTable.get(0).get("team-name"), selfPage.getUserInfo("team"));

        System.out.println("********** [DATABASE VALIDATIONS] ***********");
        Assert.assertTrue(userDB.checkIfUserExistInDB(dataTable.get(0).get("email")));
    }
}
