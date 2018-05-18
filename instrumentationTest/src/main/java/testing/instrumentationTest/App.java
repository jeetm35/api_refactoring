package testing.instrumentationTest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.osgi.service.environment.Constants;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;


import org.eclipse.jdt.internal.corext.callhierarchy.CallHierarchy;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;
import org.eclipse.jdt.internal.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ParseException, IOException
    {
        System.out.println( "Hello World!" );
        usingParser();
        
        
    }
    
    
    public void usingHierarchy(){
    	
    }


	public static void usingParser() {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        
         parser.setBindingsRecovery(true);
         
 		Map options = JavaCore.getOptions();
 		parser.setCompilerOptions(options);
  
 		String unitName = "sample.java";
 		parser.setUnitName(unitName);
  
 		String[] sources = { "C:\\Users\\kprat\\workspace\\instrumentationTest\\src" }; 
 		String[] classpath;// = {"C:\\Program Files\\Java\\jre1.8.0_25\\lib\\rt.jar"};
  
 		parser.setEnvironment(null, sources, new String[] { "UTF-8"}, true);
        String fileName="C:\\Users\\kprat\\workspace\\instrumentationTest\\src\\main\\java\\testing\\instrumentationTest\\sample.java";
        char[] source=getChar(fileName);
        parser.setSource(source);
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        
        System.out.println( "Hello World!" );
        
        /*cu.accept(new ASTVisitor() {
        	 
			Set names = new HashSet();
 
			public boolean visit(MethodInvocation node) {
               
				return true;
            }
 
		});*/
        //This does not work. Not able to bind variables to their respective classes!
        cu.accept(new ASTVisitor() {
           
                        public boolean visit(MethodInvocation node) {
                            System.out.println("Name: " + node.getName());

                            Expression expression = node.getExpression();
                            if (expression != null) {
                                System.out.println("Expr: " + expression.toString());
                                ITypeBinding typeBinding = expression.resolveTypeBinding();
                                typeBinding.getPackage();
                                if (typeBinding != null) {
                                    System.out.println("Type: " + typeBinding.getName());
                                }
                            }
                            IMethodBinding binding = node.resolveMethodBinding();
                            if (binding != null) {
                                ITypeBinding type = binding.getDeclaringClass();
                                if (type != null) {
                                    System.out.println("Decl: " + type.getName());
                                }
                            }

                            return true;
                        }
                        
                        
                      
                    });
        
        
        /*cu.accept(new ASTVisitor() {

            public boolean visit(MethodDeclaration node) {                                                  

            	
            	
            	
            	node.resolveBinding().getJavaElement();                                             
                System.out.println(node.getName());
            	return true;
            }

           
        });*/
               
	}
    
    
    public static char[] getChar(String name){
    	StringBuilder fileContents = new StringBuilder();
		File file = new File(name);
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine()).append("\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileContents.toString().toCharArray();
    }
    
    public void just(){
    	
    }
    
    
    public HashSet<IMethod> getCallersOf(IMethod m) {
     
     CallHierarchy callHierarchy = CallHierarchy.getDefault();
     
     IMember[] members = {m};
     
     MethodWrapper mw = callHierarchy.getCallerRoot(m);
      HashSet<IMethod> callers = new HashSet<IMethod>();
      
        MethodWrapper[] mw2 = mw.getCalls(new NullProgressMonitor());
        HashSet<IMethod> temp = getIMethods(mw2);
        callers.addAll(temp);    
       
     
    return callers;
    }
     
     HashSet<IMethod> getIMethods(MethodWrapper[] methodWrappers) {
      HashSet<IMethod> c = new HashSet<IMethod>(); 
      for (MethodWrapper m : methodWrappers) {
       IMethod im = getIMethodFromMethodWrapper(m);
       if (im != null) {
        c.add(im);
       }
      }
      return c;
     }
     
     IMethod getIMethodFromMethodWrapper(MethodWrapper m) {
      try {
       IMember im = m.getMember();
       if (im.getElementType() == IJavaElement.METHOD) {
        return (IMethod)m.getMember();
       }
      } catch (Exception e) {
       e.printStackTrace();
      }
      return null;
     }
    
    
    
}
