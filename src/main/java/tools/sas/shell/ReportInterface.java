package tools.sas.shell;

import java.util.Map;

public interface ReportInterface {
	
	/**
	 * #1
	 * @return
	 */
	public Map<String, String> getBinaryFilePaths();
	/**
	 * #2
	 * @return
	 */
	public Map<String, Long> getRecordNumberForAllBinaryFile();
	
	/**
	 * #3
	 * @return
	 */
	public Map<String, String> getMetadataFilePath();

	/**
	 * #6
	 * @return
	 */
	public Map<String, String> getMappingFilesPath();
	
	/**
	 * #7
	 * @return
	 */
	public Map<String, Long> getRecordNumberForAllMappingFiles();
	
	/**
	 * #8
	 * @return filename -> (line # -> record )
	 */
	public Map<String, Map<Long, String>> getInvalidMappingFiles();
	

}
