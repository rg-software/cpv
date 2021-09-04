package cpv.MyJGraph;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import cpv.*;

////////////////////////////////////////////////////////////////////////////////
// Ordinary Block Editor
////////////////////////////////////////////////////////////////////////////////
public class GenericBlockEditor extends BlockEditor 
{
    private JPanel panel1 = new JPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel jPanel1 = new JPanel();
    private JButton OKButton = new JButton();
    private JButton CancelButton = new JButton();
    private JPanel jPanel2 = new JPanel();
    private JPanel jPanel3 = new JPanel();
    private JPanel jPanel4 = new JPanel();
    private JTextField TextField = new ExtendedTextField();
    private boolean OKPressed = false;

////////////////////////////////////////////////////////////////////////////////
    public GenericBlockEditor(Frame frame, String title, boolean modal, String oldtext)
    {
        super(frame, title, modal);
        try {
            jbInit();
            pack();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        TextField.setText(oldtext);
    }
////////////////////////////////////////////////////////////////////////////////
    private void jbInit() throws Exception {
        panel1.setLayout(borderLayout1);
        OKButton.setText("OK");
        OKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OKButton_actionPerformed(e);
            }
        });
        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CancelButton_actionPerformed(e);
            }
        });
        TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    CancelButton_actionPerformed(null);
                else if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    OKButton_actionPerformed(null);
            }
        });
        panel1.setPreferredSize(new Dimension(210, 68));
        getContentPane().add(panel1,  BorderLayout.CENTER);
        panel1.add(jPanel1,  BorderLayout.SOUTH);
        jPanel1.add(OKButton, null);
        jPanel1.add(CancelButton, null);
        panel1.add(jPanel2,  BorderLayout.NORTH);
        panel1.add(jPanel3,  BorderLayout.EAST);
        panel1.add(jPanel4,  BorderLayout.WEST);
        panel1.add(TextField, BorderLayout.CENTER);
    }
////////////////////////////////////////////////////////////////////////////////
    public boolean OKPressed()
    {
        return OKPressed;
    }
////////////////////////////////////////////////////////////////////////////////
    public String GetResultingText()
    {
        return TextField.getText();
    }
////////////////////////////////////////////////////////////////////////////////
    void OKButton_actionPerformed(ActionEvent e)
    {
        OKPressed = true;
        hide();
    }
////////////////////////////////////////////////////////////////////////////////
    void CancelButton_actionPerformed(ActionEvent e)
    {
        OKPressed = false;
        hide();
    }
////////////////////////////////////////////////////////////////////////////////
}