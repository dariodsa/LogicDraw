package hr.fer.Parsing;
import java.util.*;

public class Parser 
{
	private String input;
	private String outputPostFix;
	Stack<Character>S=new Stack<>();
	public Parser(String input)
	{
		this.input=input;
	}
	int prioritet(Character x)
	{
		switch(x)
		{
			case '-':return 3;
			case '*':return 2;
			case '+':return 1;
		}
		return -1;
	}
	public String parsMyInput()
	{
		String output="";
		for(int i=0,len=input.length();i<len;++i)
		{
			if(input.charAt(i)>='a' && input.charAt(i)<='z')
			{
				output+=input.charAt(i);
			}
			else if(input.charAt(i)=='(')
				S.push('(');
			else if(input.charAt(i)==')')
			{
				while(!S.isEmpty() && S.peek()!='(')
				{
					output+=S.peek();
					S.pop();
				}
				if(S.isEmpty()==false)S.pop();
			}
			else if(input.charAt(i)!=' ')
			{
				while(!S.isEmpty() && prioritet(S.peek())>=prioritet(input.charAt(i)))
				{
					output+=S.peek();
					S.pop();
				}
				S.push(input.charAt(i));
			}
		}
		while(!S.isEmpty()){output+=S.peek();S.pop();}
		return output;
	}
	public static int getNumOfNodes(String postfix)
	{
		int ans=0;
		
		for(int i=0;i<postfix.length();++i)
		{
			if(postfix.charAt(i)=='-' ||
			   postfix.charAt(i)=='*' || 
			   postfix.charAt(i)=='+')++ans; 
			//else if(postfix.charAt(i)>='a' && postfix.charAt(i)<='z')ulaz.put((postfix.charAt(i)+new Integer(i).toString()).toLowerCase(), 2);
			//else if(postfix.charAt(i)>='A' && postfix.charAt(i)<='Z')ulaz.put((postfix.charAt(i)+new Integer(i).toString()).toLowerCase(), 2);
		}
		return ans+1;
	}
}