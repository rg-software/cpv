package cpv.MyJGraph;

import java.awt.geom.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import org.jgraph.graph.*;
import org.jgraph.*;

//------------------------------------------------------------------------
// custom edge renderer (refer to JGraph documentation for details)
//------------------------------------------------------------------------
class MyEdgeRenderer extends EdgeRenderer
{
    protected transient GeneralPath Path = new GeneralPath(GeneralPath.WIND_NON_ZERO);   // arrow path

//------------------------------------------------------------------------
    public MyEdgeRenderer()
    {
    }
//------------------------------------------------------------------------
    protected Shape createShape()                              // create an arrow
    {
        final double loopcoeff = 1.5;
        final double loopangle = Math.PI / 6;
        
        //Point SrcPoint = ((PortView)view.getSource()).getLocation(null);
        Point2D DestPoint = ((PortView)view.getTarget()).getLocation(null);         // get destination point
        //MyVertexView SrcView = (MyVertexView)view.getSource().getParentView();
        MyVertexView DestView = (MyVertexView)view.getTarget().getParentView();   // get destination view

        String lab = view.toString();// correct?
        //String lab = ((JGraph)this.graph.get()).convertValueToString(view).toString();       // get edge label
        Point2D P0 = view.getPoint(view.getPointCount() - 2);       // get two last points
        Point2D P1 = view.getPoint(view.getPointCount() - 1);
        Point PM;                                                 // middle points

        Path.reset();
        
        if(view.getSource() == view.getTarget()) // loop-styled edge
        {
        	// trying to find the current edge among all loop edges
        	var e = ((Port)view.getSource().getCell()).edges();
        	int LoopEdgesCount = 0;
        	
        	while(e.hasNext())
        	{
        		if(e.next() == view.getCell())  // found, exiting
        			break;
				LoopEdgesCount++;
			}
        	
        	Rectangle2D r = DestView.getBounds();
            Point centre = new Point((int)(r.getX() + r.getWidth() / 2), (int)(r.getY() + r.getHeight() / 2));
			double L = Math.sqrt(r.getWidth() * r.getWidth() + r.getHeight() * r.getHeight());
			
			P0 = DestView.getPerimeterPoint(null, new Point((int)(centre.x + L * Math.cos(loopangle * LoopEdgesCount)), (int)(centre.y + L * Math.sin(loopangle * LoopEdgesCount))));
			P1 = DestView.getPerimeterPoint(null, new Point((int)(centre.x + L * Math.cos(loopangle * (LoopEdgesCount + 1))), (int)(centre.y + L * Math.sin(loopangle * (LoopEdgesCount + 1)))));
			PM = new Point((int)(centre.x + loopcoeff * r.getWidth() * Math.cos(loopangle * (LoopEdgesCount + 0.5))), (int)(centre.y + loopcoeff * r.getWidth() * Math.sin(loopangle * (LoopEdgesCount + 0.5))));
        }
        else if(lab.equals("single"))  // in case of single edge make it straight
        {
	        // only straight line supports middle points
	        Path.moveTo(view.getPoint(0).getX(), view.getPoint(0).getY());    // move to the first point

    	    for(int i = 1; i < view.getPointCount() - 1; i++)
        	    Path.lineTo(view.getPoint(i).getX(), view.getPoint(i).getY());   // make a segmented line to the point number N_of_points - 2

            PM = new Point((int)(P0.getX() + P1.getX()) / 2, (int)(P0.getY() + P1.getY()) / 2);     // calculate the middle point
        }
        else                      // curved arrow
        {
            double R = Math.sqrt((P1.getX() - P0.getX())*(P1.getX() - P0.getX()) + (P1.getY() - P0.getY())*(P1.getY() - P0.getY()));
            double alpha = Math.atan2(P1.getY() - P0.getY(), P1.getX() - P0.getX());
            double Xc = (P0.getX() + P1.getX()) / 2, Yc = (P0.getY() + P1.getY()) / 2;
            double XM = Xc + (int)((R / 4) * Math.cos(alpha + Math.PI / 2));
            double YM = Yc + (int)((R / 4) * Math.sin(alpha + Math.PI / 2));

            PM = new Point((int)XM, (int)YM);                                // calculate the middle point
            // P0 = SrcView.getPerimeterPoint(PM, SrcPoint);
            // TODO(mm): use getPerimeterPoint(EdgeView edge, Point2D source, Point2D p)
            P1 = DestView.getPerimeterPoint(DestPoint, PM);        // recalculate the last point
        }

        Path.moveTo(P0.getX(), P0.getY());
        Path.quadTo(PM.x, PM.y, P1.getX(), P1.getY());              // use curved route

        Point p = new Point((int)P0.getX(), (int)P0.getY());
        view.beginShape = new Rectangle(p);                 // create arrow starting shape
        view.endShape = createLineEnd(endSize, endDeco, PM, P1);    // create arrow ending
        view.lineShape = (Shape)Path.clone();
        Path.append(view.endShape, true);

        return Path;                                           // return the ready arrow
    }
//------------------------------------------------------------------------
    protected void paintLabel(Graphics g, String label)  // no label to paint, so just exit
    {
    }
//------------------------------------------------------------------------
}
