package tools.sas.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.parso.CSVMetadataWriter;
import com.epam.parso.Column;
import com.epam.parso.SasFileProperties;
import com.epam.parso.SasFileReader;
import com.epam.parso.impl.CSVMetadataWriterImpl;
import com.epam.parso.impl.SasFileReaderImpl;

public class BinaryFile {

	public final Logger logger = LoggerFactory.getLogger( BinaryFile.class );
	
	String filename;
	String path;
	long records;
	long rowCounts;
	// name, type
	Map<String, String> columns;
	
	private BinaryFile( String path ) {
		
		File file = new File( path );
		filename = file.getName();
		this.path = file.getAbsolutePath();
		columns = new HashMap<String, String>();
	}
	
	public String getFilename() {
		return filename;
	}

	public String getPath() {
		return path;
	}

	public long getRecords() {
		return records;
	}

	public Map<String, String> getColumns() {
		return columns;
	}


	public long getRowCounts() {
		return rowCounts;
	}


	@Override
	public String toString() {
		return "BinaryFile [filename=" + filename + ", path=" + path + ", records=" + records + ", rowCounts="
				+ rowCounts + ", columns=" + columns + "]";
	}



	public static BinaryFile load( String path ) throws IOException {
		
		File file = new File(path);
		
		if( !file.isFile() )
			throw new IllegalArgumentException( "Not a file: " + path );
		
		
		BinaryFile binaryFile = new BinaryFile( path );
		
		binaryFile.process();
		
		return binaryFile;
		
	}
	
	public void process() throws IOException {

		logger.info( "Before processing binary file: " + filename );
		
		logger.info( toString() );

		InputStream is = new FileInputStream( path );
		SasFileReader sasFileReader = new SasFileReaderImpl(is );
//        Writer writer = new StringWriter();
//        CSVMetadataWriter csvMetadataWriter = new CSVMetadataWriterImpl(writer);
//        csvMetadataWriter.writeMetadata(sasFileReader.getColumns());
        
        List<Column> sasCols = sasFileReader.getColumns();
        
        for( Column sasCol: sasCols ) {
        	
        	columns.put( sasCol.getName(), sasCol.getType().getSimpleName() );
        	
        }
        
        SasFileProperties sasFileProperties = sasFileReader.getSasFileProperties();
        
        rowCounts = sasFileProperties.getRowCount();
        
        //Object[][] rowObjects = sasFileReader.readAll();
        
//        records = rowObjects.length;
        
        records = rowCounts;
        
        logger.info( "After processing binary file: " + filename );
		
        logger.info( toString() );
        
	}

}
