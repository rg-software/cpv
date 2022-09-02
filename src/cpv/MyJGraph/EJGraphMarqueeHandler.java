package cpv.MyJGraph;

import java.awt.event.MouseEvent;
import org.jgraph.graph.BasicMarqueeHandler;

//------------------------------------------------------------------------
// custom marquee handler (refer to JGraph documentation for details)
//------------------------------------------------------------------------
public class EJGraphMarqueeHandler extends BasicMarqueeHandler
{
    private EJGraph graph;

//------------------------------------------------------------------------
    EJGraphMarqueeHandler(EJGraph thegraph)   // store a reference to graph in constructor
    {
        graph = thegraph;
    }
//------------------------------------------------------------------------
    public boolean isForceMarqueeEvent(MouseEvent e)   // if cursor is over port or double-click occured,
    {                                                  // use our handler, otherwise use standard
        if(graph.IsOverPort() || e.getClickCount() == 2)
            return true;
        return super.isForceMarqueeEvent(e);
    }
//------------------------------------------------------------------------
    public void mouseMoved(MouseEvent e)        // mouse handling functions
    {
        graph.MouseMoved(e);
        super.mouseMoved(e);
    }
//------------------------------------------------------------------------
    public void mousePressed(final MouseEvent e)
    {
        graph.MousePressed(e);
        super.mousePressed(e);
    }
//------------------------------------------------------------------------
    public void mouseDragged(MouseEvent e)
    {
        graph.MouseDragged(e);
        super.mouseDragged(e);
    }
//------------------------------------------------------------------------
    public void mouseReleased(MouseEvent e)
    {
        graph.MouseReleased(e);
        super.mouseReleased(e);
    }
//------------------------------------------------------------------------
}