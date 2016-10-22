package topcoder.gates.tools.sas.prep;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;

import com.epam.parso.CSVDataWriter;
import com.epam.parso.CSVMetadataWriter;
import com.epam.parso.SasFileReader;
import com.epam.parso.impl.CSVDataWriterImpl;
import com.epam.parso.impl.CSVMetadataWriterImpl;
import com.epam.parso.impl.SasFileReaderImpl;

/**
 * Hello world!
 *
 */
public class AppBinary 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        InputStream is;
		try {
			
			String filepath = "D:/eclipseProjects/sasdatapreptool/assets/test_files/sasfiles/ex01_visit.sas7bdat";
			is = new FileInputStream( filepath );
			
			SasFileReader sasFileReader = new SasFileReaderImpl(is );
	        Writer writer = new StringWriter();
	        CSVMetadataWriter csvMetadataWriter = new CSVMetadataWriterImpl(writer);
	        csvMetadataWriter.writeMetadata(sasFileReader.getColumns());
	        
	        System.out.println( "Metadata: ");
	        System.out.println( writer.toString() );

	        //File outFile = new File( "E:/sasDataVisit.csv" );

	        //writer = new FileWriter(outFile);
	        writer = new StringWriter();
	        CSVDataWriter csvDataWriter = new CSVDataWriterImpl(writer);
	        csvDataWriter.writeColumnNames(sasFileReader.getColumns());
	        //csvDataWriter.writeRowsArray(sasFileReader.getColumns(), sasFileReader.readAll());

	        System.out.println( "Columns: ");
	        System.out.println( writer.toString() );
	        writer.flush();

	        writer.close();
	        

//			filepath = "D:/eclipseProjects/sasdatapreptool/assets/test_files/sasfiles/ex01_subj.sas7bdat";
//			is = new FileInputStream( filepath );
//			
//			sasFileReader = new SasFileReaderImpl(is );
//	        writer = new StringWriter();
//	        csvMetadataWriter = new CSVMetadataWriterImpl(writer);
//	        csvMetadataWriter.writeMetadata(sasFileReader.getColumns());
//
//	        
//	        //System.out.println( writer.toString() );
//
//	        outFile = new File( "E:/sasDataSubj.csv" );
//	        
//	        //writer = new StringWriter();
//	        
//	        writer = new FileWriter(outFile);
//	        csvDataWriter = new CSVDataWriterImpl(writer);
//	        csvDataWriter.writeColumnNames(sasFileReader.getColumns());
//	        csvDataWriter.writeRowsArray(sasFileReader.getColumns(), sasFileReader.readAll());
//	        
//	        writer.flush();
//	        
//	        writer.close();
	  
	       // System.out.println( writer.toString() );
	        
	        
	        
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
