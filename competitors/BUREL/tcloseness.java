/**
 * @file tcloseness
 * @created date Feb 4, 2010
 * @author CAO Jianneng
 */
import java.util.ArrayList;

public class tcloseness {
    private boolean sen_numerical ;
    private numEMD numDist ;
    //not tested since the experiment is on CENSUS dataset with
    //numerical sensitive attribute
    private catEMD catDist ;

    private int gValues[] ;
    
    //the minmum, maximum, and average t values 
    public double min, max, avg ;
    
    public tcloseness( Record Recs[] ){
        int si = global.DMSION-1 ;
        gValues = new int[Recs.length] ;
        for( int i = 0 ; i < Recs.length ; i++ ){
            gValues[i] = Recs[i].data[si] ;
        }
        
        min = global.LARGE ;
        max = global.SMALL ;
        avg = 0 ;
    }
    
    public void init_numDist( int senValues[] ){
        sen_numerical = true ;
        numDist = new numEMD() ;
        numDist.numInit(senValues, gValues);
    }
    
    public void init_catDist( String[][] hier ){
        sen_numerical = false ;
        catDist = new catEMD() ;
        catDist.catInit(hier, gValues);
    }
     
    private double distance( int Lvalues[] ){
        double dist ;
        if( sen_numerical == true )
            dist = numDist.EMD(Lvalues) ;
        else
            dist = catDist.EMD(Lvalues) ;        
        return dist ;
    }
    
    public double maxDist( ArrayList<Group> Glist ){                
        //for real closeness
        int gNum = Glist.size() ;
        int si = global.DMSION -1 ;
        
        //ECs one by one
        for( int k = 0 ; k < gNum ; k++ ) {            
            Group G = Glist.get( k ) ; //k-th EC
            int size = G.size() ;
            int Lvalues[] = new int[size] ; //Lvalues records the SA values in G
            for(int i = 0 ; i < size ; i ++ ){
                Record rec = G.members.get(i) ; //i-th record
                Lvalues[i] = rec.data[si] ; //the SA value of i-th record
            }                       
            double dist = distance(Lvalues);
            avg += dist ;
            if( dist > max )
                max = dist ;
            if(dist < min )
                min = dist ;
        }//end of scanning all ECs
        
        avg = avg/(double)gNum ;
        
        return max ;
    }

}
