package com.compilers.hw3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Graph {
  private HashMap<String, Node> nodes;

  Graph() {
    nodes = new HashMap<String, Node>();
  }

  public Map<String, Node> getNodes() {
    return Collections.unmodifiableMap(nodes);
  }

  public void addNode(String nodeName) {
    nodes.put(nodeName, new Node(nodeName));
  }

  public void removeNode(String nodeName) {

  }

  public void addEdge(String n1, String n2) throws Exception{
    if (!(nodes.containsKey(n1) && nodes.containsKey(n2))) {
      String err = String.format("Node '%s' and or node '%s' not in graph", n1, n2);
      throw new Exception(err);
    }
    nodes.get(n1).connect(nodes.get(n2));
  }
}
