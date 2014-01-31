package pos.project


class NewemployeeComposer extends zk.grails.Composer {

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

    	for(Groupemployee m : Groupemployee.findAll()){
        	$('#listmode').append{
        		comboitem(label:m.namegroup)
        	}
        	$('#listmode2').append{
        		comboitem(label:m.namegroup)
        	}
        }

        $('#cancle').on('click',{
        	redirect(uri: "/modeemployee.zul")
        })

        $('#emid').on('change',{
        	def searchidem = Employee.findByEmid($('#emid').text())
        	if(searchidem != null){
        		alert("มีรหัสพนักงานนี้ในฐานข้อมูลและค่ะ หมวด"+searchidem.groupname+" ค่ะ")
        		$('#emid').val(searchidem.emid)
        		$('#passem').val(searchidem.passwords)
        		$('#nameem').val(searchidem.nameem)
        		$('#numpeople').val(searchidem.numpeople)
        		$('#listmode').val(searchidem.groupname)
        		$('#address').val(searchidem.address)
        		$('#tel').val(searchidem.tel)
        		$('#other').val(searchidem.detail)
        	}
        })

        $('#save').on('click',{
        	if($('#emid').text() == "" ||
        		$('#passem').text() == "" ||
        		$('#nameem').text() == "" ||
        		$('#numpeople').text() == "" ||
        		$('#listmode').text() == "" ||
        		$('#address').text() == "" ||
        		$('#tel').text() == "" 
        		){
        		alert("กรุณากรอกข้อมูลให้ครบด้วยค่ะ")
        	}
        	else{
        		def searchidem = Employee.findByEmid($('#emid').text())
        		if(searchidem != null){
	        		searchidem.emid = $('#emid').text()
	        		searchidem.passwords = $('#passem').text()
	        		searchidem.nameem = $('#nameem').text()
	        		searchidem.numpeople = $('#numpeople').text()
	        		searchidem.groupname = $('#listmode').text()
	        		searchidem.address = $('#address').text()
	        		searchidem.tel = $('#tel').text()
	        		searchidem.detail = $('#other').text()
	        		searchidem.save()
	        		redirect(uri: "/modeemployee.zul")
	        	}
	        	else{
	        		def saveem = new Employee()
	        		saveem.emid = $('#emid').text()
	        		saveem.passwords = $('#passem').text()
	        		saveem.nameem = $('#nameem').text()
	        		saveem.numpeople = $('#numpeople').text()
	        		saveem.groupname = $('#listmode').text()
	        		saveem.address = $('#address').text()
	        		saveem.tel = $('#tel').text()
	        		saveem.detail = $('#other').text()
	        		saveem.save()
	        		redirect(uri: "/modeemployee.zul")
	        	}
        	}
        })

		boolean checkprint = false
		$('#listmode2').on('select',{	
			if(checkprint == true){
				$('#listem > rows > row').detach()
				checkprint = false
			}		
			for(Employee m : Employee.findAll()){
				if(m.groupname == $('#listmode2').text()){
					$('#listem > rows').append{
						row(style:"font-size:14px;font-weight:bold;color:black;background:pink;"){
							label(value:m.emid,style:"font-size:14px;font-weight:bold;color:black")
							label(value:m.nameem,style:"font-size:14px;font-weight:bold;color:black")
							label(value:m.passwords,style:"font-size:14px;font-weight:bold;color:black")
							label(value:m.groupname,style:"font-size:14px;font-weight:bold;color:black")
							hbox{
								button(label:"แก้ไข",id:"e${m.emid}",orient:"vertical",mold:"trendy",image:"/ext/images/icon/edit.png",style:"font-size:12px;font-weight:bold;color:black")
								button(label:"ลบ",id:m.emid,orient:"vertical",mold:"trendy",image:"/ext/images/icon/delete.png",style:"font-size:12px;font-weight:bold;color:black")
							}
						}
					}

					$('button[label="แก้ไข"]').on('click',{
						String search = it.target.id.substring(1)
						def searchidem = Employee.findByEmid(search)
			        	if(searchidem != null){
			        		$('#emid').val(searchidem.emid)
			        		$('#passem').val(searchidem.passwords)
			        		$('#nameem').val(searchidem.nameem)
			        		$('#numpeople').val(searchidem.numpeople)
			        		$('#listmode').val(searchidem.groupname)
			        		$('#address').val(searchidem.address)
			        		$('#tel').val(searchidem.tel)
			        		$('#other').val(searchidem.detail)
			        	}
					})
					$('button[label="ลบ"]').on('click',{
						def finddelete = Employee.findByEmid(it.target.id)
						if(finddelete != null){
							finddelete.delete()
						}
						redirect(uri: "/modeemployee.zul")
					})
				
					checkprint = true
				}
			}
		})
    }
}
