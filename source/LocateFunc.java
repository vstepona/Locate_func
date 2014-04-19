/* LocateFunc.java
 *
 * 2014-03-12
*/

//package loc_func;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
/*
import java.util.regex.Pattern;
import java.util.regex.Matcher;
*/

// Java parsing library
import japa.parser.*;
import japa.parser.ast.*;
import japa.parser.ast.visitor.*;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.*;


//----------------locate functions------------------------------------
public class LocateFunc {
	private class FuncInfo {
		public Integer definition = -1;
		public List<Integer> cal = new ArrayList<Integer>();
	}
	// Table for "function names" to "function definition location" and 
	// "function call locations"; order independent.
	// {funcName: {"definition": line#, "call": [line#, line#]}}
	private Map<String, FuncInfo> functionTable = 
		new HashMap<String, FuncInfo>();
	//private Map<String, Map<String, List<Integers>>> functionTable;

	//--------------------------------------------
	// Requires a path to source files.
	public static void main(String[] args)
	{
/*
		String path = "C:/Users/vid/Desktop/kent state/2014 spring/Structure of programming languages/Project Locate_func/Locate_func/loc_fun_5_0/src/testSourceFiles/";
		String f1 = path + "testClass1.java";
		String f2 = path + "testClass2.java";
		String[] files = {f1, f2};
		//files[0] = f1;
		//files[1] = f2;

		//parse files 
		parseFiles(files);
*/
		parseFiles(args);
	}


	//----------parse files using threads----------
	private static void parseFiles(String[] fileNames)
	{
		//for each file create a thread
		for (String file : fileNames){
			ParseByThread T1 = new ParseByThread(file);
			T1.start();
		}
	}


	//--------------------------------------------
	// Parse a source file to object
	//private static Map<String, List<Integer>> findFuncCalls(String fileName)
	public static void readFileToParseObjects(String fileName)
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
			new MethodVisitor().visit(cu, fileName);
			new MethodCallVisitor().visit(cu, fileName);

		} catch (IOException ioe){
			System.out.println("Input/Output error.");
			System.exit(1);
		}
	}


	//--------------------------------------------
	// Simple visitor implementation for visiting MethodDeclaration nodes. 
	//find function definitions
	private static class MethodVisitor extends VoidVisitorAdapter<Object> {
		@Override
		public void visit(MethodDeclaration n, Object arg) {
			// here you can access the attributes of the method.
			// this method will be called for all methods in this 
			// CompilationUnit, including inner class methods
			System.out.println("Declaration: " + n.getName() + " " + n.getBeginLine() + " " + arg.toString());
		}
	}


	//--------------------------------------------
	//find function calls	
	private static class MethodCallVisitor extends VoidVisitorAdapter<Object> {
		@Override
		public void visit(MethodCallExpr n, Object arg) {
			// here you can access the attributes of the method.
			// this method will be called for all methods in this 
			// CompilationUnit, including inner class methods
			System.out.println("Call: " + n.getName() + " " + n.getBeginLine() + " " + arg.toString());
		}
	}

}


//--------------------------------------------------------------------
class ParseByThread extends Thread {
	private Thread t;
	private String threadName;


	//--------------------------------------------
	ParseByThread(String name){
		threadName = name;
		System.out.println("Creating thread " +  threadName);
	}


	//--------------------------------------------
	public void run() {
		System.out.println("Running " +  threadName);
		//try {
			LocateFunc.readFileToParseObjects(threadName);
			//Thread.sleep(50);
		//} catch (InterruptedException e){
		//	System.out.println("Thread " + threadName + " interrupted.");
		//}
		System.out.println("Thread " +  threadName + " exiting.");
	}


	//--------------------------------------------
	public void start ()
	{
		System.out.println("Starting " +  threadName );
		if (t == null)
		{
			t = new Thread (this, threadName);
			t.start ();
		}
	}

}

