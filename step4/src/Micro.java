import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Micro {
	public static void main( String[] args) throws Exception {
		CharStream chars = new ANTLRFileStream(args[0]);
		MicroLexer scanner = new MicroLexer(chars);
		CommonTokenStream tokens = new CommonTokenStream(scanner);
		MicroParser parser = new MicroParser(tokens);
		ANTLRErrorStrategy es = new CustomErrorStrategy();
		parser.setErrorHandler(es);
		try{
			ParseTree parse = parser.program();
			ParseTreeWalker walker = new ParseTreeWalker();
			MicroListener listener = new MicroListener();
			walker.walk(listener, parse);
		}catch(Exception e){
			System.out.println("Error from Micro");
		}
	}
}

class CustomErrorStrategy extends DefaultErrorStrategy{
	@Override
	public void reportError(Parser recognizer, RecognitionException e){
		throw e;
	}	
}	