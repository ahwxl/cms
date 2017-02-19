 /*
  ************************************************************************************
  * Author: Doug Hendricks. doug[at]theactivegroup.com
  * Copyright 2007-2009, Active Group, Inc.  All rights reserved.
  ************************************************************************************

  License: This demonstration is licensed under the terms of
  GNU Open Source GPL 3.0 license:

  Commercial use is prohibited without contacting licensing[at]theactivegroup.com.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   see < http://www.gnu.org/licenses/gpl.html >

   Donations are welcomed: http://donate.theactivegroup.com

  */
  
  
  $JIT('@mif');
  
  $JIT.onAvailable('mif', function(loaded){
    
   var //sourceWin, 
       mifWin,
       F = Ext.util.Format;
       
   loaded && (
     mifWin = new Ext.ux.ManagedIFrame.Window({

      title         : 'Simple MIF.Window',
      width         : 845,
      height        : 469,
      maximizable   : true,
      collapsible   : true,
      id            : 'mifsimple',
      constrain     : true,
      closeAction   : 'hide',
      loadMask      : {msg: 'Loading...'},
      autoScroll    : true,
      defaultSrc    : 'http://www.extjs.com',
      plugins       : {ptype: 'toolstips'},
      listeners : {
         domready : function(frameEl){  //raised for "same-origin" frames only
            var MIF = frameEl.ownerCt;
            Demo.balloon(null, MIF.title+' reports:','domready ');
         },
         documentloaded : function(frameEl){
            var MIF = frameEl.ownerCt;
            Demo.balloon(null, MIF.title+' reports:','docloaded ');
         },
         beforedestroy : function(){
            if(sourceWin){
                sourceWin.close();
                sourceWin = null;
            }
         }
       },
       
       tbar : [
           {
             text    :'View Source',
             iconCls :'source-icon',
             tooltip : 'View the Demo Source Code..',
             handler : function(button){
                
               var sourceWin, sourceType = 'javascript';
                 
                   $JIT({method:'GET', //load the script source again
                     forced: true, 
                     noExecute : true,
                     cacheResponses : true
                     },
                     mifWin.sourceModule,
	                
                     function(ok, T, loadedModules){
                        ( sourceWin ||
		               (sourceWin = new Ext.ux.ManagedIFrame.Window(
		                {
		                  title       : 'Demo Source',
		                  iconCls     : 'source-icon',
	                      width       : 600,
	                      height      : 600,
		                  autoScroll  : true,
	                      constrain   : true,
	                      closeAction : 'hide',
		                  closable    : true,
                          manager     : mifWin.manager,
	                      plugins     : {ptype: 'toolstips'}, 
					      tbar : [
					           {
					             text    :'Print',
					             iconCls :'demo-print',
	                             tooltip : 'Print the source file.',
					             handler : function(button){
	                                sourceWin.getFrame().print();
	                              }
	                            },
	                             {
	                             text    :'Save',
	                             id      :'save-but',
	                             iconCls :'demo-save',
	                             tooltip : {text:'Save the source file. (IE only)', title:'execCommand'},
	                             disabled : true,
	                             handler : function(button){
	                                sourceWin.getFrame().execCommand('SaveAs',true);
	                              }
	                            }
	                            
	                         ],
	                             
	                      /**
	                       * Write the source of this demo module directly to the frame
	                       * using a Ext.DomHelper config.
	                       */
	                      html    : {tag : 'pre', 
	                                 cls : sourceType + ' codelife', 
	                                 cn : [
	                                    {tag:'code', 
	                                    html: Demo.codeLife.highlightFragment(
	                                        sourceType, 
	                                        $JIT.getModule(mifWin.sourceModule).content.text
	                                       )
	                                    }
	                                   ]
	                                },
	                      listeners : {
	                        
	                        //Bring the MIFWindow to the front if the nested document receives focus
	                        focus : function(frameEl){
	                            this.toFront();
	                        },
	                        
	                        //Using $JIT, sprinkle some syntax-highlighting CSS when the frame's dom is ready
	                        domready : function(frameEl){
	                           
	                            var module = String.format('codelife-{0}.css',sourceType);
	                            
	                            $JIT.css('ux/' + module, function(ok){
	                                //inject CSS directly into the iframe
	                                ok && $JIT.applyStyle(module, null, frameEl.getWindow());                                
	                            });
	                            
	                            //See if the Browser supports the SaveAs command
                                try{
		                            Ext.getCmp('save-but').setDisabled(
		                               !this.getFrameDocument().queryCommandEnabled('SaveAs')
		                            );
                                }catch(e){}
	                        }
	                      }
	                   
		              }))).show(button.btnEl);
	                
	            });
             }
          },
          {
             text    :'Reload',
             iconCls :'demo-action',
             tooltip : 'Reload the Frame',
             handler : function(button){ mifWin.setSrc(); }
          }],
          
       sourceModule :'mifsimple' /*Demo.resolveURL('mifsimple.js','')*/
       
   }));
   
   Demo.register(mifWin);
  
  });
  
  $JIT.provide('mifsimple');  //logical module registration
  

