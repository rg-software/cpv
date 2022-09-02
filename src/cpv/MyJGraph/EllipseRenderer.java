package cpv.MyJGraph;

import java.awt.*;
import org.jgraph.graph.*;

//------------------------------------------------------------------------
// custom vertex renderer (refer to JGraph documentation for details)
// provides painting of ellipse-styled vertices
//------------------------------------------------------------------------
public class EllipseRenderer extends VertexRenderer
{
//------------------------------------------------------------------------
    public void paint(Graphics g)           // paint an elliptic node
    {
        int b = borderWidth;
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = getSize();
        boolean isSelected = selected;
        
        if (super.isOpaque())                 // first draw an ellipse
        {
            g.setColor(super.getBackground());
            g.fillOval(b - 1, b - 1, d.width - b, d.height - b);
        }
        try
        {
            setBorder(null);
            setOpaque(false);
            selected = false;

            // IMPORTANT: in case of END block, output text in the centre of the block
            // otherwise use top-left alignment
            
            if(getText().equals("end"))
                super.paint(g);                // alignment: centre
            else                               // alignment: top-left
            {
                double a = Math.atan((d.width / d.height)*(d.width / d.height));
                int dx = (int)((d.width / 2) * Math.cos(a));
                int dy = (int)((d.height / 2) * Math.sin(a));
                super.setHorizontalAlignment(LEFT);
                super.setVerticalAlignment(TOP);

                super.paint(g.create(d.width / 2 - dx, d.height / 2 - dy, dx*2, dy*2));
            }
        }
        finally
        {
            selected = isSelected;
        }

        if (bordercolor != null)                  // draw selection
        {
            g.setColor(bordercolor);
            g2.setStroke(new BasicStroke(b));
            g.drawOval(b - 1, b - 1, d.width - b, d.height - b);
        }

        if (selected)
        {
            g2.setStroke(GraphConstants.SELECTION_STROKE);
            g.setColor(highlightColor);
            g.drawOval(b - 1, b - 1, d.width - b, d.height - b);
        }
    }
//------------------------------------------------------------------------
}