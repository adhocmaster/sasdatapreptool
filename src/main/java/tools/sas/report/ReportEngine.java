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
import tools.sas.metadata.MetadataFile;
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
		logger.info( "Generating primary stats");
		
		stats = new Stats();
		stats.generate( sasFolderPath );
		
		metadataFile =  new MetadataFile( metaDataFilePath );
		
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

	public Map<String, String> getMappingFilesPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Long> getRecordNumberForAllMappingFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Map<Long, String>> getInvalidMappingFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getInvalidCodeSnippets() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> validateShellAgainstMetaData() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> validateShellCodeNames() {
		// TODO Auto-generated method stub
		return null;
	}

}
