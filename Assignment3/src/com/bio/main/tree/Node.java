package com.bio.main.tree;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

public class Node implements Comparable<Node> {

	private Node parent;
	private NodeData data;
	private Set<Node> children = new TreeSet<>();

	public boolean isLeaf() {
		return children.size() == 0;
	}

	public Node getParent() {
		return parent;
	}

	public Node getNextParent() {

		Node anccestor = parent.getParent();
		if (anccestor == null) {
			return null;
		}

		Iterator<Node> iterator = anccestor.getChildren().iterator();

		while (iterator.hasNext()) {
			Node nextChild = iterator.next();
			if (StringUtils.equals(nextChild.getData().getStr(), this.getData().getStr()) && iterator.hasNext()) {
				return iterator.next();
			}
		}
		return null;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public NodeData getData() {
		return data;
	}

	public void setData(NodeData data) {
		this.data = data;
	}

	public Set<Node> getChildren() {
		return children;
	}

	public void setChildren(Set<Node> children) {
		this.children = children;
	}

	public void addChild(String str) {
		Node child = new Node();
		child.setData(new NodeData(str));
		child.parent = this;
		this.children.add(child);
	}

	@Override
	public int compareTo(Node arg0) {
		return this.getData().getStr().compareTo(arg0.getData().getStr());
	}

	public Node getFirstChild() {
		return getChildren().iterator().next();
	}
}
