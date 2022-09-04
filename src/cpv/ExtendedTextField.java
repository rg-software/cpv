package cpv;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

// JTextField with fast templates implementation
//------------------------------------------------------------------------
public class ExtendedTextField extends JTextField
{
    private JPopupMenu jPopupMenu1 = new JPopupMenu();
    private JMenuItem CommentItem = new JMenuItem("/*  */");
    private JMenuItem SimpleAssignmentItem = new JMenuItem("x := y;");
    private JMenuItem NotAssignmentItem = new JMenuItem("x := not y;");
    private JMenuItem ExtendedAssignmentItem = new JMenuItem("x := y op z;");
    private JMenuItem WaitItem = new JMenuItem("wait(S);");
    private JMenuItem SignalItem = new JMenuItem("signal(S);");
    private JMenuItem StandardIFItem = new JMenuItem("x ifop y");

//------------------------------------------------------------------------
    public ExtendedTextField()
    {
        super();
        try
        {
            jbInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        final Component textarea = this;

        addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                if (SwingUtilities.isRightMouseButton(e))
                    jPopupMenu1.show(textarea, e.getX(), e.getY());
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent e) {
            	if (e.getKeyCode() == KeyEvent.VK_M && e.isControlDown())
        			jPopupMenu1.show(textarea, 0, 0);
            }
        });
    }
//------------------------------------------------------------------------
    private void jbInit() throws Exception      // initialization routine
    {
        CommentItem.setMnemonic('/');
        CommentItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setText(CommentItem.getText());
            }
        });
        SimpleAssignmentItem.setMnemonic('x');
        SimpleAssignmentItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setText(SimpleAssignmentItem.getText());
            }
        });
        NotAssignmentItem.setMnemonic('n');
        NotAssignmentItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setText(NotAssignmentItem.getText());
            }
        });
        ExtendedAssignmentItem.setMnemonic('o');
        ExtendedAssignmentItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setText(ExtendedAssignmentItem.getText());
            }
        });
        WaitItem.setMnemonic('w');
        WaitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setText(WaitItem.getText());
            }
        });
        SignalItem.setMnemonic('s');
        SignalItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setText(SignalItem.getText());
            }
        });
        StandardIFItem.setMnemonic('i');
        StandardIFItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setText(StandardIFItem.getText());
            }
        });

        jPopupMenu1.add(CommentItem);
        jPopupMenu1.add(SimpleAssignmentItem);
        jPopupMenu1.add(NotAssignmentItem);
        jPopupMenu1.add(ExtendedAssignmentItem);
        jPopupMenu1.add(WaitItem);
        jPopupMenu1.add(SignalItem);
        jPopupMenu1.add(StandardIFItem);
    }
//------------------------------------------------------------------------
}