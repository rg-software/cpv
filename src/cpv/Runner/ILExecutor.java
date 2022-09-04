package cpv.Runner;

import cpv.*;

//------------------------------------------------------------------------
// executes IL program statements
//------------------------------------------------------------------------
public class ILExecutor
{
	private static ILExecutor ref = null;    // reference to the unique ILExecutor object
	
//------------------------------------------------------------------------
	private ILExecutor() 	      			// to prevent manual creation
	{
	}
//------------------------------------------------------------------------
	public static ILExecutor getExecutor()   // get the reference to the singleton
	{
		if (ref == null)
			ref = new ILExecutor();
		return ref;
	}
//------------------------------------------------------------------------
    // perform an operation on two variables (a + b, x or y...)
    private Variable PerformOperation(Variable lhs, Variable rhs, String op) throws RuntimeErrorException
    {
        if (op.equals("+") || op.equals("-"))                // for integer variables only
        {
            int left = ((IntVariable)lhs).getValue();
            int right = ((IntVariable)rhs).getValue();
            int result = 0;

            if (op.equals("+"))                             // perform operation
                result = left + right;
            else if (op.equals("-"))
                result = left - right;

            return new IntVariable("__unnamed", result - 1, result + 1, result);   // return result
        }
        else if (op.equals("and") || op.equals("or"))                // for boolean variables only
        {
            boolean left = ((BoolVariable)lhs).getValue();
            boolean right = ((BoolVariable)rhs).getValue();
            boolean result = false;

            if (op.equals("and"))                                    // perform operation
                result = left && right;
            else if (op.equals("or"))
                result = left || right;

            return new BoolVariable("__unnamed", result);           // return result
        }
        else
            throw new RuntimeErrorException("Illegal operation " + op);    // unknown operation detected
    }
//------------------------------------------------------------------------
    // execute assignment operation.
    // pgs and pcs describe the current situation (program state and process)
    // v is a vector of strings, forming some statement like "asgn x 10 goto 3"
    // this routine modifies program and process states
    private void ExecuteAssignment(ProgramState pgs, ProcessState pcs, String[] v) throws RuntimeErrorException
    {
        if(v[2].equals("not"))   // NOT-assignment (asgn x not y goto N)
        {
            boolean rvalue = !((BoolVariable)pgs.ReadValue(pcs, v[3])).getValue();   // obtain a value of "not y"

            pgs.WriteValue(pcs, v[1], new BoolVariable("__unnamed", rvalue));        // perform assignment
            pcs.CurLineOffset = Integer.parseInt(v[5]);                              // set instruction pointer
        }
        else								             // asgn x y [op z]
        {
            int nextline;
            Variable rvalue1 = pgs.ReadValue(pcs, v[2]);    // read value of y

            if(v.length > 5)                           		// assignment with "op z"
            {
                Variable rvalue2 = pgs.ReadValue(pcs, v[4]);   // read value of z

                rvalue1.setValue(PerformOperation(rvalue1, rvalue2, v[3]));     // perform assignment y := y op z
                nextline = Integer.parseInt(v[6]);                              // obtain new instruction pointer
            }
            else                                                                // assignment without "op z": asgn x y
                nextline = Integer.parseInt(v[4]);                              // obtain new instruction pointer

            pgs.WriteValue(pcs, v[1], rvalue1);                                 // perform assignment x := y
            pcs.CurLineOffset = nextline;                                       // set new instruction pointer
        }
    }
//------------------------------------------------------------------------
    // execute branching operation
    // strings in v form a statement like "if a < b goto N else M"
    private void ExecuteBranching(ProgramState pgs, ProcessState pcs, String[] v) throws RuntimeErrorException
    {
        Variable lvalue = pgs.ReadValue(pcs, v[1]); // read a value of a
        Variable rvalue = pgs.ReadValue(pcs, v[3]); // read a value of b
        String op = v[2];                           // read operation
        boolean result;

        if (lvalue instanceof BoolVariable)          // if left value has boolean type
        {
            boolean lhs = ((BoolVariable)lvalue).getValue();   // get both values
            boolean rhs = ((BoolVariable)rvalue).getValue();

            result = (op.equals("=") && lhs == rhs) || (op.equals("<>") && lhs != rhs);  // obtain result
        }
        else                                                 // else assuming integer types
        {
            int lhs = ((IntVariable)lvalue).getValue();      // get both values
            int rhs = ((IntVariable)rvalue).getValue();

            result = (op.equals("<") && lhs < rhs) || (op.equals(">") && lhs > rhs) ||        // obtain result
                     (op.equals("<=") && lhs <= rhs) || (op.equals(">=") && lhs >= rhs) ||
                     (op.equals("=")  && lhs == rhs) || (op.equals("<>") && lhs != rhs);
        }

        pcs.CurLineOffset = Integer.parseInt(v[result ? 5 : 7]);   // set new instruction pointer (yes or no case)
    }
//------------------------------------------------------------------------
    // execute operation on semaphore (wait s / signal s)
    private void ExecuteSemaphoreOp(ProgramState pgs, ProcessState pcs, String[] v) throws RuntimeErrorException
    {
        if (v[0].equals("wait"))                        // wait s goto N
        {
            Variable sem = pgs.ReadValue(pcs, v[1]);    // read semaphore

            if (!(sem instanceof SemVariable))           // incorrect variable
                throw new RuntimeErrorException("Variable " + v[1] + " is not a semaphore");

            int var = ((SemVariable)sem).getValue();    // get semaphore value
            if (var > 0)                                 // if value > 0, just perform operation
            {
                pgs.WriteValue(pcs, v[1], new SemVariable("__unnamed", var - 1));  // write new semaphore value
                pcs.CurLineOffset = Integer.parseInt(v[3]);                        // perform goto
                pcs.blocked = false;                                               // process is NOT blocked
            }
            else                                                // freeze the process
                pcs.blocked = true;                             // note! no goto since the process is blocked
        }
        else if (v[0].equals("signal"))                   // signal s goto N
        {
            Variable sem = pgs.ReadValue(pcs, v[1]);     // read semaphore
            if (!(sem instanceof SemVariable))            // incorrect variable
                throw new RuntimeErrorException("Variable " + v[1] + " is not a semaphore");

            // set new semaphore value
            pgs.WriteValue(pcs, v[1], new SemVariable("__unnamed", ((SemVariable)sem).getValue() + 1));
            pcs.CurLineOffset = Integer.parseInt(v[3]);          // perform goto
        }
    }
//------------------------------------------------------------------------
    // execute operation on semaphore (wait s / signal s)
    private void ExecuteComment(ProgramState pgs, ProcessState pcs, String[] v) throws RuntimeErrorException
    {
    	pcs.CurLineOffset = Integer.parseInt(v[v.length - 1]);
    }
//------------------------------------------------------------------------
    // generic Execute command routine
    public void Execute(ProgramState pgs, ProcessState pcs, String Command) throws RuntimeErrorException
    {
        String[] v = Command.split(" ");

        // zero element is the command: get command type
        if (v[0].equals("asgn"))     // assignment: asgn x y [op z] goto N
            ExecuteAssignment(pgs, pcs, v);
        else if (v[0].equals("if"))  // branching: if A op B goto K else N
            ExecuteBranching(pgs, pcs, v);
        else if (v[0].equals("wait") || v[0].equals("signal"))     // semaphore operation
            ExecuteSemaphoreOp(pgs, pcs, v);
        else if (v[0].equals("rem")) // comment
        	ExecuteComment(pgs, pcs, v);
        else
            throw new RuntimeErrorException("Unknown instruction: " + v[0]);
    }
//------------------------------------------------------------------------
}