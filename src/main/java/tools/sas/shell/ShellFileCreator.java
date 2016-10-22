package tools.sas.shell;

import java.awt.datatransfer.StringSelection;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import topcoder.gates.tools.sas.prep.App;

public class ShellFileCreator {

	public final Logger logger = LoggerFactory.getLogger( App.class );
	
	Set<String> dataTargets;
	Map<String, List<String>> dataTargetHeaders;
	
	private enum FLAG_COL_NAMES {
		
		IS_TARGET_FIELD_CODE_NAME,
		IS_TARGET_FIELD_CODE_VALUE,
		TARGET_DATANAME,
		TARGET_VARNAME;
		
		/**
		 * We will save position of the column names found in the DDF Csv file. 0 based position
		 */
		private int position;
		
		public void setPosition( int position ) {
			
			this.position = position;
			
		}

		public int getPosition() {
			
			return position;
			
		}
		
	}
	
	public ShellFileCreator() {
		
		dataTargets = new HashSet<String>();
		dataTargetHeaders = new HashMap<String, List<String>>();
		
	}
	
	public void createCodeShellFile( String DDFFilePath, String outputDir, String suffix ) throws Exception {
		
		CSVReader csvReader = new CSVReader( new FileReader( DDFFilePath ));
		
		String[] header = null;
		List<String> outputHeader = new ArrayList<String>(); //  columns in Shell file
		
		// 1. Process Header and work out positions of related columns
		if ( null != ( header = csvReader.readNext() ) ) {
			
			//logger.info( StringUtils.join( header, "," ) );

			for ( int position = 0; position < header.length; ++position ) {
				
				String colName = header[ position ];
				
				// if col position is less than 5 ( col A to E )
				if ( position < 5 ) {
					
					outputHeader.add( colName );
					
				}

				if ( FLAG_COL_NAMES.TARGET_VARNAME.name().equals( colName ) ) {
					
					FLAG_COL_NAMES.TARGET_VARNAME.setPosition( position );
					
				} else if ( FLAG_COL_NAMES.TARGET_DATANAME.name().equals( colName ) ) {
					
					FLAG_COL_NAMES.TARGET_DATANAME.setPosition( position );
					
				} else if ( FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_NAME.name().equals( colName ) ) {
					
					FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_NAME.setPosition( position );
					
				} else if ( FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.name().equals( colName ) ) {
					
					FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.setPosition( position );
					
				}
				
			}
						
		} else {
			
			throw new Exception (" header col not found ");
			
		}

		logger.info(FLAG_COL_NAMES.TARGET_DATANAME.name() + ": " + FLAG_COL_NAMES.TARGET_DATANAME.getPosition());
		logger.info(FLAG_COL_NAMES.TARGET_VARNAME.name() + ": " + FLAG_COL_NAMES.TARGET_VARNAME.getPosition());
		logger.info(FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.name() + ": " + FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_NAME.getPosition());
		logger.info(FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.name() + ": " + FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.getPosition());
		
		logger.info( StringUtils.join( outputHeader, "," ) );
		
		if ( 0 == FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_NAME.getPosition() || 0 == FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.getPosition() ) {

			throw new Exception (" required flag cols not found ");
			
		}
		
		
		List<String[]> valueRows = new ArrayList<String[]>();
		
		/**
		 * There can be multiple files depending on the TARGET_DATANAME column. Like SS will have shell file. So value rows are different
		 */
		Map<String, List<String[]>> targetValueRows = new HashMap<String, List<String[]>>();
		
		
		
		
		int valPos = FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.getPosition();
		int namePos = FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_NAME.getPosition();
		int nameTargetPos = FLAG_COL_NAMES.TARGET_VARNAME.getPosition();
		int dataTargetPos = FLAG_COL_NAMES.TARGET_DATANAME.getPosition();

		String[] row = null;
		
		//2 work out value rows and name rows
		while ( null != ( row = csvReader.readNext() ) ) {
			

			// check if this row has a flag value,
			
			// if value row, copy A-E
			if( "Y".equals( row[ valPos ] ) || "y".equals( row[ valPos ] )) {
				
				String[] rowValuesToCopy = Arrays.copyOfRange( row, 0, 5 );
				
				valueRows.add( rowValuesToCopy ); // copy the first 5 columns only
				
				// add to dataTargets
				dataTargets.add( row[dataTargetPos] );
				
				List<String[]> valueRows2 = targetValueRows.get( row[dataTargetPos] );
				
				if ( null ==  valueRows2 ) {
					
					valueRows2 = new ArrayList<String[]>();
					targetValueRows.put( row[dataTargetPos], valueRows2 );
					
				}
				
				valueRows2.add( rowValuesToCopy );
				
				
				// will it be automatically copied?
				
			}
			
			// check if this row has a flag name
			
			if( "Y".equals( row[ namePos ] ) || "y".equals( row[ namePos ] )) {

				// get value from col G / Target column of this row and save it in outputHeader
				
				//outputHeader.add( "CODE_" + row[ nameTargetPos ] );
				List<String> targetHeaders = dataTargetHeaders.get( row[dataTargetPos] );
				
				if ( null == targetHeaders ) {
					
					targetHeaders = new ArrayList<String>();
					
					dataTargetHeaders.put( row[dataTargetPos], targetHeaders );
					
				}
				
				targetHeaders.add( "CODE_" + row[ nameTargetPos ] );
				
			}
			
			
			
			
		}


//		logger.info( StringUtils.join( outputHeader, "," ) );

//		for( String[] valueRow : valueRows ) {
//			
//
//			logger.info( StringUtils.join( valueRow, "," ) );
//			
//		}

		logger.info( "Data targets found: " + dataTargets );

//		printTargetHeaders ( dataTargetHeaders );
//		printTargetRows( targetValueRows );
		
		//writeFile( outputDir + "/SS_CodeShellFile.csv", outputHeader, valueRows );
		
		for( String target: dataTargets ) {
			
			String filename = outputDir + "/" + target + suffix + ".csv";
			
			List<String> headers = new ArrayList<String>();
			headers.addAll( outputHeader );
			headers.addAll( dataTargetHeaders.get( target) );
			
			writeFile( filename, headers, targetValueRows.get( target ) );
			
		}
	}
	
	/**
	 * 
	 * @param outputHeader
	 * @param valueRows
	 * @throws IOException 
	 */
	private void writeFile( String outputFilePath, List<String> outputHeader, List<String[]> valueRows ) throws IOException {
		
		logger.info( "Creating shell file: " + outputFilePath );
		
		int size = outputHeader.size();
				
		CSVWriter writer = new CSVWriter( new FileWriter( outputFilePath ) );
		
		writer.writeNext( (String[]) outputHeader.toArray( new String[0] ) );
		
		for ( String[] valRow : valueRows ) {
			
			String[] paddedRow = new String[ size ];

			for ( int i = 0; i < paddedRow.length; ++ i ) {
				
				if( i < valRow.length )
					paddedRow[i] = valRow[i];
				else
					paddedRow[i] = " ";
			}

			//logger.info( StringUtils.join( paddedRow, "," ) );

//			writer.writeNext( new String[]{ StringUtils.join( paddedRow, "," ) } );
			writer.writeNext( paddedRow );
			
		}
		
		//logger.info( writer.toString() );
		writer.close();

		logger.info( "Writing shell file successful: " + outputFilePath );
		
	}
	
	public void printTargetRows( Map<String, List<String[]>> targetValueRows ) {
		
		Set<String> targets = targetValueRows.keySet();
		
		for( String target : targets ) {
			
			List<String[]> valRows = targetValueRows.get( target );
			
			if( null != valRows ) {
				
				for( String[] valueRow : valRows ) {
					
					logger.info( StringUtils.join( valueRow, "," ) );
					
				}
				
				
			}
			
		}
		
	}
	
	public void printTargetHeaders( Map<String, List<String>> targetHeaders ) {
		
		Set<String> targets = targetHeaders.keySet();

		for( String target : targets ) {
			
			List<String> headers = targetHeaders.get( target );
			
			if( null != headers ) {
				
				for( String valueRow : headers ) {
					
					logger.info( StringUtils.join( valueRow, "," ) );
					
				}
				
				
			}
			
		}
		
	}

}
