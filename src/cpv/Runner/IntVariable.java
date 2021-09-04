package cpv.Runner;

import cpv.RuntimeErrorException;
import cpv.Application;
import javax.swing.*;

////////////////////////////////////////////////////////////////////////////////
public class IntVariable extends Variable              // Integer Variable
{
    private int MinValue, MaxValue;            // bound values
    private int CurrValue;                     // current value

////////////////////////////////////////////////////////////////////////////////
    // make a variable using its name and integer parameters
    public IntVariable(String name, int minvalue, int maxvalue, int currvalue) throws RuntimeErrorException
    {
            Init(name, minvalue, maxvalue, currvalue);
    }
////////////////////////////////////////////////////////////////////////////////
    // make from textual representation "VarName MinValue MaxValue InitValue"
    public IntVariable(String str) throws RuntimeErrorException
    {
        String[] s = str.split(" ");
        Init(s[0], Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]));
    }
////////////////////////////////////////////////////////////////////////////////
    // the real constructor :)
    private void Init(String name, int minvalue, int maxvalue, int currvalue) throws RuntimeErrorException
    {
        if(minvalue > maxvalue)
            throw new RuntimeErrorException("'" + name + "' declaration error: MinValue > MaxValue");
		if(currvalue < minvalue || currvalue > maxvalue)
			throw new RuntimeErrorException("'" + name + "' declaration error: CurrValue is outside [MinValue..MaxValue]");

        Name = name;
        MinValue = minvalue;
        MaxValue = maxvalue;
        CurrValue = currvalue;
    }
////////////////////////////////////////////////////////////////////////////////
    public String getValueAsString()                    // get textual value representation
    {
        return Integer.toString(CurrValue);
    }
////////////////////////////////////////////////////////////////////////////////
    public Variable MakeClone()                         // make a copy of the object
    {
        try
        {
            return new IntVariable(Name, MinValue, MaxValue, CurrValue);
        }
        catch(RuntimeErrorException e)   // should never be executed
        {
            return null;
        }
    }
////////////////////////////////////////////////////////////////////////////////
    public int getValue()                            // just retrieve variable value
    {
        return CurrValue;
    }
////////////////////////////////////////////////////////////////////////////////
    // set value, taken from another variable
    public void setValue(Variable v) throws RuntimeErrorException
    {
        setValue(((IntVariable)v).getValue());
    }
////////////////////////////////////////////////////////////////////////////////
    // assign an integer value to the variable
    public void setValue(int newvalue) throws RuntimeErrorException
    {
        if(newvalue >= MinValue && newvalue <= MaxValue)
            CurrValue = newvalue;
        else
            throw new RuntimeErrorException("Error: variable '" + Name + "' value is out of range");
    }
////////////////////////////////////////////////////////////////////////////////
}