package cpv;

import com.jgraph.JGraph;
import java.awt.*;
import java.awt.event.*;
import com.jgraph.*;
import javax.swing.*;
import cpv.MyJGraph.*;
import java.io.*;
import java.beans.*;
import java.util.*;
import com.jgraph.graph.*;

////////////////////////////////////////////////////////////////////////////////
// window for the flowchart
////////////////////////////////////////////////////////////////////////////////
public class ProcessWindow extends MyInternalFrame
{
    private EJGraph flowchart = new EJGraph();                 // window controls
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JPopupMenu PopupMenu = new JPopupMenu();
    private JMenuItem AssignmentItem = new JMenuItem();
    private JMenuItem BranchingItem = new JMenuItem();
    private JMenuItem EndItem = new JMenuItem();
    private JMenuItem MarkItem = new JMenuItem();
    private JMenuItem SemaphoreOpItem = new JMenuItem();
    private JMenuItem CommentItem = new JMenuItem();


////////////////////////////////////////////////////////////////////////////////
    public ProcessWindow(String name)     // make an empty window
    {
        super(name);
        if(Application.frame.NewWindowOffset + Configuration.PROCESSWINDOW_DEFAULTWIDTH > Application.frame.desktop.getWidth() ||
           Application.frame.NewWindowOffset + Configuration.PROCESSWINDOW_DEFAULTHEIGHT > Application.frame.desktop.getHeight())
            Application.frame.NewWindowOffset = 0;
        setBounds(Application.frame.NewWindowOffset, Application.frame.NewWindowOffset, Configuration.PROCESSWINDOW_DEFAULTWIDTH, Configuration.PROCESSWINDOW_DEFAULTHEIGHT);
        Application.frame.NewWindowOffset += Configuration.PROCESSWINDOW_OFFSETDISPLACEMENT;
        show();
        Application.frame.desktop.setSelectedFrame(this);
        flowchart.EditVarsCell();

        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
////////////////////////////////////////////////////////////////////////////////
    // load window from stream
    public ProcessWindow(BufferedReader dec) throws java.io.IOException
    {
    	super(dec);

        try
        {                     // create layout
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        flowchart.LoadFromFile(dec);
        show();
    }
////////////////////////////////////////////////////////////////////////////////
    // save window to stream
    public void Save(PrintWriter enc) throws java.io.IOException
    {
        super.Save(enc);
        flowchart.SaveToFile(enc);
    }
////////////////////////////////////////////////////////////////////////////////
    private void jbInit() throws Exception   // standard JBuilder auto-generated initialization routine
    {
        AssignmentItem.setText("Assignment");
        AssignmentItem.setMnemonic('A');
        AssignmentItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AssignmentItem_actionPerformed(e);
            }
        });
        BranchingItem.setText("Branching");
        BranchingItem.setMnemonic('B');
        BranchingItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BranchingItem_actionPerformed(e);
            }
        });
        EndItem.setText("End");
        EndItem.setMnemonic('E');
        EndItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EndItem_actionPerformed(e);
            }
        });
        MarkItem.setText("Mark As Starting");
        MarkItem.setMnemonic('M');
        MarkItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MarkItem_actionPerformed(e);
            }
        });
        this.setIconifiable(true);
        this.setResizable(true);
        SemaphoreOpItem.setText("Semaphore operation");
        SemaphoreOpItem.setMnemonic('S');
        SemaphoreOpItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SemaphoreOpItem_actionPerformed(e);
            }
        });
        CommentItem.setMnemonic('C');
        CommentItem.setText("Comment");
        CommentItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CommentItem_actionPerformed(e);
            }
        });
        this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
        jScrollPane1.getViewport().add(flowchart, null);
        PopupMenu.add(AssignmentItem);
        PopupMenu.add(SemaphoreOpItem);
        PopupMenu.add(BranchingItem);
        PopupMenu.add(CommentItem);
        PopupMenu.add(EndItem);
        PopupMenu.add(MarkItem);

        // mouse handler: right click popups menu, left used for flowchart editing
        flowchart.addMouseListener(new java.awt.event.MouseAdapter()
            {
                public void mouseClicked(MouseEvent e)
                {
                    if(SwingUtilities.isRightMouseButton(e))             // show popup menu
                        PopupMenu.show(flowchart, e.getX(), e.getY());
                }
            });

        // keyboard handler: DELETE key is used for deleting selection
        flowchart.addKeyListener(new java.awt.event.KeyAdapter()
            {
                public void keyPressed(KeyEvent e)
                {
                    if (e.getKeyCode() == KeyEvent.VK_DELETE)           // delete selection
                        flowchart.DeleteSelection();
                    else if(e.getKeyCode() == KeyEvent.VK_M && e.isControlDown())
        				PopupMenu.show(flowchart, 0, 0);
                }

                public void keyReleased(KeyEvent e)
                {}

                public void keyTyped(KeyEvent e)
                {}
            });
       
    }
////////////////////////////////////////////////////////////////////////////////
    void AssignmentItem_actionPerformed(ActionEvent e)      // add ordinary block (rectangle)
    {
        flowchart.AddBlock(MyGraphCell.GENERIC, "");
    }
////////////////////////////////////////////////////////////////////////////////
    void SemaphoreOpItem_actionPerformed(ActionEvent e)    // add semaphore op block (ordinary)
    {
        flowchart.AddBlock(MyGraphCell.GENERIC, "");
    }
////////////////////////////////////////////////////////////////////////////////
    void BranchingItem_actionPerformed(ActionEvent e)       // add branching block
    {
        flowchart.AddBlock(MyGraphCell.BRANCHING, "");
    }
////////////////////////////////////////////////////////////////////////////////
    void CommentItem_actionPerformed(ActionEvent e)    // add comment block (ordinary)
    {
        flowchart.AddBlock(MyGraphCell.GENERIC, "");
    }
////////////////////////////////////////////////////////////////////////////////
    void EndItem_actionPerformed(ActionEvent e)             // add "end" block (elliptic)
    {
        flowchart.AddBlock(MyGraphCell.BEGIN_END, "end");
    }
////////////////////////////////////////////////////////////////////////////////
    void MarkItem_actionPerformed(ActionEvent e)            // mark state as starting
    {
        flowchart.MarkAsStarting();
    }
////////////////////////////////////////////////////////////////////////////////
    public EJGraph GetProcessGraph()                  // get jgraph surface
    {
        return flowchart;
    }
////////////////////////////////////////////////////////////////////////////////
}