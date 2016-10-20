Submission Files
================
The submission contains the following:
project/ - The Eclipse project folder
test_files/ - contains an example properties configuration and the sasfiles for testing
JavaClientForSAS.jar - binary build of the Eclipse project
README.txt - this file


Usage
=====
Command-line usage:
java -jar JavaClientForSAS.jar --config </path/to/properties>

where </path/to/properties> is the path to the properties file to be used.


Other Notes
===========
Parso is GPL-licensed open source third-party library which is used to parse the column metadata in the .sas7bdat files.
Github URL: https://github.com/epam/parso/
