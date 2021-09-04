package cpv.MyJGraph;

import com.jgraph.graph.EdgeView;
import com.jgraph.graph.*;
import com.jgraph.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

////////////////////////////////////////////////////////////////////////////////
// custom edge renderer (refer to JGraph documentation for details)
////////////////////////////////////////////////////////////////////////////////
class MyEdgeRenderer extends EdgeRenderer
{
    protected transient GeneralPath Path = new GeneralPath(GeneralPath.WIND_NON_ZERO);   // arrow path

////////////////////////////////////////////////////////////////////////////////
    public MyEdgeRenderer()
    {
    }
////////////////////////////////////////////////////////////////////////////////
    protected Shape createShape()                              // create an arrow
    {
        final double loopcoeff = 1.5;
        final double loopangle = Math.PI / 6;
        
        //Point SrcPoint = ((PortView)view.getSource()).getLocation(null);
        Point DestPoint = ((PortView)view.getTarget()).getLocation(null);         // get destination point
        //MyVertexView SrcView = (MyVertexView)view.getSource().getParentView();
        MyVertexView DestView = (MyVertexView)view.getTarget().getParentView();   // get destination view

        String lab = view.getGraph().convertValueToString(view).toString();       // get edge label
        Point P0 = view.getPoint(view.getPointCount() - 2);       // get two last points
        Point P1 = view.getPoint(view.getPointCount() - 1);
        Point PM;                                                 // middle points

        Path.reset();
        
        if(view.getSource() == view.getTarget()) // loop-styled edge
        {
        	// trying to find the current edge among all loop edges
        	Iterator e = ((Port)view.getSource().getCell()).edges();
        	int LoopEdgesCount = 0;
        	
        	while(e.hasNext())
        	{
        		if(e.next() == view.getCell())  // found, exiting
        			break;
				LoopEdgesCount++;
			}
        	
        	Rectangle r = DestView.getBounds();
            Point centre = new Point((int)(r.getX() + r.getWidth() / 2), (int)(r.getY() + r.getHeight() / 2));
			double L = Math.sqrt(r.getWidth() * r.getWidth() + r.getHeight() * r.getHeight());
			
			P0 = DestView.getPerimeterPoint(null, new Point((int)(centre.x + L * Math.cos(loopangle * LoopEdgesCount)), (int)(centre.y + L * Math.sin(loopangle * LoopEdgesCount))));
			P1 = DestView.getPerimeterPoint(null, new Point((int)(centre.x + L * Math.cos(loopangle * (LoopEdgesCount + 1))), (int)(centre.y + L * Math.sin(loopangle * (LoopEdgesCount + 1)))));
			PM = new Point((int)(centre.x + loopcoeff * r.getWidth() * Math.cos(loopangle * (LoopEdgesCount + 0.5))), (int)(centre.y + loopcoeff * r.getWidth() * Math.sin(loopangle * (LoopEdgesCount + 0.5))));
        }
        else if(lab.equals("single"))  // in case of single edge make it straight
        {
	        // only straight line supports middle points
	        Path.moveTo(view.getPoint(0).x, view.getPoint(0).y);    // move to the first point

    	    for(int i = 1; i < view.getPointCount() - 1; i++)
        	    Path.lineTo(view.getPoint(i).x, view.getPoint(i).y);   // make a segmented line to the point number N_of_points - 2

            PM = new Point((P0.x + P1.x) / 2, (P0.y + P1.y) / 2);     // calculate the middle point
        }
        else                      // curved arrow
        {
            double R = Math.sqrt((P1.x - P0.x)*(P1.x - P0.x) + (P1.y - P0.y)*(P1.y - P0.y));
            double alpha = Math.atan2(P1.y - P0.y, P1.x - P0.x);
            int Xc = (P0.x + P1.x) / 2, Yc = (P0.y + P1.y) / 2;
            int XM = Xc + (int)((R / 4) * Math.cos(alpha + Math.PI / 2));
            int YM = Yc + (int)((R / 4) * Math.sin(alpha + Math.PI / 2));

            PM = new Point(XM, YM);                                // calculate the middle point
            // P0 = SrcView.getPerimeterPoint(PM, SrcPoint);
            P1 = DestView.getPerimeterPoint(DestPoint, PM);        // recalculate the last point
        }

        Path.moveTo(P0.x, P0.y);
        Path.quadTo(PM.x, PM.y, P1.x, P1.y);                   // use curved route

        beginShape = new Rectangle(P0);                        // create arrow starting shape
        endShape = createLineEnd(endSize, endDeco, PM, P1);    // create arrow ending
        lineShape = (Shape)Path.clone();
        Path.append(endShape, true);

        return Path;                                           // return the ready arrow
    }
////////////////////////////////////////////////////////////////////////////////
    protected void paintLabel(Graphics g, String label)  // no label to paint, so just exit
    {
    }
////////////////////////////////////////////////////////////////////////////////
}
