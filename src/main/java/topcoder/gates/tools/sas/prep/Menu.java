package topcoder.gates.tools.sas.prep;

import java.util.HashMap;
import java.util.Map;

public class Menu {
	
	enum ListItem {
		
		CREATE_SHELL( "1. Create Shell File" ),
		VALIDATE( "2. Validate" ),
		QUIT( "0. Quit" );
		
		String message;
		
		private ListItem( String message ) {

			this.message = message;
			
		}
		
		public String getMessage() {
			
			return message;
			
		}
		
	}

	Map<Integer, ListItem> commandIndex;
	
	public Menu() {
		
		commandIndex = new HashMap<Integer, ListItem>();
		
		commandIndex.put( 1,  ListItem.CREATE_SHELL );
		commandIndex.put( 2, ListItem.VALIDATE );
		commandIndex.put( 0, ListItem.QUIT );
		
		
	}
	
	public void showMenu() {

		//System.out.println("**************************************");
		System.out.println("******************************************************************");
		System.out.println("MAIN MENU:");
		System.out.println(" ");
		
		for( ListItem item: ListItem.values() ) {
			
			System.out.println( item.getMessage() );
			
		}
		
		System.out.println("Write 1 or 2 and press enter to start processing or 0 to quit: ");
		
	}

}
