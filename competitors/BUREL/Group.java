/**
 * @file Group
 * @created date Jun 5, 2008
 * @author CAO Jianneng
 * It stores a group of records, which will be generalized
 */
import java.util.ArrayList ;
import java.util.HashSet;
import java.util.LinkedList;

public class Group {
    public ArrayList< Record > members ;//the members of the equivalence class
    public double cost ; //information loss
    public double partial_cost[] ; //the information loss of each dimension
    //the range of each dimension
    public int start[] ;
    public int end[] ;
    public  LinkedList<HashSet<Integer>> distinct;//***CHANGE***
    
    public String ascii_MBR ;//MBR of the group
    
    public Group ( ){
        members = new ArrayList<Record> () ;
        int QIsize = global.DMSION -1 ;
        partial_cost = new double[QIsize] ;
        start = new int[QIsize] ;
        end = new int[QIsize] ; 
        distinct = new LinkedList<HashSet<Integer>>();//***CHANGE***
        ascii_MBR = new String() ;
    }
    
    public Group( Record rec ){
        members = new ArrayList<Record> () ;
        members.add(rec) ;
        
        int QIsize = global.DMSION - 1 ;
        partial_cost = new double[QIsize] ;        
        start = new int[QIsize] ;
        end = new int[QIsize] ;
                     
        for(int i = 0 ; i < QIsize; i ++){
            start[i]=end[i]=rec.data[i] ;
            partial_cost[i] = 0 ;
        }
                    
        cost = 0;
        
        ascii_MBR = new String() ;
    }
    
    public void insert_rec ( Record rec ){
        members.add(rec); 
    }
    
    public void add_all (ArrayList<Record> c){
        members.addAll(c);
    }
    
    public int size (){
        return members.size() ;
    }
    
    
    
    public String toString(){        
        int QIsize = global.DMSION - 1 ;
        int dist_index=0;
        for( int i = 0 ; i < QIsize ; i ++ ){    
        	if (global.isNumeric[i] == false){
        		ascii_MBR += distinct.get(dist_index).toString()+",";
        		dist_index++;
        	}else{
            ascii_MBR += start[i] ;
            ascii_MBR += "-" ;
            ascii_MBR += end[i] ;
            ascii_MBR += "," ;            
        	}
        }
        
        return ascii_MBR ;
    }
    
    public double get_MBR( ){        
        int QIsize = global.DMSION - 1 ;
        
        for ( int dm = 0; dm < QIsize; dm++ ){
            start[dm] = global.LARGE ;
            end[dm] = global.SMALL ;
            if (global.isNumeric[dm] == false)
            	distinct.add(new HashSet<Integer>()); 
	}        
      
	//obtain MBR of records
	int size = members.size() ;
        Record rec ;
        int categorical_index;
	for ( int i = 0 ; i < size ; i++ ){
            rec = members.get( i ) ; //records one by one
            categorical_index=0;
            for (int dm = 0; dm < QIsize ; dm++ ){ //dimensions one by one
            	
            	if (global.isNumeric[dm] == false) {//categorical attribute
            		distinct.get(categorical_index).add(rec.data[dm]);
            		categorical_index++;
            	}
            	else{
            		if ( rec.data[dm] < start[dm] )
                        start[dm] = rec.data[dm] ;
                    
                    if ( rec.data[dm] > end[dm] )
                        end[dm] = rec.data[dm] ;     
            	}
                               
            }
	}//end of finding MBR 

	cost = 0.0;
	categorical_index=0;
        //attribute by attribute
	for ( int dm = 0; dm < QIsize ; dm++ )
		if( global.isNumeric[dm] == false) {//categorical attribute
			double incr = (double)(distinct.get(categorical_index).size()-1)/(double)(global.domain_length[dm]) ;	//***CHANGE***			
			partial_cost[dm] = incr ; //the infoloss at dm
			cost += incr;  
			categorical_index++;
			
			
			
			
			/*
			int minval = start[dm], maxval = end[dm] ;
			if( minval == maxval ) {//leaf node
				cost += 0.0;
				partial_cost[dm] = 0 ;
				continue ;
			}

			boolean flag = true;
			Hentry HE = global.Hierarchy[dm] ;
			//level by level
			for( int j = 0; flag == true ; j ++ ) //j level                                   
				for ( int i = 0 ; i < HE.levels[j].length ; i ++ ){ //i element
					IntervalEntry ie = HE.levels[j][i] ;
					if( minval >= ie.start && maxval <= ie.finish ){
						double incr = (double)(ie.count-1)/(double)(HE.leaf_num-1) ;	//***CHANGE***			
						partial_cost[dm] = incr ; //the infoloss at dm
						cost += incr;                            
						assert((incr >= 0.0)&&(incr <= 1.0));
						flag = false;
						break;
					}
					assert(flag==false);			
				}
			System.out.println(partial_cost[3]);
				
		*/}else{//numerical attribute
			double incr = (double)(end[dm] - start[dm]) /
					(double)global.domain_length[dm];			
			partial_cost[dm] = incr ;
			cost += incr;			
			assert((incr >= 0.0)&&(incr <= 1.0));
		}
	//System.out.println(partial_cost[1]);

        return cost ;
    }
    
    //Before merge, MBR(start and end) of this is already
    public double merge( Group G ){
        int QIsize = global.DMSION - 1 ;
        members.addAll(G.members);
        
        //obtain MBR of tuples after G is inserted into this
	int size = G.members.size() ;
        Record rec ;
	for ( int i = 0 ; i < size ; i++ ){
            rec = G.members.get( i ) ;		
            for (int dm = 0; dm < QIsize ; dm++ ){
                if ( rec.data[dm] < start[dm] )
                    start[dm] = rec.data[dm] ;
                
                if ( rec.data[dm] > end[dm] )
                    end[dm] = rec.data[dm] ;                    
            }
	}//end of finding MBR                 
        
        cost = 0.0;
        //attribute by attribute
	for ( int dm = 0; dm < QIsize ; dm++ )
            if( global.isNumeric[dm] == false) {//categorical attribute
		int minval = start[dm], maxval = end[dm] ;
		if( minval == maxval ) {
                    cost += 0.0;
                    partial_cost[dm] = 0 ;
                    continue ;
		}
			
		boolean flag = true;
                Hentry HE = global.Hierarchy[dm] ;
                //level by level
		for( int j = 0; flag == true ; j ++ )                                    
                    for ( int i = 0 ; i < HE.levels[j].length ; i ++ ){
                        IntervalEntry ie = HE.levels[j][i] ;
			if( minval >= ie.start && maxval <= ie.finish ){
                            double incr = (double)ie.count/(double)HE.leaf_num ;				
                            partial_cost[dm] = incr ;
                            cost += incr;                            
                            assert((incr >= 0.0)&&(incr <= 1.0));
                            flag = false;
                            break;
                        }
                    assert(flag==false);			
                    }
            }else{//numerical attribute
                double incr = (double)(end[dm] - start[dm]) /
                        (double)global.domain_length[dm];			
                partial_cost[dm] = incr ;
                cost += incr;			
                assert((incr >= 0.0)&&(incr <= 1.0));
            }
				
        return cost ; 
    }//end of function 'merge'
    
    public double enlargement( Record t ){
	if ( members.isEmpty() )	
            return 0.0;
	
        int QIsize = global.DMSION -1 ;
        
        //start[i] and end[i] should not be changed
        int[] S1 = new int[QIsize] ;
        int[] E1 = new int[QIsize] ;
        for(int i = 0 ; i < QIsize ; i ++){
            S1[i] = start[i] ;
            E1[i] = end[i] ;
        }
        
	boolean[] flag_part = new boolean[QIsize];
	boolean flag_chg = false;        
	for (int i = 0; i < QIsize ; i++){
            if ( t.data[i] < S1[i] ) {
		S1[i] = t.data[i];
		flag_chg = true;
		flag_part[i] = true;
		continue ;
            }

            if (t.data[i] > E1[i]){
		E1[i] = t.data[i];
		flag_chg = true;
		flag_part[i] = true;
            }
	}//end of deciding which dimensions are changed

	if (flag_chg == false)//t is in the MBR of this
            return 0 ;

	double enlarged = 0.0;
	for ( int dm = 0; dm < QIsize ; dm++ ){
            if (flag_part[dm]==false)//this dimension is not changed
		continue;

            //categorical attribute
            if(global.isNumeric[dm] == false){
		int minval = S1[dm], maxval = E1[dm];
		if(minval == maxval){
                    //enlarged += 0.0;
                    continue ;
		}

		boolean flag = true;
                Hentry HE = global.Hierarchy[dm] ;
		
                for( int j = 0; flag == true ; j ++ ) 
                    for ( int i = 0 ; i < HE.levels[j].length ; i ++ ){
                        IntervalEntry ie = HE.levels[j][i] ;
			if( (minval >= ie.start) && (maxval <= ie.finish)){
                            double incr = (double)(ie.count)/(double)HE.leaf_num ;
                            enlarged += incr ;                            					
                            flag = false;
                            break;
			}			
                    assert(flag==false);			
		}
            }else{ //numerical attribute
		double incr = (double)(E1[dm] - S1[dm]) 
                        /(double)global.domain_length[dm];
		enlarged += incr ;			
            }
	}//all QI attributes are scanned
			
	return enlarged-cost ;
    }//end of function 'enlargement'
    
}
