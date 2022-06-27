/**
 * @date Feb 4, 2010
 * @author CAO Jianneng 
 * @function partition SA values into buckets by dynamic programming
 */
import java.util.* ;

public class DPpartition {
    private ArrayList<VP> SAP ; //list of (vi, pi)--SA value and its distribution
    
    public DPpartition() {}
    
    private class VPcomp implements Comparator<VP>{

        public int compare(VP o1, VP o2) {
            if( o1.freq < o2.freq )
                return -1 ;
            if( o1.freq == o2.freq )
                return 0 ;
            
            return 1 ;
        }              
    };
    
    public void init( double freqRatios[] ){
        SAP = new ArrayList<VP>() ;                            
        
        //store SA values and their distributions into SAP
        for( int i = 0 ; i < freqRatios.length ; i++ )
            if( freqRatios[i] > 0 ){
                //sa value and its distribution
                VP instance = new VP( i, freqRatios[i] );
                SAP.add( instance );
            }
        
        //sort SAP in the ascending order of pi
        Comparator comp = new VPcomp() ;
        Collections.sort( SAP, comp );
        
        //**for debugging
		/*
		 * for( int i = 0 ; i < freqRatios.length-1 ; i++ ){ VP sv1 = SAP.get(i) ; VP
		 * sv2 = SAP.get(i+1) ; if( sv1.freq > sv2.freq ) System.out.println(
		 * "Sorting in SAP is incorrect" ) ; }
		 */
    }        
    
    //check if SAP[b].v, SAP[b+1].v, ..., SAP[e].v can be in a single bucket
    private boolean combinable( int b , int e ) {
        double sum = 0 ;
        for( int i = b ; i <= e ; i++ ){
            VP Elem = SAP.get(i) ;
            sum += Elem.freq ;
        }

        int minValue = SAP.get(b).value ;        
       
        if( sum < global.fPs[minValue] )
            return true ;
        else
            return false ;
    }
    
    //partition SA values into buckets
    public void partition( Vector<ArrayList<VP>> buckets ){
        int m = SAP.size() ;
        int N[] = new int[m] ;
        int S[] = new int[m] ;
        N[0] = 1 ;
        S[0] = 0 ;
        int b, e ;
        for( e = 1; e < m ; e++ ){
            N[e] = N[e-1] + 1 ; //SAP[e] alone in a bucket
            S[e] = e ; 
            b = e-1 ;
            while( b >= 0 && combinable( b, e )  ){
                if( b == 0 ){ //all SA values [0...e] can be in one bucket
                    N[e] = 1 ;
                    S[e] = 0 ;
                    break ;
                }
                if( 1 + N[b-1] < N[e] ){//dynamic programming
                    N[e] = 1 + N[b-1] ;
                    S[e] = b ;                  
                }
                b = b -1 ;
            }//end of while
        }//end of for

        buckets.clear();
        e = m-1 ;
        while( e >= 0 ){//stops when e is smaller than 0
            //S[e], S[e+1],..., e are in one bucket
            ArrayList<VP> B = new ArrayList<VP>() ;
            for( b = S[e]; b<= e ; b++ )
                B.add( SAP.get(b) );

            buckets.add(B);
            e = S[e]-1 ;
        }
    }
      
}
