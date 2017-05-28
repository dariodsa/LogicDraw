package hr.fer.Testing;

import hr.fer.DrawObjects.Dot;
import hr.fer.DrawObjects.Symbol;
import hr.fer.DrawObjects.Symbols;

import org.junit.*;

public class SymbolTest 
{
	@Test
	public void InputDot()
	{
		Symbol S=new Symbol(Symbols.OR);
		S.setPosition(new Dot(100,100));
		Symbol S1=new Symbol(Symbols.OR);
		Symbol S2=new Symbol(Symbols.OR);
		S.addParent(S1);S.addParent(S2);
		Dot i1=S.getInput(1);
		Dot i2=S.getInput(2);
		if((i1.compareValueTo(S.getCenterDot()))==false)
			Assert.fail();
		if(i1.getX()!=S.getCenterDot().getX()-S.getWidth()/2)Assert.fail();
		if(i1.getY()!=S.getCenterDot().getY()-S.getHeight()/2+1.0/3.0*(S.getHeight()))Assert.fail();
	}
}
