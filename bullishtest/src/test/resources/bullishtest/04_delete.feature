#Author: molsi.varghese@gmail.com
#Keywords Summary : functional, delete
@tag 
Feature: Testing DELETE API
  I want to use this template for my feature file

  @tag1
  Scenario: Positive test for delete: correct parameters
    Given I have students in the table
    When I delete with correct parameters
    Then I should get no error with delete
    
  @tag2    
  Scenario Outline:: Negative test for delete: no parameters
    When I call "delete" with no parameters
    Then I should get expected error code "400" and error message "Required request body is missing"   

  @tag3   
  Scenario Outline:: Positive test for delete: extra parameters
    Given I have students in the table
    When I call "delete" with extra parameters
    Then I should get no error with delete 
    
  @tag4        
  Scenario Outline:: Negative test for delete: invalid parameters
    Given I have students in the table  
    When I call "delete" with invalid parameters
    Then I should get expected error code "400" and error message "Validation failed"          