import java.io.IOException;
import java.util.*;
import java.io.FileWriter;


/*includes the reading of tuples and their rage queries*/
public class PrivBayesQueries {

	private static final double BIG = Double.MAX_VALUE;
	private static final boolean RANGE = false;
	private static final boolean MIXED = true;
	//private static final boolean OPTIMIZATION = true;
	private static double maxCost = BIG;//0.5436267458828434;
	static byte[] cardinalities = {79, 2, 17, 6, 9, 10, 83, 51}; //age, gender, edu_level, marital, race, work_class, country, occupation
	static int age = 0;
	static int gender = 1;
	static int edu_level = 2;
	static int marital = 3;
	static int race = 4;
	static int work_class = 5;
	static int country = 6;
	static int occupation = 7;
	//static byte[] cardinalities ={15,2};
	//***b*like***//
	//static int k;// = 10;
	static double b_param;// = 10;
	static int SA;// 0 - 7.
	//***b*like***//
	static int dims = 8; //3
	static int tuples;// = 10000;
	static int origTuples;// = tuples;
	static int bucket_size;//c;
	static int buckNum;//tuples/c
	static int partition_size;//size of bucket partitions.
	static int partition_function;//type of bucket partitioning.
	static int parts;//number of partitions per bucket.
	//static int offset=0;
	static byte[] dimension = new byte[dims];
	static short[][] map;// = new short[tuples][dims];
	static double[][] mapANON;// = new short[tuples][dims];
	static short[][][] buckets; // = new short[tuples/c][c][dims];
	static LinkedList<Integer>[][] distinctValuesPerAssign;
	static LinkedList<Integer>[] distinctValues1;
	// = (LinkedList<Short>[][]) new LinkedList[chunk_size][dims];
	static int[][][] MinMaxPerAssign;
	static int[][] MinMaxPerAttribute; //needed for queries.
	static double[][] MinMaxPerAttributeANON; //needed for queries.
	static int[][] final_assignment;// = new int[tuples][k];
	static LinkedList<Integer> chunk_sizes = new LinkedList<Integer>();
	//static HeapNode[] edges;
	static Map<Integer, Set<Short>> uniqueValsPerAttr;


	public static class qResult{
		double medianE=0.0;
		double meanE = 0.0;
		double minE = 0.0;
		double maxE = 0.0;
		public void qResult(){
		};
	};

	public static void quickSortArray(int left, int right, double[] a) {
		int i = left, j = right;
		int ref = left + (right-left)/2; //i.e., (right+left)/2
		double pivot = a[ref];
		double temp2;
		while (i <= j) {
			while (pivot > a[i])
				i++;
			while (a[j] > pivot)
				j--;
			if (i <= j) {
				temp2=a[i];
				a[i]=a[j];
				a[j]=temp2;
				
				i++;
				j--;
			}
		};
		// recursion
		if (left < j)
			quickSortArray(left, j, a);
		if (i < right) {
			quickSortArray(i, right, a);
		}
	}
	
	public static void quickSortArray(int left, int right, int[] a) {
		int i = left, j = right;
		int ref = left + (right-left)/2; //i.e., (right+left)/2
		int pivot = a[ref];
		int temp2;
		while (i <= j) {
			while (pivot > a[i])
				i++;
			while (a[j] > pivot)
				j--;
			if (i <= j) {
				temp2=a[i];
				a[i]=a[j];
				a[j]=temp2;
				
				i++;
				j--;
			}
		};
		// recursion
		if (left < j)
			quickSortArray(left, j, a);
		if (i < right) {
			quickSortArray(i, right, a);
		}
	}
	
	static double rangeQueries(double s, int lambda, double[][] rs, int times, double[] errArray, qResult qr) {
		double error = 0.0;
		double absError=0.0;
		double genVal, origVal;
		double sele, range, min, max, random, randomPos, r, overlap;
		int[] distVals = new int[buckNum];
		ArrayList<Integer> attrOptions = new ArrayList<Integer>();
		ArrayList<Integer> choices = new ArrayList<Integer>();
		//static final int[] attrNumber = new int[]{0, 1, 2, 3, 4, 5 , 6};
		
		sele = Math.pow(s, 1.0/((double)lambda+1.0));
		
		System.out.println("lambda ="+lambda+": ");
		
		for (int tm=0; tm<times; tm++){
			for (int qi=0; qi<dims-1; qi++){ //INITIALIZATION:
				rs[qi][0] = (double)MinMaxPerAttribute[qi][0]-1;//<min QI value.
				rs[qi][1] = (double)MinMaxPerAttribute[qi][1]+1;//>max QI value
			}
			//SA:
			range = (double)(MinMaxPerAttribute[SA][1] - MinMaxPerAttribute[SA][0]) * sele;
			min = (double)MinMaxPerAttribute[SA][0];//min SA value.
			max = (double)MinMaxPerAttribute[SA][1] - range;//max SA value - range.
			random = new Random().nextDouble();
			rs[dims-1][0] = min + (random * (max-min));
			rs[dims-1][1] = rs[dims-1][0] + range;
			//others:
			attrOptions.clear();
			choices.clear();
			for (int i=0; i<7; i++){ attrOptions.add(i); } //originally
			
			int temporary = 7;
			for (int ch=0; ch<lambda; ch++){
				int next = new Random().nextInt(temporary);
				//System.out.println(next+" : "+(attrOptions.get(next)));
				choices.add(attrOptions.get(next));
				attrOptions.remove(next);
				temporary--;
			}
			/*
			 System.out.print("Keeping:= ");
			 for (int i=0; i<choices.size(); i++){
			 System.out.print(choices.get(i)+" ");
			 }
			 System.out.print("-- Discarding: ");
			 for (int i=0; i<attrOptions.size(); i++){
			 System.out.print(attrOptions.get(i)+" ");
			 }
			 if (choices.size() != lambda){
			 System.out.println("WTF: "+choices.size()+" "+lambda);
			 System.exit(0);
			 }
			 System.out.println();
			 */
			for (int ii=0; ii<lambda; ii++){
				int i = (int)choices.get(ii);
				if (i==0 || i==2){ //Continuous:
					range = (double)(MinMaxPerAttribute[i][1] - MinMaxPerAttribute[i][0]) * sele;
					min = (double)MinMaxPerAttribute[i][0];//min attribute value.
					max = (double)MinMaxPerAttribute[i][1] - range;//max attribute value - range.
					random = new Random().nextDouble();
					rs[i][0] = min + (random * (max-min));
					rs[i][1] = rs[i][0] + range;
				}else{ //Categorical:
					range = (double)Math.round((double)(cardinalities[i]) * sele);
					if (range < 1.0) range = 1; //select at least 1 value.
					min = (double)MinMaxPerAttribute[i][0];//min attribute value.
					//max = (double)MinMaxPerAttribute[i][1] - range;//max attribute value - range.
					random = new Random().nextDouble();
					randomPos = (double)Math.round(random * ((double)(cardinalities[i]) * (1.0-sele)));
					rs[i][0] = min + randomPos;
					rs[i][1] = rs[i][0] + range;
				}
			}
			/*for (int i=0; i<lambda; i++)
			 System.out.println(i+"s="+s+"sele="+sele+":["+MinMaxPerAttribute[i][0]+","
			 +MinMaxPerAttribute[i][1]+"]"+rs[i][0]+" "+rs[i][1]);
			 System.out.println(" ");*/
			
			double cnt = 0; double anonCnt = 0;
			for (int j=0; j<origTuples; j++){ //in our algo: final_assignment.length not origTuples!
				//r = 1.0; overlap=1.0;
				boolean inRange = true;
				boolean genRange = true;
				for (int index=0; index<lambda; index++){
					int i = (int)choices.get(index);
					genVal = mapANON[j][i]; //indexToTupleMapping(final_assignment[j][0])[i];
					origVal = map[j][i]; //indexToTupleMapping(final_assignment[j][0])[i];

					if ((genVal <rs[i][0])||(genVal >rs[i][1])){//anon out of query range.
						genRange = false;
						//break;
					}
					
					if ((origVal <rs[i][0])||(origVal>rs[i][1])){//orig out of query range.
						inRange = false;
						//break;
					}
				}
				
				if ((map[j][SA]<rs[SA][0])||(map[j][SA]>rs[SA][1])){//out of range
					inRange = false;
				}

				if ((mapANON[j][SA]<rs[SA][0])||(mapANON[j][SA]>rs[SA][1])){//out of range
					genRange = false;
				}
				
				if (true == inRange){
					//System.out.println("orig +1 ");
					cnt++;
				}
				if (true == genRange){
					//System.out.println("anon +1 ");
					anonCnt++;
				}
			}
			//System.out.println(cnt+" "+anonCnt);
			if (cnt!=0){
				//System.out.print(tm+" ");
				error += (((double)Math.abs(anonCnt - cnt)) / ((double)cnt));
				absError += (((double)Math.abs(anonCnt - cnt)));
				errArray[tm] = (((double)Math.abs(anonCnt - cnt)) / ((double)cnt));
			}else{
				tm--; //repeat.
			}
			
		}
		quickSortArray(0, times-1, errArray);
		double median = (errArray[(times/2)-1]);
		System.out.println("query error (sel="+s+", lambda="+lambda+"): mean rel error="
						   +((error/(double)times))+" abs error="+(absError/times)
						   +" median rel error="+median);
		System.out.println("min="+errArray[0]+" max="+errArray[times-1]);
		qr.medianE = median;
		qr.meanE = ((error/(double)times));
		qr.minE = errArray[0];
		qr.maxE = errArray[times-1];
		//return (error/(double)times);
		return median;
		
		/*
		 double mean = 0.0;
		 for (i=0; i<errArray.size(); i++){
		 mean += errArray[i];
		 }
		 mean = mean / errArray.size();
		 return mean;
		 */
		
	}
	
	/*
	 * PrefixQueries: Prefix census queries of the form
	 * (income ∈(0, ihigh), age = a, marital = m, race = r, gender = g) 
	*/
	static double PrefIncQueries(int prefAttr, double[][] rs, int times, double[] errArray, qResult qr){
		double error = 0.0;
		double absError=0.0;
		double genVal, origVal;
		double min, max, random, r, overlap;
		int randomPos;
		int[] distVals = new int[buckNum];
		int[] equalityAttrs = {age, marital, race, gender};
		int[] allqueryAttrs = {prefAttr, age, marital, race, gender};
		double delta = 0.5;
		double rel_diff = 1.1;//init
		double rel_anon_diff = 1.1;//init
		
		if (prefAttr<0 || prefAttr>=dims){
			System.err.println("Prefix attribute outside of attribute range [0,"+dims+"]");
			return -1;
		}
		
		Short[] ages = uniqueValsPerAttr.get(age).toArray(new Short[uniqueValsPerAttr.get(age).size()]);
		Short[] maritals = uniqueValsPerAttr.get(marital).toArray(new Short[uniqueValsPerAttr.get(marital).size()]);
		Short[] races = uniqueValsPerAttr.get(race).toArray(new Short[uniqueValsPerAttr.get(race).size()]);
		Short[] genders = uniqueValsPerAttr.get(gender).toArray(new Short[uniqueValsPerAttr.get(gender).size()]);
		/*
		System.out.print("ages ");
		for (int i=0; i<uniqueValsPerAttr.get(age).size(); i++)
			System.out.print(ages[i]+" ");
		System.out.print("\nmaritals ");
		for (int i=0; i<uniqueValsPerAttr.get(marital).size(); i++)
			System.out.print(maritals[i]+" ");
		System.out.print("\nraces ");
		for (int i=0; i<uniqueValsPerAttr.get(race).size(); i++)
			System.out.print(races[i]+" ");
		System.out.print("\ngenders ");
		for (int i=0; i<uniqueValsPerAttr.get(gender).size(); i++)
			System.out.print(genders[i]+" ");
		System.out.println(" ");
		//System.exit(0);
		*/
		
		for (int tm=0; tm<times; tm++){
			for (int qi=0; qi<dims-1; qi++){ //INITIALIZATION:
				rs[qi][0] = (double)MinMaxPerAttribute[qi][0]-1;//<min QI value.
				rs[qi][1] = (double)MinMaxPerAttribute[qi][1]+1;//>max QI value
			}
			//PREFIX (0,high):
			min = (double)MinMaxPerAttribute[prefAttr][0];//min attribute value.
			max = (double)MinMaxPerAttribute[prefAttr][1];//max attribute value.
			random = new Random().nextDouble();//random number between 0.0 and 1.0
			rs[prefAttr][0] = min;
			rs[prefAttr][1] = min + (random * (max-min));//number between min and max.
			
			//OTHERS: (age = a, marital = m, race = r, gender = g) 
			/*for (int i: equalityAttrs){
				if (i==age || i==edu_level){ //Continuous:
					min = (double)MinMaxPerAttribute[i][0];//min attribute value.
					max = (double)MinMaxPerAttribute[i][1];//max attribute value.
					random = new Random().nextDouble();//random number from 0.0 to 1.0
					rs[i][0] = min + (double)Math.round(random * (max-min)); //becomes number from min to max
					rs[i][1] = rs[i][0]; //equality age=a
				}else{ //Categorical:
					min = (double)MinMaxPerAttribute[i][0];//min attribute value.
					random = new Random().nextDouble(); //random number from 0.0 to 1.0
					randomPos = (double)Math.round(random * ((double)(cardinalities[i])) );
					rs[i][0] = min + randomPos;
					rs[i][1] = rs[i][0]; //equality attr=value
				}
			}*/
			
			//age
			random = new Random().nextDouble(); //random number from 0.0 to 1.0
			randomPos = (int)(Math.round(random * ((double)(uniqueValsPerAttr.get(age).size()-1))));
			rs[age][0] = (double)(ages[randomPos]); // -0.5; //becomes number from min to max
			rs[age][1] = (double)(ages[randomPos]); // +0.5; //equality age=a+-0.5

			//marital 
			random = new Random().nextDouble(); //random number from 0.0 to 1.0
			randomPos = (int)(Math.round(random * ((double)(uniqueValsPerAttr.get(marital).size()-1))));
			rs[marital][0] = (double)(maritals[randomPos]); // -0.5; //becomes number from min to max
			rs[marital][1] = (double)(maritals[randomPos]); // +0.5; //equality marital=m+-0.5
			
			//race 
			random = new Random().nextDouble(); //random number from 0.0 to 1.0
			randomPos = (int)(Math.round(random * ((double)(uniqueValsPerAttr.get(race).size()-1))));
			rs[race][0] = (double)(races[randomPos]); // -0.5; //becomes number from min to max
			rs[race][1] = (double)(races[randomPos]); // +0.5; //equality race=r+-0.5
			
			//gender 
			random = new Random().nextDouble(); //random number from 0.0 to 1.0
			randomPos = (int)(Math.round(random * ((double)(uniqueValsPerAttr.get(gender).size()-1))));
			rs[gender][0] = (double)(genders[randomPos]); // -0.5; //becomes number from min to max
			rs[gender][1] = (double)(genders[randomPos]); // +0.5; //equality gender=g+-0.5

			
			/*
			System.out.println("pref"+prefAttr+" : "+rs[prefAttr][0]+", "+rs[prefAttr][1] 
				+"age"+age+" : "+rs[age][0]+", "+rs[age][1] 
				+"marital"+marital+" : "+rs[marital][0]+", "+rs[marital][1] 
				+"race"+race+" : "+rs[race][0]+", "+rs[race][1] 
				+"gender"+gender+" : "+rs[gender][0]+", "+rs[gender][1] );
			*/
			
			//evaluate query error
			double cnt = 0; double anonCnt = 0;
			delta = 0.001;
			for (int j=0; j<origTuples; j++){ //in our algo: final_assignment.length not origTuples!
				//r = 1.0; overlap=1.0;
				boolean inRange = true;
				boolean genRange = true;
				for (int i : allqueryAttrs){
					genVal = mapANON[j][i]; //indexToTupleMapping(final_assignment[j][0])[i];
					origVal = map[j][i]; //indexToTupleMapping(final_assignment[j][0])[i];
					if (i == prefAttr){ //in range (0,ihigh)
						if ((genVal <rs[i][0])||(genVal>rs[i][1])){//anon out of range.
							genRange = false;
						}
						
						if ((origVal <rs[i][0])||(origVal>rs[i][1])){//orig out of query range.
							inRange = false;
						}
					}else{ //exact match attr=v
						rel_diff = Math.abs(origVal - rs[i][0]) / Math.abs(origVal+0.001);//to avoid 0/0
						rel_anon_diff = Math.abs(genVal - rs[i][0]) / Math.abs(genVal+0.001);//to avoid 0/0
						
						if(rel_diff > delta){
							inRange = false;
						}
						
						if(rel_anon_diff > delta){
							genRange = false;
						}
					}
				}
								
				if (true == inRange){
					//System.out.println("orig +1 ");
					cnt++;
				}
				if (true == genRange){
					//System.out.println("anon +1 ");
					anonCnt++;
				}
			}
			//System.out.println(cnt+" "+anonCnt);
			if (cnt!=0){
				error += (((double)Math.abs(anonCnt - cnt)) / ((double)cnt));
				absError += (((double)Math.abs(anonCnt - cnt)));
				errArray[tm] = (((double)Math.abs(anonCnt - cnt)) / ((double)cnt));
			}else{
				tm--; //repeat.
			}
			
		}
		quickSortArray(0, times-1, errArray);
		double median = (errArray[(times/2)-1]);
		System.out.println("query error (beta="+b_param+"): mean rel error="
						   +((error/(double)times))+" abs error="+(absError/times)
						   +" median rel error="+median);
		System.out.println("min="+errArray[0]+" max="+errArray[times-1]);
		qr.medianE = median;
		qr.meanE = ((error/(double)times));
		qr.minE = errArray[0];
		qr.maxE = errArray[times-1];
		//return (error/(double)times);
		return median;
		
		/*
		 double mean = 0.0;
		 for (i=0; i<errArray.size(); i++){
		 mean += errArray[i];
		 }
		 mean = mean / errArray.size();
		 return mean;
		 */

	}

	//***********//
	//MAIN METHOD//
	//***********//

	public static void main(String[] args) 	{

		if (args.length!=5){
			System.out.println("\nUsage:   java PrivBayesQueries origFile n d SA anonFile");
			System.out.println("\t origFile: input file name (path included) of original data.");
			System.out.println("\t n: number of tuples in inFile.");
			System.out.println("\t d: dimensionality of the dataset.");
			System.out.println("\t SA: index of sensitive attribute [0 -- d-1].");
			System.out.println("\t anonFile: input file name (path included) of anonymised data by PrivBayes.\n");
		}

		String inputFile = args[0];
		tuples = Integer.parseInt(args[1]);  // n
		dims = Integer.parseInt(args[2]); //d
		SA = Integer.parseInt(args[3]); //Sensitive Attribute (0 - 7).
		String anonFile = args[4];
		origTuples = tuples;

		uniqueValsPerAttr = new HashMap<Integer, Set<Short> >();

/*
		int modl = (tuples % l_param);
		if (modl > 0){
			//change n (#tuples), so that it is divided by l:
			tuples = tuples + (l_param - modl);
			for (int i=0; i<dims-1; i++)
				cardinalities[1]++;
		}
		map = new short[tuples][dims];
		buckets = new short[l_param][tuples/l_param][dims];
*/

		map = new short[tuples][dims];
		MinMaxPerAttribute = new int[dims][2];
		
		long startTime = System.currentTimeMillis();
		try {
			CensusParser tp = new CensusParser(inputFile, dims);
			int i=0;
			if (tp.hasNext()){
				map[i++]=tp.nextTuple2();
				for (int j=0; j<dims; j++){//init using 1st tuple
					MinMaxPerAttribute[j][0] = map[i-1][j];
					MinMaxPerAttribute[j][1] = map[i-1][j];
					if (j==age||j==gender||j==marital||j==race){
						Set<Short> uniqueVals = new HashSet<Short>();
						uniqueVals.add(map[i-1][j]);
						uniqueValsPerAttr.put(j, uniqueVals);
					}
				}
			}
			while (tp.hasNext()){
				map[i++]=tp.nextTuple2();
				for (int j=0; j<dims; j++){
					if (map[i-1][j] < MinMaxPerAttribute[j][0]){ //min
						MinMaxPerAttribute[j][0] = map[i-1][j];
					}
					if (map[i-1][j] > MinMaxPerAttribute[j][1]){ //max
						MinMaxPerAttribute[j][1] = map[i-1][j];
					}
					if (j==age||j==gender||j==marital||j==race){
						(uniqueValsPerAttr.get(j)).add(map[i-1][j]);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		////////////////////////////////////////////
		mapANON = new double[tuples][dims];
		MinMaxPerAttributeANON = new double[dims][2];
		
		try {
			CensusParser tp = new CensusParser(anonFile, dims);
			int i=0;
			if (tp.hasNext()){
				mapANON[i++]=tp.nextTupleDBL();
				for (int j=0; j<dims; j++){
					MinMaxPerAttributeANON[j][0] = mapANON[i-1][j];
					MinMaxPerAttributeANON[j][1] = mapANON[i-1][j];
				}
			}
			while (tp.hasNext()){
				mapANON[i++]=tp.nextTupleDBL();
				for (int j=0; j<dims; j++){
					if (mapANON[i-1][j] < MinMaxPerAttributeANON[j][0]){ //min
						MinMaxPerAttributeANON[j][0] = mapANON[i-1][j];
					}
					if (mapANON[i-1][j] > MinMaxPerAttributeANON[j][1]){ //max
						MinMaxPerAttributeANON[j][1] = mapANON[i-1][j];
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		////////////////////////////////////////////
/*
		if (modl > 0){
			//add dummy tuples:
			for(int i=(tuples-(l_param-modl)); i<tuples; i++){
				for(int j=0; j<dims; j++){
					if (j == SA){
						map[i][j] = -1; //unique SA
					}else{
						map[i][j] = 0;
					}
				}
			}
		}
*/

		long midTime = System.currentTimeMillis();


		System.out.println("Queries.");
		double[] selectivities = {0.05, 0.1, 0.15, 0.2, 0.25};
		double qErr = 0;
		FileWriter qw = null;
		try{
			qResult  qres = new qResult();
			int qtimes = 1000; //numer of random queries.
			double[] errArray = new double[qtimes];
			double[] errArrayPref = new double[qtimes];
			qw = new FileWriter("./PrivBayes_QueryError.txt",true); //true == append
			double[][] tmpres = new double[dims][2];
			for (int qi=0; qi<dims; qi++){ //INITIALIZATION:
				tmpres[qi][0] = (double)MinMaxPerAttribute[qi][0]-1;//<min QI value.
				tmpres[qi][1] = (double)MinMaxPerAttribute[qi][1]+1;//>max QI value
			}

			System.out.println("Prefix Queries.");
			wq.write("Prefix Queries.\n");
			int prefAttr = edu_level; //2
			qErr = PrefIncQueries(prefAttr, tmpres, qtimes, errArrayPref, qres);
			qw.write(origTuples+" "+anonFile+" "+bucket_size+" ");
			qw.write(qErr+" "+ qres.meanE+" "+ qres.minE+" "+ qres.maxE+" \n");
			System.out.println("Pref Queries: "+origTuples+" "+b_param+" "+bucket_size+" "+qErr+" \n\n");
			
			System.out.println("Range Queries.");			
			//sel=0.1
			for (int qi=0; qi<dims; qi++){ //INITIALIZATION:
				tmpres[qi][0] = (double)MinMaxPerAttribute[qi][0]-1;//<min QI value.
				tmpres[qi][1] = (double)MinMaxPerAttribute[qi][1]+1;//>max QI value
			}
			
			System.out.println("Range Queries.");
			System.out.println("Vary lambda (sel=0.1): ");
			wq.write("Range Queries.\n");
			for (int l=1; l<dims; l++){
				qw.write(origTuples+" "+ anonFile+" "+
						 l+" "+selectivities[1]+" ");
				for (int qi=0; qi<dims; qi++){ //INITIALIZATION:
					tmpres[qi][0] = (double)MinMaxPerAttribute[qi][0]-1;//<min QI value.
					tmpres[qi][1] = (double)MinMaxPerAttribute[qi][1]+1;//>max QI value
				}
				qErr = rangeQueries(selectivities[1], l, tmpres, qtimes, errArray, qres);
				qw.write(qErr+" "+ qres.meanE+" "+ qres.minE+" "+ qres.maxE+" \n");
			}
			qw.write("\n");
			System.out.println("Vary selectivity (lambda=3): ");
			int l=3; //lambda = 3 first QIs.
			for (int i=0; i<selectivities.length; i++){
				qw.write(origTuples+" "+ anonFile+" "+
						 l+" "+selectivities[i]+" ");
				for (int qi=0; qi<dims; qi++){ //INITIALIZATION:
					tmpres[qi][0] = (double)MinMaxPerAttribute[qi][0]-1;//<min QI value.
					tmpres[qi][1] = (double)MinMaxPerAttribute[qi][1]+1;//>max QI value
				}
				qErr = rangeQueries(selectivities[i], l, tmpres, qtimes, errArray, qres);
				qw.write(qErr+" "+ qres.meanE+" "+ qres.minE+" "+ qres.maxE+" \n");
			}
			qw.write("\n");

		}catch(IOException ioe){
			System.err.println("IOException: " + ioe.getMessage());
		}finally{
			try{
				if(qw != null) qw.close();
			}catch(Exception e){
				System.err.println(e.getMessage());
			}
		}

	}
}
