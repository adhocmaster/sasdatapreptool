package tools.sas.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

import tools.sas.report.TypeConversion;


public class MetadataFile {

	public final Logger logger = LoggerFactory.getLogger( MetadataFile.class );

	final int colNameIndexOnFile = 1;
	final int colTypeIndexOnFile = 2;
	final int colMappingIndexOnFile = 12;
	final int colSnippetIndexOnFile = 13;
	

	boolean isCodeSnippetValid = true;
	long condeSnippetInvalidRow = 0;
	long rows; // rows including header
	
	String filename;
	String sasFolderPath;
	String path;
	// name, type which maps to binary columns. Types are num or char
	Map<String, String> columns;
	
	Map<String, MappingFile> mappingFiles;

	public MetadataFile( String path, String sasFolderPath ) throws IOException {

		this.sasFolderPath = sasFolderPath;
		File file = new File( path );
		filename = file.getName();
		this.path = file.getAbsolutePath();
		columns = new HashMap<String, String>();
		mappingFiles = new HashMap<String, MappingFile>();
		
		parseRows();
		
	}

	public String getFilename() {
		return filename;
	}

	public String getPath() {
		return path;
	}
	
	/**
	 * Must be used after parseRow is completed.
	 * @return
	 */
	public boolean isCodeSnippetValid() {
		return isCodeSnippetValid;
	}

	public Logger getLogger() {
		return logger;
	}

	public long getCondeSnippetInvalidRow() {
		return condeSnippetInvalidRow;
	}

	public long getRows() {
		return rows;
	}

	public String getSasFolderPath() {
		return sasFolderPath;
	}

	public Map<String, MappingFile> getMappingFiles() {
		return mappingFiles;
	}

	/**
	 * Returns binary columns found in metadata file ORIGINAL_VARNAME column. Also gives the types as in metadata formal ( COLUMN ORIGINAL_VARTYPE char or num )
	 * @return
	 */
	public Map<String, String> getColumns() {
		return columns;
	}




	@Override
	public String toString() {
		return "MetadataFile [isCodeSnippetValid=" + isCodeSnippetValid + ", condeSnippetInvalidRow="
				+ condeSnippetInvalidRow + ", rows=" + rows + ", filename=" + filename + ", sasFolderPath="
				+ sasFolderPath + ", path=" + path + ", columns=" + columns + ", mappingFiles=" + mappingFiles + "]";
	}

	/**
	 * 1. Sets the binary column names with are extracted from ORIGINAL_VARNAME column of metadata file
	 * 2. Sets the mapping files
	 * @throws IOException
	 */
	private void parseRows() throws IOException {
		
		isCodeSnippetValid = true;
		rows = 1;
		
		CSVReader csvReader = new CSVReader( new FileReader( path ));
		
		csvReader.readNext(); // discard header row
		
		String[] row = null;
		
		while ( null != ( row = csvReader.readNext() ) ) {
		
			++rows;

			//System.out.println( StringUtils.join( row, ","));
			
			// set binary column names and types. types are not binary 
			if ( row.length > colTypeIndexOnFile && ! "".equals( row[colNameIndexOnFile].trim() ) ) {
				
				if ( "".equals( row[colTypeIndexOnFile].trim() ) ) {
					
					logger.error( row[colNameIndexOnFile] + " type is empty!" );
					
				}
				columns.put( row[colNameIndexOnFile], row[colTypeIndexOnFile] );
				
			}
			// end binary columns
			
			
			// set mapping file.
			try {
				
				String mappingPath = row[ colMappingIndexOnFile ].trim();
				
				if( ! "".equals( mappingPath ) ) {
										
					String absMappingPath = sasFolderPath + "/" + mappingPath;
					
					File file = new File( absMappingPath );

					MappingFile mappingFile = new MappingFile( absMappingPath );
					
					mappingFiles.put( file.getName(), mappingFile ); 
					
				}
				
			} catch ( IndexOutOfBoundsException e ) {
				
				continue;
				
			}
			// end mapping files
			
			//validate open quotes. try to make it invalid
			
			if( isCodeSnippetValid ) {
			
				try {
					
					String snippet = row[ colSnippetIndexOnFile ];
					
					if( ! "".equals( snippet ) ) {
						
						if ( ! canParseCodeSnippet( snippet ) ) {
							
							condeSnippetInvalidRow = rows;
							
							isCodeSnippetValid = false;
							
						}
						
					}
					
				} catch ( Exception e ) {
					
					//ignore
				}
			}
			
			
		} // while ends
		
		
	}
	

	/**
	 * For a binary column name, return binary type which is Simple Java Class Name (Number or String)
	 * @param colName
	 * @return
	 */
	public String getBinaryType( String colName ) {
		
		return TypeConversion.DDFToBinary.get( columns.get( colName ) );
		
	}
	
	private boolean canParseCodeSnippet( String snippet ) {
		
		int doubleCount = StringUtils.countMatches( snippet, '"');
		int singleCount = StringUtils.countMatches( snippet, '\'');
		
		//System.out.println(" Double quotes " + doubleCount + " single quotes " + singleCount );
		
		if (  doubleCount % 2 == 0 &&  singleCount % 2 == 0 )
			return true;
		
		return false;
		
	}
	
	
	
}
