package cpv.MyJGraph;

import javax.swing.JDialog;
import java.awt.HeadlessException;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import cpv.*;

////////////////////////////////////////////////////////////////////////////////
// Local Variables Editor window class
////////////////////////////////////////////////////////////////////////////////
public class LocalVarsEditor extends BlockEditor
{
    private JPanel panel1 = new JPanel();                     // window controls
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel jPanel1 = new JPanel();
    private JPanel jPanel2 = new JPanel();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private ExtendedTextArea TextArea = new ExtendedTextArea();
    private JButton OKButton = new JButton();
    private JButton CancelButton = new JButton();

    private boolean OKPressed = false;

////////////////////////////////////////////////////////////////////////////////
    // create an LVE window
    public LocalVarsEditor(Frame frame, String title, boolean modal, String oldtext)
    {
        super(frame, title, modal);
        try
        {
            jbInit();
            pack();
            String substr = oldtext.substring(6, oldtext.length() - 7);  // substr between <html>...</html>
            TextArea.setText(substr.replaceAll("<br>", "\n"));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
////////////////////////////////////////////////////////////////////////////////
    // JBuilder auto-generated initialization routine
    private void jbInit() throws Exception
    {
        panel1.setLayout(borderLayout1);
        jPanel1.setLayout(borderLayout2);
        OKButton.setMnemonic('O');
        OKButton.setText("OK");
        OKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OKButton_actionPerformed(e);
            }
        });
        CancelButton.setMnemonic('C');
        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CancelButton_actionPerformed(e);
            }
        });
        TextArea.setRows(5);
        TextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                TextArea_keyPressed(e);
            }
        });
        getContentPane().add(panel1);
        panel1.add(jPanel2,  BorderLayout.SOUTH);
        jPanel2.add(OKButton, null);
        jPanel2.add(CancelButton, null);
        panel1.add(jPanel1,  BorderLayout.CENTER);
        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
        jScrollPane1.getViewport().add(TextArea, null);
    }
////////////////////////////////////////////////////////////////////////////////
    void OKButton_actionPerformed(ActionEvent e) // if OK button pressed
    {
        OKPressed = true;
        hide();
    }
////////////////////////////////////////////////////////////////////////////////
    void CancelButton_actionPerformed(ActionEvent e)  // if Cancel button pressed
    {
        OKPressed = false;
        hide();
    }
////////////////////////////////////////////////////////////////////////////////
    public String GetResultingText()          // get variables text (as HTML)
    {
        return "<html>" + TextArea.getText().replaceAll("\n", "<br>") + "</html>";
    }
////////////////////////////////////////////////////////////////////////////////
    void TextArea_keyPressed(KeyEvent e)        // if ESC pressed, it is the same as Cancel
    {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            CancelButton_actionPerformed(null);
        else if(e.getKeyCode() == KeyEvent.VK_M && e.isControlDown())
        	TextArea.ShowPopupMenu();
    }
////////////////////////////////////////////////////////////////////////////////
    public boolean OKPressed()
    {
        return OKPressed;
    }
////////////////////////////////////////////////////////////////////////////////
}