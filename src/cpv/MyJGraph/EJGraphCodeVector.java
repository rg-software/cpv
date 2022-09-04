package cpv.MyJGraph;

import java.util.Vector;
import org.jgraph.graph.*;
import cpv.*;

public class EJGraphCodeVector 
{
	EJGraph graph;
	
//------------------------------------------------------------------------
	public EJGraphCodeVector(EJGraph graph)
	{
		this.graph = graph;
	}
	
//------------------------------------------------------------------------
    // move the starting state to the first position of cells array
    private void setStartingStateFirst(Vector<MyGraphCell> cells) throws SyntaxErrorException
    {
        int counter = 0, index = -1;

        for (int i = 0; i < cells.size(); i++)  // search for the starting state
            if (GraphConstants.getBorderColor(cells.get(i).getAttributes()).equals(Configuration.EJGRAPH_SELECTED_BORDER_COLOR))
            {
                if (++counter > 1)
                    throw new SyntaxErrorException("Multiple starting states found");
                index = i;
            }

        if (counter == 0)
        	throw new SyntaxErrorException("No starting states found");

        MyGraphCell c = cells.get(index);  // make the starting state the first cell in array
        cells.remove(index);
        cells.add(0, c);
    }
//------------------------------------------------------------------------
    private void setFinalStateLast(Vector<MyGraphCell> cells) throws SyntaxErrorException
    {
        int counter = 0, index = -1;

        for (int i = 0; i < cells.size(); i++)  // searching the final state
            if (cells.get(i).getType() == MyGraphCell.BEGIN_END)
            {
                if (++counter > 1)
                	throw new SyntaxErrorException("Multiple final states found");

                index = i;
            }

        if (counter == 1)
        {
        	MyGraphCell c = cells.get(index);    // make the final state the last cell in array
        	cells.remove(index);
        	cells.add(c);
        }
    }
//------------------------------------------------------------------------
    // get destination cell for the given source port (pointed by an arrow)
    private MyGraphCell getDestination(DefaultPort port) throws SyntaxErrorException
    {
        var e = port.edges();        // get all outgoing edges of the port
        MyGraphCell result = null;
        int OutgoingEdges = 0;

        while (e.hasNext())
        {
            DefaultEdge edge = (DefaultEdge)e.next();

            if (edge.getTarget() != port) // outgoing edge found
            {
            	result = (MyGraphCell)graph.getModel().getParent(edge.getTarget());
            	OutgoingEdges++;
            }
        }

        if (OutgoingEdges != 1) // every port can have only one outgoing edge; raise an exception otherwise
            throw new SyntaxErrorException("Block " + graph.convertValueToString((MyGraphCell)graph.getModel().getParent(port)) + 
            		                       " has an incorrect number of outgoing edges");

        return result;
    }
//------------------------------------------------------------------------
    public Vector<String> GetCodeAsVector() throws SyntaxErrorException
    {
        var cells = new Vector<MyGraphCell>(); // graph cells
        var lines = new Vector<String>();      // resulting lines

        for (int i = 0; i < graph.getModel().getRootCount(); i++)  // put all cells into array
        {
            Object c = graph.getModel().getRootAt(i);
            if (c instanceof MyGraphCell && c != graph.GetVarsCell())   // except VarsCell, naturally
                cells.add((MyGraphCell)c);
        }

        setStartingStateFirst(cells);   // rearrange important states
        setFinalStateLast(cells);

        for (MyGraphCell c : cells)
        {
            if (c.getType() == MyGraphCell.BEGIN_END)     // if some cell is END, add "__endproc"
                lines.add("__endproc");
            else							 // else get its IL value
                lines.add(Translator.getTranslator().TranslateConstruction(graph.convertValueToString(c)));
        }

        for (int i = 0; i < cells.size(); i++)       	// for each cell
        {
            MyGraphCell cell = cells.get(i);   			// analyse it

            if (cell.getType() == MyGraphCell.GENERIC) 	// ordinary block, only ONE goto
            {
                int index = cells.indexOf(getDestination(graph.GetPortByName(cell, "central")));  // get destination cell of the central port

                if (index == -1)
                	throw new SyntaxErrorException("Incorrect arrow target (cell: " + cell.toString() + ")");

                lines.set(i, lines.get(i) + " goto " + index);
            }
            else if (cell.getType() == MyGraphCell.BRANCHING)  // branching: two GOTOs
            {
                int no_idx = cells.indexOf(getDestination(graph.GetPortByName(cell, "left")));  // get two destination cells
                int yes_idx = cells.indexOf(getDestination(graph.GetPortByName(cell, "right")));

                if (no_idx == -1 || yes_idx == -1)
                	throw new SyntaxErrorException("Incorrect arrow target (cell: " + cell.toString() + ")");

                lines.set(i, lines.get(i) + " goto " + yes_idx + " else " + no_idx);
            }
            else if (cell.getType() == MyGraphCell.BEGIN_END)
            	; // do nothing
            else
            	throw new SyntaxErrorException("Unknown block type");
        }

        // add onscreen text (remark) to each line (syntax: IL_string//onscreen_text)
        for (int i = 0; i < cells.size(); i++)
            lines.set(i, lines.get(i) + "//" + graph.convertValueToString(cells.get(i)));

        return lines;
    }
//------------------------------------------------------------------------
    public Vector<String> GetVarsAsVector()      // get variables (as vector) from local variables frame
    {
        var v = new Vector<String>();
        String CellText = graph.convertValueToString(graph.GetVarsCell());    // text of the cell

        // get the substring between <html>...</html>
        String[] s = CellText.substring(6, CellText.length() - 7).split("<br>");

        if (!s[0].equals(""))      // if at least one variable available, fill vector
            for (var str : s)
                v.add(str);

        return v;
    }
//------------------------------------------------------------------------	
}
