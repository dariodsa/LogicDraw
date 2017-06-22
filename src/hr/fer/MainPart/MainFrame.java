package hr.fer.MainPart;
import hr.fer.Visual.*;
import hr.fer.Parsing.*;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.*;
import java.util.Random;

import javax.swing.*;

import hr.fer.DrawObjects.Symbol;
import hr.fer.GeneticAlgorithm.*;
import hr.fer.Parsing.*;

public class MainFrame extends JFrame 
{
	private static final long serialVersionUID = -8401309144962040446L;
	JTextField textField=new JTextField("b*a+a*c+-b+c*(a+c*b+-d)");//"b*a+a*(-c+d)");
	JButton buttonOK=new JButton("OK");
	JPanel skica;
	
	int POPULATION_SIZE=60;
	int NUM_OF_GENERATION=5400;
	
	
	/*
	 * Initialize the main frame, with given height, width and other general properties
	 * @param int height
	 * @param int width
	 */
	MainFrame(int height,int width)
	{
		setSize(width, height);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
		setLayout(new BorderLayout());
		initGUI();
	}
	/*
	 * Initialize the gui. It creates the textFiled on the top, draw area in the middle
	 * and the button ok on the bottom. 
	 * @return void
	 */
	void initGUI()
	{
		add(textField,BorderLayout.NORTH);
		
		
		add(buttonOK,BorderLayout.SOUTH);
		buttonOK.addActionListener((e)->{try {
			buttonOKClick();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}});
	}
	
	void eraseGUI()
	{
		if(skica!=null)
			remove(skica);
	}
	/*
	 * The textField's text will be given to the Parser. With the string output as the result of the
	 * parsing, we can draw it. We draw it by creating the new WorkSpace(draw), and adding it to JFrame. 
	 * Workspace extends JPanel.
	 * @return void
	 */
	private Draw temp=null;
	void buttonOKClick() throws Exception
	{
		eraseGUI();
		//initGUI();
		String input=textField.getText();
		
		Parser P=new Parser(input);
		String output=P.parsMyInput();
		
		Thread p=new Thread(()-> 
		{
			temp=startOfGA(output);
			//startOfGA(output);
		});
		p.start();
		p.join();
		
		WorkSpace work=new WorkSpace(temp);
		
		add(work,BorderLayout.CENTER);
		repaint();
		//JOptionPane.showMessageDialog(null,temp.getWiresCrossing()-4*temp.getSymbols().size());
		
	}
	//b*a+a*c+-b+c*(a+c*b+-d)
	Draw BrutAllTheWay(String output)
	{
		int len=Parser.getNumOfNodes(output);
		
		Draw temp=new Draw(output,500,1100,generateBitMask1(len),generateBitMask2(len));
		
		double mina=454546545;
		
		for(int i=0;i<50000;++i)
		{
			Draw D=new Draw(output,500,1100,generateBitMask1(len),generateBitMask2(len));
			mina=Math.min(mina, D.getEdgeLengthDeviation());
			if(i%1000==0)System.out.println(i+" "+temp.getEvaluationFunction()+" "+temp.getNumWiresCrossing()+"-->"+mina);
			if(D.getEvaluationFunction()<temp.getEvaluationFunction())
			{
				temp=D;
			}
		}
		//GeneticOperations.moveItLeftOrRight(temp);
		return temp;
	}
	Draw startOfGA(String output)
	{
		List<Draw>draws=new ArrayList<>();
		int len=Parser.getNumOfNodes(output);
		
		for(int i=0;i<POPULATION_SIZE;++i)
			draws.add(new Draw(output,550,1100,generateBitMask1(len),generateBitMask2(len)));
		
		Population P=new Population(draws);
		
		
		Draw ansDraw=draws.get(0);
		int kol=0;
		
		for(int i=0;i<1880;++i)
		{
			P.generateNewGeneration();
			Draw D=P.getBestDrawFromPopulation();
			if(D.compareTo(ansDraw)<0)
			{
				ansDraw=D.duplicate();
				System.out.println("Bolja "+ansDraw.getNumWiresCrossing());
				kol=i;
			}
			
			if(i%30==0)
			{
				System.out.println(i);
			}
			//if(i-kol>320)break;
			
		}
		
		return ansDraw;
	}
	
	
	public List<Integer> generateBitMask1(int len)
	{
		List<Integer>list=new ArrayList<>();
		Random rand=new Random();
		for(int i=0;i<len;++i)
		{
			list.add(rand.nextInt(27));
		}
		return list;
	}
	public List<Integer> generateBitMask2(int len)
	{
		List<Integer>list=new ArrayList<>();
		Random rand=new Random();
		int kol=0;
		for(int i=0;i<len;++i)
		{
			int pos=(int)rand.nextGaussian();
			if(pos<=-2)list.add(kol);
			else if(pos==-1 && kol==0 && kol==1){list.add(kol+1);++kol;}
			else 
			{
				list.add(kol+2);
				kol+=2;
			}
		}
		return list;
	}
}
