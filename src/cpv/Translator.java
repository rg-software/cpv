package cpv;

import java.util.regex.*;

////////////////////////////////////////////////////////////////////////////////
public class Translator     // routines for Pascal-like syntax to IL translation
{
    private final String VarNameRE = "[a-zA-Z]\\w*";        // valid variable name
    private final String NumberRE = "[\\+\\-]?[0-9]+";      // integer number
    private final String BoolRE = "true|false";             // boolean constant
    private final String OperRE = "\\+|\\-|and|or";         // operation
    private final String IfOperRE = "\\<|\\>|\\<\\=|\\>\\=|\\=|\\<\\>";  // relation
    private final String OperandRE = VarNameRE + "|" + NumberRE + "|" + BoolRE;  // operand (variable, number or boolean constant)

	private static Translator ref = null;    // reference to unique Translator object
	
////////////////////////////////////////////////////////////////////////////////
	private Translator() {}      // to prevent manual creation
////////////////////////////////////////////////////////////////////////////////
	public static Translator getTranslator()   // get reference to the singleton
	{
		if(ref == null)
			ref = new Translator();
		return ref;
	}
////////////////////////////////////////////////////////////////////////////////
    // translate variable representation
    public String TranslateVariable(String value) throws SyntaxErrorException
    {
        Matcher m;

        // try boolean variable syntax: "x : boolean := value;" (convert to "bool VarName Value")
        m = Pattern.compile("(" + VarNameRE + ")\\s*:\\s*boolean\\s*:=\\s*(" + BoolRE + ");$").matcher(value);
        if(m.matches())
            return "bool " + m.group(1) + " " + m.group(2);

        // try integer variable syntax: "x : integer(min..max) := value;" (convert to "int VarName MinValue MaxValue CurrValue")
        m = Pattern.compile("(" + VarNameRE + ")\\s*:\\s*integer\\((" + NumberRE + ")\\.\\.(" + NumberRE + ")\\)\\s*:=\\s*(" + NumberRE + ");$").matcher(value);
        if(m.matches())
            return "int " + m.group(1) + " " + m.group(2) + " " + m.group(3) + " " + m.group(4);

        // try semaphore variable syntax: "x : semaphore := value;" (convert to "sem VarName Value")
        m = Pattern.compile("(" + VarNameRE + ")\\s*:\\s*semaphore\\s*:=\\s*(" + NumberRE + ");$").matcher(value);
        if(m.matches())
            return "sem " + m.group(1) + " " + m.group(2);

        // incorrect definition
        throw new SyntaxErrorException("Syntax error in variable definition: " + value);
    }
////////////////////////////////////////////////////////////////////////////////
    // translate construction
    public String TranslateConstruction(String value) throws SyntaxErrorException
    {
    	Matcher m;

    	// try comment instruction: "'comment"; "/*comment*/"; "(*comment*)" (convert to "rem comment")
            m = Pattern.compile("('|/\\*|\\(\\*)(.*)($|\\*/$|\\*\\)$)").matcher(value);
            if(m.matches())
		return "rem " + m.group(1);

            // try simple assignment: "x := y;" (convert to "asgn x y")
    	// x: variable, y: operand (variable, number or boolean constant)
    	m = Pattern.compile("(" + VarNameRE +")\\s*:=\\s*(" + OperandRE + ");$").matcher(value);
    	if(m.matches())
    		return "asgn " + m.group(1) + " " + m.group(2);

    	// try boolean not-assignment: "x := not y;" (convert to "asgn x not y")
    	// x: variable, y: variable or boolean constant
    	m = Pattern.compile("(" + VarNameRE +")\\s*:=\\s*not\\s*(" + VarNameRE + "|" + BoolRE + ");$").matcher(value);
    	if(m.matches())
    		return "asgn " + m.group(1) + " not " + m.group(2);

    	// try assignment with operation: "x := y op z;" (convert to "asgn x y op z")
    	// x: variable; y, z: operand; op: operation
    	m = Pattern.compile("(" + VarNameRE + ")\\s*:=\\s*(" + OperandRE + ")\\s*(" + OperRE + ")\\s*(" + OperandRE + ");$").matcher(value);
    	if(m.matches())
    		return "asgn " + m.group(1) + " " + m.group(2) + " " + m.group(3) + " " + m.group(4);

    	// try wait/signal construction: "wait(x);" or "signal(x);" (convert to "wait x", "signal x")
    	// x: variable
    	m = Pattern.compile("(wait|signal)\\((" + VarNameRE + ")\\);$").matcher(value);
    	if(m.matches())
    		return m.group(1) + " " + m.group(2);

    	// try simplified IF construction: "x" (convert to "if x = true")
            // x: operand
    	m = Pattern.compile("(" + OperandRE + ")$").matcher(value);
    	if(m.matches())
    		return "if " + m.group(1) + " = true";

    	// try simplified IF construction: "not x" (convert to "if x = false")
            // x: operand
    	m = Pattern.compile("not (" + OperandRE + ")$").matcher(value);
    	if(m.matches())
    		return "if " + m.group(1) + " = false";

            // try IF construction: "x ifop y" (convert to "if x ifop y")
    	// x, y: operand; op: if-operation: <, >, <=, >=, =, <>
    	m = Pattern.compile("(" + OperandRE + ")\\s*(" + IfOperRE + ")\\s*(" + OperandRE +")$").matcher(value);
    	if(m.matches())
    		return "if " + m.group(1) + " " + m.group(2) + " " + m.group(3);

    	throw new SyntaxErrorException("Unknown construction: " + value);
    }
////////////////////////////////////////////////////////////////////////////////
}