package cpv;

import cpv.MyJGraph.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

////////////////////////////////////////////////////////////////////////////////
public class DiagramWindow extends MyInternalFrame  // window for the state space diagram
{
    private JPanel jPanel1 = new JPanel();                     //
    private JScrollPane jScrollPane1 = new JScrollPane();      //   window
    private JButton InitializeButton = new JButton();          //
    private JButton ExpandButton = new JButton();              //   controls
    private DJGraph diagram = new DJGraph();                   //

    private boolean InitializePressed = false;                 // is "Initialize" button pressed?

////////////////////////////////////////////////////////////////////////////////
    public DiagramWindow()                                     // make a diagram window
    {
        super("State Space Diagram");
        setBounds(0, 0, Configuration.DIAGRAMWINDOW_DEFAULTWIDTH, Configuration.DIAGRAMWINDOW_DEFAULTHEIGHT);
        show();
        Application.therunner.SetSurface(diagram);    // set diagram as surface for Runner module output
        try                                           // create controls
        {                                             // (JBuilder auto-generated)
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
////////////////////////////////////////////////////////////////////////////////
    // make window from the stream
    public DiagramWindow(BufferedReader dec) throws java.io.IOException
    {
        this();
// commented: partially implemented loading (now just generate an empty window)
/*        super(dec);

        try                 // create controls (JBuilder auto-generated)
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        InitializePressed = Boolean.getBoolean(dec.readLine());
        Application.therunner.SetSurface(diagram); // set diagram as surface for Runner module output

        if(InitializePressed)       // if diagram is already initialized, load it from stream
        {
            diagram.LoadFromFile(dec);               // load diagram surface
            Application.theprogram.ilprogram = new ILProgram(dec);  // load IL program
            Application.therunner.LoadFromFile(dec);                // load ILRunner state
        }
        show();*/
    }
////////////////////////////////////////////////////////////////////////////////
    private void jbInit() throws Exception       // JBuilder auto-generated initialization routine
    {
        InitializeButton.setText("Initialize");
        InitializeButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                InitializeButton_actionPerformed(e);
            }
        });
        ExpandButton.setText("Expand All");
        ExpandButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                ExpandButton_actionPerformed(e);
            }
        });
        this.setResizable(true);
        this.getContentPane().add(jPanel1,  BorderLayout.SOUTH);
        jPanel1.add(InitializeButton, null);
        jPanel1.add(ExpandButton, null);
        this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
        jScrollPane1.getViewport().add(diagram, null);

        diagram.addMouseListener(new MouseAdapter()     //Attention! Hand-added code
        {
            public void mousePressed(MouseEvent e)
            {
                if(e.getClickCount() == 2)       // if doubleclick
                {                                // find an underlying cell
                    Object cell = diagram.getFirstCellForLocation(e.getX(), e.getY());
                    if (cell != null)            // expand this cell
                        Application.therunner.ExpandState(diagram.convertValueToString(cell));
                }
            }
        });
    }
////////////////////////////////////////////////////////////////////////////////
    // save window to the stream
    public void Save(PrintWriter enc) throws java.io.IOException
    {
// commented: partially implemented saving
/*        super.Save(enc);
        enc.println(InitializePressed);           // save initialized flag
        if(InitializePressed)                     // if diagram is initialized
        {                                         // save it too:
            diagram.SaveToFile(enc);                     // save diagram surface
            Application.theprogram.ilprogram.Save(enc);  // save IL program
            Application.therunner.SaveToFile(enc);       // save ILRunner state
        }*/
    }
////////////////////////////////////////////////////////////////////////////////
    // Initialize button pressed
    void InitializeButton_actionPerformed(ActionEvent e)
    {
        try       // perform initialization
        {
            Application.therunner.Initialize(Application.theprogram.GetILRepresentation());
            InitializePressed = true;
        }
        catch(SyntaxErrorException ex)   // on error show message
        {
            JOptionPane.showMessageDialog(Application.frame, ex.getMessage());
        }
    }
////////////////////////////////////////////////////////////////////////////////
    // Expand (all) button pressed
    void ExpandButton_actionPerformed(ActionEvent e)
    {
        if(!InitializePressed)				// cannot expand without initialization
            InitializeButton_actionPerformed(e);    // initialize

        if(InitializePressed)                  // if no exception occurs
            Application.therunner.ExpandAll();
    }
////////////////////////////////////////////////////////////////////////////////
}