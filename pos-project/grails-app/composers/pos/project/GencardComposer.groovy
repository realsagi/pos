package pos.project


class GencardComposer extends zk.grails.Composer {

    def afterCompose = { window ->
        //=============================== chec session =======================
        def checkgroup = Groupemployee.findByNamegroup(session.groupname)
        if(checkgroup != null){
            if(checkgroup.l5 == session.l5){
                window.visible = true
            }
            else{
                redirect(uri: "/subindex.zul")
            }
        }
        //=====================================================================

        int stringprice = 0
        String stringext = ""
        for(Card c : Card.findAll()){
            if(stringprice != c.bath){
                stringprice = c.bath
                $('#listmode').append{
                    comboitem(label:"${c.bath}")
                }
            }
            if(stringext != c.extday){
                stringext = c.extday
                $('#listmode2').append{
                    comboitem(label:c.extday)
                }
            }
        }
        Boolean checkprint = false
        int j = 1
        $('#listmode,#listmode2').on('select',{
            if(checkprint == true){
                $('#listcard > rows > row').detach()
                checkprint = false
            }
            for(Card b : Card.findAll()){
                if($('#listmode').text() == "${b.bath}" || $('#listmode2').text() == b.extday){
                    $('#listcard > rows').append{
                        row(style:"font-size:14px;font-weight:bold;color:black;background:pink;"){
                            label(value:"${j}",style:"font-size:14px;font-weight:bold;color:black")
                            label(value:b.nameadd,style:"font-size:14px;font-weight:bold;color:black")
                            label(value:b.impday,style:"font-size:14px;font-weight:bold;color:black")
                            label(value:b.extday,style:"font-size:14px;font-weight:bold;color:black")
                            label(value:b.idcard,style:"font-size:14px;font-weight:bold;color:black")
                            hbox{
                                label(value:"${b.bath}",style:"font-size:14px;font-weight:bold;color:black")
                                checkbox(id:b.idcard,name:"ch")
                            }
                        }
                    }
                    checkprint = true
                    j++
                }
            }
            $('#checkall').setDisabled(false)
            $('#delete').setDisabled(false)
            j=1
            $('#listmode').val("")
            $('#listmode2').val("")
        })

        Boolean checkstatecheck = false
        $('#checkall').on('click',{
            if(checkstatecheck == false){
                for(cb in $('#listcard > rows > row > hbox > checkbox[checked=false]')){
                    cb.setChecked(true)
                    $('#checkall').setLabel("ยกเลิกการเลือกทั้งหมด")
                    checkstatecheck = true
                }
            }
            else if(checkstatecheck == true){
                for(cb in $('#listcard > rows > row > hbox > checkbox[checked=true]')){
                    cb.setChecked(false)
                    $('#checkall').setLabel("เลือกทั้งหมด")
                    checkstatecheck = false
                }
            }
            

        })


        $('#delete').on('click',{
            for(cb in $('#listcard > rows > row > hbox > checkbox[checked=true]')){
                def findid = Card.findByIdcard(cb.id)
                findid.delete()
            }
            redirect(uri:'/gencard.zul')
        })


        Date date = new Date()
        $('#imday').val(date)
        $('#extday').val(date+30)
        $('#bath').val(0)
        $('#countcard').val(0)

        $('#bath').on('click',{
            $('#bath').select()
        })

        $('#countcard').on('click',{
            $('#countcard').select()
        })

        $('#clear').on('click',{
            redirect(uri:'/gencard.zul')
        })

        $('#save').on('click',{
            if($('#bath').text() == "0" || $('#bath').text() == ""){
                alert("กรุณาระบุจำนวนเงินด้วยค่ะ")
            }
            else if($('#countcard').text() == "0" || $('#countcard').text() == ""){
                alert("กรุณาระบุจนวนบัตรที่จะสร้างด้วยค่ะ")
            }
            else{
                for(int i = 1 ; i <= Integer.parseInt($('#countcard').text()) ; i++){
                    def addcard = new Card()
                    addcard.idcard = "${generratecard()}"
                    addcard.nameadd = session.user
                    addcard.impday = $('#imday').text()
                    addcard.extday = $('#extday').text()
                    addcard.bath = Integer.parseInt($('#bath').text())
                    addcard.save()
                    redirect(uri: "/gencard.zul")
                }
                
            }
        })
    }
    public int generratecard(){
        Random r = new Random()
        int num = r.nextInt(99999) + 99999
        if (num < 100000 || num > 999999) { 
            num = r.nextInt(99999) + 99999; 
            if (num < 100000 || num > 999999) { 
                throw new Exception("Unable to generate PIN at this time..")
            } 
        } 
        return num
    }
}
