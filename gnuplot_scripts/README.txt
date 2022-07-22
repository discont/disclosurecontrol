To execute our gnuplot script "Figures_GnuPlot.plt" and produce the graphs from the paper, run:
gnuplot Figures_GnuPlot.plt

BEFORE running this script you must separate the results and create the following files. You must use the filename lisetd below for each figure.


Figure 2a: 

"GR-10k-l.txt" contains the lines of LDiv_Greedy.txt where inputFile=census10k, tuples=10000, dims=8, threshold=0, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"GRth-10k-l.txt" contains the lines of LDiv_Greedy.txt where inputFile=census10k, tuples=10000, dims=8, threshold=1, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"SG-10k-l.txt" contains  contains the lines of LDiv_SortGreedy.txt where inputFile=census10k, tuples=10000, dims=8, threshold=0, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"SGth-10k-l.txt" contains the lines of LDiv_SortGreedy.txt where inputFile=census10k, tuples=10000, dims=8, threshold=1, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl  
"LD-10k-l.txt" contains the lines of LDiv_Hungarian.txt where inputFile=census10k, tuples=10000, dims=8, threshold=0, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"LDth-10k-l.txt" contains  the lines of LDiv_Hungarian.txt where inputFile=census10k, tuples=10000, dims=8, threshold=1, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"NH-10k-l.txt" contains the output of running NH with the 10k census dataset for ell=2,3,4,5,6,7,8,9,10

Figure 2b: 

"GR-unif-l.txt" contains the lines of LDiv_Greedy.txt where inputFile=uniform10k, tuples=10000, dims=8, threshold=0, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"GRth-unif-l.txt" contains the lines of LDiv_Greedy.txt where inputFile=uniform10k, tuples=10000, dims=8, threshold=1, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"SG-unif-l.txt" contains the lines of LDiv_SortGreedy.txt where inputFile=uniform10k, tuples=10000, dims=8, threshold=0, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"SGth-unif-l.txt" contains the lines of LDiv_SortGreedy.txt where inputFile=uniform10k, tuples=10000, dims=8, threshold=1, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"LD-unif-l.txt" contains the lines of LDiv_Hungarian.txt where inputFile=uniform10k, tuples=10000, dims=8, threshold=0, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"LDth-unif-l.txt" contains the lines of LDiv_Hungarian.txt where inputFile=uniform10k, tuples=10000, dims=8, threshold=1, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl 
"NH-unif-l.txt" contains the output of running NH with the uniform10k dataset for ell=2,3,4,5,6,7,8,9,10

Figure 2c: 

"GR-zipf-l.txt" contains the lines of LDiv_Greedy.txt where inputFile=zipf10k_05, tuples=10000, dims=8, threshold=0, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"GRth-zipf-l.txt" contains the lines of LDiv_Greedy.txt where inputFile=zipf10k_05, tuples=10000, dims=8, threshold=1, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"SG-zipf-l.txt" contains the lines of LDiv_SortGreedy.txt where inputFile=zipf10k_05, tuples=10000, dims=8, threshold=0, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"SGth-zipf-l.txt" contains the lines of LDiv_SortGreedy.txt where inputFile=zipf10k_05, tuples=10000, dims=8, threshold=1, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"LD-zipf-l.txt" contains the lines of LDiv_Hungarian.txt where inputFile=zipf10k_05, tuples=10000, dims=8, threshold=0, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"LDth-zipf-l.txt" contains the lines of LDiv_Hungarian.txt where inputFile=zipf10k_05, tuples=10000, dims=8, threshold=1, ell={2,3,4,5,6,7,8,9,10}, after running l-div_varying_ell.pl
"NH-zipf-l.txt" contains the output of running NH with the zipf10k_05 dataset for ell=2,3,4,5,6,7,8,9,10

Figure 3a: 

"GR-10k-d.txt" contains the lines of LDiv_Greedy.txt where inputFile=census10k, tuples=10000, threshold=0, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl
"GRth-10k-d.txt" contains the lines of LDiv_Greedy.txt where inputFile=census10k, tuples=10000, threshold=1, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl 
"SG-10k-d.txt" contains the lines of LDiv_SortGreedy.txt where inputFile=census10k, tuples=10000, threshold=0, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl
"SGth-10k-d.txt" contains the lines of LDiv_SortGreedy.txt where inputFile=census10k, tuples=10000, threshold=1, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl
"LD-10k-d.txt" contains the lines of LDiv_Hungarian.txt where inputFile=census10k, tuples=10000, threshold=0, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl
"LDth-10k-d.txt" contains the lines of LDiv_Hungarian.txt where inputFile=census10k, tuples=10000, threshold=1, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl 
"NH-10k-d.txt" contains the output of running NH with the census10k dataset with ell=10, for dims=2,3,4,5,6,7,8

Figure 3b: 

"GR-unif-d.txt" contains the lines of LDiv_Greedy.txt where inputFile=uniform10k, tuples=10000, threshold=0, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl
"GRth-unif-d.txt" contains the lines of LDiv_Greedy.txt where inputFile=uniform10k, tuples=10000, threshold=1, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl
"SG-unif-d.txt" contains the lines of LDiv_SortGreedy.txt where inputFile=uniform10k, tuples=10000, threshold=0, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl
"SGth-unif-d.txt" contains the lines of LDiv_SortGreedy.txt where inputFile=uniform10k, tuples=10000, threshold=1, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl
"LD-unif-d.txt" contains the lines of LDiv_Hungarian.txt where inputFile=uniform10k, tuples=10000, threshold=0, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl
"LDth-unif-d.txt" contains the lines of LDiv_Hungarian.txt where inputFile=uniform10k, tuples=10000, threshold=1, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl
"NH-unif-d.txt" contains the output of running NH with the uniform10k dataset with ell=10, for dims=2,3,4,5,6,7,8

Figure 3c: 

"GR-zipf-d.txt" contains the lines of LDiv_Greedy.txt where inputFile=zipf10k_05, tuples=10000, threshold=0, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl 
"GRth-zipf-d.txt" contains the lines of LDiv_Greedy.txt where inputFile=zipf10k_05, tuples=10000, threshold=1, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl 
"SG-zipf-d.txt" contains the lines of LDiv_SortGreedy.txt where inputFile=zipf10k_05, tuples=10000, threshold=0, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl 
"SGth-zipf-d.txt" contains the lines of LDiv_SortGreedy.txt where inputFile=zipf10k_05, tuples=10000, threshold=1, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl 
"LD-zipf-d.txt" contains the lines of LDiv_Hungarian.txt where inputFile=zipf10k_05, tuples=10000, threshold=0, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl 
"LDth-zipf-d.txt" contains the lines of LDiv_Hungarian.txt where inputFile=zipf10k_05, tuples=10000, threshold=1, ell=10, dims={2,3,...,8}, after running l-div_varying_d.pl 
"NH-zipf-d.txt" contains the output of running NH with the zipf10k_05 dataset with ell=10, for dims=2,3,4,5,6,7,8

NOTE: For the Figures 5a, 5b we need to MANUALLY add a 9th column in the corresponding files ("GR-n.txt", "p-GR-n.txt". "SG-n.txt", "p-SG-n.txt", etc.) 
The VALUES of this 9th column are the running times of NH (so that the ration of GR, SG and HG tmes to NH time may be claculated).

Figure 4a and Figure 5a: 

"GR-n.txt" contains the lines of LDiv_Greedy.txt where inputFile={census10k,census100k,census500k}, dims=8, ell=10, threshold=0, after running l-div_varying_n using p=1000
"p-GR-n.txt" contains the lines of LDiv_Greedy.txt where inputFile={census10k,census100k,census500k}, dims=8, ell=10, threshold=1, after running l-div_varying_n using p=1000
"SG-n.txt" contains the lines of LDiv_SortGreedy.txt where inputFile={census10k,census100k,census500k}, dims=8, ell=10, threshold=0, after running l-div_varying_n using p=1000
"p-SG-n.txt" contains the lines of LDiv_SortGreedy.txt where inputFile={census10k,census100k,census500k}, dims=8, ell=10, threshold=1, after running l-div_varying_n using p=1000
"LD-n.txt" contains the lines of LDiv_Hungarian.txt where inputFile={census10k,census100k,census500k}, dims=8, ell=10, threshold=0, after running l-div_varying_n using p=1000
"p-HG-n.txt" contains the lines of LDiv_Hungarian.txt where inputFile={census10k,census100k,census500k}, dims=8, ell=10, threshold=1, after running l-div_varying_n using p=1000
"NH-n.txt" contains the output of running NH with the census10k, census100k, and census500k datasets with ell=10, for dims=8


Figure 4b and Figure 5b: 

"GR-100k-p.txt" contains the lines of LDiv_Greedy.txt where inputFile=census100k, tuples=100000, dims=8, ell=10, threshold=0, after running l-div_varying_p using p=100,500,1000,10000, 50000, 100000
"GRth-100k-p.txt" contains the lines of LDiv_Greedy.txt where inputFile=census100k, tuples=100000, dims=8, ell=10, threshold=1, after running l-div_varying_p using p=100,500,1000,10000, 50000, 100000
"SG-100k-p.txt" contains the lines of LDiv_SortGreedy.txt where inputFile=census100k, tuples=100000, dims=8, ell=10, threshold=0, after running l-div_varying_p using p=100,500,1000,10000, 50000, 100000
"SGth-100k-p.txt" contains the lines of LDiv_SortGreedy.txt where inputFile=census100k, tuples=100000, dims=8, ell=10, threshold=1, after running l-div_varying_p using p=100,500,1000,10000, 50000, 100000
"LD-100k-p.txt" contains the lines of LDiv_Hungarian.txt where inputFile=census100k, tuples=100000, dims=8, ell=10, threshold=0, after running l-div_varying_p using p=100,500,1000,10000, 50000, 100000
"LDth-100k-p.txt" contains the lines of LDiv_Hungarian.txt where inputFile=census100k, tuples=100000, dims=8, ell=10, threshold=1, after running l-div_varying_p using p=100,500,1000,10000, 50000, 100000 
"NH-p.txt" contains the (constant) output of running NH with the census100k dataset, ell=10, dims=8 (used for reference as it does not support variable p)


NOTE: FOR FIGURES 4c, 5c YOU WILL NEED TO MANUALLY ADD A 9th (last) COLUMN IN THE FOLLOWING FILES. THE VALUES OF COLUMN WILL CORRESPOMD TO THE ZIPFIAN PARAMETER THETA THAT CAN BE FOUND IN THE INPUTFILE NAME. For example: 
zipf10k_05  ==>  theta = 0.5, 
zipf10k_06  ==>  theta = 0.6, 
zipf10k_07  ==>  theta = 0.7, etc. 

Figure 4c and Figure 5c: 

"GR-zipf-theta.txt" contains the lines of LDiv_Greedy.txt where inputFile={zipf10k_05,zipf10k_06,zipf10k_07,zipf10k_08,zipf10k_09}, tuples=10000, dims=8, ell=10, threshold=0, after running l-div_varying_theta
"GRth-zipf-theta.txt" contains the lines of LDiv_Greedy.txt where inputFile={zipf10k_05,zipf10k_06,zipf10k_07,zipf10k_08,zipf10k_09}, tuples=10000, dims=8, ell=10, threshold=1, after running l-div_varying_theta
"SG-zipf-theta.txt" contains the lines of LDiv_SortGreedy.txt where inputFile={zipf10k_05,zipf10k_06,zipf10k_07,zipf10k_08,zipf10k_09}, tuples=10000, dims=8, ell=10, threshold=0, after running l-div_varying_theta
"SGth-zipf-theta.txt" contains the lines of LDiv_SortGreedy.txt where inputFile={zipf10k_05,zipf10k_06,zipf10k_07,zipf10k_08,zipf10k_09}, tuples=10000, dims=8, ell=10, threshold=1, after running l-div_varying_theta 
"LD-zipf-theta.txt" contains the lines of LDiv_Hungarian.txt where inputFile={zipf10k_05,zipf10k_06,zipf10k_07,zipf10k_08,zipf10k_09}, tuples=10000, dims=8, ell=10, threshold=0, after running l-div_varying_theta
"LDth-zipf-theta.txt" contains the lines of LDiv_Hungarian.txt where inputFile={zipf10k_05,zipf10k_06,zipf10k_07,zipf10k_08,zipf10k_09}, tuples=10000, dims=8, ell=10, threshold=1, after running l-div_varying_theta
"NH-zipf-theta.txt" contains the output of running NH with the {zipf10k_05,zipf10k_06,zipf10k_07,zipf10k_08,zipf10k_09} datasets, ell=10, dims=8

Figure 6a: 

"GR-10k-b.txt" contains the lines of BLik_Greedy.txt where inputFile=census10k, tuples=10000, b_param={2,3,4,5,6,7,8,9,10}, after running b-lik_varying_b.pl
"SG-10k-b.txt" contains the lines of BLik_SortGreedy.txt where inputFile=census10k, tuples=10000, b_param={2,3,4,5,6,7,8,9,10}, after running b-lik_varying_b.pl
"HG-10k-b.txt" contains the lines of BLik_Hungarian.txt  where inputFile=census10k, tuples=10000, b_param={2,3,4,5,6,7,8,9,10}, after running b-lik_varying_b.pl
"Burel-10k-b.txt" contains the output of running Burel with the census10k dataset for b_param={2,3,4,5,6,7,8,9,10} after running burel_varying_b.pl

Figure 6b: 

"GR-uni-b.txt" contains the lines of BLik_Greedy.txt where inputFile=uniform10k, tuples=10000, b_param={2,3,4,5,6,7,8,9,10}, after running b-lik_varying_b.pl
"SG-uni-b.txt" contains the lines of BLik_SortGreedy.txt where inputFile=uniform10k, tuples=10000, b_param={2,3,4,5,6,7,8,9,10}, after running b-lik_varying_b.pl
"HG-uni-b.txt" contains the lines of BLik_Hungarian.txt  where inputFile=uniform10k, tuples=10000, b_param={2,3,4,5,6,7,8,9,10}, after running b-lik_varying_b.pl
"Burel-uni-b.txt" contains the output of running Burel with the uniform10k dataset for b_param={2,3,4,5,6,7,8,9,10} after running burel_varying_b.pl

Figure 6c: 

"GR-zip-b.txt" contains the lines of BLik_Greedy.txt where inputFile=zipf10k_05, tuples=10000, b_param={2,3,4,5,6,7,8,9,10}, after running b-lik_varying_b.pl
"SG-zip-b.txt" contains the lines of BLik_SortGreedy.txt where inputFile=zipf10k_05, tuples=10000, b_param={2,3,4,5,6,7,8,9,10}, after running b-lik_varying_b.pl
"HG-zip-b.txt" contains the lines of BLik_Hungarian.txt  where inputFile=zipf10k_05, tuples=10000, b_param={2,3,4,5,6,7,8,9,10}, after running b-lik_varying_b.pl
"Burel-zip-b.txt" contains the output of running Burel with the zipf10k_05 dataset for b_param={2,3,4,5,6,7,8,9,10} after running burel_varying_b.pl

Figure 7a and Figure 7b: 

"GR-10k-b3-n.txt" contains the lines of BLik_Greedy.txt where inputFile={census1k,census10k,census100k}, b_param=3, after running b-lik_varying_n.pl
"SG-10k-b3-n.txt" contains the lines of BLik_SortGreedy.txt where inputFile={census1k,census10k,census100k}, b_param=3, after running b-lik_varying_n.pl 
"HG-10k-b3-n.txt" contains the lines of BLik_Hungarian.txt where inputFile={census1k,census10k,census100k}, b_param=3, after running b-lik_varying_n.pl
"Burel-10k-b3-n.txt" contains the output of running Burel with the {census1k,census10k,census100k} datasets for b_param=3 after running burel_varying_n.pl

NOTE: FOR FIGURE 7c YOU WILL NEED TO MANUALLY ADD A 7th (last) COLUMN IN THE FOLLOWING FILES. THE VALUES OF COLUMN WILL CORRESPOMD TO THE ZIPFIAN PARAMETER THETA THAT CAN BE FOUND IN THE INPUTFILE NAME. For example: 
zipf10k_05  ==>  theta = 0.5, 
zipf10k_06  ==>  theta = 0.6, 
zipf10k_07  ==>  theta = 0.7, etc. 

Figure 7c: 

"GR-zip-b3-theta.txt" contains the lines of BLik_Greedy.txt where inputFile={zipf10k_05,zipf10k_06,zipf10k_07,zipf10k_05,zipf10k_09}, b_param=3, after running b-lik_varying_theta.pl
"SG-zip-b3-theta.txt" contains the lines of BLik_SortGreedy.txt where inputFile={zipf10k_05,zipf10k_06,zipf10k_07,zipf10k_05,zipf10k_09}, b_param=3, after running b-lik_varying_theta.pl
"HG-zip-b3-theta.txt" contains the lines of BLik_Hungarian.txt where inputFile={zipf10k_05,zipf10k_06,zipf10k_07,zipf10k_05,zipf10k_09}, b_param=3, after running b-lik_varying_theta.pl
"Burel-zip-b3-theta.txt" contains the output of running Burel with the {zipf10k_05,zipf10k_06,zipf10k_07,zipf10k_05,zipf10k_09} datasets, for b_param=3 after running burel_varying_theta.pl

NOTE: FOR FIGURES 8a, 9a YOU WILL NEED TO MANUALLY ADD AN EXTRA COLUMN IN THE "q-PB-10k-epsilon.txt" and ""q-PB-coil-epsilon.txt"" FILES. THE VALUES OF COLUMN WILL CORRESPOMD TO THE EPSILON PARAMETER THAT CAN BE FOUND IN THE ANONFILE NAME. For example: 
test10k_0_th4_syn.anon  ==>  epsilon = 0, 
test10k_20_th4_syn.anon  ==>  epsilon = 20,
test10k_1.386_th4_syn.anon  ==>  epsilon = 1.386,  etc. 

Figure 8a: 

"q-GR-10k-b.txt" contains the lines of BLik_Greedy_RangeQueryError.txt where inputFile=census10k, b_param={1,2,3,4,5}, lambda=3, selectivity=0.1 after running b-lik_varying_b_rq.pl
"q-SG-10k-b.txt" contains contains the lines of BLik_SortGreedy_RangeQueryError.txt where inputFile=census10k, b_param={1,2,3,4,5}, lambda=3, selectivity=0.1 after running b-lik_varying_b_rq.pl
"q-HG-10k-b.txt" contains contains the lines of BLik_Hungarian_RangeQueryError.txt where inputFile=census10k, b_param={1,2,3,4,5}, lambda=3, selectivity=0.1 after running b-lik_varying_b_rq.pl
"q-Burel-10k-b.txt" contains contains lines of BUREL_RangeQueryError.txt where inputFile=census10k, b_param={1,2,3,4,5}, lambda=3, selectivity=0.1 after running burel_varying_b_rq-pq.pl (found in the "competitors/BUREL" folder)

"q-PB-10k-epsilon.txt" contains the output of running PrivBayes with inputFile=census10k, epsilon={0.693,1.0986,1.386,1.61,1.79}, lambda=3, selectivity=0.1 by compiling and executing PrivBaysQueries.java

Figure 8b: 

"q-GR-10kb3-lamda.txt" contains the lines of BLik_Greedy_RangeQueryError.txt where inputFile=census10k, b_param=3, lambda={1,2,3,4,5,6}, selectivity=0.1 after running b-lik_varying_b_rq.pl
"q-SG-10kb3-lamda.txt" contains the lines of BLik_SortGreedy_RangeQueryError.txt where inputFile=census10k, b_param=3, lambda={1,2,3,4,5,6}, selectivity=0.1 after running b-lik_varying_b_rq.pl
"q-HG-10kb3-lamda.txt" contains the lines of BLik_Hungarian_RangeQueryError.txt where inputFile=census10k, b_param=3, lambda={1,2,3,4,5,6}, selectivity=0.1 after running b-lik_varying_b_rq.pl
"q-Burel-10kb3-lamda.txt" contains lines of BUREL_RangeQueryError.txt where inputFile=census10k, b_param=3, lambda={1,2,3,4,5,6}, selectivity=0.1 after running burel_varying_b_rq-pq.pl (found in the "competitors/BUREL" folder)
"q-PB-10kb3-lamda.txt" contains the the lines of PrivBayes_QueryError.txt (under Range Queries, lambda={1,2,3,4,5,6}, selectivity=0.1) from running PrivBayesQueries.java with inputFile=census10k, and using as anonfile the outputs of running PrivBayes with inputFile=census10k, epsilon=1.386 -- this anonfile is also available at "competitors/privBayes_QueryErr_and_NB_attack/anonfiles/test10k_1.386_th4_syn.anon"

Figure 8c: 

"q-GR-10kb3-sel.txt" contains the lines of BLik_Greedy_RangeQueryError.txt where inputFile=census10k, b_param=3, lambda=3, selectivity={0.05,0.1,0.15,0.2,0.25} after running b-lik_varying_b_rq.pl
"q-SG-10kb3-sel.txt" contains the lines of BLik_SortGreedy_RangeQueryError.txt where inputFile=census10k, b_param=3, lambda=3, selectivity={0.05,0.1,0.15,0.2,0.25} after running b-lik_varying_b_rq.pl
"q-HG-10kb3-sel.txt" contains the lines of BLik_Hungarian_RangeQueryError.txt where inputFile=census10k, b_param=3, lambda=3, selectivity={0.05,0.1,0.15,0.2,0.25} after running b-lik_varying_b_rq.pl
"q-Burel-10kb3-sel.txt" contains lines of BUREL_RangeQueryError.txt where inputFile=census10k, b_param=3, lambda=3, selectivity={0.05,0.1,0.15,0.2,0.25}, after running burel_varying_b_rq-pq.pl  (found in the "competitors/BUREL" folder)

"q-PB-10k-sel.txt" contains the the lines of PrivBayes_QueryError.txt (under Range Queries, lambda=3, selectivity={0.05,0.1,0.15,0.2,0.25}) from running PrivBayesQueries.java with inputFile=census10k, and using as anonfile the outputs of running PrivBayes with inputFile=census10k, epsilon=1.386 -- this anonfile is also available at "competitors/privBayes_QueryErr_and_NB_attack/anonfiles/test10k_1.386_th4_syn.anon"

Figures 9a-9c are produced in the same way as Figures 8a-8c but using the COIL2000 dataset instead of the census10k dataset.


VERY IMPORTANT NOTE - FOR FIGURES 10a,10b10c,11a,11b WE MANUALLY ADDED AN EXTRA COLUMN IN THE END OF THE DATAFILES THAT CONTAINS THE VALUES OF EPSILON THAT ARE EQUIVALENT TO THE CORRESPONDING VALUES OF B_PARAM. As proven in the paper, the equivalences are the following:
b_param  epsilon
0.0 	 0.001 
0.01 	 0.00995
0.05 	 0.04879
0.1 	 0.095
1.0 	 0.693
2.0 	 1.0986
3.0 	 1.386
4.0 	 1.61
5.0 	 1.79
10.0	 2.4
15.0 	 2.77
20.0 	 3.04
50.0 	 3.93
150.0 	 5.01

Please append the epsilon column as the last column in the files "q-SG-10k-b.txt", "q-GR-10k-b.txt", "q-HG-10k-b.txt"

For the file "pq-PB-10k-epsilon.txt" of Figure 10c, the value of epsilon is in the anonfile name (2nd column). Please add these numerical values as a last (8th) column. For example:
anonfile="test10k_1.386_th4_syn.anon"  =>  epsilon=1.386

Figure 10a: 

"q-GR-10k-b.txt" contains the lines of BLik_Greedy_RangeQueryError.txt where inputFile=census10k, b_param={0,0.01,0.05,0.1,1,5,15,20,50}, lambda=3, selectivity=0.1, after running b-lik_varying_b_rq-pq-pb.pl
"q-SG-10k-b.txt" contains the lines of BLik_SortGreedy_RangeQueryError.txt where inputFile=census10k, b_param={0,0.01,0.05,0.1,1,5,15,20,50}, lambda=3, selectivity=0.1, after running b-lik_varying_b_rq-pq-pb.pl
"q-HG-10k-b.txt" contains the lines of BLik_Hungarian_RangeQueryError.txt where inputFile=census10k, b_param={0,0.01,0.05,0.1,1,5,15,20,50}, lambda=3, selectivity=0.1, after running b-lik_varying_b_rq-pq-pb.pl
"q-Burel-10k-b.txt" contains lines of BUREL_RangeQueryError.txt where inputFile=census10k, b_param={0,0.01,0.05,0.1,1,5,15,20,50}, lambda=3, selectivity=0.1, after running burel_varying_b_rq-pq.pl  (found in the "competitors/BUREL" folder)
"q-PB-10k-epsilon.txt" contains the the lines of PrivBayes_QueryError.txt (under Range Queries, lambda=3, selectivity=0.1) from running PrivBayesQueries.java with inputFile=census10k, and using as anonfile the outputs of running PrivBayes with inputFile=census10k, epsilon={0,0.00995,0.04879,0.095,0.693,1.0986,1.386,
1.61,1.79,2.4,2.77,3.04,3.93,5.01,10,20,25,50,150} -- these anonfiles are also available in the "competitors/privBayes_QueryErr_and_NB_attack/anonfiles/" folder.

Figure 10b is produced in the same way as Figure 10a but using the COIL2000 dataset instead of the census10k dataset.

Figure 10c: 

"pq-GR-10k-b.txt" contains the lines of BLik_Greedy_PrefixQueryError.txt where inputFile=census10k, b_param={0,0.01,0.05,0.1,1,5,15,20,50}, lambda=3, selectivity=0.1, after running b-lik_varying_b_rq-pq-pb.pl
"pq-SG-10k-b.txt" contains the lines of BLik_SortGreedy_PrefixQueryError.txt where inputFile=census10k, b_param={0,0.01,0.05,0.1,1,5,15,20,50}, lambda=3, selectivity=0.1, after running b-lik_varying_b_rq-pq-pb.pl 
"pq-HG-10k-b.txt" contains the lines of BLik_Hungarian_PrefixQueryError.txt where inputFile=census10k, b_param={0,0.01,0.05,0.1,1,5,15,20,50}, lambda=3, selectivity=0.1, after running b-lik_varying_b_rq-pq-pb.pl
"pq-Burel-10k-b.txt" contains the lines of BUREL_PrefixQueryError.txt where inputFile=census10k, b_param={0,0.01,0.05,0.1,1,5,15,20,50}, lambda=3, selectivity=0.1, after running burel_varying_b_rq-pq.pl (found in the "competitors/BUREL" folder)
"pq-PB-10k-epsilon.txt" contains the lines of PrivBayes_QueryError.txt (under Prefix Queries, lambda=3, selectivity=0.1) from running PrivBayesQueries.java with origfile=census10k, and using as anonfile the outputs of running PrivBayes with inputFile=census10k, epsilon={0,0.00995,0.04879,0.095,0.693,1.0986,1.386, 1.61,1.79,2.4,2.77,3.04,3.93,5.01,10,20,25,50,150} -- these anonfiles are also available in the "competitors/privBayes_QueryErr_and_NB_attack/anonfiles/" folder.

Figure 11a and Figure 11b: 
 
"GR_NaiveBayesAttack-b.txt" contains the lines of BLik_Greedy_NaiveBayesAttack.txt where inputFile=census10k, b_param={0,0.01,0.05,0.1,1,5,15,20,50} after running b-lik_varying_b_rq-pq-pb.pl
"SG_NaiveBayesAttack-b.txt" contains the lines of BLik_SortGreedy_NaiveBayesAttack.txt where inputFile=census10k, b_param={0,0.01,0.05,0.1,1,5,15,20,50} after running b-lik_varying_b_rq-pq-pb.pl
"HG_NaiveBayesAttack-b.txt" contains the lines of BLik_Hungarian_NaiveBayesAttack.txt where inputFile=census10k, b_param={0,0.01,0.05,0.1,1,5,15,20,50} after running b-lik_varying_b_rq-pq-pb.pl
"Burel_NaiveBayesAttack-b.txt" contains the lines of BUREL_NaiveBayesAttack where inputFile=census10k, b_param={0,0.01,0.05,0.1,1,5,15,20,50}, after running burel_varying_b_rq-pq.pl
"PrivB_NaiveBayesAttack-epsilon.txt" contains the lines of PrivBayesNaiveBayesAttack.java from running PrivBayesNaiveBayesAttack.java with delta=0.0495, origfile=census10k, and using as anonfile the outputs of running PrivBayes with inputFile=census10k, epsilon={0,0.00995,0.04879,0.095,0.693,1.0986,1.386, 1.61,1.79,2.4,2.77,3.04,3.93,5.01,10,20,25,50,150} -- these anonfiles are also available in the "competitors/privBayes_QueryErr_and_NB_attack/anonfiles/" folder.
"PrivB_NaiveBayesAttack-LC-epsilon.txt" contains the lines of PrivBayesNaiveBayesAttack.java from running PrivBayesNaiveBayesAttack.java with delta=0.1, origfile=census10k, and using as anonfile the outputs of running PrivBayes with inputFile=census10k, epsilon={0,0.00995,0.04879,0.095,0.693,1.0986,1.386, 1.61,1.79,2.4,2.77,3.04,3.93,5.01,10,20,25,50,150} -- these anonfiles are also available in the "competitors/privBayes_QueryErr_and_NB_attack/anonfiles/" folder.

NOTE: FOR FIGURES 11c YOU WILL NEED TO MANUALLY ADD A 6th (last) COLUMN IN THE FOLLOWING FILE. THE VALUES OF THIS COLUMN WILL CORRESPOMD TO THE TESTED "DELTA" PARAMETER.
delta={0.0,0.0001,0.001,0.01,0.02,0.03,0.045,0.046,0.047,0.048,0.049,0.0495,0.05,0.07,0.09,0.1,0.27,081,1.0}
 
Figure 11c: 

"PrivB_NaiveBayesAttack-delta.txt" contains the lines of PrivBayesNaiveBayesAttack.java (where column 4 should be the accuracy_rate and column 5 should be the Laplace_accuracy_rate) from running PrivBayesNaiveBayesAttack.java with delta={0.0,0.0001,0.001,0.01,0.02,0.03,0.045,0.046,0.047,0.048,0.049,0.0495,0.05,0.07,0.09,0.1,0.27,081,1.0}, origfile=census10k, and using as anonfile the outputs of running PrivBayes with inputFile=census10k, epsilon=1.386 -- this anonfile is also available at "competitors/privBayes_QueryErr_and_NB_attack/anonfiles/test10k_1.386_th4_syn.anon"




