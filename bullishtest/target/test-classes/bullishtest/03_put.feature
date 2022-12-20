#Author: molsi.varghese@gmail.com
#Keywords Summary : functional, put
@tag 
Feature: Testing PUT API
  I want to use this template for my feature file

  @tag1
  Scenario: Positive test for put: all parameters
    Given I have students in the table
    When I edit the list of students with all parameters
    Then I should get no error with put

  @tag2   
  Scenario Outline:: Positive test for put: extra parameters
    Given I have students in the table
    When I call "put" with extra parameters
    Then I should get no error with put 
    
  @tag3   
  Scenario Outline:: Positive test for put: less parameters
    Given I have students in the table
    When I call put with less parameters
    Then I should get no error with put 

  @tag4    
  Scenario Outline:: Negative test for put: no parameters
    When I call "put" with no parameters
    Then I should get expected error code "400" and error message "Required request body is missing"
    
  @tag5        
  Scenario Outline:: Negative test for put: invalid parameters
    Given I have students in the table  
    When I call "put" with invalid parameters
    Then I should get expected error code "400" and error message "Validation failed"           