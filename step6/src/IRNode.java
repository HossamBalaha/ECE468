public class IRNode {
	
	private String opCode, operand1, operand2, result;

	public IRNode(String inputOpCode, String inputOperand1, String inputOperand2, String inputResult) {
		this.opCode = inputOpCode;
		this.operand1 = inputOperand1;
		this.operand2 = inputOperand2;
		this.result = inputResult;
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

	public void printNode() {
		String operand1 = (this.operand1 == null) ? "" : " " + this.operand1;
		String operand2 = (this.operand2 == null) ? "" : " " + this.operand2;
		String opCode = (this.opCode == null) ? "" : this.opCode;
		String result = (this.result == null) ? "" : " " + this.result;

		System.out.println(";" + opCode + operand1 + operand2 + result);
	}

}