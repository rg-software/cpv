package cpv;

import java.awt.*;

import java.awt.geom.*;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.graph.*;

import com.formdev.flatlaf.FlatLightLaf;
import cpv.Runner.*;



/* Concurrent Programs Verifier

   Copyright 2003 by Maxim Mozgovoy and Mordechai (Moti) Ben-Ari.
   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   as published by the Free Software Foundation; either version 2
   of the License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE
   See the GNU General Public License for more details

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
   02111-1307, USA.
*/

public class Application    // main class
{
    public static MainFrame frame;                     // main application frame
    public static Program theprogram = null;           // currently loaded program
    public static ILRunner therunner = new ILRunner(); // ILRunner instance
    public static String CurrentFileName = null;       // filename of the currently loaded program

    public Application()
    {
    	FlatLightLaf.setup();
        frame = new MainFrame();
        frame.validate();  // validate frame size
        
        // Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        frameSize.height = Math.min(frameSize.height, screenSize.height);
        frameSize.width = Math.min(frameSize.width, screenSize.width);

        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
    	// turn antialiasing on
    	//System.setProperty("awt.useSystemAAFontSettings","on");
    	//System.setProperty("swing.aatext", "true");
        new Application();
    	
    	
/*
    	GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,
    	new
    	DefaultCellViewFactory());
    	JGraph graph = new JGraph(model, view);
    	graph.setAntiAliased(true);
    	//graph.setPreferredSize(new Dimension(1000,1000));
    	//graph.setScale(2);
    	
    	DefaultGraphCell[] cells = new DefaultGraphCell[3];
    	cells[0] = new DefaultGraphCell(new String("Hello"));
    	GraphConstants.setBounds(cells[0].getAttributes(), new
    	Rectangle2D.Double(20,20,40,20));
    	//GraphConstants.setGradientColor(
    	//cells[0].getAttributes(),
    	//Color.orange);
    	GraphConstants.setOpaque(cells[0].getAttributes(), true);
    	DefaultPort port0 = new DefaultPort();
    	cells[0].add(port0);
    	cells[1] = new DefaultGraphCell(new String("World"));
    	GraphConstants.setBounds(cells[1].getAttributes(), new
    	Rectangle2D.Double(140,140,40,20));
    
    	//GraphConstants.setGradientColor(
    		//	cells[1].getAttributes(),
    			//Color.red);
    			GraphConstants.setOpaque(cells[1].getAttributes(), true);
    			DefaultPort port1 = new DefaultPort();
    			cells[1].add(port1);
    			DefaultEdge edge = new DefaultEdge();
    			edge.setSource(cells[0].getChildAt(0));
    			edge.setTarget(cells[1].getChildAt(0));
    			cells[2] = edge;
    			int arrow = GraphConstants.ARROW_CLASSIC;
    			GraphConstants.setLineEnd(edge.getAttributes(), arrow);
    			GraphConstants.setEndFill(edge.getAttributes(), true);
    			
    			//GraphConstants.setAntialiased
    			graph.getGraphLayoutCache().insert(cells);
    			JFrame frame = new JFrame();
    			frame.getContentPane().add(new JScrollPane(graph));
    			frame.pack();
    			frame.setVisible(true);
    
    */
        }
}