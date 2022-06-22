#!/usr/bin/perl

use Cwd;

my $Pcommand= "java -Xss10M -Xmx30G -cp .:bin Hungarian_LDiversity";
my $Pcommand1= "java -Xss10M -Xmx30G -cp .:bin SortGreedy_LDiversity";
my $Pcommand2 = "java -Xss10M -Xmx30G -cp .:bin Greedy_LDiversity";

my @l = (10);
my @input = ("census100k");
my @tuples = (100000);
my @d = (8);
my @p = (100,500,1000,10000,50000);



for $i (0 .. $#tuples) {
	for $j (0 .. $#l){
		for $k (0 .. $#d){
			for $m (0 .. $#p){
			system("$Pcommand $input[$i] $tuples[$i] $d[$k] 7 $l[$j] $p[$m] 0 1 false");
                        system("$Pcommand1 $input[$i] $tuples[$i] $d[$k] 7 $l[$j] $p[$m] 0 1 false");
                        system("$Pcommand2 $input[$i] $tuples[$i] $d[$k] 7 $l[$j] $p[$m] 0 1 false");
                        system("$Pcommand $input[$i] $tuples[$i] $d[$k] 7 $l[$j]  $p[$m] 0 0 false");
			system("$Pcommand1 $input[$i] $tuples[$i] $d[$k] 7 $l[$j]  $p[$m] 0 0 false");
			system("$Pcommand2 $input[$i] $tuples[$i] $d[$k] 7 $l[$j]  $p[$m] 0 0 false");
			}
			
		}
	}
}


exit;


