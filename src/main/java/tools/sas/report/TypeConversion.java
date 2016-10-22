package tools.sas.report;

import java.util.HashMap;
import java.util.Map;

public class TypeConversion {

	public static Map<String, String> DDFToBinary;
	public static Map<String, String> BinaryToDDF;
	
	static {

		DDFToBinary = new HashMap<String, String>();
		BinaryToDDF = new HashMap<String, String>();

		DDFToBinary.put("num", "Number");
		DDFToBinary.put("char", "String");
		
		BinaryToDDF.put("Number", "num");
		BinaryToDDF.put("String", "char");
		
	}

}
