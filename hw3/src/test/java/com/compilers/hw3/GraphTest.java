package com.compilers.hw3;

import org.junit.Assert;
import org.junit.Test;

public class GraphTest {

  @Test
  public void testGetNodes() throws Exception {
    Graph g = new Graph();
    Assert.assertTrue("Nodes are empty at begeining", g.getNodes().isEmpty());
  }

  @Test
  public void testAddNode() throws Exception {
    Graph g = new Graph();
    g.addNode("A");
    Assert.assertEquals("Should add nodes to grapg", g.getNodes().size(), 1);
  }
}