import java.util.LinkedHashMap;
import java.util.Iterator;
import java.io.IOException;

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
				System.out.println("name " + currSymbol.getName() + " type " + currSymbol.getType() + " value " + currSymbol.getValue());
			} else {
				System.out.println("name " + currSymbol.getName() + " type " + currSymbol.getType());
			}
		}
		System.out.println();
	}

}