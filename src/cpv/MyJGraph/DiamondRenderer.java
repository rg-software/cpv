package cpv.MyJGraph;

import com.jgraph.graph.VertexView;
import com.jgraph.*;
import com.jgraph.graph.*;
import java.awt.*;
import java.io.*;

////////////////////////////////////////////////////////////////////////////////
// Custom vertex renderer (refer to JGraph documentation for details)
// provides painting of diamond-styled (branching) blocks
////////////////////////////////////////////////////////////////////////////////
public class DiamondRenderer extends VertexRenderer
{
    public void paint(Graphics g)       // repaint vertex
    {
        int b = borderWidth;
        Graphics2D g2 = (Graphics2D)g;
        Dimension d = getSize();
        boolean tmp = selected;

        if (super.isOpaque())                     // draw a diamond
        {
            g.setColor(super.getBackground());
            g.drawLine(b - 1, (d.height - 1) / 2, (d.width - 1) / 2, b - 1);
            g.drawLine((d.width - 1) / 2, b - 1, d.width - b, (d.height - 1) / 2);
            g.drawLine(d.width - b, (d.height - 1) / 2, (d.width - 1) / 2, d.height - b);
            g.drawLine((d.width - 1) / 2, d.height - b, b - 1, (d.height - 1) / 2);
        }

        try
        {
            setBorder(null);
            setOpaque(false);
            selected = false;

            super.paint(g);
        }
        finally
        {
            selected = tmp;
        }

        if (bordercolor != null)     // draw a border
        {
            g.setColor(bordercolor);
            g2.setStroke(new BasicStroke(b));
            g.drawLine(b - 1, (d.height - 1) / 2, (d.width - 1) / 2, b - 1);
            g.drawLine((d.width - 1) / 2, b - 1, d.width - b, (d.height - 1) / 2);
            g.drawLine(d.width - b, (d.height - 1) / 2, (d.width - 1) / 2, d.height - b);
            g.drawLine((d.width - 1) / 2, d.height - b, b - 1, (d.height - 1) / 2);

            // print "yes" and "no" labels
            FontMetrics fm = g.getFontMetrics();
            int yes_width = fm.charWidth('y') + fm.charWidth('e') + fm.charWidth('s');
            g.drawString("no", b - 1, (d.height - 1) / 2 - fm.getHeight());
            g.drawString("yes", d.width - b - yes_width, (d.height - 1) / 2 - fm.getHeight());
        }

        if (selected)       // draw selection
        {
            g2.setStroke(GraphConstants.SELECTION_STROKE);
            g.setColor(graph.getHighlightColor());
            g.drawLine(b - 1, (d.height - 1) / 2, (d.width - 1) / 2, b - 1);
            g.drawLine((d.width - 1) / 2, b - 1, d.width - b, (d.height - 1) / 2);
            g.drawLine(d.width - b, (d.height - 1) / 2, (d.width - 1) / 2, d.height - b);
            g.drawLine((d.width - 1) / 2, d.height - b, b - 1, (d.height - 1) / 2);
        }
    }
}
