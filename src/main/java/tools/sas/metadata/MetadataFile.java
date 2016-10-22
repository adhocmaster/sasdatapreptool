package tools.sas.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

import tools.sas.report.TypeConversion;


public class MetadataFile {

	public final Logger logger = LoggerFactory.getLogger( MetadataFile.class );

	final int colNameIndexOnFile = 1;
	final int colTypeIndexOnFile = 2;
	
	String filename;
	String path;
	// name, type
	Map<String, String> columns;

	public MetadataFile( String path ) throws IOException {
		
		File file = new File( path );
		filename = file.getName();
		this.path = file.getAbsolutePath();
		columns = new HashMap<String, String>();
		
		setColnames();
		
	}

	public String getFilename() {
		return filename;
	}

	public String getPath() {
		return path;
	}
	
	
	
	public Map<String, String> getColumns() {
		return columns;
	}

	@Override
	public String toString() {
		return "MetadataFile [colNameIndexOnFile=" + colNameIndexOnFile + ", colTypeIndexOnFile=" + colTypeIndexOnFile
				+ ", filename=" + filename + ", path=" + path + ", columns=" + columns + "]";
	}

	private void setColnames() throws IOException {
		

		CSVReader csvReader = new CSVReader( new FileReader( path ));
		
		csvReader.readNext(); // discard header row
		
		String[] row = null;
		
		while ( null != ( row = csvReader.readNext() ) ) {
		
			if ( row.length > colTypeIndexOnFile && ! "".equals( row[colNameIndexOnFile].trim() ) ) {
				
				if ( "".equals( row[colTypeIndexOnFile].trim() ) ) {
					
					logger.error( row[colNameIndexOnFile] + " type is empty!" );
					
				}
				columns.put( row[colNameIndexOnFile], row[colTypeIndexOnFile] );
				
			}
			
		}
		
	}
	
	public String getBinaryType( String colName ) {
		
		return TypeConversion.DDFToBinary.get( columns.get( colName ) );
		
	}
	
	
	
}
