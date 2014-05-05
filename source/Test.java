/* Test.java
*/

public class Test
{
	public static void main(String[] args)
	{
		if (args.length < 1){
			System.out.println("Requires at least one source file for testing.");
			System.exit(1);
		}
		LocateFunc dataRecords = new LocateFunc();
		dataRecords.parseFiles(args);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e){
			System.out.println("Thread interrupted.");
		}

		dataRecords.dumpFunctionTable();
		for (LocateFunc.Position p : dataRecords.callsLocations(args[0])){
			System.out.println("line: " + p.line + ", begin: " + p.begin + ", end: " + p.end + ", length: " + p.length);
		}

		dataRecords.dumpLineTable();
	}
}

