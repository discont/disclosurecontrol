/**
 * @created date Feb 4, 2010
 * @author CAO Jianneng
 * @function beta-likeness is to control the information gain
 * of an attacker from the overall SA distribution to 
 * an local SA distribution of an EC. Unlike previous works, 
 * such as t-closeness, we measure the distance between pi and qi
 */
public class Main {    
    /**
     * @param args the command line arguments
     */    
    public static void main(String[] args) {   
        // TODO code application logic here        
        /*String args[]={
            "valid3.txt", //QI
            "3", //beta
            "500000", //file size         
            "sal500k.txt", //input file
            "o1_4.txt", //output file
            "stat.txt", //statistics file
            "knn-hn", // the method to retrieve tuples from buckets to form an EC 
            "hn" //the method to further split EC to improve information quality
        };*/
        
        //indicate which attributes are in QI
        String valid_file = args[0] ;        
        //likeness threshold
        double beta = Double.parseDouble(args[1]) ;        
        //number of input tuples
        int num = Integer.parseInt(args[2]) ; 
        String recs = args[3] ; //records file
        // initialization: domains, hiearchies, and global parameters
        global.init( num, beta, valid_file, 1, recs ) ;              
        
         //IO initialization        
       
        String out = args[4] ; //output file
        String stats = args[5] ; //statistics file                      
        dataIO IO = new  dataIO();
        global.method = args[6] ;
        global.rq = Boolean.parseBoolean(args[8]);
        global.pq = Boolean.parseBoolean(args[9]);
        global.nb = Boolean.parseBoolean(args[10]);
        IO.init(recs, out, stats); //read in data
        
        //we split an EC, if the resutant sub-ECs still satify \beta-likeness
        global.furtherSplit( args[7] ) ;
                
                      
        //the anonymization with regard to beta-likenss
        BL BA = new BL( IO ) ;
        BA.anonymize() ;

        //the output and statistics
        IO.finish();
    }
}
