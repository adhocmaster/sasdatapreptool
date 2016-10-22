package tools.sas.metadata;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class MetadataFileTest {

	@Test
	public void test() {

		String sasFolderPath = "D:/eclipseProjects/sasdatapreptool/assets/test_files/sasfiles";
		String metaDataFilePath = "D:/eclipseProjects/sasdatapreptool/assets/test_files/sasfiles/ex01_DDF_with_code_flags_custom.csv";
		
		try {

			MetadataFile metadataFile = new MetadataFile( metaDataFilePath, sasFolderPath );
			System.out.println( metadataFile );
			
		} catch ( Exception e ) {
			
			e.printStackTrace();
			fail( e.getMessage());
		}
		
		
	}

}
