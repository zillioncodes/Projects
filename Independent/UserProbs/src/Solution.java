import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


import java.io.*;
import java.util.*;
public class Solution {   
     
    public static void main(String args[] ) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		ArrayList<Long> inputArray = new ArrayList<Long>();
		String temp="";int flag=0;
		temp = in.readLine();
        if(temp.isEmpty()){
            System.out.println("failure"+"\n");
        }else{
        try{    
		  inputArray.add(Long.parseLong(temp));
        } catch(Exception e){ System.out.println("failure");}
        while(!(temp.isEmpty())){
			temp = in.readLine();
             if(temp.isEmpty()){
                    flag++;
                    System.out.println("failure");
                    break;
             }else{
                try{
			         inputArray.add(Long.parseLong(temp));
                }catch(Exception e){System.out.println("failure");}
			}
		}
        if(flag==0){    
		String result = arrayHopper(inputArray);
		System.out.println(result);
        }else{}
	}
   }
    private static String arrayHopper(ArrayList<Long> array) {
	
	long[] hops = new long[array.size()];long startIndex = 0;
	StringBuffer resultString = new StringBuffer("");long lastappend=0;
	if(array.get(0)==0||array.size()==0){
		return "failure";
	}
	long[] arrtest = new long[array.size()]; 
	hops[0]=0;
	long temp=0;
	for(int i=1;i<array.size();i++){
		hops[i]=Integer.MAX_VALUE;
		for(int j=0;j<i;j++){
			if(i<=j + array.get(j) && hops[j]!=Integer.MAX_VALUE){
				hops[i] = Solution.getMin(hops[i],hops[j]+1);
				if(hops[i]!=hops[i-1] && lastappend!=Integer.MAX_VALUE){
					lastappend = i-1;
					resultString.append(i-1).append(",").append(" ");
					long fly = lastappend + array.get(i-1);
					if(fly==array.size()-1){
						resultString.append(fly).append(",").append(" ");
					}
					if(fly>array.size()){
						lastappend = Integer.MAX_VALUE;
					}
				}
				break;
			}
		}
	}
    if(hops[array.size()-1]==Integer.MAX_VALUE){
		return "failure";
	}
	resultString.append("out");
	return resultString.toString();
    }
    public static long getMin(long x,long y){
        return (x<y)?x:y;
    }
	
}