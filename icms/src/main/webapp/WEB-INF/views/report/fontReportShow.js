
Ext.onReady(function(){
	
	mergTab();
	
	//合并内容相同单元格
	function mergTab() {
		var tab = document.getElementById("headTab"); //要合并的tableID
		var maxRow = tab.rows.length, val, count, start;
		if(tab != null) {
			for(var row = maxRow-1;row>=0;row--) {
				count = 1;
				val = "";
				for(var i = 0; i < tab.rows[row].cells.length; i++) {
					if(val == tab.rows[row].cells[i].innerHTML) {
						count++;
					} else {
						if(count>1) {
							start = i - count;
							tab.rows[row].cells[start].colSpan = count;
							for(var j = start + 1; j < i; j++) {
								tab.rows[row].cells[j].style.display = "none";
							}
							count = 1;
						}
						val = tab.rows[row].cells[i].innerHTML;
					}
				}
				if(count>1) {
					start = i - count;
	                tab.rows[row].cells[start].colSpan = count;
	                for (var j = start + 1; j < i; j++) {
	                    tab.rows[row].cells[j].style.display = "none";
	                }
				}
			}
		}
	}

	
});