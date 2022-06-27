Compile the java source code with your favorite Java IDE or with plain javac and place the .class files in a folder named bin. 

E.g., in command line use 
>javac *.java

For the b-likeness related experiments run
perl burel_varying_b.pl
to reproduce the BuReL results of Figure 6
perl burel_varying_n.pl
to reproduce the BuReL results of Figure 7a and 7b
perl burel_varying_theta.pl
to reproduce the BuReL results of Figure 7c
perl burel_varying_b_rq-pq-nb.pl
to reproduce the BuReL results of Figure 8, and 10


Execution time and GCP (denoted as consumed time and average information loss respectively) are printed in standard output. The accuracy of range and prefix queries is recorded in the files BUREL_RangeQueryError.txt and BUREL_PrefixQueryError.txt. 

Results for the errors of the range queries experiments based on BuReL are appended in the log file  BUREL_RangeQueryError.txt. The fields recorded there are of the form: 
inputFile b_param lambda selectivity qErr
where: 
- inputFile is the name of the input dataset, 
- b_param is the beta parameter of  beta-likeness, 
- lambda is the dimensionality of the range queries (i.e., the number of QIs used),
- selectivity is the selectivity of the range queries, and
- qErr is the median relative error of the range queries of a specific lambda and selectivity.

Results for the errors of the prefix queries experiments based on tBuReL are appended in the log file  BUREL_PrefixQueryError.txt. The fields recorded there are of the form: 
inputFile b_param qErr
where: 
- inputFile is the name of the input dataset, 
- b_param is the beta parameter of  beta-likeness, and
- qErr is the median relative error of the prefix queries.

Results for the Naive Bayes attack experiments using the anonymised data of the BuReL are appended in the log files BUREL_NaiveBayesAttack.txt. The fields recorded there are of the form: 
inputFile tuples b_param accuracy_rate Laplace_accuracy_rate
where: 
- inputFile is the name of the input dataset, 
- b_param is the beta parameter of  beta-likeness, 
- accuracy_rate is the accuracy rate of the Naive Bayes classifier (without Laplacian corrction), and
- Laplace_accuracy_rate is the accuracy rate of the Naive Bayes classifier that uses Laplacian correction.
