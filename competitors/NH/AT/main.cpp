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

// return the GCP score for tuple y
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

void partition_and_generalize(int s, int e) {        
    int m = range[d-1]+1;    
    for (int a = 0;a < m;a++) {
        bucket_size[a] = 0;
    }
               
    for (int a = s;a <= e;a++) {
        int t = data[a][d-1] - minv[d-1];
        bucket[t][bucket_size[t]] = a;
        bucket_size[t]++;
    }
    bool used[m];
    
    int remaining = n;
    while (remaining >= 2*l) {        
        // find an arbitrary tuples in each of the largest bucket to form a group of l
        num_partition++;
        for (int i = 0;i < m;i++) {
            used[i] = false;            
        }
        int group[l]; // group members
        for (int i = 0;i < l;i++) {
            int max = -1;
            for (int j = 0;j < m;j++) {
                if (!used[j]) {
                    if (max == -1 || bucket_size[j] > bucket_size[max]) {
                        max = j;
                    }
                }
            }
            if (max == -1) {
                cout << "Error 1\n";
            }
            int r = rand() % bucket_size[max];
            group[i] = bucket[max][r];
            int temp = bucket[max][r];
            bucket[max][r] = bucket[max][bucket_size[max]-1];
            bucket[max][bucket_size[max]-1] = temp;
            bucket_size[max]--;            
        }
        // generlize the group
        for (int i = 0;i < l;i++) {            
            for (int j = 0;j < l;j++){
                ref[group[i]][j] = group[j];
            }
            ref_size[group[i]] = l;
        }
        remaining -= l;
    }   
    // put the remaining to form a group
    // each bucket is ensured to have at most 1 tuple
    num_partition++;
    int lastgroup[m];
    int last_size = 0;
    for (int i = 0;i < m;i++) {
        if (bucket_size[i] > 0) {
            if (bucket_size[i] != 1) {
                cout << "Error 2\n";
            }
            lastgroup[last_size] = bucket[i][0];
            last_size++;
        }
    }
    
    for (int i = 0;i < last_size;i++) {            
        for (int j = 0;j < last_size;j++){
            ref[lastgroup[i]][j] = lastgroup[j];
        }
        ref_size[lastgroup[i]] = last_size;
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
    // int ccc = 0;
    for (int i = 0;i <  n;i++) {
        if (GCP(i) < 0) {
            // ccc++;
            // cout << i << " " << GCP(i) << endl;
            // system("pause");
        }
        sum += GCP(i);
    }
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
    
    // partition and generalize the data
    partition_and_generalize(0, n-1);
    // end

    stop_timer();
    stop_global_timer();

    analysis();
    cout << "Number of partitions : " << num_partition << endl;
    cout << "Average size of partition : " << n*1.0 / num_partition << endl;
    return EXIT_SUCCESS;
}
