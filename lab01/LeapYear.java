import java.util.*;
public class LeapYear {
	public static boolean leapYearTest(int year){
		boolean result=false;
		if(year%400==0||(year%4==0&&year%100!=0)){
			result=true;
		}
		return result;
	}
	public static void main(String[] args){
		System.out.println("Please enter the year");
		Scanner in=new Scanner(System.in);
		int year=in.nextInt();
		if(leapYearTest(year)){
			System.out.println(year+"is a leap year");
		}else{
			System.out.println("It's not");
		}
		
	}
}
