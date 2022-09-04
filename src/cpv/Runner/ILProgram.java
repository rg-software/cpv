package cpv.Runner;

import java.util.*;
import cpv.RuntimeErrorException;
import cpv.Application;
import java.io.*;

//------------------------------------------------------------------------
public class ILProgram                     // Intermediate Language Program
{
    private Vector<String> ProgramText;

//------------------------------------------------------------------------
    public ILProgram(Vector<String> text)    // make a program object from textual representation
    {
        ProgramText = text;
    }
//------------------------------------------------------------------------
    // load program from stream
    public ILProgram(BufferedReader dec) throws java.io.IOException
    {
        var v = new Vector<String>();
        int len = Integer.parseInt(dec.readLine());

        for (int i = 0; i < len; i++)
            v.add(dec.readLine());
        ProgramText = v;
    }
//------------------------------------------------------------------------
    // save program to stream
    public void Save(PrintWriter enc) throws java.io.IOException
    {
        enc.println(ProgramText.size());
        for (var v : ProgramText)
            enc.println(v);
    }
//------------------------------------------------------------------------
    public String GetLine(int index)         // get line chunk before description (syntax of each line: "code//description")
    {
        String result = ProgramText.get(index);
        int idx = result.indexOf("//");

        return (idx != -1) ? result.substring(0, idx) : result;
    }
//------------------------------------------------------------------------
    public String GetLineDescription(int index)   // get line chunk after description
    {
        String result = (String)ProgramText.get(index);
        int idx = result.indexOf("//");

        return (idx != -1) ? HTMLRep(result.substring(idx + 2)) : HTMLRep(result);
    }
//------------------------------------------------------------------------
    private String HTMLRep(String s)    // replace "<" with &lt
    {                                   // replace ">" with &gt
    	String s2 = s.replaceAll("\\<", "&lt;");
    	String s3 = s2.replaceAll("\\>", "&gt;");

    	return s3;
    }
//------------------------------------------------------------------------
    public int GetLineCount()              // a number of lines
    {
        return ProgramText.size();
    }
//------------------------------------------------------------------------
    // get all process variables as vector of variable objects
    public Vector<Variable> GetProcessVariables(String procname) throws RuntimeErrorException
    {
        for (int i = 0; i < GetLineCount(); i++)
            if (GetLine(i).equals("__process " + procname))     // finding variable definition section
            {
                int end_idx = i;
                while (!GetLine(end_idx).equals("__code"))
                    end_idx++;

            return GetVariables(i + 1, end_idx - 1);           // get all variables in range
        }
        throw new RuntimeException("Error: process '" + procname + "' not found");
    }
//------------------------------------------------------------------------
    // get all common (global) variables
    public Vector<Variable> GetCommonVariables() throws RuntimeErrorException
    {
        int beg_idx = 0;

        // trying to find global variables definition section
        while (Application.theprogram.getILProgram().GetLine(beg_idx).equals("__commonvariables") == false)
            beg_idx++;

        int end_idx = beg_idx + 1;

        while (Application.theprogram.getILProgram().GetLine(end_idx).equals("__endofcommonvariables") == false)
            end_idx++;

        return GetVariables(beg_idx + 1, end_idx - 1);     // get all variables in range
    }
//------------------------------------------------------------------------
    // get all variables (as objects) from a given line range
    private Vector<Variable> GetVariables(int BeginLine, int EndLine) throws RuntimeErrorException
    {
        var Variables = new Vector<Variable>();

        for(int i = BeginLine; i <= EndLine; i++)
        {
            String s = GetLine(i);                                // get next line
            String type = s.substring(0, s.indexOf(' '));         // get variable type
            String desc = s.substring(s.indexOf(' ') + 1);        // get variable description

            if (type.equals("int"))
                Variables.add(new IntVariable(desc));
            else if (type.equals("bool"))
                Variables.add(new BoolVariable(desc));
            else if (type.equals("sem"))
                Variables.add(new SemVariable(desc));
            else    // impossible since all definitions are generated automatically (i.e. without errors)
                throw new RuntimeErrorException("Unrecognized variable type: " + type);
        }

        return Variables;
    }
//------------------------------------------------------------------------
}
