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

public class myJavaParser {

	public static void main(String[] args) throws Exception {
		// creates an input stream for the file to be parsed
		FileInputStream in = new FileInputStream(
				"C:\\Users\\kprat\\git\\pom-parser\\src\\main\\java\\CS230\\pom_parser\\sample.java");
		// FileInputStream in = new
		// FileInputStream("main\\java\\CS230\\pom_parser\\sample.java");

		// parse the file
		File dir=new File("C:\\Users\\kprat\\git\\pom-parser\\src\\main\\java\\sample");
		String jarnames[]= {"junit-3.8.1.jar"};
		parseCode(dir,jarnames,"pomparser");
		
		
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

	public static void parseCode(File dir, String jarnames[], String repo) {

		HashSet<ApiStorage> functions = new HashSet<ApiStorage>();
		HashSet<String> fullQualifiedHash = new HashSet<String>();
		String s[] = new String[1];
		s[0] = "java";
		ParserConfiguration ps = new ParserConfiguration();

		JavaParser jp = new com.github.javaparser.JavaParser(ps);
		JavaParser.setStaticConfiguration(ps);
		CombinedTypeSolver com = new CombinedTypeSolver(new ReflectionTypeSolver(true));
		for (String su : jarnames) {
			try {
				com.add(new JarTypeSolver(new File("./jars/" + su)));
				// com.add(new JarTypeSolver(new
				// File("/Users/jeetmehta/.m2/repository/junit/junit/3.8.1/junit-3.8.1.jar")));
				// com.add(new JarTypeSolver(new
				// File("C:\\Users\\kprat\\.m2\\repository\\com\\google\\guava\\guava\\23.4-jre\\guava-23.4-jre.jar")));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		ps.setSymbolResolver(new JavaSymbolSolver(com));
		
		//
		
		Collection<File> files = FileUtils.listFiles(dir, s, true);
		for (File file : files) {
			System.out.println(file.getName());
			// parseJavaFile(file, functions,fullQualifiedHash, jp, com);
			try {
				FileInputStream in = new FileInputStream(file);
				CompilationUnit cu = jp.parse(in);
				cu.accept(new MethodCallVisitor(functions, fullQualifiedHash, com), null);
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

	private static class MethodCallVisitor extends VoidVisitorAdapter<Void> {

		HashSet<ApiStorage> functions;
		HashSet<String> fullQualifiedHash;
		CombinedTypeSolver com;

		public MethodCallVisitor(HashSet<ApiStorage> functions, HashSet<String> fullQualifiedHash, CombinedTypeSolver com) {
			this.functions = functions;
			this.fullQualifiedHash = fullQualifiedHash;
			this.com = com;
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

				SymbolReference<ResolvedMethodDeclaration> p = a.solve(n);
				p.getCorrespondingDeclaration();
				// JavaParserMethodDeclaration tp =new
				// JavaParserMethodDeclaration(, com);
				// p.getClass()
				System.out.println("xxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("Qualified name:" + p.getCorrespondingDeclaration().getQualifiedName());
				String qName= p.getCorrespondingDeclaration().getQualifiedName();
				ArrayList<String> params=new ArrayList<String>();
				p.getCorrespondingDeclaration().getQualifiedSignature();
				for (int i = 0; i < p.getCorrespondingDeclaration().getNumberOfParams(); i++) {
					System.out.println("Param type: " + p.getCorrespondingDeclaration().getParam(i).describeType());
					params.add(p.getCorrespondingDeclaration().getParam(i).describeType());

				}
				
				p.getCorrespondingDeclaration().getQualifiedName();
				functions.add(new ApiStorage(qName, params));
		        fullQualifiedHash.add(qName);
				// p.getCorrespondingDeclaration().getParam(0).describeType();
				// System.out.println(p);
				super.visit(n, arg);
			} catch (Exception e) {
				e.printStackTrace();
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