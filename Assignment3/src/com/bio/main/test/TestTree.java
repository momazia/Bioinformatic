package com.bio.main.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.bio.main.tree.Node;
import com.bio.main.tree.Tree;

public class TestTree {
	@Test
	public void testConstruct() {
		Tree tree = new Tree();
		tree.construct(3);
		assertEquals(3, tree.getHeight());
	}

	@Test
	public void testFindNodes() {
		Tree tree = new Tree();
		tree.construct(3);
		Node node = tree.findNode("AG");
		assertEquals("AG", node.getData().getStr());
		node = tree.findNode("TTT");
		assertEquals("TTT", node.getData().getStr());
	}

	@Test
	public void testNextNode() {
		Tree tree = new Tree();
		tree.construct(3);
		tree.print();
		assertEquals("AAC", tree.nextNode("AAA").getData().getStr());
		assertEquals("AC", tree.nextNode("AAT").getData().getStr());
		assertEquals("TTT", tree.nextNode("TT").getData().getStr());
	}

}
