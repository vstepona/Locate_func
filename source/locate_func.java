/* locate_func.java
 *
 * 2014-03-02
*/

import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.Map;

import japa.parser.*;
import japa.parser.ast.*;
import japa.parser.ast.visitor.*;
import japa.parser.ast.body.*;

//----------------------------------------------------------------------
class LocateFunc
{

//----------------------------------------
// Requires a path to source files.
public static void main(String[] args)
{
	findFuncCalls("testSourceFiles/testClass1.java");
}


//----------------------------------------
// Parse a source file to find function calls.
//private static Map<String, List<Integer>> findFuncCalls(String fileName)
private static void findFuncCalls(String fileName)
{
	try {
		FileInputStream inFile = new FileInputStream(fileName);

		CompilationUnit cu = new CompilationUnit();
		try {
			// parse the file
			cu = JavaParser.parse(inFile);
		} catch (Exception e){
		} finally {
			inFile.close();
		}

		//System.out.println(cu.toString());
		// visit and print the methods names
		new MethodVisitor().visit(cu, null);

/*
		BufferedReader inFile = new BufferedReader(new FileReader(fileName));

		// Function name pattern matching
		Pattern funcStart = Pattern.compile("([a-zA-Z_]{1}[a-zA-Z_0-9]+\\s*\\()");
		Pattern funcEnd = Pattern.compile("(\\))");

		Integer count = 1;
		while (inFile.ready()){
			String line = inFile.readLine();

			Matcher s = funcStart.matcher(line);
			while (s.find()){
				System.out.print("line: " + count.toString());
				System.out.println(" " + s.group(1).toString());
				Matcher e = funcEnd.matcher(line);
				if (e.find(s.end(1))){
					System.out.print("location of ) ");
					System.out.println(e.end(1));
					if (e.end(1) < line.length()){
						if (line.charAt(e.end(1)) == '{'){
							System.out.println("Found method implementation.");
						}
					}
				}
			}

//			ArrayList<String> lineData= 
//				new ArrayList<String>(Arrays.asList(line.split(",")));
//			for (String s : lineData) s = s.trim();
//
//			data.add(lineData);

			count++;
		}
*/
	} catch (IOException ioe){
		System.out.println("Input/Output error.");
		System.exit(1);
	}
}

// Simple visitor implementation for visiting MethodDeclaration nodes. 
private static class MethodVisitor extends VoidVisitorAdapter {
	@Override
	public void visit(MethodDeclaration n, Object arg) {
		// here you can access the attributes of the method.
		// this method will be called for all methods in this 
		// CompilationUnit, including inner class methods
		System.out.println(n.getName() + " " + n.getBeginLine());
	}
}


}

