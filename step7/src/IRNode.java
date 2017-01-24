import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
public class IRNode {
	
	private String opCode, operand1, operand2, result;
	private ArrayList<IRNode> predecessor;
	private ArrayList<IRNode> successor;
	private Set<String> genSet;
	private Set<String> killSet;
	private Set<String> inSet;
	private Set<String> outSet;
	private boolean isLeader;

	public IRNode(String inputOpCode, String inputOperand1, String inputOperand2, String inputResult) {
		this.opCode = inputOpCode;
		this.operand1 = inputOperand1;
		this.operand2 = inputOperand2;
		this.result = inputResult;
		this.isLeader = false;
		this.predecessor = new ArrayList<IRNode>();
		this.successor = new ArrayList<IRNode>();
		this.genSet = new HashSet<String>();
		this.killSet = new HashSet<String>();
		this.inSet = new HashSet<String>();
		this.outSet = new HashSet<String>();
	}

	public void addInSet(String str) {
 		this.inSet.add(str);
	}

	public void addOutSet(String str) {
 		this.outSet.add(str);
	}

	public void addGenSet(String str) {
 		this.genSet.add(str);
	}

	public void addKillSet(String str) {
 		this.killSet.add(str);
	}

	public Set<String> getInSet() {
		return this.inSet;
	}

	public Set<String> getOutSet() {
		return this.outSet;
	}

	public Set<String> getGenSet() {
		return this.genSet;
	}

	public Set<String> getKillSet() {
		return this.killSet;
	}

	public void setInSet(Set<String> set) {
 		this.inSet = set;
	}

	public void setOutSet(Set<String> set) {
 		this.outSet = set;
	}

	public void setGenSet(Set<String> set) {
 		this.genSet = set;
	}

	public void setKillSet(Set<String> set) {
 		this.killSet = set;
	}

	public void addPredecessor(IRNode irnode) {
 		this.predecessor.add(irnode);
	}

	public ArrayList<IRNode> getPredecessor() {
		return this.predecessor;
	}

	public void addSuccessor(IRNode irnode) {
 		this.successor.add(irnode);
	}

	public ArrayList<IRNode> getSuccessor() {
		return this.successor;
	}

	public String getOpCode() {
		return this.opCode;
	}

	public String getOperand1() {
		return this.operand1;
	}

	public String getOperand2() {
		return this.operand2;
	}

	public String getResult() {
		return this.result;
	}

	public boolean getLeader() {
		return this.isLeader;
	}

	public void setLeader() {
		this.isLeader = true;
	}

	public void setOperand1(String operand1) {
		this.operand1 = operand1;
	}

	public void setOperand2(String operand2) {
		this.operand2 = operand2;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void printNode() {
		String operand1 = (this.operand1 == null) ? "" : " " + this.operand1;
		String operand2 = (this.operand2 == null) ? "" : " " + this.operand2;
		String opCode = (this.opCode == null) ? "" : this.opCode;
		String result = (this.result == null) ? "" : " " + this.result;
		if (operand1 == "" && operand2 == "" && opCode == "" && result == "") {
			System.out.println();
		} else {
			System.out.println(";" + opCode + operand1 + operand2 + result);
		}
		
	}

}