package com.compilers.hw3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

  public boolean connectedTo(Node n) {
    return this.neighbors.containsKey(n.name) && n.neighbors.containsKey(name);
  }
}
