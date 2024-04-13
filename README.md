**Account Management Service AMS**
-----------------

**Description** - This is Account management service AMS to manage Accounts and Fund transfers.

Java and Springboot framework is used to create this AMS application.

Hibernate and Maven are used in this application.

All the  necessary dependencies used in the application are mentioned in pom.xml file.


To run the application locally -
---------------------------------

Step 1 - Build and run the application "AccountManagementServiceApplication" from "src/main/java/com/ams/AccountManagementServiceApplication.java".

Step 2 - H2 embedded database is used in this project and can be accessed through "http://localhost:8081/h2-console/"

Step 3 - To access the database enter the username - "sa" and do not provide password. Provide the correct JDBC url for "mem" Database- "jdbc:h2:mem:account_db" and login.

Step 4 - Go to browser and test the h2 database connection and connect to access the database to check the records in the database.

Step 5 - Go to path "http://localhost:8081/swagger-ui/index.html" on the web browser to access the swagger to test the created APIs.
