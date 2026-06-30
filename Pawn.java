import java.util.*;
public class Pawn
{

	public void check_Pawn_Move(int b,int a,int i,int j)			//INTIAL LOCATION(a,b) TO FINAL LOCATION(i,j) 
	{//x is to check Computer and User Movements 

		if(arr[i][j]==0) 

		{ 

			if(b==1) 

			{

				if(i<=b+2&&j==a) 

				{ 

					arr[i][j]=1; 

					arr[b][a]=0; 

				} 

				else 

				Computer_movements(); 

			} 
			else 

			{ 

				if(i==b+1&&j==a) 

				{ 

					arr[i][j]=1; 

					arr[b][a]=0; 

				} 

				else 

					Computer_movements(); 

  

			} 

		} 

		else if(arr[i][j]>10) 

		{ 

			if(i==b+1&&j==a+1) 

			{ 

				arr[i][j]=1; 

				arr[b][a]=0; 

			} 

			if(i==b+1&&j==a-1) 

			{ 

				arr[i][j]=1; 

				arr[b][a]=0; 

			} 

			else 

				Computer_movements(); 

		} 

		else 

			Computer_movements(); 

	} 
}