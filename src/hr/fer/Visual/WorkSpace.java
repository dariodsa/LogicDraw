package hr.fer.Visual;
import hr.fer.DrawObjects.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardEndHandler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import hr.fer.GeneticAlgorithm.*;
import hr.fer.Geometric.Geometric;

public class WorkSpace extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private java.util.List<hr.fer.DrawObjects.SShape> list;
	private Draw skica;
	
	private SShape selected=null;
	public WorkSpace()
	{
		this.setFocusable(true);
		this.requestFocusInWindow();
	}
	public WorkSpace(Draw w)
	{
		this();
		this.skica=w;
		list=skica.getShapes();
		setLayout(null);
		
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent event) {
				//System.out.println(event.getLocationOnScreen());
				
				SShape shape=Geometric.getClosest(list,new Dot(event.getPoint().x,event.getPoint().y));
				//System.out.println(shape);
				selected=shape;
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		InputMap im = getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
	    ActionMap am = getActionMap();

	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),KeyEvent.VK_RIGHT);
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), KeyEvent.VK_LEFT);
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),   KeyEvent.VK_UP);
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), KeyEvent.VK_DOWN);

	    am.put(KeyEvent.VK_RIGHT, new KeyPressed(KeyEvent.VK_RIGHT));
	    am.put(KeyEvent.VK_LEFT,  new KeyPressed(KeyEvent.VK_LEFT));
	    am.put(KeyEvent.VK_UP,    new KeyPressed(KeyEvent.VK_UP));
	    am.put(KeyEvent.VK_DOWN,  new KeyPressed(KeyEvent.VK_DOWN));
	    //getActionMap().put(MOVE_DOWN, new MoveAction(2, 1));
		
	}
	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(skica==null)return;
        
        for(hr.fer.DrawObjects.SShape S: list)
        {
        	try {
				S.draw(g);
				if(S instanceof Symbol)
				{
					Symbol S1=(Symbol)S;
					//if(S1.isSymbolOutInType())
						paintINOUT(S1);
				}
			} catch (IOException e) {e.printStackTrace();}
        }
    }
	public void paintINOUT(Symbol s)
	{
		JTextPane text=new JTextPane();
		text.setText(new Integer(s.getDepth()).toString());
		text.setVisible(true);
		text.setLocation(s.getCenterDot().getX(),s.getCenterDot().getY()-30);
		text.setOpaque(true);
		text.setSize(30, 20);
		add(text);
	}
	 class KeyPressed extends AbstractAction
	{
		 int key;
		public KeyPressed(int key)
		{
			this.key=key;
			
			//return null;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			//System.out.println(event.toString());
			//System.out.println(key+" "+KeyEvent.VK_LEFT);
			//System.out.println(selected);
			if(selected==null)return;
			
			if (key == KeyEvent.VK_LEFT) 
			{
				Symbol s1=(Symbol)selected;
				System.out.println(s1.getLocation());
				
				Dot D=new Dot(selected.getLocation());
				D.setX(D.getX()-5);
				selected.moveIt(D);
				
				Symbol s=(Symbol)selected;
				System.out.println(s.getLocation());
				
			}
			if (key == KeyEvent.VK_RIGHT) 
			{
				Dot D=new Dot(selected.getLocation());
				D.setX(D.getX()+5);
				selected.moveIt(D);
			}
			if (key == KeyEvent.VK_UP) 
			{
				Dot D=new Dot(selected.getLocation());
				D.setY(D.getY()-5);
				selected.moveIt(D);
			}
			if(key==KeyEvent.VK_DOWN)
			{
				Dot D=new Dot(selected.getLocation());
				D.setY(D.getY()+5);
				selected.moveIt(D);
			}
			repaint();
		}
	}
}
	
