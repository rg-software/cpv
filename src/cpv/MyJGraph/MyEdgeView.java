package cpv.MyJGraph;

import java.awt.event.MouseEvent;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.*;
import org.jgraph.*;

//------------------------------------------------------------------------
// custom edge view (refer to JGraph documentation for details)
//------------------------------------------------------------------------
public class MyEdgeView extends EdgeView
{
    static MyEdgeRenderer renderer = new MyEdgeRenderer();

//------------------------------------------------------------------------
    public MyEdgeView(Object cell, JGraph graph, CellMapper mapper)     // standard MyEdgeView functions
    {
        super(cell);
        //, graph, mapper);
    }
//------------------------------------------------------------------------
    public CellViewRenderer getRenderer()
    {
        return renderer;
    }
//------------------------------------------------------------------------
    public boolean isAddPointEvent(MouseEvent event)
    {
    	return event.isShiftDown();         // shift + click is used for add/remove points
    }
//------------------------------------------------------------------------
    public boolean isRemovePointEvent(MouseEvent event)
    {
    	return event.isShiftDown();
    }
//------------------------------------------------------------------------
}
