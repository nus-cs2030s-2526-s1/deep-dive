import java.util.Hashtable;

class Frame {
  private Hashtable<String, Entry> map;

  public Frame() {
    this.map = new Hashtable<>();
  }

  public void declare(String name, String ctt, Val val) {
    // TODO: check if it is already declared
    this.map.put(name, new Entry(ctt, val));
  }

  public void assign(String name, Val val) {
    String ctt = this.ctt(name);
    String rtt = val.rtt();
    
    // TODO: check rtt <: ctt

    this.map.put(name, new Entry(ctt, val));
  }

  public Val val(String name) {
    return this.map.get(name).val();
  }

  public String rtt(String name) {
    return this.map.get(name).rtt();
  }

  public String ctt(String name) {
    return this.map.get(name).ctt();
  }

  @Override
  public String toString() {
    return this.map.toString();
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