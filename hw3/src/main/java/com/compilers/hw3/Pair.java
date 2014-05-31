package com.compilers.hw3;

import java.util.ArrayList;

public class Pair {
  Node n = null;
  ArrayList<Node> neighbors = null;

  Pair(Node n, ArrayList<Node> neighbors) {
    this.n = n; this.neighbors = neighbors;
  }

  public Node getNode() {
    return n;
  }

  public ArrayList<Node> getNeighbors() {
    return neighbors;
  }
}
