# charging-station-app

## Purpose
This app uses a microservice architecture to get invoices from customers of various charging stations across the city.
It generates an invoice PDF for a given customer.

## Workflow
- You can input a customer id into the UI and click “Generate Invoice”
- A HTTP Request calls the REST-based API
- The application starts a new data gathering job
- When the data is gathered, it gets send to the PDF generator
- The PDF generator generates the invoice and saves it on the file system
- The UI checks every couple seconds if the invoice is available

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

##