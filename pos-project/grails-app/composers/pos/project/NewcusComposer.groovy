package pos.project


class NewcusComposer extends zk.grails.Composer {

    // -------------- defind ------------------------------------------
        boolean findcheckcus = false
        String tempreturn = ""
    // -------------- end defind---------------------------------------

    def afterCompose = { window ->

        // -------------- set defalse -------------------------------------
        $('#emid').focus()
        // -------------- end setdefalse ----------------------------------

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

        //---------------  show all group customer ----------------------------
        for(Groupcus gc : Groupcus.findAll()){
            $('#listmode').append{
                comboitem(label:gc.namegroup)
            }
            $('#listmode2').append{
                comboitem(label:gc.namegroup)
            }
        }
        //---------------  end show all group customer ------------------------

        // --------------  event onclick save ---------------------------------
            $('#save').on('click',{
                if($('#emid').text() == ""){
                    alert("กรุณากรอกรหัสลูกค้า ค่ะ")
                }
                else if($('#passem').text() == ""){
                    alert("กรุณากรอกรหัสผ่านด้วย ค่ะ")
                }
                else if($('#nameem').text() == ""){
                    alert("กรุณากรอกชื่อลูกค้าด้วย ค่ะ")
                }
                else if($('#numpeople').text() == ""){
                    alert("กรุณากรอกเลขประจำตัวประชาชนด้วย ค่ะ")
                }
                else if($('#listmode').text() == ""){
                    alert("กรุณาเลือกกลุ่มลูกค้าด้วย ค่ะ")
                }
                else if($('#address').text() == ""){
                    alert("กรุณากรอกที่อยู่ด้วย ค่ะ")
                }
                else if($('#tel').text() == ""){
                    alert("กรุณากรอกเบอร์โทรด้วย ค่ะ")
                }
                else{
                    savecustodb()
                    $('#emid').val("")
                    $('#passem').val("")
                    $('#nameem').val("")
                    $('#numpeople').val("")
                    $('#listmode').val("")
                    $('#address').val("")
                    $('#tel').val("")
                    $('#other').val("")
                }
            })
        // --------------  end event onclick save -----------------------------

        // -------------- even onclick clear ----------------------------------
            $('#cancle').on('click',{
                $('#emid').val("")
                $('#passem').val("")
                $('#nameem').val("")
                $('#numpeople').val("")
                $('#listmode').val("")
                $('#address').val("")
                $('#tel').val("")
                $('#other').val("")
            })
        // -------------- end even onclick clear ------------------------------

        // -------------- event onclick listmode2 -----------------------------
            boolean checkprint = false
            $('#listmode2').on('select',{
                if(checkprint == true){
                    $('#listem > rows > row').detach()
                    checkprint = false
                }
                if(checkprint == false){
                    for(Customer cus : Customer.findAll()){
                        if($('#listmode2').text() == cus.groupcus){
                           $('#listem > rows').append{
                                row{
                                    label(value:cus.cusid)
                                    label(value:cus.namecus)
                                    label(value:cus.passwords)
                                    label(value:cus.groupcus)
                                    hbox{
                                        button(label:"แก้ไข",id:"e${cus.cusid}",orient:"vertical",mold:"trendy",image:"/ext/images/icon/edit.png")
                                        button(label:"ลบ",id:cus.cusid,orient:"vertical",mold:"trendy",image:"/ext/images/icon/delete.png")
                                    }
                                }
                            }
                            checkprint = true 
                            //--------------------------  click edit -------------------------
                            $('button[label="แก้ไข"]').on('click',{
                                samecheckid(it.target.id.substring(1))
                            })
                            //--------------------------  end click edit ---------------------
                            //--------------------------  click delete -----------------------
                            $('button[label="ลบ"]').on('click',{
                                def finddelete = Customer.findByCusid(it.target.id)
                                if(finddelete != null){
                                    finddelete.delete()
                                    $('#listem > rows > row').detach()
                                    checkprint = false
                                }
                            })
                            //-------------------------- end click delete --------------------
                        }
                    }
                }
            })
            
        // -------------- event onclick listmode2 -----------------------------

        // -------------- change cusid ----------------------------------------
            $('#emid').on('change',{
                samecheckid($('#emid').text())
                if(findcheckcus == true){
                    alert("มีรหัสลูกค้านี้ในฐานข้อมูลและค่ะ หมวด"+tempreturn+" ค่ะ")
                }
            })
        // -------------- end change cusid ------------------------------------
    }
    // ------------------- method save dabase ---------------------------------
    public void savecustodb(){
        def searchidcus = Customer.findByCusid($('#emid').text())
        if(searchidcus != null){
            searchidcus.cusid = $('#emid').text()
            searchidcus.passwords = $('#passem').text()
            searchidcus.namecus = $('#nameem').text()
            searchidcus.cuscard = $('#numpeople').text()
            searchidcus.groupcus = $('#listmode').text()
            searchidcus.address = $('#address').text()
            searchidcus.tel = $('#tel').text()
            searchidcus.other = $('#other').text()
            searchidcus.save()
        }
        else{
            def savedb = new Customer()
            savedb.cusid = $('#emid').text()
            savedb.passwords = $('#passem').text()
            savedb.namecus = $('#nameem').text()
            savedb.cuscard = $('#numpeople').text()
            savedb.groupcus = $('#listmode').text()
            savedb.address = $('#address').text()
            savedb.tel = $('#tel').text()
            savedb.other = $('#other').text()
            savedb.save()
        }
    }
    // ------------------- end method save dabase -----------------------------

    // ------------------- method checkid -------------------------------------
    public void samecheckid(String txtid){
        def searchidcus = Customer.findByCusid(txtid)
        if(searchidcus != null){
            tempreturn = searchidcus.groupcus
            $('#emid').val(searchidcus.cusid)
            $('#passem').val(searchidcus.passwords)
            $('#nameem').val(searchidcus.namecus)
            $('#numpeople').val(searchidcus.cuscard)
            $('#listmode').val(searchidcus.groupcus)
            $('#address').val(searchidcus.address)
            $('#tel').val(searchidcus.tel)
            $('#other').val(searchidcus.other)
            findcheckcus = true
        }
        else{
            findcheckcus = false
        }
    // ------------------- end method checkid -----------------------------------
    }
}
