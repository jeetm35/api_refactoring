package CS230.pom_parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class myJavaParser {
	
	public HashSet<ApiStorage> apiData;
	public HashSet<String> apiFunctionNames;
	
	public myJavaParser(HashSet<ApiStorage> apiData,HashSet<String> apiFunctionNames) {
		this.apiData=apiData;
		this.apiFunctionNames=apiFunctionNames;
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
	
	public static void main(String[] args) throws Exception {
		// creates an input stream for the file to be parsed
		//FileInputStream in = new FileInputStream("C:\\Users\\kprat\\git\\pom-parser\\src\\main\\java\\CS230\\pom_parser\\sample.java");
		// FileInputStream in = new
		// FileInputStream("main\\java\\CS230\\pom_parser\\sample.java");
		ObjectInputStream in1 = new ObjectInputStream(new FileInputStream(new File("./results/junit_functionsHash.txt")));
		ObjectInputStream in2= new ObjectInputStream(new FileInputStream(new File("./results/junit_fullQualifiedHash.txt")));
		HashSet<ApiStorage> functionsHash = (HashSet<ApiStorage>) in1.readObject();
		HashSet<String> functionNames = (HashSet<String>) in2.readObject();
		myJavaParser jp=new myJavaParser(functionsHash, functionNames);
		File jarDir=new File("C:\\Users\\kprat\\.m2\\repository");
		
		HashMap<ApiStorage,IntegerCount> mainRes = new HashMap<ApiStorage, IntegerCount>();
		for(ApiStorage func : functionsHash){
			mainRes.put(func, new IntegerCount(0));
		}
		mainRes.put(ApiStorage.getInstance(), new IntegerCount(0));
		//List<File> dire;
		File directory=new File("C:\\Users\\kprat\\git\\pom-parser\\src\\main");
		for(File dir:directory.listFiles()){
			if(dir.isDirectory()){
				System.out.println("Dir"+dir.getName());
				HashMap<ApiStorage,IntegerCount> res = new HashMap<ApiStorage, IntegerCount>();
				
				for(ApiStorage func : functionsHash){
					res.put(func, new IntegerCount(0));
				}
				res.put(ApiStorage.getInstance(), new IntegerCount(0));
				new myJavaParser(functionsHash,functionNames).parseCode(dir,jarDir,"pomparser",res,mainRes);
				try {
					// set the right path
					String path = "./StaticResults";
					ObjectOutputStream out = new ObjectOutputStream(
							new FileOutputStream(new File(path, dir.getName() + ".txt")));
					out.writeObject(res);
					Iterator<ApiStorage> iter = res.keySet().iterator();
					System.out.println("##########################");
					while(iter.hasNext()){
						ApiStorage tetmp=iter.next();
						if(res.get(tetmp).count>0)
							System.out.println(tetmp.fullyQualifiedName);
					}
					out.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
			
		}
		try {
			// set the right path
			String path = "./StaticResults";
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(new File(path,  "totalStatic.txt")));
			out.writeObject(mainRes);
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//printing final poutput
		System.out.println("Printing results-");
		Map sortedMap = sortByValues(mainRes);
		Iterator iti = sortedMap.entrySet().iterator();
		for(int i = 0 ;i < sortedMap.size(); i++){
			if(iti.hasNext()){
				Map.Entry me = (Map.Entry)iti.next();
				 System.out.println(me.getKey()+"----"+me.getValue());
				
			}
			else{
				break;
			}
		}
		
		
		
		
		
		// parse the file
		//File dir=new File("C:\\Users\\kprat\\git\\pom-parser\\src\\main\\java\\Sample");
		//String jarnames[]= {"junit-3.8.1.jar","junit-3.8.2.jar"};
		
		
		
		
		
//		ParserConfiguration ps = new ParserConfiguration();
//		CombinedTypeSolver com = new CombinedTypeSolver(new ReflectionTypeSolver(true));
//		ps.setSymbolResolver(new JavaSymbolSolver(com));
//		JavaParser jp = new com.github.javaparser.JavaParser(ps);
//		JavaParser.setStaticConfiguration(ps);
//		CompilationUnit cu = jp.parse(in);
//
//		cu.accept(new MethodVisitor(), null);

		// prints the resulting compilation unit to default system output
	}

	public void parseCode(File dir, File jarDir, String repo, HashMap<ApiStorage, IntegerCount> res, HashMap<ApiStorage,IntegerCount> mainRes) {
		
		HashSet<ApiStorage> functions = new HashSet<ApiStorage>();
		HashSet<String> fullQualifiedHash = new HashSet<String>();
		String s[] = new String[1];
		s[0] = "java";
		
		
		ParserConfiguration ps = new ParserConfiguration();

		JavaParser jp = new com.github.javaparser.JavaParser(ps);
		JavaParser.setStaticConfiguration(ps);
		CombinedTypeSolver com = new CombinedTypeSolver(new ReflectionTypeSolver(true));
		//com.add(new JavaParserTypeSolver(new File("src/main/java/CS230/pom_parser")));
		com.add(new JavaParserTypeSolver(dir));
		String jarExt[]={"jar"};
		Collection<File> jars = FileUtils.listFiles(jarDir, jarExt, true);
		for(File file:jars){
			try {
				com.add(new JarTypeSolver(file));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
//		for (String su : jarnames) {
//			try {
//				com.add(new JarTypeSolver(new File("./jars/" + su)));
//				// com.add(new JarTypeSolver(new
//				// File("/Users/jeetmehta/.m2/repository/junit/junit/3.8.1/junit-3.8.1.jar")));
//				// com.add(new JarTypeSolver(new
//				// File("C:\\Users\\kprat\\.m2\\repository\\com\\google\\guava\\guava\\23.4-jre\\guava-23.4-jre.jar")));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}
		ps.setSymbolResolver(new JavaSymbolSolver(com));
		
		//
		//Parsing all java files 
		Collection<File> files = FileUtils.listFiles(dir, s, true);
		for (File file : files) {
			System.out.println(file.getName());
			// parseJavaFile(file, functions,fullQualifiedHash, jp, com);
			try {
				FileInputStream in = new FileInputStream(file);
				CompilationUnit cu = jp.parse(in);
				cu.accept(new MethodCallVisitor(functions, fullQualifiedHash, com,res,mainRes), null);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		try {
			// set the right path
			String path = "./results";
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(new File(path, repo + "_functionsHash.txt")));
			out.writeObject(functions);
			out.close();
			out = new ObjectOutputStream(new FileOutputStream(new File(path, repo + "_fullQualifiedHash.txt")));
			out.writeObject(fullQualifiedHash);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private class MethodCallVisitor extends VoidVisitorAdapter<Void> {

		HashSet<ApiStorage> functions;
		HashSet<String> fullQualifiedHash;
		CombinedTypeSolver com;
		HashMap<ApiStorage, IntegerCount> res;
		HashMap<ApiStorage, IntegerCount> mainRes;

		public MethodCallVisitor(HashSet<ApiStorage> functions, HashSet<String> fullQualifiedHash, CombinedTypeSolver com, HashMap<ApiStorage, IntegerCount> res, HashMap<ApiStorage,IntegerCount> mainRes) {
			this.functions = functions;
			this.fullQualifiedHash = fullQualifiedHash;
			this.com = com;
			this.res=res;
			this.mainRes=mainRes;
		}

		@Override
		public void visit(MethodCallExpr n, Void arg) {
			/*
			 * here you can access the attributes of the method. this method
			 * will be called for all methods in this CompilationUnit, including
			 * inner class methods
			 */
			try {
				// System.out.println(n.getName());
				// System.out.println(n);
				// CombinedTypeSolver com = new CombinedTypeSolver(new
				// ReflectionTypeSolver(true));
				// com.add(new JarTypeSolver(new
				// File("C:/Users/kprat/.m2/repository/junit/junit/3.8.1/junit-3.8.1.jar")));
				// com.add(new JarTypeSolver(new
				// File("C:\\Users\\kprat\\.m2\\repository\\com\\google\\guava\\guava\\23.4-jre\\guava-23.4-jre.jar")));

				JavaParserFacade a = JavaParserFacade.get(com);
				//n.resolveInvokedMethod()
				//a.solve(n.getName())
				SymbolReference<ResolvedMethodDeclaration> p = a.solve(n);
				p.getCorrespondingDeclaration();
				// JavaParserMethodDeclaration tp =new
				// JavaParserMethodDeclaration(, com);
				// p.getClass()
				String qName= p.getCorrespondingDeclaration().getQualifiedName();
				ArrayList<String> params=new ArrayList<String>();
				p.getCorrespondingDeclaration().getQualifiedSignature();
				for (int i = 0; i < p.getCorrespondingDeclaration().getNumberOfParams(); i++) {
					//System.out.println("Param type: " + p.getCorrespondingDeclaration().getParam(i).describeType());
					params.add(p.getCorrespondingDeclaration().getParam(i).describeType());

				}
				
				p.getCorrespondingDeclaration().getQualifiedName();
				ApiStorage as = new ApiStorage(qName, params);
				if(apiFunctionNames.contains(qName)){
					if(apiData.contains(as)){
						System.out.println("xxxxxxxxxxxxxxxxxxxxxx");
						System.out.println("Qualified name:" + p.getCorrespondingDeclaration().getQualifiedName());
						
						res.get(as).increment();
						res.get(ApiStorage.getInstance()).increment();
						mainRes.get(as).increment();
						mainRes.get(ApiStorage.getInstance()).increment();
					}
					
				}
				//functions.add();
		        //fullQualifiedHash.add(qName);
				// p.getCorrespondingDeclaration().getParam(0).describeType();
				// System.out.println(p);
				super.visit(n, arg);
			} catch (Exception e) {
				System.out.println("Error=========================="+n.getName());
				//e.printStackTrace();
			}
		}

		// public void visit(MethodDeclaration n, Void arg) {
		// /* here you can access the attributes of the method.
		// this method will be called for all methods in this
		// CompilationUnit, including inner class methods */
		// try{
		// System.out.println("-------------------");
		// CombinedTypeSolver com = new CombinedTypeSolver(new
		// ReflectionTypeSolver(true));
		// com.add(new JarTypeSolver(new
		// File("C:/Users/kprat/.m2/repository/junit/junit/3.8.1/junit-3.8.1.jar")));
		// com.add(new JarTypeSolver(new
		// File("C:\\Users\\kprat\\.m2\\repository\\com\\google\\guava\\guava\\23.4-jre\\guava-23.4-jre.jar")));
		//
		// //JavaParserFacade a= JavaParserFacade.get(com);
		//
		// //JavaSymbolSolver t = new JavaSymbolSolver(com);
		// ResolvedMethodDeclaration m = n.resolve();
		// System.out.println("Qualified name "+m.getQualifiedName());
		// //m.getQualifiedSignature();
		// JavaParserMethodDeclaration tp =new JavaParserMethodDeclaration(n,
		// com);
		//
		// tp.accessSpecifier();
		//
		// tp.getReturnType();
		//
		// for (int i=0;i< tp.getNumberOfParams();i++){
		// System.out.println("Param type: "+tp.getParam(i).describeType());
		// System.out.println("Param name:"+tp.getParam(i).getName());
		// }
		//
		//
		//
		// //tp.getParam(0).
		// //t.resolveDeclaration(n.getParameters().get(0).getChildNodes().get(0),
		// arg1)
		//// n.getModifiers();
		//// n.getParameters();
		//// n.getReceiverParameter();
		//// n.getSignature();
		//// n.getTypeParameters();
		//// n.getParsed();
		//// n.getParameters().get(1).getType();
		//// n.getParameters().get(0).getChildNodes().get(0);
		////
		//// m.declaringType();
		//// m.getClassName();
		//// m.getPackageName();
		//// m.getParam(1).describeType();
		////
		//// m.getQualifiedSignature();
		//// m.getParam(0).getClass();
		//// m.getTypeParameters();
		//
		//
		//
		//// SymbolReference<ResolvedMethodDeclaration> p = a.solve(m);
		//// System.out.println(p);
		// super.visit(n, arg);
		// }
		// catch(Exception e){
		// e.printStackTrace();
		// }
		// }
		//
	}
}