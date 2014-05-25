package com.compilers.hw3;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

public class NodeTest {

  @Test
  public void testGetName() {
    Node n = new Node("A");
    Assert.assertEquals("A", n.getName());
  }

  @Test
  public void testConnectSelf() throws Exception {
    Node h = new Node("Head");
    Assert.assertFalse(h.connectedTo(h));
    h.connect(h);
    Assert.assertFalse(h.connectedTo(h));
  }

  @Test
  public void testConnect() throws Exception {
    Node h = new Node("Head");
    Node t = new Node("Tail");
    h.connect(t);

    Assert.assertTrue(h.connectedTo(t));
    Assert.assertTrue(t.connectedTo(h));
  }

  @Test
  public void testConnectedTo() throws Exception {
    Node h = new Node("Head");
    Node t = new Node("Tail");
    Node x = new Node("X");

    h.connect(t);

    Assert.assertFalse(t.connectedTo(x));
    Assert.assertFalse(h.connectedTo(x));

    h.connect(x);

    Assert.assertTrue(h.connectedTo(x));
    Assert.assertTrue(x.connectedTo(h));

    Assert.assertFalse(t.connectedTo(x));
    Assert.assertFalse(x.connectedTo(t));

    t.connect(x);

    Assert.assertTrue(t.connectedTo(x));
    Assert.assertTrue(x.connectedTo(t));
  }


  @Test(expected = Exception.class)
  public void testDisconnectNotConnectedNode() throws Exception {
    Node n1 = new Node("n1");
    Node n2 = new Node("n2");

    Assert.assertFalse(n1.connectedTo(n2));
    n2.disconnect(n1);
  }

  @Test
  public void testDisconnect() throws Exception {
    Node n1 = new Node("n1");
    Node n2 = new Node("n2");

    n1.connect(n2);

    Assert.assertTrue(n1.connectedTo(n2));
    n2.disconnect(n1);
    Assert.assertFalse(n1.connectedTo(n2));
    Assert.assertFalse(n2.connectedTo(n1));
  }


  @Test
  public void testDegree() throws Exception {
    Node n1 = new Node("n1");
    Node n2 = new Node("n2");
    Node n3 = new Node("n3");
    Node n4 = new Node("n4");

    Assert.assertEquals(0, n1.degree());

    n1.connect(n2);
    n1.connect(n3);
    n1.connect(n4);

    Assert.assertEquals(3, n1.degree());

    Assert.assertEquals(1, n2.degree());
    Assert.assertEquals(1, n3.degree());
    Assert.assertEquals(1, n4.degree());
  }

  @Test
  public void testGetNeighbors() throws Exception {
    Node n1 = new Node("n1");
    Node n2 = new Node("n2");
    Node n3 = new Node("n3");
    Node n4 = new Node("n4");

    ArrayList<Node> nodes = new ArrayList<Node>();

    nodes.add(n2);
    nodes.add(n3);
    nodes.add(n4);

    n1.connect(n2);
    n1.connect(n3);
    n1.connect(n4);

    Assert.assertTrue(n1.getNeighbors().values().containsAll(nodes));

    nodes.add(n1);
    Assert.assertFalse(n1.getNeighbors().values().containsAll(nodes));
  }

  @Test (expected = UnsupportedOperationException.class)
  public void testGetNeighborsImmutability() throws Exception {
    Node n = new Node("n");
    n.connect(new Node("b"));
    n.getNeighbors().put("b", new Node("AA"));
  }

  @Test
  public void testRemoveConnections() throws Exception {
    Node n1 = new Node("n1");
    Node n2 = new Node("n2");
    Node n3 = new Node("n3");

    n1.connect(n2);
    n1.connect(n3);

    Assert.assertTrue(n1.connectedTo(n2));
    Assert.assertTrue(n1.connectedTo(n3));
    Node.removeAllConnections(n1);
    Assert.assertFalse(n1.connectedTo(n2));
    Assert.assertFalse(n1.connectedTo(n3));
  }
}