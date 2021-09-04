package cpv;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

////////////////////////////////////////////////////////////////////////////////
// Main application window
////////////////////////////////////////////////////////////////////////////////
public class MainFrame extends JFrame
{
    private JPanel contentPane;                            // window controls
    private JMenuBar jMenuBar1 = new JMenuBar();
    private JMenu jMenuFile = new JMenu();
    private JMenuItem jMenuFileExit = new JMenuItem();
    private JMenu jMenuHelp = new JMenu();
    private JMenuItem jMenuHelpAbout = new JMenuItem();
    private JMenuItem jMenuHelpHelp = new JMenuItem();
    private BorderLayout borderLayout1 = new BorderLayout();

    public JDesktopPane desktop = new JDesktopPane();
	public int NewWindowOffset = Configuration.NEW_WINDOW_OFFSET; // offset of the next created ProcessWindow frame


    private JMenuItem jMenuFileNew = new JMenuItem();
    private JMenuItem jMenuFileOpen = new JMenuItem();
    private JMenuItem jMenuFileSave = new JMenuItem();
    private JMenuItem jMenuFileSaveAs = new JMenuItem();
    private JMenuItem jMenuFileClose = new JMenuItem();
    private JMenu jMenuProcess = new JMenu();
    private JMenuItem jMenuProcessAdd = new JMenuItem();
    private JMenuItem jMenuProcessRename = new JMenuItem();
    private JMenuItem jMenuProcessRemove = new JMenuItem();

////////////////////////////////////////////////////////////////////////////////
    //Construct the frame
    public MainFrame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
////////////////////////////////////////////////////////////////////////////////
    // JBuilder auto-generated initialization routine
    private void jbInit() throws Exception  {
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(528, 387));
        this.setTitle(Configuration.SOFTWARE_NAME);
        jMenuFile.setMnemonic('F');
        jMenuFile.setText("File");
        jMenuFileExit.setMnemonic('X');
        jMenuFileExit.setText("Exit");
        jMenuFileExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(88, java.awt.event.KeyEvent.CTRL_MASK, false));
        jMenuFileExit.addActionListener(new ActionListener()  {
            public void actionPerformed(ActionEvent e) {
                jMenuFileExit_actionPerformed(e);
            }
        });
        jMenuHelp.setMnemonic('H');
        jMenuHelp.setText("Help");
        jMenuHelpAbout.setMnemonic('A');
        jMenuHelpAbout.setText("About");
        jMenuHelpAbout.addActionListener(new ActionListener()  {
            public void actionPerformed(ActionEvent e) {
                jMenuHelpAbout_actionPerformed(e);
            }
        });
        jMenuHelpHelp.setMnemonic('H');
        jMenuHelpHelp.setText("Help");
        jMenuHelpHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0, false));
        jMenuHelpHelp.addActionListener(new ActionListener()  {
            public void actionPerformed(ActionEvent e) {
                jMenuHelpHelp_actionPerformed(e);
            }
        });
        jMenuFileNew.setMnemonic('N');
        jMenuFileNew.setText("New");
        jMenuFileNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(78, java.awt.event.KeyEvent.CTRL_MASK, false));
        jMenuFileNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuFileNew_actionPerformed(e);
            }
        });
        jMenuFileOpen.setMnemonic('O');
        jMenuFileOpen.setText("Open");
        jMenuFileOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(79, java.awt.event.KeyEvent.CTRL_MASK, false));
        jMenuFileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuFileOpen_actionPerformed(e);
            }
        });
        jMenuFileSave.setEnabled(false);
        jMenuFileSave.setMnemonic('S');
        jMenuFileSave.setText("Save");
        jMenuFileSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(83, java.awt.event.KeyEvent.CTRL_MASK, false));
        jMenuFileSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuFileSave_actionPerformed(e);
            }
        });
        jMenuFileSaveAs.setEnabled(false);
        jMenuFileSaveAs.setMnemonic('A');
        jMenuFileSaveAs.setText("Save As...");
        jMenuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuFileSaveAs_actionPerformed(e);
            }
        });
        jMenuFileClose.setEnabled(false);
        jMenuFileClose.setMnemonic('C');
        jMenuFileClose.setText("Close");
        jMenuFileClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(87, java.awt.event.KeyEvent.CTRL_MASK, false));
        jMenuFileClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuFileClose_actionPerformed(e);
            }
        });
        jMenuProcess.setMnemonic('P');
        jMenuProcess.setText("Process");
        jMenuProcessAdd.setEnabled(false);
        jMenuProcessAdd.setMnemonic('A');
        jMenuProcessAdd.setText("Add");
        jMenuProcessAdd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.KeyEvent.CTRL_MASK, false));
		jMenuProcessAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuProcessAdd_actionPerformed(e);
            }
        });
        jMenuProcessRename.setEnabled(false);
        jMenuProcessRename.setMnemonic('R');
        jMenuProcessRename.setText("Rename");
        jMenuProcessRename.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.KeyEvent.CTRL_MASK, false));
		jMenuProcessRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuProcessRename_actionPerformed(e);
            }
        });
        jMenuProcessRemove.setEnabled(false);
        jMenuProcessRemove.setMnemonic('E');
        jMenuProcessRemove.setText("Remove");
        jMenuProcessRemove.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.KeyEvent.CTRL_MASK, false));
		jMenuProcessRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jMenuProcessRemove_actionPerformed(e);
            }
        });
        desktop.setBackground(Color.lightGray);
        jMenuHelp.add(jMenuHelpHelp);
        jMenuHelp.add(jMenuHelpAbout);
        jMenuBar1.add(jMenuFile);
        jMenuBar1.add(jMenuProcess);
        jMenuBar1.add(jMenuHelp);
        contentPane.add(desktop, BorderLayout.CENTER);
        jMenuFile.add(jMenuFileNew);
        jMenuFile.add(jMenuFileOpen);
        jMenuFile.add(jMenuFileSave);
        jMenuFile.add(jMenuFileSaveAs);
        jMenuFile.add(jMenuFileClose);
        jMenuFile.add(jMenuFileExit);
        jMenuProcess.add(jMenuProcessAdd);
        jMenuProcess.add(jMenuProcessRename);
        jMenuProcess.add(jMenuProcessRemove);

        this.setJMenuBar(jMenuBar1);
    }
////////////////////////////////////////////////////////////////////////////////
    //File | Exit action performed
    public void jMenuFileExit_actionPerformed(ActionEvent e) {
        System.exit(0);
    }
////////////////////////////////////////////////////////////////////////////////
    //Help | Help action performed
    public void jMenuHelpHelp_actionPerformed(ActionEvent e) {
        DisplayString(GetFileAsString("help.txt"));
    }
////////////////////////////////////////////////////////////////////////////////
    //Help | About action performed
    public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
        DisplayString(GetFileAsString("about.txt"));
    }
////////////////////////////////////////////////////////////////////////////////
    //Overridden so we can exit when window is closed
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            jMenuFileExit_actionPerformed(null);
        }
    }
////////////////////////////////////////////////////////////////////////////////
    void jMenuProcessAdd_actionPerformed(ActionEvent e)                 // add new process
    {
        String procname = JOptionPane.showInputDialog(Application.frame, "Process name:");
        if(procname != null && procname.length() > 0)
        {
            if(procname.indexOf(" ") != -1)             // spaces are forbidden
                JOptionPane.showMessageDialog(Application.frame, "Invalid process name (spaces are forbidden)", "Error", JOptionPane.OK_OPTION);
            else
            {
                ProcessWindow pw = new ProcessWindow("Process " + procname);    // create process window
                desktop.add(pw);
                try
                {
                    JInternalFrame f[] = desktop.getAllFrames();       // deselect all MDI frames
                    for(int i = 0; i < f.length; i++)
                        f[i].setSelected(false);
                    pw.setSelected(true);                              // select newly created frame
                }
                catch(Exception ex){}
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////
    void jMenuProcessRename_actionPerformed(ActionEvent e)  // rename process
    {
        JInternalFrame cframe = Application.frame.desktop.getSelectedFrame();

        if(!(cframe instanceof ProcessWindow))  // if none of process windows selected
        {
            JOptionPane.showMessageDialog(Application.frame, "No process selected", "Error", JOptionPane.OK_OPTION);
            return;
        }

        String result = JOptionPane.showInputDialog(Application.frame, "Input new name");
        if(result == null || result.length() == 0)  // invalid name
            return;

        if(result.indexOf(" ") != -1)
        {
            JOptionPane.showMessageDialog(Application.frame, "Invalid process name (spaces are forbidden)", "Error", JOptionPane.OK_OPTION);
            return;
        }

        cframe.setTitle("Process " + result);
    }
////////////////////////////////////////////////////////////////////////////////
    void jMenuProcessRemove_actionPerformed(ActionEvent e)     // remove process
    {
        JInternalFrame frame = Application.frame.desktop.getSelectedFrame();

        if(!(frame instanceof ProcessWindow))
        {
            JOptionPane.showMessageDialog(Application.frame, "No process selected", "Error", JOptionPane.OK_OPTION);
            return;
        }

        if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(Application.frame, "Removing process: " + frame.getTitle() + ". Are you sure?", "Flowchart Editor", JOptionPane.YES_NO_OPTION))
            frame.dispose();
    }
////////////////////////////////////////////////////////////////////////////////
    void SetEnableStatus(boolean status)   // enable or disable menus:
    {
        jMenuFileSave.setEnabled(status);       // File - Save
        jMenuFileSaveAs.setEnabled(status);     // File - Save As
        jMenuFileClose.setEnabled(status);      // File - Close
        jMenuProcessAdd.setEnabled(status);     // Process - Add
        jMenuProcessRemove.setEnabled(status);  // Process - Remove
        jMenuProcessRename.setEnabled(status);  // Process - Rename
    }
////////////////////////////////////////////////////////////////////////////////
    void jMenuFileNew_actionPerformed(ActionEvent e)    // create new program
    {
        jMenuFileClose_actionPerformed(e);              // close previous
        Application.theprogram = new Program();         // create program
        Application.CurrentFileName = null;
        SetEnableStatus(true);                          // enable menus
    }
////////////////////////////////////////////////////////////////////////////////
    void jMenuFileClose_actionPerformed(ActionEvent e)  // close program
    {
        if(Application.theprogram == null)              // cannot close
        	return;

        if(Application.theprogram.IsModified())         // if program is modified
        {
            int result = JOptionPane.showConfirmDialog(Application.frame.desktop, "Save changes?", "CPV", JOptionPane.YES_NO_CANCEL_OPTION);

            if(result == JOptionPane.CANCEL_OPTION)     // cancel closing
                return;

            if(result == JOptionPane.YES_OPTION)        // save changes
                Application.theprogram.SaveAs(Application.CurrentFileName);
        }

        Application.theprogram.CloseAll();          // close windows
        Application.theprogram = null;
        Application.CurrentFileName = null;
        SetEnableStatus(false);                     // disable menus
    }
////////////////////////////////////////////////////////////////////////////////
    void jMenuFileOpen_actionPerformed(ActionEvent e)   // open program
    {
        FileDialog fd = new FileDialog(Application.frame, "Open Program", FileDialog.LOAD);
        fd.show();
        if(fd.getFile() != null)               // if file is valid
        {
            jMenuFileClose_actionPerformed(e);        // close old program
            if(Application.CurrentFileName == null)   // if closed successfully
            {                                         // open new program
                Application.CurrentFileName = fd.getDirectory() + fd.getFile();
                Application.theprogram = new Program(Application.CurrentFileName);
                SetEnableStatus(true);
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////
    void jMenuFileSave_actionPerformed(ActionEvent e)            // just save
    {
        if(Application.CurrentFileName == null)                  // if name is unknown, call SaveAs
            jMenuFileSaveAs_actionPerformed(e);
        else
            Application.theprogram.SaveAs(Application.CurrentFileName);   // else save using current file name
    }
////////////////////////////////////////////////////////////////////////////////
    void jMenuFileSaveAs_actionPerformed(ActionEvent e)          // get filename from user and save
    {
        FileDialog fd = new FileDialog(Application.frame, "Save Program", FileDialog.SAVE);
        fd.show();
        if(fd.getFile() != null)
        {
            Application.CurrentFileName = fd.getDirectory() + fd.getFile();
            Application.theprogram.SaveAs(Application.CurrentFileName);
        }
    }
////////////////////////////////////////////////////////////////////////////////
	String GetFileAsString(String filename)
	{
	 	File fc = new File(filename);
	 	BufferedReader textReader = null;
        
        try 
        {
        	textReader = new BufferedReader(new FileReader(fc));
        }
        catch (IOException e) 
        {
            return "Error opening file " + fc; 
        }
        
        StringWriter textWriter = new StringWriter();
        int c = 0;       
        try 
        {   
            while(true) 
            {
                c = textReader.read();
                if (c == -1) 
                	break; 
				else 
					textWriter.write(c);   
            }
        }       
        catch (IOException e) 
        {
            return "Error reading file " + fc;
        }
        return textWriter.toString();
	}
////////////////////////////////////////////////////////////////////////////////
	void DisplayString(String s)
	{
		JFrame frame = new JFrame();
        JTextArea area = new JTextArea();
        
        area.setFont(new Font(Configuration.HELP_FONT_FAMILY, Configuration.HELP_FONT_STYLE, Configuration.HELP_FONT_SIZE));
        area.setText(s);
        area.setCaretPosition(0);
        area.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(area);
        frame.getContentPane().add(scrollPane);
        
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle(Configuration.SOFTWARE_NAME);
        int x = Configuration.HELPWINDOW_WIDTH;
        int y = Configuration.HELPWINDOW_HEIGHT;
        frame.setBounds((int)(x*0.25), (int)(y*0.2), (int)(x*0.6), (int)(y*0.5));
        frame.setVisible(true);
	}
////////////////////////////////////////////////////////////////////////////////
}