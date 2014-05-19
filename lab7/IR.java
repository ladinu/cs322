// This is supporting software for CS322 Compilers and Language Design II
// Copyright (c) Portland State University
// 
// Three-address IR definitions. 
// (Some nodes are not strictly 3-address.)
//
// Based on Version 5. (Last modified on JL on 2/15/14.)   
// 
//
import java.util.*;

class IR {

  public static final BoolLit TRUE = new BoolLit(true);
  public static final BoolLit FALSE = new BoolLit(false);

  public static boolean indexed = false;
  public static int linenum = 0;
  static String line(boolean count, String s) {
    String idx = (indexed && count) ? (linenum++) + ". " 
                   + (linenum<11 ? " " : "") : "";
    return idx + s;
  }

  // Types

  public static enum Type {
    BOOL(":B",1), INT(":I",4), PTR(":P",8);
    final String name;
    final int size;
    Type(String s, int i) { name=s; size=i; }
    public String toString() { return name; }
  }

  public static Type type(Const item) {
    if (item instanceof BoolLit) 
      return Type.BOOL;
    if (item instanceof IntLit) 
      return Type.INT;
    return Type.PTR;
  }


  // Program
  
  public static class Program {
    public final Data[] data;
    public final Func[] funcs;

    public Program(Data[] d, Func[] f) { data=d; funcs=f; }
    public Program(List<Data> dl, List<Func> fl) { 
      this(dl.toArray(new Data[0]), fl.toArray(new Func[0]));
    }
    public String toIndexedString() { 
      indexed = true;
      return toString();
    }
    public String toString() { 
      String str = "# IR Program\n";
      if (data != null && data.length > 0)
	str += "\n";
      for (Data d: data)
	str += d;
      for (Func f: funcs)
	str += "\n" + f;
      return str;
    }

  }

  // Global data records

  public static class Data {
    public final Global name;
    public final int size;
    public final Const[] items;
    
    public Data(Global n, int i, Const[] l) {
      name = n; size = i; items = l;
    }
    public Data(Global n, int i, List<Const> ll) {
      this(n, i, ll.toArray(new Const[0]));
    }
    public String toString() { 
      String str = "data " + name + " (sz=" + size + "): ";
      if (items.length > 0) {
	str += items[0].toString();
	for (int i=1; i<items.length; i++)
	  str += ", " + items[i];
      }
      return str + "\n";
    }
  }

  // Functions
  public static class Func {
    public final String name;
    public final String[] params;
    public final String[] locals;
    public final Inst[] code;

    public Func(String n, String[] p, String[] l, Inst[] c) {
      name=n; params=p; locals=l; code=c; 
    }
    public Func(String n, List<String> pl, List<String> ll, List<Inst> cl) {
      this(n, pl.toArray(new String[0]), ll.toArray(new String[0]),
	   cl.toArray(new Inst[0])); 
    }
    public String name_params_string() {
      return 
	line(false, "_" + name + " " + StringArrayToString(params)
	     + "\n");
    }
    public String locals_string() {
      return
	(locals.length==0? "" : 
	 line(false, StringArrayToString(locals) + "\n"));
    }
    public String toString() { 
      String body = "";
      linenum = 0;
      for (Inst s: code)
	body += s.toString();
      return name_params_string() + locals_string() + line(false,"{\n") + body + line(false,"}\n");
    }

    // Return set of successors for each instruction in a function
    List<Set<Integer>> successors() {
      Map<String,Integer> labelMap = new HashMap<String,Integer>(); 
      for (int i = 0; i < code.length; i++) {
	Inst c = code[i];
	if (c instanceof LabelDec)
	  labelMap.put(((LabelDec) c).name, i);
      }
      List<Set<Integer>> allSuccs = new ArrayList<Set<Integer>>(code.length); 
      for (int i = 0; i < code.length-1; i++) { // there's always a label at the end
	Inst inst = code[i];
	Set<Integer> succs = new HashSet<Integer>();
	if (inst instanceof CJump) {
	  succs.add(labelMap.get(((CJump) inst).lab.name));
	  succs.add(i+1);      // safe because there's always a label at the end
	} else if (inst instanceof Jump) 
	  succs.add(labelMap.get(((Jump) inst).lab.name));
	else if (!(inst instanceof Return))
	  succs.add(i+1);      
	allSuccs.add(i,succs);
      }
      allSuccs.add(code.length-1, new HashSet<Integer>()); // label at the end has no successors
      return allSuccs;
    }

    /** Return sets of operands used by each Inst **/
    List<Set<Reg>> used() {
      List<Set<Reg>> used = new ArrayList<Set<Reg>>(code.length);
      for (int i = 0; i < code.length; i++)  
	used.add(i,code[i].used());
      return used;
    }

    /** Return sets of operands defined by each Inst **/
    List<Set<Reg>> defined() {
      List<Set<Reg>> defined = new ArrayList<Set<Reg>>(code.length);
      for (int i = 0; i < code.length; i++)
	defined.add(i,code[i].defined());
      // Parameters are implicitly defined at top of function
      Set<Reg> top = defined.get(0); 
      for (String var : params)
	top.add(new Id(var));
      defined.set(0,top);
      return defined;
    }
  }


  public static String StringArrayToString(String[] vars) {
    String s = "(";
    if (vars.length > 0) {
      s += vars[0];
      for (int i=1; i<vars.length; i++)
	s += ", " + vars[i];
    }
    return s + ")";
  }


  // Instructions

  public static abstract class Inst {
    abstract Set<Reg> used();
    abstract Set<Reg> defined();
  }

  public static class Binop extends Inst {
    public final BOP op;
    public final Dest dst;
    public final Src src1, src2;

    public Binop(BOP o, Dest d, Src s1, Src s2) { 
      op=o; dst=d; src1=s1; src2=s2; 
    }
    public String toString() { 
      return line(true, " " + dst + " = " + src1 + " " + op + " " + src2 + "\n");
    }

    Set<Reg> used() {
      Set<Reg> s = new HashSet<Reg>();
      src1.addTo(s);
      src2.addTo(s);
      return s;
    }
    Set<Reg> defined() {
      Set<Reg> s = new HashSet<Reg>();
      dst.addTo(s);
      return s;
    }
  }

  public static class Unop extends Inst {
    public final UOP op;
    public final Dest dst;
    public final Src src;

    public Unop(UOP o, Dest d, Src s) { op=o; dst=d; src=s; }
    public String toString() { 
      return line(true, " " + dst + " = " + op + src + "\n");
    }

    Set<Reg> used() {
      Set<Reg> s = new HashSet<Reg>();
      src.addTo(s);
      return s;
    }
    Set<Reg> defined() {
      Set<Reg> s = new HashSet<Reg>();
      dst.addTo(s);
      return s;
    }
  }

  public static class Move extends Inst {
    public final Dest dst;
    public final Src src;

    public Move(Dest d, Src s) { dst=d; src=s; }
    public String toString() { 
      return line(true, " " + dst + " = " + src + "\n"); 
    }

    Set<Reg> used() {
      Set<Reg> s = new HashSet<Reg>();
      src.addTo(s);
      return s;
    }
    Set<Reg> defined() {
      Set<Reg> s = new HashSet<Reg>();
      dst.addTo(s);
      return s;
    }
  }

  public static class Load extends Inst {
    public final Type type;
    public final Dest dst;
    public final Addr addr;

    public Load (Type t, Dest d, Addr a) { type=t; dst=d; addr=a; }
    public String toString() { 
      return line(true, " " + dst + " = " + addr + type + "\n"); 
    }

    Set<Reg> used() {
      Set<Reg> s = new HashSet<Reg>();
      addr.base.addTo(s);
      return s;
    }
    Set<Reg> defined() {
      Set<Reg> s = new HashSet<Reg>();
      dst.addTo(s);
      return s;
    }
  }
    
  public static class Store extends Inst {
    public final Type type;
    public final Addr addr;
    public final Src src;

    public Store(Type t, Addr a, Src s) { type=t; addr=a; src=s; }
    public String toString() { 
      return line(true, " " + addr + type + " = " + src + "\n"); 
    }

    Set<Reg> used() {
      Set<Reg> s = new HashSet<Reg>();
      src.addTo(s);
      addr.base.addTo(s);
      return s;
    }
    Set<Reg> defined() {
      return new HashSet<Reg>();
    }
  }

  public static class Call extends Inst {
    public final CallTgt tgt;
    public final boolean ind;	// true if indirect
    public final Src[] args;
    public final Dest rdst;    // could be null

    public Call(CallTgt f, boolean b, Src[] a, Dest r) { 
      tgt=f; ind=b; args=a; rdst=r;
    }
    public Call(CallTgt f, boolean b, List<Src> al, Dest r) { 
      this(f, b, al.toArray(new Src[0]), r);
    }
    public Call(CallTgt f, boolean b, List<Src> al) { 
      this(f, b, al.toArray(new Src[0]), null);
    }
    public String toString() { 
      String arglist = "(";
      if (args.length > 0) {
	arglist += args[0];
	for (int i=1; i<args.length; i++)
	  arglist += ", " + args[i];
      }
      arglist +=  ")";
      String retstr = (rdst==null) ? " " : " " + rdst + " = ";
      return line(true, retstr +  "call " + (ind ? "* " : "") +
		  tgt + arglist + "\n");
    }

    Set<Reg> used() {
      Set<Reg> s = new HashSet<Reg>();
      tgt.addTo(s);
      for (Src a : args)
	a.addTo(s);
      return s;
    }
    Set<Reg> defined() {
      Set<Reg> s = new HashSet<Reg>();
      if (rdst != null)
	rdst.addTo(s);
      return s;
    }
  }

  public static class Return extends Inst {
    public final Src val;	// could be null

    public Return() { val=null; }
    public Return(Src s) { val=s; }
    public String toString() { 
      return line(true, " return " + (val==null ? "" : val) + "\n"); 
    }

    Set<Reg> used() {
      Set<Reg> s = new HashSet<Reg>();
      if (val != null)
	val.addTo(s);
      return s;
    }
    Set<Reg> defined() {
      return new HashSet<Reg>();
    }
  }

  public static class CJump extends Inst {
    public final RelOP op;
    public final Src src1, src2;
    public final Label lab;

    public CJump(RelOP o, Src s1, Src s2, Label l) { 
      op=o; src1=s1; src2=s2; lab=l; 
    }
    public String toString() { 
      return line(true, " if " + src1 + " " + op + " " + src2 + 
	" goto " + lab + "\n");
    }

    Set<Reg> used() {
      Set<Reg> s = new HashSet<Reg>();
      src1.addTo(s);
      src2.addTo(s);
      return s;
    }
    Set<Reg> defined() {
      return new HashSet<Reg>();
    }
  }

  public static class Jump extends Inst {
    public final Label lab;

    public Jump(Label l) { lab=l; }
    public String toString() { 
      return line(true, " goto " + lab + "\n"); 
    }
    
    Set<Reg> used() {
      return new HashSet<Reg>();
    }
    Set<Reg> defined() {
      return new HashSet<Reg>();
    }
  }

  public static class LabelDec extends Inst { 
    public final String name;

    public LabelDec(String s) { name=s; }
    public String toString() { 
      return line(true, name + ":\n"); 
    }

    Set<Reg> used() {
      return new HashSet<Reg>();
    }
    Set<Reg> defined() {
      return new HashSet<Reg>();
    }
  }

  // Operators

  public static interface BOP {
    public abstract String X86_name();
  }

  public static enum ArithOP implements BOP {
    ADD("+","add"), SUB("-","sub"), MUL("*","imul"), DIV("/","idiv"), AND("&&","and"), OR("||","or");
    final String name;
    final String xname;

    ArithOP(String n,String xn) { name = n; xname = xn;}
    public String toString() { return name; }
    public String X86_name() { return xname; }
  }

  public static enum RelOP implements BOP {
    EQ("==","e"), NE("!=","ne"), LT("<","l"), LE("<=","le"), GT(">","g"), GE(">=","ge");
    final String name;
    final String xname; 

    RelOP(String n,String xn) { name = n; xname = xn;}
    public String toString() { return name; }
    public String X86_name() { return xname; }
  }

  public static enum UOP {
    NEG("-","neg"), NOT("!","not");
    final String name;
    final String xname;

    UOP(String n, String xn) { name = n; xname = xn;}
    public String toString() { return name; }
    public String X86_name() { return xname; }
  }

  public static boolean isCompareOp(BOP op) {
    return (op == RelOP.EQ) || (op == RelOP.NE) ||
           (op == RelOP.LT) || (op == RelOP.LE) ||
           (op == RelOP.GT) || (op == RelOP.GE);
  }

  // Label

  public static class Label {
    static int labelnum=0;
    public String name;

    public Label() { name = "L" + labelnum++; }
    public Label(String s) { name = s; }
    public void set(String s) { name = s; }
    public String toString() { return name; }
  }

  // Address

  public static class Addr {   // Memory at base + offset
    public final Src base;  
    public final int offset;

    public Addr(Src b) { base=b; offset=0; }
    public Addr(Src b, int o) { base=b; offset=o; }
    public String toString() {
      return "" + ((offset == 0) ? "" : offset) + "[" + base + "]";
    }
  }

  // Operands

  public interface Operand {
    /** Add to register set if actually a register. **/
    void addTo(Set<Reg> s);
  }

  public interface Reg {}

  public interface Src extends Operand {}

  public interface Dest extends Operand, Reg {}

  public interface CallTgt extends Operand {}

  public interface Const extends Operand {}


  public static class Id implements Reg, Src, Dest, CallTgt  {
    public final String name;

    public Id(String s) { name=s; }
    public String toString() { return name; }
    public boolean equals(Object l) {
      return (l instanceof Id && (((Id) l).name.equals(name)));
    }
    public int hashCode() {  
      return name.hashCode(); 
    }
    public void addTo(Set<Reg> s) {
      s.add(this);
    }
  }


  public static class Temp implements Reg, Src, Dest, CallTgt  {
    private static int cnt=0;
    public final int num;

    public Temp(int n) { num=n; }
    public Temp() { num = ++Temp.cnt; }
    public static void reset() { Temp.cnt = 0; }
    public static int getcnt() { return Temp.cnt; }
    public String toString() { return "t" + num; }
    public boolean equals(Object l) {
      return (l instanceof Temp && (((Temp) l).num == num));
    }
    public int hashCode() {  
      return num; 
    }
    public void addTo(Set<Reg> s) {
      s.add(this);
    }
  }

  public static class Global implements Src, CallTgt, Const {
    public final String name;

    public Global(String s) { name = s; }
    public String toString() { return "_" + name; }  // should be environment dependent
    public void addTo(Set<Reg> s) {
    }
  }

  public static class IntLit implements Src, Const {
    public final int i;

    public IntLit(int v) { i=v; }
    public String toString() { return i + ""; }
    public void addTo(Set<Reg> s) {
    }
  }  


  public static class BoolLit implements Src {
    public final boolean b;

    public BoolLit(boolean v) { b=v; }
    public String toString() { return b + ""; }
    public void addTo(Set<Reg> s) {
    }
  }


  public static class StrLit implements Src {
    public final String s;

    public StrLit(String v) { s=v; }
    public String toString() { return "\"" + s + "\""; }
    public void addTo(Set<Reg> s) {
    }
  }


}



