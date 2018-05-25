package CS230.pom_parser;


import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

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
		//Make the CU unit and make compilation unit.
	}
	public static void main(String[] args){
	
		File f = new File("/Users/jeetmehta/Dropbox/CS230/Project/pom-parser/src");
		parseApi(f);
	}
	
}



class MethodVisitor extends VoidVisitorAdapter<Void> {
    
    
    HashSet<ApiStorage> functions = new HashSet<ApiStorage>();
	HashSet<String> fullQualifiedHash = new HashSet<String>();
	
	public MethodVisitor(HashSet<ApiStorage> functions,HashSet<String> fullQualifiedHash  ) {
		this.functions = functions;
		this.fullQualifiedHash = fullQualifiedHash;
	}
	
	@Override
    public void visit(MethodDeclaration n, Void arg) {
        /* here you can access the attributes of the method.
         this method will be called for all methods in this 
         CompilationUnit, including inner class methods */
    	try{
    		System.out.println("-------------------");
    		CombinedTypeSolver com = new CombinedTypeSolver(new ReflectionTypeSolver(true));
	        com.add(new JarTypeSolver(new File("/Users/jeetmehta/.m2/repository/junit/junit/3.8.1/junit-3.8.1.jar")));
//	        com.add(new JarTypeSolver(new File("C:\\Users\\kprat\\.m2\\repository\\com\\google\\guava\\guava\\23.4-jre\\guava-23.4-jre.jar")));
	        

	        ResolvedMethodDeclaration m = n.resolve();	    
	        System.out.println("Qualified name "+m.getQualifiedName());
	        fullQualifiedHash.add(m.getQualifiedName());
	        JavaParserMethodDeclaration tp =new JavaParserMethodDeclaration(n, com);
	       
	        ArrayList<String> parameters = new ArrayList<String>();
	        for (int i=0;i< tp.getNumberOfParams();i++){
	        	System.out.println("Param type: "+tp.getParam(i).describeType());
	        	parameters.add(tp.getParam(i).describeType());
	        	System.out.println("Param name:"+tp.getParam(i).getName());
	        }
	        ApiStorage temp = new ApiStorage(m.getQualifiedName(), tp.accessSpecifier().asString(), parameters, tp.getReturnType().describe());
	        functions.add(temp);

	        super.visit(n, arg);
    	}
        catch(Exception e){
        	e.printStackTrace();
        }
    }
    
}
