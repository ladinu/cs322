package com.compilers.hw3;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
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

  @Test
  public void testAddEdgeToSelf() throws Exception {
    g.addNode("A");
    g.addEdge("A", "A");
    Assert.assertEquals(0, g.getNodes().get("A").getNeighbors().size());
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

  @Test
  public void testHasEdge() throws Exception {
    g.addNode("a");
    g.addNode("b");

    Assert.assertFalse(g.hasEdge("a", "b"));
    g.addEdge("a", "b");
    Assert.assertTrue(g.hasEdge("a", "b"));
  }

  @Test (expected = Exception.class)
  public void testRemoveNodeException() throws Exception {
    g.removeNode("A");
  }

  @Test
  public void testRemoveNode() throws Exception {
    g.addNode("A");
    Assert.assertEquals(1, g.getNodes().size());
    g.removeNode("A");
    Assert.assertEquals(0, g.getNodes().size());
  }

  @Test
  public void testRemoveNodeWithConnection() throws Exception {
    g.addNode("n1");
    g.addNode("n2");
    g.addNode("n3");
    g.addNode("n4");

    g.addEdge("n1", "n2");
    g.addEdge("n1", "n3");
    g.addEdge("n1", "n4");

    Assert.assertEquals(4, g.getNodes().size());
    g.removeNode("n1");
    Assert.assertEquals(3, g.getNodes().size());

    Assert.assertFalse(g.hasEdge("n2", "n1"));
    Assert.assertFalse(g.hasEdge("n3", "n2"));
    Assert.assertFalse(g.hasEdge("n4", "n3"));

    Assert.assertFalse(g.hasEdge("n1", "n2"));
    Assert.assertFalse(g.hasEdge("n1", "n3"));
    Assert.assertFalse(g.hasEdge("n1", "n4"));
  }

  @Test
  public void testMinDegreeNodeEmptyGraph() throws Exception {
    Assert.assertNull(g.minDegreeNode());
  }

  @Test
  public void testMinDegreeNode() throws Exception {
    g.addNode("A");
    g.addNode("B");
    g.addNode("C");
    g.addNode("D");

    g.addEdge("A", "B");
    g.addEdge("A", "C");
    g.addEdge("A", "D");
    g.addEdge("B", "C");
    Assert.assertTrue(g.minDegreeNode().getName().equals("D"));
  }

  @Test
  public void testIsEmpty() {
    Assert.assertTrue(g.isEmpty());
    g.addNode("A");
    Assert.assertFalse(g.isEmpty());
  }
}