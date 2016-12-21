import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Stack;
import java.io.IOException;

class Symbol {
	private String name, type, value;

	public Symbol(String inputName, String inputType, String inputValue) {
		this.name = inputName;
		this.type = inputType;
		this.value =inputValue;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String inputValue) {
		this.value = inputValue;
	}

}

class SymbolTable {
	private String scope;
	private LinkedHashMap<String, Symbol> entryMap;

	public SymbolTable(String inputScope) {
		this.scope = inputScope;
		this.entryMap = new LinkedHashMap<String, Symbol>();
	}

	public String getScope() {
		return this.scope;
	}

	public LinkedHashMap<String, Symbol> getEntryMap() {
		return this.entryMap;
	}

	public void addEntry(Symbol entry) {
		if(this.entryMap.containsKey(entry.getName())) {
			System.out.println("DECLARATION ERROR " + entry.getName());
			System.exit(0);
		} else {
			this.entryMap.put(entry.getName(), entry);
		}
	}

	public void printSymbolTable() {
		System.out.println("Symbol table " +  this.scope);
		Iterator<Symbol> it = this.entryMap.values().iterator();
		while(it.hasNext()) {
			Symbol currSymbol = it.next();
			if(currSymbol.getType() == "STRING") {
				System.out.println("name " + currSymbol.getName + " type " + currSymbol.getType + " value " + currSymbol.getValue);
			} else {
				System.out.println("name " + currSymbol.getName + " type " + currSymbol.getType);
			}
		}
		System.out.println();
	}

}

public class SymbolTableStack {
	private Stack<SymbolTable> stack;
	private int blockCount;

	public SymbolTableStack() {
		this.stack = new Stack<SymbolTable>();
		this.blockCount = 1;
	}

	public void createScope(String inputName) {
		if (inputName.equals("global")) {
			this.stack.push(new SymbolTable("GLOBAL"));
		} else if (inputName.equals("block")){
			stack.push(new SymbolTable("BLOCK " + this.blockCount++));
		} else {
			stack.push(new SymbolTable(inputName));
		}
	}

	public void add(String inputName, String inputType) {
		String[] nameList = inputName.trim().split(",");
		SymbolTable currTable = this.stack.pop();
		for (int i = 0; i < nameList.length; i++) {
			Symbol sb = new Symbol(nameList[i], inputType, null);
			currTable.entryMap.put(sb.getName(), sb);
		}
		this.stack.push(currTable);
		currTable.printSymbolTable();
	}

	public void add(String inputName, String inputType, String inputValue) {
		SymbolTable currTable = this.stack.pop();
		Symbol sb = new Symbol(inputName, inputType, inputValue);
		currTable.entryMap.put(sb.getName(), sb);
		this.stack.push(currTable);
		currTable.printSymbolTable();
	}
}
