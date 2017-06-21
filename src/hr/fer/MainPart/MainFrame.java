package hr.fer.MainPart;
import hr.fer.Visual.*;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.*;

import hr.fer.GeneticAlgorithm.*;
import hr.fer.Parsing.*;

public class MainFrame extends JFrame 
{
	private static final long serialVersionUID = -8401309144962040446L;
	JTextField textField=new JTextField("b*a+a*(-c+d)");
	JButton buttonOK=new JButton("OK");
	JPanel skica;
	
	int POPULATION_SIZE=50;
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
			temp=BrutAllTheWay(output);
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
		Draw temp=new Draw(output,500,1100);
		
		double mina=454546545;
		
		for(int i=0;i<8000;++i)
		{
			Draw D=new Draw(output,500,1100);
			mina=Math.min(mina, D.getEdgeLengthDeviation());
			if(i%1000==0)System.out.println(i+" "+temp.getEvaluationFunction()+" "+temp.getNumWiresCrossing()+" "+temp.numOfNodes+"-->"+mina);
			if(D.getEvaluationFunction()<temp.getEvaluationFunction())
			{
				temp=D;
			}
		}
		
		return temp;
	}
	Draw startOfGA(String output)
	{
		List<Draw>draws=new ArrayList<>();
		for(int i=0;i<10;++i)
		{
			draws.add(new Draw(output,550,1100));
		}
		
		Population population=new Population(draws);
		Draw ans=new Draw(output,550,1100);
		
		int b=1566;
		for(int i=0;i<3000;++i)
		{
			population.generateNewGeneration();
			Draw bestDraw=population.getBestDrawFromPopulation();
			
			if(b>bestDraw.getNumWiresCrossing())
			{
				
				ans.setSymbols(bestDraw.getSymbols());
				ans.setWires(bestDraw.getWires());
				System.out.println("hjkhjk");
				b=new Integer(bestDraw.getNumWiresCrossing());
			}
			
			if(i%100==0)System.out.println(i+" "+bestDraw.getNumWiresCrossing()+" "+b);
			//if(i==56)return bestDraw;
		}
		//GeneticOperations.EdgeMutation2(ans);
		
		//GeneticOperations.SingleMutate(ans, 18);
		return ans;
	}
}
