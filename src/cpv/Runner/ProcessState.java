package cpv.Runner;

import java.util.*;
import cpv.*;
import java.io.*;

////////////////////////////////////////////////////////////////////////////////
public class ProcessState                             // A process and its state
{
    public String Name;                               // name of the process
    public Vector Variables = new Vector();           // Vector<Variable>
    public int FirstLine, LastLine, CurLineOffset;    // first line number, last line number, instruction pointer
    public boolean blocked = false;                   // if blocked by semaphore

////////////////////////////////////////////////////////////////////////////////
    public ProcessState()
    {
    }
////////////////////////////////////////////////////////////////////////////////
    // create a process from input stream
    public ProcessState(BufferedReader dec) throws java.io.IOException
    {
        Name = dec.readLine();
// IMPLEMENT variables read for state space diagram loading
//        Variables = (Vector)dec.readObject();
        FirstLine = Integer.parseInt(dec.readLine());
        LastLine = Integer.parseInt(dec.readLine());
        CurLineOffset = Integer.parseInt(dec.readLine());
        blocked = Boolean.getBoolean(dec.readLine());
    }
////////////////////////////////////////////////////////////////////////////////
    // save process to file
    public void SaveToFile(PrintWriter enc) throws java.io.IOException
    {
        enc.println(Name);
// IMPLEMENT variables write for state space diagram saving
//        enc.writeObject(Variables);
        enc.println(FirstLine);
        enc.println(LastLine);
        enc.println(CurLineOffset);
        enc.println(blocked);
    }
////////////////////////////////////////////////////////////////////////////////
    // make a copy of the process
    public ProcessState MakeClone()
    {
        ProcessState res = new ProcessState();

        for(int i = 0; i < Variables.size(); i++)    // copy variables
            res.Variables.add(((Variable)Variables.get(i)).MakeClone());

        res.Name = Name;                            // copy remaining parameters
        res.FirstLine = FirstLine;
        res.LastLine = LastLine;
        res.CurLineOffset = CurLineOffset;
        res.blocked = blocked;
        return res;
    }
////////////////////////////////////////////////////////////////////////////////
}