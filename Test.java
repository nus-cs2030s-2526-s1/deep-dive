import java.util.List;

class Test {
  public static void main(String[] args) {
    Stmt prog;
    Frame global = new Frame();

    /**
     * fn fact(int n) {
     *   if (n == 0) {
     *     return 1;
     *   } else {
     *     return n * fact(n - 1);
     *     return 0;
     *   }
     * }
     */

    prog = Stmt.Block(List.of(
      Stmt.Func("Int", "fact",
          new Var[] { Expr.Var("n") },
          new String[] { "Int" },
          Stmt.Block(List.of(
            Stmt.If(Expr.Bin(Expr.Var("n"), Expr.Val(0, "Int"), "=="),
                Stmt.Ret(Expr.Val(1, "Int")),
                Stmt.Block(List.of(
                  Stmt.Ret(Expr.Bin(Expr.Var("n"), Expr.Call("fact", Expr.Bin(Expr.Var("n"), Expr.Val(1, "Int"), "-")), "*")),
                  Stmt.Ret(Expr.Val(0, "Int"))
                ))
              )
          ))),
      Stmt.Decl("Int", "x", Expr.Call("fact", Expr.Val(10, "Int")))
    ));
    
    prog.exec(global);

    System.out.println(global);
  }
}