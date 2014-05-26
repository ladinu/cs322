package com.compilers.hw3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    String dot = "graph G {\n";
    for (Node n : nodes.values()) {
      dot += n.toDot();
    }
    dot += "}\n";
    return dot;
  }
}
