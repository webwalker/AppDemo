#include "string.h"
#include "AES.h"
#include "stdio.h"
#include <stdlib.h>

#define BLOCK_LENGTH 16 //明文块长度（16Byte）
/*********************************************************/
unsigned char Sbox[256];
unsigned char InvSbox[256];
unsigned char w[11][4][4];
int isKeyInit = -1;
/*********************************************************/

void initKey(unsigned char* key)
{
    memcpy(Sbox, sBox, 256);
    memcpy(InvSbox, invsBox, 256);
    KeyExpansion(key, w);
    isKeyInit = 1;
}

/*
	加密函数：Msg：明文，Key：秘钥，Cipher：输出密文，length:明文长度
	返回：密文长度
	注：密钥长度为16个字节
*/
int Encrypt(char *Msg,  char *Key, char *Cipher,int length) //length表示要加密的Msg的长度,密钥长度为16个字节
{
    if(length <=0)
    {
        return -1;
    }
    if(isKeyInit == -1)
    {
        initKey((unsigned char*)Key);
    }
    int OutLength = (length % BLOCK_LENGTH == 0) ? length : ((1 + (length / BLOCK_LENGTH)) * BLOCK_LENGTH);
    int j;
    unsigned char in[BLOCK_LENGTH];
    unsigned char out[BLOCK_LENGTH];
    if (length%16==0)//明文分块128bit
    {
        int round = length/BLOCK_LENGTH;

        for ( j=0;j<round;j++)
        {
            memcpy(&in[0], &Msg[BLOCK_LENGTH*j], BLOCK_LENGTH);
            encrypt(in, out);
            memcpy(&Cipher[BLOCK_LENGTH*j], &out, BLOCK_LENGTH);
        }
        return OutLength;
    }else
    {
        int ext = length / BLOCK_LENGTH;
        int dst = length % BLOCK_LENGTH;
        char * temp_in;
        char * temp_add;
        temp_in = (char *) malloc((ext + 1) * BLOCK_LENGTH);
        temp_add = (char *) malloc(BLOCK_LENGTH - dst);
        memcpy(&temp_in[0], &Msg[0], length);
        memset(temp_add, 0, BLOCK_LENGTH - dst);
        memcpy(&temp_in[length], &temp_add[0], BLOCK_LENGTH - dst);

        int round = ext + 1;
        for (j = 0; j < round; j++)
        {
            memcpy(&in[0], &temp_in[BLOCK_LENGTH * j], BLOCK_LENGTH);
            encrypt(in, out);
            memcpy(&Cipher[BLOCK_LENGTH * j], &out, BLOCK_LENGTH);
        }
        return OutLength;
    }
    return -1;
}

/*
	解密函数：Cipher：待解密密文，Key：秘钥，Msg：解密后输出，length:待解密密文长度
	返回：解密后输出长度
	注：密钥长度为16个字节
*/
int Decrypt(char *Cipher,  char *Key, char *Msg,int length)
{
    if(length <=0)
    {
        return -1;
    }
    if(isKeyInit == -1)
    {
        initKey((unsigned char*)Key);
    }
    int OutLength = (length % BLOCK_LENGTH == 0) ? length : ((1 + (length / BLOCK_LENGTH)) * BLOCK_LENGTH);
    int j;
    unsigned char in[BLOCK_LENGTH];
    unsigned char out[BLOCK_LENGTH];
    if (length%16==0)//明文分块128bit
    {
        int round = length/BLOCK_LENGTH;

        for ( j=0;j<round;j++)
        {
            memcpy(&in[0], &Cipher[BLOCK_LENGTH*j], BLOCK_LENGTH);
            decrypt(in, out);
            memcpy(&Msg[BLOCK_LENGTH*j], &out, BLOCK_LENGTH);
        }
        return OutLength;
    }else
    {
        int ext = length / BLOCK_LENGTH;
        int dst = length % BLOCK_LENGTH;
        char * temp_in;
        char * temp_add;
        temp_in = (char *) malloc((ext + 1) * BLOCK_LENGTH);
        temp_add = (char *) malloc(BLOCK_LENGTH - dst);
        memcpy(&temp_in[0], &Cipher[0], length);
        memset(temp_add, 0, BLOCK_LENGTH - dst);
        memcpy(&temp_in[length], &temp_add[0], BLOCK_LENGTH - dst);

        int round = ext + 1;
        for (j = 0; j < round; j++)
        {
            memcpy(&in[0], &temp_in[BLOCK_LENGTH * j], BLOCK_LENGTH);
            decrypt(in, out);
            memcpy(&Msg[BLOCK_LENGTH * j], &out, BLOCK_LENGTH);
        }
        return OutLength;
    }
    return -1;
}

unsigned char*  encrypt(unsigned char* input,unsigned char* result)
{
    unsigned char state[4][4];
    int i,r,c;
    for(r=0; r<4; r++)
    {
        for(c=0; c<4 ;c++)
        {
            state[r][c] = input[c*4+r];
        }
    }
    AddRoundKey(state,w[0]);
    for(i=1; i<=10; i++)
    {
        SubBytes(state);
        ShiftRows(state);
        if(i!=10)MixColumns(state);
        AddRoundKey(state,w[i]);
    }
    for(r=0; r<4; r++)
    {
        for(c=0; c<4 ;c++)
        {
            result[c*4+r] = state[r][c];
        }
    }
    return result;
}

unsigned char* decrypt(unsigned char* input,unsigned char* result)
{
    unsigned char state[4][4];
    int i,r,c;
    for(r=0; r<4; r++)
    {
        for(c=0; c<4 ;c++)
        {
            state[r][c] = input[c*4+r];
        }
    }
    AddRoundKey(state, w[10]);
    for(i=9; i>=0; i--)
    {
        InvShiftRows(state);
        InvSubBytes(state);
        AddRoundKey(state, w[i]);
        if(i)
        {
            InvMixColumns(state);
        }
    }
    for(r=0; r<4; r++)
    {
        for(c=0; c<4 ;c++)
        {
            result[c*4+r] = state[r][c];
        }
    }
    return result;
}


void KeyExpansion(unsigned char* key, unsigned char w[][4][4])
{
    int i,j,r,c;
    unsigned char rc[] = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36};
    for(r=0; r<4; r++)
    {
        for(c=0; c<4; c++)
        {
            w[0][r][c] = key[r+c*4];
        }
    }
    for(i=1; i<=10; i++)
    {
        for(j=0; j<4; j++)
        {
            unsigned char t[4];
            for(r=0; r<4; r++)
            {
                t[r] = j ? w[i][r][j-1] : w[i-1][r][3];
            }
            if(j == 0)
            {
                unsigned char temp = t[0];
                for(r=0; r<3; r++)
                {
                    t[r] = Sbox[t[(r+1)%4]];
                }
                t[3] = Sbox[temp];
                t[0] ^= rc[i-1];
            }
            for(r=0; r<4; r++)
            {
                w[i][r][j] = w[i-1][r][j] ^ t[r];
            }
        }
    }
}

unsigned char FFmul(unsigned char a, unsigned char b)
{
    unsigned char bw[4];
    unsigned char res=0;
    int i;
    bw[0] = b;
    for(i=1; i<4; i++)
    {
        bw[i] = bw[i-1]<<1;
        if(bw[i-1]&0x80)
        {
            bw[i]^=0x1b;
        }
    }
    for(i=0; i<4; i++)
    {
        if((a>>i)&0x01)
        {
            res ^= bw[i];
        }
    }
    return res;
}

void SubBytes(unsigned char state[][4])
{
    int r,c;
    for(r=0; r<4; r++)
    {
        for(c=0; c<4; c++)
        {
            state[r][c] = Sbox[state[r][c]];
        }
    }
}

void ShiftRows(unsigned char state[][4])
{
    unsigned char t[4];
    int r,c;
    for(r=1; r<4; r++)
    {
        for(c=0; c<4; c++)
        {
            t[c] = state[r][(c+r)%4];
        }
        for(c=0; c<4; c++)
        {
            state[r][c] = t[c];
        }
    }
}

void MixColumns(unsigned char state[][4])
{
    unsigned char t[4];
    int r,c;
    for(c=0; c< 4; c++)
    {
        for(r=0; r<4; r++)
        {
            t[r] = state[r][c];
        }
        for(r=0; r<4; r++)
        {
            state[r][c] = FFmul(0x02, t[r])
                          ^ FFmul(0x03, t[(r+1)%4])
                          ^ FFmul(0x01, t[(r+2)%4])
                          ^ FFmul(0x01, t[(r+3)%4]);
        }
    }
}

void AddRoundKey(unsigned char state[][4], unsigned char k[][4])
{
    int r,c;
    for(c=0; c<4; c++)
    {
        for(r=0; r<4; r++)
        {
            state[r][c] ^= k[r][c];
        }
    }
}

void InvSubBytes(unsigned char state[][4])
{
    int r,c;
    for(r=0; r<4; r++)
    {
        for(c=0; c<4; c++)
        {
            state[r][c] = InvSbox[state[r][c]];
        }
    }
}

void InvShiftRows(unsigned char state[][4])
{
    unsigned char t[4];
    int r,c;
    for(r=1; r<4; r++)
    {
        for(c=0; c<4; c++)
        {
            t[c] = state[r][(c-r+4)%4];
        }
        for(c=0; c<4; c++)
        {
            state[r][c] = t[c];
        }
    }
}

void InvMixColumns(unsigned char state[][4])
{
    unsigned char t[4];
    int r,c;
    for(c=0; c< 4; c++)
    {
        for(r=0; r<4; r++)
        {
            t[r] = state[r][c];
        }
        for(r=0; r<4; r++)
        {
            state[r][c] = FFmul(0x0e, t[r])
                          ^ FFmul(0x0b, t[(r+1)%4])
                          ^ FFmul(0x0d, t[(r+2)%4])
                          ^ FFmul(0x09, t[(r+3)%4]);
        }
    }
}
