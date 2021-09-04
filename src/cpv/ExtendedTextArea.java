package cpv;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

////////////////////////////////////////////////////////////////////////////////
// JTextArea with fast templates implementation
////////////////////////////////////////////////////////////////////////////////
public class ExtendedTextArea extends JTextArea
{
    private JPopupMenu jPopupMenu1 = new JPopupMenu();
    private JMenuItem IntegerItem = new JMenuItem();
    private JMenuItem BooleanItem = new JMenuItem();
    private JMenuItem SemaphoreItem = new JMenuItem();

////////////////////////////////////////////////////////////////////////////////
    public ExtendedTextArea()
    {
        super();
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        final Component textarea = this;

        addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                if(SwingUtilities.isRightMouseButton(e))
                    jPopupMenu1.show(textarea, e.getX(), e.getY());
            }

        });
    }
////////////////////////////////////////////////////////////////////////////////
    private void jbInit() throws Exception      // initialization routine
    {
        IntegerItem.setActionCommand("Integer");
        IntegerItem.setText("Integer");
        IntegerItem.setMnemonic('I');
        IntegerItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IntegerItem_actionPerformed(e);
            }
        });
        BooleanItem.setText("Boolean");
        BooleanItem.setMnemonic('B');
        BooleanItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BooleanItem_actionPerformed(e);
            }
        });
        SemaphoreItem.setText("Semaphore");
        SemaphoreItem.setMnemonic('S');
        SemaphoreItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SemaphoreItem_actionPerformed(e);
            }
        });
        jPopupMenu1.add(IntegerItem);
        jPopupMenu1.add(BooleanItem);
        jPopupMenu1.add(SemaphoreItem);
    }
////////////////////////////////////////////////////////////////////////////////
    void IntegerItem_actionPerformed(ActionEvent e)   // integer variable template
    {
        insert("VarName : integer(MinValue..MaxValue) := InitValue;", getCaretPosition());
    }
////////////////////////////////////////////////////////////////////////////////
    void BooleanItem_actionPerformed(ActionEvent e)    // boolean variable template
    {
        insert("VarName : boolean := InitValue;", getCaretPosition());
    }
////////////////////////////////////////////////////////////////////////////////
    void SemaphoreItem_actionPerformed(ActionEvent e)   // semaphore variable template
    {
        insert("VarName : semaphore := InitValue;", getCaretPosition());
    }
////////////////////////////////////////////////////////////////////////////////
	public void ShowPopupMenu()
	{
		jPopupMenu1.show(this, 0, 0);
	}
////////////////////////////////////////////////////////////////////////////////
}