 /**
 * 
 */
package edu.buffalo.cse.irf14.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author nikhillo
 * Class that parses a given file into a Document
 */
public class Parser {
	/**
	 * Static method to parse the given file into the Document object
	 * @param filename : The fully qualified filename to be parsed
	 * @return The parsed and fully loaded Document object
	 * @throws ParserException In case any error occurs during parsing
	 */
	public static Document parse(String filename) throws ParserException {
		if (filename != null && !filename.isEmpty()) {
			File f = new File(filename);
			if (f.exists()) {
				try {
					BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
					String line;
					boolean isTitle = true, isMetadata = true; 
					StringBuilder bldr = null;
					
					Document d = new Document();
					populate(d, FieldNames.FILEID, f.getName());
					populate(d, FieldNames.CATEGORY, getCategory(filename));
					while ((line = rdr.readLine()) != null) {
						if (!line.isEmpty()) {
							if (isTitle) {
								isTitle = false;
								populate(d, FieldNames.TITLE, line);
							} else if (isAuthor(line)) {
								populateAuthorFields(d, line);
							} else {
								if (isMetadata) {
									String[] splits = line.split(" - ");
									populatePlaceAndDate(d, splits[0]);
									bldr = new StringBuilder();
									
									if (splits.length > 1)
										bldr.append(splits[1]).append(" ");
									
									isMetadata = false;
								} else {
									bldr.append(line).append(" ");
								}
							}
						}
					}
					
					populate(d, FieldNames.CONTENT, bldr.toString());
					rdr.close();
					return d;
				} catch (IOException e) {
					throw new ParserException("Encountered IOException when reading / parsing the file");
				} 
			} else {
				throw new ParserException("File does not exist");
			}
		} else
			throw new ParserException("Filename is null or empty!");
	}
	
	private static void populateAuthorFields(Document d, String line) {
		line = line.substring(8, line.length() - 9);
		line = line.trim();
		line = line.substring(3); //remove by
		String[] splits = line.split(",");
		
		if (splits.length == 2) {
			//author and org
			populate(d, FieldNames.AUTHORORG, splits[1].trim());
		} 

		splits = splits[0].trim().split(" and ");
		populate(d, FieldNames.AUTHOR, splits);
		
	}

	private static boolean isAuthor(String line) {
		return line.indexOf("<AUTHOR>") != -1;
	}

	private static void populatePlaceAndDate(Document d, String string) {
		string = string.trim();
		//String[] splits = string.split(",", 2);
		int idx = string.lastIndexOf(',');
		
		if (idx != -1)
			populate(d, FieldNames.PLACE, string.substring(0, idx).trim());
		
		populate(d, FieldNames.NEWSDATE, string.substring(idx +1).trim());
	}

	private static String getCategory(String filename) {
		String[] splits = filename.split("\\"+File.separator);
		return splits[splits.length - 2];
	}

	private static void populate(Document d, FieldNames f, String... values) {
		d.setField(f, values);
	}

}
