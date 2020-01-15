package jpa.serlean.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileHelper {
	
	/**
	 * Load file from class path if it exist, or on an external path
	 * 
	 * @param name
	 * @return
	 */
	public static String getFileContent(String name) {
		return FileHelper.getFileContent(name, null);
	}
	
	/**
	 * Load file from class path if it exist, or on an external path
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public static String getFileContent(String name, String defaultValue) {
		try {			
			URL url = FileHelper.class.getClassLoader().getResource(name);
			if( url!=null ) {
				File file = new File(url.getFile());
				if( file.exists() ) {
					return new String(Files.readAllBytes( file.toPath() ));
				}
			}
			return new String(Files.readAllBytes(Paths.get(name)), StandardCharsets.US_ASCII);			
		} catch (IOException e) {
		}
		return defaultValue;
	}
	
	/**
	 * Load file as an input stream
	 * 
	 * @param name
	 * @return
	 */
	public static InputStream getFileInputStream(String name){		
		try {
			File file = new File(FileHelper.class.getClassLoader().getResource(name).getFile());
			if( file.exists() ) { 
				return new FileInputStream(file);	
			} 
			return new FileInputStream(new File(name));
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Convert InputStream to String value
	 * 
	 * @param inputStream
	 * @return
	 */
	public static String getStringInputStream(InputStream inputStream) {
		return new BufferedReader(new InputStreamReader(inputStream)).lines()
		   .parallel().collect(Collectors.joining("\n"));
	}
	
	/**
	 * 
	 * @param dir
	 * @return
	 */
	public static List<String> getFiles(String dir) {
		File file = new File(dir);
		List<String> list = new ArrayList<String>();
		if( file.exists() ) {
			FileHelper.getFilePath(list, file.getPath(), file);
		}
		return list;
	}
	
	/**
	 * 
	 * @param list
	 * @param rootdir
	 * @param dir
	 */
	private static void getFilePath(List<String> list, String rootdir, File dir) {
		if(dir.isDirectory()) {
			for( File file : dir.listFiles() ) {
				FileHelper.getFilePath(list, rootdir, file);				
			}			
		}
		else {
			list.add(dir.getPath().replace(rootdir, "") );
		}	
	}
	
}
