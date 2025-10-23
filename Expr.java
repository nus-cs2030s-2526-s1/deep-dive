abstract class Expr {
  public static Val Val(Object val, String rtt) { return new Val(val, rtt); }
  public static Var Var(String name) { return new Var(name); }
  public static Bin Bin(Expr lhs, Expr rhs, String op) { return new Bin(lhs, rhs, op); }
  public static Un Un(Expr arg, String op) { return new Un(arg, op); }
  public static Call Call(String name, Expr... expr) { return new Call(name, expr); }

  public abstract Val eval(Frame frame);

  // public TypeVal typeEval(TypeFrame frame) {
  //   return null;
  // }
}

// Val <: Expr
class Val extends Expr {
  private Object val;
  private String rtt;

  public Val(Object val, String rtt) {
    this.val = val;
    this.rtt = rtt;
  }

  public Object val() {
    return this.val;
  }

  public String rtt() {
    return this.rtt;
  }

  @Override
  public Val eval(Frame frame) {
    return this;
  }

  @Override
  public String toString() {
    return this.val.toString();
  }
}

// Var <: Expr
class Var extends Expr {
  private String name;

  public Var(String name) {
    this.name = name;
  }

  @Override
  public Val eval(Frame frame) {
    return frame.val(this.name);
  }

  public String name() {
    return this.name;
  }
}

// Bin <: Expr
class Bin extends Expr {
  private Expr lhs;
  private Expr rhs;
  private String op;

  public Bin(Expr lhs, Expr rhs, String op) {
    this.lhs = lhs;
    this.rhs = rhs;
    this.op = op;
  }

  @Override
  public Val eval(Frame frame) {
    Object lObj = this.lhs.eval(frame).val();
    Object rObj = this.rhs.eval(frame).val();

    switch(this.op) {
      case "+":
        return Expr.Val(((Integer)lObj) + ((Integer)rObj), "Int");
      case "-":
        return Expr.Val(((Integer)lObj) - ((Integer)rObj), "Int");
      case "*":
        return Expr.Val(((Integer)lObj) * ((Integer)rObj), "Int");
      case "/":
        return Expr.Val(((Integer)lObj) / ((Integer)rObj), "Int");
      case "%":
        return Expr.Val(((Integer)lObj) % ((Integer)rObj), "Int");

      case ">":
        return Expr.Val(((Integer)lObj) > ((Integer)rObj), "Bool");
      case ">=":
        return Expr.Val(((Integer)lObj) >= ((Integer)rObj), "Bool");
      case "<":
        return Expr.Val(((Integer)lObj) < ((Integer)rObj), "Bool");
      case "<=":
        return Expr.Val(((Integer)lObj) <= ((Integer)rObj), "Bool");
      case "==":
        return Expr.Val(((Integer)lObj) == ((Integer)rObj), "Bool");
      case "!=":
        return Expr.Val(((Integer)lObj) != ((Integer)rObj), "Bool");

      case "&&":
        return Expr.Val(((Boolean)lObj) && ((Boolean)rObj), "Bool");
      case "||":
        return Expr.Val(((Boolean)lObj) || ((Boolean)rObj), "Bool");

      default:
        throw new RuntimeException("Invalid binary operator `" + this.op + "`");
    }
  }
}

// Un <: Expr
class Un extends Expr {
  private Expr arg;
  private String op;

  public Un(Expr arg, String op) {
    this.arg = arg;
    this.op = op;
  }

  @Override
  public Val eval(Frame frame) {
    Object arg = this.arg.eval(frame).val();

    switch(this.op) {
      case "+":
        return Expr.Val(+((Integer)arg), "Int");
      case "-":
        return Expr.Val(-((Integer)arg), "Int");

      case "!":
        return Expr.Val(!((Boolean)arg), "Bool");

      default:
        throw new RuntimeException("Invalid unary operator `" + this.op + "`");
    }
  }
}

// Call <: Expr
class Call extends Expr {
  private String name;
  private Expr[] expr;

  public Call(String name, Expr... expr) {
    this.name = name;
    this.expr = expr;
  }

  @Override
  public Val eval(Frame frame) {
    Val[] args = new Val[this.expr.length];

    for (int i = 0; i < this.expr.length; i += 1) {
      args[i] = this.expr[i].eval(frame);
    }

    Func func = frame.invoke(name);
    Frame callFrame = new Frame(func.scope());
    Var[] params = func.vars();
    String[] types = func.types();

    // TODO: check type and typecheck
    //       - params.length == args.length
    //       - RTT(args[i]) <: CTT(params[i])

    for (int i = 0; i < args.length; i += 1) {
      callFrame.declare(params[i].name(), types[i],
          args[i]);
    }

    Stmt body = func.body();
    callFrame = body.exec(callFrame);
    Val res = callFrame.val();

    return res;
  }
}