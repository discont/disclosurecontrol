
import java.util.ArrayList;

/**
 * @file: NB
 * @author: Jianneng Cao
 * @company: National University of Singapore
 * @date: Apr 6, 2012
 */

public class NB {

    public ArrayList<Group> Glist ;
    public Record Recs[] ;

    public double Pr[][][] ;
	public double PrLaplace[][][] ;
	public double SAcnts[];
	public double globalSAcnts[];

    private int QIsize ;
    private int SI ;

    public NB( ArrayList<Group> Gs, Record Rs[] ){
        Glist = Gs ;
        Recs = Rs ;
        QIsize = global.DMSION -1 ;
        SI = global.DMSION - 1 ;
    }
	
	public void calcStats(){
		
		for( int k = 0 ; k < 50 ; k++ )
			globalSAcnts[k] = 0; //INIT
		
		int gNum = Glist.size() ;
		for( int i = 0 ; i < gNum ; i++ ){
			Group G = Glist.get(i) ;
			
			for( int k = 0 ; k < 50 ; k++ )
				SAcnts[k] = 0; //INIT
			
			for(int m=0; i<G.size(); i++){
				int pos = (G.members.get(m)).data[SI];
				SAcnts[pos] += 1.0 ;//local #occurences of SA
				globalSAcnts[pos] += 1.0 ;//global #occurences of SA
			}
		}
	}

    //globlal stores each global SA count

    //get the Glist
    //enumerate each EC, find Pr[t_i|v]
    //store Pr[t_i][v] in an array
    public void calPr(  ){
        Pr = new double[7][100][50] ;
		PrLaplace = new double[7][100][50] ;
		for (int ii=0; ii<7; ii++)
			for (int ij=0; ij<100; ij++)
				for (int ik=0; ik<50; ik++){
					Pr[ii][ij][ik] = 0; //INIT
					PrLaplace[ii][ij][ik] = 0; //INIT
				}

        int gNum = Glist.size() ;
        for( int i = 0 ; i < gNum ; i++ ){
            Group G = Glist.get(i) ;
			
            //dimensions one by one
            for ( int dm = 0; dm < QIsize; dm++ ){
                //each possible value in the generalization of dm-th QI attribute
                for( int j = G.start[dm]; j <= G.end[dm] ; j++ ){
					//each SA value
                    for( int k = 0 ; k < 50 ; k++ )
						Pr[dm][j][k] += SAcnts[k];//G.vNum[k]//add #occurences of SA
                }
            }
        }//end of scanning ECs

        //
        for( int i = 0 ; i < QIsize ; i++ )
            for( int j=global.domain_start[i]; j<=global.domain_finish[i]; j ++)
                for( int k = 0 ; k < 50 ; k++ ){
					if( globalSAcnts[k] <= 0 ){
						PrLaplace[i][j][k] = 1.0/(globalSAcnts[k]
												+global.domain_length[i]);
                        Pr[i][j][k] = 0 ;
					}else{
						PrLaplace[i][j][k] = (Pr[i][j][k]+1.0) / (globalSAcnts[k]
												+global.domain_length[i]);
						Pr[i][j][k] = Pr[i][j][k] / globalSAcnts[k] ;
					}
                }
    }

    //prediction
    private boolean predict( Record R ){
       int hatV = -1 ;
       int hatVLaplace = -1;
       double prob = -1 ;
       double probLaplace = -1;
	   boolean found = false;
	   boolean foundLaplace = false;
       for( int k = 0 ; k < 50 ; k++ ){//predict k is the SA value of R
		   double tmp = globalSAcnts[k]/global.NUM;//global.fPs[k] ;
		   double tmpLaplace = globalSAcnts[k]/global.NUM;//global.fPs[k] ;
		   //System.out.println(tmp);
           for ( int dm = 0; dm < QIsize; dm++ ){
               int tj = R.data[dm] ;
			   //System.out.println("Pr[dm][tj][k] =Pr["+dm+"]["+tj+"]["+k+"] ");
			   //System.out.println(Pr[dm][tj][k]);
               tmp = tmp * Pr[dm][tj][k] ;
			   tmpLaplace = tmpLaplace * PrLaplace[dm][tj][k];
           }
           if( tmp > prob ){
               hatV = k ;
               prob = tmp ;
           }
		   
		   if( tmpLaplace > probLaplace ){
			   hatVLaplace = k ;
			   probLaplace = tmpLaplace ;
		   }
		   
       }//end of scanning each SA value

       if(hatV == R.data[SI])
           found = true;
       else
           found = false;
		
	   if(hatVLaplace == R.data[SI])
			foundLaplace = true;
	   else
			foundLaplace = false;
		
	   return found; // found -- classic NB,
							// foundLaplace -- NB with Laplacian correction.
    }

    public double accuracy(){
        int correct = 0 ;
		
		SAcnts = new double[50];
		globalSAcnts = new double[50];
		for( int k = 0 ; k < 50 ; k++ ){
			globalSAcnts[k] = 0; //INIT
			SAcnts[k] = 0; //INIT
		}
		
		calcStats();
		calPr();
		
        for( int i = 0 ; i < global.NUM ; i ++ ){
            Record R = Recs[i] ;

            if( predict( R ) == true)
                correct ++ ;
        }

        double rate = (double)correct/(double)global.NUM ;

        String str = "NB-accuracy: " ;
        str += rate ;
        System.out.println( str ) ;
		
		return rate;
    }

}
//NB.java
//Displaying NB.java.
