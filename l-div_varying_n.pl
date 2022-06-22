#!/usr/bin/perl

use Cwd;

my $Pcommand= "java -Xss10M -Xmx32G -cp .:bin Hungarian_LDiversity";
my $Pcommand1= "java -Xss10M -Xmx32G -cp .:bin SortGreedy_LDiversity";
my $Pcommand2 = "java -Xss10M -Xmx32G -cp .:bin Greedy_LDiversity";

my @l = (10);
my @input = ("census1k","census10k","census100k","census500k");
my @tuples = (1000,10000,100000,500000);
my @d = (8);



for $i (0 .. $#tuples) {
	for $j (0 .. $#l){
		for $k (0 .. $#d){
			system("$Pcommand $input[$i] $tuples[$i] $d[$k] 7 $l[$j] $tuples[$i] 0 1 false");
                        system("$Pcommand1 $input[$i] $tuples[$i] $d[$k] 7 $l[$j] $tuples[$i] 0 1 false");
                        system("$Pcommand2 $input[$i] $tuples[$i] $d[$k] 7 $l[$j] $tuples[$i] 0 1 false");
                        system("$Pcommand $input[$i] $tuples[$i] $d[$k] 7 $l[$j] 1000 0 1 false");
			system("$Pcommand1 $input[$i] $tuples[$i] $d[$k] 7 $l[$j] 1000 0 1 false");
			system("$Pcommand2 $input[$i] $tuples[$i] $d[$k] 7 $l[$j] 1000 0 1 false");
			
		}
	}
}


exit;


