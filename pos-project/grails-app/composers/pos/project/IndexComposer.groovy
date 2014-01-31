package pos.project


class IndexComposer extends zk.grails.Composer {

    def afterCompose = { window ->
        $('#useronline').val(session.user)
    	$('#groupuse').val(session.groupname)
    	$('#logout').on('click',{
    		def del = Stamplogin.findByUser(session.user)
    		if(del != null){
    			session.user = ""
    			session.groupname = ""
    			session.l1 = ""
	    		session.l2 = ""
	    		session.l3 = ""
	    		session.l4 = ""
	    		session.l5 = ""
    			del.delete()
    			redirect(uri: "/login.zul")
    		}
    	})
        def checkgroup = Groupemployee.findByNamegroup(session.groupname)
        if(checkgroup != null){
            session.l1 = "ขายได้"
            session.l2 = "เพิ่มหมวดและสินค้าได้"
            session.l3 = "เพิ่มหมวดและพนักงานได้"
            session.l4 = "ดูยอดและใบเสร็จย้อนหลังได้"
            session.l5 = "สร้างบัตรส่วนลดได้"
        }
        else{
            redirect(uri: "/login.zul")
        } 
    }
}
