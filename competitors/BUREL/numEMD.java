/** 
 * @created date Jul 17, 2008; revised date 19 Feb 2010
 * @author CAO Jianneng
 * @function to calculate the EMD ofr numerical SA 
 * @notice we assume that SA value starts from 0; otherwise, please add a mapping
 */
import java.util.* ;

public class numEMD {
    private int senSize ; //the number of sensitive values
    private double ratios[] ; //global distribution
    private double emd ;
    private double d12 ;    
    
    public numEMD() {} 
    
    public void numInit(int senValues[], int gValues[]){ //for initialization
        senSize = senValues.length ;
        
        ratios = computeRatios(gValues) ;
        
        d12 = (double)1.0/(double)(senSize-1) ;
    }
            
    public double EMD( int gValues[] ){
        emd = 0 ;
        double real_ratios[] = computeRatios( gValues ) ;
        
        double temp = 0 ;
        int num = senSize -1 ;
        for( int i = 0 ; i < num ; i ++ ){
            temp += real_ratios[i]-ratios[i] ;
            emd += Math.abs(temp) ;
        }
        
        emd = d12*emd ;
        
        return emd ;
    }
    
    private double[] computeRatios( int gValues[] ){
        double rs[] = new double[senSize] ;
        for(int i = 0 ; i < senSize; i ++)
            rs[i] = 0 ;
                    
        int Length = gValues.length ;
        for(int i = 0 ; i < Length; i ++)//the frequency of each sensitive value            
            rs[ gValues[i] ] ++ ;
        
        
        for(int i = 0 ; i < senSize; i ++) //normalize the frequencies
            rs[i] = rs[i]/(double)Length ;
        
        return rs;
    }
}
