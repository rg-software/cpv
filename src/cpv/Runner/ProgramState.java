package cpv.Runner;

import java.util.*;
import cpv.*;
import java.io.*;

//------------------------------------------------------------------------
// state of the program
//------------------------------------------------------------------------
public class ProgramState
{
    public Vector<Variable> CommonVariables = new Vector<Variable>();        // list of common variables
    public Vector<ProcessState> ProcessStates = new Vector<ProcessState>();  // list of processes

//------------------------------------------------------------------------
    public ProgramState()
    {
    }
//------------------------------------------------------------------------
    // load program state from file
    public ProgramState(BufferedReader dec) throws java.io.IOException
    {
    	// TODO: implement common variables reading for state space diagram loading
//    	CommonVariables = (Vector)dec.readObject();     // read common variables
    	ProcessStates = new Vector<ProcessState>();
    	int size = Integer.parseInt(dec.readLine());
    	for (int i = 0; i < size; i++)                       // read process
    		ProcessStates.add(new ProcessState(dec));
    }
//------------------------------------------------------------------------
    // save program state to file
    public void SaveToFile(PrintWriter enc) throws java.io.IOException
    {
    	// TODO: IMPLEMENT common variables writing for state space diagram saving
//        enc.writeObject(CommonVariables);                      // write common variables
        enc.println(ProcessStates.size());
        for (var ps : ProcessStates)
            ps.SaveToFile(enc);   // write process
    }
//------------------------------------------------------------------------
    public ProgramState MakeClone()           // make a copy of the program state
    {
        ProgramState res = new ProgramState();
        for (var v : CommonVariables)				// copy common variables
            res.CommonVariables.add(v.MakeClone());
        for (var p : ProcessStates)             	// copy processes
            res.ProcessStates.add(p.MakeClone());
        return res;
    }
//------------------------------------------------------------------------
    private boolean IsFinal()        // a state is final (no children) if and only if
    {			             		 // the current lines of all processes are "__endproc"
        var program = Application.theprogram.getILProgram();
    	for (var procstate : ProcessStates)
            if (!program.GetLine(procstate.FirstLine + procstate.CurLineOffset).equals("__endproc"))
            	return false;
        return true;
    }
//------------------------------------------------------------------------
    public String ToString()       // returns textual representation of the state
    {
        String s = "<html>";

        boolean finalstate = IsFinal();

        // make final state blue

        String FontTag = "size=" + Integer.toString(Configuration.DJGRAPH_FONT_SIZE) + 
        		         " face=\"" + Configuration.DJGRAPH_FONT_FAMILY + "\"";
        s += finalstate ? "<font " + FontTag + " color=blue>" : "<font " + FontTag + ">";
	    s += "<b>";
        
	    for (int i = 0; i < CommonVariables.size(); i++)       // output common variables
        {
            Variable v = CommonVariables.get(i);
            s += v.Name + "=" + v.getValueAsString();
            if(i != CommonVariables.size() - 1)
                s += ", ";
        }
        
	    s += "</b><br>";

        for (var procstate : ProcessStates) 	// output each process
        {
            if (procstate.blocked)                                     // blocked processes are red
                s += "<font color=red>";
            
            s += "<i>" + procstate.Name + "</i>: ";
            s += Application.theprogram.getILProgram().GetLineDescription(procstate.FirstLine + procstate.CurLineOffset);
            
            for (var v : procstate.Variables)							// output local variables
                s += ", " + v.Name + "=" + v.getValueAsString();
            s += "<br>";
            
            if (procstate.blocked)
                s += "</font>";
        }

        s += "</font>";
        s += "</html>";

        return s;
    }
//------------------------------------------------------------------------
    // return a reference to a variable (supply a name)
    private Variable SearchForVariable(ProcessState ps_state, String varname) throws RuntimeErrorException
    {
        for (var v : ps_state.Variables)   // first try to find among local variables
            if (v.Name.equals(varname))
                return v;                                     // found

        for (var v : CommonVariables)    	// then among global variables
            if (v.Name.equals(varname))
                return v;                                     // found

        throw new RuntimeErrorException("Variable " + varname + " is not found in process " + ps_state.Name);
    }
//------------------------------------------------------------------------
    // reads a variable or a constant (returns a reference to this variable)
    public Variable ReadValue(ProcessState ps_state, String symbname) throws RuntimeErrorException
    {
        if (symbname.equals("true"))                         // boolean constant
            return new BoolVariable("__unnamed", true);      // make dummy variable
        else if (symbname.equals("false"))
            return new BoolVariable("__unnamed", false);

        try
        {
            int value = Integer.parseInt(symbname);               // numerical constant
            return new IntVariable("__unnamed", value - 1, value + 1, value);
        }
        catch (Exception e)
        {
        }

        // it should be a non-semaphore variable name; semaphores do not have string-represented constants
        return SearchForVariable(ps_state, symbname);    // trying to find variable with 'symbname' name
    }
//------------------------------------------------------------------------
    // write variable value
    void WriteValue(ProcessState ps_state, String variable, Variable v) throws RuntimeErrorException
    {
        //obtain a pointer to a left variable
        Variable var = SearchForVariable(ps_state, variable);
        var.setValue(v);
    }
//------------------------------------------------------------------------
    // obtain a reference to some process
    private ProcessState GetProcess(String procname) throws RuntimeErrorException
    {
        for (var p : ProcessStates)
            if (p.Name.equals(procname))
            	return p;
        throw new RuntimeErrorException("Process '" + procname + "' not found");
    }
//------------------------------------------------------------------------
    // make one step in the process 'procname'; return NEW program state
    ProgramState MakeOneStep(String procname) throws MyException, RuntimeErrorException
    {
        ProgramState next_ps = MakeClone();               // create new program state
        ProcessState ps = next_ps.GetProcess(procname);   // obtain a reference to 'procname'

        // Executing line FirstLine + ps.CurrLineOffset and saving changes
        // in next_ps
        String Command = Application.theprogram.getILProgram().GetLine(ps.FirstLine + ps.CurLineOffset);

        if (Command.equals("__endproc"))       // end of algorithm; exiting
            return null;

        ILExecutor.getExecutor().Execute(next_ps, ps, Command);   // otherwise use ILExecutor to execute the line

       return next_ps;
    }
//------------------------------------------------------------------------
   // returns all derived states in a Vector<ProgState>
    Vector<ProgramState> GetNextStates() throws MyException, RuntimeErrorException
    {
        var result = new Vector<ProgramState>();

        for (var ps : ProcessStates)							// get the next process
        {
            ProgramState next_ps = MakeOneStep(ps.Name);        // make a step in it

            if (next_ps != null)
                result.add(next_ps);                             // if the operation succeed, add result to vector
        }
        
        return result;
    }
//------------------------------------------------------------------------
    // create starting state
    static ProgramState MakeStartingState() throws MyException, RuntimeErrorException
    {
        ProgramState StartingState = new ProgramState();

        StartingState.CommonVariables = Application.theprogram.getILProgram().GetCommonVariables(); // fill common variables
        for (int i = 0; i < Application.theprogram.getILProgram().GetLineCount(); i++)
        {
            String line = Application.theprogram.getILProgram().GetLine(i);
            if (line.length() >= 10)
                if (line.substring(0, 10).equals("__process "))   // careful! 10 is a length of string "__process "
                {
                    String procname = line.substring(line.indexOf(' ') + 1);
                    while (Application.theprogram.getILProgram().GetLine(i).equals("__code") == false)
                        i++;
                    ProcessState p = new ProcessState();            // initialize its parameters
                    p.Name = procname;
                    p.FirstLine = i + 1;
                    p.CurLineOffset = 0;
                    p.Variables = Application.theprogram.getILProgram().GetProcessVariables(procname);  // load local variables

                    while (Application.theprogram.getILProgram().GetLine(i).equals("__endproc") == false)
                        i++;

                    p.LastLine = i - 1;
                    StartingState.ProcessStates.add(p);           // add current process to the program
                }
        }

        return StartingState;
    }
//------------------------------------------------------------------------
}