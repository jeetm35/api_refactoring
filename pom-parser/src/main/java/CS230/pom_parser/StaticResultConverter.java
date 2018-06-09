package CS230.pom_parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class StaticResultConverter {
	
	public static void main(String arg[]) throws FileNotFoundException, IOException, ClassNotFoundException{
		
		File resultDir=new File("./StaticResults");
		for(File f:resultDir.listFiles()){
			if(f.getName().endsWith("_junit.txt")){
				ObjectInputStream temp= new ObjectInputStream(new FileInputStream(f));
				HashMap<ApiStorage,IntegerCount> res=(HashMap<ApiStorage, IntegerCount>) temp.readObject();
				String name="C:/Users/kprat/Desktop/StaticResults/"+f.getName();
				BufferedWriter br=new BufferedWriter(new FileWriter(new File(name)));
				Iterator<ApiStorage> iter=res.keySet().iterator();
				while(iter.hasNext()){
					ApiStorage t=iter.next();
					String str=t.fullyQualifiedName+"_"+String.join("_", t.paramters);
					
					br.write(str+"\t"+res.get(t).count);
					br.newLine();
				}
				br.close();
				System.out.println(f.getName());
			
			}
				
		}
		
		
		
		
	}

}
