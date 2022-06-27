/**
 * @date Feb 4, 2010
 * @author CAO Jianneng 
 * @function anonymize data with respect to beta-likeness
 */
import java.util.* ;
        
public class BL {
    private class myList extends ArrayList<Record>{
       public myList(){
           super() ;
       }
        
       public void myRemove( int begin, int end ){
           super.removeRange(begin, end);
       }
    } ;
    
    private dataIO IO ;
        
    //each LIST represents a bucket
    private ArrayList<Record> [] LIST ;
    public ArrayList<int []> SIZES ;
    private ArrayList<Record> [] EC ;

    private HNSplit HNS ;
    private MondrianSplit MS ;
         
    public BL( dataIO objIO ){ 
        IO = objIO ;
    }        
        
    /* calculate each sensitive value's frequency
     * <'value', freq[value] >
     */
    private double[] calFrequency ( ){
        int SAindex = global.DMSION - 1 ;         
        //number of sensitive values
        int SN = global.domain_finish[SAindex] + 1 ;
        int freq[] = new int[SN] ;
        for(int i = 0 ; i < SN ; i++)
            freq[i] = 0 ;
        
        Record Recs[] = IO.Recs ;
        int si = global.DMSION - 1 ;
        int value ;
        for( int i = 0 ; i < global.NUM ; i ++ ){
            value = Recs[i].data[si] ;
            freq[value] ++ ;
        }
        
        double ratios[] = new double [SN] ;
        for( int i = 0 ; i < SN ; i++ )
            ratios[i] = (double)freq[i]/(double)global.NUM ;               
        
        return ratios ;
    }    
    
    public void anonymize( ){               
        //calculate the frequency of each sensitive value        
        double freqRatios[] = calFrequency( ) ;            
        //***for debugging
        /*double ratioTotal = 0 ;
        for( int i = 0 ; i < freqRatios.length ; i++ )
            ratioTotal += freqRatios[i] ;
        System.out.println( "total Ratio: " + ratioTotal ) ;*/
        
        //initialize f(pi): fPi[i]=(1+beta)*pi; fPi[i]=(1+min{pi, -LN(pi)})*pi
        global.initFPi( freqRatios ) ;
        
        //partition SA values by dynamic programming
        DPpartition DP = new DPpartition () ;
        DP.init( freqRatios ) ;
        Vector<ArrayList<VP>> buckets = new Vector<ArrayList<VP>>() ;
        DP.partition(buckets) ;
        
        //for statistics of f(pi) and buckets
        IO.Stats.PiBs(freqRatios, global.fPs, buckets);

        //allocate tuples to buckets
        //set the mapping <sv, LIST[i]>
        //a record with the sensitive value(sv) will be
        //pushed to LIST[i]
        int BNum = buckets.size() ;
        LIST =(ArrayList<Record> []) new ArrayList[BNum] ;
        for( int i = 0 ; i < BNum ; i ++ )
            LIST[i] = new ArrayList<Record> () ;
        
        HashMap<Integer, ArrayList<Record>> HM =
                new HashMap<Integer, ArrayList<Record>> () ;
        for ( int i = 0 ; i < BNum ; i ++ ){ //buckets one by one
            ArrayList<VP> B = buckets.elementAt(i) ;
            int Bsize = B.size() ;
            //all records whose sensitive values in B
            //will be allocated into LIST[i]
            for ( int j = 0 ; j < Bsize ; j ++ ){
                VP sv = B.get(j) ;
                HM.put(new Integer( sv.value ), LIST[i]) ;
            }
        }//end of mapping between sensitive value and LIST

        //allocate records to their LIST
        allocate_recs(HM) ;
        //**for debugging
        /*int totalSize = 0 ;
        for( int i = 0 ; i < BNum ; i++ )
            totalSize += LIST[i].size() ;
        if( totalSize != global.NUM ){
            System.out.println("Allocation of tuples is incorrect") ;
            return ;
        }*/
        
        //dynamically determinize the sizes of ECs
        GroupSize ECs = new GroupSize( buckets ) ;
        ECs.determine_sizes(LIST) ;       
        SIZES = ECs.SIZES ;
                                                     

        //form anonymized groups        
        EC =(ArrayList<Record> []) new ArrayList[BNum] ;
        for( int i = 0 ; i < BNum ; i ++ )
            EC[i] = new ArrayList<Record> () ;

        if( global.splitByHN )
            HNS = new HNSplit( BNum );
        else
            MS = new MondrianSplit() ;

        if(global.method.equals("hn"))//not in use
            HN_partition() ; //hilbert number only
        else if (global.method.equals("knn"))
            KNN_partition() ; //anonymization by KNN
        else
            knn_HN(); //prefered    
        
        //for debugging***
        /*int gSize = IO.Glist.size();
        for( int k = 0 ; k < gSize; k++ ){
            Group G = IO.Glist.get(k) ;
            boolean okay = HNS.privacyCheck( G.members );
            if ( okay == false )
                System.out.println("beta-likeness is not fully satisfied") ;
        }*/
    }
       
    
    /* HM is a mapping <sv, LIST[i]>
     * push each record with sensitive value 'sv' to LIST[i]
     */
    private void allocate_recs( HashMap<Integer, ArrayList<Record>> HM ){
        Record recs[] = IO.Recs ;
        int si = global.DMSION - 1 ;        
        ArrayList<Record> temp ;
        Integer key = new Integer(0) ;
        for( int i = 0 ; i < global.NUM ; i ++ ){
            key = recs[i].data[si] ;            
            temp = HM.get(key) ;
            temp.add(recs[i]);
        }
    }            
    
    //a comparator to sort each LIST[i]
    private class rec_comp implements Comparator{
        public int compare(Object o1, Object o2) {
            Record r1 = (Record)o1 ;
            Record r2 = (Record)o2 ;           
            
            if( r1.HN > r2.HN )
                return 1 ;
            
            if( r1.HN == r2.HN )
                return 0 ;
            
            return -1 ;
        }
                
    };
    
    /*take tuples from each bucket from the beginning*/
    private void HN_partition(){
        int i, j ;    
        
        //sort each LIST
        Comparator comp = new rec_comp();
        for( i = 0 ; i < LIST.length ; i++ )
            Collections.sort(LIST[i],comp);                 
                        
        //from[i]: the start subscript of next 
        //partialSize[i] records from LIST[i]
        int[] partialSize ;
        int from[] = new int[LIST.length] ;
        for(i = 0 ; i < LIST.length ; i ++ )
            from[i] = 0 ;        
        
        int listNum = LIST.length ;        
        for( int g = 0 ; g < SIZES.size() ; g ++ ) {//ECs one by one
            partialSize = SIZES.get(g) ;                        
            //form a group
            Group G = new Group() ;
            for(i = 0 ; i < listNum ; i ++ ){ //Lists one by ones
                EC[i].clear() ; //clear EC[i]
                int till = from[i] + partialSize[i] ;               
                //take the portion of records from LIST[i]    
                for(j = from[i]; j < till ; j++ ){ //in one list
                    G.insert_rec( LIST[i].get(j) );
                    EC[i].add( LIST[i].get(j) ); //refill EC[i]
                }
                from[i] = till ;//update from[i]
            }
            
            //G.get_MBR();
            //IO.addGroup(G);
            if( global.splitByHN == false )
                IO.addGs( MS.split(G) ) ;
            else
                IO.addGs( HNS.split(EC) );
        }//end of while
        
        //handle surplus records still in LIST, to be finished...
    }
    
    /*This is approximate KNN*/
    private void knn_HN(){
        int i;    
        
        //sort each LIST
        Comparator comp = new rec_comp();
        for( i = 0 ; i < LIST.length ; i ++)
            Collections.sort(LIST[i],comp);                 
                        
        myList myL [] = new myList[LIST.length] ;
        for( i = 0 ; i < LIST.length ; i ++ ){
            myL[i] = new myList() ;
            myL[i].addAll(LIST[i]);
        }
        Random ran = new Random() ;
                
        int[] partialSize ;  
        
        int listNum = LIST.length ;          
        for( int g = 0 ; g < SIZES.size() ; g ++ ) {//ECs one by one
            partialSize = SIZES.get(g) ;                        
            //form a group
            Group G = new Group() ;
            
            int begin = 0 ;
            while( partialSize[begin] == 0 ){
                EC[begin].clear();
                begin ++ ;
            }
            int ranNum = ran.nextInt( myL[begin].size()) ;
            long num = myL[begin].get(ranNum).HN ;
            
            for(i = begin ; i < listNum ; i ++ ){//Lists one by one
                portion_hn(num, myL[i], EC[i], partialSize[i]);
                G.add_all(EC[i]);
            }
            
            //G.get_MBR();
            //IO.addGroup(G);
            if( global.splitByHN == false )
                IO.addGs( MS.split(G) ) ;
            else
                IO.addGs( HNS.split(EC) );
        }//end of while        
        //handle surplus records still in LIST, to be finished...
    }
    
    private void portion_hn( long num, myList list, 
            ArrayList<Record> result, int neighbors ){
        result.clear(); 
        if(neighbors <= 0)
            return ;        
        
        //binary search for the closest record
        int L = 0 ;
        int R = list.size()-1 ;
        int m = 0 ;
        Record rec ;
        while( L <= R ){
            m = (L+R)/2 ;
            rec = list.get(m) ;
            if( rec.HN == num )
                break ;
            else if( num < rec.HN )
                R = m -1 ;
            else 
                L = m +1 ;
        }
        
        //get the neighbors
        int first = m, last=m+1 ;
        Record r1, r2 ;
        long d1, d2 ;
        int size = list.size() ;
        int count = 0 ;        
        while( count < neighbors && first >=0 && last < size ){
            r1 = list.get(first) ;
            r2 = list.get(last) ;
            d1 = r1.HN - num ;
            d2 = r2.HN - num ;
            if( Math.abs(d1)< Math.abs(d2) ){
                result.add(r1);
                first -- ;
            }else{
                result.add(r2);
                last ++ ;
            }
            count ++ ;
        }
        
        if( count < neighbors ){
            if( first < 0 ){                
                while(count < neighbors){
                    r2 = list.get(last) ;
                    result.add(r2) ;
                    last ++ ;
                    count ++ ;
                }
            }else{                
                while(count < neighbors){
                    r1 = list.get(first) ;
                    result.add(r1);
                    first -- ;
                    count ++ ;
                }                
            }
        }
         
        first ++ ;
        //remove the neighbors form list
        list.myRemove(first, last);
    }
    
    private void KNN_partition(){
        int i ;        
        int listNum = LIST.length ;
        
        int[] partialSize ;            
        for( int g = 0 ; g < SIZES.size() ; g ++ ) {//ECs one by one
            partialSize = SIZES.get(g) ;            
            
            //find the first list, from which some tuples need to be
            //taken out
            int begin = 0 ;
            while( partialSize[begin] == 0 ){
                EC[begin].clear();
                begin ++ ;
            }
            
            //form a group
            Record firstRec = LIST[begin].get(0) ;
            Group G = new Group( firstRec ) ;            
            LIST[begin].remove(0);
            portionKNN( G, LIST[begin], 
                    partialSize[begin]-1, EC[begin] ) ;
            EC[begin].add(firstRec); //this is important
                        
            for(i = begin+1 ; i < listNum ; i ++)
                portionKNN( G, LIST[i], partialSize[i], EC[i] ) ;
            
            //IO.addGroup(G);
            if( global.splitByHN == false )
                IO.addGs( MS.split(G) ) ;
            else
                IO.addGs( HNS.split(EC) );
        }//end of while
    }
    
    private void portionKNN( Group G, ArrayList<Record> list, 
            int neighbors, ArrayList<Record> result ){
        result.clear();
        
        if(neighbors <= 0) 
            return ;
        
        int size = list.size() ;
        TreeMap<Double, ArrayList<Record>> tMap = 
                new TreeMap<Double, ArrayList<Record>> () ;
        double incr ;
        Record rec ;
        //calculate the distance between each record in list and G
        //sort the records in the ascending order of their distances
        for( int i = 0; i < size; i ++){
            rec = list.get(i) ;
            incr = G.enlargement(rec) ;
            Double Dincr = new Double(incr) ;
            ArrayList<Record> L = tMap.get( Dincr ) ;
            if(L == null)
                tMap.put(Dincr, L=new ArrayList<Record>()) ;
            L.add(rec);
        }                
        
        //form a group with G's nearest 'neighbors' records
        Group g1 = new Group() ;        
        Iterator it ;
        list.clear();
        int num = 0 ;
        for( it = tMap.entrySet().iterator(); it.hasNext(); ){
            Map.Entry entry = (Map.Entry)it.next() ;
            ArrayList<Record> L = (ArrayList<Record>)entry.getValue() ;
            for(int i = 0 ; i < L.size() ; i++){                
                rec = L.get(i) ;
                if( num < neighbors ){
                    g1.insert_rec(rec);
                    result.add(rec) ;
                    num ++ ;
                }
                else
                    list.add(rec);
            }
        }
               
        //G and g1 are merged
        G.merge(g1);               
    }
}
