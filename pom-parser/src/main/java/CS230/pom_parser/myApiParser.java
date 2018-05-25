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
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import CS230.pom_parser.myJavaParser.MethodVisitor;

public class myApiParser {

	public static void parseApi(File dir){
		
		HashSet<ApiStorage> functions = new HashSet<ApiStorage>();
		HashSet<String> fullQualifiedHash = new HashSet<String>();
		String s[] = new String[1];
		s[0] = "java";
		Collection<File> files = FileUtils.listFiles(dir, s, true);     
		for(File file : files){
		    System.out.println(file.getName());  
		    parseJavaFile(file, functions,fullQualifiedHash);
		} 
		
		try{
//			set the right path
			String path = "/Users/jeetmehta/Dropbox/CS230/Project/results";
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(path,"functionsHash.txt")));
			out.writeObject(functions);
			out.close();
			out = new ObjectOutputStream(new FileOutputStream(new File(path,"fullQualifiedHash.txt")));
			out.writeObject(fullQualifiedHash);
//			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(path,"serliaze.txt")));
//			XmlParser demo = (XmlParser) in.readObject();	
		}
		catch(Exception e ){
			e.printStackTrace();
		}
		
	}
	
	public static void parseJavaFile(File file, HashSet<ApiStorage> functions, HashSet<String> fullQualifiedHash){
		
		 FileInputStream in = new FileInputStream(file);
		 	ParserConfiguration ps=new ParserConfiguration();
		 	CombinedTypeSolver com = new CombinedTypeSolver(new ReflectionTypeSolver(true));
		 	ps.setSymbolResolver(new JavaSymbolSolver(com));
		 	JavaParser jp=new com.github.javaparser.JavaParser(ps);
		 	JavaParser.setStaticConfiguration(ps);
	        CompilationUnit cu = jp.parse(in);
	        
	        
	        cu.accept(new MethodVisitor(), null);
	}
	public static void main(String[] args){
	
		File f = new File("/Users/jeetmehta/Dropbox/CS230/Project/pom-parser/src");
		parseApi(f);
	}
	
}



class MethodVisitor extends VoidVisitorAdapter<Void> {
    
    
    HashSet<ApiStorage> functions;
	HashSet<String> fullQualifiedHash;
	String jarpath[];
	CombinedTypeSolver com;
	
	public MethodVisitor(HashSet<ApiStorage> functions,HashSet<String> fullQualifiedHash  ) {
		this.functions = functions;
		this.fullQualifiedHash = fullQualifiedHash;
		com = new CombinedTypeSolver(new ReflectionTypeSolver(true));
		for(String s: jarpath){
			try{
				com.add(new JarTypeSolver(new File(s)));
//				com.add(new JarTypeSolver(new File("/Users/jeetmehta/.m2/repository/junit/junit/3.8.1/junit-3.8.1.jar")));
//		        com.add(new JarTypeSolver(new File("C:\\Users\\kprat\\.m2\\repository\\com\\google\\guava\\guava\\23.4-jre\\guava-23.4-jre.jar")));
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
    public void visit(MethodDeclaration n, Void arg) {
        /* here you can access the attributes of the method.
         this method will be called for all methods in this 
         CompilationUnit, including inner class methods */
    	try{
    		System.out.println("-------------------");
	        ResolvedMethodDeclaration m = n.resolve();	    
//	        System.out.println("Qualified name "+m.getQualifiedName());
	        JavaParserMethodDeclaration tp = new JavaParserMethodDeclaration(n, com);
	       
	        ArrayList<String> parameters = null;
	        if(tp.accessSpecifier().asString().contains("public")){
	        	parameters = new ArrayList<String>();
		        for (int i=0;i< tp.getNumberOfParams();i++){
		        	System.out.println("Param type: "+tp.getParam(i).describeType());
		        	parameters.add(tp.getParam(i).describeType());
		        	System.out.println("Param name:"+tp.getParam(i).getName());
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
