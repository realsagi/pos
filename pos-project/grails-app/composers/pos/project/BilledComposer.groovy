package pos.project


class BilledComposer extends zk.grails.Composer {

    def afterCompose = { window ->
       //=============================== chec session =======================
        def checkgroup = Groupemployee.findByNamegroup(session.groupname)
        if(checkgroup != null){
            if(checkgroup.l4 == session.l4){
                window.visible = true
            }
            else{
                redirect(uri: "/subindex.zul")
            }
        }
        //=====================================================================
    }
}
