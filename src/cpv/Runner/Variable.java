package cpv.Runner;

import cpv.RuntimeErrorException;
import java.io.*;

//------------------------------------------------------------------------
public abstract class Variable implements Serializable   // a parent for all variable types
{
    public String Name;                       		// variable name

    abstract public Variable MakeClone();          // make copy of an object
    abstract public String getValueAsString();     // get textual representation of variable's value
    abstract public void setValue(Variable v) throws RuntimeErrorException;   // set new variable value
}
