package tools.sas.shell;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class ShellFileCreatorTest {

	@Before
	public void setUp() throws Exception {

	}
	
	@Test
	public void testCreateCodeShellFile() {
		
		String DDFFilePath = "D:/eclipseProjects/sasdatapreptool/assets/Example 01 Shell and Validation/ex01_DDF_with_code_flags_custom.csv";
		String outDir = "D:/eclipseProjects/sasdatapreptool/assets/output";
		String suffix = "_CodeShellFile";
		
		try {
			
			new ShellFileCreator().createCodeShellFile( DDFFilePath, outDir, suffix );
			
		} catch ( Exception e ) {
			
			e.printStackTrace();
			
			fail( e.getMessage() );
			
		}
		
	}

}
