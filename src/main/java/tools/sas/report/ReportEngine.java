package tools.sas.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.parso.Column;

import tools.sas.binary.BinaryFile;
import tools.sas.binary.Stats;
import tools.sas.metadata.MappingFile;
import tools.sas.metadata.MetadataFile;
import tools.sas.shell.ShellFileCreator;
import topcoder.gates.tools.sas.prep.App;

public class ReportEngine implements ReportInterface {

	public static final Logger logger = LoggerFactory.getLogger( ReportEngine.class );

	String sasFolderPath;
	String metaDataFilePath;
	String shellSuffix;
	
	Stats stats;
	MetadataFile metadataFile;


	public ReportEngine(String sasFolderPath, String metaDataFilePath, String shellSuffix) {
		
		super();
		this.sasFolderPath = sasFolderPath;
		this.metaDataFilePath = metaDataFilePath;
		this.shellSuffix = shellSuffix;
		
	}
	
	public void start() throws IOException {

		logger.info( "Starting up reporting engine");

		logger.info( "Processing metadata file" );
		
		metadataFile =  new MetadataFile( metaDataFilePath, sasFolderPath );

		logger.info( "Generating primary stats for binary files...");
		stats = new Stats();
		stats.generate( sasFolderPath );
		
		
		//#1
		printBinaryFilePaths();
		
		//#2
		printRecordNumberForAllBinaryFile();
		
		//#3
		printMetadataFilePath();
		
		//#4
		printMisstingColsBinaryFile();
		
		//#5
		printMisMatchedCols();
		
		//#6
		printMappingFilesPath();
		
		//#7
		printRecordNumberForAllMappingFiles();
		
		//#8
		printInvalidMappingFiles();
		
		//#9
		printInvalidCodeSnippets();
		
		//#10 & #11
		validateShellAgainstMetaData();
	}

	/**
	 * #1 The name and full path of each binary input file found.
	 * @return
	 */
	public void printBinaryFilePaths() {

		logger.info( "---------------------------------------------------------------------------------------------" );
		logger.info( "#1 The name and full path of each binary input file found: " );
		
		Map<String, BinaryFile> binaryFiles = stats.getBinaryFiles();
		
		Set<String> names = binaryFiles.keySet();
		
		for ( String name: names ) {
			
			logger.info( name + " - " + binaryFiles.get( name ).getPath() );
			
		}

	}

	/**
	 * #2 The number in records in each binary file.
	 * @return
	 */
	public void printRecordNumberForAllBinaryFile() {

		logger.info( "---------------------------------------------------------------------------------------------" );
		logger.info( "#2 The number in records in each binary file: " );
		
		Map<String, BinaryFile> binaryFiles = stats.getBinaryFiles();
		
		Set<String> names = binaryFiles.keySet();
		
		for ( String name: names ) {
			
			logger.info( name + " - " + binaryFiles.get( name ).getRecords() );
			
		}
	}

	/**
	 * #3 The name and full path of the metadata file.
	 * @return
	 */
	public void printMetadataFilePath() {

		logger.info( "---------------------------------------------------------------------------------------------" );
		logger.info( "#3 The name and full path of the metadata file: " );
		
		logger.info( metadataFile.getFilename() + " - " + metadataFile.getPath() );
		
	}

	/**
	 * #4 A report of column names that are found in the metadata file but not found in the source binary file
	 * @return
	 */
	public void printMisstingColsBinaryFile() {

		logger.info( "---------------------------------------------------------------------------------------------" );
		logger.info( "#4 A report of column names that are found in the metadata file but not found in the source binary file: " );
		
		Map<String, String> dffCols = metadataFile.getColumns();
		
		Set<String> metaCols = dffCols.keySet();
		List<String> notFound = new ArrayList<String>();
		
		Map<String, String> binaryCols = stats.getAllCols();
		
		Set<String> binaryColNames = binaryCols.keySet();
		
		for( String metaCol : metaCols ) {
			
			if ( ! binaryColNames.contains( metaCol ) ) {
				
				notFound.add( metaCol );
				
			}
			
		}
		
		if( notFound.size() > 0 )
			logger.info( StringUtils.join( notFound, ",") );
		else 
			logger.info( "None" );
		
	}

	/**
	 * #5 A report of column types which don’t match between the metadata file and the source binary file.
	 * @param srcColumns
	 * @return
	 */
	public void printMisMatchedCols() {

		logger.info( "---------------------------------------------------------------------------------------------" );
		logger.info( "#5 A report of column types which don’t match between the metadata file and the source binary file: " );

		Map<String, String> dffCols = metadataFile.getColumns();
		
		Set<String> metaCols = dffCols.keySet();
		
		Map<String, String> mismatch = new HashMap<String, String>();
		
		List<String> mismatchStr = new ArrayList<String>();
		
		Map<String, String> binaryCols = stats.getAllCols();
		
		Set<String> binaryColNames = binaryCols.keySet();
		
		for( String metaCol : metaCols ) {
			
			if ( binaryColNames.contains( metaCol ) && ! binaryCols.get( metaCol ).equals( metadataFile.getBinaryType( metaCol ) )) {
				
				mismatch.put( metaCol,  dffCols.get( metaCol ) + " - " +  TypeConversion.BinaryToDDF.get( binaryCols.get( metaCol ) ) );
				mismatchStr.add( metaCol + ": " + dffCols.get( metaCol ) + "(meta) - " +  TypeConversion.BinaryToDDF.get( binaryCols.get( metaCol ) ) + "(bin)");
				
			}
			
		}
		

		if( mismatchStr.size() > 0 )
			logger.info( StringUtils.join( mismatchStr, ",") );
		else 
			logger.info( "None" );
	
		
		
	}

	/**
	 * #6 The name and full path of each mapping file identified in the MAPPING_FILE column of the metadata file.
	 * @return
	 */
	public void printMappingFilesPath() {

		logger.info( "---------------------------------------------------------------------------------------------" );
		logger.info( "#6 The name and full path of each mapping file identified in the MAPPING_FILE column of the metadata file: " );
		
		Map<String, MappingFile> mappingFiles = metadataFile.getMappingFiles();
		
		Set<String> names = mappingFiles.keySet();
		
		for ( String name : names ) {
			
			MappingFile mappingFile = mappingFiles.get( name );
			
			if ( mappingFile.isValidFile() ) {
				
				logger.info( "Found: " + mappingFile.getFilename() + " - " + mappingFile.getPath() );
				
			} else {
				
				logger.error( "NOT Found: " + mappingFile.getFilename() + " - " + mappingFile.getPath() );
			}
			
		}
		
	}
	
	/**
	 * #7 The number of records present in each mapping file.
	 * @return
	 */
	public  void printRecordNumberForAllMappingFiles() {

		logger.info( "---------------------------------------------------------------------------------------------" );
		logger.info( "#7 The number of records present in each mapping file: " );
		
		Map<String, MappingFile> mappingFiles = metadataFile.getMappingFiles();
		
		Set<String> names = mappingFiles.keySet();
		
		for ( String name : names ) {
			
			MappingFile mappingFile = mappingFiles.get( name );

			if ( ! mappingFile.isValidFile() ) {
				
				logger.error( "NOT Found: " + mappingFile.getFilename() + " - " + mappingFile.getPath() );
				
			
			} else if ( mappingFile.isValidCSV() ) {
				
				logger.info( mappingFile.getFilename() + " - " + mappingFile.getRecords() );
				
			} else {
				
				//logger.error( mappingFile.getFilename() + " line #" + mappingFile.getErrorLineNo() + " " + mappingFile.getErrorRecord() );
			}
			
		}
		
		
	}

	/**
	 * #8 A report for each mapping file validating the csv format of the file. Line number and the first invalid record found in each file.
	 * @return filename -> (line # -> record )
	 */
	public void printInvalidMappingFiles() {

		logger.info( "---------------------------------------------------------------------------------------------" );
		logger.info( "#8 A report for each mapping file validating the csv format of the file. Please provide line number and the first invalid record found in each file: " );

		Map<String, MappingFile> mappingFiles = metadataFile.getMappingFiles();
		
		Set<String> names = mappingFiles.keySet();
		
		for ( String name : names ) {
			
			MappingFile mappingFile = mappingFiles.get( name );

			if ( ! mappingFile.isValidFile() ) {
				
				logger.error( "NOT Found: " + mappingFile.getFilename() + " - " + mappingFile.getPath() );
				
			
			} else if ( ! mappingFile.isValidCSV() ) {

				logger.error( "INVALID: " + mappingFile.getFilename() + " line #" + mappingFile.getErrorLineNo() + " " + mappingFile.getErrorRecord() );
				
			} else {
				
				logger.info( mappingFile.getFilename() + " is VALID ");
				
			}
			
		}
		
		
	}

	/**
	 * #9 Please ensure that there are no open quotes in the in the Code Snippet column. For example, “Example 1 rather than “Example 1”.
	 * @return
	 */
	public void printInvalidCodeSnippets() {

		logger.info( "---------------------------------------------------------------------------------------------" );
		logger.info( "#9 Open quotes validation in the Code Snippet column: " );
		
		if ( metadataFile.isCodeSnippetValid() ) {
			
			logger.info( "Code snippets have no open quotes");
			
		} else {
			
			logger.error( "INVALID: open quotes at line #" + metadataFile.getCondeSnippetInvalidRow() );
			
		}
		
	}

	/**
	 * #10 The validation process should look for the shell files described in Section 1 above. If there are ‘Y’ values in the IS_TARGET_FIELD_CODE_NAME column 
	 * of the DDF file there should a corresponding shell file for the target file which contains the appropriate code values.
	 * #11 The validation solution should also verify that the names of code fields in shell files match up with the target field names in the metadata document. 
	 * The code fields in the shell document have a prefix of “CODE_” + the actual field name.
	 * @return
	 */
	public void validateShellAgainstMetaData() {
		
		logger.info( "---------------------------------------------------------------------------------------------" );
		logger.info( "#10 & #11 Validate Shell file agains DDF (matches column names and data from A-E: " );
		
		ShellFileCreator creator = new ShellFileCreator();
		
		try {
		
			List<String> errors = creator.validate( metaDataFilePath, sasFolderPath, shellSuffix );
			
			if( errors.isEmpty() ) {
				
				logger.info( "Shell files are valid");
				
			} else {
				
				logger.error( StringUtils.join( errors, "\n") );
				
			}
			
		} catch ( Exception e ) {
			
			logger.error( e.getMessage(), e);
			
		}
		
		
	}

	/**
	 * #11 The validation solution should also verify that the names of code fields in shell files match up with the target field names in the metadata document. 
	 * The code fields in the shell document have a prefix of “CODE_” + the actual field name.
	 * @return
	 */
	public List<String> validateShellCodeNames() {
		// TODO Auto-generated method stub
		return null;
	}

}
