// This is supporting software for CS322 Compilers and Language Design II
// Copyright (c) Portland State University
// 
// IR code generator for miniJava's AST.
//
// (Starting version.)
//
package com.compiler;

import java.util.*;
import java.io.*;
import com.compiler.ast.*;
import com.compiler.ir.*;
import com.sun.org.apache.bcel.internal.classfile.Code;

public class IRGen {

  static class GenException extends Exception {
    public GenException(String msg) { super(msg); }
  }

  //------------------------------------------------------------------------------
  // Class Information Record
  //-------------------------
  //  For keeping all useful information about a class declaration 
  //  for later use in the codegen.
  //
  static class ClassInfo {
    String name;			// class name
    ClassInfo parent;			// ptr to parent's record
    boolean isMainClass; 		// true if class contains "main"
    Ast.ClassDecl classDecl; 		// class source ast
    ArrayList<String> vtable; 		// (virtual) method table        
    HashMap<String,Integer> offsets; 	// field variable offsets
    int objSize; 			// object size

    // Constructor -- clone a parent's record
    //
    ClassInfo(Ast.ClassDecl cdecl, ClassInfo parent) {
      this.name = cdecl.nm;
      this.parent = parent;
      this.isMainClass = false; 
      this.classDecl = cdecl;
      this.vtable = new ArrayList<String>(parent.vtable);
      this.offsets = new HashMap<String,Integer>(parent.offsets); 
      this.objSize = parent.objSize;
    }      

    // Constructor -- create a new record
    //
    ClassInfo(Ast.ClassDecl cdecl) {
      this.name = cdecl.nm;
      this.parent = null;
      this.isMainClass = false; 
      this.classDecl = cdecl;
      this.vtable = new ArrayList<String>();
      this.offsets = new HashMap<String,Integer>(); 
      this.objSize = IR.Type.PTR.size; 	// reserve space for ptr to class
    }      

    // Utility Routines
    // ----------------
    // For accessing information stored in class information record
    //

    // Find method's base class record
    //
    ClassInfo methodBaseClass(String mname) throws Exception {
      for (Ast.MethodDecl mdecl: classDecl.mthds)
	if (mdecl.nm.equals(mname)) 
	  return this;
      if (parent != null)
        return parent.methodBaseClass(mname);
      throw new GenException("Can't find base class for method " + mname);
    }	

    // Find method's return type
    //
    Ast.Type methodType(String mname) throws Exception {
      for (Ast.MethodDecl mdecl: classDecl.mthds)
	if (mdecl.nm.equals(mname))
	  return mdecl.t;
      if (parent != null)
        return parent.methodType(mname);
      throw new GenException("Can't find MethodDecl for method " + mname);
    }

    // Return method's vtable offset
    //
    int methodOffset(String mname) {
      return vtable.indexOf(mname) * IR.Type.PTR.size;
    }

    // Find field variable's type
    //
    Ast.Type fieldType(String fname) throws Exception {
      for (Ast.VarDecl fdecl: classDecl.flds) {
	if (fdecl.nm.equals(fname))
	  return fdecl.t;
      }
      if (parent != null)
        return parent.fieldType(fname);
      throw new GenException("Can't find VarDecl for field " + fname);
    }

    // Return field variable's offset
    //
    int fieldOffset(String fname) {
      return offsets.get(fname);
    }

    public String toString() {
      return "ClassInfo: " + " " + name + " " + parent + " " + isMainClass
	+ " " + vtable + " " + offsets + " " + objSize + " " + classDecl;
    }
  }

  // End of Class Information Record ---------------------------------------------


  //------------------------------------------------------------------------------
  // Other Supporting Data Structures
  //---------------------------------

  // CodePack
  // --------
  // For returning <type,src,code> tuple from gen routines
  //
  static class CodePack {
    Ast.Type type;
    IR.Src src;
    List<IR.Inst> code;
    CodePack(Ast.Type type, IR.Src src, List<IR.Inst> code) { 
      this.type=type; this.src=src; this.code=code; 
    }
    CodePack(Ast.Type type, IR.Src src) { 
      this.type=type; this.src=src; code=new ArrayList<IR.Inst>(); 
    }
  }

  // AddrPack
  // --------
  // For returning <type,addr,code> tuple from genAddr routines
  //
  static class AddrPack {
    Ast.Type type;
    IR.Addr addr;
    List<IR.Inst> code;
    AddrPack(Ast.Type type, IR.Addr addr, List<IR.Inst> code) { 
      this.type=type; this.addr=addr; this.code=code; 
    }
  }

  // Env
  // ---
  // For keeping track of local variables and parameters and for finding 
  // their types.
  //
  private static class Env extends HashMap<String, Ast.Type> {}

  // End of Other Supporting Data Structures -------------------------------------


  //------------------------------------------------------------------------------
  // Global Variables
  // ----------------
  //

  // The whole collection of ClassInfo records
  private static HashMap<String, ClassInfo> classInfos 
                             = new HashMap<String, ClassInfo>();

  // IR code representation of the current object
  private static IR.Src thisObj = new IR.Id("obj");

  // Constant nodes for convenience 
  private static final Ast.Type AstIntType = new Ast.IntType();
  private static final Ast.Type AstBoolType = new Ast.BoolType();

  // Helper methods
  private static final IR.Type toIRType(Ast.Type t) {
    if (t instanceof Ast.BoolType) {
      return IR.Type.BOOL;
    } else if (t instanceof Ast.IntType) {
      return IR.Type.INT;
    } else if (t instanceof Ast.ObjType) {
      return IR.Type.PTR;
    } else {
      throw new Error("Unknown type " + t);
    }
  }


  //------------------------------------------------------------------------------
  // The Main Codegen Routine
  //-------------------------
  //
  public static void main(String [] args) throws Exception {
    if (args.length == 1) {
      FileInputStream stream = new FileInputStream(args[0]);
      Ast.Program p = new astParser(stream).Program();
      stream.close();
      IR.Program ir = IRGen.gen(p);
      System.out.print(ir.toString());
    } else {
      System.out.println("You must provide an input file name.");
    }
  }

  // TODO: remove this method before turning in
  public String generate(String miniJava) throws Exception {
    Ast.Program p = new astParser(new StringReader(miniJava)).Program();
    IR.Program ir = IRGen.gen(p);
    return ir.toString();
  }

  // Sort ClassDecls, so that parent will be visited before children.
  //
  private static Ast.ClassDecl[] topoSort(Ast.ClassDecl[] classes) {
    List<Ast.ClassDecl> cl = new ArrayList<Ast.ClassDecl>();
    Vector<String> done = new Vector<String>();
    int cnt = classes.length;
    while (cnt > 0) {
      for (Ast.ClassDecl cd: classes)
	if (!done.contains(cd.nm)
	    && ((cd.pnm == null) || done.contains(cd.pnm))) {
	  cl.add(cd);
	  done.add(cd.nm);
	  cnt--;
	} 
    }
    return cl.toArray(new Ast.ClassDecl[0]);
  }

  // Create class info record
  //
  // Codegen Guideline: 
  // 1. If parent exists, clone parent's record; otherwise create a new one
  // 2. Walk the MethodDecl list. If a method is not in the v-table, add it in;
  // 3  If the "main" method is in the list, set 'isMainClass' flag to true
  // 4. Compute offset values for field variables
  // 5. Decide object's size
  //
  private static ClassInfo createClassInfo(Ast.ClassDecl n) throws Exception {
    ClassInfo cinfo = (n.pnm != null) ?
        new ClassInfo(n, classInfos.get(n.pnm)) : new ClassInfo(n);

    for (Ast.MethodDecl method: n.mthds) {
      if(!cinfo.vtable.contains(method.nm)) {
        cinfo.vtable.add(method.nm);
      }
      cinfo.isMainClass = method.nm.equals("main");
    }


    int size = cinfo.objSize;
    int offset = 0;
    boolean flag = true;

    for (Ast.VarDecl field : n.flds) {

      int fieldSize;
      if (field.t instanceof Ast.IntType) {
        fieldSize = IR.Type.INT.size;
      } else if (field.t instanceof Ast.BoolType) {
        fieldSize = IR.Type.BOOL.size;
      } else {
        fieldSize = IR.Type.PTR.size;
      }

      size += fieldSize;

      if (flag) {
        cinfo.offsets.put(field.nm, 0);
        flag = false;
      } else {
        offset += fieldSize;
        cinfo.offsets.put(field.nm, offset);
      }
    }

    cinfo.objSize = size;
    return cinfo;
  }

  //------------------------------------------------------------------------------
  // Codegen Routines for Individual AST Nodes
  //------------------------------------------

  // Program ---
  // ClassDecl[] classes;
  //
  // Three passes over class decls:
  //  0. topo-sort class decls
  //  1. create class info records 
  //  2. generate IR code
  //     2.1 generate list of static data (i.e. class descriptors)
  //     2.2 generate list of functions
  //
  public static IR.Program gen(Ast.Program n) throws Exception {
    Ast.ClassDecl[] classes = topoSort(n.classes);
    ClassInfo cinfo;
    for (Ast.ClassDecl c: classes) {
      cinfo = createClassInfo(c);
      classInfos.put(c.nm, cinfo);
    }
    List<IR.Data> allData = new ArrayList<IR.Data>();
    List<IR.Func> allFuncs = new ArrayList<IR.Func>();
    for (Ast.ClassDecl c: classes) {
      cinfo = classInfos.get(c.nm);
      IR.Data data = genData(c, cinfo);
      List<IR.Func> funcs = gen(c, cinfo);
      if (data != null)
	allData.add(data);
      allFuncs.addAll(funcs);
    }
    return new IR.Program(allData, allFuncs);
  }

  // ClassDecl ---
  // String nm, pnm;
  // VarDecl[] flds;
  // MethodDecl[] mthds;
  //

  // 1. Generate static data
  //
  // Codegen Guideline: 
  //   1.1 For each method in class's vtable, construct a global label of form
  //       "<base class name>_<method name>" and save it in an IR.Global node
  //   1.2 Assemble the list of IR.Global nodes into an IR.Data node with a
  //       global label "class_<class name>" 
  //
  // (Skip this method if class is the static class containing "main".)
  //
  static IR.Data genData(Ast.ClassDecl n, ClassInfo cinfo) throws Exception {
    if (cinfo.isMainClass) return null;

    String pnm  = (cinfo.parent == null) ? cinfo.name : cinfo.parent.name;
    ArrayList<IR.Global> globalList = new ArrayList<IR.Global>();

    for (String method : cinfo.vtable) {
      globalList.add(new IR.Global(pnm + "_" + method));
    }

    int heapSize = 0;
    for (Ast.VarDecl field : n.flds) {
      heapSize += cinfo.fieldOffset(field.nm);
    }

    return new IR.Data(new IR.Global("class_" + cinfo.name), heapSize, globalList);
  }

  // 2. Generate code
  //
  // Codegen Guideline: 
  //   Straightforward -- generate a IR.Func for each mthdDecl.
  //
  static List<IR.Func> gen(Ast.ClassDecl n, ClassInfo cinfo) throws Exception {
    ArrayList<IR.Func> funcList = new ArrayList<IR.Func>();
    for (Ast.MethodDecl mthd : n.mthds) {
      funcList.add(gen(mthd, cinfo));
    }
    return funcList;
  }

  // MethodDecl ---
  // Type t;
  // String nm;
  // Param[] params;
  // VarDecl[] vars;
  // Stmt[] stmts;
  //
  // Codegen Guideline: 
  // 1. Construct a global label of form "<base class name>_<method name>"
  // 2. Add "obj" into the params list as the 0th item
  // (Skip these two steps if method is "main".)
  // 3. Create an Env() containing all params and all local vars 
  // 4. Generate IR code for all statements
  // 5. Return an IR.Func with the above
  //
  static IR.Func gen(Ast.MethodDecl n, ClassInfo cinfo) throws Exception {
    // Populate the env with params and local vars
    Env env = new Env();
    for (Ast.Param param : n.params) {
      env.put(param.nm, param.t);
    }
    for (Ast.VarDecl var : n.vars) {
      env.put(var.nm, var.t);
    }

    String funcName = (cinfo.isMainClass) ?
        n.nm : cinfo.name + "_" + n.nm;

    // Get list of params with type String
    ArrayList<String> params = new ArrayList<String>();
    if (!cinfo.isMainClass) params.add("obj");

    for (Ast.Param param: n.params) {
      params.add(param.nm);
    }
    // Get list of locals with type String
    ArrayList<String> locals = new ArrayList<String>();
    for (Ast.VarDecl var: n.vars) {
      locals.add(var.nm);
    }
    // Get a list of IR instructions for function body
    ArrayList<IR.Inst> body = new ArrayList<IR.Inst>();
    body.add(new IR.LabelDec("Begin"));

    // Add Inst for VarDecls
    for (Ast.VarDecl var: n.vars) {
      body.addAll(gen(var, cinfo, env));
    }

    // Add Inst for all other statements
    for (Ast.Stmt stmt : n.stmts) {
      body.addAll(gen(stmt, cinfo, env));
    }
    // Check if method return type is void
    if (cinfo.methodType(n.nm) == null) {
      body.add(new IR.Return(null));
    }
    body.add(new IR.LabelDec("End"));

    return new IR.Func(funcName, params, locals, body);
  }

  // VarDecl ---
  // Type t;
  // String nm;
  // Exp init;
  //
  // Codegen Guideline: 
  // 1. If init exp exists, generate IR code for it and assign result to var
  // 2. Return generated code (or null if none)
  //
  private static List<IR.Inst> gen(Ast.VarDecl n, ClassInfo cinfo,
				    Env env) throws Exception {
    ArrayList<IR.Inst> code = new ArrayList<IR.Inst>();
    if (n.init == null) return code;
    CodePack pack = gen(n.init, cinfo, env);

    if (pack != null) {
      IR.Move mov = new IR.Move(new IR.Id(n.nm), pack.src);
      code.addAll(pack.code);
      code.add(mov);
    }
    return code;
  }

  // STATEMENTS

  // Dispatch a generic call to a specific Stmt routine
  // 
  static List<IR.Inst> gen(Ast.Stmt n, ClassInfo cinfo, Env env) throws Exception {
    if (n instanceof Ast.Block) 	return gen((Ast.Block) n, cinfo, env);
    else if (n instanceof Ast.Assign)   return gen((Ast.Assign) n, cinfo, env);
    else if (n instanceof Ast.CallStmt) return gen((Ast.CallStmt) n, cinfo, env);
    else if (n instanceof Ast.If) 	return gen((Ast.If) n, cinfo, env);
    else if (n instanceof Ast.While)    return gen((Ast.While) n, cinfo, env);
    else if (n instanceof Ast.Print)    return gen((Ast.Print) n, cinfo, env);
    else if (n instanceof Ast.Return)   return gen((Ast.Return) n, cinfo, env);
    throw new GenException("Illegal Ast Stmt: " + n);
  }

  // Block ---
  // Stmt[] stmts;
  //
  static List<IR.Inst> gen(Ast.Block n, ClassInfo cinfo, Env env) throws Exception {


    //    ... need code
    // TODO: implement

    throw new Exception("gen BLOCK");
  }

  // Assign ---
  // Exp lhs, rhs;
  //
  // Codegen Guideline: 
  // 1. call gen() on rhs
  // 2. if lhs is ID, check against Env to see if it's a local var or a param;
  //    if yes, generate an IR.Move instruction
  // 3. otherwise, call genAddr() on lhs, and generate an IR.Store instruction
  //
  static List<IR.Inst> gen(Ast.Assign n, ClassInfo cinfo, Env env) throws Exception {
    ArrayList<IR.Inst> codes = new ArrayList<IR.Inst>();
    CodePack rhsPack = gen(n.rhs, cinfo, env);

    codes.addAll(rhsPack.code);

    if (n.lhs instanceof Ast.Id && (env.get(((Ast.Id) n.lhs).nm) != null)) {
      IR.Move mov = new IR.Move(new IR.Id(((Ast.Id) n.lhs).nm), rhsPack.src);
      codes.add(mov);
    } else {
      AddrPack addrPack = genAddr(n.lhs, cinfo, env);

      IR.Type type = toIRType(addrPack.type);

      IR.Store store = new IR.Store(type, addrPack.addr, rhsPack.src);
      codes.add(store);
    }

    return codes;
  }

  // CallStmt ---
  // Exp obj; 
  // String nm;
  // Exp[] args;
  //
  //
  static List<IR.Inst> gen(Ast.CallStmt n, ClassInfo cinfo, Env env) throws Exception {
    if (n.obj != null) {
      CodePack p = handleCall(n.obj, n.nm, n.args, cinfo, env, false);
      return p.code;
    }
    throw new GenException("In CallStmt, obj is null " + n);  
  }

  // handleCall
  // ----------
  // Common routine for Call and CallStmt nodes
  //
  // Codegen Guideline: 
  // 1. Invoke gen() on obj, which returns obj's storage address (and type and code)
  // 2. With type info in the returning CodePack, figure out obj's base class
  // 3. Access the base class's ClassInfo rec to get the method's offset in vtable 
  // 4. Add obj's as the 0th argument to the args list
  // 5. Generate an IR.Load to get the class descriptor from obj's storage
  // 6. Generate another IR.Load to get the method's global label
  // 7. If retFlag is set, prepare a temp for receiving return value; also figure
  //    out return value's type (through method's decl in ClassInfo rec)
  // 8. Generate an indirect call with the global label
  //
  static CodePack handleCall(Ast.Exp obj, String name, Ast.Exp[] args, 
			     ClassInfo cinfo, Env env, boolean retFlag) throws Exception {


    //    ... need code
    // TODO: implement

    throw new Exception("handleCall");
  }

  // If ---
  // Exp cond;
  // Stmt s1, s2;
  //
  // (See class notes.)
  //
  static List<IR.Inst> gen(Ast.If n, ClassInfo cinfo, Env env) throws Exception {
    List<IR.Inst> codes = new ArrayList<IR.Inst>();
    CodePack p = gen(n.cond, cinfo, env);

    IR.Label L1 = new IR.Label();

    codes.addAll(p.code);
    codes.add(new IR.CJump(IR.RelOP.EQ, p.src, IR.FALSE, L1));
    codes.addAll(gen(n.s1, cinfo, env));

    if (n.s2 == null) {
      codes.add(new IR.LabelDec(L1.toString()));
    } else {
      IR.Label L2 = new IR.Label();
      codes.add(new IR.Jump(L2));
      codes.add(new IR.LabelDec(L1.toString()));
      codes.addAll(gen(n.s2, cinfo, env));
      codes.add(new IR.LabelDec(L2.toString()));
    }
    return codes;
  }

  // While ---
  // Exp cond;
  // Stmt s;
  //
  // (See class notes.)
  //
  static List<IR.Inst> gen(Ast.While n, ClassInfo cinfo, Env env) throws Exception {
    List<IR.Inst> codes = new ArrayList<IR.Inst>();

    IR.Label L1 = new IR.Label();
    IR.Label L2 = new IR.Label();

    codes.add(new IR.LabelDec(L1.toString()));

    CodePack p = gen(n.cond, cinfo, env);
    codes.addAll(p.code);
    codes.add(new IR.CJump(IR.RelOP.EQ, p.src, IR.FALSE, L2));
    codes.addAll(gen(n.s, cinfo, env));
    codes.add(new IR.Jump(L1));
    codes.add(new IR.LabelDec(L2.toString()));
    return codes;
  }
  
  // Print ---
  // Exp arg;
  //
  // Codegen Guideline: 
  // 1. If arg is null, generate an IR.Call with "print"
  // 2. If arg is StrLit, generate an IR.Call with "printStr"
  // 3. Otherwise, generate IR code for arg, and use its type info
  //    to decide which of the two functions, "printInt" and "printBool",
  //    to call
  //
  static List<IR.Inst> gen(Ast.Print n, ClassInfo cinfo, Env env) throws Exception {
    ArrayList<IR.Inst> codes = new ArrayList<IR.Inst>();
    ArrayList<IR.Src>  args = new ArrayList<IR.Src>();


    if (n.arg == null) {
      codes.add(new IR.Call(new IR.Global("print"), false, args));
    } else if (n.arg instanceof Ast.StrLit) {
      String argStr = ((Ast.StrLit)n.arg).s;
      IR.StrLit arg = new IR.StrLit(argStr);
      args.add(arg);
      codes.add(new IR.Call(new IR.Global("printStr"), false, args));
    } else {
      CodePack codePack = gen(n.arg, cinfo, env);
      codes.addAll(codePack.code);
      args.add(codePack.src);

      if (codePack.type instanceof Ast.BoolType) {
        codes.add(new IR.Call(new IR.Global("printBool"), false, args));
      } else if (codePack.type instanceof Ast.IntType) {
        codes.add(new IR.Call(new IR.Global("printInt"), false, args));
      } else {
        throw new Exception("cannot print " + codePack.type.toString());
      }
    }
    return codes;
  }

  // Return ---  
  // Exp val;
  //
  // Codegen Guideline: 
  // 1. If val is non-null, generate IR code for it, and generate an IR.Return
  //    with its value
  // 2. Otherwise, generate an IR.Return with no value
  //
  static List<IR.Inst> gen(Ast.Return n, ClassInfo cinfo, Env env) throws Exception {
    ArrayList<IR.Inst> codes = new ArrayList<IR.Inst>();
    if (n.val == null) {
      codes.add(new IR.Return(null));
    } else {
      CodePack pack = gen(n.val, cinfo, env);
      codes.addAll(pack.code);
      codes.add(new IR.Return(pack.src));
    }
    return codes;
  }

  // EXPRESSIONS

  // 1. Dispatch a generic gen call to a specific gen routine
  //
  static CodePack gen(Ast.Exp n, ClassInfo cinfo, Env env) throws Exception {
    if (n instanceof Ast.Call)    return gen((Ast.Call) n, cinfo, env);
    if (n instanceof Ast.NewObj)  return gen((Ast.NewObj) n, cinfo, env);
    if (n instanceof Ast.Field)   return gen((Ast.Field) n, cinfo, env);
    if (n instanceof Ast.Id)	  return gen((Ast.Id) n, cinfo, env);
    if (n instanceof Ast.This)    return gen((Ast.This) n, cinfo);
    if (n instanceof Ast.IntLit)  return gen((Ast.IntLit) n);
    if (n instanceof Ast.BoolLit) return gen((Ast.BoolLit) n);
    if (n instanceof Ast.StrLit)  return gen((Ast.StrLit) n);
    throw new GenException("Exp node not supported in this codegen: " + n);
  }

  // 2. Dispatch a generic genAddr call to a specific genAddr routine
  //    (Only one LHS Exp needs to be implemented for this assignment)
  //
  static AddrPack genAddr(Ast.Exp n, ClassInfo cinfo, Env env) throws Exception {
    if (n instanceof Ast.Field) return genAddr((Ast.Field) n, cinfo, env);
    throw new GenException(" LHS Exp node not supported in this codegen: " + n);
  }

  // Call ---
  // Exp obj; 
  // String nm;
  // Exp[] args;
  //
  static CodePack gen(Ast.Call n, ClassInfo cinfo, Env env) throws Exception {
    if (n.obj != null)
	return handleCall(n.obj, n.nm, n.args, cinfo, env, true);
    throw new GenException("In Call, obj is null: " + n);  
  } 
  
  // NewObj ---
  // String cn;
  // Exp[] args; (ignored)
  //
  // Codegen Guideline: 
  //  1. Use class name to find the corresponding ClassInfo record
  //  2. Find the class's type and object size from the ClassInfo record
  //  3. Cosntruct a malloc call to allocate space for the object
  //  4. Store a pointer to the class's descriptor into the first slot of
  //     the allocated space
  //
  static CodePack gen(Ast.NewObj n, ClassInfo cinfo, Env env) throws Exception {
    ArrayList<IR.Inst> codes = new ArrayList<IR.Inst>();
    ArrayList<IR.Src>  args = new ArrayList<IR.Src>();

    ClassInfo newCinfo = classInfos.get(n.nm);
    args.add(new IR.IntLit(newCinfo.objSize));

    IR.Temp t0 = new IR.Temp();
    IR.Call malloc = new IR.Call(new IR.Global("malloc"),false, args, t0);

    IR.Addr addr = new IR.Addr(t0, 0);

    IR.Store store = new IR.Store(IR.Type.PTR, addr, new IR.Global("class_"+newCinfo.name));

    codes.add(malloc);
    codes.add(store);

    return new CodePack(new Ast.ObjType(newCinfo.name), t0, codes);
  }
  
  // Field ---
  // Exp obj; 
  // String nm;
  //

  // 1. gen()
  //
  // Codegen Guideline: 
  //   1.1 Call genAddr to generate field variable's address
  //   1.2 Add an IR.Load to get its value
  //
  static CodePack gen(Ast.Field n, ClassInfo cinfo, Env env) throws Exception {
    ArrayList<IR.Inst> codes = new ArrayList<IR.Inst>();

    AddrPack addrPack = genAddr(n, cinfo, env);
    IR.Temp t = new IR.Temp();
    IR.Load load = new IR.Load(toIRType(addrPack.type), t, addrPack.addr);

    codes.addAll(addrPack.code);
    codes.add(load);

    CodePack pack = new CodePack(addrPack.type, t, codes);
    return pack;
  }
  
  // 2. genAddr()
  //
  // Codegen Guideline: 
  //   2.1 Call gen() on the obj component
  //   2.2 Use the type info to figure out obj's base class
  //   2.3 Access base class's ClassInfo rec to get field variable's offset
  //   2.4 Generate an IR.Addr based on the offset
  //
  static AddrPack genAddr(Ast.Field n, ClassInfo cinfo, Env env) throws Exception {
    CodePack pack = gen(n.obj, cinfo, env);
    ClassInfo baseCinfo = classInfos.get(((Ast.ObjType)pack.type).nm);

    int fieldOffset = IR.Type.PTR.size + baseCinfo.fieldOffset(n.nm);
    IR.Addr addr = new IR.Addr(pack.src, fieldOffset);

    return new AddrPack(baseCinfo.fieldType(n.nm), addr, pack.code);
  }
  
  // Id ---
  // String nm;
  //
  // Codegen Guideline: 
  //  1. Check to see if the Id is in the env.
  //  2. If so, it means it is a local variable or a parameter. Just return
  //     a CodePack containing the Id.
  //  3. Otherwise, the Id is an instance variable. Convert it into an
  //     Ast.Field node with Ast.This() as its obj, and invoke the gen routine 
  //     on this new node
  //
  static CodePack gen(Ast.Id n, ClassInfo cinfo, Env env) throws Exception {
    CodePack pack;
    Ast.Type type = env.get(n.nm);

    if (type != null) {
      pack = new CodePack(type, new IR.Id(n.nm));
    } else {
      Ast.Field field = new Ast.Field(new Ast.This(), n.nm);
      pack = gen(field, cinfo, env);
    }
    return pack;
  }

  // This ---
  //
  static CodePack gen(Ast.This n, ClassInfo cinfo) throws Exception {
    return new CodePack(new Ast.ObjType(cinfo.name), thisObj);
  }

  // IntLit ---
  // int i;
  //
  static CodePack gen(Ast.IntLit n) throws Exception {
    return  new CodePack(AstIntType, new IR.IntLit(n.i));
  }

  // BoolLit ---
  // boolean b;
  //
  static CodePack gen(Ast.BoolLit n) throws Exception {
    return  new CodePack(AstBoolType, n.b ? IR.TRUE : IR.FALSE);
  }

  // StrLit ---
  // String s;
  //
  static CodePack gen(Ast.StrLit n) throws Exception {
    return new CodePack(null, new IR.StrLit(n.s));
  }

  // Type mapping (AST -> IR)
  //
  static IR.Type gen(Ast.Type n) throws Exception {
    if (n == null)                  return null;
    if (n instanceof Ast.IntType)   return IR.Type.INT;
    if (n instanceof Ast.BoolType)  return IR.Type.BOOL;
    if (n instanceof Ast.ObjType)   return IR.Type.PTR;
    throw new GenException("Invalid Ast type: " + n);
  }

}
