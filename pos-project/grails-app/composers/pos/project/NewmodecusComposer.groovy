package pos.project


class NewmodecusComposer extends zk.grails.Composer {

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

        $('#namegroupcustomer').focus()

        int i=1
        for(Groupcus g : Groupcus.findAll()){
            $('#listnamegroup > rows').append{
                row{
                    label(value:"${i}",style:"font-size:14px;font-weight:bold;color:black")
                    label(value:g.namegroup,style:"font-size:14px;font-weight:bold;color:black")
                    button(id:g.namegroup,style:"font-size:14px;font-weight:bold;color:black",image:"/ext/images/icon/delete.png",mold:"trendy")
                }
            }
            i++
            $('#listnamegroup > rows > row > button').on('click',{
                def delete = Groupcus.findByNamegroup(it.target.id)
                delete.delete()
                redirect(uri: "/modeemployee.zul")
            })
        }

        $('#submit').on('click',{
            if($('#namegroupcustomer').text() == ""){
                alert("กรุณากรอกชื่อกลุ่มลูกค้าใหม่ด้วยค่ะ")
            }
            else{
                Groupcus groupcus = new Groupcus()
                groupcus.namegroup = $('#namegroupcustomer').text()
                groupcus.save()
                redirect(uri: "/modeemployee.zul")
            }
        })

        $('#clear').on('click',{
            $('#namegroupcustomer').val("")
        })
    }
}
