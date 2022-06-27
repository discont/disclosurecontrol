#include <cstdlib>
#include <iostream>
#include <fstream>
#include <math.h>

#include "hilbert.h"

/* Please change the following values accordingly for different dataset */

#define DIM 16 // maximum number of dimension of each record
#define MAXN 110000 // maximum number of tuples
#define MAXL 20 // maximum number of k
#define SASIZE 100 // size of domain of sensitive attribute
/* - End - */

/**
Note: bitmask_t has 64 bits
if (ORDER * num_of_dimensions_of_QID) > 64, hilbert value computation will fail.
**/
#define ORDER 6 // input to hilbert curve

using namespace std;

int n, d, l; // number of tuples, number of dimensions, input to k-anonymity
int mode; // 1 = homogeneous, 0 = non-homogeneous
int dq; // number of dimensions of QID
int minv[DIM], maxv[DIM], range[DIM]; // minimum value, maximum value, range of each dimension
int data[MAXN][DIM];
int mbr[DIM][2]; // the MBR of a generalized range, mbr[i][0] -> lower bound, mbr[i][1] -> upper bound
bitmask_t hilbert_value[MAXN];

// for holding buckets
int bucket[SASIZE][MAXN];
int bucket_size[SASIZE];
int bucket_index[SASIZE];

// for holding the generalized value of each anonymized tuple
int ref[MAXN][SASIZE]; // used to point to k different QID 
int ref_size[MAXN];
int ref_other[MAXN]; // used to point to other attribute

int partition_count = 0;

/***** to record the execution time of the program *****/

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

// sort data by hilbert value
void quickSort(int left, int right) { 
    int i = left, j = right;
    bitmask_t temp1;
    int temp2[DIM];
    bitmask_t pivot = hilbert_value[(left + right) / 2];
    
    while (i <= j) {
        while (hilbert_value[i] < pivot)
            i++;
        while (hilbert_value[j] > pivot)
            j--;
        if (i <= j) {
            temp1 = hilbert_value[i];
            hilbert_value[i] = hilbert_value[j];
            hilbert_value[j] = temp1;
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
    if (i < right)
        quickSort(i, right);
}

// initiate an MBR by generalizing tuple x to tuple y
void init_mbr(int x, int y) {
    for (int i = x;i <= y;i++) {        
        for (int j = 0;j < dq;j++) {
            if (i == x) {
                mbr[j][0] = mbr[j][1] = data[x][j];
            } else {
                if (mbr[j][0] > data[i][j]) {
                    mbr[j][0] = data[i][j];
                }
                if (mbr[j][1] < data[i][j]) {
                    mbr[j][1] = data[i][j];
                }
            }
        }
    }
}

// extend the MBR by including tuple x
void extend_mbr(int x) {
    for (int j = 0;j < dq;j++) {
        if (mbr[j][0] > data[x][j]) {
            mbr[j][0] = data[x][j];
        }
        if (mbr[j][1] < data[x][j]) {
            mbr[j][1] = data[x][j];
        }
    }    
}

// return the GCP score for tuple i
double GCP(int y) {      
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
    if (score > dq) { // error checking
        cout << score << " " << y << endl;
        system("pause");
    }
    return score;
}

// a sufficient condition for l-diverity (see [6, 26])
bool EG() {
    int m = range[d-1]+1;
    int max = 0;
    int total = 0;
    for (int i = 0;i < m;i++) {
        int size = bucket_size[i] - bucket_index[i];
        total += size;
        if (size > max) {
            max = size;
        }
    }    
    return max == 0 || (total / max >= l);
}

// partitioning based on 1D hilbert values
// and generalize the data according to the partitioning result
void partition_and_generalize() {
    // initialization - count sensitive values, put them in buckets
    int m = range[d-1] + 1;
    for (int i = 0;i < m;i++) {
        bucket_size[i] = 0;
        bucket_index[i] = 0;
    }    
    for (int i = 0;i < n;i++) {
        int x = data[i][d-1] - minv[d-1];        
        bucket[x][bucket_size[x]] = i;
        bucket_size[x]++;
    }
    
    int group[m];
    int group_size;
    bool used[m];    
    
    int remaining = n;
    while (remaining > 0) {
        
        group_size = 0;
            
        for (int i = 0;i < m;i++) {
            used[i] = false;
        }
        // add the lowest QID to the group
        while (group_size < l || (!EG() && group_size < m)) {            
            int min = -1;
            for (int i = 0;i < m;i++) {
                if (!used[i] && bucket_index[i] != bucket_size[i]) {
                    if (min == -1 || hilbert_value[bucket[i][bucket_index[i]]] < hilbert_value[bucket[min][bucket_index[i]]]) {
                        min = i;
                    }
                }
            }
            if (min == -1) {
                break;                
            }
            // add the minimum QID to the group
            group[group_size] = min;
            bucket_index[min]++;
            used[min] = true;
            group_size++;
        }
        
        if (!EG()) {            
            // fall back
            for (int i = 0;i < group_size;i++) {
                int x = group[i];
                bucket_index[x]--;
                used[x] = false;
            }
            group_size = 0;
            
            // add the biggest bucket to the group            
            while (group_size < l || (!EG())) {                
                int max = -1;
                for (int i = 0;i < m;i++) {
                    if (!used[i] && bucket_index[i] != bucket_size[i]) {
                        if (max == -1 || bucket_size[i] - bucket_index[i] > bucket_size[max] - bucket_index[max]) {
                            max = i;
                        }
                    }
                }               
                
                // add the minimum QID to the group
                group[group_size] = max;
                bucket_index[max]++;
                used[max] = true;
                group_size++;
            }
        }                
        
        partition_count++;
        // generalize group G
        if (mode == 0) { // homogeneous
            for (int i = 0;i < group_size;i++) {
                int t1 = bucket[group[i]][bucket_index[group[i]]-1];                                
                ref_size[t1] = group_size;
                for (int j = 0;j < group_size;j++) {
                    ref[t1][j] = bucket[group[j]][bucket_index[group[j]]-1];
                }
            }
        } else { // mode == 1, non-homogeneous
            for (int i = 0;i < group_size;i++) {
                int t1 = bucket[group[i]][bucket_index[group[i]]-1];                                
                ref_size[t1] = l;
                for (int j = 0;j < l;j++) {
                    int index = i+j;
                    if (index >= group_size) {
                        index -= group_size;
                    }
                    ref[t1][j] = bucket[group[index]][bucket_index[group[index]]-1];
                }
            }
        }
        
        remaining -= group_size;
        
    }
        
    stop_timer();
    stop_global_timer(); 
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
        sum += GCP(i);        
    }
    cout << "GCP: " <<  sum / dq / n << endl;
    
    // GCP - range
    sum = 0;
    for (int i = 0;i <  n;i++) {
        // cout << i << endl;
        for (int j = 0;j < dq;j++) {
            mbr[j][1] = mbr[j][0] = data[ref[i][0]][j];
        }        
        for (int j = 1;j < ref_size[i];j++) {
            extend_mbr(ref[i][j]);
        }
        for (int j = 0;j < dq;j++) {
            sum += (mbr[j][1] - mbr[j][0])*1.0 / range[j];
        }
    }
    cout << "GCP - range representation: " <<  sum / dq / n << endl;    
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

4. mode

0 = homogeneous, 1 = non-homogeneous

5. Number of dimensions are QID

Suppose the input is x
The first x dimensions are treated as QID while others are retained in the generalization

*/

int main(int argc, char *argv[])
{    
    if (argc != 6) {
        cout << "usage: program [input data file name] [file name of description to data file] [l] [homogeneous?] [# dimensions as QID]\n";
        return 0;
    }
    dq = atoi(argv[5]);
    
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
    mode = atoi(argv[4]);
    
    start_global_timer();
    start_timer();
    
    // read the data file
    ifstream infile(argv[1]);
    bitmask_t grid_size = 1 << ORDER;
    int id;
    n = 0;
    while (infile >> id) {
        bitmask_t n_point[d]; // normalized point
        for (int i = 0;i < d;i++) {
            infile >> data[n][i];
            if (i < dq) {
                n_point[i] = (data[n][i] - minv[i]) * grid_size / (range[i] + 1);
                if (n_point[i] >= grid_size) { // check for boundary case
                    n_point[i] = grid_size - 1;
                }
            }            
        }
         
        hilbert_value[n] = hilbert_c2i(dq, ORDER, n_point);         
        n++;
    }
    infile.close();
    
    quickSort(0, n-1); // sort the data according to hilbert value
    
    // partition on 1D and generalize the data
    partition_and_generalize();
    // end
    
    analysis(); 
    cout << "Number of partitions: " << partition_count << endl;
    return EXIT_SUCCESS;
}
