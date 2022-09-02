package cpv.MyJGraph;

import org.jgraph.graph.*;
import java.io.*;
import javax.swing.*;
import java.util.regex.*;

////////////////////////////////////////////////////////////////////////////////
// custom graph cell class (refer to JGraph documentation for details)
////////////////////////////////////////////////////////////////////////////////
public class MyGraphCell extends DefaultGraphCell
{
    public static final int BEGIN_END = 0, BRANCHING = 1, GENERIC = 2;    // block types
    public static final int NO_VALUE = 0, VARS_CELL = 1;                  // tag types; can be useful in the future

    private int Tag = NO_VALUE;
    private int type;

    public MyGraphCell(int celltype, String text)
    {
        super(text);
        type = celltype;
    }
    
    public int getTag()
    {
    	return Tag;
    }
    
    public void setTag(int newtag)
    {
    	Tag = newtag;
    }
    
    public int getType()
    {
    	return type;		
    }
}