package cpv.MyJGraph;

import java.awt.*;
import java.awt.geom.*;
import org.jgraph.graph.*;

//------------------------------------------------------------------------
// custom vertex view (refer to JGraph documentation for details)
//------------------------------------------------------------------------
public class MyVertexView extends VertexView
{
    private MyGraphCell Vertex;                                       // vertex itself
    static EllipseRenderer BeginEndRenderer = new EllipseRenderer();  // two custom renderers
    static DiamondRenderer BranchingRenderer = new DiamondRenderer();
//------------------------------------------------------------------------
    public MyVertexView(Object v)
    {
        super(v);
        Vertex = (MyGraphCell)v;
    }
//------------------------------------------------------------------------
    public int GetVertexType()                                       // return type of the vertex
    {
    	return Vertex.getType();
    }
//------------------------------------------------------------------------
    public CellViewRenderer getRenderer()             // standard VertexView interface function
    {
        switch (Vertex.getType())
        {
            case MyGraphCell.GENERIC: return super.getRenderer();     // default rectangular renderer
            case MyGraphCell.BRANCHING: return BranchingRenderer;
            case MyGraphCell.BEGIN_END: return BeginEndRenderer;
        }

        return null;
    }
//------------------------------------------------------------------------
    private Point2D gppBEGIN_END(Point2D source, Point2D p)   // getPerimeterPoint() taken from JGraphPad
    {                                                   	  // used for elliptic nodes
        Rectangle2D r = getBounds();

        double x = r.getX();
        double y = r.getY();
        double a = (r.getWidth() + 1) / 2;
        double b = (r.getHeight() + 1) / 2;

        // x0,y0 - center of ellipse
        double x0 = x + a;
        double y0 = y + b;

        // x1, y1 - point
        double x1 = p.getX();
        double y1 = p.getY();

        // calculate straight line equation through point and ellipse center
        // y = d * x + h
        double dx = x1 - x0;
        double dy = y1 - y0;

        if (dx == 0)
            return new Point((int) x0, (int) (y0 + b * dy / Math.abs(dy)));

        double d = dy / dx;
        double h = y0 - d * x0;

        // calculate intersection
        double e = a * a * d * d + b * b;
        double f = - 2 * x0 * e;
        double g = a * a * d * d * x0 * x0 + b * b * x0 * x0 - a * a * b * b;

        double det = Math.sqrt(f * f - 4 * e * g);

        // two solutions (perimeter points)
        double xout1 = (-f + det) / (2 * e);
        double xout2 = (-f - det) / (2 * e);
        double yout1 = d * xout1 + h;
        double yout2 = d * xout2 + h;

        double dist1 = Math.sqrt(Math.pow((xout1 - x1), 2)
                                 + Math.pow((yout1 - y1), 2));
        double dist2 = Math.sqrt(Math.pow((xout2 - x1), 2)
                                 + Math.pow((yout2 - y1), 2));

        // correct solution
        double xout, yout;

        if (dist1 < dist2)
        {
            xout = xout1;
            yout = yout1;
        }
        else
        {
            xout = xout2;
            yout = yout2;
        }

        return new Point((int)xout, (int)yout);
    }
//------------------------------------------------------------------------
    public Point2D getPerimeterPoint(EdgeView edge, Point2D source, Point2D p) // returns a point on a border of a node
    {                                                         				   // between two given points (inside and outside)
    	switch (Vertex.getType())
        {
            case MyGraphCell.GENERIC:   var p2d = super.getPerimeterPoint(null, source, p);
            							return new Point((int)p2d.getX(), (int)p2d.getY());
            case MyGraphCell.BRANCHING: return source;                              // just a source point in case of a diamond node
            case MyGraphCell.BEGIN_END: return gppBEGIN_END(source, p);
        }

        return new Point();
    }
////////////////////////////////////////////////////////////////////////////////
}