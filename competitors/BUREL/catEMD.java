/**
 * @file EMD
 * @created date Jul 17, 2008
 * @author CAO Jianneng
 */
import java.util.* ;

public class catEMD {
    private class node{//node in the hierarchy of sensitive attribute
        public double dst ;
        public node children[] ;
        public String name ;
        public double height ;        
    };
    
    private node root ;
    private int treeHight ;
    private int leafCount ;    
    private node leaves[] ;
    private double ratios[] ;
    private int senSize ;
    private double emd ;    
    
    public catEMD() {}
    
    public void catInit(String[][] hier, int gValues[]) {
        //the hight and width of the hierarchy       
        int height = hier[0].length - 1 ;
        int width = hier.length -1 ;
                
        root = new node () ;
        root.name = hier[0][height] ;
        ArrayList <node> parent = new ArrayList<node> () ;
        parent.add( root ) ;
        ArrayList <node> child = new ArrayList<node> () ;
        node curPar ; 
        String curChild = new String() ;
        int parIndex ;
        
        //build hierarchy, level by level(column by column)
        for ( int col = height- 1; col >= 0 ; col-- ){            
            curPar = parent.get(0) ;
            parIndex = 0 ;
            ArrayList<String> children = new ArrayList<String> () ;
            //row by row
            curChild = "" ;
            for ( int row = 0; row <= width ; row ++  ){                
                if ( hier[row][col+1].equals(curPar.name) ){
                    //no duplicate child of curPar is added
                    if ( !curChild.equals( hier[row][col] ) ){
                        curChild = hier[row][col] ;
                        children.add( hier[row][col] ) ;
                    }
                }
                else{//we need to allocate children to curPar
                    int size = children.size() ;                    
                    curPar.children = new node[size] ;                    
                    for( int i = 0 ; i < size ; i ++ ){
                        node cnode = new node () ;
                        cnode.name = children.get(i) ;                            
                        curPar.children[i] = cnode ;
                        child.add(cnode);
                    }                                                
                    
                    //process the next parent and its children
                    parIndex ++ ;
                    curPar = parent.get(parIndex) ;
                    children.clear();
                    children.add(hier[row][col]);
                    curChild = hier[row][col] ;
                }
                
                if( row == width ){//for the last element at the column
                    int size = children.size() ;                    
                    curPar.children = new node[size] ; 
                    for( int i = 0 ; i < size ; i ++ ){
                        node cnode = new node () ;
                        cnode.name = children.get(i) ;                            
                        curPar.children[i] = cnode ;
                        child.add(cnode);
                    }                                                
                }
            }//end of inner for
            
            //proceed to process next column
            parent.clear() ;
            parent.addAll(child); 
            child.clear();
        }//end of for
       
        //hight, leaves, labels        
        leafCount = 0 ;
        treeHight = height ;      
        senSize = width + 1 ;
        leaves = new node[senSize] ;
        hierParamInit(root) ;
        
        ratios = computeRatios(gValues) ;
    }        
    
    private void hierParamInit( node subRoot){
        if ( subRoot.children == null ){//leaf node       
            subRoot.height = 0 ;            
            leaves[leafCount] = subRoot ;
            leafCount ++ ;            
        }else{
            double subH = 0 ;
            int childNum = subRoot.children.length ;
            for ( int i = 0 ; i < childNum ; i++ ){
                node child = subRoot.children[i] ;
                hierParamInit( child) ;
                if( child.height > subH )
                    subH = child.height ;                
            }
            //parent is higher than child by '1.0/(double)treeHight'
            subRoot.height = subH + 1.0/(double)treeHight ;            
        }
    }
    
    private double[] computeRatios(int gValues[]){
        double rs[] = new double[senSize] ;
        for(int i = 0 ; i < senSize; i ++)
            rs[i] = 0 ;
                    
        int value ;
        int Length = gValues.length ;
        for(int i = 0 ; i < Length; i ++){//the frequency of each sensitive value           
            value = gValues[i] ;
            rs[value-1] ++ ;
        }
        
        for(int i = 0 ; i < senSize; i ++) //normalize the frequencies
            rs[i] = rs[i]/(double)Length ;
        
        return rs;
    }

    private void computeEMD( node root ){
         if( root.children == null )
            return ;

        double negative=0, positive=0 ; 
        for( int i = 0 ; i < root.children.length ; i ++ ){
            node g = root.children[i] ;
            computeEMD( g ) ;
            if( g.dst >= 0 )
                positive += g.dst ;
            else
                negative += g.dst ;
        }
        
        //min{|negative|,positive} can be cancelled under root
        //|root.dst| need to be cancelled by its siblings
        root.dst = positive + negative ;
        if( root.dst >= 0 ) //|negative|<= positive
            emd -= negative*root.height ;
        else
            emd += positive*root.height ;
    }
            
    public double EMD(int gValues[] ){
        double real_ratios[] = computeRatios(gValues) ;
        for(int i = 0 ; i < senSize; i ++)
            leaves[i].dst = ratios[i]-real_ratios[i] ;
        
        emd = 0;
        computeEMD(root) ;                
        
        return emd ;        
    }
}
