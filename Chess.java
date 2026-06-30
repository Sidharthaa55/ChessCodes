import java.util.*; 

public class Chess 

{ 

int arr[][]=new int[8][8]; 

public void Pieces() 

{int a=2; 

for(int i=0;i<=1;i++) 

{ 

for(int j=0;j<5;j++) 

{ 

if(i==0) 

{ 

if(j<3){ 

arr[i][j]=arr[i][8-j-1]=a; 

arr[8-i-1][j]=arr[8-i-1][8-j-1]=10+a; 

a++; 

} 

else{

arr[i][j]=a; 

arr[8-i-1][j]=10+a; 

a++;} 

} 

if(i==1){ 

arr[i][j]=arr[i][8-j-1]=1; 

arr[8-i-1][j]=arr[8-i-1][8-j-1]=11;} 

} 

} 

} 

public void print() 

{ 

for(int i=0;i<8;i++) 

{ 

for(int j=0;j<8;j++) 

{ 

System.out.print(arr[i][j]+"\t"); 

} 

System.out.println(); 

} 

} 

public void check_Pawn_Move(int b,int a,int i,int j)			//INTIAL LOCATION(a,b) TO FINAL LOCATION(i,j) 

{										//x is to check Computer and User Movements 

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

/*spublic void Rook_movements() 

{ 
*/
  

  

  

  

  

  

  

  

  

  

  

  

  

  

  

  

  

  

  

  

  

public void Computer_movements() 

{ 

int p=(int)(Math.random()*8);	//Initial Point 

int q=(int)(Math.random()*8);	//Initial Point 

int r=(int)(Math.random()*8);	//Final Point 

int s=(int)(Math.random()*8);	//Final Point 

if(arr[p][q]==1) 

{ 

check_Pawn_Move(p,q,r,s); 

} 

  

else 

Computer_movements(); 

} 

public void User_movements() 

{ 

Scanner sc=new Scanner(System.in); 

System.out.println("Enter Initial Position"); 

int p=sc.nextInt(); 

int q=sc.nextInt(); 

System.out.println("Enter Final Position"); 

int r=sc.nextInt(); 

int s=sc.nextInt(); 

arr[r][s]=arr[p][q]; 

arr[p][q]=0; 

} 

public static void main(String args[]) 

{ 

Scanner in=new Scanner(System.in); 

Chess ob=new Chess(); 

ob.Pieces(); 

ob.print(); 

ob.Computer_movements(); 

System.out.println("\n"); 

ob.print(); 

ob.User_movements(); 

System.out.println("\n"); 

ob.print(); 

} 

} 

 