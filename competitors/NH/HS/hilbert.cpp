#include "hilbert.h"
#include <iostream>
#include <string>
#include <fstream>
#include <cstdlib>
#include <ctime>
#include <cmath>
#include <vector>
#include <iomanip>
#include <algorithm>


using namespace std;

/* implementation of the hilbert functions, obtained from web, forgot where 
some other unused functions are removed */

#define adjust_rotation(rotation,nDims,bits)                            \
do {                                                                    \
      bits &= -bits & nd1Ones;                                          \
      while (bits)                                                      \
        bits >>= 1, ++rotation;                                         \
      if ( ++rotation >= nDims )                                        \
        rotation -= nDims;                                              \
} while (0)

#define ones(T,k) ((((T)2) << (k-1)) - 1)

#define rdbit(w,k) (((w) >> (k)) & 1)
     
#define rotateRight(arg, nRots, nDims)                                  \
((((arg) >> (nRots)) | ((arg) << ((nDims)-(nRots)))) & ones(bitmask_t,nDims))

#define rotateLeft(arg, nRots, nDims)                                   \
((((arg) << (nRots)) | ((arg) >> ((nDims)-(nRots)))) & ones(bitmask_t,nDims))

#define DLOGB_BIT_TRANSPOSE

static bitmask_t
bitTranspose(unsigned nDims, unsigned nBits, bitmask_t inCoords)
#if defined(DLOGB_BIT_TRANSPOSE)
{
  unsigned const nDims1 = nDims-1;
  unsigned inB = nBits;
  unsigned utB;
  bitmask_t inFieldEnds = 1;
  bitmask_t inMask = ones(bitmask_t,inB);
  bitmask_t coords = 0;

  while ((utB = inB / 2))
    {
      unsigned const shiftAmt = nDims1 * utB;
      bitmask_t const utFieldEnds =
	inFieldEnds | (inFieldEnds << (shiftAmt+utB));
      bitmask_t const utMask =
	(utFieldEnds << utB) - utFieldEnds;
      bitmask_t utCoords = 0;
      unsigned d;
      if (inB & 1)
	{
	  bitmask_t const inFieldStarts = inFieldEnds << (inB-1);
	  unsigned oddShift = 2*shiftAmt;
	  for (d = 0; d < nDims; ++d)
	    {
	      bitmask_t in = inCoords & inMask;
	      inCoords >>= inB;
	      coords |= (in & inFieldStarts) <<	oddShift++;
	      in &= ~inFieldStarts;
	      in = (in | (in << shiftAmt)) & utMask;
	      utCoords |= in << (d*utB);
	    }
	}
      else
	{
	  for (d = 0; d < nDims; ++d)
	    {
	      bitmask_t in = inCoords & inMask;
	      inCoords >>= inB;
	      in = (in | (in << shiftAmt)) & utMask;
	      utCoords |= in << (d*utB);
	    }
	}
      inCoords = utCoords;
      inB = utB;
      inFieldEnds = utFieldEnds;
      inMask = utMask;
    }
  coords |= inCoords;
  return coords;
}
#else
{
  bitmask_t coords = 0;
  unsigned d;
  for (d = 0; d < nDims; ++d)
    {
      unsigned b;
      bitmask_t in = inCoords & ones(bitmask_t,nBits);
      bitmask_t out = 0;
      inCoords >>= nBits;
      for (b = nBits; b--;)
	{
	  out <<= nDims;
	  out |= rdbit(in, b);
	}
      coords |= out << d;
    }
  return coords;
}
#endif

int jcbi(double** a, int n, double** v, double eps, int jt){ 
    int i,j,p,q,u,w,t,s,l;
    double fm,cn,sn,omega,x,y,d;
    l=1;
    for (i=0; i<=n-1; i++)
      { v[i][i]=1.0;
        for (j=0; j<=n-1; j++)
          if (i!=j) v[i][j]=0.0;
      }
    while (1==1)
      { fm=0.0;
        for (i=1; i<=n-1; i++)
        for (j=0; j<=i-1; j++)
          { d=fabs(a[i][j]);
            if ((i!=j)&&(d>fm))
              { fm=d; p=i; q=j;}
          }
        if (fm<eps)  return(1);
        if (l>jt)  return(-1);
        l=l+1;
        //u=p*n+q; w=p*n+p; t=q*n+p; s=q*n+q;
        x=-a[p][q]; y=(a[q][q]-a[p][p])/2.0;
        omega=x/sqrt(x*x+y*y);
        if (y<0.0) omega=-omega;
        sn=1.0+sqrt(1.0-omega*omega);
        sn=omega/sqrt(2.0*sn);
        cn=sqrt(1.0-sn*sn);
        fm=a[p][p];
        a[p][p]=fm*cn*cn+a[q][q]*sn*sn+a[p][q]*omega;
        a[q][q]=fm*sn*sn+a[q][q]*cn*cn-a[p][q]*omega;
        a[p][q]=0.0; a[q][p]=0.0;
        for (j=0; j<=n-1; j++)
        if ((j!=p)&&(j!=q))
          { //u=p*n+j; w=q*n+j;
            fm=a[p][j];
            a[p][j]=fm*cn+a[q][j]*sn;
            a[q][j]=-fm*sn+a[q][j]*cn;
          }
        for (i=0; i<=n-1; i++)
          if ((i!=p)&&(i!=q))
            { //u=i*n+p; w=i*n+q;
              fm=a[i][p];
              a[i][p]=fm*cn+a[i][q]*sn;
              a[i][q]=-fm*sn+a[i][q]*cn;
            }
        for (i=0; i<=n-1; i++)
          { //u=i*n+p; w=i*n+q;
            fm=v[i][p];
            v[i][p]=fm*cn+v[i][q]*sn;
            v[i][q]=-fm*sn+v[i][q]*cn;
          }
      }
    return(1);
  }
 
/*****************************************************************
 * hilbert_c2i
 * 
 * Convert coordinates of a point on a Hilbert curve to its index.
 * Inputs:
 *  nDims:      Number of coordinates.
 *  nBits:      Number of bits/coordinate.
 *  coord:      Array of n nBits-bit coordinates.
 * Outputs:
 *  index:      Output index value.  nDims*nBits bits.
 * Assumptions:
 *      nDims*nBits <= (sizeof bitmask_t) * (bits_per_byte)
 *  32 >= qd * ORDER
 */
bitmask_t hilbert_c2i(unsigned nDims, unsigned nBits, bitmask_t const coord[]) {
	assert(nDims*nBits<=sizeof(bitmask_t)*8);
	if (nDims>1) {
		unsigned const nDimsBits = nDims*nBits;
		bitmask_t index;
		unsigned d;
		bitmask_t coords = 0;
		for (d = nDims; d--; ) {
			coords <<= nBits;
			coords |= coord[d];
		}
	
		if (nBits > 1) {
			halfmask_t const ndOnes = ones(halfmask_t,nDims);
			halfmask_t const nd1Ones= ndOnes >> 1; /* for adjust_rotation */
			unsigned b = nDimsBits;
			unsigned rotation = 0;
			halfmask_t flipBit = 0;
			bitmask_t const nthbits = ones(bitmask_t,nDimsBits) / ndOnes;
			coords = bitTranspose(nDims, nBits, coords);
			coords ^= coords >> nDims;
			index = 0;
			do {
				halfmask_t bits = (coords >> (b-=nDims)) & ndOnes;
				bits = rotateRight(flipBit ^ bits, rotation, nDims);
				index <<= nDims;
				index |= bits;
				flipBit = (halfmask_t)1 << rotation;
				adjust_rotation(rotation,nDims,bits);
			} while (b);
			index ^= nthbits >> 1;
		} else
			index = coords;
	
		for (d = 1; d < nDimsBits; d *= 2)
			index ^= index >> d;
		return index;
	} else
		return coord[0];
}
