package com.bplow.look.bass.ui.tree;

public class ZtreeNode {
	
	private  String  id;
	private  String  name;
	private  boolean isParent;
	private  boolean open;//是否打开
	private  String  url;
	
	public ZtreeNode() {
		super();
	}
	

	public ZtreeNode(String id, String name, boolean isParent) {
		super();
		this.id = id;
		this.name = name;
		this.isParent = isParent;
	}


	public boolean isOpen() {
		return open;
	}


	public void setOpen(boolean open) {
		this.open = open;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsParent() {
		return isParent;
	}


	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
	
	

}
