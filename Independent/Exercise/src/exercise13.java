import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class exercise13 {
	
	public static void main(String[] args) throws NumberFormatException, IOException{
	 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));double scount=0;
     int t = Integer.parseInt(in.readLine());
     for(int i=0;i<t;i++){
         String[] str = in.readLine().split(" ");
         double r = Double.parseDouble(str[0]);
         double k = Double.parseDouble(str[1]);
         double rootr = Math.sqrt(r);
         for(int x=0;x<=(int)rootr;x++){
             for(int y=0;y<=(int)rootr;y++){
                 int tmp = (x*x) + (y*y);
                 if(r>=tmp && x==y && (x+y)!=0){
                     scount = scount+4;
                 }else if(r>=tmp && x!=y){
                     scount = scount+2;
                 }
             }
         }
         if(scount==k&&scount!=0){
             System.out.println("possible");
         }else{
             System.out.println("impossible");
         }
         scount=0;
     }
 }
}

