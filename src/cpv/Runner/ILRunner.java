package cpv.Runner;

import java.util.*;
import org.jgraph.graph.*;
import cpv.*;
import cpv.MyJGraph.*;
import javax.swing.*;
import java.io.*;


//------------------------------------------------------------------------
// Intermediate Language Program Runner class
//------------------------------------------------------------------------
public class ILRunner
{
	class Properties             		 // properties of a program state
	{
	    public ProgramState State;       // state and
	    public DefaultPort Port;         // port of a state
	    public boolean expanded;         // already expanded or not

	    public Properties(ProgramState ps1, DefaultPort p1, boolean exp)   // simple constructor
	    {
	        State = ps1;
	        Port = p1;
	        expanded = exp;
	    }
	}

	private final int MAXITERATIONS = 1000;   // maximum number of iterations

    DJGraph surface = null;                   // surface for the diagram
    Hashtable<String, Properties> States;     // properties of the state
    HashSet<String> NEStates;                 // states to be expanded

 //------------------------------------------------------------------------
    // load ILRunner instance from file
    public void LoadFromFile(BufferedReader dec) throws java.io.IOException
    {
        GraphLayoutManager.LoadFromFile(dec);        // load layout manager

        // TODO: implement loading state space diagram
/*        NEStates = (HashSet)dec.readObject();        // load states
        States = new Hashtable();

        int states = ((Integer)dec.readObject()).intValue();     // load number of states
        for(int i = 0; i < states; i++)
        {
            String str = (String)dec.readObject();                             // state text
            boolean expanded = ((Boolean)dec.readObject()).booleanValue();     // expanded flag
            ProgramState state = new ProgramState(dec);                        // corresponding ProgramState object
            States.put(str, new Properties(state, null, expanded));            // add a state
        }

        Object[] s = surface.getRoots();                   // get all JGraph elements
        for(int i = 0; i < s.length; i++)
            if(s[i] instanceof MyGraphCell)                 // extract properties
            {
                String str = surface.convertValueToString(s[i]);
                Properties p = (Properties)States.get(str);
                p.Port = surface.GetPortByName((MyGraphCell)s[i], "central");
            }*/
    }
//------------------------------------------------------------------------
    // save ILRunner instance to file
    public void SaveToFile(PrintWriter enc) throws java.io.IOException
    {
        GraphLayoutManager.SaveToFile(enc);        // save layout manager

        // TODO: implement state space diagram saving
/*        enc.writeObject(NEStates);                    // save nestates hashset
        enc.writeObject(new Integer(States.size()));  // save number of states

        Iterator e = States.keySet().iterator();      // for each state:
        while(e.hasNext())
        {
            String str = (String)e.next();               // save text
            enc.writeObject(str);
            Properties p = (Properties)States.get(str);   // save expanded flag
            enc.writeObject(new Boolean(p.expanded));
            p.State.SaveToFile(enc);                      // save state
        }*/
    }
//------------------------------------------------------------------------
    public void SetSurface(DJGraph graph)                 // set drawing surface
    {
        surface = graph;
    }
//------------------------------------------------------------------------
    public void Initialize(Vector<String> text)                    // initialization routine
    {
        GraphLayoutManager.Initialize();                           // initialize layout manager
        Application.theprogram.setILProgram(new ILProgram(text));  // create an il-program from given text

        States = new Hashtable<String, Properties>();
        NEStates = new HashSet<String>();
        surface.ClearGraph();				// clear graph
        try
        {
            ProgramState StartingState = ProgramState.MakeStartingState();
            String s = StartingState.ToString();                            // make first state

            // visualize the state and add it to States hashtable
            States.put(s, new Properties(StartingState, surface.insert(GraphLayoutManager.GetNextPoint(), s), false));
            NEStates.add(s);          // add it also to "states to be expanded"
        }
        catch (RuntimeErrorException ex)
        {
            JOptionPane.showMessageDialog(Application.frame, ex.getMessage());
        }
        catch (MyException ex)          // show error messages
        {
            JOptionPane.showMessageDialog(Application.frame, ex.getMessage());
        }
    }
//------------------------------------------------------------------------
    public void ExpandAll()               // expand ALL states of the program
    {
        int iterations = 0;

        if (NEStates.size() == 0)         // if nothing to expand, exit
            return;
        do
        {
            if (!ExpandState(NEStates.iterator().next()))   // perform step-by-step expansion
                break;
        }
        while (!NEStates.isEmpty() && iterations++ < MAXITERATIONS);
    }
//------------------------------------------------------------------------
    public boolean ExpandState(String State)                       // expand the given state
    {
        try
        {
            Properties p = States.get(State);   // this state is already on the screen, so it is in hashtable
            if (p.expanded)
                return true;        // already expanded

            var childstates = p.State.GetNextStates();         // get all child states
            for ( int k = 0; k < childstates.size(); k++)
            {
                ProgramState ps = (ProgramState)childstates.get(k);        // for every child state:
                DefaultPort childport;
                String ChildState = ps.ToString();

                if(States.containsKey(ChildState))                          // if the child state is alredy on the screen
                    childport = ((Properties)States.get(ChildState)).Port;  // get port
                else                                                        // if not:
                {
                    String s = ps.ToString();                      // create a new vertex on the screen
                    childport = surface.insert(GraphLayoutManager.GetNextPoint(), s);
                    States.put(s, new Properties(ps, childport, false));    // add this new state
                    NEStates.add(s);                                        // to hashtables
                }

                surface.connect(p.Port, childport);                        // make an arrow
            }
            p.expanded = true;                 // now state is expanded
            NEStates.remove(State);

            return true;   // success
        }
        catch(RuntimeErrorException ex)
        {
            JOptionPane.showMessageDialog(Application.frame, ex.getMessage());
            return false; // fail
        }
        catch(MyException ex)                 // show error messages
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return false; // fail
        }
    }
////////////////////////////////////////////////////////////////////////////////
}