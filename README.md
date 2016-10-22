# sasdatapreptool
Sas data prep tool ( Topcoder, Gates Foundation )

# To run

java -jar SASPrepTool.jar --config absoulte_path_to_config_file

- Sample config file is given as config.properties file.

- Log file can be found in the jar folder upon quitting (app.log).

- Log also written to console.

# To compile

Go to source package where pom.xml can be found. Open console in this folder and then issue this command:

mvn package -Dmaven.test.skip=true

This command creates the jar without running UNIT tests. If you want to run UNIT tests, you will need to replace the hardcoded paths in each test file before packaging as jar with following command:
mvn package

In your target folder, you will get "SASPrepTool.jar" which is a executable fat jar and can be run standalone.
