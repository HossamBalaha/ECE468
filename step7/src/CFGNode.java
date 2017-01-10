public class CFGNode {
	private IRNode irNode;
	private ArrayList<CFGNode> predecessor;
	private ArrayList<CFGNode> successor;
	private boolean leader;

	public CFGNode(IRNode irNode) {
		this.irNode = irNode;
		this.predecessor = new ArrayList<CFGNode>();
		this.successor = new ArrayList<CFGNode>();
		this.leader = false;
	}

	public IRNode getirNode() {
		return this.irNode;
	}

	public ArrayList<CFGNode> getPredecessor() {
		return this.predecessor;
	}

	public ArrayList<CFGNode> getSuccessor() {
		return this.successor;
	}

	public boolean getLeader() {
		return this.leader;
	}
}