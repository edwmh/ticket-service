# ticket-service

Welcome to the WISARD (Walmart Interview Seating and Reservation Details) application

# Dependencies for building: 
* Java SDK 8
* Maven
* Git

# Dependencies for just running the pre-built .jar file
* Java Runtime Environment 8

# Install
* Clone this project
* Navigate to the root directory, type mvn package
* .Jar files will be built/packaged and placed in the "target" directory

# Run
* java -jar target/ticket-application-1.0-SNAPSHOT-jar-with-dependencies.jar

# Guide
* You can run the application without having Git, Maven, or Java SDK installed.  You just need to have the JRE 8 installed and download the [.jar file](https://github.com/edwmh/ticket-service/blob/master/target/ticket-application-1.0-SNAPSHOT-jar-with-dependencies.jar?raw=true) directly and run it using "java -jar" from the command line
* This is a simple, command-line application that will guide and prompt the user for input as necessary.  The application runs continuously until exited.  The state is never saved to disk or a data store
* As an added extra (even though it would be more of an administrative view/function), at the end of each seat reservation, the application will report a list of seats reserved, grouped by email