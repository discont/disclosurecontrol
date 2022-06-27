/**
 * @date Feb 04, 2010
 * @author CAO Jianneng 
 * @function statistics of the anonymized data
 */
import java.io.*;
import java.util.* ;

public class statistics {
    public double totalInfoloss ;
    public int outputTuples ;  
    public BufferedWriter outFile ;
    public long start, end ; //to record consumed time    
    
    private double Ps[] ;
    private double fPs[] ;
    private Vector<ArrayList<VP>> buckets ;
    
    public statistics( String stat_file ){        
        totalInfoloss = 0 ;
        outputTuples = 0 ;        
        try{                       
            outFile = new BufferedWriter(new FileWriter(stat_file));
        }catch (IOException e){
            System.out.println("IOException:");
            e.printStackTrace();
        }        
    }
    
    public void setStartTime(){
        start = System.currentTimeMillis();
    }
    
    public void setEndTime() {
        end = System.currentTimeMillis();
    }
    
    public void finish(){
        try{
            outFile.close();
        }catch(IOException e){
            System.out.println("IOException:");
            e.printStackTrace();
        } 
    }
    
    //statistics on an EC/group
    public void output_group( Group G ){
        int gsize = G.size() ;
        outputTuples += gsize ;
        totalInfoloss += (double)gsize * G.cost ;        
        
        try{            
            String str = new String(G.ascii_MBR) ;
            str += ", " + gsize ;
            str += ", " + G.cost ;
            outFile.write(str);  
            outFile.newLine() ;
        }catch(IOException e){
            System.out.println("IOException:");
            e.printStackTrace();
        }        
    }      
    
    public void PiBs( double P[], double fP[], Vector<ArrayList<VP>> Bs ){
        Ps = P ;
        fPs = fP ;
        buckets = Bs ;
    }
    
    private void statPiBs() throws IOException{
        outFile.write("beta: "+ global.BETA) ; 
        outFile.newLine() ;
        
        //Ps and fPs
        outFile.write("**(vi, pi, -LN(pi), f(pi))**"); 
        outFile.newLine() ;
        for( int i = 0 ; i < Ps.length ; i++ ){
            String str = "(" ;
            str += i + "," ;
            str += Ps[i] + "," ;
            str += -Math.log( Ps[i] ) + "," ;
            str += fPs[i] + ")  " ;
            outFile.write(str);            
        }
        outFile.newLine() ;
        
        //Bs
        int Bnum = buckets.size() ;
        outFile.write("**buckets from DPpartition: [(vi, pi)]**: "+ Bnum) ;
        outFile.newLine() ;
        for( int i = 0 ; i < Bnum ; i++ ){
            ArrayList<VP> B = buckets.elementAt(i) ;
            String str = "[" ;
            for( int j = 0 ; j < B.size() ; j++ ){
                VP sv = B.get(j) ;
                str += "(" + sv.value + "," + sv.freq + "); " ;
            }
            str += "]  " ;
            outFile.write(str);    
        }//end of scanning buckets
    }
    
    public void summary( double tmax, double tmin, double tavg,
            ArrayList<Group> Glist ){
        //consumed time
        String difference = "consumed time(MS): " + (end-start);
        System.out.println( difference ) ;
        
        //average information loss by GLM
        String numStr = "anonymized tuples: " + outputTuples ;
        String totalStr = "total information loss: " + totalInfoloss ;
        double avg = totalInfoloss/(double)outputTuples ;
        avg = avg/(double)(global.DMSION-1) ;
        String avgStr = "average information loss: " + avg ;        
        System.out.println( avgStr ) ;                    
        
        //average group size        
        int groupNum = Glist.size() ;        
        String GSstr = "Number of groups: " ;
        GSstr += groupNum ;
        double avgGS = (double)global.NUM/(double)groupNum ;                
        GSstr += "; Average group size: " ;
        GSstr += avgGS ;
        System.out.println( GSstr ) ;
        
        //discernibility metric
        long total = 0 ;        
        long gSize ;        
        for(int i = 0 ; i < groupNum ;  i ++){
            Group G = Glist.get(i) ;
            gSize = G.size() ;            
            total += gSize*gSize ;
        }
        
        String DM = "DM: " ;
        DM += total ;
        System.out.println( DM ) ;
        
        //the closeness
        String tStr = "t-closeness. max: " ;
        tStr += tmax ;
        tStr += ", min: " ;
        tStr += tmin ;
        tStr += ", avg: " ;
        tStr += tavg ;
        System.out.println( tStr ) ;
        
        try{
            outFile.write("*****summary information*****") ;
            outFile.newLine() ;
            outFile.write(difference);
            outFile.newLine() ;      
            outFile.write( numStr );
            outFile.newLine() ;
            outFile.write(totalStr);
            outFile.newLine() ;
            outFile.write(avgStr);            
            outFile.newLine() ;            
            outFile.write(GSstr);
            outFile.newLine() ;
            outFile.write(DM);
            outFile.newLine();
            outFile.write(tStr);
            outFile.newLine() ;
            
            statPiBs() ; //statistics for a better understanding of the effect
                         //of beta on anonymized data
        }catch(IOException e){
            System.out.println("IOException:");
            e.printStackTrace();
        }  
    }
    
    
   
    
}
