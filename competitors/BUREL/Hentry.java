/**
 * @file Hentry
 * @created date Jun 5, 2008
 * @author CAO Jianneng
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Hentry {
    public int max_level ;//the depth of the hierarchy
    public int leaf_num ;
    IntervalEntry levels[][] ;
    
    public Hentry ( ){}
    
    public Hentry ( String file ){
        try{
            set_hierarchy( file ) ;
        }catch( IOException e ){
            System.out.println("IOException:");
            e.printStackTrace();
        }
    }
    
    public void set_hierarchy (String file)throws IOException  {        
        BufferedReader in = new BufferedReader(new FileReader(file));
        String inLine ;

        //the first non-empty line indicates the depth of the hierarchy 
        inLine = in.readLine() ;
        String[] Smax = inLine.split("\\s+") ;        
        max_level = Integer.parseInt( Smax[0] ) ;
        levels = new IntervalEntry[max_level][] ;

        int tmp ;
        //levels one by one
        for (int j = 0; j < max_level; j ++ ){            
            inLine = in.readLine() ;
            String[] numL = inLine.split("\\s+") ;            
            tmp = Integer.parseInt( numL[0] ) ;//number of elements at level j
            levels[j] = new IntervalEntry[tmp] ;
            
            for ( int i = 0; i < tmp ; i++ ){
                inLine = in.readLine() ;
                String[] se = inLine.split("\\s+") ;                
                levels[j][i] = new IntervalEntry() ;
                levels[j][i].start = Integer.parseInt(se[0]) ;
                levels[j][i].finish = Integer.parseInt(se[1]) ;						
                /*
                 *count: the number of leaf nodes included by the node
                 * represented by levels[j][i]
                 */
                levels[j][i].count = levels[j][i].finish 
                        - levels[j][i].start + 1;                                   
            }//end of one level
        }//end of for    
                
        leaf_num = levels[max_level-1][0].count ;
        in.close();         
    }//end of function 'set_hierarchy'
    
}
