package com.razz.util.spreadsheet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CsvReader<X> {

	static final String DELIMITER = ",";
	
	private final char keyIdentifier;
	private final CsvTranslator<X> translator;
	
	public CsvReader(char keyIdentifier, CsvTranslator<X> translator) {
		this.keyIdentifier = keyIdentifier;
		this.translator = translator;
	}
	
	public List<X> read(File file) throws Exception {
		//
		final List<X> objectList = new ArrayList<>();
		
		//
		try( FileReader fileReader = new FileReader(file);
			 BufferedReader buffReader = new BufferedReader(fileReader)	) 
		{
			final Map<String, List<String>> data = new LinkedHashMap<>();
			String line;
			while((line = buffReader.readLine()) != null) {
				//
				if( line.trim().charAt(0) == keyIdentifier ) {
					//
					if( !data.isEmpty() ) {
						//
						final X obj = translator.translate(data);
						objectList.add(obj);
						
						//
						data.clear();
					}
					
					//
					final String[] split = line.split(DELIMITER);
					for(String s : split) {
						final String key = s.trim();
						final List<String> value = new ArrayList<>();
						data.put(key, value);
					}
				}
				
				//
				else if( line.trim().length() > 0 ) {
					final Set<String> keys = data.keySet();
					final String[] split = line.split(DELIMITER);
					int i = 0;
					for(String k : keys) {
						final List<String> list = data.get(k);
						if(i < split.length) {
							final String value = split[i].trim();
							if(value.length() > 0)
								list.add(value);
						}
						i++;
					}
				}
			}
		}
		
		//
		return objectList;	
	}
	
}
