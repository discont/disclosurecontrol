/**
 * @Date Feb 16 2010
 * @author CAO Jianneng
 * @function to further split ECs to improve the information quality
 * based on HN
 */
import java.util.* ;

public class HNSplit {    
    private int _senIndex ;    
    private int _senNum ;       
    private int portionNum ;
    private Comparator RecComp ;

    public ArrayList<Group> newGs ;

    public HNSplit( int pNum ) {
        _senIndex = global.DMSION -1 ;
        _senNum = global.domain_finish[_senIndex]+1 ;        
        portionNum = pNum ;
        RecComp = new rec_comp();

        newGs = new ArrayList<Group> () ;
    }

    private double[] computeRatios( ArrayList<Record> Recs ){
        double [] ratios = new double[_senNum] ;
        int c[] = new int[_senNum] ;

        int Rnum = Recs.size() ;
        for( int i = 0 ; i < Rnum ; i++ ){
            Record r = Recs.get(i) ;
            c[r.data[_senIndex]]++ ;
        }

        double total = Recs.size() ;
        for( int i = 0 ; i < _senNum ; i++ )
            ratios[i] = (double)c[i]/total ;

        return ratios ;
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

    public ArrayList<Group> split( ArrayList<Record>[] LIST ){        
        newGs.clear() ; //clear the result first
        
        //sort each LIST        
        for( int i = 0 ; i < LIST.length ; i ++)
            Collections.sort( LIST[i], RecComp );

        partitionSpace( LIST ) ;
        return newGs ;
    }

    private Group formG ( ArrayList<Record>[] LIST ){
        Group G = new Group() ;
        for( int i = 0 ; i < portionNum ; i++ )
            G.add_all(LIST[i]);
        
        return G ;
    }

    public boolean privacyCheck(ArrayList<Record> recordList) {
        double realRatios [] = computeRatios( recordList ) ;
        for( int i = 0 ; i< _senNum ; i++ )
            if( realRatios[i] > global.fPs[i] )
                return false;

        return true ;
    }   

    private void partitionSpace( ArrayList<Record>[] LIST ) {
        //see if some part contains at least 2 tuples
        boolean L2 = true ;
        for( int i = 0 ; i < portionNum ; i ++ ){
            if( LIST[i].size() >= 2 ){
                L2 = false ;
                break ;
            }
        }
        if( L2 == true ){
            newGs.add(formG(LIST));
            return ;
        }

        //dichotomize LIST
        ArrayList<Record>[] LC =(ArrayList<Record> [])
                new ArrayList[portionNum] ;        
        ArrayList<Record>[] RC =(ArrayList<Record> []) 
                new ArrayList[portionNum] ;
        for( int i = 0 ; i < portionNum ; i ++ ){
            LC[i] = new ArrayList<Record> () ;
            RC[i] = new ArrayList<Record> () ;
        }

        ArrayList<Record> G1 = new ArrayList<Record>() ;
        ArrayList<Record> G2 = new ArrayList<Record>() ;
        
        double temp, ls ;
        int total ;
        for( int i = 0 ; i < portionNum ; i ++ ){
            total = LIST[i].size() ;
            temp = (double)total/2.0 ;
            ls = (int)Math.round(temp) ;
            

            int j ;
            for( j = 0 ; j < ls ; j++ )
                LC[i].add( LIST[i].get(j) );
            G1.addAll(LC[i]);

            for( ; j < total ; j++ )
                RC[i].add(LIST[i].get(j)) ;
            G2.addAll(RC[i]);
        }

        if( !privacyCheck(G1)|| !privacyCheck(G2) ){//split is not allowed
            newGs.add(formG(LIST));
            return ;
        }else{
            G1.clear() ; //to release
            G2.clear() ; //memory            
            
            partitionSpace( LC ) ;
            partitionSpace( RC ) ;
        }        
    }
}
