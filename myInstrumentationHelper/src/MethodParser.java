import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;


public class MethodParser {

	
	public static void parse(String str, String path, String sourcePath, BufferedWriter bw) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		
		parser.setBindingsRecovery(true);
		parser.setUnitName(path);
		
		//parser.setProject();
		String [] sources = {sourcePath};
		String[] classpath = {"C:\\Sneha\\Studies\\UCLA\\Classes\\Q3Spring2018\\CS230\\Project\\org.eclipse.jdt.core-3.7.1.jar" };
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		parser.setSource(str.toCharArray());
		
 
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		
		if (cu.getAST().hasBindingsRecovery()) {
			//System.out.println("Binding activated!!!");
		}
		
		cu.accept(new ASTVisitor() {

			
		    public boolean visit(MethodDeclaration node) {  
		    	//System.out.println("declaring method '" + node.getName() + "' that returns " + node.getReturnType2());

		    	int modifiers = node.getModifiers();
		    	int lineNumber = cu.getLineNumber(node.getName().getStartPosition());
		    	try {
		    	if (Modifier.isPublic(modifiers) && !Modifier.isAbstract(modifiers)) {
		    		IMethodBinding im =  node.resolveBinding();
			    	String fullClassName=im.getDeclaringClass().getQualifiedName();
			    	String methodName = node.getName().toString();
			    	String param="(";
			    	String fullName=fullClassName+"."+node.getName();
			    	String className = im.getDeclaringClass().getName();
			    	
			    	List<?> mList=node.modifiers();
			    	  for (int i=mList.size() - 1; i >= 0; i--) {
			    		    Object obj=mList.get(i);
			    		    if (obj instanceof Annotation) {
			    		    	mList.remove(obj);
			    		    }
			    		  }
			    	
			    	
			    	//System.out.println("ClassName="+className);
			    	//System.out.println("Fully qualified name="+fullName);
			    	//System.out.println(fullName+" declared at line number "+lineNumber);
			    	if(!(className.equals(methodName))) {
			    	List<?> parameters = node.parameters();
			    	for(int i = 0; i < parameters.size(); i++) {
			    		SingleVariableDeclaration var = (SingleVariableDeclaration) parameters.get(i);
			    		if(i==parameters.size()-1) param+=var.getName();
			    		else param+=var.getName()+","; 		
			    	}
			    	param+=")";
			    	
			    	//System.out.println("Method parameters="+param);
			    	String rType = node.getReturnType2().toString();
			    	//System.out.println("Method return type="+rType);
			    	String lineToBePrinted = path+"|"+fullName+"|"+lineNumber+"|"+param+"|"+rType;
			    	System.out.println(lineToBePrinted);
			    	System.out.println("********************************************************");
			    	String output = lineToBePrinted+"\n";
			    	bw.write(output);
			    	}
			        return true;	
		    	}
		    	} catch(Exception e) {}
		        return false;		        
		    }
			
		});	
	}
	
	//read file content into a string
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
 
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			//System.out.println(numRead);
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
 
		reader.close();
 
		return  fileData.toString();	
	}
	
	public static void parseFilesInDir() throws IOException {
		File dirs = new File("C:\\Sneha\\Studies\\UCLA\\Classes\\Q3Spring2018\\CS230\\Project\\commons-io-2.4-src\\commons-io-2.4-src");
		//File dirs = new File("C:\\Sneha\\Studies\\UCLA\\Classes\\Q3Spring2018\\CS230\\Project\\api-refactoring\\api_refactoring\\Sample");
		String s[] = new String[1];
		s[0] = "java";
		File[] myDir = (dirs.listFiles());
		
		File parsedFile = new File("parsedCommonsIO.txt");
		FileWriter fw = new FileWriter(parsedFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        
		for(File f: myDir) {
			if(f.isDirectory()) {
				if(f.getName().equals("src")) {
					System.out.println("Found src at "+f.getAbsolutePath());	
					Collection<File> files = FileUtils.listFiles(f, s, true);
					for(File file : files){
					   // String sourcePath=file.getCanonicalPath();
					    String filePath = file.getAbsolutePath();
					    System.out.println("Parsing: "+filePath);
					    if(file.isFile()) {
							parse(readFileToString(filePath), filePath, f.getName(), bw);
						}
					    
					}
					
				}
			}
		}	
		bw.close();

	}
	
	public static void main(String args[]) throws IOException {
		System.out.println("Starting...");
		parseFilesInDir();
	}
}
