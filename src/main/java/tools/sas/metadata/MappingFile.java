package tools.sas.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.opencsv.CSVReader;

public class MappingFile {

	String filename;
	String path;
	
	long errorLineNo = 0;
	String errorRecord = "";
	long records;
	
	boolean isValidCSV = false;
	

	public String getFilename() {
		return filename;
	}



	public String getPath() {
		return path;
	}



	public long getErrorLineNo() {
		return errorLineNo;
	}



	public String getErrorRecord() {
		return errorRecord;
	}



	public long getRecords() {
		return records;
	}



	@Override
	public String toString() {
		return "MappingFile [filename=" + filename + ", path=" + path + "]";
	}



	public MappingFile( String path ) {
		
		super();
		
		File file = new File( path );
		this.filename = file.getName();
		this.path = path;
		
		process();
		
	}
	
	public boolean isValidFile() {
		

		File file = new File( path );
		
		return file.isFile();
		
		/**
		if ( ! file.isFile() ) {
			
			//logger.error( "Mapping not a valid file: " + absMappingPath );
			
		} else {
			
			//logger.info( "Found mapping file: " + absMappingPath );
			
			
		}
		**/
	}
	
	public boolean isValidCSV() {

		return isValidCSV;
		
	}
	
	/**
	 * Counts records
	 */
	public void process() {
		
		//System.out.println( "processing" + filename );

		try {
			
			CSVReader csvReader = new CSVReader( new FileReader( path ));
			
			isValidCSV = true;
			
			try {
				
				String[] header = csvReader.readNext();
				records = 1;
				// must have header columns
				if ( ! "ORIGINAL_VALUE".equals( header[0].trim() ) || ! "MAPPED_VALUE".equals( header[1].trim() ) ) {
					
					errorLineNo = records;
					errorRecord = StringUtils.join( header, ",");
					isValidCSV = false;
					
					csvReader.close();
					return;
					
				}
				
			} catch (IOException e) {

				errorLineNo = records;
				errorRecord = "no record parsed";
				isValidCSV = false;
				try {
					csvReader.close();
				} catch (IOException e1) {

				}
				return;
				
			}
			
			String[] row = null;
			
			try {
				
				while ( null != ( row = csvReader.readNext() ) ) {
					
					
					if ( "".equals( String.valueOf( row[0] ) ) && "".equals( String.valueOf( row[1] ) ) )  {
						
						continue;
						
					}

					//System.out.println( StringUtils.join( row, "," ) );
					++records;
					
					if ( row.length < 2 || "".equals( row[0] ) || "".equals( row[1] ) ) {

						errorLineNo = records;
						errorRecord = StringUtils.join( row, ",");
						isValidCSV = false;
						csvReader.close();
						return;
						
					}
					
				}
				
			} catch (IOException e) {

				errorLineNo = records;
				errorRecord = "no record parsed";
				isValidCSV = false;
				try {
					csvReader.close();
				} catch (IOException e1) {

				}
				return;

			}
			
			--records; // trimming header
			errorLineNo = 0;
			isValidCSV = true;
			try {
				csvReader.close();
			} catch (IOException e) {

			}
			return;
			
		} catch ( FileNotFoundException e ) {

			return;
		
		}
	}
	
	

}
