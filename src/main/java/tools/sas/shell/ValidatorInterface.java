package tools.sas.shell;

import java.util.List;
import java.util.Map;

import com.epam.parso.Column;

public interface ValidatorInterface {
	
	/**
	 * Create a validator against a new metadata file
	 * @param metaDataFilePath
	 */
	//public void create( String metaDataFilePath );
	
	public void initMetaData( String metaDataFilePath );
	
	public List<Map<String, String>> getMetaColumns();

	public List<String> getNotFoundColNames( List<Column> srcColumns );
	
	
	

}
