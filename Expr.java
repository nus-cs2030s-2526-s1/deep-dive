abstract class Expr {
  public static Val Val(Object val, String rtt) { return new Val(val, rtt); }
  public static Var Var(String name) { return new Var(name); }
  public static Bin Bin(Expr lhs, Expr rhs, String op) { return new Bin(lhs, rhs, op); }
  public static Un Un(Expr arg, String op) { return new Un(arg, op); }

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