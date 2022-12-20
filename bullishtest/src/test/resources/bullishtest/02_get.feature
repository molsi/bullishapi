#Author: molsi.varghese@gmail.com
#Keywords Summary : functional, get
@tag 
Feature: Testing GET API
  I want to use this template for my feature file

  @tag1
  Scenario Outline:: Positive test for get: no parameters
    Given I have students in the table
    When I call "get" with no parameters
    Then I should get no error with get
    
  @tag2
  Scenario: Positive test for get: parameter id
    Given I have students in the table
    When I get the list of students with "id" parameter
    Then I should get no error with get 
    
  @tag3
  Scenario: Positive test for get: parameter studentClass
    Given I have students in the table
    When I get the list of students with "studentClass" parameter
    Then I should get no error with get    
    
  @tag4
  Scenario: Positive test for get: parameter id and studentClass
    Given I have students in the table
    When I get the list of students with "id, studentClass" parameter
    Then I should get no error with get            
    
  @tag5
  Scenario: Negative test for get: value of parameter studentClass is wrong
    Given I have students in the table
    When I get the list of students with wrong "studentClass" parameter
    Then I should get empty list
    
  @tag6
  Scenario: Negative test for get: value of parameter id is wrong
    Given I have students in the table
    When I get the list of students with wrong "id" parameter
    Then I should get empty list    
    
  @tag7
  Scenario: Negative test for get: value of parameter id and studentClass is wrong
    Given I have students in the table
    When I get the list of students with wrong "id, studentClass" parameter
    Then I should get empty list    