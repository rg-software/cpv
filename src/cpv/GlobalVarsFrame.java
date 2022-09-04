package cpv;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

// window for global variables
//------------------------------------------------------------------------
public class GlobalVarsFrame extends MyInternalFrame
{
    private JScrollPane jScrollPane1 = new JScrollPane();          // window controls
    private ExtendedTextArea TextArea = new ExtendedTextArea();

//------------------------------------------------------------------------
    public GlobalVarsFrame(String name)                // construct window
    {
        super(name);
        setBounds(Application.frame.desktop.getBounds().width - Configuration.GVF_DEFAULTWIDTH,
                  Application.frame.desktop.getBounds().height - Configuration.GVF_DEFAULTHEIGHT,
                  Configuration.GVF_DEFAULTWIDTH, Configuration.GVF_DEFAULTHEIGHT);

        show();

        try
        {
            jbInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
//------------------------------------------------------------------------
    // load window from object stream
    public GlobalVarsFrame(BufferedReader dec) throws java.io.IOException
    {
        super(dec);             // always use superclass' constructor

        try
        {
            jbInit();           // create layout
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        int lines = Integer.parseInt(dec.readLine());
        for (int i = 0; i < lines - 1; i++)
            TextArea.append(dec.readLine() + "\n");       // load textarea content
        if (lines >= 1)
            TextArea.append(dec.readLine());
        show();                                           // show the form
    }
//------------------------------------------------------------------------
    private void jbInit() throws Exception     // initialization routine (JBuilder auto-generated)
    {
        this.setIconifiable(true);
        this.setResizable(true);
        this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
        jScrollPane1.getViewport().add(TextArea, null);
        TextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent e) {
            	if (e.getKeyCode() == KeyEvent.VK_M && e.isControlDown())
        			TextArea.ShowPopupMenu();
            }
        });
    }
//------------------------------------------------------------------------
    // save window to the stream
    public void Save(PrintWriter enc) throws java.io.IOException
    {
        super.Save(enc);                              // save MyInternalFrame

        var vec = GetAsVector();
        enc.println(vec.size());
        
        for (var line : vec)
            enc.println(line);              		  // save textarea content
    }
//------------------------------------------------------------------------
    public Vector<String> GetAsVector()        		  // get all global variables as vector
    {
        var v = new Vector<String>();
        String[] s = TextArea.getText().split("\n");   // each line is vector element
        
        for (var line : s)							   // create vector
            if(!line.equals(""))                       // from non-empty lines
                v.add(line);
        
        return v;
    }
//------------------------------------------------------------------------
}