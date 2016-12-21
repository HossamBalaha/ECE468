import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Micro {
	public static void main( String[] args) throws Exception {
		CharStream chars = new ANTLRFileStream(args[0]);
		MicroLexer scanner = new MicroLexer(chars);
		Vocabulary vocabulary = scanner.getVocabulary();			
		CommonTokenStream tokens = new CommonTokenStream((TokenSource)scanner);
		tokens.fill();
		for(Object o: tokens.getTokens()){
			CommonToken token = (CommonToken)o;
			System.out.println("Token Type: " + vocabulary.getDisplayName(token.getType()));
			System.out.println("Value: " + token.getText());					
		}		
	}	
}