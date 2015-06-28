<%@ page language="java" pageEncoding="utf-8" %>
<div id='tree-div' style="width:100%"></div>
<script type="text/javascript">
/**
 * 演示分页
 */
// create the Data Store
    var store = new Ext.data.JsonStore({
        root: 'topics',
        totalProperty: 'totalCount',
        //idProperty: 'threadid',
        remoteSort: true,

        fields: [
            'cnt_id', 'cnt_caption', 'catalog_id', 'operate_date'
            //{name: 'replycount', type: 'int'},
            //{name: 'lastpost', mapping: 'lastpost', type: 'date', dateFormat: 'n/j h:ia'},// 
            //'lastposter', 'excerpt'
        ],

        // load using script tags for cross domain, if the data in on the same domain as
        // this page, an HttpProxy would be better
        //proxy: new Ext.data.ScriptTagProxy({
            url: 'getProcessItemJson'
        //})
    });
    //store.setDefaultSort('lastpost', 'desc');


    // pluggable renders
    function renderTopic(value, p, record){
        return String.format(
                '我是一只小小鸟{0}',
                value);
    }
    function renderLast(value, p, r){
    	//value.dateFormat('M j, Y, g:i a')
        return String.format('{0}<br/>by {1}',value , r.data['cnt_id']);
    }
    function renderOpt(value, p, r){
    	return String.format('<u  onclick=\"doShowProcessInfo(\'{0}\')\" >查看</u> 删除 <u onclick=\"doPulicCnt(\'{0}\')\">暂停</u>',r.data['cnt_id']);
    }
    
    function doShowProcessInfo(processId){
    	
    	var url = 'viewProcessInfo?processId='+processId;

    	/**
    	*打开一个iframe窗口
    	*/
    	Ext.mainScreem.addNewTab(Ext.getmifObj({id:'liucheng1',title:'流程信息',src:url}),'liucheng1');
    }
    
    
    function doPulicCnt(id){
    	//alert(id);
    	/**/
    	Ext.Ajax.request({
    		   url: 'doPublicCnt?id='+id,
    		   method:'get',
    		   success: function(response, opts) {
    			      //var obj = Ext.decode(response.responseText);
    			      alert(response.responseText);
    		   },
    		   failure: function(response, opts) {
    			      alert('失败');
    		   },
    		   params: { id: id }
    		});
    	
    	
    }
    
    var topbar = new Ext.Toolbar({
        //width: 600,
        //height: 100,
        items: [
            {
                // xtype: 'button', // default for Toolbars, same as 'tbbutton'
                text: '加载',
                handler: function (){
                	Ext.Ajax.request({
                		   url: 'loadProceeDefined?processKey=myholidayProcess',
                		   method :'GET',
                		   success: function(response, opts) {
                		      var obj = Ext.decode(response.responseText);
                		      
                		   },
                		   failure: function(response, opts) {
                		      //console.log('server-side failure with status code ' + response.status); processKey
                		      alert(response.status);
                		   }
                	});
                }
            },'-',
            {
                // xtype: 'button', // default for Toolbars, same as 'tbbutton'
                text: '创建',
                handler: function (){
                	Ext.Ajax.request({
             		   url: 'createProcessInstance?processKey=myholidayProcess',
             			method :'GET',
             		   success: function(response, opts) {
             		      var obj = Ext.decode(response.responseText);
             		      
             		   },
             		   failure: function(response, opts) {
             		      //console.log('server-side failure with status code ' + response.status);
             		      alert(response.status);
             		   }
             	});
                }
            },'-',
            {
                // xtype: 'button', // default for Toolbars, same as 'tbbutton'
                text: '我的代办',
                handler: function (){
                	
                }
            },'-',
            // begin using the right-justified button container
            '->', // same as {xtype: 'tbfill'}, // Ext.Toolbar.Fill
            // add a vertical separator bar between toolbar items
            '-'
        ]
    });
    
    

    var mygrid = new Ext.grid.GridPanel({
        //width:700,
        //height:500,
        autoHeight: true,
        //title:'分页演示',
        store: store,
        renderTo: 'tree-div',
        closable:true,
        trackMouseOver:true,
        disableSelection:true,
        //loadMask: true,

        // grid columns
        columns:[{
            //id: 'topic', // id assigned so we can apply custom css (e.g. .x-grid-col-topic b { color:#333 })
            header: "文章编号",
            dataIndex: 'cnt_id',
            width: 150,
            //renderer: renderTopic,
            sortable: true
        },{
            header: "标题",
            dataIndex: 'cnt_caption',
            width: 100,
            //hidden: true,
            sortable: true
        },{
            header: "目录编码",
            dataIndex: 'catalog_id',
            width: 70,
            align: 'right',
            sortable: true
        },{
            id: '发布日期',
            header: "发布日期",
            dataIndex: 'operate_date',
            width: 150,
            //renderer: renderLast,
            sortable: true
        },{            
            header: "操作",
            //dataIndex: 'operate_date',
            width: 150,
            renderer: renderOpt,
            sortable: true
        }],

        // customize view config
        viewConfig: {
            forceFit:true,
            enableRowBody:false,
            showPreview:false,
            getRowClass : function(record, rowIndex, p, store){
                if(this.showPreview){
                    p.body = '<p>'+record.data.cnt_id+'</p>';
                    return 'x-grid3-row-expanded';
                }
                return 'x-grid3-row-collapsed';
            }
        },
        
        tbar: topbar,

        // paging bar on the bottom
        bbar: new Ext.PagingToolbar({
            pageSize: 10,
            store: store,
            displayInfo: true,
            displayMsg: 'Displaying topics {0} - {1} of {2}',
            emptyMsg: "No topics to display",
            items:[
                '-', {
                pressed: true,
                enableToggle:true,
                text: 'Show Preview',
                cls: 'x-btn-text-icon details',
                toggleHandler: function(btn, pressed){
                    var view = grid.getView();
                    view.showPreview = pressed;
                    view.refresh();
                }
            }]
        })
    });

    // render it
    //Ext.mainScreem.addNewTab(mygrid,  '10003');
    // trigger the data store load
    store.load({params:{start:0, limit:10}});
    
    
    //show process info
    function showProcessInfo(processId){
    	
    	
    	
    	
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
</script>