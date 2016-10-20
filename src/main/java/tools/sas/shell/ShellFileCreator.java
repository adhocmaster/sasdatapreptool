package tools.sas.shell;

import java.awt.datatransfer.StringSelection;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class ShellFileCreator {
	
	private enum FLAG_COL_NAMES {
		
		IS_TARGET_FIELD_CODE_NAME,
		IS_TARGET_FIELD_CODE_VALUE,
		TARGET_VARNAME;
		
		/**
		 * We will save position of the flag columns found in the DDF Csv file. 0 based position
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
		
	}
	
	public void createCodeShellFile( String DDFFilePath ) throws Exception {
		
		CSVReader csvReader = new CSVReader( new FileReader( DDFFilePath ));
		
		String[] header = null;
		List<String> outputHeader = new ArrayList<String>(); //  columns in Shell file
		
		// 1. Process Header and work out positions of related columns
		if ( null != ( header = csvReader.readNext() ) ) {
			
			//System.out.println( StringUtils.join( header, "," ) );

			for ( int position = 0; position < header.length; ++position ) {
				
				String colName = header[ position ];
				
				// if col position is less than 5 ( col A to E )
				if ( position < 5 ) {
					
					outputHeader.add( colName );
					
				}

				if ( FLAG_COL_NAMES.TARGET_VARNAME.name().equals( colName ) ) {
					
					FLAG_COL_NAMES.TARGET_VARNAME.setPosition( position );
					
				} else if ( FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_NAME.name().equals( colName ) ) {
					
					FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_NAME.setPosition( position );
					
				} else if ( FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.name().equals( colName ) ) {
					
					FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.setPosition( position );
					
				}
				
			}
						
		} else {
			
			throw new Exception (" header col not found ");
			
		}

		System.out.println(FLAG_COL_NAMES.TARGET_VARNAME.name() + ": " + FLAG_COL_NAMES.TARGET_VARNAME.getPosition());
		System.out.println(FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.name() + ": " + FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_NAME.getPosition());
		System.out.println(FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.name() + ": " + FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.getPosition());
		System.out.println( StringUtils.join( outputHeader, "," ) );
		
		if ( 0 == FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_NAME.getPosition() || 0 == FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.getPosition() ) {

			throw new Exception (" required flag cols not found ");
			
		}
		
		
		List<String[]> valueRows = new ArrayList<String[]>();
		
		
		int valPos = FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_VALUE.getPosition();
		int namePos = FLAG_COL_NAMES.IS_TARGET_FIELD_CODE_NAME.getPosition();
		int nameTargetPos = FLAG_COL_NAMES.TARGET_VARNAME.getPosition();

		String[] row = null;
		
		//2 work out value rows and name rows
		while ( null != ( row = csvReader.readNext() ) ) {
			

			// check if this row has a flag value,
			
			if( "Y".equals( row[ valPos ] ) || "y".equals( row[ valPos ] )) {
				
				valueRows.add( Arrays.copyOfRange( row, 0, 5 ) ); // copy the first 5 columns only
				
			}
			
			// check if this row has a flag name
			
			if( "Y".equals( row[ namePos ] ) || "y".equals( row[ namePos ] )) {

				// get value from col G / Target column of this row and save it in outputHeader
				
				outputHeader.add( "CODE_" + row[ nameTargetPos ] );
				
			}
			
			
			
			
		}


		System.out.println( StringUtils.join( outputHeader, "," ) );

		for( String[] valueRow : valueRows ) {
			

			System.out.println( StringUtils.join( valueRow, "," ) );
			
		}
		
		writeFile( "e:/SS_CodeShellFile.csv", outputHeader, valueRows );
	}
	
	/**
	 * 
	 * @param outputHeader
	 * @param valueRows
	 * @throws IOException 
	 */
	private void writeFile( String outputFilePath, List<String> outputHeader, List<String[]> valueRows ) throws IOException {
		
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

			System.out.println( StringUtils.join( paddedRow, "," ) );

//			writer.writeNext( new String[]{ StringUtils.join( paddedRow, "," ) } );
			writer.writeNext( paddedRow );
			
		}
		
		System.out.println( writer.toString() );
		writer.close();
		
		
	}

}
