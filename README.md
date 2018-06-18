# api_refactoring
This project is part of Software Engineering class.

--------------------------------------------------
GitHub mining
--------------------------------------------------
1)You need the following things in place before starting:
	i) GoogleApplication credentials to connect to Google BigQuery from your machine
	ii) google.cloud package in python
2) After this, run queryFromBQ.py script. This script takes the query written in query.txt. Make sure you change the location of the output file.
3) The output file will contain a list of repositories in the form of organizationName/projectName OR userName/projectName
4) Use the download.sh script to download the GitHub projects you obtained from the above step.
5) Supply the file which has the list of repos as an input argument to this download script.
6) Make sure you change the paths in download.sh as required. The projects are saved as "organizationName_projectName" OR "userName_projectName" in the specified path.
7) Run build.sh to check which projects are buildable. This script also takes a file as an argument and has the capability to build only those projects mentioned in the file. If not, it will go to the default location in the script and build all projects therein.

--------------------------------------------------
Dynamic Analysis
--------------------------------------------------
1) Firstly, run MethodParser.java in myInstrumentationHelper project
2) Supply the correct path of the API source code on line 147
3) Make source the sourcepath is set correctly to where the source files are present (i.e. the parent src/ directory of the source file)
4) Set all output file name on line 153

===========

5) Download the API source code and create a copy of the same which will serve as the instrumented version
6) Then run API_Instrumentation/InstrumentationScript.ipynb script which does a string rewrite on the API source files (in the copy) so as to insert the extra code
7) Make sure to use the file obtained from MethodParser
8) Set the API and API_new variables to the original and copied folder name

===========

9) Create a new package called myPackage at the topmost level of the instrumented API source code
10) Create a class called myXstreamClass inside that package and be sure to alter the pom.xml file so as to incorporate the dependencies

******Important****** ==> Please refer junit_instrument.zip for the above 2 steps which contains the complete instrumented and buildable version of junit 4.12

******Very Important****** ==> Please change the path of the output directory location where you will be getting the XMLdumps. This is to be changed in myXstreamClass.java in the printFile function. If this is not pointing to the correct location, you will encounter a file not found exception during runtime and the XML dumps will not be created.

11) After obtaining the new jar, place it in the M2 repository

=============


12) Run any project with instrument.sh script. This script runs the maven tests of the project(s) specified in the file which can be given as input to the instrument.sh script. 
*****Important***** ==> The project names should be in the form of orgName_projectName and not orgName/projectName. This script will dump the test results in the executions folder. If you do not have this directory, please create one! If you don't want to do so, please alter the redirection path in the echo statements of the instrument.sh script. The test results for each project will be saved separately in a file called '<projectName>_tests.txt' in the executions folder.

During the run, the XML dumps will be saved in the directory which you mentioned in myXstreamClass.java

13) Calculate the number of tests using the script calculateTests.sh. This script takes a file as an argument which takes the names of the project(s) whose test counts are needed. The name of the project should be orgName_projectName and not orgName/projectName. The script will then read the file (created in step 12 inside the executions folder) of each project mentioned. 

14) Run the countFunctionCalls.sh script which will save the number of function calls in a csv file by reading files from the XML dump folder.

15) Run the script API_Instrumentation/XmlEdits.ipynb which adds a starting and closing xml tag to all XMLDumps. Be sure to change the path so as to where your XML Dumps are saved. Also, correctly mention the source and the destination directory.

16) After this, run the xmlScripting/Result_Parser.ipynb script to obtain refactoring suggestions. The dir variable contains the destination directory (where the modified XMLDumps are located) created in step 15.

P.S.: JakeWharton_picasso2-okhttp3-downloader.zip contains byte code instrumentation. The Agent class is added in the same project, which means you will have to do a mvn install first, get the jar of the project and supply the same jar as an argument '-javaagent:' before running maven tests.
