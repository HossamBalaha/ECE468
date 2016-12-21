import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Micro {
  public static void main( String[] args) throws Exception 
  {
    MicroLexer lexer = new MicroLexer( new ANTLRFileStream(args[0]));
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    MicroParser parser = new MicroParser(tokens);

    ANTLRErrorStrategy es = new CustomErrorStrategy();
	parser.setErrorHandler(es);

	try{
		parser.program();
		System.out.println("Accepted")
	}catch(Exception e){
		System.out.println("Not Accepted")
	}

  }
}