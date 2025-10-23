import java.util.List;

abstract class Stmt {
  public static Decl Decl(String type, String name, Expr expr) { return new Decl(type, name, expr); }
  public static Assg Assg(String name, Expr expr) { return new Assg(name, expr); }
  public static If If(Expr cond, Stmt _then, Stmt _else) { return new If(cond, _then, _else); }
  public static While While(Expr cond, Stmt stmt) { return new While(cond, stmt); }
  public static Block Block(List<Stmt> stmts) { return new Block(stmts); }
  public static Func Func(String ret, String name, Var[] vars, String[] types, Stmt body) { return new Func(ret, name, vars, types, body); }
  public static Ret Ret(Expr expr) { return new Ret(expr); }

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
    if (frame.tag() == Frame.RETURN) {
      return frame;
    }

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
    if (frame.tag() == Frame.RETURN) {
      return frame;
    }
    
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
    if (frame.tag() == Frame.RETURN) {
      return frame;
    }
    
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
    if (frame.tag() == Frame.RETURN) {
      return frame;
    }
    
    while (((Boolean)this.cond.eval(frame).val()) == true) {
      frame = this.stmt.exec(frame);

      if (frame.tag() == Frame.RETURN) {
        return frame;
      }  
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
    if (frame.tag() == Frame.RETURN) {
      return frame;
    }
    
    for (Stmt stmt : stmts) {
      frame = stmt.exec(frame);

      if (frame.tag() == Frame.RETURN) {
        return frame;
      }  
    }
    return frame;
  }
}

// Func <: Stmt
class Func extends Stmt {
  private String ret;
  private String name;
  private Var[] vars;
  private String[] types;
  private Stmt body;
  private Frame scope;

  public Func(String ret, String name,
      Var[] vars, String[] types, Stmt body) {
    this.ret = ret;
    this.name = name;
    this.vars = vars;
    this.types = types;
    this.body = body;
    this.scope = null;
  }

  @Override
  public Frame exec(Frame frame) {
    this.scope = frame;
    frame.define(this.name, this);
    return frame;
  }
  
  public Var[] vars() {
    return this.vars;
  }

  public String[] types() {
    return this.types;
  }

  public Stmt body() {
    return this.body;
  }

  public Frame scope() {
    return this.scope;
  }
}

// Ret <: Stmt
class Ret extends Stmt {
  private Expr expr;

  public Ret(Expr expr) {
    this.expr = expr;
  }

  @Override
  public Frame exec(Frame frame) {
    if (frame.tag() == Frame.RETURN) {
      return frame;
    }
    
    Val res = this.expr.eval(frame);
    frame.ret(res);
    return frame;
  }
}