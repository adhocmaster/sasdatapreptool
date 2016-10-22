package topcoder.gates.tools.sas.prep;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.parso.CSVDataWriter;
import com.epam.parso.CSVMetadataWriter;
import com.epam.parso.SasFileReader;
import com.epam.parso.impl.CSVDataWriterImpl;
import com.epam.parso.impl.CSVMetadataWriterImpl;
import com.epam.parso.impl.SasFileReaderImpl;

import tools.sas.binary.Stats;
import tools.sas.report.ReportEngine;
import tools.sas.shell.ShellFileCreator;

/**
 * Hello world!
 *
 */
public class App 
{
	
	public static final Logger logger = LoggerFactory.getLogger( App.class );
	
	String sasFolderPath;
	String metaDataFilePath;
	String shellSuffix;
	
	
	
    @Override
	public String toString() {
		return "App [sasFolderPath=" + sasFolderPath + ", metaDataFilePath=" + metaDataFilePath + ", shellSuffix="
				+ shellSuffix + "]";
	}

	public static void main( String[] args ) throws Exception
    {

    	
    	// the menu
    	
    	if( null == args || args.length < 2 ) {
    		
    		throw new IllegalArgumentException( "--config filename missing" );
    	}
    	
    	App app = new App();
    	
    	app.loadPropertiesFile( args[1] );
    	
    	while ( true ) {
    		
    		Menu menu = new Menu();
    		
    		menu.showMenu();
    		
    		Scanner scanner = new Scanner( System.in );
    		
    		int operationIndex = scanner.nextInt();
    		
    		if ( 0 == operationIndex )  {

    			logger.info( "Thank you for using it. Good bye!" );
    			break;
    			
    		}
    		
    		
    		switch( operationIndex ) {
    		
    		case 1:

    			logger.info( "You chose operation 1. Starting..." );
    			
    			new ShellFileCreator().createCodeShellFile( app.metaDataFilePath, app.sasFolderPath, app.shellSuffix );
    			
    			logger.info( "Operation 1 ends..." );
    			break;
    			
    		case 2:
    			
    			logger.info( "You chose operation 2. Starting..." );
    			
    			new ReportEngine( app.metaDataFilePath, app.sasFolderPath, app.shellSuffix ).start();
    			
    			logger.info( "Operation 2 ends..." );
    			break;
    			
    		default:

    			logger.info( " You chose operation something which is not implemented yet." );
    		
    		}
    		
    	}
    }
    
    private App() {
    	
    	
    }
    
    public void loadPropertiesFile( String propertiesPath ) throws IOException {
    	
    	logger.info( "Trying to load properties file: " + propertiesPath );
    	
    	InputStream ins = new FileInputStream( propertiesPath );
//    	File file = new File ( propertiesPath );
    	
    	Properties props = new Properties();
    	
    	props.load( ins );

    	ins.close();
    	
    	sasFolderPath = props.getProperty( "sas_folder_path" );
    	metaDataFilePath = props.getProperty( "metadata_file" );
    	shellSuffix = props.getProperty( "shell_suffix" );
    	
    	if( null == shellSuffix ) {
    		
    		logger.info( "Default shell suffix is being used ( _CodeShellFile ).");
    		shellSuffix = "_CodeShellFile";
    		
    	}

    	if ( null ==  sasFolderPath ) {
    		
    		throw new IllegalArgumentException( "sas_folder_path is not found in config file");
    		
    	}
    	
    	if ( null ==  metaDataFilePath ) {
    		
    		throw new IllegalArgumentException( "metadata_file is not found in config file");
    		
    	}
    	
    	File file = new File( sasFolderPath );

    	if ( ! file.exists() ) {

    		throw new IllegalArgumentException( "Directory does not exist: " + sasFolderPath );
    		
    	}
    	if ( ! file.canRead() ) {

    		throw new IllegalArgumentException( "Cannot read: " + sasFolderPath );
    		
    	}
    	if ( ! file.isDirectory()) {

    		throw new IllegalArgumentException( "Not a directory: " + sasFolderPath );
    		
    	}
    	
    	file = new File( metaDataFilePath );
    	
    	if ( ! file.exists() ) {

    		throw new IllegalArgumentException( "file does not exist: " + metaDataFilePath );
    		
    	}
    	
    	if ( ! file.canRead() ) {

    		throw new IllegalArgumentException( "Cannot read: " + metaDataFilePath );
    		
    	}
    	
    	if ( ! file.isFile() ) {

    		throw new IllegalArgumentException( "Not a valid file: " + metaDataFilePath );
    		
    	}
    	
    	logger.info("Loaded properties:");
    	logger.info( toString() );
    	
    	
    }
    
    
}
