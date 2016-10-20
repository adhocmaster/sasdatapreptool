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
		
		String DDFFilePath = "E:/projects/R/gates/Example 01 Shell and Validation/ex01_DDF_with_code_flags.csv";
		
		try {
			
			new ShellFileCreator().createCodeShellFile( DDFFilePath );
			
		} catch ( Exception e ) {
			
			e.printStackTrace();
			
			fail( e.getMessage() );
			
		}
		
	}

}
