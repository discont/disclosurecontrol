/**
 * @date Feb 15, 2010
 * @author CAO Jianneng 
 * @function dynamically determine the size of each EC
 */
import java.util.*;

public class GroupSize {  
    public int portionNum ;
    private double []fBi ;    
    public ArrayList<int []> SIZES ;//each group and its size    
    
    private class group{
        public int[] partialSize ;
        public group[] children ;
        
        public group(){
            partialSize = new int[portionNum] ;          
        }
    };        
    
    //numerical sensitive attribute
    public GroupSize( Vector<ArrayList<VP>> buckets ){
        portionNum = buckets.size() ;
        fBi = new double[portionNum] ;
        
        initFBi ( buckets ) ;
    }      
        
    private void initFBi( Vector<ArrayList<VP>> buckets ){
        int Bnum = buckets.size() ;
        if( global.withLN == false ){//qi <= (1+beta)*pi
            for( int i = 0 ; i < Bnum; i++ ){
                ArrayList<VP> B = buckets.get(i) ;
                VP sv = B.get(0) ;
                fBi[i] = ( 1.0 + global.BETA )*sv.freq ;
            }
        }else{//qi <= (1+min{beta, -LN(pi)})*pi
            for( int i = 0; i < Bnum; i++ ){
                ArrayList<VP> B = buckets.get(i) ;
                VP minP = B.get(0) ;            
                double negLnP = - Math.log(minP.freq);

                //increased is the min{global.BETA, negLnP}
                double increased ;
                if( global.BETA > negLnP )
                    increased = negLnP ;
                else
                    increased = global.BETA ;

                fBi[i] = ( 1.0 + increased )*minP.freq ;
            }
        }//end of if...else
    }
    
    //to see if G follows beta-likeness
    private boolean canSplit ( group G ){
        int i ;
        int Gsize = 0 ;
        for( i = 0 ; i < portionNum ; i++ )//the size of G
            Gsize += G.partialSize[i] ;
        
        for( i = 0 ; i < portionNum ; i++ ){
            double partialRatio = 
                    (double)G.partialSize[i]/(double)Gsize ;
            if( partialRatio > fBi[i] )
                return false ;
        }

        return true ;
    }

    //dichotomize B
    private void split( group B ){
        boolean L2 = true ; //see if some part contains at least 2 tuples
        for( int i = 0 ; i < portionNum ; i ++ ){            
            if( B.partialSize[i] >= 2 ){
                L2 = false ;
                break ;
            }
        }

        if( L2 == true )//each part of B is smaler than 2
            return ;

        group lchild = new group () ; //the two children
        group rchild = new group () ; //of B
        double temp ;
        for( int i = 0 ; i < portionNum ; i ++ ){
            temp = (double)B.partialSize[i]/2.0 ;
            lchild.partialSize[i] = (int)Math.round(temp) ;
            rchild.partialSize[i] = B.partialSize[i] - 
                    lchild.partialSize[i] ;
        }
                
        if( canSplit(lchild) && canSplit(rchild) ){ //split is okay
            B.children = new group[2] ;
            B.children[0] = lchild ;
            B.children[1] = rchild ;
            split(lchild) ; //recursively split children
            split(rchild) ;
        }        
    }
    
    private void leaves( group root ){
        if( root.children == null )//a leaf            
            SIZES.add(root.partialSize) ;
        else{
            leaves(root.children[0]) ;
            leaves(root.children[1]) ;
        }
    }
    
    public void determine_sizes( ArrayList<Record> [] LIST ){
        SIZES = new ArrayList<int []>() ;
        
        group ROOT = new group () ;        
        for(int i = 0 ; i < portionNum ; i ++ )
            ROOT.partialSize[i] = LIST[i].size() ;
                
        
        split(ROOT) ;
        
        leaves(ROOT) ;
    }
}
