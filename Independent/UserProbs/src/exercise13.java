
	import java.util.Arrays;


	public class exercise13 {

		public static void main(String[] args) 
		{
	        exercise13 obj = new exercise13();
	        try
	        {
	        	int [] inputArr = {7, 4, 5, 2, 3 ,-4 ,-3 ,-5};//{1,6,10,4,7,9,5};//7 4 5 2 3 -4 -3 -5 
	        	Arrays.sort(inputArr);
	        	obj.findTeam(inputArr);
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
		}
		
		public void findTeam(int[] inputArr)
		{
			int minGrpSize = 0;
			int startingIndex =-1;
			int [] longestSeq = new int[inputArr.length];
			for(int i=0;i<inputArr.length;i++)
			{
				if(i==0)
				{
					longestSeq[i]=1;
				}
				else
				{
					if(inputArr[i]-inputArr[i-1]==1)
					{
						longestSeq[i] = longestSeq[i-1]+1;
					}
					else
					{
						longestSeq[i]=1;
						if(longestSeq[i-1]!=1)
						{
							if(minGrpSize==0)
							{
								minGrpSize = longestSeq[i-1];
								startingIndex = i-(longestSeq[i-1]);
							}
							else
							{
								if(longestSeq[i-1]<minGrpSize)
								{
									minGrpSize = longestSeq[i-1];
									startingIndex = i-(longestSeq[i-1]);
								}
							}
							
						}
					}
				}
			}
			System.out.println(minGrpSize+" "+startingIndex);
		}

	}


