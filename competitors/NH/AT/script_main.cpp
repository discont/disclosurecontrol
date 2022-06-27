#include <cstdlib>
#include <iostream>
#include <sstream>
#include <string>

using namespace std;

int main(int argc, char *argv[])
{   
    for (int l = 2;l < 10;l++) {        
        for (int d = 2;d < 8;d++) {
            int count = 0;
            if (l == 5) {
                count++;
            }             
            if (d == 3) {
                count++;
            }
            if (count > 0) {
                stringstream ss (stringstream::in | stringstream::out);
                ss << "ldiv ";
                ss << "../dataset/Occ-100k.txt ";                
                ss << "../dataset/dc.txt ";
                ss << l << " ";
                ss << d << " ";
                ss << "> log/AT-" << d << "-" << l << ".txt";
                string command = ss.str();
                cout << command << endl;
                system(command.c_str());
            }
        }        
    }
    
    //system("pause");
    return EXIT_SUCCESS;
}
