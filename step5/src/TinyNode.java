public class TinyNode {
	
	private String opCode, operand1, operand2;

	public TinyNode(String inputOpCode, String inputOperand1, String inputOperand2) {
		this.opCode = inputOpCode;
		this.operand1 = inputOperand1;
		this.operand2 = inputOperand2;
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

	public void printNode() {
		String operand1 = (this.operand1 == null) ? "" : " " + this.operand1;
		String operand2 = (this.operand2 == null) ? "" : " " + this.operand2;
		String opCode = (this.opCode == null) ? "" : this.opCode;
		System.out.println(opCode + operand1 + operand2);
	}

}