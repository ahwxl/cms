package com.bplow.look.bass;

import java.io.Serializable;

/**
 * 
 * @author www
 *
 */

public class TreeNode implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String text;
	private String leaf;
	private String cls;
	private String node;
	
	
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getLeaf() {
		return leaf;
	}
	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}
	
	

}
