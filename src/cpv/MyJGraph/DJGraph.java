package cpv.MyJGraph;

import cpv.*;
import com.jgraph.JGraph;
import javax.accessibility.*;
import com.jgraph.graph.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;

import javax.swing.*;

////////////////////////////////////////////////////////////////////////////////
// JGraph extension for state space diagram drawing
////////////////////////////////////////////////////////////////////////////////
public class DJGraph extends GenericJGraph
{
    public DefaultPort insert(Point point, String text) // Insert a new vertex at the given point using given label
    {
        MyGraphCell cell = InsertBlock(point.x, point.y, MyGraphCell.BEGIN_END, text, Configuration.DJGRAPH_DEFAULT_CELL_WIDTH, Configuration.DJGRAPH_DEFAULT_CELL_HEIGHT);

        return (DefaultPort)getModel().getChild(cell, 0); // return a port of this vertex (get 0-th port)
    }
}