package cpv;

import javax.swing.*;
import java.io.*;
import java.util.*;
import cpv.Runner.ILProgram;

// concurrent program class
//------------------------------------------------------------------------
public class Program
{
    private ILProgram ilprogram = null;    // IL representation
    String data;

//------------------------------------------------------------------------
    // create program from input stream
    public Program(String filename)
    {
        try
        {
            BufferedReader dec = new BufferedReader(new FileReader(filename));

            int frames = Integer.parseInt(dec.readLine()); // number of windows
            JInternalFrame f = null;

            for (int i = 0; i < frames; i++)
            {
                String s = dec.readLine();              // read window signature
                f = null;
                // create a specified window
                if(s.equals("GVF"))                		// Global Variables Frame
                    f = new GlobalVarsFrame(dec);
                else if(s.equals("PW"))				    // Process Window
                    f = new ProcessWindow(dec);
                else if(s.equals("DW"))				    // Diagram Window
                    f = new DiagramWindow(dec);
                else
                    throw new MyException("Unknown window type");

                Application.frame.desktop.add(f);                  // add MDI child window to desktop
            }

            for (var fr : Application.frame.desktop.getAllFrames())
            	fr.setSelected(false);
            f.setSelected(true);                       // make only one frame selected
    
            dec.close();      // now the program is loaded
            SaveData();       // save it into memory
        }
        catch(MyException e)
        {
            JOptionPane.showMessageDialog(Application.frame, "Exception in Program(): " + e.getMessage());
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(Application.frame, "Exception in Program(): " + e.getMessage());
        }
        catch (Exception ex)
        {}
    }

//------------------------------------------------------------------------
    public Program()                     // create new program
    {
        JInternalFrame p = new GlobalVarsFrame("Global Variables");
        Application.frame.desktop.add(p);
        try
        {
            p.setSelected(true);               // select global variables frame by default
        }
        catch (Exception e)
        {
        }
        Application.frame.desktop.add(new DiagramWindow());
    }
//------------------------------------------------------------------------
    // save program to memory (to "data" string)
    public void SaveData()
    {
        try
        {
            StringWriter bs = new StringWriter();
            PrintWriter enc = new PrintWriter(bs);

            var flist = Application.frame.desktop.getAllFrames();

            enc.println(flist.length);                    // number of frames

            for(var f : flist)
            {
                if(f instanceof GlobalVarsFrame)   		// write window signature
                    enc.println("GVF");			  		// Global Variables Frame
                else if(f instanceof ProcessWindow)
                    enc.println("PW");                  // Process Window
                else if(f instanceof DiagramWindow)
                    enc.println("DW");		          	// Diagram Window
                else
                    throw new MyException("Unknown window type");

                ((MyInternalFrame)f).Save(enc);          // write each window
            }

            data = bs.toString();       // save data
            enc.close();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(Application.frame, "Exception in SaveData(): " + e.getMessage());
        }
    }
//------------------------------------------------------------------------
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
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(Application.frame, "Exception in SaveAs(): " + e.getMessage());
        }
    }
//------------------------------------------------------------------------
    public void CloseAll()      // close windows
    {
        for(var f : Application.frame.desktop.getAllFrames())
            f.dispose();
    }
//------------------------------------------------------------------------
    // is the program modified or not
    boolean IsModified()
    {
        String olddata = data;         // olddata is the copy of data

        SaveData();                    // save program to memory
        boolean is_mod = !data.equals(olddata);
        data = olddata;			// restore original data

        return is_mod;
    }
//------------------------------------------------------------------------
    // get global variables frame
    public GlobalVarsFrame GetGVF()
    {
        for (var f : Application.frame.desktop.getAllFrames())
            if(f instanceof GlobalVarsFrame)
                return (GlobalVarsFrame)f;
        return null;
    }
//------------------------------------------------------------------------
    // get program as IL text
    public Vector<String> GetILRepresentation() throws SyntaxErrorException
    {
        var vec = new Vector<String>();            // program text
        var vars = GetGVF().GetAsVector();    	 // get global variables

        vec.add("__commonvariables");              // extract common variables
        for (var v : vars)
            vec.add(Translator.getTranslator().TranslateVariable(v));  // add a variable to the list
        vec.add("__endofcommonvariables");

        for (var f : Application.frame.desktop.getAllFrames())
            if (f instanceof ProcessWindow)     // for each process
            {
                vec.add("__process " + f.getTitle().substring(8)); // skip "Process " substring
                vars = ((ProcessWindow)f).GetProcessGraph().GetVarsAsVector();

                for(var v : vars)      // extract local variables
                    vec.add(Translator.getTranslator().TranslateVariable(v));
                vec.add("__code");

                // extract code
                vec.addAll(((ProcessWindow)f).GetProcessGraph().GetCodeAsVector());

                // endproc is added by end block; in case of no end block should add manually
                if(!vec.get(vec.size() - 1).equals("__endproc"))
                    vec.add("__endproc");
            }

        return vec;
    }
//------------------------------------------------------------------------
	public ILProgram getILProgram()
	{
		return ilprogram;
	}
//------------------------------------------------------------------------
	public void setILProgram(ILProgram p)
	{
		ilprogram = p;
	}
//------------------------------------------------------------------------
}