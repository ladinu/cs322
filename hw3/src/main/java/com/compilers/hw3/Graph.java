package com.compilers.hw3;

import java.util.HashMap;

public class Graph {
  private HashMap<String, Node> nodes;

  Graph() {
    nodes = new HashMap<String, Node>();
  }

  public HashMap<String, Node> getNodes() {
    return nodes;
  }

  public void addNode(String nodeName) {
    nodes.put(nodeName, new Node(nodeName));
  }

  public void addEdge(String n1, String n2) throws Exception{
    if (n1.equals(n2)) {
      throw new Exception("Cannot have an edge to self");
    } else if (!(nodes.containsKey(n1) && nodes.containsKey(n2))) {
      String err = String.format("Node '%s' and or node '%s' not in graph", n1, n2);
      throw new Exception(err);
    }

    nodes.get(n1).connect(nodes.get(n2));
  }
}
