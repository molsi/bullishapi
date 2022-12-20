#Author: molsi.varghese@gmail.com
#Keywords Summary : functional, post
@tag 
Feature: Testing POST API
  I want to use this template for my feature file
  
  @tag1
  Scenario: Positive test for post: valid parameters
    When I post with valid parameters
    Then I should get no error with post

  @tag2
  Scenario Outline:: Positive test for post: extra parameters
    When I call "post" with extra parameters
    Then I should get no error with post

  @tag3
  Scenario Outline:: Negative test for post: no parameters
    When I call "post" with no parameters
    Then I should get expected error code "400" and error message "Required request body is missing"

  @tag4        
  Scenario Outline:: Negative test for post: invalid parameters
    When I call "post" with invalid parameters
    Then I should get expected error code "400" and error message "Validation failed"
    
  @tag5
  Scenario Outline:: Negative test for post: lesser parameters
    When I post with lesser parameters
    Then I should get expected error code "400" and error message "Validation failed"
    
  @tag6
  Scenario Outline:: Negative test for post: duplicate parameters
    When I post with duplicate parameters
    Then I should get expected error code "500" and error message "Student already exists"