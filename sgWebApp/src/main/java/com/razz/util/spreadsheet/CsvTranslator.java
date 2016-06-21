package com.razz.util.spreadsheet;

import java.util.List;
import java.util.Map;

public interface CsvTranslator<X> {

	public X translate(Map<String, List<String>> data);
	
}
