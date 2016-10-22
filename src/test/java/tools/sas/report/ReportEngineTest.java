package tools.sas.report;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import topcoder.gates.tools.sas.prep.App;

public class ReportEngineTest {

	@Test
	public void test() throws IOException {

		
		String sasFolderPath = "D:/eclipseProjects/sasdatapreptool/assets/test_files/sasfiles";
		String metaDataFilePath = "D:/eclipseProjects/sasdatapreptool/assets/test_files/sasfiles/ex01_DDF_with_code_flags_custom.csv";
		String shellSuffix = "_CodeShellFile";
		
		ReportEngine engine = new ReportEngine( sasFolderPath, metaDataFilePath, shellSuffix );
		engine.start();
		
	}

}
