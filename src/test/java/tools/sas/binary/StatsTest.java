package tools.sas.binary;

import static org.junit.Assert.*;

import org.junit.Test;

public class StatsTest {

	@Test
	public void test() {

		Stats stats = new Stats();
		
		stats.generate( "D:/eclipseProjects/sasdatapreptool/assets/test_files/sasfiles" );
		
	}

}
