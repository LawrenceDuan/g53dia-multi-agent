package uk.ac.nott.cs.g53dia.multilibrary;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
/**
 * A simple user interface for watching an individual Tanker.
 *
 * @author Neil Madden.
 */
/*
 * Copyright (c) 2003 Stuart Reeves
 * Copyright (c) 2003-2005 Neil Madden (nem@cs.nott.ac.uk).
 * Copyright (c) 2011 Julian Zappala (jxz@cs.nott.ac.uk).
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public class TankerViewer extends JFrame implements ActionListener {
   
	private static final long serialVersionUID = -2810783821678793885L;
	final static int SIZE = (Tanker.VIEW_RANGE * 2) + 1, ICON_SIZE = 25, PSIZE = SIZE * ICON_SIZE;

	TankerViewerIconFactory iconfactory;
    Fleet fleet;
    Tanker tank;
    JLabel[][] cells;
	JLabel[][] tankers;
    JLabel tstep, fuel, pos, waste, disposed, score;
	JLayeredPane lp;
	JPanel pCells;
	JPanel pTankers;
	JPanel infop;
	JComboBox<String> tankerList;

    public TankerViewer(Tanker Tanker) { this(Tanker, new DefaultTankerViewerIconFactory()); }
    
    public TankerViewer(Tanker Tanker, TankerViewerIconFactory fac) {
        this.tank = Tanker;
        this.iconfactory = fac;
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        // Create the cell viewer
        cells = new JLabel[SIZE][SIZE];
		tankers = new JLabel[SIZE][SIZE];
        lp = new JLayeredPane();
        lp.setSize(new Dimension(PSIZE,PSIZE));
		pCells = new JPanel(new GridLayout(SIZE, SIZE));
        pCells.setBackground(Color.WHITE);
		pTankers = new JPanel(new GridLayout(SIZE, SIZE));
		pTankers.setOpaque(false);

		for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                cells[x][y] = new JLabel();
                pCells.add(cells[x][y]);
				
                tankers[x][y] = new JLabel(iconfactory.getIconForTanker(Tanker));
                tankers[x][y].setBounds(PSIZE/2 - ICON_SIZE/2,PSIZE/2 - ICON_SIZE/2,ICON_SIZE,ICON_SIZE);
                tankers[x][y].setVisible(false);
				pTankers.add(tankers[x][y]);
            }
        }
        
		lp.add(pCells,new Integer(0));
		lp.add(pTankers,new Integer(1));
        pCells.setBounds(0,0,PSIZE,PSIZE);
        pTankers.setBounds(0,0,PSIZE,PSIZE);
		c.add(lp, BorderLayout.CENTER);
        
        // Create some labels to show info about the Tanker and environment
        infop = new JPanel(new GridLayout(0,4));
        infop.add(new JLabel("Timestep:"));
        tstep = new JLabel("0");
        infop.add(tstep);
        infop.add(new JLabel("Fuel:"));
        fuel = new JLabel("100");
        infop.add(fuel);
        infop.add(new JLabel("Position:"));
        pos = new JLabel("(0,0)");
        infop.add(pos);
        infop.add(new JLabel("Waste:"));
        waste = new JLabel("0");
        infop.add(waste);
        infop.add(new JLabel("Disposed:"));
        disposed = new JLabel("0");
        infop.add(disposed);
        infop.add(new JLabel("Overall Score:"));
        score = new JLabel("0");
        infop.add(score);
        
        c.add(infop, BorderLayout.SOUTH);
        //infop.setPreferredSize(new Dimension(200,100));
        
        setSize(PSIZE,PSIZE + 50);
        setTitle("Tanker Viewer");
        setVisible(true);
    }

	public TankerViewer(Fleet fleet) {
		this(fleet.get(0));
		this.fleet = fleet;

		String[] tankerNames = new String[fleet.size()];
		for (int i = 0; i < fleet.size(); i++) {
			tankerNames[i] = "Tanker " + i;
		}

		//A drop down list to select which tanker to view
		tankerList = new JComboBox<String>(tankerNames);
		infop.add(tankerList);

		//Event handler for drop down list
		tankerList.addActionListener(this);

	}

	public void setTanker(Tanker t) {
		this.tank = t;
	}

    public void tick(Environment env) {
        Cell[][] view = env.getView(tank.getPosition(),Tanker.VIEW_RANGE);
        pos.setText(tank.getPosition().toString());
        tstep.setText(new String(""+env.getTimestep()));
        waste.setText(new String(""+tank.wasteLevel));
        fuel.setText(new String(""+tank.getFuelLevel()));
        disposed.setText("" + tank.wasteDisposed);
        score.setText("" + fleet.getScore());
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Icon cur = iconfactory.getIconForCell(view[x][y]);
                cells[x][y].setIcon(cur);
				tankers[x][y].setVisible(false);
				// Now Draw Tankers
				for (Tanker t : fleet) {
					if (view[x][y].getPoint().equals(t.getPosition())) {
						tankers[x][y].setVisible(true);
					}	
				}
            }
        }
    }
    
	@Override
	public void actionPerformed(ActionEvent arg0) {
		tank = fleet.get(tankerList.getSelectedIndex());

	}

}

