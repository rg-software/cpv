package cpv.MyJGraph;

import javax.swing.JDialog;
import java.awt.Frame;

//------------------------------------------------------------------------
// Parent class for block editors
//------------------------------------------------------------------------
abstract public class BlockEditor extends JDialog
{
    public BlockEditor(Frame frame, String title, boolean modal)
    {
        super(frame, title, modal);
    }

    abstract public boolean OKPressed();
    abstract public String GetResultingText();
}
