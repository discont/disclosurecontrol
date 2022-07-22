reset
set terminal postscript eps enhanced 24
##set terminal postscript enhanced fontfile 'cmmi10.pfb'
##set terminal epslatex
set size 1.11,1  
##set terminal png transparent nocrop enhanced font arial 8 size 420,320 
set datafile missing "-"

set key top right maxrows 2


#set lmargin at screen 0.11
#set rmargin 1.2
#set tmargin 0.4
##set lmargin 9.2

set pointsize 2.5

#set key right out
#set key bottom right 
#set key top left width -2.2 maxrows 3
set key top left

set ylabel "Information Loss (GCP)"

set xlabel "{/Times-Italic l}"
set xtics 1
set xrange [2:10]
set yrange [0:0.09]

set output "fig2b.eps"

plot "GR-unif-l.txt" index 0 u 4:8 ti "{GR}" w lp pt 8 linecolor 1,\
"GRth-unif-l.txt" index 0 u 4:8 ti "{GR_{/Symbol-Oblique t}}" w lp pt 6 linecolor 1,\
"SG-unif-l.txt" index 0 u 4:8 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"SGth-unif-l.txt" index 0 u 4:8 ti "{SG_{/Symbol-Oblique t}}" w lp pt 9 linecolor rgb "#00AA00",\
"LD-unif-l.txt" index 0 u 4:8 ti "{HG}" w lp pt 3 linecolor 3,\
"LDth-unif-l.txt" index 0 u 4:8 ti "{HG_{/Symbol-Oblique t}}" w lp pt 10 linecolor 3,\
"NH-unif-l.txt" index 0 u 4:8 ti "{NH}" w lp pt 4 linecolor 4

set yrange [0:0.12]
set output "fig2c.eps"

plot "GR-zipf-l.txt" index 0 u 4:8 ti "{GR}" w lp pt 8 linecolor 1,\
"GRth-zipf-l.txt" index 0 u 4:8 ti "{GR_{/Symbol-Oblique t}}" w lp pt 6 linecolor 1,\
"SG-zipf-l.txt" index 0 u 4:8 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"SGth-zipf-l.txt" index 0 u 4:8 ti "{SG_{/Symbol-Oblique t}}" w lp pt 9 linecolor rgb "#00AA00",\
"LD-zipf-l.txt" index 0 u 4:8 ti "{HG}" w lp pt 3 linecolor 3,\
"LDth-zipf-l.txt" index 0 u 4:8 ti "{HG_{/Symbol-Oblique t}}" w lp pt 10 linecolor 3,\
"NH-zipf-l.txt" index 0 u 4:8 ti "{NH}" w lp pt 4 linecolor 4

set yrange [0:0.41]
set output "fig2a.eps"

plot "GR-10k-l.txt" index 0 u 4:8 ti "{GR}" w lp pt 8 linecolor 1,\
"GRth-10k-l.txt" index 0 u 4:8 ti "{GR_{/Symbol-Oblique t}}" w lp pt 6 linecolor 1,\
"SG-10k-l.txt" index 0 u 4:8 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"SGth-10k-l.txt" index 0 u 4:8 ti "{SG_{/Symbol-Oblique t}}" w lp pt 9 linecolor rgb "#00AA00",\
"LD-10k-l.txt" index 0 u 4:8 ti "{HG}" w lp pt 3 linecolor 3,\
"LDth-10k-l.txt" index 0 u 4:8 ti "{HG_{/Symbol-Oblique t}}" w lp pt 10 linecolor 3,\
"NH-10k-l.txt" index 0 u 4:8 ti "{NH}" w lp pt 4 linecolor 4

set xlabel "d (dimensions)"
set xtics 1
set xrange [3:8]
set yrange [0:0.1]

set key bottom right
set output "fig3b.eps"

plot "GR-unif-d.txt" index 0 u 3:8 ti "{GR}" w lp pt 8 linecolor 1,\
"GRth-unif-d.txt" index 0 u 3:8 ti "{GR_{/Symbol-Oblique t}}" w lp pt 6 linecolor 1,\
"SG-unif-d.txt" index 0 u 3:8 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"SGth-unif-d.txt" index 0 u 3:8 ti "{SG_{/Symbol-Oblique t}}" w lp pt 9 linecolor rgb "#00AA00",\
"LD-unif-d.txt" index 0 u 3:8 ti "{HG}" w lp pt 3 linecolor 3,\
"LDth-unif-d.txt" index 0 u 3:8 ti "{HG_{/Symbol-Oblique t}}" w lp pt 10 linecolor 3,\
"NH-unif-d.txt" index 0 u 3:8 ti "{NH}" w lp pt 4 linecolor 4

set key top left
set yrange [0:0.14]
set output "fig3c.eps"

plot "GR-zipf-d.txt" index 0 u 3:8 ti "{GR}" w lp pt 8 linecolor 1,\
"GRth-zipf-d.txt" index 0 u 3:8 ti "{GR_{/Symbol-Oblique t}}" w lp pt 6 linecolor 1,\
"SG-zipf-d.txt" index 0 u 3:8 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"SGth-zipf-d.txt" index 0 u 3:8 ti "{SG_{/Symbol-Oblique t}}" w lp pt 9 linecolor rgb "#00AA00",\
"LD-zipf-d.txt" index 0 u 3:8 ti "{HG}" w lp pt 3 linecolor 3,\
"LDth-zipf-d.txt" index 0 u 3:8 ti "{HG_{/Symbol-Oblique t}}" w lp pt 10 linecolor 3,\
"NH-zipf-d.txt" index 0 u 3:8 ti "{NH}" w lp pt 4 linecolor 4

set key top right
set yrange [0:0.7]
set output "fig3a.eps"

plot "GR-10k-d.txt" index 0 u 3:8 ti "{GR}" w lp pt 8 linecolor 1,\
"GRth-10k-d.txt" index 0 u 3:8 ti "{GR_{/Symbol-Oblique t}}" w lp pt 6 linecolor 1,\
"SG-10k-d.txt" index 0 u 3:8 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"SGth-10k-d.txt" index 0 u 3:8 ti "{SG_{/Symbol-Oblique t}}" w lp pt 9 linecolor rgb "#00AA00",\
"LD-10k-d.txt" index 0 u 3:8 ti "{HG}" w lp pt 3 linecolor 3,\
"LDth-10k-d.txt" index 0 u 3:8 ti "{HG_{/Symbol-Oblique t}}" w lp pt 10 linecolor 3,\
"NH-10k-d.txt" index 0 u 3:8 ti "{NH}" w lp pt 4 linecolor 4

set xlabel "n (x1000 tuples)"
set xtics (1,10,100,500)
set logscale x
set yrange [0:0.5]
set xrange [1:500]
set xlabel "n (x1000 tuples) \ \ p=1000"

set output "fig4a.eps"

plot "GR-n.txt" index 0 u ($2/1000):8 ti "{GR}" w lp pt 8 linecolor 1,\
"p-GR-n.txt" index 0 u ($2/1000):8 ti "{GR_{p}}" w lp pt 6 linecolor 1,\
"SG-n.txt" index 0 u ($2/1000):8 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"p-SG-n.txt" index 0 u ($2/1000):8 ti "{SG_{p}}" w lp pt 9 linecolor rgb "#00AA00",\
"LD-n.txt" index 0 u ($2/1000):10 ti "{HG}" w lp pt 3 linecolor 3,\
"p-HG-n.txt" index 0 u ($2/1000):8 ti "{HG_{p}}" w lp pt 10 linecolor 3,\
"NH-n.txt" index 0 u ($2/1000):8 ti "{NH}" w lp pt 4 linecolor 4

set xlabel "n (x1000 tuples)"
set key top left
set nologscale x

set xlabel "partition size  (for n=100k tuples)"
set logscale x
set xtics (100,500,1000,10000,50000)
set xrange [100:100005]
set yrange [0:0.5]
set key top right maxrows 2

set output "fig4b.eps"

plot "GR-100k-p.txt" index 0 u ($6*10):8 ti "{GR_p}" w lp pt 8 linecolor 1,\
"GRth-100k-p.txt" index 0 u ($6*10):8 ti "{GR_{/Symbol-Oblique t} _p}" w lp pt 6 linecolor 1,\
"SG-100k-p.txt" index 0 u ($6*10):8 ti "{SG_p}" w lp pt 2 linecolor rgb "#00AA00",\
"SGth-100k-p.txt" index 0 u ($6*10):8 ti "{SG_{/Symbol-Oblique t} _p}" w lp pt 9 linecolor rgb "#00AA00",\
"LD-100k-p.txt" index 0 u ($6*10):8 ti "{HG_p}" w lp pt 3 linecolor 3,\
"LDth-100k-p.txt" index 0 u ($6*10):8 ti "{HG_{/Symbol-Oblique t} _p}" w lp pt 10 linecolor 3,\
"NH-p.txt" index 0 u ($6*10):8 ti "{NH}" w lp pt 4 linecolor 4


set nologscale x

set xlabel "{/Symbol-Oblique q} (zipfian data)"
set key top 
set xtics 0.1
set xrange [0.5:0.9]
set yrange [0:0.15]

set output "fig4c.eps"

plot "GR-zipf-theta.txt" index 0 u 9:8 ti "{GR}" w lp pt 8 linecolor 1,\
"GRth-zipf-theta.txt" index 0 u 9:8 ti "{GR_{/Symbol-Oblique t}}" w lp pt 6 linecolor 1,\
"SG-zipf-theta.txt" index 0 u 9:8 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"SGth-zipf-theta.txt" index 0 u 9:8 ti "{SG_{/Symbol-Oblique t}}" w lp pt 9 linecolor rgb "#00AA00",\
"LD-zipf-theta.txt" index 0 u 9:8 ti "{HG}" w lp pt 3 linecolor 3,\
"LDth-zipf-theta.txt" index 0 u 9:8 ti "{HG_{/Symbol-Oblique t}}" w lp pt 10 linecolor 3,\
"NH-zipf-theta.txt" index 0 u 9:8 ti "{NH}" w lp pt 4 linecolor 4


set ylabel "Time (sec)"

set logscale y

set xlabel "{/Symbol-Oblique q} (zipfian data)"
set xtics 0.1
set xrange [0.5:0.9]
set yrange [0.01:2000]
set key top right

set output "fig5c.eps"

plot "GR-zipf-theta.txt" index 0 u 9:($7/1000) ti "{GR}" w lp pt 8 linecolor 1,\
"GRth-zipf-theta.txt" index 0 u 9:($7/1000) ti "{GR_{/Symbol-Oblique t}}" w lp pt 6 linecolor 1,\
"SG-zipf-theta.txt" index 0 u 9:($7/1000) ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"SGth-zipf-theta.txt" index 0 u 9:($7/1000) ti "{SG_{/Symbol-Oblique t}}" w lp pt 9 linecolor rgb "#00AA00",\
"LD-zipf-theta.txt" index 0 u 9:($7/1000) ti "{HG}" w lp pt 3 linecolor 3,\
"LDth-zipf-theta.txt" index 0 u 9:($7/1000) ti "{HG_{/Symbol-Oblique t}}" w lp pt 10 linecolor 3,\
"NH-zipf-theta.txt" index 0 u 9:($7) ti "{NH}" w lp pt 4 linecolor 4

set key top right maxrows 2

set xlabel "n (x1000 tuples) \ \ using p=1000"
set xtics (1,10,100,500)
set xrange [1:500]
set ylabel "Ratio of NH time"
set logscale y
set yrange [0.001:1000000]

set output "fig5a.eps"

plot "GR-n.txt" index 0 u ($2/1000):($7/1000)/$9 ti "{GR}" w lp pt 8 linecolor 1,\
"p-GR-n.txt" index 0 u ($2/1000):((($7*$6)/$2)/1000)/$9 ti "{GR_{p}}" w lp pt 6 linecolor 1,\
"SG-n.txt" index 0 u ($2/1000):($7/1000)/$9 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"p-SG-n.txt" index 0 u ($2/1000):((($7*$6)/$2)/1000)/$9 ti "{SG_{p}}" w lp pt 9 linecolor rgb "#00AA00",\
"LD-n.txt" index 0 u ($2/1000):($7/1000)/$9 ti "{HG}" w lp pt 3 linecolor 3,\
"p-HG-n.txt" index 0 u ($2/1000):((($7*$6)/$2)/1000)/$9 ti "{HG_{p}}" w lp pt 10 linecolor 3,\
"NH-n.txt" index 0 u ($2/1000):($7/$7) ti "{NH}" w lp pt 4 linecolor 4

set key top left maxrows 2
set xlabel "partition size  (for n=100k tuples)"
set logscale x
set logscale y
set ylabel "Ratio of NH time"
#set key top left maxrows 1
set xtics (100,500,1000,10000,50000)
set xrange [100:100005]
set yrange [0.01:10000]

set output "fig5b.eps"

plot "GR-100k-p.txt" index 0 u ($6*10):((($7*$6)/$2)/1000)/0.65 ti "{GR_p}" w lp pt 8 linecolor 1,\
"GRth-100k-p.txt" index 0 u ($6*10):((($7*$6)/$2)/1000)/0.65 ti "{GR_{/Symbol-Oblique t} _p}" w lp pt 6 linecolor 1,\
"SG-100k-p.txt" index 0 u ($6*10):((($7*$6)/$2)/1000)/0.65 ti "{SG_p}" w lp pt 2 linecolor rgb "#00AA00",\
"SGth-100k-p.txt" index 0 u ($6*10):((($7*$6)/$2)/1000)/0.65 ti "{SG_{/Symbol-Oblique t} _p}" w lp pt 9 linecolor rgb "#00AA00",\
"LD-100k-p.txt" index 0 u ($6*10):((($7*$6)/$2)/1000)/0.65 ti "{HG_p}" w lp pt 3 linecolor 3,\
"LDth-100k-p.txt" index 0 u ($6*10):((($7*$6)/$2)/1000)/0.65 ti "{HG_{/Symbol-Oblique t} _p}" w lp pt 10 linecolor 3,\
"NH-p.txt" index 0 u ($6*10):($9/$9) ti "{NH}" w lp pt 4 linecolor 4


set nologscale x
set nologscale y

#============================================================================#

set key top right maxrows 2
set pointsize 2.5

set key top left

set ylabel "Information Loss (GCP)"

set xlabel "{/Symbol-Oblique b}"
set xtics 1
set xrange [0.9:5.1]
set yrange [0.3:0.8]

set output "fig6a.eps"

plot "GR-10k-b.txt" index 0 u 3:6 ti "{GR}" w lp pt 8 linecolor 1,\
"SG-10k-b.txt" index 0 u 3:6 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"HG-10k-b.txt" index 0 u 3:6 ti "{HG}" w lp pt 3 linecolor 3,\
"Burel-10k-b.txt" index 0 u 3:6 ti "{Burel}" w lp pt 4 linecolor 4

set yrange [0:0.8]

set output "fig6b.eps"

plot "GR-uni-b.txt" index 0 u 3:6 ti "{GR}" w lp pt 8 linecolor 1,\
"SG-uni-b.txt" index 0 u 3:6 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"HG-uni-b.txt" index 0 u 3:6 ti "{HG}" w lp pt 3 linecolor 3,\
"Burel-uni-b.txt" index 0 u 3:6 ti "{Burel}" w lp pt 4 linecolor 4

set yrange [0.1:0.5]

set output "fig6c.eps"

plot "GR-zip-b.txt" index 0 u 3:6 ti "{GR}" w lp pt 8 linecolor 1,\
"SG-zip-b.txt" index 0 u 3:6 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"HG-zip-b.txt" index 0 u 3:6 ti "{HG}" w lp pt 3 linecolor 3,\
"Burel-zip-b.txt" index 0 u 3:6 ti "{Burel}" w lp pt 4 linecolor 4


set key top right maxrows 2

set nologscale y

set ylabel "Information Loss (GCP)"
set xlabel "{/Symbol-Oblique q} (zipfian data)"
set xtics 0.1
set xrange [0.5:0.9]
set yrange [0.2:0.4]

set output "fig7c.eps"

plot "GR-zip-b3-theta.txt" index 0 u 7:6 ti "{GR}" w lp pt 8 linecolor 1,\
"SG-zip-b3-theta.txt" index 0 u 7:6 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"HG-zip-b3-theta.txt" index 0 u 7:6 ti "{HG}" w lp pt 3 linecolor 3,\
"Burel-zip-b3-theta.txt" index 0 u 7:6 ti "{Burel}" w lp pt 4 linecolor 4

set logscale x
set nologscale y

set ylabel "Information Loss (GCP)"
set xlabel "n"

set yrange [0.2:0.7]
set xrange [1000:100000]
set xtics (1000,10000,100000)

set output "fig7a.eps"

plot "GR-10k-b3-n.txt" index 0 u ($2/1000):6 ti "{GR}" w lp pt 8 linecolor 1,\
"SG-10k-b3-n.txt" index 0 u ($2/1000):6 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"HG-10k-b3-n.txt" index 0 u ($2/1000):6 ti "{HG}" w lp pt 3 linecolor 3,\
"Burel-10k-b3-n.txt" index 0 u ($2/1000):6 ti "{Burel}" w lp pt 4 linecolor 4


set ylabel "Time (sec)"
set xlabel "n"
set xtics (1000,10000,100000)
set logscale x
set xrange [1000:100000]
set logscale y
set yrange [0.05:100000]

set output "fig7b.eps"

plot "GR-10k-b3-n.txt" index 0 u ($2/1000):($5/1000) ti "{GR}" w lp pt 8 linecolor 1,\
"SG-10k-b3-n.txt" index 0 u ($2/1000):($5/1000) ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"HG-10k-b3-n.txt" index 0 u ($2/1000):($5/1000) ti "{HG}" w lp pt 3 linecolor 3,\
"Burel-10k-b3-n.txt" index 0 u ($2/1000):($5/1000) ti "{Burel}" w lp pt 4 linecolor 4

set nologscale x
set nologscale y

#============================================================================#

set key top left maxrows 3
set pointsize 2.5
set key top right

set ylabel "Accuracy of NB attack"

set xlabel "{/Symbol-Oblique d}"

set logscale x
set nologscale y
set yrange [0:0.11]
set xrange [0.001:1.2]
set xtics ("0" 0.001, ".01" 0.01, ".0495" 0.0495, ".1" 0.1, "1" 1)

set output "fig11c.eps"

plot "PrivB_NaiveBayesAttack-delta.txt" index 0 u 6:4 ti "{NB}" w lp pt 5 linecolor 5,\
"PrivB_NaiveBayesAttack-delta.txt" index 0 u 6:5 ti "{NB with LC}" w lp pt 6 linecolor 7

set ylabel "Accuracy of NB attack"

set xlabel "{/Symbol-Oblique e}   = ln(1+{/Symbol-Oblique b}) "
 
set logscale x
set nologscale y
set yrange [0:0.22]
set xrange [0.001:100.2]
set xtics ("0" 0.001, "0.01" 0.01, "0.1" 0.1, "1" 1, "10" 10, "100" 100)

set output "fig11a.eps"

plot "GR_SG_HG_NaiveBayesAttack-b.txt" index 0 u 6:4 ti "{SG}" w lp pt 6 linecolor rgb "#00AA00",\
"GR_SG_HG_NaiveBayesAttack-b.txt" index 0 u 6:4 ti "{GR}" w lp pt 8 linecolor 1,\
"GR_SG_HG_NaiveBayesAttack-b.txt" index 0 u 6:4 ti "{HG}" w lp pt 2 linecolor 3,\
"PrivB_NaiveBayesAttack-epsilon.txt" index 0 u 3:4 ti "{PrivB}" w lp pt 5 linecolor 5,\
"Burel_NaiveBayesAttack-b.txt" index 0 u 6:4 ti "{Burel}" w lp pt 4 linecolor 4

set ylabel "Accuracy of NB attack"

set xlabel "{/Symbol-Oblique e}   = ln(1+{/Symbol-Oblique b}) "

set logscale x
set nologscale y
set yrange [0:0.22]
set xrange [0.001:100.2]
set xtics ("0" 0.001, "0.01" 0.01, "0.1" 0.1, "1" 1, "10" 10, "100" 100)

set output "fig11b.eps"

plot "GR_SG_HG_NaiveBayesAttack-b.txt" index 0 u 6:5 ti "{SG}" w lp pt 6 linecolor rgb "#00AA00",\
"GR_SG_HG_NaiveBayesAttack-b.txt" index 0 u 6:5 ti "{GR}" w lp pt 8 linecolor 1,\
"GR_SG_HG_NaiveBayesAttack-b.txt" index 0 u 6:5 ti "{HG}" w lp pt 2 linecolor 3,\
"PrivB_NaiveBayesAttack-LC-epsilon.txt" index 0 u 3:5 ti "{PrivB}" w lp pt 5 linecolor 5,\
"Burel_NaiveBayesAttack-b.txt" index 0 u 6:5 ti "{Burel}" w lp pt 4 linecolor 4


set ylabel "Median relative query error"

set xlabel "{/Symbol-Oblique e}   = ln(1+{/Symbol-Oblique b}) "

set logscale x
set nologscale y
set yrange [0:1.22]
set xrange [0.001:100.2]
set xtics ("0" 0.001, "0.01" 0.01, "0.1" 0.1, "1" 1, "10" 10, "100" 100)

set output "fig10a.eps"

plot "q-SG-10k-b.txt" index 0 u 8:7 ti "{SG}" w lp pt 6 linecolor rgb "#00AA00",\
"q-GR-10k-b.txt" index 0 u 8:7 ti "{GR}" w lp pt 8 linecolor 1,\
"q-HG-10k-b.txt" index 0 u 8:7 ti "{HG}" w lp pt 2 linecolor 3,\
"q-PB-10k-epsilon.txt" index 0 u 9:5 ti "{PrivB}" w lp pt 5 linecolor 5,\
"q-Burel-10k-b.txt" index 0 u 6:5 ti "{Burel}" w lp pt 4 linecolor 4


set xtics mirror
set nologscale x
set nologscale y
set yrange [0:1.2]

set xlabel "{/Symbol-Oblique b}"
set x2tics 
set xtics nomirror
set x2range [0.642:1.808]
set x2label '{/Symbol-Oblique e}'
set xtics 1
set xrange [0.9:5.1]
set nologscale x

set output "fig8a.eps"

plot "q-GR-10k-b.txt" index 0 u 3:7 ti "{GR}" w lp pt 8 linecolor 1 axes x1y1,\
"q-SG-10k-b.txt" index 0 u 3:7 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00" axes x1y1,\
"q-HG-10k-b.txt" index 0 u 3:7 ti "{HG}" w lp pt 3 linecolor 3 axes x1y1,\
"q-Burel-10k-b.txt" index 0 u 3:5 ti "{Burel}" w lp pt 4 linecolor 4 axes x1y1,\
"q-PB-10k-epsilon.txt" index 0 u 9:5 ti "{PrivBayes}" w lp pt 5 linecolor 5  axes x2y1


unset x2tics
unset x2label
set xtics nomirror

set key top left maxrows 3

#set xlabel "{/Symbol-Oblique Q}"
set xlabel "Selectivity"
set xtics 0.05
set xrange [0.05:0.25]
set yrange [0.0:1.0]

set output "fig8c.eps"

plot "q-GR-10kb3-sel.txt" index 0 u 6:7 ti "{GR}" w lp pt 8 linecolor 1,\
"q-SG-10kb3-sel.txt" index 0 u 6:7 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"q-HG-10kb3-sel.txt" index 0 u 6:7 ti "{HG}" w lp pt 3 linecolor 3,\
"q-Burel-10kb3-sel.txt" index 0 u 4:5 ti "{Burel}" w lp pt 4 linecolor 4,\
"q-PB-10k-sel.txt" index 0 u 4:5 ti "{PrivBayes}" w lp pt 5 linecolor 5


set xlabel "{/Symbol-Oblique l}"
set xtics 1
set xrange [1:6]

set logscale y
set yrange [0.01:100.0]

set output "fig8b.eps"

plot "q-GR-10kb3-lamda.txt" index 0 u 5:7 ti "{GR}" w lp pt 1 linecolor 1,\
"q-SG-10kb3-lamda.txt" index 0 u 5:7 ti "{SG}" w lp pt 2 linecolor rgb "#00AA00",\
"q-HG-10kb3-lamda.txt" index 0 u 5:7 ti "{HG}" w lp pt 3 linecolor 3,\
"q-Burel-10kb3-lamda.txt" index 0 u 3:5 ti "{Burel}" w lp pt 4 linecolor 4,\
"q-PB-10kb3-lamda.txt" index 0 u 3:5 ti "{PrivBayes}" w lp pt 5 linecolor 5


set nologscale x
set nologscale y

#============================================================================#

set key top left maxrows 3
set pointsize 2.5
set key center right

set ylabel "Median relative query error"

set xlabel "{/Symbol-Oblique e}   = ln(1+{/Symbol-Oblique b}) "

set logscale x
set nologscale y
set yrange [0:1.22]
set xrange [0.001:100.2]
set xtics ("0" 0.001, "0.01" 0.01, "0.1" 0.1, "1" 1, "10" 10, "100" 100)

set output "fig10b.eps"

plot "q-SG-coil-b.txt" index 0 u 8:7 ti "{SG}" w lp pt 6 linecolor rgb "#00AA00",\
"q-GR-coil-b.txt" index 0 u 8:7 ti "{GR}" w lp pt 8 linecolor 1,\
"q-HG-coil-b.txt" index 0 u 8:7 ti "{HG}" w lp pt 2 linecolor 3,\
"q-PB-coil-epsilon.txt" index 0 u 9:5 ti "{PrivB}" w lp pt 5 linecolor 5,\
"q-Burel-coil-b.txt" index 0 u 6:5 ti "{Burel}" w lp pt 12 linecolor 4


set xlabel "{/Symbol-Oblique e}  [ = ln(1+{/Symbol-Oblique b}) ]"

set xtics mirror
set yrange [0:1.2]

set xlabel "{/Symbol-Oblique b}"
set x2tics 
set xtics nomirror
set x2range [0.642:1.808]
set x2label '{/Symbol-Oblique e}'
set xtics 1
set xrange [0.9:5.1]
set nologscale x
set key center right

set output "fig9a.eps"

plot "q-GR-coil-b.txt" index 0 u 3:7 ti "{GR}" w lp pt 8 linecolor 1 axes x1y1,\
"q-SG-coil-b.txt" index 0 u 3:7 ti "{SG}" w lp pt 6 linecolor rgb "#00AA00" axes x1y1,\
"q-HG-coil-b.txt" index 0 u 3:7 ti "{HG}" w lp pt 3 linecolor 3 axes x1y1,\
"q-PB-coil-epsilon.txt" index 0 u 9:5 ti "{PrivBayes}" w lp pt 5 linecolor 5  axes x2y1,\
"q-Burel-coil-b.txt" index 0 u 2:5 ti "{Burel}" w lp pt 12 linecolor 4 axes x1y1

# -------------------------------------------------------------------
unset x2tics
unset x2label
set xtics nomirror

set key center left maxrows 3

#set xlabel "{/Symbol-Oblique Q}"
set xlabel "Selectivity"
set xtics 0.05
set xrange [0.05:0.25]
set yrange [0.0:1.2]

set output "fig9c.eps"

plot "q-GR-coilB3-sel.txt" index 0 u 6:7 ti "{GR}" w lp pt 8 linecolor 1,\
"q-SG-coilB3-sel.txt" index 0 u 6:7 ti "{SG}" w lp pt 6 linecolor rgb "#00AA00",\
"q-HG-coilB3-sel.txt" index 0 u 6:7 ti "{HG}" w lp pt 3 linecolor 3,\
"q-PB-coilB3-sel.txt" index 0 u 4:5 ti "{PrivBayes}" w lp pt 5 linecolor 5,\
"q-Burel-coilB3-sel.txt" index 0 u 4:5 ti "{Burel}" w lp pt 12 linecolor 4


set key top left maxrows 3

set xlabel "{/Symbol-Oblique l}"
set xtics 1
set xrange [1:4]
set logscale y
set yrange [0.01:100.0]

set output "fig9b.eps"

plot "q-GR-coilB3-lamda.txt" index 0 u 5:7 ti "{GR}" w lp pt 8 linecolor 1,\
"q-SG-coilB3-lamda.txt" index 0 u 5:7 ti "{SG}" w lp pt 6 linecolor rgb "#00AA00",\
"q-HG-coilB3-lamda.txt" index 0 u 5:7 ti "{HG}" w lp pt 3 linecolor 3,\
"q-PB-coilB3-lamda.txt" index 0 u 3:5 ti "{PrivBayes}" w lp pt 5 linecolor 5,\
"q-Burel-coilB3-lamda.txt" index 0 u 3:5 ti "{Burel}" w lp pt 12 linecolor 4


set nologscale x
set nologscale y

#============================================================================#

set key top left maxrows 3
set pointsize 2.5

set ylabel "Median relative query error"
set xlabel "{/Symbol-Oblique e}   = ln(1+{/Symbol-Oblique b}) "
set key bottom right
set logscale x
set nologscale y
set yrange [0:1.22]
set xrange [0.001:100.2] #set xrange [0.01:1000.2]
set xtics ("0" 0.001, "0.01" 0.01, "0.1" 0.1, "1" 1, "10" 10, "100" 100)

set output "fig10c.eps"

plot "pq-SG-10k-b.txt" index 0 u 6:5 ti "{SG}" w lp pt 6 linecolor rgb "#00AA00",\
"pq-GR-10k-b.txt" index 0 u 6:5 ti "{GR}" w lp pt 8 linecolor 1,\
"pq-HG-10k-b.txt" index 0 u 6:5 ti "{HG}" w lp pt 2 linecolor 3,\
"pq-PB-10k-epsilon.txt" index 0 u 8:4 ti "{PrivB}" w lp pt 5 linecolor 5,\
"pq-Burel-10k-b.txt" index 0 u 4:3 ti "{Burel}" w lp pt 4 linecolor 4
