package engine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.koossa.logger.Log;

public class FileUtils {
	
	public static String fileToString(File file) {
		String str = "", temp;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			try {
				while ((temp = reader.readLine()) != null) {
					str = str + temp + "\n";
				}
			} catch (IOException e) {
				Log.error(FileUtils.class, "Error reading file: " + file.getPath());
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			Log.error(FileUtils.class, "File not found: " + file.getPath());
			e1.printStackTrace();
		}
		
		return str;
	}
	
	public static String fileToString(String path) {
		String str = "", temp;
		BufferedReader reader;
		File file = new File(path);
		try {
			reader = new BufferedReader(new FileReader(file));
			try {
				while ((temp = reader.readLine()) != null) {
					str = str + temp + "\n";
				}
			} catch (IOException e) {
				Log.error(FileUtils.class, "Error reading file: " + file.getPath());
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			Log.error(FileUtils.class, "File not found: " + file.getPath());
			e1.printStackTrace();
		}
		
		return str;
	}
	
	public static String getResourceAsString(String location) {
		String str = "", temp;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream(location)));
			while ((temp = reader.readLine()) != null) {
				str = str + temp + "\n";
			}
		} catch (Exception e) {
			Log.error(FileUtils.class, "Failed to read: " + location);
			e.printStackTrace();
		}
		return str;
	}

}
