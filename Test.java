import java.util.List;

class Test {
  public static void main(String[] args) {
    Stmt prog;
    Frame global = new Frame();

    prog = Stmt.Block(List.of(
      Stmt.Decl("Int", "x", Expr.Val(5, "Int")),
      Stmt.Decl("Int", "r", Expr.Val(1, "Int")),
      Stmt.While(Expr.Bin(Expr.Var("x"), Expr.Val(0, "Int"), ">"), Stmt.Block(List.of(
        Stmt.Assg("r", Expr.Bin(Expr.Var("r"), Expr.Var("x"), "*")),
        Stmt.Assg("x", Expr.Bin(Expr.Var("x"), Expr.Val(1, "Int"), "-"))
      )))
    ));
    
    prog.exec(global);

    System.out.println(global);
  }
}