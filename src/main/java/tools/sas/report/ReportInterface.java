package tools.sas.report;

import java.util.List;
import java.util.Map;

import com.epam.parso.Column;

public interface ReportInterface {
	
	/**
	 * #1 The name and full path of each binary input file found.
	 * @return
	 */
	public void printBinaryFilePaths();
	
	/**
	 * #2 The number in records in each binary file.
	 * @return
	 */
	public void printRecordNumberForAllBinaryFile();
	
	/**
	 * #3 The name and full path of the metadata file.
	 * @return
	 */
	public void printMetadataFilePath();
	
	/**
	 * #4 A report of column names that are found in the metadata file but not found in the source binary file
	 * @return
	 */
	public void printMisstingColsBinaryFile();

	/**
	 * #5 A report of column types which don’t match between the metadata file and the source binary file.
	 * @param srcColumns
	 * @return
	 */
	public void printMisMatchedCols();
	
	/**
	 * #6 The name and full path of each mapping file identified in the MAPPING_FILE column of the metadata file.
	 * @return
	 */
	public void printMappingFilesPath();
	
	/**
	 * #7 The number of records present in each mapping file.
	 * @return
	 */
	public void printRecordNumberForAllMappingFiles();
	
	/**
	 * #8 A report for each mapping file validating the csv format of the file. Please provide line number and the first invalid record found in each file.
	 * @return filename -> (line # -> record )
	 */
	public void printInvalidMappingFiles();

	/**
	 * #9 Please ensure that there are no open quotes in the in the Code Snippet column. For example, “Example 1 rather than “Example 1”.
	 * @return
	 */
	public void printInvalidCodeSnippets();
	
	/**
	 * #10 The validation process should look for the shell files described in Section 1 above. If there are ‘Y’ values in the IS_TARGET_FIELD_CODE_NAME column 
	 * of the DDF file there should a corresponding shell file for the target file which contains the appropriate code values.
	 * @return
	 */
	public void validateShellAgainstMetaData();
	
	/**
	 * #11 The validation solution should also verify that the names of code fields in shell files match up with the target field names in the metadata document. 
	 * The code fields in the shell document have a prefix of “CODE_” + the actual field name.
	 * @return
	 */
	public List<String> validateShellCodeNames();
	

}
