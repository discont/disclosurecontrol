/**
 * @created date Feb 4, 2010
 * @author CAO Jianneng
 * @function this is the class for IO; the statistics about the anonymized data
 * are components of this class
 */
import java.io.* ;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class dataIO {

	public Record Recs[] ; //the input tuples
	static int pref_attr = 2;

	private BufferedWriter out ;
	private BufferedWriter non_out ;    

	public statistics Stats ; //the class for statistics
	public ArrayList<Group> Glist ;//to records all formed ECs    

	public dataIO (){ 
	}

	public void init( String rec_file, String anon_file,
			String stat_file ) {        
		try{           
			//the class for statistics
			Stats = new statistics( stat_file ) ;
			Stats.setStartTime() ;

			//read in records
			Recs = new Record[global.NUM] ;
			read_records(rec_file);

			//files to store anonymized tuples with their original ones
			out = new BufferedWriter(new FileWriter(anon_file));
			String str = "non_" ;
			str += anon_file ;
			non_out = new BufferedWriter(new FileWriter(str));                        

			Glist = new ArrayList<Group> () ;
		}catch (IOException e){
			System.out.println("IOException:");
			e.printStackTrace();
		}                
	}//end of function 'init'                      

	public void finish () {
		makeStats() ;



		try{
			out.close();
			non_out.close();            
		}catch (IOException e){
			System.out.println("IOException:");
			e.printStackTrace();
		}                        
	}             

	private void makeStats(){
		Stats.setEndTime(); //it is the time that all tuples are anonymized

		//the statistics for each EC
		int gNum = Glist.size() ;
		for( int i = 0 ; i < gNum ; i++ ){
			Group G = Glist.get(i) ;
			G.get_MBR() ;
			output_recs(G) ;
			Stats.output_group(G);
		}

//		System.out.println("Prefix Queries.");
		//double[] selectivities = {0.05, 0.1, 0.15, 0.2, 0.25};
		double[] selectivities = {0.05, 0.1, 0.15, 0.2, 0.25};
		double qErr = 0;
		FileWriter qw = null;
		try{

			if (global.pq==true) {
				int qtimes = 1000; //numer of random queries.
				double[] errArray = new double[qtimes];
				qw = new FileWriter("./BUREL_PrefixQueryError.txt",true); //true == append
				double[][] tmpres = new double[global.DMSION][2];
				System.out.println("Vary lambda (sel=0.1): ");
//				for (int l=1; l<global.DMSION; l++){
					qw.write(global.INPUT+" "+global.BETA+" "+selectivities[1]+" ");
					for (int qi=0; qi<global.DMSION; qi++){ //INITIALIZATION:
						tmpres[qi][0] = (double)global.domain_start[qi];//min QI value.
						tmpres[qi][1] = (double)global.domain_finish[qi];//>max QI value
					}
					qErr = PrefixQueries(tmpres, qtimes, errArray);
					qw.write(qErr+" \n");
//				}
			}
			if (global.rq==true) {
				int qtimes = 1000; //numer of random queries.
				double[] errArray = new double[qtimes];
				qw = new FileWriter("./BUREL_RangeQueryError.txt",true); //true == append
				double[][] tmpres = new double[global.DMSION][2];
				System.out.println("Vary lambda (sel=0.1): ");
				for (int l=1; l<global.DMSION; l++){
					qw.write(global.INPUT+" "+global.BETA+" "+l+" "+selectivities[1]+" ");
					for (int qi=0; qi<global.DMSION; qi++){ //INITIALIZATION:
						tmpres[qi][0] = (double)global.domain_start[qi];//min QI value.
						tmpres[qi][1] = (double)global.domain_finish[qi];//>max QI value
					}
					qErr = rangeQueries(selectivities[1], l, tmpres, qtimes, errArray);
					qw.write(qErr+" \n");
				}
				qw.write("\n");
				System.out.println("Vary selectivity (lambda=3): ");
				int l=3; //lambda = 3 first QIs.
				for (int i=0; i<selectivities.length; i++){
					qw.write(global.INPUT+" "+global.BETA+" "+l+" "+selectivities[i]+" ");
					for (int qi=0; qi<global.DMSION; qi++){ //INITIALIZATION:
						tmpres[qi][0] = (double)global.domain_start[qi];//min QI value.
						tmpres[qi][1] = (double)global.domain_finish[qi];//>max QI value
					}
					qErr = rangeQueries(selectivities[i], l, tmpres, qtimes, errArray);
					qw.write(qErr+" \n");
				}
				qw.write("\n");

			}
			if (global.nb==true){
			System.out.println("Naive Bayes Attack.");
		FileWriter nbw = null;
		try{
			NB NBattack = new NB(Glist, Recs);
			nbw = new FileWriter("./BUREL_NaiveBayesAttack.txt",true);
			nbw.write(global.INPUT+" "+global.BETA+" ");
			//double[] Laplace_rate = new double[1];
			//Laplace_rate[0] = 0.0;
			//double accuracy_rate = NBattack.accuracy(Laplace_rate);
			//nbw.write(accuracy_rate+" "+Laplace_rate[0]+"\n");
			double accuracy_rate = NBattack.accuracy();
			nbw.write(accuracy_rate+"\n");
			System.out.println("accuracy_rate = "+accuracy_rate+"\n");
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

			}
			/*qw.write("\n");
			System.out.println("Vary selectivity (lambda=3): ");
			int l=3; //lambda = 3 first QIs.
			for (int i=0; i<selectivities.length; i++){
				qw.write("b "+global.BETA+" dims "+l+" "+selectivities[i]+" ");
				for (int qi=0; qi<global.DMSION; qi++){ //INITIALIZATION:
					tmpres[qi][0] = (double)global.domain_start[qi];//min QI value.
					tmpres[qi][1] = (double)global.domain_finish[qi];//>max QI value
				}
				qErr = rangeQueries(selectivities[i], l, tmpres, qtimes, errArray);
				qw.write(qErr+" \n");
			}
			qw.write("\n");*/
		}catch(IOException ioe){
			System.err.println("IOException: " + ioe.getMessage());
		}finally{
			try{
				if(qw != null) qw.close();
			}catch(Exception e){
				System.err.println(e.getMessage());
			}
		}
                tcloseness T = new tcloseness(Recs) ;
		int SAindex = global.DMSION - 1 ;     
		if( global.isNumeric[SAindex] == true ){
			//number of sensitive values
			int SN = global.domain_finish[SAindex] + 1 ;            
			int senValues[] = new int[SN] ;
			for( int j = 0 ; j < SN ; j++ )
				senValues[j] = j ;

			T.init_numDist(senValues) ;
		}
		else //
			T.init_catDist( global.occup );
		T.maxDist(Glist) ;

		Stats.summary( T.max, T.min, T.avg, Glist) ;


		Stats.finish(); //close the statistics file
	}

	private void read_records ( String file )throws IOException  {
		BufferedReader in = new BufferedReader(new FileReader(file));
		String inLine ;

		int i = 0 ;
		while( (inLine = in.readLine()) != null ){
			Recs[i] = new Record( inLine ) ;
			i ++ ;            
		}

		in.close(); //close the tuple file
	}//end of function 'read_records'         

	public void addGroup ( Group G ){
		Glist.add(G);
	}

	public void addGs ( ArrayList<Group> Gs){        
		Glist.addAll(Gs);
	}

	private void output_recs ( Group G ){
		//obtain the string of the MBR
		if ( G.ascii_MBR.length() == 0 )
			G.toString() ;

		int gSize = G.size() ;
		//for debugging
		/*if(gSize <=1){
            String betaECSize = "" ;
            betaECSize += global.BETA ;
            betaECSize += "; a group has size<=1" ; 
            System.out.println( betaECSize ) ;
        }*/

		try{
			for ( int i = 0 ; i < gSize ; i ++ ){
				String str = new String ( G.ascii_MBR ) ;
				Record rec = G.members.get(i) ;                
				str += rec.data[global.DMSION-1] ;//sensitive value
				out.write(str);
				out.newLine() ;

				String myStr = rec.toStr() ;
				non_out.write(myStr);
				non_out.newLine() ;
			}
		}catch (IOException e){
			System.out.println("IOException:");
			e.printStackTrace();
		}   


	}//end of function 'output_recs'   
	double rangeQueries(double s, int lambda, double[][] rs, int times, double[] errArray){
		double error = 0.0;
		double absError=0.0;
		double genMx, genMn;
		double sele, range, min, max, random, r, overlap, randomPos;
		ArrayList<Integer> attrOptions = new ArrayList<Integer>();
		ArrayList<Integer> choices = new ArrayList<Integer>();
		//static final int[] attrNumber = new int[]{0, 1, 2, 3, 4, 5 , 6};
		int SAindex = global.DMSION - 1;

		sele = Math.pow(s, 1.0/((double)lambda+1.0));

		System.out.println("lambda ="+lambda+": ");

		for (int tm=0; tm<times; tm++){
			for (int qi=0; qi<global.DMSION; qi++){ //INITIALIZATION:
				rs[qi][0] = (double)global.domain_start[qi];//min SA value.
				rs[qi][1] = (double)global.domain_finish[qi];//>max SA value
			}
			//SA:
			range = (double)global.domain_length[SAindex] * sele;
			min = (double)global.domain_start[SAindex];//min SA value.
			max = (double)global.domain_finish[SAindex] - range;//max SA value - range.
			random = new Random().nextDouble();
			rs[SAindex][0] = min + (random * range);
			rs[SAindex][1] = rs[SAindex][0] + range;
			//others:
			attrOptions.clear();
			choices.clear();
			for (int i=0; i<7; i++){ attrOptions.add(i); } //originally

			//int temporary = 7;
			for (int ch=0; ch<lambda; ch++){
				//int next = new Random().nextInt(temporary);
				int next = ch;//System.out.println(next+" : "+(attrOptions.get(next)));
				choices.add(attrOptions.get(next));
				//attrOptions.remove(next);
			//	temporary--;
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
					range = (double)global.domain_length[i] * sele;
					min = (double)global.domain_start[i];//min attribute value.
					max = (double)global.domain_finish[i] - range;//max attribute value - range.
					random = new Random().nextDouble();
					rs[i][0] = min + (random * range);
					rs[i][1] = rs[i][0] + range;
				}else{ //Categorical:
					range = (double)Math.round((double)(global.domain_length[i]+1) * sele);
					if (range < 1.0) range = 1; //select at least 1 value.
					min = (double)global.domain_start[i];//min attribute value.
					//max = (double)MinMaxPerAttribute[i][1] - range;//max attribute value - range.
					random = new Random().nextDouble();
					randomPos = (double)Math.round(random * ((double)(global.domain_length[i]+1) * (1.0-sele)));
					rs[i][0] = min + randomPos;
					rs[i][1] = rs[i][0] + range;
				}
			}
			/*for (int i=0; i<lambda; i++)
				System.out.println(i+"s="+s+"sele="+sele+":["+MinMaxPerAttribute[i][0]+","
								   +MinMaxPerAttribute[i][1]+"]"+rs[i][0]+" "+rs[i][1]);
			System.out.println(" ");*/
			double cnt = 0; double anonCnt = 0;

			int gNum = Glist.size() ;
			for( int g = 0 ; g < gNum ; g++ ){
				r = 1.0; overlap=1.0;
				boolean inRange = true;
				boolean genRange = true;
				Group G = Glist.get(g) ;
				int gSize = G.size() ;
				for ( int gsz = 0 ; gsz < gSize ; gsz ++ ){
					Record rec = G.members.get(gsz) ; 
					int inx=0;
					for (int lIndex=0; lIndex<lambda; lIndex++){
						int l = (int)choices.get(lIndex);
						genMn = G.start[l];
						genMx = G.end[l];

						if (l==0 || l==2){ //Continuous:
							if ((genMx<rs[l][0])||(genMn>rs[l][1])){//gen out of query range.
								//if (!((genMx<rs[i][1])&&(genMn>rs[i][0]))){//gen not within range.
								genRange = false;
							}else{ //there is some overlap:
								if ((genMn<=rs[l][0])&&(rs[l][1]<=genMx)){
									overlap = (rs[l][1] - rs[l][0])/(genMx - genMn);
								}else if ((genMn<=rs[l][0])&&(genMx<=rs[l][1])){
									overlap = (genMx - rs[l][0])/(genMx - genMn);
								}else if ((rs[l][0]<=genMn)&&(rs[l][1]<=genMx)){
									overlap = (rs[l][1] - genMn)/(genMx - genMn);
								}else if ((rs[l][0]<=genMn)&&(genMx<=rs[l][1])){
									overlap = 1.0;
								}
								r = r * overlap;
							}
						}else{
							//categorical

							int tempCnt = 0;
							if (G.distinct.size()==0)
								System.out.println("lala");;
								Iterator it = G.distinct.get(inx).iterator();
								while (it.hasNext()){
									int value = (int) it.next();
									if((value>=rs[l][0])&&(value<=rs[l][1]))
										tempCnt++;
								}
								overlap = ((double)tempCnt) / ((double)G.distinct.get(inx).size());
								if (overlap == 0){
									genRange = false;
								}else
									r = r * overlap;
								inx++;
						}

						if (rec.data[l]<rs[l][0] || rec.data[l]>rs[l][1]){
							//orig out of range:
							inRange = false;
							break;
						}

					}
					if (rec.data[global.DMSION -1]<rs[SAindex][0] || rec.data[global.DMSION -1]>rs[SAindex][1]){//out of range
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

			}
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
	double rangeQueries_old(double s, int lambda, double[][] rs, int times, double[] errArray){
		double error = 0.0;
		double absError=0.0;
		double genMx, genMn;
		double sele, range, min, max, random, r, overlap, randomPos;
		ArrayList<Integer> attrOptions = new ArrayList<Integer>();
		ArrayList<Integer> choices = new ArrayList<Integer>();
		//static final int[] attrNumber = new int[]{0, 1, 2, 3, 4, 5 , 6};
		int SAindex = global.DMSION - 1;

		sele = Math.pow(s, 1.0/((double)lambda+1.0));

		System.out.println("lambda ="+lambda+": ");

		for (int tm=0; tm<times; tm++){
			for (int qi=0; qi<global.DMSION; qi++){ //INITIALIZATION:
				rs[qi][0] = (double)global.domain_start[qi];//min SA value.
				rs[qi][1] = (double)global.domain_finish[qi];//>max SA value
			}
			//SA:
			range = (double)global.domain_length[SAindex] * sele;
			min = (double)global.domain_start[SAindex];//min SA value.
			max = (double)global.domain_finish[SAindex] - range;//max SA value - range.
			random = new Random().nextDouble();
			rs[SAindex][0] = min + (random * range);
			rs[SAindex][1] = rs[SAindex][0] + range;
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
					range = (double)global.domain_length[i] * sele;
					min = (double)global.domain_start[i];//min attribute value.
					max = (double)global.domain_finish[i] - range;//max attribute value - range.
					random = new Random().nextDouble();
					rs[i][0] = min + (random * range);
					rs[i][1] = rs[i][0] + range;
				}else{ //Categorical:
					range = (double)Math.round((double)(global.domain_length[i]+1) * sele);
					if (range < 1.0) range = 1; //select at least 1 value.
					min = (double)global.domain_start[i];//min attribute value.
					//max = (double)MinMaxPerAttribute[i][1] - range;//max attribute value - range.
					random = new Random().nextDouble();
					randomPos = (double)Math.round(random * ((double)(global.domain_length[i]+1) * (1.0-sele)));
					rs[i][0] = min + randomPos;
					rs[i][1] = rs[i][0] + range;
				}
			}
			/*for (int i=0; i<lambda; i++)
				System.out.println(i+"s="+s+"sele="+sele+":["+MinMaxPerAttribute[i][0]+","
								   +MinMaxPerAttribute[i][1]+"]"+rs[i][0]+" "+rs[i][1]);
			System.out.println(" ");*/
			double cnt = 0; double anonCnt = 0;

			int gNum = Glist.size() ;
			for( int g = 0 ; g < gNum ; g++ ){
				r = 1.0; overlap=1.0;
				boolean inRange = true;
				boolean genRange = true;
				Group G = Glist.get(g) ;
				int gSize = G.size() ;
				for ( int gsz = 0 ; gsz < gSize ; gsz ++ ){
					Record rec = G.members.get(gsz) ; 
					int inx=0;
					for (int lIndex=0; lIndex<lambda; lIndex++){
						int l = (int)choices.get(lIndex);
						genMn = G.start[l];
						genMx = G.end[l];

						if (l==0 || l==2){ //Continuous:
							if ((genMx<rs[l][0])||(genMn>rs[l][1])){//gen out of query range.
								//if (!((genMx<rs[i][1])&&(genMn>rs[i][0]))){//gen not within range.
								genRange = false;
							}else{ //there is some overlap:
								if ((genMn<=rs[l][0])&&(rs[l][1]<=genMx)){
									overlap = (rs[l][1] - rs[l][0])/(genMx - genMn);
								}else if ((genMn<=rs[l][0])&&(genMx<=rs[l][1])){
									overlap = (genMx - rs[l][0])/(genMx - genMn);
								}else if ((rs[l][0]<=genMn)&&(rs[l][1]<=genMx)){
									overlap = (rs[l][1] - genMn)/(genMx - genMn);
								}else if ((rs[l][0]<=genMn)&&(genMx<=rs[l][1])){
									overlap = 1.0;
								}
								r = r * overlap;
							}
						}else{
							//categorical

							int tempCnt = 0;
							if (G.distinct.size()==0)
								System.out.println("lala");;
								Iterator it = G.distinct.get(inx).iterator();
								while (it.hasNext()){
									int value = (int) it.next();
									if((value>=rs[l][0])&&(value<=rs[l][1]))
										tempCnt++;
								}
								overlap = ((double)tempCnt) / ((double)G.distinct.get(inx).size());
								if (overlap == 0){
									genRange = false;
								}else
									r = r * overlap;
								inx++;
						}

						if (rec.data[l]<rs[l][0] || rec.data[l]>rs[l][1]){
							//orig out of range:
							inRange = false;
							break;
						}

					}
					if (rec.data[global.DMSION -1]<rs[SAindex][0] || rec.data[global.DMSION -1]>rs[SAindex][1]){//out of range
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

			}
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

	double rangeQueries2(double s, int lamda, double[][] rs, int times, double[] errArray){
		double error = 0.0;
		double absError=0.0;
		double genMx, genMn;
		double sele, range, min, max, random, r, overlap, randomPos;
		int SAindex = global.DMSION - 1;

		for (int tm=0; tm<times; tm++){
			sele = Math.pow(s, 1.0/((double)lamda+1.0));
			//SA:
			range = (double)global.domain_length[global.DMSION-1] * sele;
			min = (double)global.domain_start[global.DMSION-1];//min SA value.
			max = (double)global.domain_finish[global.DMSION-1] - range;//max SA value - range.
			random = new Random().nextDouble();
			rs[SAindex][0] = min + (random * range);
			rs[SAindex][1] = rs[SAindex][0] + range;
			//others:
			for (int i=0; i<lamda; i++){
				//Categorical:
				range = (double)Math.round((double)(global.domain_length[i]+1) * sele);
				if (range < 1.0) range = 1; //select at least 1 value.
				min = (double)global.domain_start[i];//min attribute value.
				//max = (double)MinMaxPerAttribute[i][1] - range;//max attribute value - range.
				random = new Random().nextDouble();
				randomPos = (double)Math.round(random * ((double)(global.domain_length[i]+1) * (1.0-sele)));
				rs[i][0] = min + randomPos;
				rs[i][1] = rs[i][0] + range;

			}
			/*for (int i=0; i<lamda; i++)
				System.out.println(i+"s="+s+"sele="+sele+":["+MinMaxPerAttribute[i][0]+","
								   +MinMaxPerAttribute[i][1]+"]"+rs[i][0]+" "+rs[i][1]);
			System.out.println(" ");*/
			double cnt = 0; double anonCnt = 0;

			int gNum = Glist.size() ;
			for( int g = 0 ; g < gNum ; g++ ){
				r = 1.0; overlap=1.0;

				Group G = Glist.get(g) ;
				int gSize = G.size() ;
				for ( int gsz = 0 ; gsz < gSize ; gsz ++ ){
					boolean inRange = true;
					boolean genRange = true;
					Record rec = G.members.get(gsz) ; 
					int inx=0;
					for (int l=0; l<lamda; l++){
						genMn = G.start[l];
						genMx = G.end[l];
						//categorical

						int tempCnt = 0;
						if (G.distinct.size()==0)
							System.out.println("lala");;
							Iterator<Integer> it = G.distinct.get(inx).iterator();
							while (it.hasNext()){
								int value = it.next();
								if((value>=rs[l][0])&&(value<=rs[l][1]))
									tempCnt++;
							}
							overlap = ((double)tempCnt) / ((double)G.distinct.get(inx).size());
							if (overlap == 0){
								genRange = false;
							}else
								r = r * overlap;
							inx++;




							if (rec.data[l]<rs[l][0] || rec.data[l]>rs[l][1]){
								//orig out of range:
								inRange = false;
								break;
							}




					}
					if (rec.data[global.DMSION -1]<rs[global.DMSION -1][0] || rec.data[global.DMSION -1]>rs[global.DMSION -1][1]){//out of range
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

			}
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
		System.out.println("query error (sel="+s+", lamda="+lamda+"): mean rel error="
				+((error/(double)times))+" abs error="+(absError/times)
				+" median rel error="+median);
		System.out.println("min="+errArray[0]+" max="+errArray[times-1]);
		//return (error/(double)times);
		return median;	
	}

	double PrefixQueries(double[][] rs, int times, double[] errArray){
		double error = 0.0;
		double absError=0.0;
		double genMx, genMn;
		double sele, range, min, max, random, r, overlap, randomPos;
		//int SAindex = global.DMSION - 1;

		for (int tm=0; tm<times; tm++){
			//sele = Math.pow(s, 1.0/((double)lamda+1.0));
			//SA:
			//range = (double)global.domain_length[pref_attr] * sele;
			min = (double)global.domain_start[pref_attr];//min SA value.
			//max = (double)global.domain_finish[pref_attr] - range;//max SA value - range.
			random = new Random().nextDouble();
			rs[pref_attr][0] = min;
			rs[pref_attr][1] = rs[pref_attr][0] + (double)global.domain_length[pref_attr]*random;
			//others:
			for (int i=0; i<5; i++){
				//Categorical:
				if (i==pref_attr)
					continue;
				min = (double)global.domain_start[i];//min attribute value.
				//max = (double)MinMaxPerAttribute[i][1] - range;//max attribute value - range.
				random = new Random().nextDouble();
				randomPos = (double)Math.round(random * ((double)(global.domain_length[i]+1)));
				rs[i][0] = min + randomPos;
				rs[i][1] = rs[i][0] ;

			}
			/*for (int i=0; i<lamda; i++)
				System.out.println(i+"s="+s+"sele="+sele+":["+MinMaxPerAttribute[i][0]+","
								   +MinMaxPerAttribute[i][1]+"]"+rs[i][0]+" "+rs[i][1]);
			System.out.println(" ");*/
			double cnt = 0; double anonCnt = 0;

			int gNum = Glist.size() ;
			for( int g = 0 ; g < gNum ; g++ ){
				r = 1.0; overlap=1.0;

				Group G = Glist.get(g) ;
				int gSize = G.size() ;
				for ( int gsz = 0 ; gsz < gSize ; gsz ++ ){
					boolean inRange = true;
					boolean genRange = true;
					Record rec = G.members.get(gsz) ; 
					int inx=0;
					for (int l=0; l<4; l++){
						genMn = G.start[l];
						genMx = G.end[l];
						//categorical

						int tempCnt = 0;
						if (G.distinct.size()==0)
							System.out.println("lala");;
							Iterator<Integer> it = G.distinct.get(inx).iterator();
							while (it.hasNext()){
								int value = (int) it.next();
								if((value>=rs[l][0])&&(value<=rs[l][1]))
									tempCnt++;
							}
							overlap = ((double)tempCnt) / ((double)G.distinct.get(inx).size());
							if (overlap == 0){
								genRange = false;
							}else
								r = r * overlap;
							inx++;




							if (rec.data[l]<rs[l][0] || rec.data[l]>rs[l][1]){
								//orig out of range:
								inRange = false;
								break;
							}




					}
					if (rec.data[global.DMSION -1]<rs[global.DMSION -1][0] || rec.data[global.DMSION -1]>rs[global.DMSION -1][1]){//out of range
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

			}
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
		System.out.println("query error mean rel error="
				+((error/(double)times))+" abs error="+(absError/times)
				+" median rel error="+median);
		System.out.println("min="+errArray[0]+" max="+errArray[times-1]);
		//return (error/(double)times);
		return median;	
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
}
