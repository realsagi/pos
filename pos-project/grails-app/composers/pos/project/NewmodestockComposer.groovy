package pos.project


class NewmodestockComposer extends zk.grails.Composer {

    def afterCompose = { window ->
        //=============================== chec session =======================
        def checkgroup = Groupemployee.findByNamegroup(session.groupname)
        if(checkgroup != null){
            if(checkgroup.l2 == session.l2){
                window.visible = true
            }
            else{
                redirect(uri: "/subindex.zul")
            }
        }
        //=====================================================================

        $('#namemodestock').focus()

    	int countlist = 1
    	for(Modestock m : Modestock.findAll()){
    		$('#listnamemode > rows').append{
    			row(style:"font-size:14px;font-weight:bold;color:black;background:pink;"){
    				label(value:"${countlist}",style:"font-size:14px;font-weight:bold;color:black")
    				label(value:m.namemode,style:"font-size:14px;font-weight:bold;color:black")
    				button(id:m.namemode,style:"font-size:14px;font-weight:bold;color:black",image:"/ext/images/icon/delete.png",mold:"trendy")
    			}
    		}
            countlist++
	    	$('#listnamemode > rows > row > button').on('click',{
	        	def delete = Modestock.findByNamemode(it.target.id)
	        	delete.delete()
	        	redirect(uri: "/newmodestock.zul")
	        })
    	}
        
        $('#clear').on('click',{
        	$('#namemodestock').val("")
        })

        $('#submit').on('click',{
        	if($('#namemodestock').text() == ""){
        		alert("กรุณากรอกชื่อหมวดสินค้าด้วยค่ะ")
        	}
        	else{
        		Modestock savemode = new Modestock()
	        	savemode.namemode = $('#namemodestock').text()
	        	savemode.save()
	        	redirect(uri: "/newmodestock.zul")
        	}
        })
    }
}
