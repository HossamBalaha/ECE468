import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Stack;
import java.io.IOException;

public class SymbolTableStack {
	public static Stack<SymbolTable> stack = new Stack<SymbolTable>();
	private static int blockCount = 1;

	public static void createScope(String inputName) {
		if (inputName.equals("global")) {
			stack.push(new SymbolTable("GLOBAL"));
		} else if (inputName.equals("block")){
			stack.push(new SymbolTable("BLOCK " + blockCount++));
		} else {
			stack.push(new SymbolTable(inputName));
		}
	}

	public static void add(String inputName, String inputType) {
		String[] nameList = inputName.trim().split(",");
		SymbolTable currTable = stack.pop();
		for (int i = 0; i < nameList.length; i++) {
			Symbol sb = new Symbol(nameList[i], inputType, null);
			currTable.addEntry(sb);
		}
		stack.push(currTable);
	}

	public static void add(String inputName, String inputType, String inputValue) {
		SymbolTable currTable = stack.pop();
		Symbol sb = new Symbol(inputName, inputType, inputValue);
		currTable.addEntry(sb);
		stack.push(currTable);
	}
}
