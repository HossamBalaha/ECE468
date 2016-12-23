import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.HashMap;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class MicroIRListener extends MicroBaseListener{
	private ArrayList<IRNode> IRList = new ArrayList<IRNode>();
	private ArrayList<TinyNode> TNList = new ArrayList<TinyNode>();
	private ParseTreeProperty<Node> PTProperty =  new ParseTreeProperty<Node>();
	private HashMap<String, String> typeMap = new HashMap<String, String>();
	private HashMap<String, String> regMap = new HashMap<String, String>();
	private HashMap<String, String> tinyMap = new HashMap<String, String>();
	private ArrayList<String> opList = new ArrayList<String>();
	private Stack<LabelNode> labelStack = new Stack<LabelNode>();
	private int registerCount = 1;
	private int tinyCount = 0;
	private int labelNum = 1;
	private int localNum = 0;
	private int paraNum = 0;

	private void addopList() {
		opList.add("ADDI");
		opList.add("ADDF");
		opList.add("SUBI");
		opList.add("SUBF");
		opList.add("MULTI");
		opList.add("MULTF");
		opList.add("DIVI");
		opList.add("DIVF");
		return;
	}

	private String getTinyReg(String str) {
		if(str.contains("$") && regMap.containsKey(str)) {
			return regMap.get(str);
		} else if(str.contains("$") && str.length() > 2){
			if (str.charAt(1) == 'L') {
				regMap.put(str,("$-" + str.substring(2)));
				return ("$-" + str.substring(2));
			} else if(str.charAt(1) == 'P') {
				regMap.put(str, ("$" + (6 + paraNum - Integer.parseInt(str.substring(2)))));
				return ("$" + (6 + paraNum - Integer.parseInt(str.substring(2))));
			} else if (str.charAt(1) == 'R') {
				regMap.put(str, "$" + (6 + paraNum));
				return ("$" + (6 + paraNum));
			} else {
				regMap.put(str, ("r" + tinyCount++));
				return ("r" + tinyCount++);
			}
		} else {
			String s = "r" + tinyCount++;
			regMap.put(str, s);
			return s;
		}
	}

	private String getReg(){
		return "$T" + Integer.toString(registerCount++);
	}
	private String convertTinyReg(String input){
		if (input == null) {
			return null;
		}
		if (input.contains("$")) {
			return "r" + Integer.toString(Integer.parseInt(input.substring(2))-1);
		}
		return input;
	}

	private Node getNode(ParseTree ctx) {

		if (ctx == null || ctx.getText().equals("")) {
			return null;
		} else {
			return PTProperty.get(ctx);
		}
	}

	private String getLabel() {
		return "label" + (Integer.toString(labelNum++));
	}

	private String getLocalNum() {
		return (Integer.toString(labelNum));
	}

	private String checkType(String input) {

		if (typeMap.get(input) == null) {
			if(input.matches("[0-9]+")){
				return "INT";
			} else if(input.matches("[0-9]*\\.[0-9]+")){
				return "FLOAT";
			} else{
				return "STRING";
			}
		}else{
			return "ID";
		}
	}

	//Macth operators with opCodes
	private String matchOpCode(String opCode, String type) {
		switch (opCode) {
			case "+": return (type.equals("INT")) ? "ADDI" : "ADDF";
			case "-": return (type.equals("INT")) ? "SUBI" : "SUBF";
			case "*": return (type.equals("INT")) ? "MULTI" : "MULTF";
			case "/": return (type.equals("INT")) ? "DIVI" : "DIVF";
		}

		return "OpCode not valid";

	}
	//Convert IR opCode to Tiny opCode
	private String getOp(String opCode) {
		switch (opCode) {
			case "ADDI": return "addi";
			case "SUBI": return "subi";
			case "ADDF": return "addr";
			case "SUBF": return "subr";
			case "MULTI": return "muli";
			case "DIVI": return "divi";
			case "MULTF": return "mulr";
			case "DIVF": return "divr";
			case "WRITEI": return "sys writei";
			case "WRITEF": return "sys writer";
			case "READI": return "readi";
			case "READF": return "readf";
			case "EQ": return "jeq";
			case "NE": return "jne";
			case "GT": return "jgt";
			case "GE": return "jge";
			case "LT": return "jlt";
			case "LE": return "jle";
			case "JUMP": return "jmp";
			case "LABEL": return "label";
			case "LINK": return "link";
			case "WRITES": return "writes";
		}
		return opCode;
	}

	public void convertIRtoTiny(IRNode irNode) {
		addopList();
		String opCode = irNode.getOpCode();
		String operand1 = irNode.getOperand1();
		String operand2 = irNode.getOperand2();
		String result = irNode.getResult();
		String temp;
		String type;
		if(opCode.equals("STOREI") || opCode.equals("STOREF")) {
			if(operand1.contains("$")) {
				TNList.add(new TinyNode("move", getTinyReg(operand1), result));
			} else if (result.contains("$")) {
				TNList.add(new TinyNode("move", operand1, getTinyReg(result)));
			} else {
				temp = getTinyReg(operand1);
				TNList.add(new TinyNode("move", operand1, temp));
				TNList.add(new TinyNode("move", temp, result));
			} 
			if(opCode.equals("STOREI") && result.contains("$")){
				tinyMap.put(result,"INT");
			} else if (opCode.equals("STOREF") && result.contains("$")){
				tinyMap.put(result,"FLOAT");
			}
		} else if(opCode.equals("WRITEI") || opCode.equals("WRITEF") || opCode.equals("WRITES") || opCode.equals("READI") || opCode.equals("READF")) {
			if(opCode.contains("$")) {
				TNList.add(new TinyNode("sys", getOp(opCode), getTinyReg(result)));
			} else {
				TNList.add(new TinyNode("sys", getOp(opCode), result));
			}
			TNList.add(new TinyNode(getOp(opCode), null, result));
		} else if(opCode.equals("LINK")) {
			TNList.add(new TinyNode(getOp(opCode), null, getLocalNum()));
		} else if(opList.contains(opCode)){
			if (operand1.contains("$") && operand2.contains("$")) {
				temp = getTinyReg(operand1);
				TNList.add(new TinyNode(getOp(opCode), getTinyReg(operand2), temp));
			} else if (operand1.contains("$")) {
				temp = getTinyReg(operand1);
				TNList.add(new TinyNode(getOp(opCode), operand2, temp));
			} else if (operand2.contains("$")) {
				temp = getTinyReg(operand1);
				TNList.add(new TinyNode("move", operand1, temp));
				TNList.add(new TinyNode(getOp(opCode), getTinyReg(operand2), temp));
			} else {
				temp = getTinyReg(operand1);
				TNList.add(new TinyNode("move", operand1, temp));
				TNList.add(new TinyNode(getOp(opCode), operand2, temp));
			}
			regMap.put(result,temp);
		} else if (opCode.equals("LABEL") || opCode.equals("JUMP")) {
			TNList.add(new TinyNode(getOp(opCode), null, result));
		} else {
			//case op2 is constant
			temp = getTinyReg(operand2);
			if (!operand2.contains("$")) {
				TNList.add(new TinyNode("move", operand2, temp));
			}
			// cmp type

			if(checkType(operand1).equals("ID")){
				type = typeMap.get(operand1);
			} else if (checkType(operand1).equals("INT")){
				type = "INT";
			} else if (checkType(operand1).equals("FLOAT")) {
				type = "FLOAT";
			} else {
				type = "ERROR";
			}

			if (tinyMap.get(operand1) != null && tinyMap.get(operand1).equals("INT") && tinyMap.get(operand2).equals("INT")) {
				TNList.add(new TinyNode("cmpi", getTinyReg(operand1), temp));
			} else if(tinyMap.get(operand1) != null && tinyMap.get(operand1).equals("FLOAT") && tinyMap.get(operand2).equals("FLOAT")) {
				TNList.add(new TinyNode("cmpr", getTinyReg(operand1), temp));
			} else if (tinyMap.get(operand2) != null && tinyMap.get(operand2).equals("INT")) {
				TNList.add(new TinyNode("cmpi", operand1, temp));
			} else if(tinyMap.get(operand2) != null && tinyMap.get(operand2).equals("FLOAT")) {
				TNList.add(new TinyNode("cmpr", operand1, temp));
			} else if(type.equals("INT")) {
				TNList.add(new TinyNode("cmpi", operand1, temp));
			} else if(type.equals("FLOAT")) {
				TNList.add(new TinyNode("cmpr", operand1, temp));
			}
			TNList.add(new TinyNode(getOp(opCode), null, result));
		}

	}

//Generate IR Node
	@Override public void exitPgm_body(MicroParser.Pgm_bodyContext ctx) { 
		System.out.println(";IR code");
		for (int i = 0; i < IRList.size(); i++) {
			IRList.get(i).printNode();
			convertIRtoTiny(IRList.get(i));
		}
		TNList.add(new TinyNode("sys halt", null, null));
		System.out.println(";tiny code");
		for (int i = 0; i < TNList.size(); i++) {
			TNList.get(i).printNode();
		}
		

	}

	@Override public void exitVar_decl(MicroParser.Var_declContext ctx) {
		String type = ctx.getChild(0).getText();
		String[] idList = ctx.getChild(1).getText().trim().split(",");
		for (int i = 0; i < idList.length; i++) {
			TNList.add(new TinyNode("var", idList[i], null));
			typeMap.put(idList[i], type);
		}
	}

	@Override public void exitString_decl(MicroParser.String_declContext ctx) {
		String name = ctx.getChild(1).getText();
		String value = ctx.getChild(3).getText();
		typeMap.put(name, "STRING");
		TNList.add(new TinyNode("str", name, value));
	}


	@Override public void exitId(MicroParser.IdContext ctx) {
		Node node = new Node(null, ctx.getText(),typeMap.get(ctx.getText()));
		PTProperty.put(ctx,node);
		
	}

	@Override public void exitPrimary(MicroParser.PrimaryContext ctx){

		Node expr = getNode(ctx.getChild(1));

		if (expr == null) {
			String primary = ctx.getChild(0).getText();
			String type = checkType(primary);
			
			if(type.equals("FLOAT") || type.equals("INT")) {
				String regName = getReg();
				String opCode = (type.equals("INT")) ? "STOREI" : "STOREF";
				IRNode irNode = new IRNode(opCode, primary, null, regName);
				IRList.add(irNode);
				Node node = new Node(null, regName, type);
				PTProperty.put(ctx,node);
			} else {
				Node node = getNode(ctx.getChild(0));
				PTProperty.put(ctx,node);
			}
		} else {
			PTProperty.put(ctx, expr);
		}
	}

	@Override public void exitFactor(MicroParser.FactorContext ctx){
		Node factor_prefix = getNode(ctx.getChild(0));
		Node postfix_expr = getNode(ctx.getChild(1));
		String postfixText = postfix_expr.getValue();
		String postfixType = postfix_expr.getType();

		if(factor_prefix == null) {
			Node factor = new Node(null, postfixText, postfixType);
			PTProperty.put(ctx, factor);
		} else {
			String regName = getReg();
			String opCode = matchOpCode(factor_prefix.getOpCode(), postfixType);
			IRNode irNode = new IRNode(opCode, factor_prefix.getValue(), postfixText, regName);
			IRList.add(irNode);
			Node factor = new Node(null, regName, postfixType);
			PTProperty.put(ctx, factor);
		}
	}

	@Override public void exitExpr_prefix(MicroParser.Expr_prefixContext ctx){
		if(ctx.getText() == ""){
			return;
		}
		Node expr_prefix = getNode(ctx.getChild(0));
		Node factor = getNode(ctx.getChild(1));

		if(expr_prefix == null) {
			Node expr_prefixNew = new Node(ctx.getChild(2).getText(), factor.getValue(), factor.getType());
			PTProperty.put(ctx, expr_prefixNew);
		} else {
			String regName = getReg();
			String opCode = matchOpCode(expr_prefix.getOpCode(), factor.getType());
			IRNode irNode = new IRNode(opCode, expr_prefix.getValue(), factor.getValue(), regName);
			IRList.add(irNode);
			Node expr_prefixNew = new Node(ctx.getChild(2).getText(), regName, factor.getType());
			PTProperty.put(ctx, expr_prefixNew);
		}

	}

	@Override public void exitPostfix_expr(MicroParser.Postfix_exprContext ctx) {
		PTProperty.put(ctx, getNode(ctx.getChild(0)));
	}

	@Override public void exitFactor_prefix(MicroParser.Factor_prefixContext ctx) {

		if (ctx.getText() == "") {
			return;
		} else {
			Node factor_prefix = getNode(ctx.getChild(0));
			Node postfix_expr = getNode(ctx.getChild(1));
			String mulop = ctx.getChild(2).getText();
			if(factor_prefix == null) {
				Node node = new Node(mulop, postfix_expr.getValue(), postfix_expr.getType());
				PTProperty.put(ctx, node);
			} else {
				String regName = getReg();
				String value = factor_prefix.getValue();
				String opCode = matchOpCode(factor_prefix.getOpCode(), postfix_expr.getType());
				IRNode irNode = new IRNode(opCode, value, postfix_expr.getValue(), regName);
				IRList.add(irNode);
				Node node = new Node(mulop, value, postfix_expr.getType());
				PTProperty.put(ctx, node);

			}
		}
	}

	@Override public void exitExpr(MicroParser.ExprContext ctx){
		Node expr_prefix = getNode(ctx.getChild(0));
		Node factor = getNode(ctx.getChild(1));
		String factorValue = factor.getValue();
		String factorType = factor.getType();

		if (expr_prefix != null) {
			String regName = getReg();
			String value = expr_prefix.getValue();
			String addop = expr_prefix.getOpCode();
			String opCode = matchOpCode(addop, factorType);
			IRNode irNode = new IRNode(opCode, value, factorValue, regName);
			IRList.add(irNode);
			Node node = new Node(null, regName, factorType);
			PTProperty.put(ctx, node);
		} else {
			Node node = new Node(null, factorValue, factorType);
			PTProperty.put(ctx, node);
		}
	}


	@Override public void exitAssign_expr(MicroParser.Assign_exprContext ctx){
		String type = typeMap.get(ctx.getChild(0).getText());
		Node expr = getNode(ctx.getChild(2));
		String opName = "";
		if(type == null) {
			return;
		} else if(type.equals("INT")) {
			opName = "STOREI";
		} else if(type.equals("FLOAT")) {
			opName = "STOREF";
		} else {
			return;
		}
		IRNode irNode = new IRNode(opName,expr.getValue(), null, ctx.getChild(0).getText());
		IRList.add(irNode);
	}

	@Override public void exitWrite_stmt(MicroParser.Write_stmtContext ctx){
		if (ctx.getChild(2) == null || ctx.getChild(2).getText().length() == 0) {
			return;
		} else {
			String[] idList = ctx.getChild(2).getText().trim().split(",");
			for (int i = 0; i < idList.length; i++) {
				String type = typeMap.get(idList[i]);
				if(type.equals("INT")) {
					IRNode irNode = new IRNode("WRITEI", null, null, idList[i]);
					IRList.add(irNode);
				} else if (type.equals("FLOAT")) {
					IRNode irNode = new IRNode("WRITEF", null, null, idList[i]);
					IRList.add(irNode);
				} else {
					System.out.println("Invalid type to write");
				}
			}
		}
	}
	@Override public void exitRead_stmt(MicroParser.Read_stmtContext ctx){
		if (ctx.getChild(2) == null || ctx.getChild(2).getText().length() == 0) {
			return;
		} else {
			String[] idList = ctx.getChild(2).getText().trim().split(",");
			for (int i = 0; i < idList.length; i++) {
				String type = typeMap.get(idList[i]);
				if(type.equals("INT")) {
					IRNode irNode = new IRNode("READI", null, null, idList[i]);
					IRList.add(irNode);
				} else if (type.equals("FLOAT")) {
					IRNode irNode = new IRNode("READF", null, null, idList[i]);
					IRList.add(irNode);
				} else {
					System.out.println("Invalid type to read");
				}
			}
		}
	}

	@Override public void enterDo_while_stmt(MicroParser.Do_while_stmtContext ctx) {
		String headLabel = getLabel();
		String outLabel = getLabel();
		labelStack.push(new LabelNode("do_while", outLabel, headLabel));
		IRList.add(new IRNode("LABEL", null, null, headLabel));
	}

	@Override public void exitDo_while_stmt(MicroParser.Do_while_stmtContext ctx) {
		String outLabel = labelStack.peek().getOutLabel();
		String headLabel = labelStack.pop().getHeadLabel();
		IRList.add(new IRNode("JUMP", null, null, outLabel));
		IRList.add(new IRNode("LABEL", null, null, headLabel));
	}

	//if statement
	@Override public void enterIf_stmt(MicroParser.If_stmtContext ctx) {
		String headLabel = getLabel();
		String outLabel = getLabel();
		labelStack.push(new LabelNode("if", headLabel, outLabel));
	}
	@Override public void exitIf_stmt(MicroParser.If_stmtContext ctx) {
		while(labelStack.peek().getName() != "if") {
			labelStack.pop();
		}
		IRList.add(new IRNode("LABEL", null, null, labelStack.pop().getOutLabel()));
	}
	//else if statement
	@Override public void enterElse_part(MicroParser.Else_partContext ctx) {
		String headLabel = labelStack.peek().getHeadLabel();
		String outLabel = labelStack.peek().getOutLabel();
		IRList.add(new IRNode("JUMP", null, null, outLabel));
		IRList.add(new IRNode("LABEL", null, null, headLabel));
		if (ctx.getChildCount() > 1) {
			labelStack.push(new LabelNode("else_if", getLabel(), outLabel));
		}
	}
	//condition block
	@Override public void exitCond(MicroParser.CondContext ctx) {
		String headLabel = labelStack.peek().getHeadLabel();
		//True or False
		if (ctx.getChildCount() == 1) {
			String regName1 = getReg();
			String regName2 = getReg();
			IRList.add(new IRNode("STOREI", "1", null, regName1));
			IRList.add(new IRNode("STOREI", "1", null, regName2));
			String trueFalse = ctx.getChild(0).getText();
			if (trueFalse.equals("TRUE")) {
				IRList.add(new IRNode("NE", regName1, regName2, headLabel));
			} else if (trueFalse.equals("FALSE")){
				IRList.add(new IRNode("EQ", regName1, regName2, headLabel));
			}

			return;
		} else {
			Node expr1 = getNode(ctx.getChild(0));
			Node expr2 = getNode(ctx.getChild(2));
			String compop = ctx.getChild(1).getText();

			if (compop.equals("<")) {
				IRList.add(new IRNode("GE", expr1.getValue(), expr2.getValue(), headLabel));
			}else if (compop.equals(">")) {
				IRList.add(new IRNode("LE", expr1.getValue(), expr2.getValue(), headLabel));
			}else if (compop.equals("=")) {
				IRList.add(new IRNode("NE", expr1.getValue(), expr2.getValue(), headLabel));
			}else if (compop.equals("!=")) {
				IRList.add(new IRNode("EQ", expr1.getValue(), expr2.getValue(), headLabel));
			}else if (compop.equals("<=")) {
				IRList.add(new IRNode("GT", expr1.getValue(), expr2.getValue(), headLabel));
			}else if (compop.equals(">=")) {
				IRList.add(new IRNode("LT", expr1.getValue(), expr2.getValue(), headLabel));
			}
		}

	}


//END Generate IR Node
}



class Node {
	private String opCode, value, type;

	public Node(String inputOpCode, String inputValue, String inputType) {
		this.opCode = inputOpCode;
		this.value = inputValue;
		this.type = inputType;
	}

	public String getOpCode() {
		return this.opCode;
	}

	public String getValue() {
		return this.value;
	}

	public String getType() {
		return this.type;
	}
}

class LabelNode {
	private String name, headLabel, outLabel;
	public LabelNode(String name, String headLabel, String outLabel) {
		this.name = name;
		this.headLabel = headLabel;
		this.outLabel = outLabel;
	}

	public String getName() {
		return this.name;
	}
	public String getHeadLabel() {
		return this.headLabel;
	}
	public String getOutLabel() {
		return this.outLabel;
	}
}