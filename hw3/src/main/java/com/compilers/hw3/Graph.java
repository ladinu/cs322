package com.compilers.hw3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.compilers.hw3.X86;

public class Graph {
  private HashMap<String, Node> nodes;

  Graph() {
    nodes = new HashMap<String, Node>();
  }

  public boolean isEmpty() {
    return (nodes.size() == 0);
  }

  public Map<String, Node> getNodes() {
    return Collections.unmodifiableMap(nodes);
  }

  public void addNode(Node n) {
    nodes.put(n.getName(), n);
  }

  public void addNode(IR.Reg r) {
    Node n = new Node(r.toString());
    n.irReg = r;
    nodes.put(n.getName(), n);
  }

  public void addNode(String nodeName) {
    nodes.put(nodeName, new Node(nodeName));
  }

  public void removeNode(String n) throws Exception {
    if (!nodes.containsKey(n))
      throw new Exception("Cannot remove node not in graph");
    Node.removeAllConnections(nodes.get(n));
    nodes.remove(n);
  }

  public void addEdge(String n1, String n2) throws Exception{
    if (!(nodes.containsKey(n1) && nodes.containsKey(n2))) {
      String err = String.format("Node '%s' and or node '%s' not in graph", n1, n2);
      throw new Exception(err);
    }
    nodes.get(n1).connect(nodes.get(n2));
  }

  public boolean hasEdge(String n1, String n2) {
    if (!(nodes.containsKey(n1) && nodes.containsKey(n2)))
      return false;
    return nodes.get(n1).connectedTo(nodes.get(n2));
  }

  public Node minDegreeNode() {
    Node minNode = null;
    int degree = nodes.size();
    for(Node n : nodes.values()) {
      if (n.degree() < degree) {
        minNode = n;
        degree = n.degree();
      }
    }
    return minNode;
  }

  public String toDot() {
    String dot = "strict graph G {\n";
    for (Node n : nodes.values()) {
      String label = (n.x86Reg == null) ? "none" : n.x86Reg.toString();
      dot += String.format(
          "\t%s [xlabel=\"%s\" style=\"filled\" fillcolor=\"%s\"];\n",
          n.getName(), label, n.color);
    }
    for (Node n : nodes.values()) {
      dot += n.toDot();
    }
    dot += "}\n";
    return dot;
  }

  public static String mapRegtoColor(X86.Reg reg) {
    String r = reg.toString();
      if (r.equals("%rsi"))
        return "#a6cee3";
      if (r.equals("%rax"))
        return "#1f78b4";
      if (r.equals("%rbp"))
        return "#b2df8a";
      if (r.equals("%rbx"))
        return "#33a02c";
      if (r.equals("%rcx"))
        return "#fb9a99";
      if (r.equals("%rdi"))
        return "#e31a1c";
      if (r.equals("%rdx"))
        return "#fdbf6f";
      if (r.equals("%rsp"))
        return "#ff7f00";

     if(r.equals("%r8") )
        return "#cab2d6";
     if(r.equals("%r9" ))
        return "#6a3d9a";
     if(r.equals("%r10"))
        return "#ffff99";
     if(r.equals("%r11"))
        return "#b15928";
     if(r.equals("%r12"))
        return "white";
     if(r.equals("%r13"))
        return "black";
     if(r.equals("%r14"))
        return "#8c510a";
     if(r.equals("%r15"))
        return "#bababa";

    return "black";
  }
}
