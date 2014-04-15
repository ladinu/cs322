package com.dt.interpreter;
import java.util.ArrayList;

enum Errors {
   LIST_VALUE_EXPECTED("ABORT: list value expected"),
   NON_EMPTY_LIST_EXPECTED("ABORT: nonempty list expected"),
   BOOLEAN_VALUE_EXPECTED("ABORT: Boolean value expected"),
   INTEGER_VALUE_EXPECTED("ABORT: Integer value expected"),
   FIRST_CLASS_FUNCTION_EXPECTED("ABORT: First-class function expected"),
   CANNOT_FIND_FUNCTION("ABORT: Cannot find function "),
   WRONG_NUMBER_OF_ARGUMENTS("ABORT: Wrong number of arguments for ");


   private final String error;
   private Errors(String error) {
      this.error = error;
   }
   @Override
   public String toString() {
      return this.error;
   }
}

abstract class Value {
   abstract String show();

   boolean asBool() {
      System.out.println(Errors.BOOLEAN_VALUE_EXPECTED.toString());
      System.exit(1);
      return true; // Not reached
   }

   int asInt() {
      System.out.println(Errors.INTEGER_VALUE_EXPECTED.toString());
      System.exit(1);
      return 0; // Not reached
   }

   Value enter(Value val) {
      System.out.println(Errors.FIRST_CLASS_FUNCTION_EXPECTED.toString());
      System.exit(1);
      return null; // Not reached
   }
}

class BValue extends Value {
   private boolean b;

   BValue(boolean b) { this.b = b; }
   String show() { return Boolean.toString(b); }
   boolean asBool() { return b; }
}

class IValue extends Value {
   private int i;

   IValue(int i) { this.i = i; }
   String show() { return Integer.toString(i); }
   int asInt() { return i; }
}

class FValue extends Value {
   private Env env;
   private String arg;
   private Expr body;

   FValue(Env env, String arg, Expr body) {
      this.env = env; this.arg = arg; this.body = body;
   }

   Value enter(Value val) {
      return body.eval(new ValEnv(arg, val, env));
   }

   String show() {
      return "<function>";
   }
}

abstract class LValue extends Value {
}

class EmptyList extends LValue {
   String show() { return "[]"; }
}

class NonEmptyList extends LValue {
   private Value  head;
   private LValue tail;

   NonEmptyList(Value head, LValue tail) {
     this.head = head; this.tail=tail;
   }
   
   String show() {
      ArrayList<Value> elements = getArrayList();
      String str = "[";
      int index = 0;
      for (Value val: elements ) {
         if (index == elements.size() - 1) {
            str += val.show() + "]";
         } else {
            str += val.show() + ", ";
         }
         index += 1;
      }
      return str;
   }

   Value getHead() {
      return head;
   }

   Value getTail() {
      return tail;
   }

   ArrayList<Value> getArrayList() {
      ArrayList<Value> elements = new ArrayList<Value>();
      elements.add(head);
      LValue next = tail;
      while ( next instanceof NonEmptyList ) {
         elements.add(((NonEmptyList)next).head);
         next = ((NonEmptyList)next).tail;
      }
      return elements;
   }
}

abstract class Expr {
  abstract Value eval(Env env);
  Env evalRef(Env env) { return new ValEnv("", eval(env), null); }
  abstract String show();
}

class Var extends Expr {
  private String name;
  Var(String name) { this.name = name; }

  Value eval(Env env) { return Env.lookup(env, name).getValue(); }
  Env evalRef(Env env) { return Env.lookup(env, name); }
  String show() { return name; }
}

class Int extends Expr {
  private int num;
  Int(int num) { this.num = num; }

  Value eval(Env env) { return new IValue(num); }
  String show() { return Integer.toString(num); }
}

class Mult extends Expr {
  private Expr l, r;
  Mult(Expr l, Expr r) { this.l = l; this.r = r; }

  Value eval(Env env) { return new IValue(l.eval(env).asInt() * r.eval(env).asInt()); }
  String show() { return "(" + l.show() + " * " + r.show() + ")"; }
}

class Plus extends Expr {
  private Expr l, r;
  Plus(Expr l, Expr r) { this.l = l; this.r = r; }

  Value eval(Env env) { return new IValue(l.eval(env).asInt() + r.eval(env).asInt()); }
  String show() { return "(" + l.show() + " + " + r.show() + ")"; }
}

class Minus extends Expr {
  private Expr l, r;
  Minus(Expr l, Expr r) { this.l = l; this.r = r; }

  Value eval(Env env) { return new IValue(l.eval(env).asInt()- r.eval(env).asInt()); }
  String show() { return "(" + l.show() + " - " + r.show() + ")"; }
}

class LT extends Expr {
  private Expr l, r;
  LT(Expr l, Expr r) { this.l = l; this.r = r; }

  Value eval(Env env) { return new BValue(l.eval(env).asInt() < r.eval(env).asInt()); }
  String show()  { return "(" + l.show() + " < " + r.show() + ")"; }
}

class EqEq extends Expr {
  private Expr l, r;
  EqEq(Expr l, Expr r) { this.l = l; this.r = r; }

  Value eval(Env env) { return new BValue(l.eval(env).asInt() == r.eval(env).asInt()); }
  String show()  { return "(" + l.show() + " == " + r.show() + ")"; }
}


class Lambda extends Expr {
   private String var;
   private Expr body;

   Lambda(String var, Expr body) { this.body = body; this.var = var; }

   Value eval(Env env) {
      return new FValue(env, var, body);
   }

   String show() { return "(\\" + var + " -> " + body.show() + ")"; }
}

class Apply extends Expr {
   private Expr fun, arg;

   Apply(Expr fun, Expr arg) { this.fun = fun; this.arg = arg; }

   Value eval(Env env) {
      return fun.eval(env).enter(arg.eval(env));
   }

   String show() { return "(" + fun.show() + " @ " + arg.show() + ")";}
}

class Nil extends Expr {
   Value eval(Env env) { return new EmptyList(); }
   String show() { return "[]"; }
}

class Cons extends Expr {
   private Expr head;
   private Expr tail;

   Cons(Expr head, Expr tail) {
      this.head = head; this.tail = tail;
   }

   Value eval(Env env) {
      checkIfLValue(env, tail);
      LValue tailEnd = (LValue)tail.eval(env);
      return new NonEmptyList(head.eval(env), tailEnd);
   }

   String show() {
      return "cons(" + head.show() + ", " + tail.show() +")";
   }

   void checkIfLValue(Env env, Expr expr) {
      if ( !(expr.eval(env) instanceof LValue) ) {
         System.out.println(Errors.LIST_VALUE_EXPECTED.toString());
         System.exit(1);
      }
   }
}

class NonEmpty extends Expr {
   private Expr e;
   NonEmpty(Expr e) { this.e = e;}

   Value eval(Env env) {
      if (!isLValue(env, e)) {
         System.out.println(Errors.LIST_VALUE_EXPECTED.toString());
         System.exit(1);
      }

      if (e.eval(env) instanceof NonEmptyList ) {
         return new BValue(true);
      } else {
         return new BValue(false);
      }
   }

   String show() {
      return "nonEmpty(" + e.show() + ")";
   }

   static boolean isLValue(Env env, Expr e) {
      return e.eval(env) instanceof LValue;
   }
}

class Head extends Expr {
   private Expr e;
   Head(Expr e) {this.e = e;}

   Value eval(Env env) {
      if (e.eval(env) instanceof NonEmptyList) {
         NonEmptyList list = (NonEmptyList)e.eval(env);
         return list.getHead();
      } else if (e.eval(env) instanceof EmptyList) {
         System.out.println(Errors.NON_EMPTY_LIST_EXPECTED.toString());
         System.exit(1);
         return new BValue(false); // Not reached
      } else {
         System.out.println(Errors.LIST_VALUE_EXPECTED.toString());
         System.exit(1);
         return new BValue(false); // Not reached
      }
   }

   String show() {
      return "head(" + e.show() + ")";
   }
}

class Tail extends Expr {
   private Expr e;
   Tail(Expr e) { this.e = e;}

   Value eval(Env env) {
      if (e.eval(env) instanceof NonEmptyList) {
         NonEmptyList list = (NonEmptyList)e.eval(env);
         return list.getTail();
      } else if (e.eval(env) instanceof EmptyList) {
         System.out.println(Errors.NON_EMPTY_LIST_EXPECTED.toString());
         System.exit(1);
         return new BValue(false); // Not reached
      } else {
         System.out.println(Errors.LIST_VALUE_EXPECTED.toString());
         System.exit(1);
         return new BValue(false); // Not reached
      }
   }

   String show() {
      return "tail(" + e.show() + ")";
   }
}

abstract class Stmt {
  abstract Env exec(Program prog, Env env);
  abstract void print(int ind);

  static void indent(int ind) {
    for (int i=0; i<ind; i++) {
      System.out.print(" ");
    }
  }
}

class Seq extends Stmt {
  private Stmt l, r;
  Seq(Stmt l, Stmt r) { this.l = l; this.r = r; }

  Env exec(Program prog, Env env) {
    return r.exec(prog, l.exec(prog, env));
  }

  void print(int ind) {
    l.print(ind);
    r.print(ind);
  }
}

class VarDecl extends Stmt {
   private String var;
   private Expr expr;

   VarDecl(String var, Expr expr) {
      this.var = var; this.expr = expr;
   }

   Env exec(Program prog, Env env) {
      return new ValEnv(var, expr.eval(env), env);
   }

   void print(int ind) {
      indent(ind);
      System.out.println("var " + var + " = " + expr.show() + ";");
   }
}

class Assign extends Stmt {
  private String lhs;
  private Expr  rhs;
  Assign(String lhs, Expr rhs) {
    this.lhs = lhs; this.rhs = rhs;
  }

  Env exec(Program prog, Env env) {
     Env.lookup(env, lhs).setValue(rhs.eval(env));
     return env;
  }

  void print(int ind) {
    indent(ind);
    System.out.println(lhs + " = " + rhs.show() + ";");
  }
}

class While extends Stmt {
  private Expr test;
  private Stmt  body;
  While(Expr test, Stmt body) {
    this.test = test; this.body = body;
  }

  Env exec(Program prog, Env env) {
    while (test.eval(env).asBool()) {
      body.exec(prog, env);
    }
    return env;
  }

  void print(int ind) {
    indent(ind);
    System.out.println("while (" + test.show() + ") {");
    body.print(ind+2);
    indent(ind);
    System.out.println("}");
  }
}

class If extends Stmt {
  private Expr test;
  private Stmt  t, f;
  If(Expr test, Stmt t, Stmt f) {
    this.test = test; this.t = t; this.f = f;
  }

  Env exec(Program prog, Env env) {
    if (test.eval(env).asBool()) {
      t.exec(prog, env);
    } else {
      f.exec(prog, env);
    }
    return env;
  }

  void print(int ind) {
    indent(ind);
    System.out.println("if (" + test.show() + ") {");
    t.print(ind+2);
    indent(ind);
    System.out.println("} else {");
    f.print(ind+2);
    indent(ind);
    System.out.println("}");
  }
}

class Print extends Stmt {
  private Expr exp;
  Print(Expr exp) { this.exp = exp; }

  Env exec(Program prog, Env env) {
    System.out.println("Output: " + exp.eval(env).show());
    return env;
  }

  void print(int ind) {
    indent(ind);
    System.out.println("print " + exp.show() + ";");
  }
}

class Call extends Stmt {
   private String name;
   private Expr[] actuals;

   Call(String name, Expr[] actuals) {
      this.name = name; this.actuals = actuals;
   }

   Env exec(Program prog, Env env) {
      prog.call(env, name, actuals);
      return env;
   }

   void print(int ind) {
      indent(ind);
      System.out.print("" + name + "(");
      for (int i = 0; i < actuals.length; i++) {
         System.out.print(actuals[i].show());
         if (i != actuals.length - 1) {
            System.out.print(", ");
         }
      }
      System.out.println(");");
   }
}

class Case extends Stmt {
   private Expr   expr;
   private Stmt   ifEmpty;
   private String h;
   private String t;
   private Stmt   ifNonEmpty;
   Case(Expr expr, Stmt ifEmpty, String h, String t, Stmt ifNonEmpty) {
      this.expr       = expr;
      this.ifEmpty    = ifEmpty;
      this.h          = h;
      this.t          = t;
      this.ifNonEmpty = ifNonEmpty;
   }

   Env exec(Program prog, Env env) {
      Value val = expr.eval(env);

      if (val instanceof EmptyList) {
         return ifEmpty.exec(prog, env);
      } else if (val instanceof NonEmptyList) {
         NonEmptyList list = (NonEmptyList)val;
         Value head = list.getHead();
         Value tail = list.getTail();
         env = new ValEnv(h, head, new ValEnv(t, tail, env));
         return ifNonEmpty.exec(prog, env);
      } else {
         System.out.println(Errors.LIST_VALUE_EXPECTED.toString());
         System.exit(1);
         return env;
      }
   }

   void print(int ind) {
      indent(ind);
      System.out.println("case " + expr.show() + " of");
      indent(ind+2);
      System.out.println("[] ->");
      ifEmpty.print(ind+4);
      indent(ind+2);
      System.out.println("cons(" + h + ", " + t + ") ->");
      ifNonEmpty.print(ind+4);
      System.out.println("end");
   }
}

class For extends Stmt {
   private String v;
   private Expr list;
   private Stmt body;
   For(String v, Expr list, Stmt body) {
      this.v = v; this.list = list; this.body = body;
   }

   Env exec(Program prog, Env env) {
      Value evaledList = list.eval(env);
      if (evaledList instanceof NonEmptyList) {
         NonEmptyList l = (NonEmptyList)evaledList;
         env = new ValEnv(v, new EmptyList(), env);
         for(Value val : l.getArrayList()) {
            env.setValue(val);
            env = body.exec(prog, env);
         }
         return env;
      } else if (evaledList instanceof EmptyList) {
         return env;
      } else {
         System.out.println(Errors.LIST_VALUE_EXPECTED.toString());
         System.exit(1);
         return env; // Not reached
      }
   }

   void print(int ind) {
      indent(ind);
      System.out.println("for (" + v + " in " + list.show() + ") {");
      body.print(ind+2);
      indent(ind);
      System.out.println("}");
   }
}

class Program {
   private Proc[] procs;
   private Stmt body;

   Program(Stmt body) {
      this.body = body;
   }
   Program(Proc[] procs, Stmt body) {
      this.procs = procs; this.body = body;
   }

   void run() {
      body.exec(this, null);
   }

   void call(Env env, String name, Expr[] actuals) {
      for (Proc proc : procs) {
         if (name.equals(proc.getName())) {
            proc.call(this, env, actuals);
            return;
         }
      }
      System.out.println(Errors.CANNOT_FIND_FUNCTION.toString() + name);
      System.exit(1);
   }

   void print() {
      int indent = 0;
      printProcs(indent);
      body.print(indent);
      System.out.println();
   }

   void printProcs(int indent) {
      if (procs != null) {
         for (Proc proc: procs) {
            proc.print(indent);
         }
      }
   }
}

class Formal {
   protected String name;

   Formal(String name) {
      this.name = name;
   }

   Env extend(Env env, Expr expr, Env newenv) {
      return new ValEnv(name, expr.eval(env), newenv);
   }

   public String toString() {
      return name;
   }
}

class ByRef extends Formal {
   ByRef(String name) { super(name); }

   public String toString() {
      return "ref " + name;
   }

   Env extend(Env env, Expr expr, Env newenv) {
      return new RefEnv(name, expr.evalRef(env), newenv);
   }
}

class Proc {
   private String name;
   private Formal[] formals;
   private Stmt body;

   Proc(String name, Formal[] formals, Stmt body) {
      this.name = name; this.formals = formals; this.body = body;
   }

   String getName() { return name; }

   void call(Program prog, Env env, Expr[] actuals) {
      if (actuals.length != formals.length) {
         System.out.println(Errors.WRONG_NUMBER_OF_ARGUMENTS.toString() + name);
         System.exit(1);
      }
      Env newenv = null;
      for (int i = 0; i < actuals.length; i++) {
         newenv = formals[i].extend(env, actuals[i], newenv);
      }
      body.exec(prog, newenv);
   }

   void print(int ind) {
      Stmt.indent(ind);
      System.out.print("procedure " + name + "(");
      for (int i = 0; i < formals.length; i++) {
         if (i > 0) {
            System.out.print(", ");
         }
         System.out.print(formals[i]);
      }
      System.out.println(") {");

      body.print(ind+2);
      
      Stmt.indent(ind);
      System.out.println("}");
   }
}

