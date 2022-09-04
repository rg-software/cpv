package cpv;

// simple syntax error exception class
//------------------------------------------------------------------------
public class SyntaxErrorException extends MyException
{
    public SyntaxErrorException(String s)
    {
        super(s);
    }
}