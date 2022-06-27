/**
 * @file HilbertCurve
 * @created date Jun 5, 2008
 * @author CAO Jianneng
 * Given a record calculate its Hilbert number
 */
public class HilbertCurve { 
    
    public HilbertCurve () {}
    
    public static long hilbert(int x[], int nDims) {
	int nBits;		
	nBits=63/nDims; //number of bits for each dimension 
	
        //if( nBits >= 63 ) return x[0] ;
                
        if( nBits >= 31 ) nBits = 21 ;
        
	int coord[] = new int[nDims] ;

	int cDOM = 1;
	cDOM <<= nBits;
	assert( cDOM > 0 );

	// assumption: x[j] lies in the range [domain_start, domain_finish]
	// we map x[j] to a value in the range [0,cDOM)
	for ( int j = 0 ; j < nDims ; j++ ) {            
            double temp = (double)(x[j]-global.domain_start[j]) ;
            temp = temp/(double)global.domain_length[j] ;
            coord[j] = (int)(temp * cDOM) ;                
                      
            if ( coord[j] >= cDOM ) 
                coord[j] = cDOM - 1 ;
	}
	long index = hilbertC2i(nDims,nBits,coord);	
	
        return index ;
}

    private static long hilbertC2i(int nDims, int nBits, int coord[]){
        long one = 1;
        long ndOnes = (one << nDims) - 1;
        long nthbits = (((one << nDims*nBits) - one) / ndOnes) >>> 1;
        int b, d;
        int rotation = 0; /* or (nBits * (nDims-1)) % nDims; */
        long reflection = 0;
        long index = 0;

        for ( b = nBits; b > 0; ){
            b-- ;
            long bits = reflection;
            reflection = 0;
            for ( d = 0; d < nDims; d++ )
                reflection |= ((coord[d] >>> b) & 1 ) << d;
            bits ^= reflection;
            bits = (((bits >>> rotation) | (bits << (nDims-rotation))) & ((1 << nDims) - 1)) ;
            index |= bits << nDims*b;
            reflection ^= one << rotation;
     
            /* rotation = (rotation + 1 + ffs(bits)) % nDims; */		
            bits &= -bits & ((1 << (nDims-1)) - 1);				
            while ( bits > 0 ){
                bits >>>= 1; 
                ++rotation;
            }
            if ( ++rotation >= nDims )					
                rotation -= nDims;						

        }
        
        index ^= nthbits;
        /*
          for (d = 1; index >>> d; d *= 2)
            index ^= index >>> d;
        */
        for (d = 1; ; d *= 2) {
            long t;
            if (d <= 32) {
                t = index >>> d;
                if ( t == 0 )
                    break;
            } else {
                t = index >>> 32;
                t = t >>> (d - 32);
                if ( t == 0 )
                    break;
            }
            index ^= t;
        }
        return index;
    }

}
