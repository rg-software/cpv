package cpv.MyJGraph;

import cpv.*;
import org.jgraph.JGraph;
import javax.accessibility.*;
import org.jgraph.graph.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.beans.*;

////////////////////////////////////////////////////////////////////////////////
// JGraph extension for flowchart editing
////////////////////////////////////////////////////////////////////////////////
public class EJGraph extends GenericJGraph
{
    private int CurrentX = 0, CurrentY = 0;         // current coordinates of mouse pointer
    private PortView StartPort = null;              // current source port (for arrow creation)
    private MyGraphCell VarsCell = null;            // reference to Local Variables Vertex
    private boolean IsOverPort = false;              // is the mouse pointer over port or not

////////////////////////////////////////////////////////////////////////////////
    public EJGraph()                 // make a new instance of EJGraph
    {
    	// create an empty Local Variables Vertex
        VarsCell = InsertBlock(CurrentX, CurrentY, MyGraphCell.GENERIC, "             ", Configuration.EJGRAPH_DEFAULT_CELL_WIDTH, Configuration.EJGRAPH_DEFAULT_CELL_HEIGHT); // space for "<html></html>"
        VarsCell.setTag(MyGraphCell.VARS_CELL);      // cell type = VARS_CELL
        setMarqueeHandler(new EJGraphMarqueeHandler(this));     // attach custom marquee handler
    }
////////////////////////////////////////////////////////////////////////////////
    // load EJGraph from input stream (override GenericJGraph implementation)
    public void LoadFromFile(BufferedReader dec) throws java.io.IOException
    {
        super.LoadFromFile(dec);
        Object[] roots = getRoots();       // get all cells of the graph

        for(int i = 0; i < roots.length; i++)       // finding Vars Cell Vertex
            if(roots[i] instanceof MyGraphCell)
                if(((MyGraphCell)roots[i]).getTag() == MyGraphCell.VARS_CELL)
                    VarsCell = (MyGraphCell)roots[i];    // validate VarsCell reference
    }
////////////////////////////////////////////////////////////////////////////////
    public void EditVarsCell()       // convenience function: shows Local Variables Editor box
    {
        EditCell(VarsCell);
    }
////////////////////////////////////////////////////////////////////////////////
    private void EditCell(Object cell)     // shows an editor box for a given cell
    {
        // if the given cell is not END (END block edition is forbidden), use cell editor
        if(((MyGraphCell)cell).getType() != MyGraphCell.BEGIN_END)
        {
        	BlockEditor editor;

            //  if the given cell is the VarsCell use Local Variables Editor
            if((MyGraphCell)cell == VarsCell)
                editor = new LocalVarsEditor(null, "Local Variables Editor", true, convertValueToString(cell));
            else
                editor = new GenericBlockEditor(null, "Input Command", true, convertValueToString(cell));

            editor.setLocationRelativeTo(this);
            editor.show();
        
        	if(editor.OKPressed())             // if edition succeded
        	{
            	Map nest = new Hashtable();
            	Map attr = new Hashtable();//GraphConstants.createMap();
            	GraphConstants.setValue(attr, editor.GetResultingText());   // modify cell data
            	nest.put((MyGraphCell)cell, attr);        // (refer to JGraph documentation)
            	getModel().edit(nest, null, null, null);
        	}
        }
    }
////////////////////////////////////////////////////////////////////////////////
    public void MousePressed(MouseEvent e)         // mouse handler
    {
        if(SwingUtilities.isLeftMouseButton(e))
        {
            if(e.getClickCount() == 2) // on left-button doubleclick show cell editor box
            {
                Object cell = getFirstCellForLocation(e.getX(), e.getY());
                if (cell instanceof MyGraphCell)    // edit cell contents
                    EditCell(cell);
                e.consume();
            }
            else if(e.getClickCount() == 1)  // on single click get starting port
            {
                Point2D tmp = fromScreen(new Point(e.getPoint()));
                PortView pv = getPortViewAt(tmp.getX(), tmp.getY());        // get portview under mouse pointer
                // if there IS a port under mouse pointer and it is NOT VarsCell's port, make a reference to this port
                // StartPort is used for arrow creation; see also MouseDragged() and MouseReleased()
                StartPort = (pv != null && getModel().getParent((Port)pv.getCell()) != VarsCell) ? pv : null;
                if(StartPort != null)
                    e.consume();
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////
    public void MouseReleased(MouseEvent e)  // on mouse release possibly create an arrow
    {
        if(SwingUtilities.isLeftMouseButton(e))
        {
            Point2D tmp = fromScreen(new Point(e.getPoint()));
            PortView pv = getPortViewAt(tmp.getX(), tmp.getY());
            // get an EndPort - the port of the destination cell
            PortView EndPort = (pv != null && getModel().getParent((Port)pv.getCell()) != VarsCell) ? pv : null;

            if(StartPort != null && EndPort != null)  // if both ports are valid
            {
                // get corresponding vertices
                Port p1 = (Port)StartPort.getCell(), p2 = (Port)EndPort.getCell();

                // connect if ports belong to different cells
/*                if(getModel().getParent(p1) != getModel().getParent(p2))
                    connect(p1, p2);*/
                // now it is ALLOWED
                if(p1 != p2)
                    connect(p1, p2);
            }

            StartPort = EndPort = null;
        }
    }
////////////////////////////////////////////////////////////////////////////////
    public void MouseMoved(MouseEvent e)                       // OnMouseMove event handler
    {
        Point2D tmp = fromScreen(new Point(e.getPoint()));
        PortView pv = getPortViewAt(tmp.getX(), tmp.getY());             // get a port under mouse pointer

        CurrentX = e.getX();          // get current coordinates
        CurrentY = e.getY();

        if(pv != null)                // if the port is valid
        {
            Object cell = getModel().getParent((Port)pv.getCell());   // get a parental cell

            if(cell instanceof MyGraphCell && cell != VarsCell)       // if it is not VarsCell
            {
                IsOverPort = true;                                    // now we are "over port"
                setCursor(new Cursor(Cursor.HAND_CURSOR));            // change current cursor
                e.consume();
            }
        }
        else if(IsOverPort)   // else if we WERE "over port"
        {
            IsOverPort = false;
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // change cursor to default
            e.consume();
        }
    }
////////////////////////////////////////////////////////////////////////////////
    public void MouseDragged(MouseEvent e)       // OnMouseDragged handler
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
            g.drawLine(Start.getX(), Start.getY(), OldEnd.getX(), OldEnd.getY());

            g.setColor(getBackground());                     // draw a new one
            g.setXORMode(Configuration.JGRAPH_DEFAULT_EDGE_COLOR);
            g.drawLine(Start.x, Start.y, NewEnd.x, NewEnd.y);

            e.consume();
        }
    }
////////////////////////////////////////////////////////////////////////////////
    public void DeleteSelection()          // delete all selected cells and attached arrows
    {                                // known issue: when deleting curved arrow, should straighten remaining one
        if(!isSelectionEmpty())         // if some cells are selected
        {
            Object[] cells = getSelectionCells();     // get them
            cells = getDescendants(cells);            // get also their descendants

            Vector v = new Vector();                  // insert all cells into vector (except VarsCell)
            for(int i = 0; i < cells.length; i++)
                if(cells[i] != VarsCell)
                    v.add(cells[i]);

            int N = v.size();
            for(int i = 0; i < N; i++)
                if(v.get(i) instanceof DefaultPort)
                {
                    Iterator e = ((DefaultPort)v.get(i)).edges();    // add also attached edges
                    while(e.hasNext())
                        v.add(e.next());
                }

            getModel().remove(v.toArray());           // remove objects
        }
    }
////////////////////////////////////////////////////////////////////////////////
    public void MarkAsStarting()	// mark selected cell as starting
    {
    	Object[] cells = getSelectionCells();

        // if the operation is legal
        if(cells.length == 1 && cells[0] instanceof MyGraphCell && cells[0] != VarsCell)
            MarkAsStarting(cells[0]);
    }
////////////////////////////////////////////////////////////////////////////////
    public void MarkAsStarting(Object cell)  // mark given cell as starting
    {
        Map nest = new Hashtable();
        Map attr = new Hashtable();//GraphConstants.createMap();
        GraphConstants.setBorderColor(attr, Configuration.JGRAPH_DEFAULT_BORDER_COLOR);

        Object[] roots = getRoots();                 // get all cells in the graph
        for(int i = 0; i < roots.length; i++)
            if(roots[i] instanceof MyGraphCell)
                nest.put(roots[i], attr);         // mark all cells as NOT starting

        getModel().edit(nest, null, null, null);  // apply changes

        nest = new Hashtable();
        attr = new Hashtable();//GraphConstants.createMap();
        GraphConstants.setBorderColor(attr, Configuration.EJGRAPH_SELECTED_BORDER_COLOR);
        nest.put(cell, attr);
        if(cell != VarsCell)                          // mark selected cell as starting
            getModel().edit(nest, null, null, null);
    }
////////////////////////////////////////////////////////////////////////////////
    public MyGraphCell AddBlock(int type, String text)   // for convenience: add a block of a given type with a given label
    {
        MyGraphCell c = InsertBlock(CurrentX, CurrentY, type, text, Configuration.EJGRAPH_DEFAULT_CELL_WIDTH, Configuration.EJGRAPH_DEFAULT_CELL_HEIGHT);
        if(type != MyGraphCell.BEGIN_END)   // edit any inserted block (except END)
            EditCell(c);
        return c;
    }
////////////////////////////////////////////////////////////////////////////////
    public Vector GetVarsAsVector()      // get variables (as vector) from local variables frame
    {
        Vector v = new Vector();
        String CellText = convertValueToString(VarsCell);    // text of the cell

        // get substring between <html>...</html>
        String[] s = CellText.substring(6, CellText.length() - 7).split("<br>");

        if(!s[0].equals(""))      // if at least one variable available, fill vector
            for(int i = 0; i < s.length; i++)
                v.add(s[i]);

        return v;
    }
////////////////////////////////////////////////////////////////////////////////
    // get destination cell for given source port (pointed by an arrow)
    private MyGraphCell GetDestination(DefaultPort port) throws SyntaxErrorException
    {
        Iterator e = port.edges();        // get all outgoing edges of the port
        MyGraphCell result = null;
        int OutgoingEdges = 0;

        while(e.hasNext())
        {
            DefaultEdge edge = (DefaultEdge)e.next();

            if(edge.getTarget() != port) // outgoing edge found
            {
            	result = (MyGraphCell)getModel().getParent(edge.getTarget());
            	OutgoingEdges++;
            }
        }

        if(OutgoingEdges != 1) // every port can have only one outgoing edge; raise an exception otherwise
            throw new SyntaxErrorException("Block " + convertValueToString((MyGraphCell)getModel().getParent(port)) + " has incorrect number of outgoing edges");

        return result;
    }
////////////////////////////////////////////////////////////////////////////////
    // move starting state to the first position of cells array
    private void SetStartingStateFirst(Vector cells) throws SyntaxErrorException
    {
        int counter = 0, index = -1;

        for(int i = 0; i < cells.size(); i++)  // search for statring state
            if(GraphConstants.getBorderColor(((MyGraphCell)cells.get(i)).getAttributes()).equals(Configuration.EJGRAPH_SELECTED_BORDER_COLOR))
            {
                if(++counter > 1)
                    throw new SyntaxErrorException("Multiple starting states found");
                index = i;
            }

        if(counter == 0)
            throw new SyntaxErrorException("No starting states found");

        MyGraphCell c = (MyGraphCell)cells.get(index);  // make starting state first cell in array
        cells.remove(index);
        cells.add(0, c);
    }
////////////////////////////////////////////////////////////////////////////////
    private void SetFinalStateLast(Vector cells) throws SyntaxErrorException
    {
        int counter = 0, index = -1;

        for(int i = 0; i < cells.size(); i++)  // searching final state
            if(((MyGraphCell)cells.get(i)).getType() == MyGraphCell.BEGIN_END)
            {
                if(++counter > 1)
                	throw new SyntaxErrorException("Multiple final states found");

                index = i;
            }

        if(counter == 1)
        {
        	MyGraphCell c = (MyGraphCell)cells.get(index);    // make final state last cell in array
        	cells.remove(index);
        	cells.add(c);
        }
    }
////////////////////////////////////////////////////////////////////////////////
    // get IL program as vector
    public Vector GetCodeAsVector() throws SyntaxErrorException
    {
        Vector cells = new Vector();      // graph cells
        Vector lines = new Vector();      // resulting lines

        for(int i = 0; i < getModel().getRootCount(); i++)  // put all cells into array
        {
            Object c = getModel().getRootAt(i);
            if(c instanceof MyGraphCell && c != VarsCell)   // except VarsCell, naturally
                cells.add(c);
        }

        SetStartingStateFirst(cells);   // rearrange important states
        SetFinalStateLast(cells);

    	for(int i = 0; i < cells.size(); i++)
        {
            MyGraphCell c = (MyGraphCell)cells.get(i);

            if(c.getType() == MyGraphCell.BEGIN_END)     // if some cell is END, add "__endproc"
                lines.add("__endproc");
            else							 // else get its IL value
                lines.add(Translator.getTranslator().TranslateConstruction(convertValueToString(c)));
        }

        for(int i = 0; i < cells.size(); i++)        // for each cell
        {
            MyGraphCell cell = (MyGraphCell)cells.get(i);   // analyse it

            if(cell.getType() == MyGraphCell.GENERIC) // ordinary block, only ONE goto
            {
                int index = cells.indexOf(GetDestination(GetPortByName(cell, "central")));  // get destination cell of the central port

                if(index == -1)
                	throw new SyntaxErrorException("Incorrect arrow target (cell: " + cell.toString() + ")");

                lines.set(i, lines.get(i) + " goto " + index);
            }
            else if(cell.getType() == MyGraphCell.BRANCHING)  // branching: two GOTOs
            {
                int no_idx = cells.indexOf(GetDestination(GetPortByName(cell, "left")));  // get two destination cells
                int yes_idx = cells.indexOf(GetDestination(GetPortByName(cell, "right")));

                if(no_idx == -1 || yes_idx == -1)
                	throw new SyntaxErrorException("Incorrect arrow target (cell: " + cell.toString() + ")");

                lines.set(i, lines.get(i) + " goto " + yes_idx + " else " + no_idx);
            }
            else if(cell.getType() == MyGraphCell.BEGIN_END)
            	; // do nothing
            else
            	throw new SyntaxErrorException("Unknown block type");
        }

        // add onscreen text (remark) to each line (syntax: IL_string//onscreen_text)
        for(int i = 0; i < cells.size(); i++)
            lines.set(i, lines.get(i) + "//" + convertValueToString(cells.get(i)));

        return lines;
    }
////////////////////////////////////////////////////////////////////////////////
    public boolean IsOverPort()                // is the mouse pointer over port or not
    {
    	return IsOverPort;	
    }
////////////////////////////////////////////////////////////////////////////////
}


