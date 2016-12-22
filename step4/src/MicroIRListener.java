import java.util.ArrayList;
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
	private int registerCount = 1;
	private int tinyCount = 0;

	private String getTinyReg(String str) {
		if(str.contains("$") && regMap.containsKey(str)) {
			return regMap.get(str);
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

	private String checkType(String input) {

		if (typeMap.get(input) == null) {
			if(input.matches("[0-9]+")){
				return "INT";
			} else if(input.matches("[0-9]*\\.[0-9]+")){
				return "FLOAT";
			}else{
				return "STRING";
			}
		}else{
			return "ID";
		}
	}

	private String matchOpCode(String opCode, String type) {
		switch (opCode) {
			case "+": return (type.equals("INT")) ? "ADDI" : "ADDF";
			case "-": return (type.equals("INT")) ? "SUBI" : "SUBF";
			case "*": return (type.equals("INT")) ? "MULTI" : "MULTF";
			case "/": return (type.equals("INT")) ? "DIVI" : "DIVF";
		}

		return "OpCode not valid";

	}

	public void convertIRtoTiny(IRNode irNode) {
		String opCode = irNode.getOpCode();
		String operand1 = irNode.getOperand1();
		String operand2 = irNode.getOperand2();
		String result = irNode.getResult();
		String temp;
		if(opCode.equals("STOREI") || opCode.equals("STOREF")) {
			if(operand1.contains("$")) {
				TNList.add(new TinyNode("move", getTinyReg(operand1), result));
			} else {
				TNList.add(new TinyNode("move", operand1, getTinyReg(result)));
			}
		} else if(opCode.equals("WRITEI") || opCode.equals("WRITEF")) {
			TNList.add(new TinyNode(getOp(opCode), null, result));
		} else {
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

	@Override public void exitPgm_body(MicroParser.Pgm_bodyContext ctx) { 
		System.out.println(";IR code");
		for (int i = 0; i < IRList.size(); i++) {
			convertIRtoTiny(IRList.get(i));
			IRList.get(i).printNode();
			convertIRtoTiny(IRList.get(i));
		}
		TNList.add(new TinyNode("sys halt", null, null));
		System.out.println(";tiny code");
		for (int i = 0; i < TNList.size(); i++) {
			TNList.get(i).printNode();
		}

	}


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