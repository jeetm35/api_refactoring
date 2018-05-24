package CS230.pom_parser;
import java.util.*;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;


import java.io.*;
 

public class myJavaParser {

	 public static void main(String[] args) throws Exception {
	        // creates an input stream for the file to be parsed
		 FileInputStream in = new FileInputStream("C:\\Users\\kprat\\git\\pom-parser\\src\\main\\java\\CS230\\pom_parser\\sample.java");

	        // parse the file
		 	ParserConfiguration ps=new ParserConfiguration();
		 	CombinedTypeSolver com = new CombinedTypeSolver(new ReflectionTypeSolver(true));
		 	ps.setSymbolResolver(new JavaSymbolSolver(com));
		 	JavaParser jp=new com.github.javaparser.JavaParser(ps);
		 	JavaParser.setStaticConfiguration(ps);
	        CompilationUnit cu = jp.parse(in);
	        
	        
	        cu.accept(new MethodVisitor(), null);
	   
	        // prints the resulting compilation unit to default system output
	    }
	 
	 private static class MethodVisitor extends VoidVisitorAdapter<Void> {
		    @Override
//		    public void visit(MethodCallExpr n, Void arg) {
//		        /* here you can access the attributes of the method.
//		         this method will be called for all methods in this 
//		         CompilationUnit, including inner class methods */
//		    	try{
//		    		System.out.println(n.getName());
//			        System.out.println(n);
//			        CombinedTypeSolver com = new CombinedTypeSolver(new ReflectionTypeSolver(true));
//			        com.add(new JarTypeSolver(new File("/Users/jeetmehta/.m2/repository/junit/junit/3.8.1/junit-3.8.1.jar")));
//			        JavaParserFacade a= JavaParserFacade.get(com);
//			        SymbolReference<ResolvedMethodDeclaration> p = a.solve(n);
//			        System.out.println(p);
//			        super.visit(n, arg);
//		    	}
//		        catch(Exception e){
//		        	e.printStackTrace();
//		        }
//		    }
		    
		    public void visit(MethodDeclaration n, Void arg) {
		        /* here you can access the attributes of the method.
		         this method will be called for all methods in this 
		         CompilationUnit, including inner class methods */
		    	try{
		    		System.out.println(n.getName());
		    		n.getModifiers();
		    		n.getParameters();
		    		n.getReceiverParameter();
		    		n.getSignature();
		    		n.getTypeParameters();
		    		n.getParsed();
		    		
		    		
			        ResolvedMethodDeclaration m = n.resolve();
			        m.declaringType()
			        m.getClassName()
			        m.getPackageName()
			        m.getQualifiedName()
			        m.getQualifiedSignature()
			        m.getParam(0).getClass()
			        
			       
			        CombinedTypeSolver com = new CombinedTypeSolver(new ReflectionTypeSolver(true));
			        com.add(new JarTypeSolver(new File("C:/Users/kprat/.m2/repository/junit/junit/3.8.1/junit-3.8.1.jar")));
			        JavaParserFacade a= JavaParserFacade.get(com);
//			        SymbolReference<ResolvedMethodDeclaration> p = a.solve(m);
//			        System.out.println(p);
			        super.visit(n, arg);
		    	}
		        catch(Exception e){
		        	e.printStackTrace();
		        }
		    }
		    
		}
}