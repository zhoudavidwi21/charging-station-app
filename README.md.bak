# charging-station-app
[Github Link](https://github.com/zhoudavidwi21/charging-station-app)

- *Here as a seperate link as well:* (https://github.com/zhoudavidwi21/charging-station-app) 


## Setup/Installation
To set this application up you need to just simply download and extract the zip.
Make sure you have the `docker-compose up` running. 
> **_NOTE:_**  The docker files may or may not be included in this repository. (@lecturer: Probably is included in the zip)

You will need to open the JavaFX folder in your IDE and start the application.
You will need to open the SpringBootApp folder in your IDE and start the application.

For the other services you can try using the backend.bat (on Windows), which should automatically open the backend services.
If that does not work you can either: 
- a.) Open each service *(Data_Collection_Dispatcher, Data_Collection_Receiver, Station_Data_Collector, PDF_Generator)* folder in your IDE and start the application.
- b.) Try opening the executable jar files in the *Apps* folder with `java -jar ./<service>.jar`, or via double click
	

## Purpose
This app uses a microservice architecture to get invoices from customers of various charging stations across the city.
It generates an invoice PDF for a given customer.


## Workflow
- You can input a customer id into the UI and click *Create Invoice*
- A HTTP Request calls the REST-based API
- The application starts a new data gathering job
- When the data is gathered, it gets sent to the PDF generator
- The PDF generator generates the invoice and saves it on the file system
- Upon clicking *Show Status* it shows various information on the response and displays a download link to the PDF.


## Specifications
This a short overview of the specifications for the application. For more detailed specifications: https://moodle.technikum-wien.at/mod/page/view.php?id=1286607
The app consists of 6 different services.

For the frontend we have a JavaFX App, where you can specify which for which customer you want to get the invoice PDF.

Next we have a Spring Boot REST API which offers two endpoints:
	- POST '/invoices/<customer-id>' endpoint, which starts the data gathering process.
	- GET '/invoices/<customer-id>' endpoint, which gets the PDF file from the file system.
	
Afterwards we have 4 seperate services in the backend.
These are: 
	- Data Collection Dispatcher: Starts the data gathering job
	- Station Data Collector: Gathers data for a specific customer from a specific charging station
	- Data Collecton Receiver: Receives all collected data and sends it to the PDF Generator
	- PDF Generator: Generates the PDF from the data and saves it to the file system
	
The REST API and the services in the backend communicate via a messaging queue (rabbitMQ).


### UML Diagram
To further demonstrate the data flow we have shown a rough reprensentation of our application as an UML Diagram.

![UML Diagram displaying the communication and data flow between the different services](./Documents/UML_FuelStation.png)


## User Guide
How to use the application:
Enter a customerId (1, 2, 3) and click *Create Invoice* which should start the data gathering process, and save a PDF file on your local system.
After a few seconds you can click *Show Status* which will display if the request has been successful or not.
It will show the path of the invoice pdf, as well as creation time and for which customerId.

In case the customerId does not exist or is invalid, no invoice will be available.

To stop the application simply close every service that has been started.
> **_NOTE:_**  *Close App* in the JavaFX application will only close the JavaFX app, not the other services running in the background


## Responsibilities
JavaFX - Martin Frischmann

SpringBootApplication/REST - Can Oezalp

Backend Services - David Zhou

Unit Tests - Can Oezalp


## Unit Testing decisions
We have decided to test the repository in Data_Collection_Dispatcher service. Since this is the start of the whole data flow
it is essential, that it correctly returns all the stations it has as well as returns the correct number of stations.

Additionally we have tested the *isNumeric* method in DataCollectionDispatcherApp. Which is there to ensure we are receiving a number as the customerId.
For that method we tested the cases: 	- It receives a numeric string (should return true)
										- It receives a non-numeric string (should return false)
										- It receives a mixed string (should return false)