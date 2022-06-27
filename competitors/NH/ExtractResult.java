import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class ExtractResult {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// Hard-coded to extract the results to excel files
		
		// HP-adult
		String programs[] = {"AT", "HS", "NH"};
		String output = "GCP-d.xls";		
		PrintWriter fout = new PrintWriter(new BufferedWriter(new FileWriter(output)));
		fout.println("d\tAT\tHS\tNH");
		for (int d = 2;d < 8;d++) {
			fout.print(d);
			fout.print("\t");
			String tail = "-" + d + "-5";
			for (int i = 0;i < programs.length;i++) {
				fout.print(getGCP(programs[i], tail));
				fout.print("\t");
			}
			fout.println();
		}
		fout.close();
		
		output = "time-d.xls";		
		fout = new PrintWriter(new BufferedWriter(new FileWriter(output)));
		fout.println("d\tAT\tHS\tNH");
		for (int d = 2;d < 8;d++) {
			fout.print(d);
			fout.print("\t");
			String tail = "-" + d + "-5";
			for (int i = 0;i < programs.length;i++) {
				fout.print(getTime(programs[i], tail));
				fout.print("\t");
			}
			fout.println();
		}
		fout.close();
		
		output = "GCP-l.xls";		
		fout = new PrintWriter(new BufferedWriter(new FileWriter(output)));
		fout.println("l\tAT\tHS\tNH");
		for (int l = 2;l < 10;l++) {
			fout.print(l);
			fout.print("\t");
			String tail = "-3-" + l;
			for (int i = 0;i < programs.length;i++) {
				fout.print(getGCP(programs[i], tail));
				fout.print("\t");
			}
			fout.println();
		}
		fout.close();
		
		output = "time-l.xls";		
		fout = new PrintWriter(new BufferedWriter(new FileWriter(output)));
		fout.println("l\tAT\tHS\tNH");
		for (int l = 2;l < 10;l++) {
			fout.print(l);
			fout.print("\t");
			String tail = "-3-" + l;
			for (int i = 0;i < programs.length;i++) {
				fout.print(getTime(programs[i], tail));
				fout.print("\t");
			}
			fout.println();
		}
		fout.close();
				
		System.out.println("done");
	}

	private static String getGCP(String program, String tail) throws Exception {
		// TODO Auto-generated method stub
		String ret = null;
		String path = program + "/log/" + program + tail + ".txt";
		// System.out.println(path);
		BufferedReader fin = new BufferedReader(new FileReader(path));
		String line = fin.readLine();
		boolean done = false; 
		while (line != null && !done) {			
			if (line.length() > 3 && line.substring(0, 3).equals("GCP")) {
				done = true;
				ret = line.substring(5);
			}
			line = fin.readLine();
		}
		fin.close();
		return ret;
	}
	
	private static String getTime(String program, String tail) throws Exception {
		// TODO Auto-generated method stub
		String ret = null;
		String path = program + "/log/" + program + tail + ".txt";
		// System.out.println(path);
		BufferedReader fin = new BufferedReader(new FileReader(path));
		String line = fin.readLine();
		boolean done = false; 
		while (line != null && !done) {			
			if (line.length() > 10 && line.substring(0, 10).equals("Total time")) {
				done = true;
				ret = line.substring(12);
				ret = ret.substring(0, ret.length() - 7);
			}
			line = fin.readLine();
		}
		fin.close();
		return ret;
	}
}
