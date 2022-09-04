package cpv;

import javax.swing.*;
import java.io.*;
import java.awt.*;

// ancestor class for all MDI windows
//------------------------------------------------------------------------
public class MyInternalFrame extends JInternalFrame
{
//------------------------------------------------------------------------
    public MyInternalFrame()
    {
    }
//------------------------------------------------------------------------
    public MyInternalFrame(String name)     // create window with given title
    {
        super(name);
    }
//------------------------------------------------------------------------
    // create window from input stream data
    public MyInternalFrame(BufferedReader dec) throws java.io.IOException
    {
        super(dec.readLine());
        Rectangle r = new Rectangle();
        r.x = Integer.parseInt(dec.readLine());
        r.y = Integer.parseInt(dec.readLine());
        r.height = Integer.parseInt(dec.readLine());
        r.width = Integer.parseInt(dec.readLine());
        setBounds(r);
    }
//------------------------------------------------------------------------
    // save window to stream
    public void Save(PrintWriter enc) throws java.io.IOException
    {
        enc.println(getTitle());
        enc.println(getBounds().x);
        enc.println(getBounds().y);
        enc.println(getBounds().height);
        enc.println(getBounds().width);
    }
//------------------------------------------------------------------------
}