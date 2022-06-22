import java.io.IOException;
import java.util.*;
import java.io.FileWriter;
import java.util.Random;

/*includes the reading of tuples and their assignment into buckets*/
public class Greedy_BLikeness {

	private static final double BIG = Double.MAX_VALUE;
	private static final boolean RANGE = false;
	private static final boolean MIXED = true;
	//private static final boolean OPTIMIZATION = true;
	private static double maxCost = BIG;//0.5436267458828434;
	static byte[] cardinalities = {79, 2, 17, 6, 9, 10, 83, 51}; //age, gender, edu_level, marital, race, work_class, country, occupation
	//static byte[] cardinalities ={10,6,6,10,41};//Coil2000
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
	static boolean rq;
	static boolean pq;
	static boolean nb;
	static int parts;//number of partitions per bucket.
	//static int offset=0;
	static byte[] dimension = new byte[dims];
	static short[][] map;// = new short[tuples][dims];
	static short[][][] buckets; // = new short[tuples/c][c][dims];
	static LinkedList<Integer>[][] distinctValuesPerAssign;
	static LinkedList<Integer>[] distinctValues1;
	// = (LinkedList<Short>[][]) new LinkedList[chunk_size][dims];
	static int[][][] MinMaxPerAssign;
	static int[][] MinMaxPerAttribute; //needed for queries.
	static int[][] final_assignment;// = new int[tuples][k];
	static LinkedList<Integer> chunk_sizes = new LinkedList<Integer>();
	static HeapNode[] edges;
	static int edge_size;
	static double threshold;
	static Map<Integer, Set<Short>> uniqueValsPerAttr;

	//*******************************************//
	//METHODS THAT PERFORM ARRAY-PROCESSING TASKS//
	//*******************************************//

	private static int[] greedyAssign(double[][] array, int[] assignment, int assign_size) {
		int[] matchOrder = new int[assign_size];
		//int[] iToj = new int[assign_size];
		int[] jToi = new int[assign_size];
		int index = 0;
		Arrays.fill(assignment, -1);
		Arrays.fill(jToi, -1);
		for (int i=0; i<edges.length; i++){
			HeapNode node = (HeapNode) edges[i];
			/*if (node.getI() == 906)
			 System.out.println(node.getJ());*/
			if (assignment[node.getI()]==-1 && jToi[node.getJ()]==-1){
				if (node.getCost()!= BIG){
					assignment[node.getI()]=node.getJ();
					jToi[node.getJ()]=node.getI();
					matchOrder[index++]=node.getI();
				}else{
					for (int l=index-1; l>=0; l--){
						if (array[matchOrder[l]][node.getJ()]!=BIG && array[node.getI()][assignment[matchOrder[l]]]!=BIG){
							assignment[node.getI()]=assignment[matchOrder[l]];
							assignment[matchOrder[l]]=node.getJ();
							jToi[node.getJ()]=matchOrder[l];
							jToi[assignment[node.getI()]]=node.getI();
							matchOrder[index++]=node.getI();
							break;
						}

					}

				}
			}

		}
		return assignment;
	}

	//*******************************************//
	//	METHODS to pre-process the data			//
	//*******************************************//

	public static void dimension_sort() { // sort the dimensions according to their effect to GCP
		for (int i = 0;i < dims;i++) {
			dimension[i] = (byte) i;
		}
		for (int i = 0; i < dims; i++) { // small domain, use bubble sort
			if (i != SA){ //sort all except SA!!!
				for (int j = 0; j < dims-i-1;j++) { // smaller range, put it the front
					if (j != SA){ //sort all except SA!!!
						if (cardinalities[dimension[j]] > cardinalities[dimension[j+1]]) {
							byte temp = dimension[j];
							dimension[j] = dimension[j+1];
							dimension[j+1] = temp;
						}
					}
				}
			}
		}
	}

	// return true if data x > data y
	public static boolean compare_data(short[] x, short[] y) {
		for (int i = 0;i < dims;i++) {
			if (i != SA){ //compare w.r.t. all except SA!!!
				if (x[dimension[i]] > y[dimension[i]]) {
					/*if (map[x][dimension[0]]<map[y][dimension[0]])
					 System.out.println("oops");*/
					return true;
				} else if (x[dimension[i]] < y[dimension[i]]) {
					/*if (map[x][dimension[0]]>map[y][dimension[0]])
					 System.out.println("oops");*/
					return false;
				}
			}
		}
		/*if (map[x][dimension[0]]>map[y][dimension[0]])
			System.out.println("oops");*/
		return false;
	}

	// sort edges[] by lexicographical order
	public static void qSort(int left, int right){
		int i = left, j = right;
		int ref = left + (right-left)/2;
		HeapNode pivot = edges[ref];
		HeapNode temp2 ;
		while (i <= j) {
			while (compare(pivot, edges[i]))
				i++;
			while (compare(edges[j], pivot))
				j--;
			if (i <= j) {
				temp2=edges[i];
				edges[i]=edges[j];
				edges[j]=temp2;

				i++;
				j--;
			}
		};
		// recursion
		if (left < j)
			qSort(left, j);
		if (i < right) {
			qSort(i, right);
		}

	}

	public static boolean compare(Object o1, Object o2) {
		if (((HeapNode) o1).getCost()>((HeapNode)o2).getCost())
			return true;
		else
			return false;
	}

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
	// sort data by lexicographical order
	public static void quickSortBucket(int left, int right, int b) {
		int i = left, j = right;
		int ref = left + (right-left)/2; //i.e., (right+left)/2
		short[] pivot = buckets[b][ref];
		short[] temp2 = new short[dims];
		while (i <= j) {        
			while (compare_data(pivot, buckets[b][i]))
				i++;
			while (compare_data(buckets[b][j], pivot))
				j--;
			if (i <= j) {
				temp2=buckets[b][i];
				buckets[b][i]=buckets[b][j];
				buckets[b][j]=temp2;

				i++;
				j--;
			}
		};    
		// recursion
		if (left < j)
			quickSortBucket(left, j, b);
		if (i < right) {
			quickSortBucket(i, right, b);
		}
	}

	/*
	 * Option 0: bucket_partition.
	 * Partitions each bucket, while maintaining SA distributions
	 * Note: This is the correct one!
	 */
	static void bucket_partition(int b_size, LikenessBuckets bk){
		int parts = b_size/partition_size; //#partitions per bucket
		double ratio; int offsetB; int offsetP;
		int chunk_size; int partSA;
		short[][] tmpBucket;
		int[] loaded;

		if(parts == 0){
			parts = 1;
			chunk_sizes.add(b_size);
		}else{
			for(int i=0; i<parts-1; i++){
				chunk_sizes.add(partition_size);
			}
			if ((b_size % partition_size) <= (partition_size/2)){
				chunk_sizes.addFirst(partition_size + (b_size % partition_size));
			}else{
				parts++;
				chunk_sizes.add(partition_size);
				chunk_sizes.addLast(b_size % partition_size);
			}
		}
		loaded = new int[parts];

		for(int b=0; b<buckNum; b++){//for every bucket
			tmpBucket = new short[bucket_size][dims];
			ArrayList<Integer> chg = bk.changeSA.get(b);
			int first = 0; int last;
			for(int i=0; i<parts; i++){
				loaded[i] = 0;
			}
			for(int i=0; i<chg.size(); i++){//for every SA
				last = chg.get(i);
				if((last-first) > 1){
					//System.out.println("sorting bucket["+b+"]["
					//				   +first+"--"+last+"]");
					quickSortBucket(first, last, b);
				}
				offsetB = first;
				offsetP = 0;
				for(int jj=0; jj<parts; jj++){//for every partition
					chunk_size = chunk_sizes.get(jj);
					//ratio = ((double)chunk_size) / ((double)bucket_size);
					//partSA = (int)(Math.ceil(ratio * ((float)(last-offsetB+1))));
					partSA = (int)Math.ceil(((double)(last-offsetB+1)) / ((double)(parts-jj)));
					if (partSA > (chunk_size - loaded[jj]))
						partSA = chunk_size - loaded[jj];
					for(int j=0; j<partSA; j++){
						tmpBucket[offsetP + loaded[jj] + j] = buckets[b][offsetB + j];
					}
					loaded[jj] += partSA;
					offsetB += partSA;
					offsetP += chunk_size;
				}
				first = last+1;
			}
			buckets[b] = tmpBucket;
		}
	}

	/*
	 * Option 1: bucket_partition2.
	 * Partitions each bucket, maintaining the distribution of the most freq SA
	 * while partitioning the rest  based only on their QIDs, ignoring SAs.
	 * Note: It preserves better data utility, but may lead to a deadlock
	 * (chunk assignments that have 50% the same SAs.)
	 */
	static void bucket_partition2(int b_size, LikenessBuckets bk){
		int parts = b_size/partition_size; //#partitions per bucket
		float ratio; int offsetB; int offsetB2; int offsetP;
		int chunk_size; int partSA; int partSA2;
		short[][] tmpBucket;

		if(parts == 0){
			parts = 1;
			chunk_sizes.add(b_size);
		}else{
			for(int i=0; i<parts-1; i++){
				chunk_sizes.add(partition_size);
			}
			if ((b_size % partition_size) <= (partition_size/2)){
				chunk_sizes.addFirst(partition_size + (b_size % partition_size));
			}else{
				parts++;
				chunk_sizes.add(partition_size);
				chunk_sizes.addLast(b_size % partition_size);
			}
		}

		for(int b=0; b<buckNum; b++){//for every bucket
			tmpBucket = new short[bucket_size][dims];
			//tuples of most freq SA:
			ArrayList<Integer> chg = bk.changeSA.get(b);
			int first = 0; int last = chg.get(0);
			if((last-first) > 0){ // (last-first+1) > 1.
				//System.out.println("sorting bucket["+b+"]["
				//				   +first+"--"+last+"]");
				quickSortBucket(first, last, b);
			}
			//remaining tuples:
			if((bucket_size - last) > 3){ // (bucket_size-1 -last-1) > 1.
				//System.out.println("sorting bucket["+b+"]["
				//				   +(last+1)+"--"+(bucket_size-1)+"]");
				quickSortBucket(last+1, bucket_size-1, b);
			}
			offsetB = 0;
			offsetP = 0;
			offsetB2 = last+1;
			for(int jj=0; jj<parts; jj++){//for every partition
				chunk_size = chunk_sizes.get(jj);
				//ratio = ((float)chunk_size) / ((float)bucket_size);
				//partSA = Math.round(ratio * ((float)(last-first+1))); //freq SA
				partSA = (int)Math.ceil(((double)(last-offsetB+1)) / ((double)(parts-jj)));
				partSA2 = chunk_size - partSA; //remaining SAs
				for(int j=0; j<partSA; j++){
					tmpBucket[offsetP + j] = buckets[b][offsetB + j];
				}
				offsetB += partSA;
				for(int j=0; j<partSA2; j++){
					tmpBucket[offsetP + partSA + j] = buckets[b][offsetB2 + j];
				}
				offsetB2 += partSA2;
				offsetP += chunk_size;
			}
			buckets[b] = tmpBucket;
		}
	}

	/*
	 * rangeQueries: Range queries of the form
	 * (QI_1 ∈(min_1, max_1), ..., QI_N ∈(min_N, max_N), SA ∈(min_S, max_S)) 
	*/
	static double rangeQueries(double s, int lambda, double[][] rs, int times, double[] errArray){
		double error = 0.0;
		double absError=0.0;
		double genMx, genMn;
		double sele, range, min, max, random, randomPos, r, overlap;
		int[] distVals = new int[buckNum];
		ArrayList<Integer> attrOptions = new ArrayList<Integer>();
		ArrayList<Integer> choices = new ArrayList<Integer>();
		//static final int[] attrNumber = new int[]{0, 1, 2, 3, 4, 5 , 6};
		
		sele = Math.pow(s, 1.0/((double)lambda+1.0));

		System.out.println("lambda ="+lambda+": ");

		for (int tm=0; tm<times; tm++){
			for (int qi=0; qi<dims-1; qi++){ //INITIALIZATION:
				rs[qi][0] = (double)MinMaxPerAttribute[qi][0]-1;//<min SA value.
				rs[qi][1] = (double)MinMaxPerAttribute[qi][1]+1;//>max SA value
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
			for (int j=0; j<final_assignment.length; j++){ //not origTuples!
				r = 1.0; overlap=1.0;
				boolean inRange = true;
				boolean genRange = true;
				for (int index=0; index<lambda; index++){
					int i = (int)choices.get(index);
					genMx = indexToTupleMapping(final_assignment[j][0])[i];
					genMn = indexToTupleMapping(final_assignment[j][0])[i];
					
					for (int bi=0; bi<buckNum; bi++){
						//Generalization Range:
						if (final_assignment[j][bi] < origTuples){ //not a dummy:
							distVals[bi] = indexToTupleMapping(final_assignment[j][bi])[i];
							if (genMx < distVals[bi])
								genMx = distVals[bi];
							if ((genMn > distVals[bi]) || (genMn==-1))
								if (-1 != distVals[bi])
									genMn = distVals[bi];
						}
					}
					if ((genMx<rs[i][0])||(genMn>rs[i][1])){//gen out of query range.
						//if (!((genMx<rs[i][1])&&(genMn>rs[i][0]))){//gen not within range.
						genRange = false;
						//break;
					}else{ //there is some overlap:
						if (i==0 || i==2){ //Continuous:
							if ((genMn<=rs[i][0])&&(rs[i][1]<=genMx)){
								overlap = (rs[i][1] - rs[i][0])/(genMx - genMn);
							}else if ((genMn<=rs[i][0])&&(genMx<=rs[i][1])){
								overlap = (genMx - rs[i][0])/(genMx - genMn);
							}else if ((rs[i][0]<=genMn)&&(rs[i][1]<=genMx)){
								overlap = (rs[i][1] - genMn)/(genMx - genMn);
							}else if ((rs[i][0]<=genMn)&&(genMx<=rs[i][1])){
								overlap = 1.0;
							}
						}else{ //Categorical:
							if ((rs[i][0]<=genMn)&&(genMx<=rs[i][1])){
								overlap = 1.0;
							}else{
								int tempCnt = 0;
								for (int it=0; it<distVals.length; it++){
									if((distVals[it]>=rs[i][0])&&(distVals[it]<=rs[i][1]))
										tempCnt++;
								}
								overlap = ((double)tempCnt) / ((double)distVals.length);
							}
						}
						r = r * overlap;
						//System.out.println("r="+r);
					}
					
					if ((indexToTupleMapping(final_assignment[j][0])[i]<rs[i][0])||
						(indexToTupleMapping(final_assignment[j][0])[i]>rs[i][1])){
						//orig out of range:
						inRange = false;
						break;
					}
				}
				
				if ((indexToTupleMapping(final_assignment[j][0])[SA]<rs[SA][0])||
					(indexToTupleMapping(final_assignment[j][0])[SA]>rs[SA][1])){//out of range
					inRange = false;
					genRange = false;
				}
				
				if (true == inRange){
					//System.out.println("orig +1 ");
					cnt++;
				}
				if (true == genRange){
					//System.out.println("anon +r "+r);
					anonCnt += r;
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
	static double PrefIncQueries(int prefAttr, double[][] rs, int times, double[] errArray){
		double error = 0.0;
		double absError=0.0;
		double[] genMx, genMn;
		double range, min, max, random, r, overlap;
		int randomPos;
		int[] distVals = new int[buckNum];
		int[] equalityAttrs = {age, marital, race, gender};
		int[] allqueryAttrs = {prefAttr, age, marital, race, gender};
		genMx = new double[final_assignment.length];
		genMn = new double[final_assignment.length];
		
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
		
		TreeSet<Integer> uniquesetvals = new TreeSet<Integer>();;
		Map<Integer, TreeSet<Integer>> UniqueValsPerAssignMap = new HashMap<Integer, TreeSet<Integer>>();
		for (int j=0; j<final_assignment.length; j++){ //not origTuples!
			for (int i : allqueryAttrs){
				if ( i == prefAttr){
					genMx[j] = indexToTupleMapping(final_assignment[j][0])[i];//init
					genMn[j] = indexToTupleMapping(final_assignment[j][0])[i];//init
				}else{
					uniquesetvals = new TreeSet<Integer>();
				}
				for (int bi=0; bi<buckNum; bi++){
					//Generalization Range:
					if (final_assignment[j][bi] < origTuples){ //not a dummy:
						distVals[bi] = indexToTupleMapping(final_assignment[j][bi])[i];
						if ( i == prefAttr){
							if (genMx[j] < distVals[bi])
								genMx[j] = distVals[bi];
							if ((genMn[j] > distVals[bi]) || (genMn[i]==-1))
								if (-1 != distVals[bi])
									genMn[j] = distVals[bi];
						}else{
							//get set of uniquesetvals[j][i]
							uniquesetvals.add(distVals[bi]);
						}
					}
				}
				if ( i != prefAttr){
					UniqueValsPerAssignMap.put(((10*j)+i), uniquesetvals);
				}
			}
		}
		
		for (int tm=0; tm<times; tm++){
			//for (int qi=0; qi<dims-1; qi++){ //INITIALIZATION:
			//	rs[qi][0] = (double)MinMaxPerAttribute[qi][0]-1;//<min QI value.
			//	rs[qi][1] = (double)MinMaxPerAttribute[qi][1]+1;//>max QI value
			//}
			//PREFIX (0,high):
			min = (double)MinMaxPerAttribute[prefAttr][0];//min attribute value.
			max = (double)MinMaxPerAttribute[prefAttr][1];//max attribute value.
			random = new Random().nextDouble();//random number between 0.0 and 1.0
			rs[prefAttr][0] = min;
			rs[prefAttr][1] = min + (random * (max-min));//number between min and max.
			
			//OTHERS: (age = a, marital = m, race = r, gender = g) 
			/*for (int i: equalityAttrs){
				min = (double)MinMaxPerAttribute[i][0];//min attribute value.
				max = (double)MinMaxPerAttribute[i][1];//max attribute value.
				random = new Random().nextDouble(); //random number from 0.0 to 1.0
				rs[i][0] = min + (double)Math.round(random * (max-min)); //becomes number from min to max
				rs[i][1] = rs[i][0]; //equality age=a
			}*/
			
			
			//age
			random = new Random().nextDouble(); //random number from 0.0 to 1.0
			randomPos = (int)(Math.round(random * ((double)(uniqueValsPerAttr.get(age).size()-1))));
			rs[age][0] = ages[randomPos]; // -0.5; //becomes number from min to max
			rs[age][1] = ages[randomPos]; // +0.5; //equality age=a

			//marital 
			random = new Random().nextDouble(); //random number from 0.0 to 1.0
			randomPos = (int)(Math.round(random * ((double)(uniqueValsPerAttr.get(marital).size()-1))));
			rs[marital][0] = maritals[randomPos]; // -0.5; //becomes number from min to max
			rs[marital][1] = maritals[randomPos]; // +0.5; //equality marital=m+-0.5
			
			//race 
			random = new Random().nextDouble(); //random number from 0.0 to 1.0
			randomPos = (int)(Math.round(random * ((double)(uniqueValsPerAttr.get(race).size()-1))));
			rs[race][0] = races[randomPos]; // -0.5; //becomes number from min to max
			rs[race][1] = races[randomPos]; // +0.5; //equality race=r
			
			//gender 
			random = new Random().nextDouble(); //random number from 0.0 to 1.0
			randomPos = (int)(Math.round(random * ((double)(uniqueValsPerAttr.get(gender).size()-1))));
			rs[gender][0] = genders[randomPos]; // -0.5; //becomes number from min to max
			rs[gender][1] = genders[randomPos]; // +0.5; //equality gender=g
			
			/*System.out.println("pref"+prefAttr+" : "+rs[prefAttr][0]+", "+rs[prefAttr][1] 
				+"age"+age+" : "+rs[age][0]+", "+rs[age][1] 
				+"marital"+marital+" : "+rs[marital][0]+", "+rs[marital][1] 
				+"race"+race+" : "+rs[race][0]+", "+rs[race][1] 
				+"gender"+gender+" : "+rs[gender][0]+", "+rs[gender][1] );
			*/

			//evaluate query error
			double cnt = 0; double anonCnt = 0;
			for (int j=0; j<final_assignment.length; j++){ //not origTuples!
				r = 1.0; overlap=1.0;
				boolean inRange = true;
				boolean genRange = true;
				for (int i : allqueryAttrs){
					//prefix range(0,ihigh)
					
					if (i == prefAttr){
					
						//check gen
						if ((genMx[j]<rs[i][0])||(genMn[j]>rs[i][1])){//gen out of query range.
							//if (!((genMx[j]<rs[i][1])&&(genMn[j]>rs[i][0]))){//gen not within range.
							genRange = false;
							overlap = 0.0;
						}else{ //there is some overlap:
							if ((genMn[j]<=rs[i][0])&&(rs[i][1]<=genMx[j])){
								overlap = (rs[i][1]-rs[i][0]+1.0)/(genMx[j]-genMn[j]+1.0);
							}else if ((genMn[j]<=rs[i][0])&&(genMx[j]<=rs[i][1])){
								overlap = (genMx[j]-rs[i][0]+1.0)/(genMx[j]-genMn[j]+1.0);
							}else if ((rs[i][0]<=genMn[j])&&(rs[i][1]<=genMx[j])){
								overlap = (rs[i][1]-genMn[j]+1.0)/(genMx[j]-genMn[j]+1.0);
							}else if ((rs[i][0]<=genMn[j])&&(genMx[j]<=rs[i][1])){
								overlap = 1.0;
							}
						}
						
					}else{ //attr==v
						uniquesetvals = UniqueValsPerAssignMap.get(((10*j)+i));
						if ( i == age){
							//continuous with gen range [ageMn, ageMx]
							int ageMn = uniquesetvals.first();
							int ageMx = uniquesetvals.last();
							if ((ageMn <=rs[i][0])&&(rs[i][1]<= ageMx)){
								//value is within the age gen range
								overlap = (rs[i][1]-rs[i][0]+1.0)/(ageMx-ageMn +1.0);
							/*}else if ((ageMn <=rs[i][0])&&(genMx[j]<=rs[i][1])){
								overlap = (ageMx-rs[i][0]+1.0)/(ageMx-ageMn +1.0);
							}else if ((rs[i][0]<= ageMn)&&(rs[i][1]<= ageMx)){
								overlap = (rs[i][1]-ageMn +1.0)/(ageMx-ageMn +1.0);
							}else if ((rs[i][0]<= ageMn)&&(ageMx <=rs[i][1])){
								overlap = 1.0;*/
							}else{
								//value is outside the age gen range
								genRange = false;
								overlap = 0.0;
							}
						}else{
							//categorical with gen set of vals
							if (uniquesetvals.contains((int)(rs[i][0]))){
								//overlap = 1.0;
								overlap = 1.0/(uniquesetvals.size());
							}else{
								genRange = false;
								overlap = 0.0;
							}
						}
					}
					
					r = r * overlap;
					//System.out.println("r="+r);
					
					//check original
					if ((indexToTupleMapping(final_assignment[j][0])[i]<rs[i][0])||
					    (indexToTupleMapping(final_assignment[j][0])[i]>rs[i][1])){
						//orig either out of range (0,ihigh), or !=v:
						inRange = false;
					}
				}
				
				if (true == inRange){
					//System.out.println("orig +1 ");
					cnt++;
				}
				if (true == genRange){
					//System.out.println("anon +r "+r);
					anonCnt += r;
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
		System.out.println("query error (beta="+b_param+"): mean rel error="
						   +((error/(double)times))+" abs error="+(absError/times)
						   +" median rel error="+median);
		System.out.println("min="+errArray[0]+" max="+errArray[times-1]);
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
	* naiveBayes: Naive Bayes attack on the original data
	* given knowlegde of (training on) the anonymized data.
	*/
	static double naiveBayes(double[] Laplace_rate){
		
		short[] tuple;
		double[] countQIs = new double[dims];
		double countSA = 0.0;

		double accuracy_rate = 0.0;
		double accuracy_rate_Laplace = 0.0;
		
		double origLabel = 0.0; //just INIT
		
		//COUNT(SA) OCCURENCES ON THE ANONYMISED DATA:
		//First count all SA frequencies (SAfreq):
		Map<Short, Double> SAfreq = new HashMap<Short, Double>();
		Map<String, Map<Short, Double> > QI_map = new HashMap<String, Map<Short, Double> >();
		short qi_value = 0;
		short sa_value = 0;
		double prev_cnt = 0;
		for (int j = 0; j <final_assignment.length; j++) { //includes dummies.
			sa_value = indexToTupleMapping(final_assignment[j][0])[SA];
			if (SAfreq.containsKey(sa_value)) {
				double tempFreq = SAfreq.get(sa_value) + 1.0;
				SAfreq.put(sa_value, tempFreq);
			}else{
				SAfreq.put(sa_value, 1.0);
			}
			/////////////
			//COUNT(QI|SA) OCCURENCES ON THE ANONYMISED DATA:
			short genMx=0; //INIT
			short genMn=0; //INIT
			for (int i=0; i<dims-1; i++){
				genMx = indexToTupleMapping(final_assignment[j][0])[i]; //INIT
				genMn = indexToTupleMapping(final_assignment[j][0])[i]; //INIT
				for (int bi=0; bi<buckNum; bi++){
					//Generalization Range:
					////if (final_assignment[j][bi] < origTuples) //not a dummy:
					short temp = indexToTupleMapping(final_assignment[j][bi])[i];
					/*if (i==0 || i==2){ //continuous ranges
					 //NEEDED!!!
						if (genMx < temp)
							genMx = temp;
						if ((genMn > temp) || (genMn==-1))
							if (-1 != temp)
								genMn = temp;
					}else{ //discrete sets of values */
						qi_value = temp;
						if (!QI_map.containsKey(sa_value+"_"+i)){
							QI_map.put(sa_value+"_"+i, new HashMap<Short, Double>());
							QI_map.get(sa_value+"_"+i).put(qi_value, 1.0);
						}else{
							if (!QI_map.get(sa_value+"_"+i).containsKey(qi_value)){
								QI_map.get(sa_value+"_"+i).put(qi_value, 1.0);
							}else{
								prev_cnt = (double)QI_map.get(sa_value+"_"+i).get(qi_value);
								QI_map.get(sa_value+"_"+i).put(qi_value, prev_cnt+1.0);
							}
						}
					//}*/
				}
				/*/if (i==0 || i==2){ //continuous ranges
				 //NEEDED!!!
					for(qi_value=genMn; qi_value<genMx; qi_value++){ //==
						if (!QI_map.containsKey(sa_value+"_"+i)){
							QI_map.put(sa_value+"_"+i, new HashMap<Short, Double>());
							QI_map.get(sa_value+"_"+i).put(qi_value, 1.0);
						}else{
							if (!QI_map.get(sa_value+"_"+i).containsKey(qi_value)){
								QI_map.get(sa_value+"_"+i).put(qi_value, 1.0);
							}else{
								prev_cnt = (double)QI_map.get(sa_value+"_"+i).get(qi_value);
								QI_map.get(sa_value+"_"+i).put(qi_value, prev_cnt+1.0);
							}
						}
					}
					if (genMx-genMn<2){ //==
						qi_value=genMx;
						if (!QI_map.containsKey(sa_value+"_"+i)){
							QI_map.put(sa_value+"_"+i, new HashMap<Short, Double>());
							QI_map.get(sa_value+"_"+i).put(qi_value, 1.0);
						}else{
							if (!QI_map.get(sa_value+"_"+i).containsKey(qi_value)){
								QI_map.get(sa_value+"_"+i).put(qi_value, 1.0);
							}else{
								prev_cnt = (double)QI_map.get(sa_value+"_"+i).get(qi_value);
								QI_map.get(sa_value+"_"+i).put(qi_value, prev_cnt+1.0);
							}
						}
					}
				*/
				//}
			}
		}
		/////////////
		
		////////////////////////////////
		//count SAs per Assignment, for all different SA values:
		/*From main: "countSAs = new double[final_assignment.length][cardinalities[SA]];"
		for (int j=0; j<final_assignment.length; j++){
			for (short tested_SA : SAfreq.keySet() ){
				countSAs[j][tested_SA] = 0.0;
				for (int bi=0; bi<buckNum; bi++){
					//if (final_assignment[j][bi] < origTuples){ //not a dummy:
					if (tested_SA == indexToTupleMapping(final_assignment[j][bi])[SA]) {
						countSAs[j][tested_SA] += 1.0;
					}
					//}
				}
			}
		}
		*/
		////////////////////////////////
		/* Count(QI|SA) in the anonymised data.
		//this should happen _before_ the tuples loop:
		Map<String, Map<Short, Double> > QI_map = new HashMap<String, Map<Short, Double> >();
		for (int j=0; j<final_assignment.length; j++){ //for each tuple in the assignemnt
		//not origTuples!
			short qi_value = 0;
			short sa_value = 0;
			double prev_cnt = 0;
			for (int bi=0; bi<buckNum; bi++){ // for each bucket
				sa_value=indexToTupleMapping(final_assignment[j][bi])[SA];
				for (int i=0; i<dims-1; i++){ // for each QI index
					//if (final_assignment[j][bi] < origTuples){ //if not a dummy:
					qi_value=indexToTupleMapping(final_assignment[j][bi])[i];
					if (!QI_map.containsKey(sa_value+"_"+i)){
						QI_map.put(sa_value+"_"+i, new HashMap<Short, Double>());
						QI_map.get(sa_value+"_"+i).put(qi_value, 1.0);
					}else{
						if (!QI_map.get(sa_value+"_"+i).containsKey(qi_value)){
							QI_map.get(sa_value+"_"+i).put(qi_value, 1.0);
						}else{
							prev_cnt = (double)QI_map.get(sa_value+"_"+i).get(qi_value);
							QI_map.get(sa_value+"_"+i).put(qi_value, prev_cnt+1.0);
						}
					}
					//}
				}
			}
		}
		*/
		////////////////////////////////
		
		// NOW TEST NAIVE BAYES USING ORIGINAL TUPLES:
		//Attack each original tuple using Naive Bayes on the anonymous data:
		for (int row=0; row<origTuples; row++){
			tuple = map[row];
			origLabel = tuple[SA]; //true label.
			
			//Original NB:
			double SA_QI_prob = 1.0; // initially 1 -- we multiply it.
			double SA_predicted_prob = 0.0; // initially 0 -- we replace by greatest.
			short SA_predicted = 1234;
			//NB with Laplacian correction:
			double SA_QI_prob_Laplace = 1.0; // initially 1 -- we multiply it.
			double SA_predicted_prob_Laplace = 0.0; // initially 0 -- we replace by greatest.
			short SA_predicted_Laplace = 1234;
			
			for (short tested_SA : SAfreq.keySet() ){
				countSA = SAfreq.get(tested_SA);
				SA_QI_prob = countSA/tuples; // Prob(SA)
				SA_QI_prob_Laplace = countSA/tuples; // Prob(SA)
				//Count(QI|SA), given QI values from tuple:
				for (int i=0; i<dims-1; i++){ //EACH QI
					if (!QI_map.get(tested_SA+"_"+i).containsKey(tuple[i])){
						//Prob[QI_i|SA] = 0
						SA_QI_prob = SA_QI_prob * 0.0;
						//Laplace Prob[QI_i|SA] = 1 / cardinalities[i]
						SA_QI_prob_Laplace = SA_QI_prob_Laplace *
							(1.0/(countSA+cardinalities[i]));
					}else{
						double countQISA = (double)QI_map.get(tested_SA+"_"+i).get(tuple[i]);
						SA_QI_prob = SA_QI_prob * (countQISA / countSA);
						SA_QI_prob_Laplace = SA_QI_prob_Laplace *
							((countQISA+1.0) / (countSA+cardinalities[i]));
					}
				}
				
				if (SA_predicted_prob < SA_QI_prob){
					SA_predicted_prob = SA_QI_prob;
					SA_predicted = tested_SA;
				}
				
				//Do the Laplacian alternative independently:
				if (SA_predicted_prob_Laplace < SA_QI_prob_Laplace){
					SA_predicted_prob_Laplace = SA_QI_prob_Laplace;
					SA_predicted_Laplace = tested_SA;
				}
			} //end of P[SA|QI] estimation
			
			if(origLabel == SA_predicted){
				accuracy_rate+=1.0;
			}
			
			if(origLabel == SA_predicted_Laplace){
				accuracy_rate_Laplace+=1.0;
			}
		} //end of tuple attack
		
		System.out.println("NB:"+accuracy_rate+" out of "+origTuples+" tuples.");
		System.out.println("NB_Laplace:"+accuracy_rate_Laplace+" out of "+origTuples+" tuples.");
		
		accuracy_rate = accuracy_rate/origTuples; //in our algo: final_assignment.length not origTuples!
		
		accuracy_rate_Laplace = accuracy_rate_Laplace/origTuples; //in our algo: final_assignment.length not origTuples!
		
		System.out.println("accuracy rate NB_Laplace:"+accuracy_rate_Laplace);
		
		Laplace_rate[0] = accuracy_rate_Laplace;
		
		return accuracy_rate;
	}
	/******************************************/
	
	/*
	* Saves the anonynized data as a csv file 
	* with the name OUTFILE_Greedy.txt 
	* Please rename it before overiding it. 
	*/
	static void saveOutFile(){
		double genMx, genMn;
		int[] distVals = new int[buckNum];
		Set<Integer> setvals;
		FileWriter fw = null;
		try{
			fw = new FileWriter("./OUTFILE_Greedy.txt",false); //true == append, false=replace

			//Option 1: This saves cont attributes as 1 comumn. The value is a range: [Min:Max]
			fw.write("A1,A2,A3,A4,A5,A6,A7,SA\n"); 
			//Option 2: This saves them as two different attributes (columns): MIN,MAX
			//fw.write("A1min,A1max,A2,A3min,A3max,A4,A5,A6,A7,SA\n"); 

			for (int j=0; j<final_assignment.length; j++){ 
				for (int i=0; i<dims; i++){
					if (i==0 || i==2){ //Continuous:
						genMx = indexToTupleMapping(final_assignment[j][0])[i];
						genMn = indexToTupleMapping(final_assignment[j][0])[i];				
						for (int bi=0; bi<buckNum; bi++){
							//Generalization Range:
							if (final_assignment[j][bi] < origTuples){ //not a dummy:
								distVals[bi] = indexToTupleMapping(final_assignment[j][bi])[i];
								if (genMx < distVals[bi])
									genMx = distVals[bi];
								if ((genMn > distVals[bi]) || (genMn==-1))
									if (-1 != distVals[bi])
										genMn = distVals[bi];
							}
						}
						
						//NOTE: Use this to save as a range [MIN:MAX] :
						fw.write("[" + genMn + ":" + genMx + "],"); 
						//
						//Use this to save as a two different attributes (columns): MIN,MAX, :
						//fw.write(genMn + "," + genMx + ","); //Use this to save as a two different attributes (columns): MIN,MAX,
						
					}else{ //Categorical:
						fw.write("{;");
						setvals = new HashSet<Integer>();
						for (int bi=0; bi<buckNum; bi++){
							if (final_assignment[j][bi] < origTuples){ //not a dummy:
								//fw.write(indexToTupleMapping(final_assignment[j][bi])[i] + ";"); //DUPLICATES
								setvals.add((int)indexToTupleMapping(final_assignment[j][bi])[i]); //UNIQUE VALS
							}
						}
						if (! setvals.isEmpty()){
							for (Integer v : setvals) {
								fw.write(v + ";");
							}
							setvals.clear();
						}
						if (i==7){
							fw.write("}\n");
						}else{
							fw.write("},");
						}
					}
				}		
			}
			System.out.println("Saved file: OUTFILE_Greedy.txt\nPlease rename the outfile before overiding it.\n");
			
		}catch(IOException ioe){//599
			System.err.println("IOException: " + ioe.getMessage());
		}finally{
			try{
				if(fw != null) fw.close();
			}catch(Exception e){
				System.err.println(e.getMessage());
			}
		}
	}
	///////////////////////////////////////////////////
	
	//***********//
	//MAIN METHOD//
	//***********//

	public static void main(String[] args) 	{

		if (args.length!=9){
			System.out.println("\nUsage:   java Greedy_BLikeness inFile n SA beta part_size part_option rq pq nb");
			System.out.println("\t inFile: input file name (path included).");
			System.out.println("\t n: number of tuples in inFile.");
			//System.out.println("\t d: dimensionality of the dataset.");
			System.out.println("\t SA: index of sensitive attribute [0 -- d-1].");
			System.out.println("\t l: beta: B-likeness parameter.");
			System.out.println("\t part_size: size of the bucket partitions.");
			System.out.println("\t              (ignored if part_option=2 -- no bucket partitioning).");
			System.out.println("\t part_option: 0 (safer, keeps all SAs distributions), or");
			System.out.println("\t              1 (better utility, but may cause problems), or ");
			System.out.println("\t              2 (no bucket partitioning).");
			System.out.println("\t rq: True with range queries, False without.");
			System.out.println("\t pq: True with prefix queries, False without.");
			System.out.println("\t nb: True with naive Bayes attack, False without.\n");
			//			System.out.println("\t th: distance threshold to place chunk in bucket, in [0, 1].");
			return;
		}

		String inputFile = args[0];
		tuples = Integer.parseInt(args[1]);  // n
		//dims = Integer.parseInt(args[2]); //d
		SA = Integer.parseInt(args[2]); //Sensitive Attribute (0 - 7).
		b_param = Double.parseDouble(args[3]); // beta
		partition_size = Integer.parseInt(args[4]);
		partition_function = Integer.parseInt(args[5]);
		rq = Boolean.parseBoolean(args[6]);
		pq = Boolean.parseBoolean(args[7]);
		nb = Boolean.parseBoolean(args[8]);
		//threshold = Double.parseDouble(args[7]);
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

		LikenessBuckets bk = new LikenessBuckets(b_param, tuples, dims, map, buckets, 0, inputFile);
		buckets = bk.bucketization(SA);
		//bk.printBuckets();

		long bucketEndTime = System.currentTimeMillis();
		System.out.println("Time of reading dataset: "+(midTime - startTime)+" miliseconds.");
		System.out.println("Time of creating buckets: "+(bucketEndTime - midTime)+" miliseconds.");
		//bk.printBuckets();

		//Do not delete map... I need it for the NB attack.
		//map=null; //delete map
		System.gc();
		//-----------------------------------------------------------//		
		//TopCoderAlgo algo = new TopCoderAlgo();

		//Below enter "max" or "min" to find maximum sum or minimum sum assignment.
		String sumType = "min";

		bucket_size = bk.bucketSize;//bucket capacity (c).
		buckNum = bk.buckNum; //number of buckets (|B|).
		System.out.println("Number of buckets:"+buckNum);
		//update "tuples" number, taking into account the dummies:
		tuples = (bucket_size * buckNum);

		final_assignment = new int[tuples][buckNum];

		long matchTime = System.currentTimeMillis();
		long time_of_hungarian = 0;
		long start_of_hungarian = 0;
		double distortion = 0.0;
		int chunk_size;

		/*
		 * Sort groups of same-SA tuples in each bucket, wrt QIDs.
		 * Then form bucket partitions, keeping SA distributions.
		 */
		if (partition_function == 0)
			bucket_partition(bucket_size, bk); //keep all SAs distrubutions.
		else if (partition_function == 1){
			bucket_partition2(bucket_size, bk); //only keep 1st SA distribution.
		} //else NO_PARTITION //default.
		System.gc();
		//bk.printBuckets();

		if((partition_function == 0) || (partition_function == 1)){ //partitioned buckets:

			for (int bucket_index=0; bucket_index<buckNum; bucket_index++){

				int chunk_offset = 0;
				for (int chunk_index=0; chunk_index<chunk_sizes.size(); chunk_index++){
					chunk_size = chunk_sizes.get(chunk_index);

					edges = new HeapNode[chunk_size*chunk_size*2];

					//we need SA, too!
					if (MIXED){
						MinMaxPerAssign = new int[chunk_size][dims-1][2];
						distinctValuesPerAssign = (LinkedList<Integer>[][]) new LinkedList[chunk_size][dims];
					}else{
						if (RANGE){
							MinMaxPerAssign = new int[chunk_size][dims-1][2];
							distinctValues1 = (LinkedList<Integer>[]) new LinkedList[chunk_size];
						}else{
							distinctValuesPerAssign = (LinkedList<Integer>[][]) new LinkedList[chunk_size][dims];
						}
					}
					//HeapComparator hc = new HeapComparator();
					double[][] array = computeCostMatrix(buckets[bucket_index],buckets[(bucket_index+1)%buckNum], bucket_index*bucket_size, chunk_offset, chunk_size);
					int[] assignment = new int[array.length];
					int times = 0;

					while (++times<buckNum){
						//start_of_hungarian = System.currentTimeMillis();
						//algo.hungarian(array, assignment);
						qSort(0, chunk_size*chunk_size-1);
						greedyAssign(array, assignment, chunk_size);//Call Hungarian algorithm.
						//time_of_hungarian+=(System.currentTimeMillis() - start_of_hungarian);

						//System.out.println("time "+times);
						for (int i=0; i<assignment.length; i++){
							final_assignment[i+chunk_offset+bucket_index*bucket_size][times] = bucketToIndexMapping((bucket_index+times)%buckNum,(chunk_offset+assignment[i]));
							if (MIXED){
								findSet_mixed(i, buckets[(bucket_index+times)%buckNum][chunk_offset+assignment[i]] );
							}else{
								if (RANGE)
									findSet_numerical(i, buckets[(bucket_index+times)%buckNum][chunk_offset+assignment[i]]);
								else
									findSet(i,buckets[(bucket_index+times)%buckNum][chunk_offset+assignment[i]]);
							}
						}
						if (times!=buckNum-1)
							recomputeCostMatrix(array, (bucket_index+times+1)%buckNum, chunk_offset, chunk_size);
					}
					for (int i=0; i<chunk_size; i++){
						if (MIXED){
							distortion += NCP_mixed(MinMaxPerAssign[i], distinctValuesPerAssign[i]);
						}else
							if (RANGE)
								distortion += NCP_numerical(MinMaxPerAssign[i]);
							else
								distortion += NCP(distinctValuesPerAssign[i]);
					}
					chunk_offset += chunk_size;
				}
			}
		}else{ //No partitioning:
			for (int bucket_index=0; bucket_index<buckNum; bucket_index++){

				edges = new HeapNode[bucket_size*bucket_size*2];

				//we need SA, too!
				if (MIXED){
					MinMaxPerAssign = new int[bucket_size][dims-1][2];
					distinctValuesPerAssign = (LinkedList<Integer>[][]) new LinkedList[bucket_size][dims];
				}else{
					if (RANGE){
						MinMaxPerAssign = new int[bucket_size][dims-1][2];
						distinctValues1 = (LinkedList<Integer>[]) new LinkedList[bucket_size];
					}else{
						distinctValuesPerAssign = (LinkedList<Integer>[][]) new LinkedList[bucket_size][dims];
					}
				}
				//HeapComparator hc = new HeapComparator();
				double[][] array = computeCostMatrix(buckets[bucket_index],buckets[(bucket_index+1)%buckNum], bucket_index*bucket_size, 0, bucket_size);

				int[] assignment = new int[array.length];
				int times = 0;

				while (++times<buckNum){
					//start_of_hungarian = System.currentTimeMillis();
					//algo.hungarian(array, assignment);
					qSort(0, bucket_size*bucket_size-1);
					greedyAssign(array, assignment, bucket_size);//Call Hungarian algorithm.
					//time_of_hungarian+=(System.currentTimeMillis() - start_of_hungarian);

					//System.out.println("time "+times);
					for (int i=0; i<assignment.length; i++){
						final_assignment[i+bucket_index*bucket_size][times] = bucketToIndexMapping((bucket_index+times)%buckNum, assignment[i]);
						if (MIXED){
							findSet_mixed(i, buckets[(bucket_index+times)%buckNum][assignment[i]] );
						}else{
							if (RANGE)
								findSet_numerical(i, buckets[(bucket_index+times)%buckNum][assignment[i]]);
							else
								findSet(i,buckets[(bucket_index+times)%buckNum][assignment[i]]);
						}
					}
					if (times!=buckNum-1)
						recomputeCostMatrix(array, (bucket_index+times+1)%buckNum, 0, bucket_size);
				}
				for (int i=0; i<bucket_size; i++){
					if (MIXED){
						distortion += NCP_mixed(MinMaxPerAssign[i], distinctValuesPerAssign[i]);
					}else
						if (RANGE)
							distortion += NCP_numerical(MinMaxPerAssign[i]);
						else
							distortion += NCP(distinctValuesPerAssign[i]);
				}
			}
		}//endif (partition or no_partition)

		//**** BEGIN XUE MINGQIANG **** //
		// this call returns a random assignment generated from the k-regular matching graph
		int [] rand_A = Randomization.run(final_assignment, 0, final_assignment.length, buckNum);
		//**** END XUE MINGQIANG **** //
		long endTime = System.currentTimeMillis();

		//System.out.println("The winning assignment after "+index+" runs (" + sumType + " sum) is:\n");	

		/*
		for (int i=0; i<final_assignment.length; i++){
			for (int j=0; j<buckNum; j++){
				System.out.print((final_assignment[i][j] +1)+" ");
			}
			System.out.println();
		}
		 */

		System.out.println("Time: "+(endTime - startTime)+"ms  "+"\n Distortion "+ (double)(distortion/((dims-1)*tuples)));

		System.out.println("Saving results.");
		//Save Results:
		FileWriter fw = null;
		try{
			fw = new FileWriter("./BLik_Greedy.txt",true); //true == append
			fw.write(inputFile+"\t"+origTuples+"\t "+b_param+"\t ");
			if((partition_function == 0) || (partition_function == 1)){
				fw.write(partition_size+"\t ");
			}else{
				fw.write(bucket_size+" ");
			}
			fw.write((endTime - startTime)+"\t "
					+((double)(distortion/((dims-1)*tuples)))+"\n");
		}catch(IOException ioe){
			System.err.println("IOException: " + ioe.getMessage());
		}finally{
			try{
				if(fw != null) fw.close();
			}catch(Exception e){
				System.err.println(e.getMessage());
			}
		}
		
		// Q U E R I E S :
		if (rq==true){
			System.out.println("Range Queries.");
			double[] selectivities = {0.05, 0.1, 0.15, 0.2, 0.25};
			double qErr = 0;
			FileWriter rqw = null;
			int qtimes = 1000; //numer of random queries.
			try{
				double[] errArray = new double[qtimes];
				double[][] tmpres = new double[dims][2];
				rqw = new FileWriter("./BLik_Greedy_RangeQueryError.txt",true); //true == append
				//rqw.write("#tuples beta size lamda sel error\n");
				//sel=0.1
				System.out.println("Vary lambda (sel=0.1): ");
				for (int l=1; l<dims; l++){
					rqw.write(inputFile+"\t"+origTuples+"\t "+b_param+"\t "+bucket_size+"\t "+
							 l+"\t "+selectivities[1]+"\t ");
					for (int qi=0; qi<dims; qi++){ //INITIALIZATION:
						tmpres[qi][0] = (double)MinMaxPerAttribute[qi][0]-1;//<min SA value.
						tmpres[qi][1] = (double)MinMaxPerAttribute[qi][1]+1;//>max SA value
					}

					qErr = rangeQueries(selectivities[1], l, tmpres, qtimes, errArray);
					rqw.write(qErr+" \n");
				}
				rqw.write("\n");
				
				System.out.println("Vary selectivity (lambda=3): ");
				int l=3; //lambda = 3 first QIs.
				for (int i=0; i<selectivities.length; i++){
					rqw.write(origTuples+" "+b_param+" "+bucket_size+" "+
							 l+" "+selectivities[i]+" ");
					for (int qi=0; qi<dims; qi++){ //INITIALIZATION:
						tmpres[qi][0] = (double)MinMaxPerAttribute[qi][0]-1;//<min SA value.
						tmpres[qi][1] = (double)MinMaxPerAttribute[qi][1]+1;//>max SA value
					}

					qErr = rangeQueries(selectivities[i], l, tmpres, qtimes, errArray);
					rqw.write(qErr+" \n");
				}
				rqw.write("\n");
				
			}catch(IOException ioe){
				System.err.println("IOException: " + ioe.getMessage());
			}finally{
				try{
					if(rqw != null) rqw.close();
				}catch(Exception e){
					System.err.println(e.getMessage());
				}
			}
		}
		
		if (pq==true){
			System.out.println("Prefix Queries.");
			double qErr = 0;
			FileWriter qw = null;
			int qtimes = 1000; //numer of random queries.
			try{
				double[] errArrayPref = new double[qtimes];
				double[][] tmpres = new double[dims][2];
				qw = new FileWriter("./BLik_Greedy_PrefixQueryError.txt",true); //true == append
				//qw.write("#tuples beta size error\n");
				for (int qi=0; qi<dims; qi++){ //INITIALIZATION:
					tmpres[qi][0] = (double)MinMaxPerAttribute[qi][0]-1;//<min QI value.
					tmpres[qi][1] = (double)MinMaxPerAttribute[qi][1]+1;//>max QI value
				}
				int prefAttr = edu_level; //2
				qErr = PrefIncQueries(prefAttr, tmpres, qtimes, errArrayPref);
				qw.write("\n");
				qw.write(inputFile+"\t"+origTuples+"\t "+b_param+"\t "+bucket_size+"\t "+qErr+" \n");
				System.out.println("Pref Queries: "+origTuples+" "+b_param+" "+bucket_size+" "+qErr+" \n\n");
				
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

		if (nb==true){
			System.out.println("Naive Bayes Attack.");
			FileWriter nbw = null;
			try{
				nbw = new FileWriter("./BLik_Greedy_NaiveBayesAttack.txt",true); //true == append
				//nbw.write("#tuples beta data accuracy_rate Laplace_rate\n");
				String dtfile = inputFile.substring(inputFile.lastIndexOf('/')+1,
								inputFile.lastIndexOf('.'));
				nbw.write(inputFile+"\t"+origTuples+"\t "+b_param+"\t "+dtfile+"\t ");
				double[] Laplace_rate = new double[1];
				Laplace_rate[0] = 0.0;
				double accuracy_rate = naiveBayes(Laplace_rate);
				nbw.write(accuracy_rate+" "+Laplace_rate[0]+"\n");
				System.out.println("accuracy_rate = "+accuracy_rate+"\n");
								
			}catch(IOException ioe){
				System.err.println("IOException: " + ioe.getMessage());
			}finally{
				try{
					if(nbw != null) nbw.close();
				}catch(Exception e){
					System.err.println(e.getMessage());
				}
			}
		}
		//To save the anonymized data as a csv file, uncomment the saveOutFile() line. 
		//Not recommended for limited disk space, nor needed for evaluation.
		//saveOutFile();
	}


	private static double[][] computeCostMatrix(short[][] in1, short[][] in2, int offset, int first, int size) {
		int index = 0;

		double[][] cost = new double[size][size];
		for (int i=0; i<size; i++){
			for (int j=0; j<size; j++){
				double c;
				if (MIXED){
					c=NCP_mixed(in1[first+i], in2[first+j]);
				}else if (RANGE){
					c=NCP_numerical(in1[first+i], in2[first+j]);
				}else{
					c=NCP(in1[first+i], in2[first+j]);
				}
				if (c<=maxCost){
					cost[i][j]=c;
				}else{
					cost[i][j]=BIG;
				}
				HeapNode hn = new HeapNode(i,j,c);
				HeapNode hn2 = new HeapNode(j,i,c);
				edges[index++]=hn;
				edges[index++]=hn2;
			}
			final_assignment[offset+first+i][0]= offset+first+i;
			if (MIXED){
				for (int l=0; l<dims; l++){
					if (l==0||l==2){
						MinMaxPerAssign[i][l][0]=in1[first+i][l];
						MinMaxPerAssign[i][l][1]=in1[first+i][l];
					}else{
						LinkedList<Integer> list = new LinkedList<Integer>();
						if (in1[first+i][l] != -1){ //dummy
							list.add((int) in1[first+i][l]);
						}
						distinctValuesPerAssign[i][l] = list;
					}
				}
			}else{
				if (RANGE){
					for (int l=0; l<dims-1; l++){
						MinMaxPerAssign[i][l][0]=in1[first+i][l];
						MinMaxPerAssign[i][l][1]=in1[first+i][l];
					}
					LinkedList<Integer> list = new LinkedList<Integer>();
					//if (in1[first+i][SA-1] != -1){ //dummy
					list.add((int) in1[first+i][SA]);
					//}
					distinctValues1[i] = list;
				}else{
					for (int l=0; l<dims; l++){
						LinkedList<Integer> list = new LinkedList<Integer>();
						if (in1[first+i][l] != -1){ //dummy
							list.add((int) in1[first+i][l]);
						}
						distinctValuesPerAssign[i][l] = list;
					}
				}
			}
		}
		return cost;
	}

	private static void recomputeCostMatrix(double[][] array, int bucket_index, int first, int size) {
		for (int i=0; i<size; i++){
			for (int j=0; j<size; j++){
				if (MIXED){
					array[i][j]=NCP_mixed(buckets[bucket_index][first+j], MinMaxPerAssign[i],
							distinctValuesPerAssign[i]);
					HeapNode hn = new HeapNode(i,j,(array[i][j]));
					edges[i*size+j]=hn;
				}else if (RANGE){
					array[i][j]=NCP_numerical(buckets[bucket_index][first+j], MinMaxPerAssign[i],
							distinctValues1[i]);
					HeapNode hn = new HeapNode(i,j,(array[i][j]));
					edges[i*size+j]=hn;
				}else{
					array[i][j]=NCP(buckets[bucket_index][first+j], (distinctValuesPerAssign[i]));
					HeapNode hn = new HeapNode(i,j,(array[i][j]));
					edges[i*size+j]=hn;
				}
			}
		}
	}

	///////////////////Mixed Representation//////////////	
	private static double NCP_mixed(short[] tuple1, short[] tuple2){
		double score=0.0;

		//if (tuple1[SA] == tuple2[SA]) //Beta-likeness does not require this!.
		//	return BIG; //inf

		for (int i=0; i<dims-1; i++){
			if((tuple1[i]==-1)||(tuple2[i]==-1)){ //Beta-likeness dummy tuple.
				return 0;
			}
			if (i==0 || i==2){
				score+=(double)Math.abs(tuple1[i]-tuple2[i])/(double)(cardinalities[i]-1);
			}else{
				if (tuple1[i]==tuple2[i])
					score+=0;
				else 
					score+=(double)(1)/(double)(cardinalities[i]-1);
			}
		}
		return score;
	}

	private static double NCP_mixed(short[] tuple, int[][] MinMaxPerDim, LinkedList<Integer>[] distinctValuesPerDim ){
		double score=0.0;
		int min;
		int max;

		LinkedList<Integer> distinctValues2 = distinctValuesPerDim[SA];
		//if (distinctValues2.contains((int)tuple[SA])) //Beta-likeness does not require this!.
		//	return BIG; //inf

		for (int i=0; i<dims-1; i++){
			if(tuple[i]==-1){ //Beta-likeness dummy tuple.
				return 0;
			}

			if (i==0 || i==2){
				int[] distinctValues = MinMaxPerDim[i];
				min = distinctValues[0];
				max = distinctValues[1];
				if (tuple[i] < min)
					score+=(double)(max-tuple[i])/(double)(cardinalities[i]-1);
				else if ((tuple[i]>max) && (max != -1))
					score+=(double)(tuple[i]-min)/(double)(cardinalities[i]-1);
				else
					score+=(double)(max-min)/(double)(cardinalities[i]-1);
			}else{
				LinkedList<Integer> distinctValues = distinctValuesPerDim[i];
				if (!distinctValues.contains((int)tuple[i])){
					if(distinctValues.contains(-1))
						score+=(double)(distinctValues.size()-1)/(double)(cardinalities[i]-1); //dummy
					else
						score+=(double)(distinctValues.size())/(double)(cardinalities[i]-1);
				}else{
					if(distinctValues.contains(-1))
						score+=(double)(distinctValues.size()-2)/(double)(cardinalities[i]-1); //dummy
					else
						score+=(double)(distinctValues.size()-1)/(double)(cardinalities[i]-1);
				}
			}
		}
		return score;
	}
	private static double NCP_mixed(int[][] MinMaxPerDim, LinkedList<Integer>[] distinctValuesPerDim){
		double score=0.0;

		for (int i=0; i<dims-1; i++){
			if (i==0 || i==2){
				int[] distinctValues = MinMaxPerDim[i];
				score+=(double)(distinctValues[1]-distinctValues[0])/(double)(cardinalities[i]-1);
			}else{
				LinkedList<Integer> distinctValues = distinctValuesPerDim[i];
				score+=(double)(distinctValues.size()-1)/(double)(cardinalities[i]-1);
			}
		}
		return score;
	}

	private static void findSet_mixed(int assign_number, short[] newTuple){
		LinkedList<Integer>[] dValues = distinctValuesPerAssign[assign_number];
		int[][] MinMaxPerDim = MinMaxPerAssign[assign_number];
		if(newTuple[1]==-1){ //Beta-Likeness -- not sure about this!
			return; //dummy tuple
		}
		for (int i=0; i<dims; i++){ //we need SA, too!
			if (i==0 || i==2){
				int[] distinctValues = MinMaxPerDim[i];
				if ((newTuple[i] < distinctValues[0])||(distinctValues[0]==-1)){
					distinctValues[0]=(int) newTuple[i];
				}
				if ((newTuple[i] > distinctValues[1])||(distinctValues[1]==-1)){
					distinctValues[1]=(int) newTuple[i];
				}
			}else{
				LinkedList<Integer> distValuesPerDim = dValues[i];
				if (distValuesPerDim != null)
					if (!distValuesPerDim.contains((int)newTuple[i]))
						distValuesPerDim.add((int)newTuple[i]);
			}

		}
		return;
	}


	///////////////////Range Representation//////////////			
	private static double NCP_numerical(short[] tuple1, short[] tuple2){
		double score=0.0;

		//if (tuple1[SA] == tuple2[SA]) //Beta-likeness does not require this!
		//	return BIG; //inf

		for (int i=0; i<dims-1; i++){
			if((tuple1[i]==-1)||(tuple2[i]==-1)){ //Beta-likeness dummy tuple.
				return 0;
			}
			score+=(double)Math.abs(tuple1[i]-tuple2[i])/(double)(cardinalities[i]-1);

		}
		return score;
	}

	private static double NCP_numerical(short[] tuple, int[][] MinMaxPerDim,
			LinkedList<Integer> distinctValues2){
		double score=0.0;
		int min;
		int max;

		//if (distinctValues2.contains((int)tuple[SA]))  //Beta-likeness does not require this!
		//	return BIG; //inf

		for (int i=0; i<dims-1; i++){
			if(tuple[i]==-1){ //Beta-likeness dummy tuple.
				return 0;
			}
			int[] distinctValues = MinMaxPerDim[i];
			min = distinctValues[0];
			max = distinctValues[1];
			if (tuple[i] < min)
				score+=(double)(max-tuple[i])/(double)(cardinalities[i]-1);
			else if ((tuple[i]>max)&&(max!=-1))
				score+=(double)(tuple[i]-min)/(double)(cardinalities[i]-1);
			else
				score+=(double)(max-min)/(double)(cardinalities[i]-1);
		}
		return score;
	}

	private static double NCP_numerical(int[][] MinMaxPerDim){
		double score=0.0;
		for (int i=0; i<dims-1; i++){
			int[] distinctValues = MinMaxPerDim[i];
			score+=(double)(distinctValues[1]-distinctValues[0])/(double)(cardinalities[i]-1);

		}
		return score;
	}

	private static void findSet_numerical(int assign_number, short[] newTuple){
		int[][] MinMaxPerDim = MinMaxPerAssign[assign_number];
		if(newTuple[0]==-1){ //Beta-Likeness -- not sure about this!
			return; //dummy tuple
		}
		for (int i=0; i<MinMaxPerDim.length; i++){
			int[] distinctValues = MinMaxPerDim[i];
			if ((newTuple[i] < distinctValues[0])||(distinctValues[0]==-1)){
				distinctValues[0]=(int) newTuple[i];
			}
			if ((newTuple[i] > distinctValues[1])||(distinctValues[1]==-1)){
				distinctValues[1]=(int) newTuple[i];
			}
		}

		return;
	}

	////////////////Set Representation///////////////
	private static double NCP(short[] tuple1, short[] tuple2){
		double score=0.0;

		//if (tuple1[SA] == tuple2[SA]) //Beta-likeness does not require this!
		//	return BIG; //inf

		for (int i=0; i<dims-1; i++){
			if((tuple1[i]==-1)||(tuple2[i]==-1)){ //Beta-likeness dummy tuple.
				return 0;
			}
			if (tuple1[i]==tuple2[i])
				score+=0;
			else 
				score+=(double)(1)/(double)(cardinalities[i]-1);
		}
		return score;
	}

	private static double NCP(short[] tuple, LinkedList<Integer>[] distinctValuesPerDim){
		double score=0.0;

		LinkedList<Integer> distinctValues2 = distinctValuesPerDim[SA];
		//if (distinctValues2.contains((int)tuple[SA])) //Beta-likeness does not require this!
		//	return BIG; //inf

		for (int i=0; i<dims-1; i++){
			if(tuple[i]==-1){ //Beta-likeness dummy tuple.
				return 0;
			}
			LinkedList<Integer> distinctValues = distinctValuesPerDim[i];
			if (!distinctValues.contains((int)tuple[i])){
				if (distinctValues.contains(-1))
					score+=(double)(distinctValues.size()-1)/(double)(cardinalities[i]-1); //dummy
				else
					score+=(double)(distinctValues.size())/(double)(cardinalities[i]-1);
			}else{
				if (distinctValues.contains(-1))
					score+=(double)(distinctValues.size()-2)/(double)(cardinalities[i]-1); //dummy
				else
					score+=(double)(distinctValues.size()-1)/(double)(cardinalities[i]-1);
			}
		}
		return score;
	}

	private static double NCP(LinkedList<Integer>[] distinctValuesPerDim){
		double score=0.0;
		for (int i=0; i<dims-1; i++){
			LinkedList<Integer> distinctValues = distinctValuesPerDim[i];
			if(distinctValues.contains(-1))
				score+=(double)(distinctValues.size()-2)/(double)(cardinalities[i]-1); //dummy
			else
				score+=(double)(distinctValues.size()-1)/(double)(cardinalities[i]-1);
		}
		return score;
	}

	private static void findSet(int assign_number, short[] newTuple){
		LinkedList<Integer>[] distinctValues = distinctValuesPerAssign[assign_number];
		if(newTuple[0]==-1){ //Beta-Likeness -- not sure about this!
			return; //dummy tuple
		}
		for (int i=0; i<distinctValues.length; i++){
			LinkedList<Integer> distValuesPerDim = distinctValues[i];
			if (distValuesPerDim != null)
				if (!distValuesPerDim.contains((int)newTuple[i]))
					distValuesPerDim.add((int)newTuple[i]);
		}
		return;
	}

	private static int bucketToIndexMapping(int bucket, int i){
		return bucket*bucket_size+i;
	}

	private static short[] indexToTupleMapping(int index){
		return buckets[index/bucket_size][index%bucket_size];
	}
}
