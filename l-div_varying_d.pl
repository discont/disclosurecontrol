#!/usr/bin/perl

use Cwd;

my $Pcommand= "java -Xss10M -Xmx30G -cp .:bin Hungarian_LDiversity";
my $Pcommand1= "java -Xss10M -Xmx30G -cp .:bin SortGreedy_LDiversity";
my $Pcommand2 = "java -Xss10M -Xmx30G -cp .:bin Greedy_LDiversity";

my @l = (10);
my @input = ("census10k","uniform10k","zipf10k_05");
my @tuples = (10000,10000,10000);
my @d = (2,3,4,5,6,7,8);


for $i (0 .. $#tuples) {
	for $j (0 .. $#l){
		for $k (0 .. $#d){
			system("$Pcommand $input[$i] $tuples[$i] $d[$k] 7 $l[$j] $tuples[$i] 0 1 false");
                        system("$Pcommand1 $input[$i] $tuples[$i] $d[$k] 7 $l[$j] $tuples[$i] 0 1 false");
                        system("$Pcommand2 $input[$i] $tuples[$i] $d[$k] 7 $l[$j] $tuples[$i] 0 1 false");
                        system("$Pcommand $input[$i] $tuples[$i] $d[$k] 7 $l[$j] $tuples[$i] 0 0 false");
			system("$Pcommand1 $input[$i] $tuples[$i] $d[$k] 7 $l[$j] $tuples[$i] 0 0 false");
			system("$Pcommand2 $input[$i] $tuples[$i] $d[$k] 7 $l[$j] $tuples[$i] 0 0 false");
			
		}
	}
}


exit;


