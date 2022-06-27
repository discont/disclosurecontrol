/**
 * @file global
 * @created date Feb 10, 2010
 * @author CAO Jianneng
 */

import java.io.* ;

public class global {
    public static int NUM ; //total number of records
    
    public static boolean rq;
    public static boolean pq;
    public static boolean nb;
    
    //number of valid dimensions, the first DMSION-1 are QI
    //the last one is sensitive attribute
    public static int DMSION ;        
    
    //indicate which attribute is turned on: t(turn on)/f(turn off)
    public static boolean valid[] ;   
    
     //the parameter for beta
    public static double BETA ;     
    
    //indicate which attribute is numerical (isNumeric[i] == true)
    public static boolean isNumeric [] ;            
    
    public static int domain_start[] ;
    public static int domain_length[] ;
    public static int domain_finish[] ;

    public static Hentry Hierarchy[] ;
    
    public static String method ;    
        
    
    /**values with initialization**/
    //domain and hierarchy
    public final static int DMLength = 8;
    private final static String domain_file = "domains.txt" ;
    private final static String hier_file = "hiers.txt" ;
        
    public static final int LARGE = 600000 ;
    public static final int SMALL = -600000 ;
       
    public static final boolean withLN = true ;
    public static boolean splitByHN ;

    private static double Ps[] ;

    public static double fPs[] ;    
    
    /**values not in use**/
    public static int KPARAM ; //initialized to be 1
    public static String INPUT;
    public final static double DIFFERENCE = 0.01 ;
    
    //hierarchies for categorical (sensitive) attribute           
    public static final String[][] martialStat ={
        {"Married-civ-spouse","Partner-present","Married","Any"},
        {"Married-AF-spouse","Partner-present","Married","Any"},
        {"Divorced","Partner-absent","Married","Any"},
        {"Separated","Partner-absent","Married","Any"},
        {"Widowed","Partner-absent","Married","Any"},
        {"Married-spouse-absent","Partner-absent","Married","Any"},
        {"Never-married","Never-married","Never-married","Any"}
    };         
            
    public static final String[][] occup ={
        {"Exec-managerial","White-collar","Any"},
        {"Prof-specialty","White-collar","Any"},
        {"Sales","White-collar","Any"},
        {"Adm-clerical","White-collar","Any"},
        {"Tech-support","Blue-collar","Any"},
        {"Craft-repair","Blue-collar","Any"},
        {"Machine-op-inspct","Blue-collar","Any"},
        {"Handlers-cleaners","Blue-collar","Any"},
        {"Transport-moving","Blue-collar","Any"},
        {"Priv-house-serv","Blue-collar","Any"},
        {"Protective-serv","Other","Any"},
        {"Armed-Forces","Other","Any"},
        {"Farming-fishing","Other","Any"},
        {"Other-service","Other","Any"}
    };
              
                
    public static void init( int N, double beta,
            String valid_file, int kparam, String input ){
        NUM = N ;
        BETA = beta ;        
        KPARAM = kparam ;
        INPUT = input;
        
        try{
            init_valid (valid_file) ;
            domains_init( domain_file ) ;
            read_hierarchies( hier_file ) ;
        }catch (IOException e){
            System.out.println("IOException:");
            e.printStackTrace();
        }        
    }
    
    private static void init_valid ( String file ) throws IOException {
        DMSION = 0 ;
        valid = new boolean [DMLength] ;
        BufferedReader in = new BufferedReader(new FileReader(file));
        String inLine ;
        for(int i = 0 ; i < DMLength ; i ++){
            inLine = in.readLine() ;            
            if ( inLine.charAt(0) == 't' ){
                valid[i] = true ;
                DMSION ++ ;
            }
            else// if ( c == 'f' )
                valid[i] = false ;
        }
    }
    
    private static void domains_init(String file)throws IOException {
        domain_start = new int[DMSION] ;
        domain_finish = new int[DMSION] ;
        domain_length = new int[DMSION] ;
        
        BufferedReader in = new BufferedReader(new FileReader(file));
        String inLine ;
        int used = 0 ;
        for( int i = 0 ; i < DMLength ; i ++ ){
            inLine = in.readLine() ; 
            if(valid[i]==false) //ignore turned-off attribute
                continue ;
            String[] sf = inLine.split("\\s+") ; 
            domain_start[used] = Integer.parseInt(sf[0]) ;
            domain_finish[used] = Integer.parseInt(sf[1]) ;
            domain_length[used] = domain_finish[used] -
                    domain_start[used] ;
            used ++ ;
        }
        
        assert( used == DMSION ) ;
        in.close () ;
    }//end of function 'domains_init'
    
    private static void read_hierarchies ( String hierarchies ) throws IOException {
        isNumeric = new boolean[DMSION] ;
        Hierarchy = new Hentry[DMSION-1] ;
        
        BufferedReader in = new BufferedReader(new FileReader(hierarchies));
        String inLine ;
                
        //dimensions one by one. Each one is read from a file
        //QI
        int used = 0 ;
        for( int i = 0 ; i < DMLength -1; i ++ ){ //the last attribute is sensitive
            inLine = in.readLine() ;            
            if( valid[i] == false )
                continue ;
            if( inLine.charAt(0) == '*' )
                isNumeric[used] = true ;
            else{
                isNumeric[used] = false ;
                Hierarchy[used] = new Hentry( inLine ) ;
            }
            used ++ ;
        }//end of for
        
        
        assert( used == DMSION -1 ) ;
        //sensitive dimension
        inLine = in.readLine() ;
        if( inLine.charAt(0) == '*' )
            isNumeric[used] = true ;
        else
            isNumeric[used] = false ;
        
        in.close();
    }//end of function 'read_hierarchies'       
    
    public static void initFPi( double freqRatios[] ){
        Ps = freqRatios ;        
        
        int SAindex = DMSION - 1 ;
        //number of sensitive values
        int _senNum = domain_finish[SAindex] + 1 ;
        fPs = new double[_senNum] ;

        if( withLN == false ){ //only beta
            for( int i = 0 ; i < _senNum ; i++ )
                fPs[i] = ( 1.0 + BETA )*Ps[i] ;
        }
        else{//min{beta, -LN(pi)}
            for( int i = 0 ; i < _senNum ; i++ ){
                if( Ps[i] == 0 )
                    fPs[i] = 0 ;
                else{
                    double negLnP = - Math.log( Ps[i] );
                    //increased is the min{global.BETA, negLnP}
                    double increased ;
                    if( BETA > negLnP )
                        increased = negLnP ;
                    else
                        increased = BETA ;

                    fPs[i] = ( 1.0 + increased )*Ps[i] ;
                }
            }//end of for
        }//end of if...else
    }
    
    public static void furtherSplit( String splitMethod ){
        if( splitMethod.equalsIgnoreCase("hn") )
            splitByHN = true ;
        else
            splitByHN = false ;
    }
}
