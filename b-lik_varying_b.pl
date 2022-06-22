#!/usr/bin/perl

use Cwd;

my $Pcommand= "java -Xss10M -Xmx30G -cp .:bin Hungarian_BLikeness";
my $Pcommand1= "java-Xss10M -Xmx30G -cp .:bin SortGreedy_BLikeness";
my $Pcommand2 = "java -Xss10M -Xmx30G -cp .:bin Greedy_BLikeness";

my @l = (1, 2, 3, 4 , 5);
my @input = ("census10k","uniform10k","zipf10k_05");
my @tuples = (10000,10000,10000);
my @d = (8);


for $i (0 .. $#tuples) {
	for $j (0 .. $#l){
		for $k (0 .. $#d){
			system("$Pcommand $input[$i] $tuples[$i] 7 $l[$j] $tuples[$i] 2 true false false");
                        system("$Pcommand1 $input[$i] $tuples[$i] 7 $l[$j] $tuples[$i] 2 true false false");
                        system("$Pcommand2 $input[$i] $tuples[$i] 7 $l[$j] $tuples[$i] 2 true false false");
			
		}
	}
}


exit;

