package pos.project


class LoginComposer extends zk.grails.Composer {

    def afterCompose = { window ->
        Date date = new Date()      
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat() 
		df.applyPattern("HHmmss")

    	$('#clear').on('click',{
    		$('#user').val("")
    		$('#pass').val("")
    	})

    	$('#login').on('click',{
    		def check = Employee.findByEmid($('#user').text())
    		if(check != null){
    			if(check.emid == $('#user').text() && check.passwords == $('#pass').text()){
    				session.user = check.emid
    				session.groupname = check.groupname
    				def savestamp = new Stamplogin()
    				savestamp.user = session.user
					savestamp.groupname = session.groupname
					savestamp.timelogin = df.format(date)
					savestamp.save()
    				redirect(uri: "/subindex.zul")
    			}
    			else{
    				alert("รหัสผ่านไม่ถูกต้องค่ะ")
    			}
    		}
    		else{
    			if($('#user').text() == "admin" && $('#pass').text() == "admin"){
    				session.user = "admin"
	    			session.groupname = "admin"
	    			def savestamp = new Stamplogin()
	    			savestamp.user = session.user
					savestamp.groupname = session.groupname
					savestamp.timelogin = df.format(date)
					savestamp.save()
					def groupsave = new Groupemployee()
		        	groupsave.namegroup = session.groupname
		        	groupsave.l1 = "ขายได้"
		        	groupsave.l2 = "เพิ่มหมวดและสินค้าได้"
		        	groupsave.l3 = "เพิ่มหมวดและพนักงานได้"
		        	groupsave.l4 = "ดูยอดและใบเสร็จย้อนหลังได้"
		        	groupsave.l5 = "สร้างบัตรส่วนลดได้"
		        	groupsave.save()
	    			redirect(uri: "/subindex.zul")
    			}
    			alert("ไม่พบ Username "+$('#user').text()+" ค่ะ")
    		}
    	})
    }
}
