package com.compilers.hw3;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class PairTest {
  Node n = null;
  ArrayList<Node> neighbors = null;

  @Before
  public void initVars() {
    n = new Node("n");

    Node n1 = new Node("n1");
    Node n2 = new Node("n2");
    Node n3 = new Node("n3");
    neighbors = new ArrayList<Node>();
    neighbors.add(n1);
    neighbors.add(n2);
    neighbors.add(n3);
  }

  @Test
  public void testGetNode() {
    Pair p = new Pair(n, neighbors);
    Assert.assertEquals(n.getName(), p.getNode().getName());
  }

  @Test
  public void testGetNeighbors() {
    Pair p = new Pair(n, neighbors);
    Assert.assertTrue(p.getNeighbors().size() == neighbors.size());
  }
}