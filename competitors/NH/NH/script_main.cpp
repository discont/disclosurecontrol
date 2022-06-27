#include <cstdlib>
#include <iostream>
#include <sstream>
#include <string>

using namespace std;

int main(int argc, char *argv[])
{   
    for (int l = 2;l <= 10;l++) {        
        for (int d = 7;d < 8;d++) {
                stringstream ss (stringstream::in | stringstream::out);
                ss << "./ldiv ";
		ss << "../dataset/Occ-10k.txt ";
                ss << "../dataset/dc.txt ";
                ss << l << " ";
                ss << d << " ";
                ss << "> log/census10k-d" << d << "-l" << l << ".txt";
		string command = ss.str();
                cout << command << endl;
                system(command.c_str());
        }        
    }
    for (int l = 2;l <= 10;l++) {
        for (int d = 7;d < 8;d++) {
                stringstream ss (stringstream::in | stringstream::out);
                ss << "./ldiv ";
                ss << "../dataset/uniform10k.txt ";
                ss << "../dataset/dc.txt ";
                ss << l << " ";
                ss << d << " ";
                ss << "> log/uniform10k-d" << d << "-l" << l << ".txt";
                string command = ss.str();
                cout << command << endl;
                system(command.c_str());
        }
    }
    for (int l = 2;l <= 10;l++) {
        for (int d = 7;d < 8;d++) {
                stringstream ss (stringstream::in | stringstream::out);
                ss << "./ldiv ";
                ss << "../dataset/zipf-10k-05 ";
                ss << "../dataset/dc.txt ";
                ss << l << " ";
                ss << d << " ";
                ss << "> log/zipf10k_05-d" << d << "-l" << l << ".txt";
                string command = ss.str();
                cout << command << endl;
                system(command.c_str());
        }
    }
    for (int l = 10;l <= 10;l++) {
        for (int d = 2;d < 8;d++) {
                stringstream ss (stringstream::in | stringstream::out);
                ss << "./ldiv ";
                ss << "../dataset/Occ-10k.txt ";
                ss << "../dataset/dc.txt ";
                ss << l << " ";
                ss << d << " ";
                ss << "> log/census10k-d" << d << "-l" << l << ".txt";
                string command = ss.str();
                cout << command << endl;
                system(command.c_str());
        }
    }
    for (int l = 10;l <= 10;l++) {
        for (int d = 2;d < 8;d++) {
                stringstream ss (stringstream::in | stringstream::out);
                ss << "./ldiv ";
                ss << "../dataset/uniform10k.txt ";
                ss << "../dataset/dc.txt ";
                ss << l << " ";
                ss << d << " ";
                ss << "> log/uniform10k-d" << d << "-l" << l << ".txt";
                string command = ss.str();
                cout << command << endl;
                system(command.c_str());
            }
    }
    for (int l = 10;l <= 10;l++) {
        for (int d = 2;d < 8;d++) {
                stringstream ss (stringstream::in | stringstream::out);
                ss << "./ldiv ";
                ss << "../dataset/zipf-10k-05 ";
                ss << "../dataset/dc.txt ";
                ss << l << " ";
                ss << d << " ";
                ss << "> log/zipf10k_05-d" << d << "-l" << l << ".txt";
                string command = ss.str();
                cout << command << endl;
                system(command.c_str());
        }
    }
    for (int l = 10;l <= 10;l++) {
        for (int d = 7;d < 8;d++) {
                stringstream ss (stringstream::in | stringstream::out);
                ss << "./ldiv ";
                ss << "../dataset/Occ-100k.txt ";
                ss << "../dataset/dc.txt ";
                ss << l << " ";
                ss << d << " ";
                ss << "> log/census100k-d" << d << "-l" << l << ".txt";
                string command = ss.str();
                cout << command << endl;
                system(command.c_str());
        }
    } 
for (int l = 10;l <= 10;l++) {
        for (int d = 7;d < 8;d++) {
                stringstream ss (stringstream::in | stringstream::out);
                ss << "./ldiv ";
                ss << "../dataset/Occ-500k.txt ";
                ss << "../dataset/dc.txt ";
                ss << l << " ";
                ss << d << " ";
                ss << "> log/census100k-d" << d << "-l" << l << ".txt";
                string command = ss.str();
                cout << command << endl;
                system(command.c_str());
        }
    }   
    for (int l = 10;l <= 10;l++) {
        for (int d = 7;d < 8;d++) {
                stringstream ss (stringstream::in | stringstream::out);
                ss << "./ldiv ";
                ss << "../dataset/Occ-1k.txt ";
                ss << "../dataset/dc.txt ";
                ss << l << " ";
                ss << d << " ";
                ss << "> log/census1k-d" << d << "-l" << l << ".txt";
                string command = ss.str();
                cout << command << endl;
                system(command.c_str());
        }
    } 
    for (int l = 10;l <= 10;l++) {
        for (int d = 7;d < 8;d++) {
              for (int theta = 5; theta<=9; theta++){
                stringstream ss (stringstream::in | stringstream::out);
                ss << "./ldiv ";
                ss << "../dataset/zipf-10k-0" << theta << " ";
                //ss << "../dataset/Occ-10k.txt ";
        //        ss << "../dataset/Occ-10k.txt ";// << theta << " ";
                ss << "../dataset/dc.txt ";
                ss << l << " ";
                ss << d << " ";
        //      ss << "> log/NH-10k-d" << d << "-l" << l << ".txt";
                ss << "> log/Zipf-10k-0" << theta << "-d" << d << "-l" << l << ".txt";
        //      ss << "> log/census10k-d" << d << "-l" << l << ".txt";
                string command = ss.str();
                cout << command << endl;
                system(command.c_str());
              }
        }
    }
    //system("pause");
    return EXIT_SUCCESS;
}
