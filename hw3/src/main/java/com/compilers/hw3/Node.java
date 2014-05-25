package com.compilers.hw3;

import java.util.HashMap;

public class Node {

  private String name;
  private HashMap<String, Node> neighbors;

  Node(String name) {
    this.name = name;
    neighbors = new HashMap<String, Node>();
  }

  public String getName() {
    return name;
  }

  public int degree() {
    return neighbors.size();
  }

  public void connect(Node n) throws Exception {
    if (n.name.equals(this.name))
      throw new Exception("Cannot connect node to self");
    this.neighbors.put(n.name, n);
    n.neighbors.put(name, this);
  }

  public void disconnect(Node n) throws Exception {
    if (!n.connectedTo(this))
      throw new Exception("Attempted to disconnect non connected node");
    this.neighbors.remove(n.name);
  }

  public boolean connectedTo(Node n) {
    return this.neighbors.containsKey(n.name) && n.neighbors.containsKey(name);
  }
}
