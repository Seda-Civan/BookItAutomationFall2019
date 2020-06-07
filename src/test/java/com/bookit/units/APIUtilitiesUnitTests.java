package com.bookit.units;

import com.bookit.utilities.APIUtilities;
import com.bookit.utilities.Environment;
import org.junit.Assert;
import org.junit.Test;

/**
 * Class with unit tests for APIUtilities class
 * Here we ensure that utilities work fine before using in action
 */
public class APIUtilitiesUnitTests {

    @Test
    public void getTokenTest(){
        String token = APIUtilities.getToken();
        String tokenForStudent = APIUtilities.getToken("student");
        String tokenForTeacher = APIUtilities.getToken("teacher");

        Assert.assertNotNull(token);
        Assert.assertNotNull(tokenForStudent);
        Assert.assertNotNull(tokenForTeacher);
    }

    @Test
    public void testIfUserExist(){
        //we ensure that user does not exist
        int actual = APIUtilities.getUserID("thereisnoemaillikethis@email.com","9856123");
        Assert.assertEquals(-1,actual); //negative test

        //positive test
        int actual2 = APIUtilities.getUserID(Environment.MEMBER_USERNAME,Environment.MEMBER_PASSWORD);
        Assert.assertTrue(actual2 > 0); //if ID is positive -user exists, otherwise it returns -1
    }
}
