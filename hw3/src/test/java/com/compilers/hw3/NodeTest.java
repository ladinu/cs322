package com.compilers.hw3;

import org.junit.Assert;
import org.junit.Test;

public class NodeTest {

  @Test
  public void testGetName() {
    Node n = new Node("A");
    Assert.assertEquals("A", n.getName());
  }

  @Test
  public void testConnect() {
    Node h = new Node("Head");
    Node t = new Node("Tail");
    h.connect(t);

    Assert.assertTrue(h.connectedTo(t));
    Assert.assertTrue(t.connectedTo(h));
  }

  @Test
  public void testConnectedTo() {
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

    Assert.assertFalse(h.connectedTo(h));
    h.connect(h);
    Assert.assertTrue(h.connectedTo(h));
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
  }

  @Test
  public void testDegree() {
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
}