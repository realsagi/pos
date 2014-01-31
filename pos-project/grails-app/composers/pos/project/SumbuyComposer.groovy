package pos.project


class SumbuyComposer extends zk.grails.Composer {

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

        String stringstate = ""
        for(Buy m : Buy.findAll()){
            if(stringstate != m.productimport.substring(3)){
                stringstate = m.productimport.substring(3)
                $('#search').append{
                    comboitem(label:m.productimport.substring(3))
                }
            }
        }

        Boolean checkprint = false
        Double sumall = 0
        $('#search').on('select',{
            if(checkprint == true){
                $('#listbuy > rows > row').detach()
                checkprint = false
            }
            for(Buy b : Buy.findAll()){
                if($('#search').text() == b.productimport.substring(3)){
                    $('#listbuy > rows').append{
                        row(style:"font-size:14px;font-weight:bold;color:black;background:pink;"){
                            label(value:b.productimport,style:"font-size:14px;font-weight:bold;color:black")
                            label(value:b.barcode,style:"font-size:14px;font-weight:bold;color:black")
                            label(value:b.nameproduct,style:"font-size:14px;font-weight:bold;color:black")
                            label(value:b.modeproduct,style:"font-size:14px;font-weight:bold;color:black")
                            label(value:b.useradd,style:"font-size:14px;font-weight:bold;color:black")
                            label(value:b.pricetoon,style:"font-size:14px;font-weight:bold;color:black")
                            label(value:b.countproduct,style:"font-size:14px;font-weight:bold;color:black")
                            label(value:b.pricetoon*b.countproduct,style:"font-size:14px;font-weight:bold;color:black")
                            sumall = sumall+(b.pricetoon*b.countproduct)
                            checkbox(id:b.id)
                        }
                    }
                    $('#checkall').setDisabled(false)
                    $('#delete').setDisabled(false)
                    checkprint = true
                }
            }
            $('#listbuy > rows').append{
                row(spans:"7",style:"font-size:14px;font-weight:bold;color:black;background:pink;"){
                    label(value:"รวม",style:"font-size:14px;font-weight:bold;color:red")
                    label(value:"${sumall}",style:"font-size:14px;font-weight:bold;color:red")
                    label(style:"font-size:14px;font-weight:bold;color:red")
                }
            }
        })
        
        Boolean checkstatecheck = false
        $('#checkall').on('click',{
            if(checkstatecheck == false){
                for(cb in $('#listbuy > rows > row > checkbox[checked=false]')){
                    cb.setChecked(true)
                    $('#checkall').setLabel("ยกเลิกการเลือกทั้งหมด")
                    checkstatecheck = true
                }
            }
            else if(checkstatecheck == true){
                for(cb in $('#listbuy > rows > row > checkbox[checked=true]')){
                    cb.setChecked(false)
                    $('#checkall').setLabel("เลือกทั้งหมด")
                    checkstatecheck = false
                }   
            }
            

        })

        $('#delete').on('click',{
            for(cb in $('#listbuy > rows > row > hbox > checkbox[checked=true]')){
                def findid = Buy.findById(cb.id)
                findid.delete()
            }
            redirect(uri:'/sumbuy.zul')
        })

    }
}
