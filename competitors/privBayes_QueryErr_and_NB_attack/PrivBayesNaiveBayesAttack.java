import java.io.IOException;
import java.util.*;
import java.io.FileWriter;


/*includes the reading of tuples and their rage queries*/
public class PrivBayesNaiveBayesAttack {

	private static final double BIG = Double.MAX_VALUE;
	private static final boolean RANGE = false;
	private static final boolean MIXED = true;
	//private static final boolean OPTIMIZATION = true;
	private static double maxCost = BIG;//0.5436267458828434;
	static byte[] cardinalities = {79, 2, 17, 6, 9, 10, 83, 51};
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
	
	/******************************************/
	static double naiveBayes(double[] Laplace_rate, double delta){
		double genVal, origVal;
		double sele, range, min, max, random, randomPos, r, overlap;
		int[] distVals = new int[buckNum];
		
		double accuracy_rate = 0.0;
		double accuracy_rate_Laplace = 0.0;
		
		/** We need a map from all distinct values to their counts in the
		 dataset, and within each class. **/
		Map<Double, Double> SA_map = new HashMap<Double, Double>(); // COUNT(SA)
		//Map<String, Double> QI_map = new HashMap<String, Double>(); // COUNT(QI|SA)
		Map<String, Map<Double, Double> > QI_map = new HashMap<String, Map<Double, Double> >(); // COUNT(QI|SA)
		//Map<String, Double> QI_Laplace_map = new HashMap<String, Double>(); // counts the FREQ(QI|SA), using: [count(QI and SA)+1] / [count(SA) + cardinalities[QI]].
		
		double origLabel = 0.0; //Init that will be overwritten.
		double realLabel = 0.0;
		
		// COUNT OCCURENCES ON THE ANONYMISED DATA:
		double prev_cnt = 0; double SA_anonCnt = 0;
		for (int j=0; j<origTuples; j++){ //in our algo: final_assignment.length not origTuples!
			
			realLabel = mapANON[j][SA]; //Sensitive attribute label.
			
			// Count Occurences of SAs:
			if (!SA_map.containsKey(realLabel)){
				SA_map.put(realLabel, 1.0);
			}else{
				prev_cnt = (double)SA_map.get(realLabel);
				SA_map.put(realLabel, prev_cnt+1.0);
			}
			
			// Count Occurences of QIs|SAs:
			// (numerator of each of the Prob_anonQI_SA)
			for (int i=0; i<dims-1; i++){ //For all QIs:
				//if (!QI_map.containsKey(realLabel+"_"+i+"_"+mapANON[j][i])){
				//	QI_map.put(realLabel+"_"+i+"_"+mapANON[j][i], 1.0);
				if (!QI_map.containsKey(realLabel+"_"+i)){
					QI_map.put(realLabel+"_"+i, new HashMap<Double, Double>());
					QI_map.get(realLabel+"_"+i).put(mapANON[j][i], 1.0);
				}else{
					//System.out.println(realLabel+"_"+i+"_"+mapANON[j][i]+".");
					//System.out.flush();
					//prev_cnt = (double)QI_map.get(realLabel+"_"+i+"_"+mapANON[j][i]);
					//QI_map.put(realLabel+"_"+i+"_"+mapANON[j][i], prev_cnt+1.0);
					if (!QI_map.get(realLabel+"_"+i).containsKey(mapANON[j][i])){
						QI_map.get(realLabel+"_"+i).put(mapANON[j][i], 1.0);
					}else{
						prev_cnt = (double)QI_map.get(realLabel+"_"+i).get(mapANON[j][i]);
						QI_map.get(realLabel+"_"+i).put(mapANON[j][i], prev_cnt+1.0);
					}
				}
			}
		}
		
		/* DIVIDE EACH FREQ(QI|SA) BY FREQ(SA) TO GET PROB(QI|SA):
		Set<Map.Entry<String, Double> > set=QI_map.entrySet();
		Iterator<Map.Entry<String, Double> > itr=set.iterator();
		while(itr.hasNext()){
			Map.Entry<String, Double> entry = itr.next();
			double numerator = (double)entry.getValue();
			String key_str = entry.getKey().toString();
			double denominator = SA_map.get(Double.parseDouble(
					key_str.substring(0, key_str.indexOf('_')) ) );
			int iindex = Integer.parseInt(key_str.substring(key_str.indexOf('_')+1, key_str.lastIndexOf('_') ) );
			QI_map.put(entry.getKey().toString(), numerator/denominator);
			// LAPLACIAN CORRECTION: numerator+1 / denominator+QI_dimensionality
			QI_Laplace_map.put(entry.getKey().toString(),
							   (numerator+1)/(denominator+cardinalities[iindex]));
		}
		// Nope. I do it below!!! I need the count[QI|SA].
		
		// DIVIDE EACH FREQ(SA) BY #TUPLES TO GET PROB(SA):
		Set<Map.Entry<Double, Double> > set2=SA_map.entrySet();
		Iterator<Map.Entry<Double, Double> > itr2=set2.iterator();
		while(itr2.hasNext()){
			Map.Entry<Double, Double> entry = itr2.next();
			double numerator = (double)entry.getValue();
			double denominator = origTuples; //in our algo: final_assignment.length not origTuples!
			SA_map.put(entry.getKey(), numerator/denominator);
		}
		 // Nope. I do it below!!! I need the count[SA] for the laplacian.
		*/
		
		// NOW TEST NAIVE BAYES USING ORIGINAL TUPLES:
		for (int j=0; j<origTuples; j++){ //in our algo: final_assignment.length not origTuples!
			
			origLabel = map[j][SA]; //Sensitive attribute label.
			realLabel = mapANON[j][SA]; //This is irrelevant!
			//Original NB:
			double SA_QI_prob = 1.0; // initially 1 -- we multiply it.
			double SA_predicted_prob = 0.0; // initially 0 -- we replace by greatest.
			double SA_predicted = 1234;
			//NB with Laplacian correction:
			double SA_QI_prob_Laplace = 1.0; // initially 1 -- we multiply it.
			double SA_predicted_prob_Laplace = 0.0; // initially 0 -- we replace by greatest.
			double SA_predicted_Laplace = 1234;
			
			Set<Map.Entry<Double, Double> > set3=SA_map.entrySet();
			Iterator<Map.Entry<Double,Double> > itr3=set3.iterator();
			while(itr3.hasNext()){ // All SAs:
				Map.Entry<Double, Double> SA_entry = itr3.next();
				double SA_key = (double)SA_entry.getKey();
				double SA_count = (double)SA_entry.getValue();
				SA_QI_prob = SA_count / origTuples; //SA_prob INIT
				SA_QI_prob_Laplace = SA_count / origTuples; //SA_prob INIT

				double rel_diff = 1000; //just init as a large value > delta.
				double abs_diff = 1000; //just init as a large value > delta.
				
				for (int i=0; i<dims-1; i++){ //For all QIs:
					//String searchKey = (SA_key.toString())+"_"+i+"_"+map[j][i];
					String searchKey = (SA_key+"_"+i);
					double countHits = 0.0; // INIT
					Set<Map.Entry<Double, Double> > set4=QI_map.get(searchKey).entrySet();
					Iterator<Map.Entry<Double, Double> > itr4=set4.iterator();
					while(itr4.hasNext()){ // All QI values with this SA:
						Map.Entry<Double, Double> qiEntry = itr4.next();
						double qi_val = qiEntry.getKey();//getKey() is a QI value
						//getValue() is the count of qi_val
						
						abs_diff = Math.abs(qi_val - map[j][i]);
						rel_diff = Math.abs(qi_val - map[j][i]) / Math.abs(map[j][i]);
						
						if (rel_diff <= delta){ //use either rel_ or abs_
							//HIT!!!
							countHits += qiEntry.getValue();
						} //else MISS!!! Add nothing.
					}
					if (countHits == 0.0){
						//Prob[QI_i|SA] = 0
						SA_QI_prob = SA_QI_prob * 0.0;
						//Laplace Prob[QI_i|SA] = 1 / cardinalities[i]
						SA_QI_prob_Laplace = SA_QI_prob_Laplace *
									(1.0/(SA_count+cardinalities[i]));
						//System.out.print("-- Not exists: ");
						//System.out.println(searchKey);
						//System.out.flush();
					}else{ // countHits > 0
						SA_QI_prob = SA_QI_prob * (countHits/SA_count);
						SA_QI_prob_Laplace = SA_QI_prob_Laplace * ((countHits+1.0) / (SA_count+cardinalities[i]));
						//System.out.print("EXISTS: ");
						//System.out.println(searchKey);
						//System.out.flush();
					}
				}
				
				if (SA_predicted_prob < SA_QI_prob){
					SA_predicted_prob = SA_QI_prob;
					SA_predicted = SA_key;
				}
				
				//Do the Laplacian alternative independently:
				if (SA_predicted_prob_Laplace < SA_QI_prob_Laplace){
					SA_predicted_prob_Laplace = SA_QI_prob_Laplace;
					SA_predicted_Laplace = SA_key;
				}
			}
			if(origLabel == SA_predicted){
				accuracy_rate+=1.0;
			}
			
			if(origLabel == SA_predicted_Laplace){
				accuracy_rate_Laplace+=1.0;
			}
		}

		System.out.println("NB:"+accuracy_rate+" out of "+origTuples+" tuples.");
		System.out.println("NB_Laplace:"+accuracy_rate_Laplace+" out of "+origTuples+" tuples.");

		accuracy_rate = accuracy_rate/origTuples; //in our algo: final_assignment.length not origTuples!

		accuracy_rate_Laplace = accuracy_rate_Laplace/origTuples; //in our algo: final_assignment.length not origTuples!

		System.out.println("accuracy rate NB_Laplace:"+accuracy_rate_Laplace);
		
		Laplace_rate[0] = accuracy_rate_Laplace;
		
		return accuracy_rate;
	}
	/******************************************/

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
				error += ((Math.abs(anonCnt - cnt)) / (cnt));
				absError += ((Math.abs(anonCnt - cnt)));
				errArray[tm] = ((Math.abs(anonCnt - cnt)) / (cnt));
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

	//***********//
	//MAIN METHOD//
	//***********//

	public static void main(String[] args) 	{

		if (args.length!=5){
			System.out.println("\nUsage:   java PrivBayesNaiveBayesAttack origFile n d SA anonFile");
			System.out.println("\t origFile: input file name (path included) of original data.");
			System.out.println("\t n: number of tuples in inFile.");
			System.out.println("\t d: dimensionality of the dataset.");
			System.out.println("\t SA: index of sensitive attribute [0 -- d-1].");
			System.out.println("\t anonFile: input file name (path included) of anonymised data by PrivBayes.\n");
			System.exit(1);
		}

		String inputFile = args[0];
		tuples = Integer.parseInt(args[1]);  // n
		dims = Integer.parseInt(args[2]); //d
		SA = Integer.parseInt(args[3]); //Sensitive Attribute (0 - 7).
		String anonFile = args[4];
		origTuples = tuples;
		
		//double[] deltas = {0, 0.0001, 0.001, 0.01, 0.1, 1.0, 0.03, 0.09, 0.27, 0.81, 2.43};
		//double[] deltas = {0.0495}; //NB_LC
		double[] deltas = {0.1}; //NB


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
			while (tp.hasNext()){
				map[i++]=tp.nextTuple2();
				for (int j=0; j<dims; j++){
					if (map[i-1][j] < MinMaxPerAttribute[j][0]){ //min
						MinMaxPerAttribute[j][0] = map[i-1][j];
					}
					if (map[i-1][j] > MinMaxPerAttribute[j][1]){ //max
						MinMaxPerAttribute[j][1] = map[i-1][j];
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


		System.out.println("Naive Bayes Attack.");
		FileWriter nbw = null;
		try{
			nbw = new FileWriter("./PrivB_NaiveBayesAttack.txt",true);
			for(double delta : deltas) {
				System.out.println("delta = "+delta);
				nbw.write(origTuples+" "+anonFile+" ");
				double[] Laplace_rate = new double[1];
				Laplace_rate[0] = 0.0;
				double accuracy_rate = naiveBayes(Laplace_rate, delta);
				nbw.write(accuracy_rate+" "+Laplace_rate[0]+" "+delta+"\n");
				System.out.println("accuracy_rate = "+accuracy_rate+"\n");
			}
		}catch(IOException ioe){
			System.err.println("IOException: " + ioe.getMessage());
		}finally{
			try{
				if(nbw != null) nbw.close();
			}catch(Exception e){
				System.err.println(e.getMessage());
				System.exit(-1);
			}
		}
		//////////////////////////////////////////////////////////
		/*
		System.out.println("Range Queries.");
		double[] selectivities = {0.05, 0.1, 0.15, 0.2, 0.25};
		double qErr = 0;
		FileWriter qw = null;
		try{
			qResult  qres = new qResult();
			int qtimes = 1000; //numer of random queries.
			double[] errArray = new double[qtimes];
			qw = new FileWriter("./PrivBayes_QueryError.txt",true); //true == append
			//qw.write("#tuples beta size lambda sel error\n");
			/////
			//for (int i=0; i<selectivities.length; i++){
			//	for (int l=1; l<dims; l++){
			//		qw.write(origTuples+" "+anonFile+" "+
							 l+" "+selectivities[i]+" ");
			//		double[][] tmpres = new double[l+1][2];
			//		qErr = rangeQueries(selectivities[i], l, tmpres, qtimes, qres);
			//		qw.write(qErr+" \n");
			//	}
			//}
			/////
			//sel=0.1
			double[][] tmpres = new double[dims][2];
			
			System.out.println("Vary lambda (sel=0.1): ");
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
		*/
		//////////////////////////////////////////////////////////
		System.exit(0);
	} //end of main
} // end of class
