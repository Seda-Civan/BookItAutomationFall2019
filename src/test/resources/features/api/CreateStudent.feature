@api @create_student
Feature: Create student

  @create_student_1
  Scenario: 1. Create student as a team member and verify status code 403
    Given authorization token is provided for "team member"
    And user accepts content type as "application/json"
    When user sends POST request to "/api/students/student" with following information:
      | first-name | last-name | email                  | password | role                | campus-location | batch-number | team-name      |
      | studenta   | McDonald  | whoisfromb15@email.com | 1111     | student-team-member | VA              | 15           | Online_Hackers |
    And user verifies that response status code is 403
#    team member not allowed to create student, so response status code is 403

  @create_student_2
  Scenario: 2. Create student as a teacher and verify status code 201
    Given authorization token is provided for "teacher"
    And user accepts content type as "application/json"
    When user sends POST request to "/api/students/student" with following information:
      | first-name | last-name | email                  | password | role                | campus-location | batch-number | team-name      |
      | studenta   | SDET      | whoisfromb15@email.com | 1111     | student-team-member | VA              | 15           | Online_Hackers |
    And user verifies that response status code is 201
#    Then user deletes previously added students
#      | first-name | last-name | email                  | password | role                | campus-location | batch-number | team-name      |
#      | Lesly      | SDET      | whoisfromb15@email.com | 1111     | student-team-member | VA              | 15           | Online_Hackers |
#above last step is handled in APIUtilities class, in below method: when we add user -send post request- in step definitions;
#  we just called ensureUserDoesnotExist method and deleted user if already exists
#  we created user-then delete because on 2. time if user exists, test will fail. same user can not exist two times.
#  So not after, before adding user we needed to delete and we decided to do this with utilities method, not in features file
#       /**
#     * Use this method before creating new user
#     * if user exist - this method will delete that user
#     * @param email
#     * @param password
#     */
#    public static void ensureUserDoesnotExist(String email, String password) {
#       int userID = getUserID(email, password);
#       // condition is true, if userID is positive value
#        // there is no users with 0 or negative id
#       if(userID > 0){
#           deleteUserByID(userID);
#       }
#    }
#    we can add only one student
#  so to resolve this issue, we can delete added student at the end of the test

#  ui and api mixed
#  we have before hook with same tag-@ui, it will activated for scenarios that have same tag
#  @db calls hooks class, ad that class will make connection with database
  @create_student_3 @ui @db
  Scenario: 2. Create student over API and ensure that student info is correct on the UI
    Given authorization token is provided for "teacher"
    And user accepts content type as "application/json"
    When user sends POST request to "/api/students/student" with following information:
      | first-name | last-name | email                  | password | role                | campus-location | batch-number | team-name      |
      | Lesly      | SDET      | whoisfromb15@email.com | 1111     | student-team-member | VA              | 15           | Online_Hackers |
    And user verifies that response status code is 201
    And user is on the landing page
    When user logs in with "whoisfromb15@email.com" email and "1111" password
    And user navigates to personal page
    Then user verifies that information displays correctly:
      | first-name | last-name | email                  | password | role                | campus-location | batch-number | team-name      |
      | Lesly      | SDET      | whoisfromb15@email.com | 1111     | student-team-member | VA              | 15           | Online_Hackers |