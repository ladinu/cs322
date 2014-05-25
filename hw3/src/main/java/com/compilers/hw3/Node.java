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

  public void connect(Node n) {
    neighbors.put(n.name, n);
    n.neighbors.put(name, this);
  }

  public boolean isConnectedTo(Node n) {
    return neighbors.containsKey(n.name) && n.neighbors.containsKey(name);
  }
}
