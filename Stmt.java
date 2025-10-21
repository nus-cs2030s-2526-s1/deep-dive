import java.util.List;

abstract class Stmt {
  public static Decl Decl(String type, String name, Expr expr) { return new Decl(type, name, expr); }
  public static Assg Assg(String name, Expr expr) { return new Assg(name, expr); }
  public static If If(Expr cond, Stmt _then, Stmt _else) { return new If(cond, _then, _else); }
  public static While While(Expr cond, Stmt stmt) { return new While(cond, stmt); }
  public static Block Block(List<Stmt> stmts) { return new Block(stmts); }

  public abstract Frame exec(Frame frame);
}

// Decl <: Stmt
class Decl extends Stmt {
  private String type;
  private String name;
  private Expr expr;

  public Decl(String type, String name, Expr expr) {
    this.type = type;
    this.name = name;
    this.expr = expr;
  }

  @Override
  public Frame exec(Frame frame) {
    Val val = this.expr.eval(frame);

    // TODO:
    // if (!TypeCheck.isSubtype(val.rtt(), this.type)) {
    //   throw new RuntimeException("Mismatch Type");
    // }

    frame.declare(this.name, this.type, val);
    return frame;
  }
}

// Assg <: Stmt
class Assg extends Stmt {
  private String name;
  private Expr expr;

  public Assg(String name, Expr expr) {
    this.name = name;
    this.expr = expr;
  }

  @Override
  public Frame exec(Frame frame) {
    Val val = this.expr.eval(frame);
    frame.assign(this.name, val);
    return frame;
  }
}

// If <: Stmt
class If extends Stmt {
  private Expr cond;
  private Stmt _then;
  private Stmt _else;

  public If(Expr cond, Stmt _then, Stmt _else) {
    this.cond = cond;
    this._then = _then;
    this._else = _else;
  }

  @Override
  public Frame exec(Frame frame) {
    if (((Boolean)this.cond.eval(frame).val()) == true) {
      this._then.exec(frame);
      return frame;
    }

    if (this._else != null) {
      this._else.exec(frame);
      return frame;
    }

    return frame;
  }
}

// While <: Stmt
class While extends Stmt {
  private Expr cond;
  private Stmt stmt;

  public While(Expr cond, Stmt stmt) {
    this.cond = cond;
    this.stmt = stmt;
  }

  @Override
  public Frame exec(Frame frame) {
    while (((Boolean)this.cond.eval(frame).val()) == true) {
      this.stmt.exec(frame);
    }
    return frame;
  }
}

// Block <: Stmt
class Block extends Stmt {
  private List<Stmt> stmts;

  public Block(List<Stmt> stmts) {
    this.stmts = stmts;
  }

  @Override
  public Frame exec(Frame frame) {
    for (Stmt stmt : stmts) {
      stmt.exec(frame);
    }
    return frame;
  }
}