package pos.project


class ModeemployeeComposer extends zk.grails.Composer {

    def afterCompose = { window ->
        //=============================== chec session =======================
        def checkgroup = Groupemployee.findByNamegroup(session.groupname)
        if(checkgroup != null){
            if(checkgroup.l3 == session.l3){
                window.visible = true
            }
            else{
                redirect(uri: "/subindex.zul")
            }
        }
        //=====================================================================

        $('#namegroup').focus()

    	int countlist = 1
    	for(Groupemployee g : Groupemployee.findAll()){
    		$('#listgroup > rows').append{
    			row(style:"font-size:14px;font-weight:bold;color:black;background:pink;"){
    				label(value:"${countlist}",style:"font-size:12px;font-weight:bold;color:black")
    				label(value:g.namegroup,style:"font-size:12px;font-weight:bold;color:black")
    				label(value:g.l1,style:"font-size:12px;font-weight:bold;color:black")
    				label(value:g.l2,style:"font-size:12px;font-weight:bold;color:black")
    				label(value:g.l3,style:"font-size:12px;font-weight:bold;color:black")
    				label(value:g.l4,style:"font-size:12px;font-weight:bold;color:black")
    				label(value:g.l5,style:"font-size:12px;font-weight:bold;color:black")
    				button(id:g.id,orient:"vertical",mold:"trendy",image:"/ext/images/icon/delete.png",style:"font-size:12px;font-weight:bold;color:black")
    			}
    		}
    		countlist++
    		$('#listgroup > rows > row > button').on('click',{
    			def groupdelete = Groupemployee.findById(it.target.id)
    			if(groupdelete != null){
					for(Employee moo : Employee.findAll()){
		
					if(moo.groupname == g.namegroup){
					moo.delete()
					}
				}
    				groupdelete.delete()
    				redirect(uri: "/modeemployee.zul")
    			}
    		})
    	}
        
        $('#save').on('click',{
        	if($('#namegroup').text() == ""){
        		alert("กรุณาระบุชื่อกลุ่มด้วยค่ะ")
        	}
        	else if(!$('#l1').checked() &&
        			!$('#l2').checked() &&
        			!$('#l3').checked() &&
        			!$('#l4').checked() &&
        			!$('#l5').checked() ){
        		alert("กรุณาระบุสิทธ์กลุ่มนี้อย่างน้อยหนึ่งอย่างค่ะ")
        	}
        	else{
        		String l1 = "null"
        		String l2 = "null"
        		String l3 = "null"
        		String l4 = "null"
        		String l5 = "null"
	        	if($('#l1').checked()){	l1 = $('#l1').text() }
	        	if($('#l2').checked()){	l2 = $('#l2').text() }
	        	if($('#l3').checked()){	l3 = $('#l3').text() }
	        	if($('#l4').checked()){	l4 = $('#l4').text() }
	        	if($('#l5').checked()){	l5 = $('#l5').text() }
	        	def groupsave = new Groupemployee()
	        	groupsave.namegroup = $('#namegroup').text()
	        	groupsave.l1 = l1
	        	groupsave.l2 = l2
	        	groupsave.l3 = l3
	        	groupsave.l4 = l4
	        	groupsave.l5 = l5
	        	groupsave.save()
	        	redirect(uri: "/modeemployee.zul")
        	}
        })
    }
}
