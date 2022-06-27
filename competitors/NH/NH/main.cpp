#include <cstdlib>
#include <iostream>
#include <fstream>
#include <math.h>

/* Please change the following values accordingly for different dataset */

#define DIM 16 // maximum number of dimension of each record
#define MAXN 110000 // maximum number of tuples
#define MAXL 20 // maximum number of k
#define SASIZE 100 // size of domain of sensitive attribute
/* - End - */

#define SEED 23806 // paratmeter for random

using namespace std;

int num_partition = 0;

int n, d, l; // number of tuples, number of dimensions, input to k-anonymity
int dq; // number of dimensions of QID
int minv[DIM], maxv[DIM], range[DIM]; // minimum value, maximum value, range of each dimension
int data[MAXN][DIM];
int dimension[DIM];

// for holding the generalized value of each anonymized tuple
int ref[MAXN][MAXL]; // used to point to k different QID 
int ref_size[MAXN];
int ref_other[MAXN]; // used to point to other attribute

// for l-diversity ordering
int bucket[SASIZE][MAXN];
int bucket_size[SASIZE];
int bucket_index[SASIZE];
int last_picked[SASIZE];
int order[SASIZE];
int tuples[MAXN]; 

// for randomization
int mappings[MAXN][SASIZE];

/***** to record the execution of the program *****/

clock_t tstart2;
time_t tstart;
time_t tend;
clock_t tend2;

void start_global_timer() { // record the time for entire program
    tstart = clock();
}

void stop_global_timer() {
    tend = clock();
    cout << "Total time: " << (tend - tstart) * 1.0 / CLOCKS_PER_SEC << "seconds\n";
}

void start_timer() { // record the time for different parts
    tstart2 = clock();
}

void stop_timer() {
    tend2 = clock();
    cout << "time: " << (tend2 - tstart2) * 1.0 / CLOCKS_PER_SEC << "seconds\n";
}

/***** - end - *****/

void dimension_sort() { // sort the dimensions according to their effect to GCP
    for (int i = 0;i < dq;i++) {
        dimension[i] = i;
    }
    for (int i = 0;i < dq;i++) { // small domain, use bubble sort
        for (int j = 0;j < dq-i-1;j++) { // smaller range, put it the front
            if (range[dimension[j]] > range[dimension[j+1]]) {
                int temp = dimension[j];
                dimension[j] = dimension[j+1];
                dimension[j+1] = temp;
            }
        }
    }
}

// return true if data x > data y
bool compare_data(int x, int y) {
    for (int i = 0;i < dq;i++) {
        if (data[x][dimension[i]] > data[y][dimension[i]]) {
            return true;
        } else if (data[x][dimension[i]] < data[y][dimension[i]]) {
            return false;
        }        
    }    
    return false;
}

// sort data by lexicographical order
void quickSort(int left, int right) {    
    int i = left, j = right;
    int ref = (i + j) / 2;
    for (int i = 0; i < dq;i++) {
        data[n][i] = data[ref][i];        
    }
    int temp2[DIM];
    while (i <= j) {        
        while (compare_data(n, i))
            i++;
        while (compare_data(j, n))
            j--;
        if (i <= j) {
            for (int k = 0;k < d;k++) {
                temp2[k] = data[i][k];
            }

            for (int k = 0;k < d;k++) {
                data[i][k] = data[j][k];
            }           
            
            for (int k = 0;k < d;k++) {
                data[j][k] = temp2[k];
            }
            i++;
            j--;
        }
    };    
    // recursion
    if (left < j)
        quickSort(left, j);
    if (i < right) {
        quickSort(i, right);
    }
}

// return the GCP score for tuple i
double GCP(int y) {    
    if (ref_size[y] == 0) {
        cout << y << endl;
    }
    double score = 0;
    for (int a = 0;a < dq;a++) {
        bool used[ref_size[y]];
        int count = 0;
        for (int i = 0;i < ref_size[y];i++) {
            used[i] = false;
        }    
        for (int i = 0;i < ref_size[y];i++) {
            if (!used[i]) {
                count++;
                for (int j = i + 1;j < ref_size[y];j++) {
                    if (data[ref[y][i]][a] == data[ref[y][j]][a]) {
                        used[j] = true;
                    }
                }
            }
            used[i] = true;
        }
        score += (count - 1) * 1.0 / range[a];
    }
    return score;
}

// return the GCP score for tuple i - RANGE REPRESENTATION
double GCP_range(int y) {
    double score = 0;
    int min;
    int max;
    for (int a = 0;a < dq;a++) {
       int count = 0;
       min = maxv[a];
       max = minv[a];
       for (int i = 0;i < ref_size[y];i++) {
            if (data[ref[y][i]][a] < min) {
                    min = data[ref[y][i]][a];
            }
            if (data[ref[y][i]][a] > max) {
                    max = data[ref[y][i]][a];
            }

        }
        count = max - min;
        //cout << "Count: "  << count ;
        score += count  * 1.0 / range[a];
    }
    //cout << "\n";
    return score;
}
// return the GCP score for tuple i - MIXED REPRESENTATION
double GCP_mixed(int y) {
    double score = 0;
    int min;
    int max;
    int count;
    for (int a = 0;a < dq;a++) {
        count = 0;
        if (a==0 || a==2){
            min = maxv[a];
            max = minv[a];
            for (int i = 0;i < ref_size[y];i++) {
                if (data[ref[y][i]][a] < min) {
                    min = data[ref[y][i]][a];
                }
                if (data[ref[y][i]][a] > max) {
                   max = data[ref[y][i]][a];
                }
            }
            count = max - min + 1;
        }else{
              bool used[ref_size[y]];
              for (int i = 0;i < ref_size[y];i++) {
                  used[i] = false;
              }
              // count number of distinct values
              for (int i = 0;i < ref_size[y];i++) {
                  if (!used[i]) {
                     count++;
                     for (int j = i + 1;j < ref_size[y];j++) {
                         if (data[ref[y][i]][a] == data[ref[y][j]][a]) {
                            used[j] = true;
                         }
                     }
                  }
                  used[i] = true;
              }
        }
        //cout << "Count: "  << count ;
        score += (count-1)  * 1.0 / range[a];
    }
    //cout << " " << score << "\n";
    return score;
}



// a sufficient condition for l-diversity, see [6, 26]
bool EG(int s, int e) {
    int m = range[d-1]+1;
    int counts[m];    
    for (int i = 0;i < m;i++) {
        counts[i] = 0;
    }
    int max = 0;
    for (int i = s;i <= e;i++) {
        int t = data[i][d-1] - minv[d-1];        
        counts[t]++;
        if (counts[t] > max) {
            max = counts[t];
        }        
    }
    return (e-s+1)/max >= l;
}

// partitioning and generalize the data according to the partitioning result
void partition_and_generalize(int s, int e, int dim_ref) {            
    int max = MAXN;
    if (range[dimension[dim_ref]] + 1< MAXN) {
        max = range[dimension[dim_ref]] + 1;
    }
    int group_size[max];
    bool combined_group[max];
    
    // group the data by attribute value
    
    combined_group[0] = false;
    
    int group_count = 0;
    group_size[group_count] = 1;
    for (int i = s+1;i <= e;i++) {     
        if (data[i][dimension[dim_ref]] == data[i-1][dimension[dim_ref]]) {
            group_size[group_count] ++;
        } else {
            group_count++;
            combined_group[group_count] = false;
            group_size[group_count] = 1;
        }
    }
    group_count++;
    
    // see if each group is ok and merge to neighboring group
    int remain = e-s+1;
    for (int i = 0;i < group_count - 1;i++) {
        if (remain - group_size[i] < l) { // we have to group the remaining groups
            if (remain < 2 * l) {
                for (int j = i;j < group_count-1;j++) {
                    group_size[j] = -1;                
                }
                group_size[group_count - 1] = remain;
                combined_group[group_count-1] = true;
                break;            
            } else {
                for (int j = i+1;j < group_count-1;j++) {
                    group_size[j] = -1;
                }
                group_size[group_count - 1] = l;
                combined_group[group_count-1] = true;
                group_size[i] = remain - l;
                break;            
            }
        } else if (group_size[i] < l) {
            if (i != group_count - 1 && group_size[i+1] < l) {
                group_size[i+1] += group_size[i];
                combined_group[i+1] = true;
                group_size[i] = -1;
            } else if (i == 0 || group_size[i-1] == -1) {
                if (group_size[i+1] + group_size[i] >= 2 * l) {                   
                    group_size[i+1] -= l - group_size[i];
                    group_size[i] = l;
                    combined_group[i] = true;
                } else {
                    group_size[i+1] += group_size[i];
                    combined_group[i+1] = true;
                    group_size[i] = -1;
                }
            } else {
                if (combined_group[i-1] || group_size[i-1] + group_size[i] < 2 * l) {
                    group_size[i] += group_size[i-1];
                    combined_group[i] = true;
                    remain += group_size[i-1];
                    group_size[i-1] = -1;    
                } else {
                    remain += l - group_size[i];
                    group_size[i-1] -= l - group_size[i];
                    group_size[i] = l;
                    combined_group[i] = true;
                }
            }
        }
        if (group_size[i] != -1) {
            remain -= group_size[i];
        }
    }            
    
    // merge group based on l-diversity
    int start = s;
    for (int i = 0;i < group_count;) {
        if (group_size[i] != -1) {
            if (!EG(start, start+group_size[i]-1)) { // if the group cannot satisfy l-diversity, merge with others
                int mark = i;
                int end = start+group_size[i]-1;
                // try merge groups to the right
                while (!EG(start, end) && i < group_count - 1) {
                    i++;
                    if (group_size[i] != -1) {
                        end += group_size[i];
                    }
                }                
                if (EG(start, end)) {
                    for (int j = mark;j < i;j++) {
                        group_size[j] = -1;
                    }
                    group_size[i] = end-start+1;
                    combined_group[i] = true;
                } else {
                    // extending to right is useless, try merge groups to the left
                    int k = mark;
                    while (!EG(start, end) && k > 0) {
                        k--;
                        if (group_size[k] != -1) {
                            start -= group_size[k];
                        }
                    }
                    if (EG(start, end)) {                   
                        for (int j = k;j < group_count - 1;j++) {
                            group_size[j] = -1;
                        }
                        group_size[group_count-1] = end-start+1;
                        combined_group[i] = true;
                    } else { // the entire partition does not satisfy l-diversity
                        cout << "Error\n";                        
                        system("pause");
                    }
                }                
            } else {
                start += group_size[i];
                i++;
            }
        } else {
            i++;
        }
    }
    
    start = s;
    for (int i = 0;i < group_count;i++) {
        if (group_size[i] != -1) {
            if (combined_group[i] || group_count == 1 || dim_ref == dq - 1) {                                
                int m = range[d-1]+1;
                
                for (int a = 0;a < m;a++) {
                    bucket_size[a] = 0;
                    bucket_index[a] = 0;
                    last_picked[a] = -l;
                    order[a] = a;
                }
                
                for (int a = 0;a < group_size[i];a++) {
                    int t = data[start+a][d-1] - minv[d-1];
                    bucket[t][bucket_size[t]] = start+a;
                    bucket_size[t]++;
                }
                
                // first find the l largest buckets
                // the first t tuples will be the 1st tuple in each of the first l buckets
                // for the last few tuples, we will also need to pick in this order                
                for (int a = 0;a < l;a++) {
                    for (int b = m-1;b > a;b--) {
                        if (bucket_size[order[b]] > bucket_size[order[b-1]]) {
                            int temp = order[b];
                            order[b] = order[b-1];
                            order[b-1] = temp;
                        }
                    }                    
                }                
                
                for (int a = 0;a < group_size[i];a++) {
                    int max = -1;
                    for (int b = 0;b < m;b++) {
                        if (last_picked[order[b]] + l <= a) { // can be picked
                            if (max == -1 || bucket_size[order[b]] - bucket_index[order[b]] > bucket_size[order[max]] - bucket_index[order[max]])  {
                                max = b;
                            }
                        }
                    }
                    if (max == -1) {
                        cout << "Error\n";
                        system("pause");
                    }
                    tuples[a] = bucket[order[max]][bucket_index[order[max]]];
                    bucket_index[order[max]] ++;
                    last_picked[order[max]] = a;
                }                
                
                bool used[m];
                // order them and generalize them                
                int end = start+group_size[i]-1;
                int offset = 0;
                // break into smaller partitions
                while (offset < group_size[i]) {
                    num_partition++;                 
                    int sa = data[tuples[offset]][d-1];
                    int endset;                    
                    for (endset = offset + l; endset < group_size[i] && data[tuples[endset]][d-1] != sa;endset++); // align the group so that each group start with the same sensitive value => ensure l-diversity in each smaller partition
                    endset--; // senset points to the tuple with the same sa or outside the array, decrement by 1 to find the end point of the group                                        
                    int small_size = endset - offset + 1;
                    for (int j = 0;j < small_size;j++) {
                        int t = tuples[offset+j];
                        for (int a = 0;a < m;a++) {
                            used[a] = false;
                        }
                        for (int a = 0;a < l;a++) {                        
                            int target = a + j;
                            if (target >= small_size) {
                                target -= small_size;
                            }
                            ref[t][a] = tuples[offset+target];
                            int temp = data[ref[t][a]][d-1] - minv[d-1];
                            if (used[temp]) {
                                cout << "Error2\n";
                            } else {
                                used[temp] = true;
                            }
                            mappings[j][a] = target;
                        }
                        ref_size[t] = l;
                    }                                                                                
                    
                    /* start of randomization, same codes from k-anonymity version */
                    
                    int goal = rand() % l;
                                        
                    // generate k assignments                
                    for (int a = 0;a <= goal;a ++) {                          
                        int inverted[small_size];
                        int assignment[small_size];
                        for (int b = 0;b < small_size;b++) { // initital assignment;
                            assignment[b] = b; // assignment[a] = b -> ta' = tb
                            inverted[b] = b; // inverted[a] = b -> tb' = ta
                        }                                                                      
                        
                        // monte carol permutation
                        for (int b = 0;b < small_size;b++) {
                            // find a random entry to swap
                            int target = rand() % (l-a);                        
                            target = mappings[b][target];                            
                            // tb -> ttarget'
                            int holder = assignment[target];
                            inverted[holder] = inverted[b];
                            inverted[b] = target;
                            assignment[target] = b;
                            assignment[inverted[holder]] = holder;                            
                        }
                        
                        int undone[small_size];
                        bool done[small_size];
                        int undone_size = 0;
                        for (int b = 0;b < small_size;b++) {
                            int x = assignment[b];
                            done[x] = false;                            
                            for (int c = 0;!done[x] && c < l - a;c++) {
                                if (mappings[x][c] == b) {
                                    done[x] = true;
                                }
                            }                             
                            if (done[x] == false) {
                                undone[undone_size] = x;
                                undone_size++;
                            }                            
                        }
                                                                    
                        while (undone_size != 0) {                                                
                            bool travelled[small_size];
                            for (int b = 0;b < small_size;b++) {
                                travelled[b] = false;
                            }
                            // pick an edge to start
                            int selected = undone[rand() * undone_size / (RAND_MAX+1)];
                            int edge_num = rand() * (l-a) / (RAND_MAX+1); // self loop allowed
                            int n = mappings[selected][edge_num];
                            travelled[n] = true;
                            int travelling[small_size];
                            travelling[0] = n;
                            int num_visited = 1;
                            
                            while (assignment[n] != selected) {                                                                                           
                                int sp = assignment[n]; // start with the one that picked n before
                                // filter its possible outgoing edge
                                int possible[l];
                                int num_possible = 0;
                                for (int b = 0;b < l-a;b++) {
                                    int x = mappings[sp][b];
                                    if (!travelled[x]) {
                                        possible[num_possible] = x;
                                        num_possible++;
                                    }
                                }                        
                                
                                if (num_possible == 0) { // back track
                                    num_visited--;
                                    n = travelling[num_visited - 1];
                                } else {                            
                                    edge_num = rand() * num_possible / (RAND_MAX+1); // no self loop can be chosen;                
                                    n = possible[edge_num];
                                    travelled[n] = true;
                                    travelling[num_visited] = n;
                                    num_visited++;
                                }
                                
                            }
                            
                            // update assignment
                            
                            for (int b = num_visited - 2;b >= 0;b--) {
                                int sp = assignment[travelling[b]];
                                assignment[travelling[b+1]] = sp;                            
                            }                        
                            assignment[travelling[0]] = selected;                                                                                     
                            
                            // update undone;
                            for (int b = 0;b < num_visited;b++) {
                                for (int c = 0;!done[assignment[travelling[b]]] && c < undone_size;c++) {                                    
                                    if (assignment[travelling[b]] == undone[c]) {
                                        undone_size--;
                                        int temp = undone[c];                                    
                                        undone[c] = undone[undone_size];
                                        undone[undone_size] = temp;
                                        done[assignment[travelling[b]]] = true;
                                        break;
                                    }
                                }
                            }
                        }
                        
                        // filter the match by current assignment
                        for (int b = 0;b < small_size;b++) {
                            int x = assignment[b];
                            bool changed = false;
                            for (int c = 0;!changed && c < l - a;c++) {
                                if (mappings[x][c] == b) {
                                    int temp = mappings[x][c];
                                    mappings[x][c] = mappings[x][l-a-1];
                                    mappings[x][l-a-1] = temp;
                                    changed = true;
                                }
                            }
                            if (!changed) {
                                cout << start << " " << a << endl;
                                cout << "Error " << assignment[b] << " " << b << endl;
                                for (int c = 0;c < l;c++) {
                                    cout << mappings[assignment[b]][c] << " ";
                                }
                                cout << endl;
                                system("pause");
                            }
                        }
                        
                    }            
                    
                    // copy other attributes according to mappings[k-goal-1][]
                    for (int a = 0;a < small_size;a++) {
                        // ref_other[start + mappings[a][k-goal-1]] = start+a;
                        ref_other[tuples[offset+a]] = tuples[offset + mappings[a][l-goal-1]];
                    }
                    
                    offset = endset + 1;
                }
                
                /* end of randomization */
                
                start += group_size[i];
            } else {
                partition_and_generalize(start, start+group_size[i]-1, dim_ref + 1);                
                start += group_size[i];
            }
        } 
    }    
}

void analysis() {
    
    cout << "\nNumber of records: " << n << endl;
    cout << "Number of dimensions: " << d << endl;
    cout << "QID dimensions: " << dq << endl;
    cout << "l: " << l << endl;
    cout << endl;
    
    // GCP    
    double sum = 0;
     for (int i = 0;i <  n;i++) {
        sum += GCP_mixed(i);
    }
    //cout << sum;
    cout << "GCP: " <<  sum / dq / n << endl;
}

/*
Input to the program

1. Input data format

[tuple_id] [value 1] [value 2] ... [value d]

2. Format of description to data file

[number of dimensions, d]
[lowest value to dimension 1] [lowest value to dimension 2] ...  [lowest value to dimension d] 
[highest value to dimension 1] [highest value to dimension 2] ... [highest value to dimension d]

3. l

parameter of l-diversity

4. Number of dimensions are QID

Suppose the input is x
The first x dimensions are treated as QID while others are retained in the generalization

*/

int main(int argc, char *argv[])
{    
//    system("pause");
    srand(SEED);
    if (argc != 5) {
        cout << "usage: program [input data file name] [file name of description to data file] [l] [# dimensions as QID]\n";
        return 0;
    }
    dq = atoi(argv[4]);
    
    // read the description to data
    ifstream description(argv[2]);
    description >> d;
    if (dq > d) {
        description.close();
        cout << "Error: # dimension of data < # dimension of QID";
        return 0;
    }
    for (int i = 0;i < d;i++) {
        description >> minv[i];
    }
    for (int i = 0;i < d;i++) {
        description >> maxv[i];
        range[i] = maxv[i] - minv[i];
    }    
    description.close();
    
    l = atoi(argv[3]);
    
    start_global_timer();
    start_timer();
        
    // read the data file
    ifstream infile(argv[1]);
    int id;
    n = 0;
    while (infile >> id) {
        for (int i = 0;i < d;i++) {
            infile >> data[n][i];
        }
        n++;
    }
    infile.close();        
    
    dimension_sort();        
    
    quickSort(0, n-1);        
     
    partition_and_generalize(0, n-1, 0);
    // end

    stop_timer();
    stop_global_timer();

    analysis();
    cout << "Number of partitions : " << num_partition << endl;
    cout << "Average size of partition : " << n*1.0 / num_partition << endl;
//     cout << big_temp2 << endl;
//     cout << n << " " << big_temp << endl;
//    system("pause"); 
    return EXIT_SUCCESS;
}
