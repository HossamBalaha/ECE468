import java.util.ArrayList;
import java.util.Stack;
import java.util.HashMap;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class MicroIRListener extends MicroBaseListener{
	private ArrayList<IRNode> IRList = new ArrayList<IRNode>();
	private ParseTreeProperty<Node> PTProperty =  new ParseTreeProperty<Node>();
	private HashMap<String, String> typeMap = new HashMap<String, String>();
	private int registerCount = 1;

	private String getReg(){
		return "$T" + Integer.toString(registerCount++);
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
			} else if(input.matches("[0-9]*'.'[0-9]+")){
				return "FLOAT";
			}else{
				return "STRING";
			}
		}else{
			return typeMap.get(input);
		}
	}

	private String matchOpCode(String opCode, String type) {
		switch (opCode) {
			case "+": return (type == "INT") ? "ADDI" : "ADDF";
			case "-": return (type == "INT") ? "SUBI" : "SUBF";
			case "*": return (type == "INT") ? "MULTI" : "MULTF";
			case "/": return (type == "INT") ? "DIVI" : "DIVF";
		}

		return "OpCode not valid";

	}
	@Override public void exitVar_decl(MicroParser.Var_declContext ctx) {
		String type = ctx.getChild(0).getText();
		String[] idList = ctx.getChild(1).getText().trim().split(",");
		for (int i = 0; i < idList.length; i++) {
			typeMap.put(idList[i], type);
		}
	}

	@Override public void exitId(MicroParser.IdContext ctx) {
		Node node = new Node(null, ctx.getText(),typeMap.get(ctx.getText()));
		PTProperty.put(ctx,node);
		
	}
	@Override public void exitExpr(MicroParser.ExprContext ctx) {

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
				//System.out.println(opCode + " " + primary + " "+ regName + " " + type);
			} else {
				Node node = getNode(ctx.getChild(0));
				PTProperty.put(ctx,node);
				//System.out.println(primary + " " + type);
			}
		} else {
			PTProperty.put(ctx, expr);
			//System.out.println(ctx.getChild(1).getText());
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
			PTProperty.put(ctx.expr_prefixNew);
		} else {
			String regName = getReg();
			String opCode = matchOpCode(expr_prefix.getOpCode(), factor.getType());
			IRNode irNode = new IRNode(opCode, expr_prefix.getValue(), factor.getValue(), regName);
			IRList.add(irNode);
			Node expr_prefixNew = new Node(ctx.getChild(2).getText(), regName, factor.getType());
			PTProperty.put(ctx.expr_prefixNew);
		}

	}

	@Override public void exitPostfix_expr(MicroParser.Postfix_exprContext ctx) {
		PTProperty.put(ctx, getNode(ctx.getChild(0)));
	}

	@Override public void exitFactor_prefix(MicroParser.Factor_prefixContext ctx) {
		if (ctx.getChild(0) == null) {
			return;
		} else {
			Node factor_prefix = getNode(ctx.getChild(0));
			Node postfix_expr = getNode(ctx.getChild(0));
			String mulop = ctx.getChild(2).getText();

			if(factor_prefix == null) {
				Node node = new Node(mulop, postfix_expr.getValue(), postfix_expr.getType());
				PTProperty.put(ctx, node);
			} else {
				String regName = getReg();
				String value = factor_prefix.getValue();
				String opCode = factor_prefix.getOpCode();

			}
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