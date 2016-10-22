package tools.sas.metadata;

import java.io.File;

public class MappingFile {

	String filename;
	String path;
	
	
	
	@Override
	public String toString() {
		return "MappingFile [filename=" + filename + ", path=" + path + "]";
	}



	public MappingFile( String path ) {
		
		super();
		
		File file = new File( path );
		this.filename = file.getName();
		this.path = path;
		
	}
	
	

}
