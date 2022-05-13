# Getting Started

### Problem Statement
Create a Spring Boot service that exposes a REST API that allows a user to request change for a given bill. The service should assume there are a finite number of coins.

#### Requirements:

  * Available bills are (1, 2, 5, 10, 20, 50, 100)
  * Available coins are (0.01, 0.05, 0.10, 0.25)
  * Start with 100 coins of each type
  * Change should be made by utilizing the least amount of coins
  * API should validate bad input and respond accordingly
  * The service should maintain the state of the coins throughout the transactions
  * Deliver the code with test cases
  * Upload your code to Github and come to interview prepared to walk through code

#### Bonus:
  * Allow for number of coins to be configurable and easily changed
  * Allow for the user to request for the most amount of coins to make change

### Solution Overview
  * Using Spring Boot v2.6.7
  * Created 4 REST APIs with basic validation
  * Spring Boot Test cases for Controller
  * Spring Boot application is running on port 8081 (by default)

### APIs Documentation:

#### GET
  * URL for viewing coin inventory
    * http://localhost:8081/bill-change
  * URL for getting coin change for a given bill (min coins algo)
    * http://localhost:8081/bill-change/{bill}
  * URL for getting max coin change for a given bill
    * http://localhost:8081/bill-change/{bill}?maximize_coins=true
#### POST
  * URL for updating coin inventory
    * http://localhost:8081/bill-change
    
      Request Body:
      [
      {"denomination": 0.10,
      "count": "100"
      }
      ]
    