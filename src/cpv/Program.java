package cpv;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.beans.*;
import java.util.*;
import cpv.Runner.ILProgram;

////////////////////////////////////////////////////////////////////////////////
// concurrent program class
////////////////////////////////////////////////////////////////////////////////
public class Program
{
    private ILProgram ilprogram = null;    // IL representation
    String data;

////////////////////////////////////////////////////////////////////////////////
    // create program from input stream
    public Program(String filename)
    {
        try
        {
            BufferedReader dec = new BufferedReader(new FileReader(filename));

            int frames = Integer.parseInt(dec.readLine()); // number of windows
            JInternalFrame f = null;

            for(int i = 0; i < frames; i++)
            {
                String s = dec.readLine();                          // read window signature
                f = null;
                // create a specified window
                if(s.equals("GVF"))                		    // Global Variables Frame
                    f = new GlobalVarsFrame(dec);
                else if(s.equals("PW"))				    // Process Window
                    f = new ProcessWindow(dec);
                else if(s.equals("DW"))				    // Diagram Window
                    f = new DiagramWindow(dec);
                else
                    throw new MyException("Unknown window type");

                Application.frame.desktop.add(f);                  // add MDI child window to desktop
            }

            try
            {
                JInternalFrame flist[] = Application.frame.desktop.getAllFrames();
                for(int i = 0; i < flist.length; i++)
                    flist[i].setSelected(false);
                f.setSelected(true);                       // make only one frame selected
            }
            catch(Exception ex)
            {}

            dec.close();      // now the program is loaded
            SaveData();       // save it into memory
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(Application.frame, "Exception in Program(): " + e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////
    public Program()                     // create new program
    {
        JInternalFrame p = new GlobalVarsFrame("Global Variables");
        Application.frame.desktop.add(p);
        try
        {
            p.setSelected(true);               // select global variables frame by default
        }
        catch(Exception e)
        {}
        Application.frame.desktop.add(new DiagramWindow());
    }
////////////////////////////////////////////////////////////////////////////////
    // save program to memory (to "data" string)
    public void SaveData()
    {
        try
        {
            StringWriter bs = new StringWriter();
            PrintWriter enc = new PrintWriter(bs);

            JInternalFrame[] flist = (JInternalFrame [])Application.frame.desktop.getAllFrames();

            enc.println(flist.length);                    // number of frames

            for(int i = 0; i < flist.length; i++)
            {
                if(flist[i] instanceof GlobalVarsFrame)   // write window signature
                    enc.println("GVF");			  // Global Variables Frame
                else if(flist[i] instanceof ProcessWindow)
                    enc.println("PW");                    // Process Window
                else if(flist[i] instanceof DiagramWindow)
                    enc.println("DW");		          // Diagram Window
                else
                    throw new MyException("Unknown window type");

                ((MyInternalFrame)flist[i]).Save(enc);          // write each window
            }

            data = bs.toString();       // save data
            enc.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(Application.frame, "Exception in SaveData(): " + e.getMessage());
        }
    }
////////////////////////////////////////////////////////////////////////////////
    // save program to file
    public void SaveAs(String FileName)
    {
        SaveData();

        try
        {
            PrintWriter f = new PrintWriter(new BufferedWriter(new FileWriter(FileName)));

            f.write(data);

            f.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(Application.frame, "Exception in SaveAs(): " + e.getMessage());
        }
    }
////////////////////////////////////////////////////////////////////////////////
    public void CloseAll()      // close windows
    {
        JInternalFrame[] flist = Application.frame.desktop.getAllFrames();

        for(int i = 0; i < flist.length; i++)
            flist[i].dispose();
    }
////////////////////////////////////////////////////////////////////////////////
    // is the program modified or not
    boolean IsModified()
    {
        String olddata = data;         // olddata is the copy of data

        SaveData();                    // save program to memory
        boolean is_mod = !data.equals(olddata);
        data = olddata;			// restore original data

        return is_mod;
    }
////////////////////////////////////////////////////////////////////////////////
    // get global variables frame
    public GlobalVarsFrame GetGVF()
    {
        JInternalFrame[] flist = Application.frame.desktop.getAllFrames();

        for(int i = 0; i < flist.length; i++)
            if(flist[i] instanceof GlobalVarsFrame)
                return (GlobalVarsFrame)flist[i];
        return null;
    }
////////////////////////////////////////////////////////////////////////////////
    // get program as IL text
    public Vector GetILRepresentation() throws SyntaxErrorException
    {
        Vector v = new Vector();                // program text
        Vector vars = GetGVF().GetAsVector();   // get global variables

        v.add("__commonvariables");              // extract common variables
        for(int i = 0; i < vars.size(); i++)
            v.add(Translator.getTranslator().TranslateVariable((String)vars.get(i)));       // add a variable to the list
        v.add("__endofcommonvariables");

        JInternalFrame[] flist = Application.frame.desktop.getAllFrames();

        for(int i = 0; i < flist.length; i++)
            if(flist[i] instanceof ProcessWindow)     // for each process
            {
                v.add("__process " + flist[i].getTitle().substring(8)); // skip "Process " substring
                vars = ((ProcessWindow)flist[i]).GetProcessGraph().GetVarsAsVector();

                for(int j = 0; j < vars.size(); j++)      // extract local variables
                    v.add(Translator.getTranslator().TranslateVariable((String)vars.get(j)));
                v.add("__code");

                // extract code
                v.addAll(((ProcessWindow)flist[i]).GetProcessGraph().GetCodeAsVector());

                // endproc is added by end block; in case of no end block should add manually
                if(!v.get(v.size() - 1).equals("__endproc"))
                    v.add("__endproc");
            }

        return v;
    }
////////////////////////////////////////////////////////////////////////////////
	public ILProgram getILProgram()
	{
		return ilprogram;
	}
////////////////////////////////////////////////////////////////////////////////
	public void setILProgram(ILProgram p)
	{
		ilprogram = p;
	}
////////////////////////////////////////////////////////////////////////////////
}