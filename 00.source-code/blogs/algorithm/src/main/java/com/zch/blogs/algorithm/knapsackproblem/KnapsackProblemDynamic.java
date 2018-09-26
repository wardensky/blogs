package com.zch.blogs.algorithm.knapsackproblem;

/**
 * @Description 动态规划解决背包问题。
 * @author zch
 * @time 2018年9月26日 下午7:41:32
 * 
 */
public class KnapsackProblemDynamic {
	void FindMax()//动态规划
	{
	    int i,j;
	    //填表
	    for(i=1;i<=number;i++)
	    {
	        for(j=1;j<=capacity;j++)
	        {
	            if(j<w[i])//包装不进
	            {
	                V[i][j]=V[i-1][j];
	            }
	            else//能装
	            {
	                if(V[i-1][j]>V[i-1][j-w[i]]+v[i])//不装价值大
	                {
	                    V[i][j]=V[i-1][j];
	                }
	                else//前i-1个物品的最优解与第i个物品的价值之和更大
	                {
	                    V[i][j]=V[i-1][j-w[i]]+v[i];
	                }
	            }
	        }
	    }
	}
	
	void FindWhat(int i,int j)//寻找解的组成方式
	{
	    if(i>=0)
	    {
	        if(V[i][j]==V[i-1][j])//相等说明没装
	        {
	            item[i]=0;//全局变量，标记未被选中
	            FindWhat(i-1,j);
	        }
	        else if( j-w[i]>=0 && V[i][j]==V[i-1][j-w[i]]+v[i] )
	        {
	            item[i]=1;//标记已被选中
	            FindWhat(i-1,j-w[i]);//回到装包之前的位置
	        }
	    }
	}
}
