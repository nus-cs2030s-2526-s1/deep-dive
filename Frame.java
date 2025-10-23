import java.util.Hashtable;

class Frame {
  private Hashtable<String, Entry> var;
  private Hashtable<String, Func> fun;

  private int tag;
  private Val val;

  private Frame out;

  public static final int NORMAL = 0;
  public static final int RETURN = 1;

  public Frame() {
    this.var = new Hashtable<>();
    this.fun = new Hashtable<>();
    this.tag = Frame.NORMAL;
    this.val = null;
    this.out = null;
  }

  public Frame(Frame out) {
    this.var = new Hashtable<>();
    this.fun = new Hashtable<>();
    this.tag = Frame.NORMAL;
    this.val = null;
    this.out = out;
  }

  public void declare(String name, String ctt, Val val) {
    // TODO: check if it is already declared
    this.var.put(name, new Entry(ctt, val));
  }

  public void define(String name, Func func) {
    // TODO: check if it is already defined
    this.fun.put(name, func);
  }

  public void assign(String name, Val val) {
    if (this.var.containsKey(name)) {
      String ctt = this.ctt(name);
      String rtt = val.rtt();
      
      // TODO: check rtt <: ctt

      this.var.put(name, new Entry(ctt, val));
    } else if (this.out == null) {
      throw new RuntimeException("Variable " + name + " does not exist");
    } else {
      this.out.assign(name, val);
    }
  }

  public Func invoke(String name) {
    if (this.fun.containsKey(name)) {
      return this.fun.get(name);
    } else if (this.out == null) {
      throw new RuntimeException("Function " + name + " does not exist");
    } else {
      return this.out.invoke(name);
    }
  }

  public Val val() {
    return this.val;
  }

  public Val val(String name) {
    if (this.var.containsKey(name)) {
      return this.var.get(name).val();
    } else if (this.out == null) {
      throw new RuntimeException("Variable " + name + " does not exist");
    } else {
      return this.out.val(name);
    }
  }

  public String rtt(String name) {
    if (this.var.containsKey(name)) {
      return this.var.get(name).rtt();
    } else if (this.out == null) {
      throw new RuntimeException("Variable " + name + " does not exist");
    } else {
      return this.out.rtt(name);
    }
  }

  public String ctt(String name) {
    if (this.var.containsKey(name)) {
      return this.var.get(name).ctt();
    } else if (this.out == null) {
      throw new RuntimeException("Variable " + name + " does not exist");
    } else {
      return this.out.ctt(name);
    }
  }

  public int tag() {
    return this.tag;
  }

  public void ret(Val val) {
    this.tag = Frame.RETURN;
    this.val = val;
  }

  @Override
  public String toString() {
    return this.var.toString();
  }
}

class Entry {
  private String ctt;
  private Val val;

  public Entry(String ctt, Val val) {
    this.ctt = ctt;
    this.val = val;
  }

  public Val val() {
    return this.val;
  }

  public String rtt() {
    return this.val.rtt();
  }

  public String ctt() {
    return this.ctt;
  }

  @Override
  public String toString() {
    return this.val().toString();
  }
}