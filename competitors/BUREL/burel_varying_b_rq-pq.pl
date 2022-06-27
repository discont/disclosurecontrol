#!/usr/bin/perl

use Cwd;

my $Pcommand= "~/java-8/jdk1.8.0_91/bin/java -Xss10M -Xmx4G -cp .:bin Main";
my @tuples = (10000);#, 100000);
my @input = ("census10k");
my @b = (0, 0.01, 0.05, 0.1, 1, 5, 15, 20, 50);

for $i (0 .. $#tuples) {
		for $k (0 .. $#b){
		system("$Pcommand valid8.txt $b[$k] $tuples[$i] $input[$i] o_b$b[$k]_d8_$input[$i] s_b$b[$k]_d8_$input[$i] knn-hn hn true true");
			}
	}

exit;


