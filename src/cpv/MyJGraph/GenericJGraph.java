package cpv.MyJGraph;

import org.jgraph.JGraph;
import org.jgraph.graph.*;
import java.io.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import cpv.*;

////////////////////////////////////////////////////////////////////////////////
// custom JGraph extension
////////////////////////////////////////////////////////////////////////////////
public class GenericJGraph extends JGraph
{
////////////////////////////////////////////////////////////////////////////////
    protected GenericJGraph()
    {
        super(new DefaultGraphModel());                 // use default graph model
        setEditable(false);                             // forbid editing
    }
////////////////////////////////////////////////////////////////////////////////
    protected VertexView createVertexView(Object v, CellMapper cm)   // provide a custom vertex view
    {
        return new MyVertexView(v, this, cm);
    }
////////////////////////////////////////////////////////////////////////////////
    protected EdgeView createEdgeView(Object e, CellMapper cm)  // provide also custom edge view
    {                                                           // (straight / curved)
        return new MyEdgeView(e, this, cm);
    }
////////////////////////////////////////////////////////////////////////////////
    // generic InsertBlock routine; returns just inserted block
    protected MyGraphCell InsertBlock(int X, int Y, int type, String text, int width, int height, Color bcolor)
    {
        MyGraphCell vertex = new MyGraphCell(type, text);       // create new block
        Hashtable attributes = new Hashtable();
 
        // create attributes
        if(type == MyGraphCell.BRANCHING)      // if this block is branching block
        {
        	int PERCENT = GraphConstants.PERMILLE / 10;
        	
            Map newmap = new Hashtable();//GraphConstants.createMap();
            GraphConstants.setOffset(newmap, new Point(PERCENT, PERCENT / 2));
            DefaultPort port = new DefaultPort("right");  // three ports will be available:
            attributes.put(port, newmap);                 // "right", "left" and "up"
            vertex.add(port);
            newmap = new Hashtable();//GraphConstants.createMap();
            GraphConstants.setOffset(newmap, new Point(0, PERCENT / 2));
            port = new DefaultPort("left");
            attributes.put(port, newmap);
            vertex.add(port);
            newmap = new Hashtable();//Constants.createMap();
            GraphConstants.setOffset(newmap, new Point(PERCENT / 2, 0));
            port = new DefaultPort("up");
            attributes.put(port, newmap);
            vertex.add(port);
        }
        else                                          // else we need central port only
            vertex.add(new DefaultPort("central"));

        // apply attributes
        Point2D point = snap(new Point(X, Y));
        Dimension size = new Dimension(width, height);
        Map map = new Hashtable();//GraphConstants.createMap();
        GraphConstants.setBounds(map, new Rectangle(new Point((int)point.getX(), (int)point.getY()), size));
        GraphConstants.setBorderColor(map, bcolor);
        GraphConstants.setBackground(map, Configuration.JGRAPH_DEFAULT_BACKGROUND_COLOR);
        GraphConstants.setOpaque(map, true);
        attributes.put(vertex, map);

        getModel().insert(new Object[]{vertex}, attributes, null, null, null);
        return vertex;
    }
////////////////////////////////////////////////////////////////////////////////
    // InsertBlock routine with default node border color (for convenience)
    protected MyGraphCell InsertBlock(int X, int Y, int type, String text, int width, int height)
    {
        return InsertBlock(X, Y, type, text, width, height, Configuration.JGRAPH_DEFAULT_BORDER_COLOR);
    }
////////////////////////////////////////////////////////////////////////////////
    private void InsertEdge(DefaultEdge edge, Port source, Port target)   // insert an edge between ports
    {
        ConnectionSet cs = new ConnectionSet();         // standard JGraph way (refer to documentation)
        cs.connect(edge, source, target);
        Map map = new Hashtable();//GraphConstants.createMap();
        GraphConstants.setLineEnd(map, GraphConstants.ARROW_TECHNICAL);
        Hashtable attributes = new Hashtable();
        attributes.put(edge, map);
        getModel().insert(new Object[]{edge}, attributes, cs, null, null);
    }
////////////////////////////////////////////////////////////////////////////////
    // create an edge between source and target; label is supplied by user
    public DefaultEdge connect(Port source, Port target, String label)
    {
        DefaultEdge edge = new DefaultEdge(label);
        InsertEdge(edge, source, target);

		return edge;
    }
////////////////////////////////////////////////////////////////////////////////
    // create an edge between source and target; label is determined automatically ("single", "left" or "right")
    public void connect(Port source, Port target)
    {
/*        if(source == target) // loop arrow
        {
        	connect(source, target, "loop");
        	return;
        }*/
        
        MyGraphCell srcCell = (MyGraphCell)getModel().getParent(source);   // get source cell

        // get edges, attached to the source cell
        Object[] edges = (DefaultGraphModel.getEdges(getModel(), new Object[]{srcCell})).toArray();
        DefaultEdge straight = null, reverse = null;

        for(int i = 0; i < edges.length; i++)    // trying to find existing edges between two given ports
        {
            DefaultEdge edge = (DefaultEdge)edges[i];
            DefaultPort srcPort = (DefaultPort)getModel().getSource(edge);
            DefaultPort trgPort = (DefaultPort)getModel().getTarget(edge);

            if(srcPort == source && trgPort == target)
                straight = edge;
            if(srcPort == target && trgPort == source)
                reverse = edge;
        }

        if(source == target) // loop edges are always allowed
        {
        	connect(source, target, "single");
        	return;
        }
        
        if(straight != null)   // connection is already exists; exit
            return;                                   

        if(straight == null && reverse == null)   // no connections detected - connecting
            connect(source, target, "single");

        if(straight == null && reverse != null)    // reverse connection exists:
        {
            getModel().remove(new Object[]{reverse});   // removing reverse edge
            connect(source, target, "left");            // create two curved edges
            connect(target, source, "right");
        }
    }
////////////////////////////////////////////////////////////////////////////////
    public void ClearGraph()      // delete all graph objects
    {
	     getModel().remove(getDescendants(getRoots()));
    }
////////////////////////////////////////////////////////////////////////////////
    public DefaultPort GetPortByName(MyGraphCell cell, String name)  // return desired port of a cell
    {
        ListIterator p = cell.getChildren().listIterator();

        while(p.hasNext())
        {
            DefaultPort port = (DefaultPort)p.next();
            if(convertValueToString(port).equals(name))
                return port;
        }

        return null;
    }
////////////////////////////////////////////////////////////////////////////////
    // load graph from file
    public void LoadFromFile(BufferedReader dec) throws java.io.IOException
    {
        ClearGraph();                                          // firstly clear it

        int cells = Integer.parseInt(dec.readLine());          // read number of vertices
        Hashtable ports = new Hashtable();                     // hashtable<Integer, Port>

        int idx = 0;
        for(int i = 0; i < cells; i++)                            // for each cell read values:
        {
            Color c = new Color(Integer.parseInt(dec.readLine())); // vertex color
            int tag = Integer.parseInt(dec.readLine());            // vertex tag
            int type = Integer.parseInt(dec.readLine());           // vertex type
            Rectangle r = new Rectangle();
            r.x = Integer.parseInt(dec.readLine());                // bounding rectangle
            r.y = Integer.parseInt(dec.readLine());
            r.height = Integer.parseInt(dec.readLine());
            r.width = Integer.parseInt(dec.readLine());
            String label = dec.readLine();                         // label

            // create vertex
            MyGraphCell cell = InsertBlock(r.x, r.y, type, label, r.width, r.height, c);
            cell.setTag(tag);    // write tag

            for(int j = 0; j < getModel().getChildCount(cell); j++)        // memorize ports
            {
                DefaultPort p = (DefaultPort)getModel().getChild(cell, j); // get j-th port
                ports.put(new Integer(idx++), p);
            }
        }

        int edges = Integer.parseInt(dec.readLine());            // read number of edges

        for(int i = 0; i < edges; i++)                         // for each edge read corresponding ports
        {
            DefaultPort src = (DefaultPort)ports.get(new Integer(Integer.parseInt(dec.readLine())));
            DefaultPort dest = (DefaultPort)ports.get(new Integer(Integer.parseInt(dec.readLine())));

            connect(src, dest);
	// TODO: somehow load and add middle points
        }
    }
////////////////////////////////////////////////////////////////////////////////
    // save graph to file
    public void SaveToFile(PrintWriter enc) throws java.io.IOException
    {
        Object[] objects = getDescendants(getRoots());    // get all graph cells
        Hashtable ports = new Hashtable();                // hashtable<Port, Integer>

        objects = getGraphLayoutCache().order(objects);   // reorder them
        Map viewAttributes = GraphConstants.createAttributes(objects, getGraphLayoutCache());  // get attributes

        int cells = 0;      // number of vertices
        int edges = 0;      // number of edges

        for(int i = 0; i < objects.length; i++)           // calculate these values
            if(objects[i] instanceof MyGraphCell)
                cells++;
        else if(objects[i] instanceof DefaultEdge)
            edges++;

        enc.println(cells);                           // write number of vertices

        int idx = 0;
        for(int i = 0; i < objects.length; i++)
            if(objects[i] instanceof MyGraphCell)     // for each vertex
            {
                Map attrs = (Map)viewAttributes.get(objects[i]);    // get parameters

                Rectangle2D r = GraphConstants.getBounds(attrs);              // bounding rectangle
                String lab = convertValueToString(objects[i]).toString();   // label
                int type = ((MyGraphCell)objects[i]).getType();                  // type
                int tag = ((MyGraphCell)objects[i]).getTag();                    // tag
                Color c = GraphConstants.getBorderColor(((MyGraphCell)objects[i]).getAttributes());  // color

                for(int j = 0; j < getModel().getChildCount(objects[i]); j++)
                {
                    DefaultPort p = (DefaultPort)getModel().getChild(objects[i], j); // get j-th port
                    ports.put(p, new Integer(idx++));                  // memorize ports
                }

                enc.println(c.getRGB());                    // write values
                enc.println(tag);
                enc.println(type);
                enc.println(r.x);
                enc.println(r.y);
                enc.println(r.height);
                enc.println(r.width);
                enc.println(lab);
            }

        enc.println(edges);           // write number of edges

        for(int i = 0; i < objects.length; i++)
            if(objects[i] instanceof DefaultEdge)      // for each edge
            {
                // write source port, target port, edge
                DefaultPort src = (DefaultPort)((DefaultEdge)objects[i]).getSource();
                DefaultPort dest = (DefaultPort)((DefaultEdge)objects[i]).getTarget();

                enc.println((Integer)ports.get(src));
                enc.println((Integer)ports.get(dest));

// commented: saving middle points of the edge
/*                EdgeView ev = (EdgeView)getGraphLayoutCache().getMapping(objects[i], true);

                enc.println(ev.getPointCount());
                for(int j = 0; j < ev.getPointCount(); j++)
                {
                    enc.println(ev.getPoint(j).x);
                    enc.println(ev.getPoint(j).y);
                }*/
            }
    }
////////////////////////////////////////////////////////////////////////////////
}