/**
 * @created date Jun 5, 2008; revised on 22 Feb, 2010
 * @author CAO Jianneng
 * @function each tuple corresponds to one Record
 */
public class Record {
    //public int pos ; //pos is not used in the scheme
    public long HN ; //Hilbert number
    
    //the first global.DMSION attributes are QI
    //the last one is sensitive
    public int data[] ;
    
    public Record() { }
    
    public Record( String str ){        	
	data = new int[global.DMSION] ;
        
        String[] attrs = str.split("\\s+") ;
        /*pos = Integer.parseInt(attrs[0]) ; //for HN is available
        
        HN = Double.parseDouble(attrs[1]) ;  //HN is calculated in the code      
        
        for( int j = 0 ; j < global.DMSION ; j ++ )
            data[j] = Integer.parseInt(attrs[j+2]) ;  */
        
        int used = 0 ;
        for( int j = 0 ; j < global.DMLength ; j ++ ){
            if(global.valid[j]==false)
                continue ;
            data[used] = Integer.parseInt(attrs[j].split(",")[0]) ;
            used ++ ;
        }
        
        //assert( used == global.DMSION ) ;
        //******it is commented since we may need hilbert value 
        //to further split an EC
        //if(!global.method.equals("knn")) //partition based on HN
        
        HN = HilbertCurve.hilbert(data, global.DMSION -1 ) ;
    } //end of 'Record( String str )' 
    
    public String toStr(){
        String str = "" ;
        for(int i = 0 ; i < global.DMSION ; i ++){
            str += data[i] ;
            if( i != global.DMSION -1 )
                str += "," ;
        }
        return str ;
    }
}
