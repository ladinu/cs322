package com.compilers.hw3; // TODO: remove

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;

/** Simplistic register assignment **/
class Assignment {

  /** Assign IR.Regs to locations described by X86.Operands. **/
  static Map<IR.Reg,X86.Reg> assignRegisters(IR.Func func,List<Set<IR.Reg>> liveOutSets) {

    Map<IR.Reg,X86.Reg> env = new HashMap<IR.Reg,X86.Reg>();

    // Get non-binding preferences for assignments. 
    Map<IR.Reg,X86.Reg> preferences = IR.getPreferences(func);

    Map<IR.Reg,Set<Integer>> liveRanges = Liveness.calculateLiveRanges(liveOutSets);

    Graph g = new Graph();
    for(IR.Reg reg : liveRanges.keySet()) {
      g.addNode(reg);
    }

    for(Set<IR.Reg> regSet : liveOutSets) {
      for(IR.Reg reg1 : regSet) {
        for(IR.Reg reg2 : regSet) {
          try {
            g.addEdge(reg1.toString(), reg2.toString());
          } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
          }
        }
      }
    }

    ArrayDeque<Pair> stack = new ArrayDeque<Pair>();

    // Populate the stack
    while (!g.isEmpty()) {
      ArrayList<Node> neighbors = new ArrayList<Node>();

      Node n = g.minDegreeNode();
      for (Node neighbor : n.getNeighbors().values()) {
        neighbors.add(neighbor);
      }
      Pair p = new Pair(n, neighbors);
      stack.push(p);
      try {
        g.removeNode(n.getName());
      } catch (Exception e) {
        System.err.println(e);
        System.exit(1);
      }
    }


    while (!stack.isEmpty()) {
      // Keep track of available registers
      Set<X86.Reg> availableRegs = new HashSet<X86.Reg>();
      // start by assuming all registers are available
      for (X86.Reg r : X86.allRegs)
        availableRegs.add(r);
      // always rule out special-purpose registers
      availableRegs.remove(X86.RSP);
      availableRegs.remove(IR.tempReg1);
      availableRegs.remove(IR.tempReg2);

      Pair p = stack.pop();
      Node node = p.getNode();


      for (Node n : p.getNeighbors()) {
        if (env.containsKey(n.irReg)) {
          availableRegs.remove(env.get(n.irReg));
        }
      }

      X86.Reg r = preferences.get(node.irReg);
      Set<Integer> s = liveRanges.get(node.irReg);

      X86.Reg reg = findAssignment(
          availableRegs,
          r,
          rangeContainsCall(func, s)
      );
      node.x86Reg = reg;
      if (reg == null) {
        System.err.println("oops: out of registers");
        assert (false);
      }
      env.put(node.irReg, reg);
    }

    // ... TO HERE

    // For documentation purposes
    System.out.println("# Allocation map");
    for (Map.Entry<IR.Reg,X86.Reg> me : env.entrySet())
      System.out.println("# " + me.getKey() + "\t" + me.getValue());

    return env;
  }

  /** Find assignment for a register from the available set.
      Try to match a preferred register if one is given.
      Try to use a callee-save register if live range of this
      register overlaps a call; otherwise, try to use a caller-save register. */
  static  X86.Reg findAssignment(Set<X86.Reg> available,
				 X86.Reg preference, // may be null
				 boolean rangeContainsCall) {

    // Try to find a register
    X86.Reg treg = null;
      
    if (rangeContainsCall) {
      // try for a callee-save reg (ignoring preference for now)
      for (X86.Reg reg : X86.calleeSaveRegs) 
	  if (available.contains(reg)) {
	  treg = reg;
	  break;
	}
      if (treg == null) {
	// otherwise, try for a preference register (always caller-save)
	if (preference !=null && available.contains(preference))
	  treg = preference;
      }
      if (treg == null)
	// otherwise, try for arbitrary caller-save reg, but trying first
	// those without special roles
	for (int i = X86.callerSaveRegs.length - 1; i >= 0; i--) {
	  X86.Reg reg = X86.callerSaveRegs[i];
	  if (available.contains(reg)) {
	    treg = reg;
	    break;
	  }
	}
    } else {
      // try first for a preference register (always caller-save)
      if (preference !=null && available.contains(preference)) 
	treg = preference;
      if (treg == null) 
	// try for arbitrary caller-save reg
	for (X86.Reg reg : X86.callerSaveRegs) 
	  if (available.contains(reg)) {
	    treg = reg;
	    break;
	  }
      if (treg == null) 
	// otherwise, try a callee-save 
	for (X86.Reg reg : X86.calleeSaveRegs) 
	  if (available.contains(reg)) {
	    treg = reg;
	    break;
	  }
    }
    return treg;
  }

  /**  Return true if specified range covers an IR instruction
       that will cause an X86.call (or invoke an X86.divide).
       By "cover" we mean that the register is liveIn and liveOut
       of the instruction, but our range only contains liveOut data.
       Fortunately, because labels occupy an instruction slot,
       we know that the liveIn for any (real) instruction equals
       the liveOut of its predecessor.
 **/
  static boolean rangeContainsCall(IR.Func func,Set<Integer> s) {
    for (int i: s) 
      if ((s.contains(i-1)) &&
	  (func.code[i] instanceof IR.Call ||
	   (func.code[i] instanceof IR.Binop && 
	    ((IR.Binop) func.code[i]).op == IR.ArithOP.DIV)))
	return true;
    return false;
  }

}