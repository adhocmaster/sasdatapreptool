package adhocmaster.sas.parser;

import com.epam.parso.SasFileReader;

public class MetaParser {
	
	private SasFileReader sasFileReader;
	
	/*
	 * Inject reader
	 */
	private MetaParser( SasFileReader sasFileReader ) {
		
		this.sasFileReader = sasFileReader;
		
	}
	
	/**
	 * Validate reader before creating a parser object
	 * @param sasFileReader
	 * @return
	 */
	public static MetaParser create( SasFileReader sasFileReader ) {
		
		if ( null == sasFileReader ) {
			
			throw new IllegalArgumentException( "reader cannot be null" );
			
		}
		
		return new MetaParser( sasFileReader );
		
	}
	
	public void getColumns() {
		
		sasFileReader.getColumns();
		
	}

}
