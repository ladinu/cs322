package com.compilers.hw3;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Node {

  private String name;
  private HashMap<String, Node> neighbors;

  public X86.Reg x86Reg = null;
  public IR.Reg irReg = null;
  public String color = "white";

  Node(String name) {
    this.name = name;
    neighbors = new HashMap<String, Node>();
  }

  public Node copy() {
    Node n = new Node(name);
    n.x86Reg = x86Reg;
    n.irReg = irReg;
    n.color = color;
    n.neighbors = (HashMap<String, Node>)neighbors.clone();
    return n;
  }

  public boolean hasReg() {
    return x86Reg != null;
  }
  public String getName() {
    return name;
  }

  public Map<String, Node> getNeighbors() {
    return Collections.unmodifiableMap(neighbors);
  }

  public int degree() {
    return neighbors.size();
  }

  // Connect this node an some node n with an undirected edge
  public void connect(Node n) throws Exception {
    if (n.name.equals(this.name))
      return;
    this.neighbors.put(n.name, n);
    n.neighbors.put(name, this);
  }

  // Disconnect this node with some node n by removing the node
  // and all of its edges
  public void disconnect(Node n) throws Exception {
    if (!n.connectedTo(this))
      throw new Exception("Attempted to disconnect non connected node");
    this.neighbors.remove(n.name);
    n.neighbors.remove(this.name);
  }

  public static void removeAllConnections(Node n) throws Exception {
    for (Node neighbor: new HashSet<Node>(n.neighbors.values())) {
      neighbor.disconnect(n);
    }
  }

  public boolean connectedTo(Node n) {
    return this.neighbors.containsKey(n.name) && n.neighbors.containsKey(name);
  }

  public String toDot() {
    if (neighbors.isEmpty())
      return "\t" + name + ";\n";
    String dot = "";
    for (Node n : neighbors.values()) {
      dot += String.format("\t%s -- %s;\n", name, n.name);
    }
    return dot;
  }
}
