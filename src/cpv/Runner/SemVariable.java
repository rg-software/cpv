package cpv.Runner;

import cpv.RuntimeErrorException;

//------------------------------------------------------------------------
public class SemVariable extends Variable   // semaphore variable (very similar to integer)
{
    private int CurrValue;   // current value

//------------------------------------------------------------------------
    public SemVariable(String name, int currvalue)   // initialize using name and current value
    {
        Init(name, currvalue);
    }
//------------------------------------------------------------------------
    public SemVariable(String str) // make from textual representation "VarName InitValue"
    {
        String[] s = str.split(" ");

        Init(s[0], Integer.parseInt(s[1]));
    }
//------------------------------------------------------------------------
    private void Init(String name, int currvalue) // real constructor
    {
        Name = name;
        CurrValue = currvalue;
    }
//------------------------------------------------------------------------
    public Variable MakeClone()    // make a copy of this object
    {
        return new SemVariable(Name, CurrValue);
    }
//------------------------------------------------------------------------
    public int getValue()        // get a value of the semaphore
    {
        return CurrValue;
    }
//------------------------------------------------------------------------
    public void setValue(int newvalue)    // set new semaphore value
    {
        CurrValue = newvalue;
    }
//------------------------------------------------------------------------
    // set new value, taken from another variable
    public void setValue(Variable v) throws RuntimeErrorException
    {
        setValue(((SemVariable)v).getValue());
    }
//------------------------------------------------------------------------
    public String getValueAsString()   // get textual representation of the semaphore value
    {
        return Integer.toString(CurrValue);
    }
//------------------------------------------------------------------------
}