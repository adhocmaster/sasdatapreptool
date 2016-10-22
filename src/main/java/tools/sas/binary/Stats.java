package tools.sas.binary;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stats {

	public final Logger logger = LoggerFactory.getLogger( Stats.class );
	
	private Map<String, BinaryFile> binaryFiles;
	
	
	
	public Map<String, BinaryFile> getBinaryFiles() {
		
		return binaryFiles;
		
	}

	public Stats() {
		
		binaryFiles = new HashMap<String, BinaryFile>();
	}
	
	public void generate( String sasFolder ) {
		
		String message = "Searching binary files in " + sasFolder ;
		//System.out.println( message );
		logger.info( message );
		
		File dir = new File( sasFolder );
		
		File[] files = dir.listFiles();
		
		for ( File file : files ) {
			
			if ( file.getName().endsWith( ".sas7bdat" ) ) {
				
				logger.info( "Found binary file" + file.getName() );
				
				try {
					
					loadBinaryFile(file);
					
				} catch ( Exception e ) {
					
					logger.error( e.getMessage(), e );
					
				}
				
				
			} else {
				
				logger.info( "Skipping file " + file.getName() );
				
			}
			
		}
		
		
	}
	
	private void loadBinaryFile( File file ) throws IOException {
		
		logger.info( "Loading binary file: " + file.getName() );
		
		BinaryFile binaryFile = BinaryFile.load( file.getAbsolutePath() );
		
		binaryFiles.put( binaryFile.getFilename(), binaryFile );
		
	}
	
	public Map<String, String> getAllCols() {
		
		Map<String, String> cols = new HashMap<String, String>();
		Set<String> names = binaryFiles.keySet();
		
		for( String name: names ) {
			
			BinaryFile binaryFile = binaryFiles.get( name );
			
			cols.putAll( binaryFile.getColumns() );
			
		}
		
		return cols;
		
	}
	

}
