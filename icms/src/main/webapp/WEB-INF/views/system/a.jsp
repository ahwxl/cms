<script type="text/javascript">
	var tree, Virtual, root, node001, node001001, node0010010013, node0010010012, node0010010011;
	Ext.onReady(function() {
				Ext.SSL_SECURE_URL = '/e3/e3/commons/ext/resources/images/default/s.gif';
				Ext.BLANK_IMAGE_URL = '/e3/e3/commons/ext/resources/images/default/s.gif';
				var initConfig = {
					autoHeight : false,
					autoWidth : false,
					autoScroll : true,
					animate : false,
					enableDD : true,
					lines : true,
					rootVisible : true,
					title : '',
					border : false,
					containerScroll : true
				};
				if (typeof (treeConfigHandler) == 'function')
					treeConfigHandler(initConfig);
				tree = new Ext.tree.TreePanel(initConfig);
				Ext.override(
								Ext.tree.TreeNodeUI,
								{
									onClick : function(e) { //debugger;
										if (this.dropping) {
											e.stopEvent();
											return;
										}
										if (this.fireEvent("beforeclick",
												this.node, e) !== false) {
											var a = e.getTarget('a');
											if (!this.disabled
													&& this.node.attributes.href
													&& a) {
												this.fireEvent("click",
														this.node, e);
												return;
											} else if (a && e.ctrlKey) {
												e.stopEvent();
											}
											e.preventDefault();
											if (this.disabled) {
												return;
											}
											if (this.node.attributes.singleClickExpand
													&& !this.animating
													&& this.node
															.hasChildNodes()) {
												//this.node.expand();
												//this.node.toggle();
											}
											this.fireEvent("click", this.node,
													e);
										} else {
											e.stopEvent();
										}
									}
								});
				Ext.override(Ext.tree.TreeNodeUI, {
					onDblClick : function(e) { //debugger;
						e.preventDefault();
						if (this.node.attributes.disabled) {
							return;
						}
						if (this.checkbox) {
							this.toggleCheck();
						}
						if (this.animating && this.node.hasChildNodes()) {
							//this.node.toggle();
							//this.node.expand();
						}
						this.fireEvent("dblclick", this.node, e);
					}
				});
				Virtual = new Ext.tree.TreeNode({
					id : 'Virtual',
					text : '虚拟跟节点',
					href : "",
					hrefTarget : '',
					qtip : '',
					disabled : false,
					allowDrag : false,
					allowDrop : false,
					iconCls : 'E3-TREE-STYLE-PREFIX1'
				});
				root = Virtual;
				tree.setRootNode(root);
				var checkChildren = function(node) {
					if (node.isLeaf()) {//非叶子节点
						return;
					}
					var nodeUI = node.getUI();
					var children = node.childNodes;
					for ( var i = 0; i < children.length; i++) {
						var child = children[i];
						var childUI = child.getUI();
						if (typeof child.attributes.checked == 'undefined') {
							continue;
						}
						if (child.attributes.checked == node.attributes.checked) {
							continue;
						}
						if (child.attributes.disabled) {
							return;
						}
						if (node.attributes.checked) {
							child.getOwnerTree().fireEvent('onChecked', child);
						} else {
							child.getOwnerTree()
									.fireEvent('onUnchecked', child);
						}
						childUI.toggleCheck(node.attributes.checked);
						child.attributes.checked = node.attributes.checked;
						checkChildren(child);
					}
				};
				var checkParents = function(node) {
					if (node == null) {
						return;
					}
					var nodeUI = node.getUI();
					if (typeof node.attributes.checked == 'undefined') {
						return;
					}
					if (node.attributes.checked == false) {//取消父亲.
						uncheckParents(node);
						return;
					}
					var parentNode = node.parentNode;
					if (parentNode == null) {
						return;
					}
					var parentNodeUI = parentNode.getUI();
					if (typeof parentNode.attributes.checked == 'undefined') {
						return;
					}
					if (parentNode.attributes.checked) {//已经选种
						return;
					}
					//只要有一个没选种就返回,全选种就递归
					var children = parentNode.childNodes;
					for ( var i = 0; i < children.length; i++) {
						var child = children[i];
						if (typeof child.attributes.checked == 'undefined') {
							continue;
						}
						if (child.attributes.checked == false) {
							return;
						}
					}
					if (parentNode.attributes.disabled) {
						return;
					}
					parentNodeUI.toggleCheck(true);
					parentNode.getOwnerTree()
							.fireEvent('onChecked', parentNode);
					parentNode.attributes.checked = true;
					checkParents(parentNode);
				};
				var uncheckParents = function(node) {
					var parentNode = node.parentNode;
					if (parentNode == null) {
						return;
					}
					var parentNodeUI = parentNode.getUI();
					if (typeof parentNode.attributes.checked == 'undefined') {
						return;
					}
					if (parentNode.attributes.disabled) {
						return;
					}
					parentNodeUI.toggleCheck(false);
					parentNode.attributes.checked = false;
					parentNode.getOwnerTree().fireEvent('onUnchecked',
							parentNode);
					uncheckParents(parentNode);
				};
				// 双击节点，checkbox选中了，但是实际并没有触发Ext的checkchange事件，所以本来想checkchange时做点事情的话，这时就失败了。
				//这里需要特别处理下这个双击事件
				tree.on('dblclick', function(n, e) {
					var ckb = n.getUI().checkbox;
					if (typeof ckb == 'undefined') {
						return true;
					}
					if (n.attributes.disabled == 'undefined') {
						return true;
					}
					if (n.attributes.disabled == true) {
						return true;
					}
					n.fireEvent('checkchange', n, ckb.checked)
				}, tree);
				tree.on('checkchange', function(pNode, pChecked) {
					if (pChecked) {
						pNode.getOwnerTree().fireEvent('onChecked', pNode);
					} else {
						pNode.getOwnerTree().fireEvent('onUnchecked', pNode);
					}
					pNode.attributes.checked = pChecked;
					if (true) {
						checkChildren(pNode);
					}
					if (true) {
						checkParents(pNode);
					}
				});
				node001 = new Ext.tree.TreeNode({
					id : 'node001',
					text : '进创集团',
					href : "",
					hrefTarget : '',
					qtip : '',
					disabled : false,
					allowDrag : false,
					allowDrop : false,
					viewOrder : '1',
					iconCls : 'E3-TREE-STYLE-PREFIX1'
				});
				Virtual.appendChild(node001);
				node001001 = new Ext.tree.TreeNode({
					id : 'node001001',
					text : '进创软件',
					href : "",
					hrefTarget : '',
					qtip : '',
					disabled : false,
					allowDrag : false,
					allowDrop : false,
					viewOrder : '1',
					iconCls : 'E3-TREE-STYLE-PREFIX1'
				});
				node001.appendChild(node001001);
				node0010010013 = new Ext.tree.TreeNode({
					id : 'node0010010013',
					text : 'Z软件公司',
					href : "",
					hrefTarget : '',
					qtip : '',
					disabled : false,
					allowDrag : false,
					allowDrop : false,
					viewOrder : '3',
					iconCls : 'E3-TREE-STYLE-PREFIX1'
				});
				node001001.appendChild(node0010010013);
				node0010010012 = new Ext.tree.TreeNode({
					id : 'node0010010012',
					text : 'Y软件公司',
					href : "",
					hrefTarget : '',
					qtip : '',
					disabled : false,
					allowDrag : false,
					allowDrop : false,
					viewOrder : '2',
					iconCls : 'E3-TREE-STYLE-PREFIX1'
				});
				node001001.appendChild(node0010010012);
				node0010010011 = new Ext.tree.TreeNode({
					id : 'node0010010011',
					text : 'X软件公司',
					href : "",
					hrefTarget : '',
					qtip : '',
					disabled : false,
					allowDrag : false,
					allowDrop : false,
					viewOrder : '1',
					iconCls : 'E3-TREE-STYLE-PREFIX1'
				});
				node001001.appendChild(node0010010011);
				if (typeof (E3TreeExtReadyHandler) == 'function')
					E3TreeExtReadyHandler(tree, 'tree');
				if (typeof (treeRenderBeforeHandler) == 'function')
					treeRenderBeforeHandler(tree);
				tree.render('tree');
				root.expand();
				if (typeof (treeRenderAfterHandler) == 'function')
					treeRenderAfterHandler(tree);
			});
</script>
