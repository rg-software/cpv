package cpv.Runner;

import java.awt.*;
import java.io.*;
import cpv.*;

////////////////////////////////////////////////////////////////////////////////
// the simplest layouting manager: determines a point for the next cell to be created
////////////////////////////////////////////////////////////////////////////////
public class GraphLayoutManager
{
    static private int px = 0, py = 0;         // current point coordinates
    static private boolean is_first = true;    // is the next cell to be created is the first one?

////////////////////////////////////////////////////////////////////////////////
    static void Initialize()                   // call this routine before creating the first cell
    {
    	px = 0; py = 0;
    	is_first = true;
    }
////////////////////////////////////////////////////////////////////////////////
    static public Point GetNextPoint()         // returns the point for the next cell
    {
        if(is_first)
        {
            is_first = false;
            return new Point(px, py);
        }

        if(px > py)
        {
            px = 0; py += Configuration.LAYOUTMANAGER_YDISPLACEMENT;
        }
        else
            px += Configuration.LAYOUTMANAGER_XDISPLACEMENT;

        return new Point(px, py);
    }
////////////////////////////////////////////////////////////////////////////////
    static public void SaveToFile(PrintWriter enc) throws java.io.IOException  // save an object to stream
    {
        enc.println(px);
        enc.println(py);
        enc.println(is_first);
    }
////////////////////////////////////////////////////////////////////////////////
    // load an object from stream
    static public void LoadFromFile(BufferedReader dec) throws java.io.IOException
    {
    	px = Integer.parseInt(dec.readLine());
    	py = Integer.parseInt(dec.readLine());
    	is_first = Boolean.getBoolean(dec.readLine());
    }
////////////////////////////////////////////////////////////////////////////////
}