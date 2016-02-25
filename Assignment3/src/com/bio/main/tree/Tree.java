package com.bio.main.tree;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.bio.main.pojo.TCGA;

public class Tree {

	private Node root;
	private int height;

	public Tree() {
		root = new Node();
		root.setData(new NodeData());
	}

	public Node getRoot() {
		return this.root;
	}

	public void construct(int height) {
		this.height = height;
		createNode(root, "", height);
	}

	private void createNode(Node node, String prefix, int height) {

		if (height == 0) {
			return;
		}

		for (TCGA tcga : TCGA.values()) {
			node.addChild(prefix + tcga);
		}

		for (Node child : node.getChildren()) {
			createNode(child, child.getData().getStr(), height - 1);
		}

	}

	public Node findNode(String str) {
		Node result = new Node();
		findNode(result, root, str);
		return result;
	}

	public void findNode(Node result, Node node, String str) {

		if (StringUtils.equals(node.getData().getStr(), str)) {
			result.setData(node.getData());
			result.setParent(node.getParent());
			result.setChildren(node.getChildren());
			return;
		}

		if (node.getChildren().size() > 0) {
			for (Node child : node.getChildren()) {
				findNode(result, child, str);
			}
		}
	}

	public Node nextNode(String str) {
		return nextNode(findNode(str));
	}

	private Node nextNode(Node node) {

		if (!node.isLeaf()) {
			return node.getFirstChild();
		}
		
		Iterator<Node> sibilingsIterator = node.getParent().getChildren().iterator();
		while (sibilingsIterator.hasNext()) {
			Node nextChild = sibilingsIterator.next();
			if (StringUtils.equals(nextChild.getData().getStr(), node.getData().getStr()) && sibilingsIterator.hasNext()) {
				return sibilingsIterator.next();
			} else if (StringUtils.equals(nextChild.getData().getStr(), node.getData().getStr()) && !sibilingsIterator.hasNext()) {
				return node.getNextParent();
			}
		}
		return null;
	}

	public void print() {
		print("", root, this.height + 1);
	}

	private void print(String prefix, Node node, int height) {

		if (height == 0) {
			return;
		}

		System.out.println(prefix + node.getData().getStr());
		for (Node child : node.getChildren()) {
			print(prefix + "|" + new String(new char[this.height - height + 1]).replace("\0", "-"), child, height - 1);
		}

	}

	public int getHeight() {
		return this.height;
	}

}
