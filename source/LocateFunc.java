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
	private static class FuncInfo {
		public Integer definition = -1;
		public List<Integer> calls = new ArrayList<Integer>();
	}
	// Table for "function names" to "function definition location" and 
	// "function call locations"; order independent.
	// {fileName: {funcName: {"definition": line#, "call": [line#, line#]} } }
	private static Map<String, Map<String, FuncInfo>> functionTable = 
		new HashMap<String, Map<String, FuncInfo>>();

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
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e){
			System.out.println("Thread interrupted.");
		}
		dumpFunctionTable();
		/*
		System.out.println(functionTable);
		System.out.println(functionTable.get("testSourceFiles/testClass1.java").get("foo").definition);
		System.out.println(functionTable.get("testSourceFiles/testClass2.java").get("foo").call);
		*/
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
	// Record a definition location
	private static void addDefinition(
		String fileName, 
		String functionName,
		Integer lineNumber
	)
	{
		if (functionTable.containsKey(fileName)){
			if (functionTable.get(fileName).containsKey(functionName)){
				functionTable.get(fileName).get(functionName)
					.definition = lineNumber;
			} else {
				FuncInfo info = new FuncInfo();
				info.definition = lineNumber;
				functionTable.get(fileName).put(functionName, info);
			}
		} else {
			FuncInfo info = new FuncInfo();
			info.definition = lineNumber;
			//info.calls = new ArrayList<Integer>();
			Map<String, FuncInfo> function = new HashMap<String, FuncInfo>();
			function.put(functionName, info);
			functionTable.put(fileName, function);
		}
	}


	//--------------------------------------------
	// Record a call location
	private static void addCall(
		String fileName, 
		String functionName,
		Integer lineNumber
	)
	{
		if (functionTable.containsKey(fileName)){
			if (functionTable.get(fileName).containsKey(functionName)){
				functionTable.get(fileName).get(functionName)
					.calls.add(lineNumber);
			} else {
				FuncInfo info = new FuncInfo();
				info.calls.add(lineNumber);
				functionTable.get(fileName).put(functionName, info);
			}
		} else {
			FuncInfo info = new FuncInfo();
			//info.calls = new ArrayList<Integer>();
			info.calls.add(lineNumber);
			Map<String, FuncInfo> function = new HashMap<String, FuncInfo>();
			function.put(functionName, info);
			functionTable.put(fileName, function);
		}
	}

	//--------------------------------------------
	// Simple visitor implementation for visiting MethodDeclaration nodes. 
	//find function definitions
	private static class MethodVisitor extends VoidVisitorAdapter<Object>{
		@Override
		public void visit(MethodDeclaration n, Object arg){
			// Here is where attributes of the method can be accessed.
			// This visit method will be called for all methods in the 
			// CompilationUnit, including inner class methods.
//			System.out.println("Declaration: " + n.getName() + " " + n.getBeginLine() + " " + arg.toString());
			addDefinition(arg.toString(), n.getName(), n.getBeginLine());
		}
	}


	//--------------------------------------------
	//find function calls	
	private static class MethodCallVisitor extends VoidVisitorAdapter<Object>{
		@Override
		public void visit(MethodCallExpr n, Object arg){
			// Here is where attributes of the method can be accessed.
			// This visit method will be called for all methods in the 
			// CompilationUnit, including inner class methods.
//			System.out.println("Call: " + n.getName() + " " + n.getBeginLine() + " " + arg.toString());
			addCall(arg.toString(), n.getName(), n.getBeginLine());
		}
	}


	//--------------------------------------------
	// A method for debugging the logging table, that record function locations.
	private static void dumpFunctionTable()
	{
		System.out.println();
		System.out.println("================================================================================");
		System.out.println("Dumping function records table...");
		System.out.println("================================================================================");

		for (String fileName : functionTable.keySet()){
			System.out.println("--------------------------------------------------------------------------------");
			System.out.println("File name: " + fileName);
			System.out.format("\tFunctions:\n");
			for (String functionName : functionTable.get(fileName).keySet()){
				System.out.format("\t\tName: " + functionName + "\n");
				System.out.format("\t\t\tDefinition: " + 
					functionTable.get(fileName).get(functionName).definition + "\n");
				System.out.format("\t\t\tCalls: " + 
					functionTable.get(fileName).get(functionName).calls + "\n");
			}
		}

		System.out.println("================================================================================");
		System.out.println();
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

