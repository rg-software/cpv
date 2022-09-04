package cpv.Runner;

import cpv.RuntimeErrorException;

//------------------------------------------------------------------------
public class BoolVariable extends Variable        // Boolean variable
{
    private boolean CurrValue;                    // current value of the variable
    
//------------------------------------------------------------------------
    public BoolVariable(String name, boolean currvalue) // make a variable using name and value
    {
            Init(name, currvalue);
    }
//------------------------------------------------------------------------
    public BoolVariable(String str) throws RuntimeErrorException // make from textual representation
    {                                                            // "VarName InitValue"
        String[] s = str.split(" ");

        if (s[1].equals("true"))
                Init(s[0], true);
        else if (s[1].equals("false"))
                Init(s[0], false);
        else
            throw new RuntimeErrorException("Error: value of '" + Name + "' should be either 'true' or 'false'");
    }
//------------------------------------------------------------------------
    private void Init(String name, boolean currvalue)           // variable initialization
    {
        Name = name;
        CurrValue = currvalue;
    }
//------------------------------------------------------------------------
    public String getValueAsString()                            // return textual representation
    {
            return CurrValue ? "true" : "false";
    }
//------------------------------------------------------------------------
    public Variable MakeClone()                                 // make a clone of the object
    {
        return new BoolVariable(Name, CurrValue);
    }
//------------------------------------------------------------------------
    public boolean getValue()                                   // get variable value
    {
        return CurrValue;
    }
//------------------------------------------------------------------------
    public void setValue(Variable v) throws RuntimeErrorException  // set variable value using variable as rvalue
    {
        setValue(((BoolVariable)v).getValue());
    }
//------------------------------------------------------------------------
    public void setValue(boolean newvalue)                        // set variable value using constant as rvalue
    {
        CurrValue = newvalue;
    }
//------------------------------------------------------------------------
}