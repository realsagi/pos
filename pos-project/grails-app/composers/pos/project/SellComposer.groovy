package pos.project
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont
import org.apache.pdfbox.encoding.Encoding

class SellComposer extends zk.grails.Composer {

	Double tempsumall = 0
    boolean checkprin = false
    Double vat = 0
    String vatornostate = ""

    def afterCompose = { window ->
        //=============================== chec session =======================
        def checkgroup = Groupemployee.findByNamegroup(session.groupname)
        if(checkgroup != null){
            if(checkgroup.l1 == session.l1){
                window.visible = true
            }
            else{
                redirect(uri: "/subindex.zul")
            }
        }
        //=====================================================================
        vatornostate = $('#vatorno').selectedItem.label
        $('#sumprice').val(0)
        $('#idcus').val("0000")
        $('#namecus').val("ลูกค้าทั่วไป")
        $('#idproduct').focus()

        int countpro = Integer.parseInt($('#count').text())

        Date date = new Date()
        $('#imday').val(date)

        $('#count').on('click',{
            $('#count').select()
        })
        $('#count').on('change',{
            countpro = Integer.parseInt($('#count').text())
        })

        $('#idcus').on('click',{
            $('#idcus').select()
        })

        $('#idcus').on('change',{
            def findcus = Customer.findByCusid($('#idcus').text())
            if(findcus != null){
                $('#namecus').val(findcus.namecus)
            }
            else{
                $('#namecus').val("")
            }
        })

        def findem = Employee.findByEmid(session.user)
        if(findem != null){
            $('#idem').val(findem.emid)
            $('#nameem').val(findem.nameem)
        }

        Double maxnum = 1
        def findnumbill = Runbill.findByNumbill(maxnum)
        if(findnumbill == null){
            def runnum = new Runbill()
            runnum.numbill = maxnum
            runnum.user = session.user
            runnum.save()
            $('#idbill').val(maxnum)
        }
        else{
            for(Runbill run : Runbill.findAll()){
                if(maxnum <= run.numbill){
                    maxnum = run.numbill
                }
            }
            def findmax = Runbill.findByNumbill(maxnum)
            if(findmax != null){
                if(findmax.user != session.user){
                    maxnum++
                    def runnum = new Runbill()
                    runnum.numbill = maxnum
                    runnum.user = session.user
                    runnum.save()
                }
            }
            $('#idbill').val(maxnum)
        }
        
        printrowsell()
        $('#idproduct').on('change',{
            Double sumtemp = 0
            Double sumall = 0
            def findidbar = Newstock.findByBarcode($('#idproduct').text())

            if(findidbar != null){
                String bar = findidbar.barcode
                String name = findidbar.nameproduct
                Double priceu = findidbar.priceperunit
                Double priceb = findidbar.pricebig
                sumtemp = countpro*findidbar.priceperunit

                boolean checkseartemp = false
                for(Tempsell findtempbar : Tempsell.findAll()){
                    if(findtempbar.barcode == $('#idproduct').text() && session.user == findtempbar.emid){
                        findtempbar.countpro = countpro+findtempbar.countpro
                        findtempbar.priceperunit = priceu
                        findtempbar.pricebig = priceb
                        findtempbar.sum = sumtemp+findtempbar.sum
                        findtempbar.save()

                        printrowsell()
                        $('#idproduct').val("")
                        $('#idproduct').focus()
                        $('#count').val(1)
                        countpro = 1
                        checkseartemp = true
                    }
                }
                if(checkseartemp == false){
                    def savetemp = new Tempsell()
                    savetemp.emid = session.user
                    savetemp.barcode = bar
                    savetemp.nameproduct = name
                    savetemp.countpro = countpro
                    savetemp.priceperunit = priceu
                    savetemp.pricebig = priceb
                    savetemp.sum = sumtemp
                    savetemp.save()

                    printrowsell()
                    $('#idproduct').val("")
                    $('#idproduct').focus()
                    $('#count').val(1)
                    countpro = 1
                    checkseartemp = true
                }
           }
           else{
                printrowsell()
                $('#count').val(1)
                countpro = 1
           }
        })
        
        $('#vatorno').on('click',{
            alert("test")
            printrowsell()
        })

        $('#toonorsent').on('click',{
            printrowsell()
        })

        $('#idgift').on('change',{
            Double oldsumall = $('#sumprice').getValue() 
            Double down = Double.parseDouble($('#downpricefromlist').text())
            def findcard = Card.findByIdcard($('#idgift').text())
            if(findcard != null){
                DateFormat  df = new SimpleDateFormat("dd/MM/yyyy")
                Date today = df.parse($('#imday').text())
                Date extcard = df.parse(findcard.extday)
                if(today <= extcard){
                    int downbath = findcard.bath
                    $('#downpricefromlist').val("${downbath}")
                    printrowsell()
                }
                else{
                    alert("บัตรส่วนลดหมดอายุแล้วค่ะ")
                    $('#idgift').val("")
                }
            }
            else{
                $('#downpricefromlist').val("0")
                printrowsell()
            }
        })

        $('#finish').on('click',{
            $d("#finishpage").setVisible(true)
            $d("#finishpage").setLeft("30%")
            $d("#finishpage").setTop("30%")
            $d("#finishpage").setSizable(true)
            $d("#finishpage").doPopup() 
            $d('#sumfinish').val($('#sumprice').getValue())
            $d('#payfinish').focus()
            $d('#payfinish').val(0)
            $d('#tonfinish').val(0)
            $d('#okfinish').setDisabled(true)
            $d('#payfinish').on('change',{
                if($d('#payfinish').getValue() == null){
                    $d('#payfinish').val(0)
                }
                else{
                    $d('#tonfinish').val($d('#payfinish').getValue() - $d('#sumfinish').getValue())
                    if($d('#tonfinish').getValue() > 0){
                        $d('#okfinish').setDisabled(false)
                    }
                    else{
                        $d('#okfinish').setDisabled(true)
                    }
                }
            })
            $d('#okfinish').on('click',{
                $d("#finishpage").setVisible(false)
            })
        })
        
        $('#printbill').on('click',{
            ArrayList<String> stringtext = new ArrayList<String>();

            stringtext.add("A")
            stringtext.add("ก")
            stringtext.add("test language")

            toPDF(stringtext)
        })
    }
    public void toPDF(ArrayList<String> data){
        try {
            PDDocument document = new PDDocument()
            PDPage page = new PDPage()
            page.setMediaBox(PDPage.PAGE_SIZE_A4);
            document.addPage(page);
            PDFont font = PDTrueTypeFont.loadTTF(document, application.getRealPath("/font/angsau.ttf"));
            font = PDType1Font.HELVETICA
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
        
            float fit = 800
            for(int i = 0 ; i < data.size() ; i++){
                contentStream.beginText();
                contentStream.setFont( font, 12 );
                contentStream.moveTextPositionByAmount( 13, fit );
                contentStream.drawString(data.get(i));
                contentStream.endText();
                fit-=15
            }

            contentStream.close();
            document.save(application.getRealPath("myPDF.pdf"));
            document.close();
            
        } catch (Exception e) {
            alert(e)
        }
    }
    public BigDecimal twodigit(Double num){
        BigDecimal twodi = new BigDecimal(num)
        twodi = twodi.setScale(2,BigDecimal.ROUND_HALF_EVEN)
        return twodi
    }
    public void printrowsell(){
        Double sumallm = 0
        int j = 1
        int checkbutton = 0
        String toonorsentstate = $('#toonorsent').selectedItem.label
        if(checkprin == true){
            $('#listbuy > rows > row').detach()
            checkprin = false
        }
        for(Tempsell temp : Tempsell.findAll()){
            if(temp.emid == session.user){
                $('#listbuy > rows').append{
                row{
                    label(value:"${j}",style:"font-size:14px;font-weight:bold;color:black")
                    label(value:temp.nameproduct,style:"font-size:14px;font-weight:bold;color:black")
                    label(value:"${temp.countpro}",style:"font-size:14px;font-weight:bold;color:black")
                    if(toonorsentstate == "[ขายปลีก]"){
                        label(value:"${temp.priceperunit}",style:"font-size:14px;font-weight:bold;color:black")
                        label(value:"${temp.sum}",style:"font-size:14px;font-weight:bold;color:black")
                        sumallm = sumallm+temp.sum
                        tempsumall = sumallm
                    }
                    if(toonorsentstate == "[ขายส่ง]"){
                        label(value:"${temp.pricebig}",style:"font-size:14px;font-weight:bold;color:black")
                        label(value:"${temp.pricebig * temp.countpro}",style:"font-size:14px;font-weight:bold;color:black")
                        sumallm = sumallm+(temp.pricebig*temp.countpro)
                        tempsumall = sumallm
                    }
                    button(id:temp.barcode,style:"font-size:14px;font-weight:bold;color:black",image:"/ext/images/icon/delete.png",mold:"trendy")
                    }
                    j++
                    checkprin = true
                }
                $('#listbuy > rows > row > button').on('click',{
                    for(Tempsell finddelete : Tempsell.findAll()){
                        if(finddelete.barcode == it.target.id && finddelete.emid == session.user){
                            finddelete.delete()
                            redirect(uri: "/sell.zul")
                        }
                    }
                    
                })
                vat = twodigit(sumallm-(sumallm/1.07))
                Double down = Double.parseDouble($('#downpricefromlist').text())
                vatornostate = $('#vatorno').selectedItem.label
                if(vatornostate == "[ไม่คิดVAT]"){
                    $('#vatpricefromlist').val("0")
                    $('#sumpricefromlist').val(tempsumall - vat - down)
                    $('#sumprice').val(tempsumall - vat - down)
                }
                if(vatornostate == "[คิดVAT]"){
                    $('#vatpricefromlist').val("${vat}")
                    $('#sumprice').val(tempsumall - down)
                    $('#sumpricefromlist').val($('#sumprice').text())
                }
            }
        }
    }
}
