import java.util.ArrayList;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class MicroListener extends MicroBaseListener{
	
	void enterProgram(MicroParser.ProgramContext ctx);
	
	void exitProgram(MicroParser.ProgramContext ctx) {

	}
	void enterId(MicroParser.IdContext ctx);

	void exitId(MicroParser.IdContext ctx);

	void enterPgm_body(MicroParser.Pgm_bodyContext ctx);

	void exitPgm_body(MicroParser.Pgm_bodyContext ctx);

	void enterDecl(MicroParser.DeclContext ctx);

	void exitDecl(MicroParser.DeclContext ctx);

	void enterString_decl(MicroParser.String_declContext ctx);

	void exitString_decl(MicroParser.String_declContext ctx);

	void enterStr(MicroParser.StrContext ctx);

	void exitStr(MicroParser.StrContext ctx);

	void enterVar_decl(MicroParser.Var_declContext ctx);

	void exitVar_decl(MicroParser.Var_declContext ctx);
	
	void enterVar_type(MicroParser.Var_typeContext ctx);

	void exitVar_type(MicroParser.Var_typeContext ctx);
	
	void enterAny_type(MicroParser.Any_typeContext ctx);

	void exitAny_type(MicroParser.Any_typeContext ctx);

	void enterId_list(MicroParser.Id_listContext ctx);
	void exitId_list(MicroParser.Id_listContext ctx);

	void enterId_tail(MicroParser.Id_tailContext ctx);
	void exitId_tail(MicroParser.Id_tailContext ctx);

	void enterParam_decl_list(MicroParser.Param_decl_listContext ctx);

	void exitParam_decl_list(MicroParser.Param_decl_listContext ctx);

	void enterParam_decl(MicroParser.Param_declContext ctx);

	void exitParam_decl(MicroParser.Param_declContext ctx);

	void enterParam_decl_tail(MicroParser.Param_decl_tailContext ctx);

	 
	void exitParam_decl_tail(MicroParser.Param_decl_tailContext ctx);

	void enterFunc_declarations(MicroParser.Func_declarationsContext ctx);

	void exitFunc_declarations(MicroParser.Func_declarationsContext ctx);

	void enterFunc_decl(MicroParser.Func_declContext ctx);
	
	void exitFunc_decl(MicroParser.Func_declContext ctx);

	void enterFunc_body(MicroParser.Func_bodyContext ctx);
	
	void exitFunc_body(MicroParser.Func_bodyContext ctx);

	void enterStmt_list(MicroParser.Stmt_listContext ctx);
	
	void exitStmt_list(MicroParser.Stmt_listContext ctx);
	void enterStmt(MicroParser.StmtContext ctx);
	void exitStmt(MicroParser.StmtContext ctx);

	void enterBase_stmt(MicroParser.Base_stmtContext ctx);
	
	void exitBase_stmt(MicroParser.Base_stmtContext ctx);
	
	void enterAssign_stmt(MicroParser.Assign_stmtContext ctx);
	
	void exitAssign_stmt(MicroParser.Assign_stmtContext ctx);
	
	void enterAssign_expr(MicroParser.Assign_exprContext ctx);
	
	void exitAssign_expr(MicroParser.Assign_exprContext ctx);
	
	void enterRead_stmt(MicroParser.Read_stmtContext ctx);
	
	void exitRead_stmt(MicroParser.Read_stmtContext ctx);
	
	void enterWrite_stmt(MicroParser.Write_stmtContext ctx);
	
	void exitWrite_stmt(MicroParser.Write_stmtContext ctx);
	
	void enterReturn_stmt(MicroParser.Return_stmtContext ctx);
	
	void exitReturn_stmt(MicroParser.Return_stmtContext ctx);
	void enterExpr(MicroParser.ExprContext ctx);
	void exitExpr(MicroParser.ExprContext ctx);
	
	void enterExpr_prefix(MicroParser.Expr_prefixContext ctx);
	
	void exitExpr_prefix(MicroParser.Expr_prefixContext ctx);
	void enterFactor(MicroParser.FactorContext ctx);
	void exitFactor(MicroParser.FactorContext ctx);
	
	
	void enterFactor_prefix(MicroParser.Factor_prefixContext ctx);
	

	void exitFactor_prefix(MicroParser.Factor_prefixContext ctx);
	

	void enterPostfix_expr(MicroParser.Postfix_exprContext ctx);
	
	void exitPostfix_expr(MicroParser.Postfix_exprContext ctx);
	
	void enterCall_expr(MicroParser.Call_exprContext ctx);
	
	void exitCall_expr(MicroParser.Call_exprContext ctx);
	
	void enterExpr_list(MicroParser.Expr_listContext ctx);
	
	void exitExpr_list(MicroParser.Expr_listContext ctx);
	 
	void enterExpr_list_tail(MicroParser.Expr_list_tailContext ctx);
	
	
	void exitExpr_list_tail(MicroParser.Expr_list_tailContext ctx);

	void enterPrimary(MicroParser.PrimaryContext ctx);
	void exitPrimary(MicroParser.PrimaryContext ctx);
	void enterAddop(MicroParser.AddopContext ctx);
	void exitAddop(MicroParser.AddopContext ctx);
	void enterMulop(MicroParser.MulopContext ctx);
	void exitMulop(MicroParser.MulopContext ctx);

	void enterIf_stmt(MicroParser.If_stmtContext ctx);
	void exitIf_stmt(MicroParser.If_stmtContext ctx);
	void enterElse_part(MicroParser.Else_partContext ctx);
	
	void exitElse_part(MicroParser.Else_partContext ctx);

	void enterCond(MicroParser.CondContext ctx);
	void exitCond(MicroParser.CondContext ctx);
	void enterCompop(MicroParser.CompopContext ctx);
	void exitCompop(MicroParser.CompopContext ctx);
	
	
	void enterDo_while_stmt(MicroParser.Do_while_stmtContext ctx);
	

	void exitDo_while_stmt(MicroParser.Do_while_stmtContext ctx);
}