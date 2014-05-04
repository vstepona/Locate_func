/* LocateFunc.java
 * 2014-03-12
	//--------------------------------------------
	// Requires paths to source files.
	public static void main(String[] args)
	{
		LocateFunc dataRecords = new LocateFunc();
		dataRecords.parseFiles(args);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e){
			System.out.println("Thread interrupted.");
		}
		dataRecords.dumpFunctionTable();
	}
*/

//package loc_func;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

// Java parsing library
import japa.parser.*;
import japa.parser.ast.*;
import japa.parser.ast.visitor.*;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.*;


//----------------locate functions------------------------------------
public class LocateFunc {
	//--------------------------------------------
	// Function definition location information object.
	public class Definition {
		String fileName = "NOTFOUND";
		Integer line = -1;
	}


	//--------------------------------------------
	// Helper structure for FuncInfo.
	// Also used for returning general location information for a call.
	public class Position {
		public Integer line = -1;
		public Integer begin = -1;
		public Integer end = -1;
		public Integer length = -1;
	}
	//--------------------------------------------
	// Helper structure for functionTable.
	private class FuncInfo {
		// Line number of where the function is defined.
		public Integer definition = -1;
		// List of line numbers of where the function is called.
		//public List<Integer> calls = new ArrayList<Integer>();
		// List of (line number, begin position, name length) tuples of where the function is called.
		public List<Position> calls = new ArrayList<Position>();
	}
	//--------------------------------------------
	// Table for "function names" to "function definition location" and 
	// "function call locations"; order independent.
	// {fileName: {funcName: {"definition": line#, "call": [line#, line#]} } }
	private Map<String, Map<String, FuncInfo>> functionTable;


	//--------------------------------------------
	// Table for keeping track of line numbers and their position in the 
	// active file. Each record is a key,value pair where the line number is 
	// the key, and the value is the byte-offset from the beginning of the 
	// file to the first character of the line.
	//private Map<String, Map<Integer, Integer>> lineTable;
	private Map<Integer, Integer> lineTable;


	//--------------------------------------------
	// Constructors
	LocateFunc()
	{
		functionTable = new HashMap<String, Map<String, FuncInfo>>();
		lineTable = new HashMap<Integer, Integer>();
		//lineTable = new HashMap<String, Map<Integer, Integer>>();
	}
	LocateFunc(String[] fileNames)
	{
		this();
		parseFiles(fileNames);
	}


	//----------parse files using threads----------
	public void parseFiles(String[] fileNames)
	{
		ArrayList<ParseByThread> threadList = new ArrayList<ParseByThread>();
		// For each file create a thread
		for (String file : fileNames){
			ParseByThread t = new ParseByThread(file);
			t.start();
			// Keep a list of created threads
			threadList.add(t);
		}

		// Wait for the threads to finish
		try {
			for (ParseByThread t : threadList){
				t.join();
			}
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}


	//--------------------------------------------
	// Parse a source file to object
	private void readFileInToParseObjects(String fileName)
	{
		buildLineToOffsetTable(fileName);
		try (FileInputStream inFile = new FileInputStream(fileName)){
			CompilationUnit cu = new CompilationUnit();
			try {
				//buildLineToOffsetTable(inFile); Using Java7 Files instead.
				try {
					// parse the file
					cu = JavaParser.parse(inFile);
				} catch (Exception e){
					e.printStackTrace();
				} finally {
				}
			} catch (Exception e){
				e.printStackTrace();
			}

			//System.out.println(cu.toString());
			// visit and print the methods names
			new MethodDefinitionVisitor().visit(cu, fileName);
			new MethodCallVisitor().visit(cu, fileName);

		} catch (IOException ioe){
			System.out.println("Input/Output error.");
			ioe.printStackTrace();
			System.exit(1);
		}
	}


	//--------------------------------------------
	// Only the line number is known from japa.
	// File relative byte-offset is needed for UI positioning methods.
	private void buildLineToOffsetTable(String fileName)
	{
		// Using Java7 Files instead of file stream or buffer reader.
		List<String> lines = new ArrayList<String>();
		Integer offset = 0;
		try {
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
			for (int i = 0; i < lines.size(); ++i){
				lineTable.put(i, offset);
				offset += lines.get(i).length();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		System.out.println(" -------- LINE TABLE ----------");
		System.out.println(lineTable);
		/*
		try (FileInputStream inFile = new FileInputStream(fileName)){
			buildLineToOffsetTable(inFile);
		} catch (IOException e){
			e.printStackTrace();
		}
		*/
	}
	private void buildLineToOffsetTable(FileInputStream inFile)
		throws IOException
	{
		/*
		List<String> lines = new ArrayList<String>();
		Integer offset = 0;
		try {
			//lines = Files.readAllLines(inFile, StandardCharsets.UTF_8);
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		} catch (IOException e){
			e.printStackTrace();
		}
		*/
	}


	//--------------------------------------------
	// For some file, record a method's definition location.
	// Location is only recorded as line number for definitions.
	private void addDefinition(
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
	// For some file, record the location of a method call.
	// Location is line number and character position for calls.
	/*
	private void addCall(
		String fileName, 
		MethodCallExpr callInfo
	)
	*/
	private void addCall(
		String fileName, 
		String functionName,
		Integer lineNumber
	)
	{
//addDefinition(arg.toString(), n.getName(), n.getBeginLine());
//n.getScope()
//n.getData()
		//String functionName = callInfo.getName();
		Position p = new Position();
		p.line = lineNumber;
		//p.begin = 
		//p.end = 
		//p.length = 

		if (functionTable.containsKey(fileName)){
			if (functionTable.get(fileName).containsKey(functionName)){
				functionTable.get(fileName).get(functionName).calls.add(p);
			} else {
				FuncInfo info = new FuncInfo();
				info.calls.add(p);
				functionTable.get(fileName).put(functionName, info);
			}
		} else {
			FuncInfo info = new FuncInfo();
			info.calls.add(p);
			Map<String, FuncInfo> function = new HashMap<String, FuncInfo>();
			function.put(functionName, info);
			functionTable.put(fileName, function);
		}
	}


	//--------------------------------------------
	// Simple visitor implementation for visiting MethodDeclaration nodes. 
	// Find function definitions
	private class MethodDefinitionVisitor extends VoidVisitorAdapter<Object>
	{
		@Override
		public void visit(MethodDeclaration n, Object arg){
			// Here is where attributes of the method can be accessed.
			// This visit method will be called for all methods in the 
			// CompilationUnit, including inner class methods.
			//if (n.getScope() != null){
			//	addDefinition(arg.toString(), n.getScope() + n.getName(), n.getBeginLine());
			//} else {
				addDefinition(arg.toString(), n.getName(), n.getBeginLine());
			//}
		}
	}


	//--------------------------------------------
	// Find function calls	
	private class MethodCallVisitor extends VoidVisitorAdapter<Object>
	{
		@Override
		public void visit(MethodCallExpr n, Object arg){
			// Here is where attributes of the method can be accessed.
			// This visit method will be called for all methods in the 
			// CompilationUnit, including inner class methods.
			if (n.getScope() != null){
				addCall(arg.toString(), n.getScope() + n.getName(), n.getBeginLine());
			} else {
				addCall(arg.toString(), n.getName(), n.getBeginLine());
			}
			//addCall(arg.toString(), n.getName(), n.getBeginLine());
			//addCall(arg.toString(), n, arg.toString());
			System.out.println(MethodCallExpr.class.getMethods().toString());
System.out.println("file: " + arg.toString() + ", scope: " + n.getScope() + ", funcName: " + n.getName() + ", other data: " + n.getData());
//n.getScope()
//n.getData()
		}
	}


	//--------------------------------------------
	// Accessor for retrieving method call locations in a file.
	public List<Position> callsLocations(String fileName)
	{
		List funcCalls = new ArrayList<Position>();
		for (String functionName : functionTable.get(fileName).keySet()){
			// `calls` is a list of Position objects.
			funcCalls.addAll(functionTable.get(fileName).get(functionName).calls);
		}
		return funcCalls;
	}


	//--------------------------------------------
	// Accessor for retrieving the file name and line number of where a 
	// method is defined.
	public Definition callDefinitionLocation(String findFunc)
	{
		Definition def = new Definition();

		// Iterate through every file's records
		for (String fileName : functionTable.keySet()){
			// If there is a record for the function and it has a definition 
			// set, then return the found information.
			if (functionTable.get(fileName).containsKey(findFunc) && 
				(-1 != functionTable.get(fileName).get(findFunc).definition))
			{
				def.fileName = fileName;
				def.line = functionTable.get(fileName).get(findFunc).definition;
				return def;
			}
		}
		return def;
	}


	// A method for debugging the logging table, that record function locations.
	public void dumpFunctionTable()
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


	//--------------------------------------------------------------------
	private class ParseByThread
		extends Thread
	{
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
			readFileInToParseObjects(threadName);
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
}

