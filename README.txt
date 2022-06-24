---------------------------------------------------------------
Clone the repository using the command 
git clone https://github.com/discont/disclosurecontrol.git

---------------------------------------------------------------
Compile the java source code with your favorite Java IDE or with plain javac and place the .class files in a folder named bin. E.g., in command line use 
>mkdir -p ./bin; javac -d ./bin *.java

---------------------------------------------------------------
For the l-diversity related experiments run:

perl l-div_varying_ell.pl
to reproduce the results of Figure 2

perl l-div_varying_d
to reproduce the results of Figure 3

perl l-div_varying_n
to reproduce the results of Figure 4a and 5a

perl l-div_varying_theta
to reproduce the results of Figure 4c and 5c 

perl l-div_varying_p 
to reproduce the results of Figure 4b and 5b


Results for the l-diversity experiments based on the Hungarian, Greedy and SortGreedy algorithms are appended in the log files LDiv_Hungarian.txt, LDiv_Greedy.txt and LDiv_SortGreedy.txt respectively. The fields recorded there are of the form: 
inputFile tuples dims ell threshold time GCP 
where: 
- inputFile is the name of the input dataset, 
- tuples is the number of tuples it contains, 
- dims is the number of QI dimensions, 
- ell is the l parameter of l-diversity, 
- threshold is the threshold value, 
- time is the end to end execution time, and 
- GCP is the metric used for informacion loss.

---------------------------------------------------------------
For the b-likeness related experiments run:

perl b-lik_varying_b.pl
to reproduce the results of Figure 6

perl b-lik_varying_n.pl
to reproduce the results of Figure 7a and 7b

perl b-lik_varying_theta.pl
to reproduce the results of Figure 7c

perl b-lik_varying_b_rq.pl
to reproduce the results of Figure 8

perl b-lik_varying_b_rq-pq-pb.pl
to reproduce the results of Figure 10 and 11

Results for the beta-likeness experiments based on the Hungarian, Greedy and SortGreedy algorithms are appended in the log files BLik_Hungarian.txt, BLik_Greedy.txt and BLik_SortGreedy.txt respectively. The fields recorded there are of the form: 
inputFile tuples b_param partition_or_bucket_size time GCP
where: 
- inputFile is the name of the input dataset, 
- tuples is the number of tuples it contains, 
- b_param is the beta parameter of  beta-likeness, 
- size is the size of each partition (in number of tuples) 
  if there is partitioning, or the size of each bucket otherwise, 
- time is the end to end execution time, and 
- GCP is the metric used for informacion loss.

Results for the errors of the range queries experiments based on the Hungarian, Greedy and SortGreedy algorithms of beta-likeness are appended in the log files BLik_Hungarian_RangeQueryError.txt, BLik_Greedy_RangeQueryError.txt, and BLik_SortGreedy_RangeQueryError.txt respectively. The fields recorded there are of the form: 
inputFile tuples b_param size lambda selectivity qErr
where: 
- inputFile is the name of the input dataset, 
- tuples is the number of tuples it contains, 
- b_param is the beta parameter of  beta-likeness, 
- size is the size of each bucket (in number of tuples), 
- lambda is the dimensionality of the range queries (i.e., the number of QIs used),
- selectivity is the selectivity of the range queries, and
- qErr is the median relative error of the range queries of a specific lambda and selectivity.

Results for the errors of the prefix queries experiments based on the Hungarian, Greedy and SortGreedy algorithms of beta-likeness are appended in the log files BLik_Hungarian_PrefixQueryError.txt, BLik_Greedy_PrefixQueryError.txt, and BLik_SortGreedy_PrefixQueryError.txt respectively. The fields recorded there are of the form: 
inputFile tuples b_param size qErr
where: 
- inputFile is the name of the input dataset, 
- tuples is the number of tuples it contains, 
- b_param is the beta parameter of  beta-likeness, 
- size is the size of each bucket (in number of tuples), and
- qErr is the median relative error of the prefix queries.

Results for the Naive Bayes attack experiments using the anonymised data of the Hungarian, Greedy and SortGreedy algorithms of beta-likeness are appended in the log files BLik_Hungarian_NaiveBayesAttack.txt, BLik_Greedy_NaiveBayesAttack.txt, and BLik_SortGreedy_NaiveBayesAttack.txt respectively. The fields recorded there are of the form: 
inputFile tuples b_param accuracy_rate Laplace_accuracy_rate
where: 
- inputFile is the name of the input dataset, 
- tuples is the number of tuples it contains, 
- b_param is the beta parameter of  beta-likeness, 
- accuracy_rate is the accuracy rate of the Naive Bayes classifier (without Laplacian corrction), and
- Laplace_accuracy_rate is the accuracy rate of the Naive Bayes classifier that uses Laplacian correction.


---------------------------------------------------------------
To cite this work using BibTeX:

@inproceedings {281328,
title = {One-off Disclosure Control by Heterogeneous Generalization},
authors = {Gkountouna, Olga and Doka, Katerina and Xue, Mingqiang and Cao, Jianneng and Karras, Panagiotis}
booktitle = {31st USENIX Security Symposium (USENIX Security 22)},
year = {2022},
address = {Boston, MA},
url = {https://www.usenix.org/conference/usenixsecurity22/presentation/gkountouna},
publisher = {USENIX Association},
month = aug
}
---------------------------------------------------------------
