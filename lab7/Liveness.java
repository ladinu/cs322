import java.io.*;
import java.util.*;

/**  Liveness analysis producing LiveOut sets */
class Liveness {
  public static void main(String [] args) {
    try {
      if (args.length == 1) {
	FileInputStream stream = new FileInputStream(args[0]);
	IR.Program p = new irParser(stream).Program();
	stream.close();
	IR.indexed = true;
	for (IR.Func f : p.funcs) {
	  assert (f.code[0] instanceof IR.LabelDec);
	  assert (f.code[f.code.length-1] instanceof IR.LabelDec);
	  List<Set<IR.Reg>> liveOutSets = calculateLiveOutSets(f);
	  calculateLiveRanges(liveOutSets);
	}
      } else {
	System.err.println("Need a file name as command-line argument.");
      }
    } catch (TokenMgrError e) {
      System.err.println(e.getMessage());
    } catch (ParseException e) {
      System.err.println(e.getMessage());
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }


  // Calculate liveOut sets for each instruction in a function
  static List<Set<IR.Reg>> calculateLiveOutSets (IR.Func func) {
    List<Set<Integer>> succs = func.successors();
    
    List<Set<IR.Reg>> used = func.used();
    List<Set<IR.Reg>> defined = func.defined();
    // DEBUG
    // System.err.println(func.name + " succs/use/def:");
    // for (int i = 0; i < func.code.length; i++) 
    //   System.err.println(i + "\t" + "S:" + succs.get(i) + "\t" + "U:" + used.get(i) + "\t" + "D:" + defined.get(i));

    // Now solve dataflow equations to calculate
    // set of operands that are live out of each Inst
    List<Set<IR.Reg>> liveIn = new ArrayList<Set<IR.Reg>>(func.code.length);
    List<Set<IR.Reg>> liveOut = new ArrayList<Set<IR.Reg>>(func.code.length);
    for (int i = 0; i < func.code.length; i++) {
      liveIn.add(i, new HashSet<IR.Reg>());
      liveOut.add(i,new HashSet<IR.Reg>());
    }
    
    boolean changed = true;
    int round = 0;
    while (changed) {
      changed = false;
      for (int i = func.code.length-1; i >= 0; i--) {
	Set<IR.Reg> newLiveOut = new HashSet<IR.Reg>();
	for (int j : succs.get(i)) 
	  newLiveOut.addAll(liveIn.get(j));
	if (!liveOut.get(i).equals(newLiveOut)) {
	  liveOut.set(i,newLiveOut);
	  changed = true;
	}
	Set<IR.Reg> newLiveIn = new HashSet<IR.Reg>();
	newLiveIn.addAll(liveOut.get(i));
	newLiveIn.removeAll(defined.get(i));
	newLiveIn.addAll(used.get(i));
	if (!liveIn.get(i).equals(newLiveIn)) {
	  liveIn.set(i,newLiveIn);
	  changed = true;
	}
      }
      // DEBUG
      // System.err.println("Round " + (++round));
      // System.err.println(func.name + " liveOut:");
      // for (int i = 0; i < liveOut.size(); i++) 
      //   System.err.println(i + "\t" + liveOut.get(i));
      // System.err.println(func.name + " liveIn:");
      // for (int i = 0; i < liveIn.size(); i++) 
      //   System.err.println(i + "\t" + liveIn.get(i));
    }

    System.out.println(func.name + " liveOut:");
    for (int i = 0; i < liveOut.size(); i++) 
      System.out.println(i + "\t" + liveOut.get(i));
    System.out.println(func.name + " liveIn:");
    for (int i = 0; i < liveIn.size(); i++) 
      System.out.println(i + "\t" + liveIn.get(i));
    return liveOut;
  }

  // calculate live ranges from liveOut sets
  static Map<IR.Reg,Set<Integer>> calculateLiveRanges(List<Set<IR.Reg>> liveOutSets) {
    Map<IR.Reg,Set<Integer>> liveRanges = new HashMap<IR.Reg,Set<Integer>>();
    for (int i = 0; i < liveOutSets.size(); i++) {
      for (IR.Reg t : liveOutSets.get(i)) {
	Set<Integer> s = liveRanges.get(t);
	if (s == null) {
	  s = new HashSet<Integer>();
	  s.add(i); 
	  liveRanges.put(t,s);
	} else
	  s.add(i);
      }
    }

    System.out.println("Live Ranges:");
    Set<Map.Entry<IR.Reg,Set<Integer>>> lis = liveRanges.entrySet();
    for (Map.Entry<IR.Reg,Set<Integer>> me : lis) {
      IR.Reg t = me.getKey();
      Set<Integer> s = me.getValue();
      System.out.println(t + "\t" + s);
    }
    return liveRanges;
  }
}


