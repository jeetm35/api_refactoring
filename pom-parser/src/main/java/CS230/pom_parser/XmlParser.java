package CS230.pom_parser;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;


import com.github.javaparser.ParserConfiguration;

public class XmlParser implements Serializable {
	
	TreeMap<String,Integer> packageCount;
	HashMap<String,HashMap<String,Integer>> versionCount;
	int repoDependencyCount;
	int reposScanned;
	public XmlParser(){
		packageCount = new TreeMap<String,Integer>();
		versionCount = new HashMap<String,HashMap<String,Integer>>();
		repoDependencyCount = 0;
		reposScanned = 0;
	}
	public void add(String api, String version){
		addtoHash(api, version, this.packageCount, this.versionCount);
	}
	
	public static void addtoHash(String api, String version, TreeMap<String,Integer> packageCount, HashMap<String,HashMap<String,Integer>> versionCount){
		packageCount.put(api, packageCount.getOrDefault(api, 0)+1);
		if(!version.equals("null")){
			if(versionCount.containsKey(api)){
				versionCount.get(api).put(version, versionCount.get(api).getOrDefault(version, 0)+1);
			}
			else{
				HashMap<String,Integer> temp = new HashMap<String,Integer>();
				temp.put(version, 1);
				versionCount.put(api, temp );
			}
		}
	}
	
	public void showPackages(){
		Iterator<String> iti= packageCount.keySet().iterator();
		while(iti.hasNext()){
			String temp = iti.next();
			System.out.println(temp+"----->"+packageCount.get(temp));
		}

	}
	
	public void showVersion(){
		Iterator<String> iti= packageCount.keySet().iterator();
		while(iti.hasNext()){
			String temp = iti.next();
			System.out.println("----------------------------------------------");
			System.out.println(temp+"----->"+packageCount.get(temp));
			Iterator<Entry<String,Integer>> iti_version = versionCount.get(temp) != null ? versionCount.get(temp).entrySet().iterator() : null;
			if(iti_version == null)
				continue;
			while(iti_version.hasNext()){
				Entry<String,Integer> e = iti_version.next();
				System.out.println(e.getKey()+"------"+e.getValue());
				
			}
		}
	}
	
	public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
    Comparator<K> valueComparator = new Comparator<K>() {
      public int compare(K k1, K k2) {
        int compare = 
              map.get(k1).compareTo(map.get(k2));
        if (compare == 0) 
          return 1;
        else 
          return compare*-1;
      }
    };
 
    Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
    sortedByValues.putAll(map);
    return sortedByValues;
    }
	
	public void parsefile(File pom, TreeMap<String,Integer> tempPackageCount, HashMap<String,HashMap<String,Integer>> tempVersionCount ){
		MavenXpp3Reader xpp3Reader = null;
		Reader reader = null;
		List<Dependency> dependencies = null;
		Properties properties = null;
		Model model = null;
		Dependency dependency = null;
		if(pom.exists()){
			try{
				xpp3Reader = new MavenXpp3Reader();
				reader = new FileReader(pom); 
			    model = xpp3Reader.read(reader);
			    dependencies = model.getDependencies();
			    if(model.getDependencyManagement() != null)
			    	dependencies.addAll(model.getDependencyManagement().getDependencies());
			    properties = model.getProperties();
			    if(dependencies.size() >= 1)
			    for (int i = 0; i < dependencies.size(); i++) {
			    	try{
			    		dependency = dependencies.get(i);
				        String q = "null";
				        if(dependency.getVersion() != null){
				        	if(dependency.getVersion().trim().startsWith("$")){
					        	q = dependency.getVersion().trim();
					        	q = q.substring(2, q.length()-1);
					        	q = properties.getProperty(q);
					        }
					        else{
					        	q = dependency.getVersion().trim();
					        }
				        }
				        String group = dependency.getGroupId() != null ?  dependency.getGroupId() : "";
				        String artifact = dependency.getArtifactId() != null ? dependency.getArtifactId() : "";
				        q = q == null ?  "null" : q;
				        addtoHash(group+"/"+artifact,q,tempPackageCount,tempVersionCount ); 
//				        System.out.println(dependency.getGroupId()+"/"+dependency.getArtifactId());
			    	}
			    	catch(Exception e){
			    		e.printStackTrace();
			    	}
			        
			    }
			} 
			catch(Exception e ){
				e.printStackTrace();
			}
		
		}
	}
	public void parseRepo(File repo,TreeMap<String,Integer> tempPackageCount, HashMap<String,HashMap<String,Integer>> tempVersionCount ){
		String s[] = new String[1];
		s[0] = "xml";
		ParserConfiguration ps=new ParserConfiguration();
		Collection<File> files = FileUtils.listFiles(repo, s, true); 
		for(File f: files){
			if(f.getName().equals("pom.xml")){
				parsefile(f,tempPackageCount, tempVersionCount);
			}
		}
	}
	
	
	
	public ArrayList<String> showTopK(int k){
		ArrayList<String> res = new ArrayList<String>();
		Map sortedMap = sortByValues(packageCount);
		Iterator iti = sortedMap.entrySet().iterator();
		for(int i = 0 ;i < k; i++){
			if(iti.hasNext()){
				Map.Entry me = (Map.Entry)iti.next();
				 System.out.println(me.getKey()+"----"+me.getValue());
				res.add(me.getKey().toString());
			}
			else{
				break;
			}
		}
		return res;
	}
	
	public void showtopversion(ArrayList<String> api){
		for(int i = 0 ; i < api.size();i++){
			if(versionCount.get(api.get(i)) == null){
				System.out.println("indide null hash ");
				continue;
			}
				
			Map sortedMap = sortByValues(versionCount.get(api.get(i)));
			Iterator iti = sortedMap.entrySet().iterator();
			System.out.println(api.get(i)+"---------"+packageCount.get(api.get(i)));
			if(iti.hasNext()){
					Map.Entry me = (Map.Entry)iti.next();
					System.out.println(me.getKey()+"----"+me.getValue());
				}
		}
	}
	
	public void parseDirectory(String path){
		File directory = new File(path);
		MavenXpp3Reader xpp3Reader = null;
		Reader reader = null;
		List<Dependency> dependencies = null;
		Properties properties = null;
		Model model = null;
		Dependency dependency = null;
		for(File child: directory.listFiles()){
			System.out.println("----------------------------------------------");
			System.out.println(child.getName());
			if(child.isDirectory()){
				reposScanned++;
				TreeMap<String,Integer> tempPackageCount = new TreeMap<String,Integer>();;
				HashMap<String,HashMap<String,Integer>> tempVersionCount = new HashMap<String,HashMap<String,Integer>>();
				parseRepo(child, tempPackageCount,tempVersionCount );
				for(String key :tempPackageCount.keySet()){
					if(tempVersionCount.get(key) == null){
						add(key,"null");
						continue;
					}	
					Map sortedMap = sortByValues(tempVersionCount.get(key));
					Iterator iti = sortedMap.entrySet().iterator();
					if(iti.hasNext()){
							Map.Entry me = (Map.Entry)iti.next();
//							System.out.println(me.getKey()+"----"+me.getValue());
							add(key,me.getKey().toString());
					}
					
				}
			}
		}
	}
	

	
	public void findPackageMove(String path, String api, String destination, String version , boolean checkVersion ){
		File directory = new File(path);
		MavenXpp3Reader xpp3Reader = null;
		Reader reader = null;
		List<Dependency> dependencies = null;
		Properties properties = null;
		Model model = null;
		Dependency dependency = null;
		for(File child: directory.listFiles()){
			System.out.println(child);
			if(child.isDirectory()){
				File pom = new File(child, "pom.xml"); 
				try{
					TreeMap<String,Integer> tempPackageCount = new TreeMap<String,Integer>();;
					HashMap<String,HashMap<String,Integer>> tempVersionCount = new HashMap<String,HashMap<String,Integer>>();
					parseRepo(child, tempPackageCount,tempVersionCount );
					if(checkVersion == false && tempPackageCount.containsKey(api)){
						org.codehaus.plexus.util.FileUtils.copyDirectoryStructure(child, new File(destination,child.getName()));
					}
					else if (checkVersion && tempPackageCount.containsKey(api)) {
						if(tempVersionCount.get(api) != null && tempVersionCount.get(api).containsKey(version)){
							org.codehaus.plexus.util.FileUtils.copyDirectoryStructure(child, new File(destination,child.getName()));
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "C:\\Users\\kprat\\workspace\\github-repos";
//		File f = new File(path);
//		Collection<File> files = FileUtils.listFilesAndDirs(f, DirectoryFileFilter.INSTANCE, DirectoryFileFilter.INSTANCE);
//		for(File a : files){
//			System.out.println(a.getAbsolutePath());
//		}
		
		
		XmlParser  parser = new XmlParser();
//		parser.parseDirectory(path);
//		parser.showPackages();
//		parser.showVersion();
//		parser.showTopK(3);	 
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(path,"serliaze.txt")));
			out.writeObject(parser);
//			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(path,"serliaze.txt")));
//			XmlParser demo = (XmlParser) in.readObject();	
		}
		catch(Exception e ){
			System.out.println(e);
		}
		parser.findPackageMove(path, "junit/junit", path+"_api", "4.12", true);
		System.out.println(parser.reposScanned);
//		System.out.println(parser.repoDependencyCount);
		parser.showtopversion(parser.showTopK(20));
	}
}
