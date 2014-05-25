package com.compilers.hw3;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class GraphTest {
  Graph g;

  @Before
  public void clearGraph() {
    g = new Graph();
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testGetNodesImmutability() {
    g.addNode("A");
    g.getNodes().put("A", new Node("B"));
  }

  @Test
  public void testGetNodes() throws Exception {
    Assert.assertTrue("Nodes are empty initially", g.getNodes().isEmpty());
  }

  @Test
  public void testAddNode() throws Exception {
    g.addNode("A");
    Assert.assertEquals("Should add nodes to graph", g.getNodes().size(), 1);
  }

  @Test(expected = Exception.class)
  public void testAddEdgeException() throws Exception {
    g.addEdge("A", "B");
  }

  @Test(expected = Exception.class)
  public void testAddEdgeException1() throws Exception {
    g.addNode("A");
    g.addEdge("A", "B");
  }

  @Test(expected = Exception.class)
  public void testAddEdgeException2() throws Exception {
    g.addNode("A");
    g.addEdge("A", "A");
  }


  @Test
  public void testAddEdge() throws Exception {
    g.addNode("A");
    g.addNode("B");

    g.addEdge("A", "B");

    Map<String, Node> nmap = g.getNodes();

    Assert.assertTrue(nmap.get("A").connectedTo(nmap.get("B")));
    Assert.assertTrue(nmap.get("B").connectedTo(nmap.get("A")));
  }
}