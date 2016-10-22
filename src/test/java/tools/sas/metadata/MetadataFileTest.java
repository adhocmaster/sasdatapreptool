package tools.sas.metadata;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class MetadataFileTest {

	@Test
	public void test() throws IOException {
		
		String metaDataFilePath = "D:/eclipseProjects/sasdatapreptool/assets/test_files/sasfiles/ex01_DDF_with_code_flags_custom.csv";
		
		MetadataFile metadataFile = new MetadataFile( metaDataFilePath );
		
		System.out.println( metadataFile );
		
	}

}