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
			parser.program();
			System.out.println("Accepted");
		}catch(Exception e){
			System.out.println("Not Accepted");
		}
	}
	class CustomErrorStrategy extends DefaultErrorStrategy{
		@Override
		public void reportError(Parser recognizer, RecognitionException e){
			throw e;
		}	
	}	
}