package XOlogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SimpleDictionary {
	Map<String, String> dictionary;
	public SimpleDictionary(){
		dictionary = new HashMap<String, String>();	
		loadMap("./dictionaryParts.txt",false);
	}
	
	public SimpleDictionary(String path, boolean mode){
		dictionary = new HashMap<String, String>();	
		loadMap(path, mode);
		//System.out.println(dictionary.size());
	}
	public String findItem(String key){				
		return dictionary.get(key);
	}

	public void loadMap(String path, boolean mode){
		File file = new File(path);
		try {
			Scanner scanner= new Scanner(file,"utf-8");
			while(scanner.hasNextLine()){
				String line =scanner.nextLine();				
				if (scanner.hasNextLine()){
					String line2 =scanner.nextLine();
					//System.out.println(trimTag(line2));
					if (mode == true)
						dictionary.put(line2.toLowerCase(), line);
					else
						dictionary.put(line.toLowerCase(),line2); //???
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Dictionary size: "+dictionary.size());
	}

	public List<String> getTags() {
		List <String> result = new ArrayList<String>();
		for (String key : dictionary.keySet()){
			result.add(key);
		}
		// TODO Auto-generated method stub
		return result;
	}
}
