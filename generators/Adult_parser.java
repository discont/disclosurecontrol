package generators;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Adult_parser {

	private static HashMap<String, Integer> A;
	private static HashMap<String, Integer> B;
	private static HashMap<String, Integer> C;
	private static HashMap<String, Integer> D;
	private static HashMap<String, Integer> E;
	private static HashMap<String, Integer> F;
	private static HashMap<String, Integer> G;
	private static HashMap<String, Integer> H;


	public static void main(String[] args) {
		A = new HashMap();
		A.put("Private", 1);
		A.put("Self-emp-not-inc",2);
		A.put("Self-emp-inc",3);
		A.put("Federal-gov",4);
		A.put("Local-gov",5);
		A.put("State-gov",6);
		A.put("Without-pay",7);
		A.put("Never-worked",8);

		B = new HashMap();
		B.put("Bachelors",1);
		B.put("Some-college",2);
		B.put("11th",3);
		B.put("HS-grad",4);
		B.put("Prof-school",5);
		B.put("Assoc-acdm",6);
		B.put("Assoc-voc",7);
		B.put("9th",8);
		B.put("7th-8th",9);
		B.put("12th",10);
		B.put("Masters",11);
		B.put("1st-4th",12);
		B.put("10th",13);
		B.put("Doctorate",14);
		B.put("5th-6th",15);
		B.put("Preschool",16);

		C = new HashMap();
		C.put("Married-civ-spouse",1);
		C.put("Divorced",2);
		C.put("Never-married",3);
		C.put("Separated",4);
		C.put("Widowed",5);
		C.put("Married-spouse-absent",6);
		C.put("Married-AF-spouse",7);

		D = new HashMap();
		D.put("Tech-support",1);
		D.put("Craft-repair",2);
		D.put("Other-service",3);
		D.put("Sales",4);
		D.put("Exec-managerial",5);
		D.put("Prof-specialty",6);
		D.put("Handlers-cleaners",7);
		D.put("Machine-op-inspct",8);
		D.put("Adm-clerical",9);
		D.put("Farming-fishing",10);
		D.put("Priv-house-servTransport-moving",11);
		D.put("Protective-serv",12);
		D.put("Armed-Forces",13);

		E = new HashMap();
		E.put("Wife",1);
		E.put("Own-child",2);
		E.put("Husband",3);
		E.put("Not-in-family",4);
		E.put("Other-relative",5);
		E.put("Unmarried",6);

		F = new HashMap();
		F.put("White",1);
		F.put("Asian-Pac-Islander",2);
		F.put("Amer-Indian-Eskimo",3);
		F.put("Amer-Indian-Eskimo",4);
		F.put("Black",5);

		H = new HashMap();
		H.put("Female", 1);
		H.put("Male", 2);

		G = new HashMap();
		G.put("United-States",1);
		G.put("Cambodia",2);
		G.put("England",3);
		G.put("Puerto-Rico",4);
		G.put("Canada",5);
		G.put("Germany",6);
		G.put("Outlying-US(Guam-USVI-etc)",7);
		G.put("India",8);
		G.put("Japan",9);
		G.put("Greece",10);
		G.put("South",11);
		G.put("China",12);
		G.put("Cuba",13);
		G.put("Iran",14);
		G.put("Honduras",15);
		G.put("Philippines",16);
		G.put("Italy",17);
		G.put("Poland",18);
		G.put("Jamaica",19);
		G.put("Vietnam",20);
		G.put("Mexico",21);
		G.put("Portugal",22);
		G.put("Ireland",23);
		G.put("France",24);
		G.put("Dominican-Republic",25);
		G.put("Laos",26);
		G.put("Ecuador",27);
		G.put("Taiwan",28);
		G.put("Haiti",29);
		G.put("Columbia",30);
		G.put("Hungary",31);
		G.put("Guatemala",32);
		G.put("Nicaragua",33);
		G.put("Scotland",34);
		G.put("Thailand",35);
		G.put("Yugoslavia",36);
		G.put("El-Salvador",37);
		G.put("Trinadad&Tobago",38);
		G.put("Peru",39);
		G.put("Hong",40);
		G.put("Holand-Netherlands",41);


		try {
			DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream("adult.txt")));
			FileWriter qw = new FileWriter("./adult_int",true);
			
			int min=100;
			int max=0;
			while (dis.available()>1) {
				String str = "";
				String line = dis.readLine();
				String[] splitLine = line.split(", ");
					if (max<Integer.parseInt(splitLine[0]))
						max=Integer.parseInt(splitLine[0]);
					if (min>Integer.parseInt(splitLine[0]))
						min=Integer.parseInt(splitLine[0]);
					str+= Integer.parseInt(splitLine[0])+", ";
					str+= A.get(splitLine[1])+", ";
					//str+= Integer.parseInt(splitLine[2])+", ";
					str+= B.get(splitLine[3])+", ";
					//str+= Integer.parseInt(splitLine[4])+", ";
					str+= C.get(splitLine[5])+", ";
					
					str+= E.get(splitLine[7])+", ";//RELATIONSHIP
					str+= F.get(splitLine[8])+", ";//RACE
					str+= H.get(splitLine[9])+", ";//SEX
					//str+= Integer.parseInt(splitLine[10])+", ";
					//str+= Integer.parseInt(splitLine[11])+", ";
					//str+= Integer.parseInt(splitLine[12])+", ";
					str+= G.get(splitLine[13])+", ";
					str+= D.get(splitLine[6]);//occupation
					if (str.contains("null"))
						continue;
					System.out.println(str);
					qw.write(str+" \n");
				
			}
			System.out.println(min);
			System.out.println(max);
			dis.close();
			if(qw != null) qw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
