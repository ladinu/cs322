package com.compilers.hw3;

import java.util.ArrayList;

public class Graph {
  private ArrayList<Node> nodes;

  Graph() {
    nodes = new ArrayList<Node>();
  }

  public ArrayList<Node> getNodes() {
    return nodes;
  }

  public void addNode(String nodeName) {
    nodes.add(new Node(nodeName));
  }
}
