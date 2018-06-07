package CS230.pom_parser;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;



public class myApiParser {

	public static void parseApi(File dir, String jarnames[], String apiName){
		
		HashSet<ApiStorage> functions = new HashSet<ApiStorage>();
		HashSet<String> fullQualifiedHash = new HashSet<String>();
		String s[] = new String[1];
		s[0] = "java";
		ParserConfiguration ps=new ParserConfiguration();
		
	 	JavaParser jp=new com.github.javaparser.JavaParser(ps);
	 	JavaParser.setStaticConfiguration(ps);
	 	CombinedTypeSolver com = new CombinedTypeSolver(new ReflectionTypeSolver());
	 	
	 	Collection<File> jars = FileUtils.listFiles(dir, new String[]{"jar"}, true);
	 	for(File f : jars){
	 		try{
				com.add(new JarTypeSolver(f));
			}
			catch(Exception e){
				e.printStackTrace();
			}
	 	}
	 	
	 	com.add(new JavaParserTypeSolver(dir));
	 	
	 	for(String su: jarnames){
			try{
				com.add(new JarTypeSolver(new File("./jars/"+su)));
//				com.add(new JarTypeSolver(new File("/Users/jeetmehta/.m2/repository/junit/junit/3.8.1/junit-3.8.1.jar")));
//		        com.add(new JarTypeSolver(new File("C:\\Users\\kprat\\.m2\\repository\\com\\google\\guava\\guava\\23.4-jre\\guava-23.4-jre.jar")));
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
	 	
	 	ps.setSymbolResolver(new JavaSymbolSolver(com));
	 	
	 	
		Collection<File> files = FileUtils.listFiles(dir, s, true);     
		for(File file : files){
		    System.out.println(file.getName());  
//		    parseJavaFile(file, functions,fullQualifiedHash, jp, com);
		    try{
					FileInputStream in = new FileInputStream(file);
					CompilationUnit cu = jp.parse(in);
					cu.accept(new MethodVisitor(functions,fullQualifiedHash,com), null);
		    }
		    catch(Exception e){
		    	e.printStackTrace();
		    }
		    
		} 
		
		try{
//			set the right path
			String path = "./results";
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(path,apiName+"_functionsHash.txt")));
			out.writeObject(functions);
			out.close();
			out = new ObjectOutputStream(new FileOutputStream(new File(path,apiName+"_fullQualifiedHash.txt")));
			out.writeObject(fullQualifiedHash);
//			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(path,"serliaze.txt")));
//			XmlParser demo = (XmlParser) in.readObject();	
		}
		catch(Exception e ){
			e.printStackTrace();
		}
		
	}
	
	public static void parseJavaFile(File file, HashSet<ApiStorage> functions, HashSet<String> fullQualifiedHash, JavaParser jp,CombinedTypeSolver com ){
		
		 
	}
	public static void main(String[] args){
	
		System.out.println("sff");
		//jar to be analysed
		
		File f = new File("C:/Sneha/Studies/UCLA/Classes/Q3Spring2018/CS230/Project/junit4-r4.12");

//		for(File fi : f.listFiles()){
//			System.out.println(fi.getName());
//		}
		//used for symbol resolver

		String jarnames[]= {"junit-4.12.jar"};
		parseApi(f,jarnames,"junit");
		
		System.out.println("check");
	}
	
}



class MethodVisitor extends VoidVisitorAdapter<Void> {
    
    
    HashSet<ApiStorage> functions;
	HashSet<String> fullQualifiedHash;
	CombinedTypeSolver com;
	
	public MethodVisitor(HashSet<ApiStorage> functions,HashSet<String> fullQualifiedHash,CombinedTypeSolver com  ) {
		this.functions = functions;
		this.fullQualifiedHash = fullQualifiedHash;
		this.com = com;
		
	}
	
	@Override
    public void visit(MethodDeclaration n, Void arg) {
        /* here you can access the attributes of the method.
         this method will be called for all methods in this 
         CompilationUnit, including inner class methods */
    	try{
//    		System.out.println("-------------------");
	        ResolvedMethodDeclaration m = n.resolve();	    
	        
	        JavaParserMethodDeclaration tp = new JavaParserMethodDeclaration(n, com);
	       
	        ArrayList<String> parameters = null;
	        if(tp.accessSpecifier().asString().contains("public")){
	        	System.out.println("Qualified name "+m.getQualifiedName());
	        	parameters = new ArrayList<String>();
		        for (int i=0;i< tp.getNumberOfParams();i++){
		        	try{
//		        		System.out.println("Param type: "+tp.getParam(i).describeType());
			        	parameters.add(tp.getParam(i).describeType());
//			        	System.out.println("Param name:"+tp.getParam(i).getName());
		        	}
		        	catch(Exception e ){
		        		e.printStackTrace();
		        		parameters.add("*");
		        	}
		        	
		        }
		        functions.add(new ApiStorage(m.getQualifiedName(), parameters));
		        fullQualifiedHash.add(m.getQualifiedName());
	        }
	        super.visit(n, arg);
    	}
        catch(Exception e){
        	e.printStackTrace();
        }
    }
    
}
