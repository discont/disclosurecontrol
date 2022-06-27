/**
 * @Date Feb 15 2010
 * @author CAO Jianneng
 * @function to further split ECs to improve the information quality
 */
import java.util.* ;

public class MondrianSplit {    
    private int _sizeQI ;
    private int _senIndex ;
    private final static int _kPara = 1 ; //actually not in use
    private int _senNum ;
    
    public ArrayList<Group> newGs ;
    
    public MondrianSplit( ){        
        _sizeQI = global.DMSION-1 ;
        _senIndex = global.DMSION -1 ;
        _senNum = global.domain_finish[_senIndex]+1 ; 
        
        newGs = new ArrayList<Group>() ;
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

   
    public ArrayList<Group> split( Group G ){
        newGs.clear() ; //clear the result 
        
        partitionSpace( G ) ;
        
        return newGs ;
    }
    
    private void partitionSpace( Group G ) {        
        int gSize = G.size() ;
        if( gSize <  2*_kPara) {//impossible to split G
            newGs.add( G );
            return ;
        }

        Record rec;
        //the dimension to be split
        int dim = chooseDimension( G.members );
        //find the splitting point
        int median = findMedian(G.members, dim);

        Group G1 = new Group() ; //the left 
        Group G2 = new Group() ; //and right children       
        ArrayList<Record> tmpList = new ArrayList<Record>();

        for(int i = 0; i < gSize ; i++) { //split G into two
            rec = G.members.get(i);
            int value = rec.data[dim] ;
            if( value < median )
                G1.insert_rec(rec);
            else if( value > median )
                G2.insert_rec(rec);
            else if(value == median)
                tmpList.add(rec);
        }

        //make sure that the size of the two resultant list is the 'same'
        int addNum = (G2.size()-G1.size()+tmpList.size())/2;
        for(int j = 0; j < addNum; j++) {
            rec = tmpList.get(j);
            G1.insert_rec(rec);
        }
        for(int j = addNum; j < tmpList.size(); j++) {
            rec = tmpList.get(j);
            G2.insert_rec(rec);
        }
               
        //see if the splitting is allowed
        if( !privacyCheck(G1.members) || !privacyCheck(G2.members)) {
            newGs.add(G);
            return ;
        }
        else {
            G.members.clear() ; //to release
            tmpList.clear() ; //memory
            
            //recursion here
            partitionSpace( G1 );
            partitionSpace( G2 );            
        }
    }    

    private boolean privacyCheck(ArrayList<Record> recordList) {
        double realRatios [] = computeRatios( recordList ) ;
        for( int i = 0 ; i< _senNum ; i++ )
            if( realRatios[i] > global.fPs[i] )
                return false;

        return true ;
    }   

    public int chooseDimension(ArrayList<Record> recordList) {
        Record rec;
        int index;
        int[] minIndex = new int[_sizeQI];
        int[] maxIndex = new int[_sizeQI];

        for(int j = 0; j < _sizeQI; j++) {
            minIndex[j] = global.LARGE ;
            maxIndex[j] = global.SMALL ;
        }

        for(int i = 0; i < recordList.size(); i++) {
            rec = recordList.get(i);
            for(int j = 0; j < _sizeQI; j++) {
                index = rec.data[j] ;
                if(index < minIndex[j])
                    minIndex[j] = index;
                if(index > maxIndex[j])
                    maxIndex[j] = index;
            }
        }

        double minRange = 1.0;
        double range;
        int dimension = -1;

        //the heuristic is the opposite from that adopted by Mondrian
        //One heuristic chooses the dimension with the widest (normalized)
        //range of values
        for(int j = 0; j < _sizeQI; j++) {            
            range = (double)(maxIndex[j]-minIndex[j])/
                    (double)(global.domain_length[j]+1); //normalized widest
            if(range <= minRange) {
                minRange = range;
                dimension = j;
            }
        }
        if(dimension == -1)
            throw new RuntimeException("Error: no dimension found.");
        return dimension;
    }

    public int findMedian(ArrayList<Record> recordList, int dim) {               
        int maxValue = global.domain_finish[dim] ;
        int[] fs = new int[maxValue+1];

        Record rec; 
        for(int i = 0; i < recordList.size(); i++) {
            rec = recordList.get(i);
            fs[rec.data[dim]]++ ;
        }

        int total = recordList.size() ;
        double halfSize = (double)total/2.0 ;
        double currentTotal = 0;        
        for(int j = 0; j <= maxValue; j++) {
            currentTotal += fs[j];
            if(currentTotal >= halfSize )
                    return j;
        }
        throw new RuntimeException("Error: median not found.");
    }    

}
