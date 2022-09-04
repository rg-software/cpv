package cpv.MyJGraph;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.*;
import org.jgraph.graph.*;
import cpv.*;

//------------------------------------------------------------------------
// JGraph extension for flowchart editing
//------------------------------------------------------------------------
public class EJGraph extends GenericJGraph
{
//------------------------------------------------------------------------
	private int CurrentX = 0, CurrentY = 0;         // current coordinates of mouse pointer
    private PortView StartPort = null;              // current source port (for arrow creation)
    private MyGraphCell VarsCell = null;            // reference to Local Variables Vertex
    private boolean IsOverPort = false;             // is the mouse pointer over port or not
//------------------------------------------------------------------------
    public EJGraph()
    {
    	super(new DefaultGraphModel());
    	// create an empty Local Variables Vertex
    	// space is reserved for "<html></html>"
        VarsCell = InsertBlock(CurrentX, CurrentY, MyGraphCell.GENERIC, "             ", 
        					   Configuration.EJGRAPH_DEFAULT_CELL_WIDTH, Configuration.EJGRAPH_DEFAULT_CELL_HEIGHT);
        VarsCell.setTag(MyGraphCell.VARS_CELL);      		// cell type = VARS_CELL
        setMarqueeHandler(new EJGraphMarqueeHandler(this)); // attach custom marquee handler
    }
//------------------------------------------------------------------------
    // load EJGraph from the input stream (override GenericJGraph implementation)
    public void LoadFromFile(BufferedReader dec) throws java.io.IOException
    {
        super.LoadFromFile(dec);

        for (Object r : getRoots()) // finding Vars Cell Vertex
            if (r instanceof MyGraphCell && ((MyGraphCell)r).getTag() == MyGraphCell.VARS_CELL)
            	VarsCell = (MyGraphCell)r;    // validate VarsCell reference
    }
//------------------------------------------------------------------------
    public void EditVarsCell()       // convenience function: shows Local Variables Editor box
    {
        editCell(VarsCell);
    }
//------------------------------------------------------------------------
    public MyGraphCell GetVarsCell()
    {
    	return VarsCell;
    }
//------------------------------------------------------------------------
    private void editCell(MyGraphCell cell)     // shows an editor box for a given cell
    {
        // if the given cell is not END (END block edition is forbidden), use cell editor
        if (cell.getType() != MyGraphCell.BEGIN_END)
        {
            //  if the given cell is the VarsCell use Local Variables Editor
        	BlockEditor editor = cell == VarsCell ? 
        				new LocalVarsEditor(null, "Local Variables Editor", true, convertValueToString(cell)) : 
        				new GenericBlockEditor(null, "Input Command", true, convertValueToString(cell));

            editor.setLocationRelativeTo(this);
            editor.setVisible(true);
        
        	if (editor.OKPressed())             // if edition succeeded
        	{
            	var nest = new HashMap<DefaultGraphCell, AttributeMap>();            	
            	var attr = new AttributeMap();
            	GraphConstants.setValue(attr, editor.GetResultingText());   // modify cell data
            	nest.put(cell, attr);        								// (refer to JGraph documentation)
            	getModel().edit(nest, null, null, null);
        	}
        }
    }
//------------------------------------------------------------------------
    public void MousePressed(MouseEvent e)         // mouse handler
    {
        if (SwingUtilities.isLeftMouseButton(e))
        {
            if (e.getClickCount() == 2) // on left-button double-click show cell editor box
            {
                Object cell = getFirstCellForLocation(e.getX(), e.getY());
                if (cell instanceof MyGraphCell)    // edit cell contents
                    editCell((MyGraphCell)cell);
                e.consume();
            }
            else if (e.getClickCount() == 1)  // on single click get starting port
            {
                Point2D p = fromScreen(new Point(e.getPoint()));
                PortView pv = getPortViewAt(p.getX(), p.getY());        // get portview under mouse pointer
                
                // if there IS a port under mouse pointer and it is NOT VarsCell's port, make a reference to this port
                // StartPort is used for arrow creation; see also MouseDragged() and MouseReleased()
                
                StartPort = (pv != null && getModel().getParent((Port)pv.getCell()) != VarsCell) ? pv : null;
                if (StartPort != null)
                    e.consume();
            }
        }
    }
//------------------------------------------------------------------------
    public void MouseReleased(MouseEvent e)  // on mouse release possibly create an arrow
    {
        if (SwingUtilities.isLeftMouseButton(e))
        {
            Point2D tmp = fromScreen(new Point(e.getPoint()));
            PortView pv = getPortViewAt(tmp.getX(), tmp.getY());
            
            // get an EndPort - the port of the destination cell
            PortView EndPort = (pv != null && getModel().getParent((Port)pv.getCell()) != VarsCell) ? pv : null;

            if (StartPort != null && EndPort != null)  // if both ports are valid
            {
                // get corresponding vertices
                Port p1 = (Port)StartPort.getCell(), p2 = (Port)EndPort.getCell();

                // transitions within the same vertex are allowed now (we only care that the ports are different)
                // to ensure different port vertices, check (getModel().getParent(p1) != getModel().getParent(p2))
                if (p1 != p2)
                    connect(p1, p2);
            }

            StartPort = EndPort = null;
        }
    }
//------------------------------------------------------------------------
    public void MouseMoved(MouseEvent e)
    {
        Point2D tmp = fromScreen(new Point(e.getPoint()));
        PortView pv = getPortViewAt(tmp.getX(), tmp.getY());             // get a port under the mouse pointer

        CurrentX = e.getX();          // get current coordinates
        CurrentY = e.getY();

        if (pv != null)                // if the port is valid
        {
            Object cell = getModel().getParent((Port)pv.getCell());   // get a parental cell

            if (cell instanceof MyGraphCell && cell != VarsCell)       // if it is not VarsCell
            {
                IsOverPort = true;                                    // now we are "over port"
                setCursor(new Cursor(Cursor.HAND_CURSOR));            // change current cursor
                e.consume();
            }
        }
        else if (IsOverPort)   // else if we WERE "over port"
        {
            IsOverPort = false;
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // change cursor to default
            e.consume();
        }
    }
//------------------------------------------------------------------------
    public void MouseDragged(MouseEvent e)
    {
        Point OldEnd = new Point(CurrentX, CurrentY);  // previous current point

        MouseMoved(e);                     // do MouseMoved() stuff

        Point NewEnd = e.getPoint();       // new current point
        Point2D Start;

        if (StartPort != null)      // if starting port is valid
        {
            // we are dragging a new arrow

            Start = toScreen(StartPort.getLocation(null));  // get starting point of an arrow

            Graphics g = getGraphics();

            g.setColor(Configuration.JGRAPH_DEFAULT_EDGE_COLOR);    // erase arrow, painted on the previous step
            g.setXORMode(getBackground());
            g.drawLine((int)Start.getX(), (int)Start.getY(), (int)OldEnd.getX(), (int)OldEnd.getY());

            g.setColor(getBackground());                     // draw a new one
            g.setXORMode(Configuration.JGRAPH_DEFAULT_EDGE_COLOR);
            g.drawLine((int)Start.getX(), (int)Start.getY(), (int)NewEnd.getX(), (int)NewEnd.getY());

            e.consume();
        }
    }
//------------------------------------------------------------------------
    public void DeleteSelection()       // delete all selected cells and attached arrows
    {                                	// known issue: when deleting curved arrow, should straighten remaining one
        if (!isSelectionEmpty())        // if some cells are selected
        {
            Object[] cells = getSelectionCells();     // get them
            cells = getDescendants(cells);            // get also their descendants

            var v = new Vector<Object>();                  // insert all cells into vector (except VarsCell)
            
            for (var c : cells)
                if (c != VarsCell)
                    v.add(c);

            for (var p : v)
                if (p instanceof DefaultPort)
                {
                    var e = ((DefaultPort)p).edges();    // add also attached edges
                    while (e.hasNext())
                        v.add(e.next());
                }

            getModel().remove(v.toArray());           // remove objects
        }
    }
//------------------------------------------------------------------------
    public void MarkAsStarting()	// mark the selected cell as starting
    {
    	Object[] cells = getSelectionCells();

        // if the operation is legal
        if (cells.length == 1 && cells[0] instanceof MyGraphCell && cells[0] != VarsCell)
            markAsStarting(cells[0]);
    }
//------------------------------------------------------------------------
    private void markAsStarting(Object cell)  // mark the given cell as starting
    {
    	var nest = new HashMap<DefaultGraphCell, AttributeMap>();            	
    	var attr = new AttributeMap();
        GraphConstants.setBorderColor(attr, Configuration.JGRAPH_DEFAULT_BORDER_COLOR);

        for (var r : getRoots())						// get all cells in the graph
            if (r instanceof MyGraphCell)
                nest.put((MyGraphCell)r, attr);         // mark all cells as NOT starting

        getModel().edit(nest, null, null, null);  // apply changes

        nest = new HashMap<DefaultGraphCell, AttributeMap>();
        attr = new AttributeMap();
        GraphConstants.setBorderColor(attr, Configuration.EJGRAPH_SELECTED_BORDER_COLOR);
        nest.put((MyGraphCell)cell, attr);
        
        if (cell != VarsCell)                          // mark selected cell as starting
            getModel().edit(nest, null, null, null);
    }
//------------------------------------------------------------------------
    public MyGraphCell AddBlock(int type, String text)   // add a block of the given type with the given label
    {
        MyGraphCell c = InsertBlock(CurrentX, CurrentY, type, text, 
        		                    Configuration.EJGRAPH_DEFAULT_CELL_WIDTH, Configuration.EJGRAPH_DEFAULT_CELL_HEIGHT);
        if (type != MyGraphCell.BEGIN_END)   // edit any inserted block (except END)
            editCell(c);
        
        return c;
    }
//------------------------------------------------------------------------
    public Vector<String> GetVarsAsVector()      // get variables (as a vector) from the local variables frame
    {
    	return new EJGraphCodeVector(this).GetVarsAsVector();
    }
//------------------------------------------------------------------------
    public Vector<String> GetCodeAsVector() throws SyntaxErrorException // get the IL program as a vector
    {
    	return new EJGraphCodeVector(this).GetCodeAsVector();
    }
//------------------------------------------------------------------------
    public boolean IsOverPort()                // is the mouse pointer over port or not
    {
    	return IsOverPort;	
    }
//------------------------------------------------------------------------
}
