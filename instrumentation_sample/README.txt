Steps to instrument:
1) Create the pom.xml as in this directory
2) Write agent code as required
3) Create a jar of the project using the following command:
mvn install
This will create a jar in the target/ directory.
4) Now run the class which has the main function in the following manner:
java -javaagent:target/instrumentation_sample-0.0.1-SNAPSHOT.jar -cp ./lib/javassist-3.14.0-GA.jar sneha.instrumentation_sample.AgentTest