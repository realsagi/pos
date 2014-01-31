package pos.project


class NewstockComposer extends zk.grails.Composer {

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

        //ประกาศตัวแปร state ต่างๆ
        boolean checkchangecount = false
        int sentnewcount = 0
       	int checkprintbuttonadd = 0
       	boolean checkedit = false
       	boolean checkprintbeforchecedit = false
       	boolean checkprint = false

    	$('#barcod').focus()
    	Date date = new Date()
    	$('#dateimport').val(date)

        for(Modestock m : Modestock.findAll()){
        	$('#listmode').append{
        		comboitem(label:m.namemode)
        	}
        	$('#listmode2').append{
        		comboitem(label:m.namemode)
        	}
        }

        $('#cancle').on('click',{
        	redirect(uri: "/newmodestock.zul")
        })

        $('#barcod').on('change',{
        	def searchbarcode = Newstock.findByBarcode($('#barcod').text())
        	if(searchbarcode != null){
        		checkedit = true
        		if(checkprintbeforchecedit == true){
        			$('#listproduct > rows > row').detach()
        			checkprint = false
        			$('#listmode2').val("")
        		}
        		$('#barcod').val(searchbarcode.barcode)
			    $('#barcod').setDisabled(true)
				$('#nameproduct').val(searchbarcode.nameproduct)
				$('#listmode').val(searchbarcode.modeproduct)
				$('#pricetoon').val(searchbarcode.pricetoon)
				$('#priceperunit').val(searchbarcode.priceperunit)
				$('#priecbig').val(searchbarcode.pricebig)
				$('#countproduct').val(searchbarcode.countproduct)
				$('#countproduct').setDisabled(true)							
				$('#dateimport').val(searchbarcode.productimport)
				$('#extdat').val(searchbarcode.productexport)
				$('#detail').val(searchbarcode.detail)
				if(checkprintbuttonadd == 0){
					$('#addcount').append{
						button(label:"เพิ่ม")
						button(label:"ลด")
						checkprintbuttonadd = 1
					}
					$('button[label="เพิ่ม"]').on('click',{
						$d("#windowpop").setVisible(true)
				        $d("#windowpop").setLeft("30%")
				        $d("#windowpop").setTop("30%")
				        $d("#windowpop").setSizable(true)
				        $d("#windowpop").doPopup() 
				        $d("#newcountpro").focus() 
				        $d("#savenewcount").on('click',{
				        	int sumcount = 0
				          	int oldcount = searchbarcode.countproduct
				           	int newcount = Integer.parseInt($d("#newcountpro").text())
				           	sumcount = oldcount+newcount
				           	if(newcount < 0){
				           		alert("ขออภัยค่ะท่านสามารถเพิ่มสินค้าได้ เป็นจำนวน เต็ม บวกเท่านั้นค่ะ")
				           		$d("#windowpop").setVisible(false)
				           	}
				           	else if(sumcount < 0){
				           		alert("ขออภัย ค่ะ ไม่สามารถทำให้จำนวนสินค้าน้อยกว่า 0 ได้ค่ะ")
				           		$d("#windowpop").setVisible(false)
				           	}else{
				           		sentnewcount = newcount
				           		$('#countproduct').val(sumcount)
				           		$d("#windowpop").setVisible(false)
				           		checkchangecount = true
				          	}
				        })
					})
					$('button[label="ลด"]').on('click',{
						$d("#discount").setVisible(true)
				        $d("#discount").setLeft("30%")
				        $d("#discount").setTop("30%")
				        $d("#discount").setSizable(true)
				        $d("#discount").doPopup() 
				        $d("#disnewcount").focus()
				        $d("#savedisnew").on('click',{
				        	int sumcount = 0
				          	int oldcount = searchbarcode.countproduct
				           	int newcount = Integer.parseInt($d("#disnewcount").text())
				           	sumcount = oldcount+newcount
				           	if(newcount > 0){
				           		alert("กรุณาใส่เครื่องหมาย ลบ (\"-\") ด้านหน้าตัวเลขด้วยค่ะ")
				           		$d("#discount").setVisible(false)
				           	}
				           	else if(sumcount < 0){
				           		alert("ขออภัย ค่ะ ไม่สามารถทำให้จำนวนสินค้าน้อยกว่า 0 ได้ค่ะ")
				           		$d("#discount").setVisible(false)
				           	}else{
				           		sentnewcount = newcount
				           		$('#countproduct').val(sumcount)
				           		$d("#discount").setVisible(false)
				           		checkchangecount = true
				          	}
				        })
					})
				}
        	}
        })

        $('#save').on('click',{
        	if($('#barcod').text() == "" ||
        		$('#nameproduct').text() == "" ||
        		$('#listmode').text() == "" ||
        		$('#pricetoon').text() == "" ||
        		$('#priceperunit').text() == "" ||
        		$('#priecbig').text() == "" ||
        		$('#countproduct').text() == "" ||
        		$('#dateimport').text() == "" ||
        		$('#txtnameunit').text() == "" ||
        		$('#extdat').text() == "" 
        		){
        		alert("กรุณากรอกข้อมูลให้ครบด้วยค่ะ")
        	}
        	else{
        		def searchsock = Newstock.findByBarcode($('#barcod').text())
        		if(searchsock != null){
        			if(checkchangecount == true){
        				addsumbuy(sentnewcount)
        				checkchangecount = false
        			}
        			searchsock.barcode = $('#barcod').text()
					searchsock.nameproduct = $('#nameproduct').text()
					searchsock.modeproduct = $('#listmode').text()
					searchsock.pricetoon = Double.parseDouble($('#pricetoon').text())
					searchsock.priceperunit = Double.parseDouble($('#priceperunit').text())
					searchsock.pricebig = Double.parseDouble($('#priecbig').text())
					searchsock.countproduct =  Integer.parseInt($('#countproduct').text())
					searchsock.nameunit = $('#txtnameunit').text()
					searchsock.productimport = $('#dateimport').getValue() 
					searchsock.productexport = $('#extdat').getValue() 
					searchsock.detail = $('#detail').text()
					searchsock.save()
					redirect(uri: "/newmodestock.zul")
				}
        		else{
        			addsumbuy(Integer.parseInt($('#countproduct').text()))
        			def stock = new Newstock()
	        		stock.barcode = $('#barcod').text()
					stock.nameproduct = $('#nameproduct').text()
					stock.modeproduct = $('#listmode').text()
					stock.pricetoon = Double.parseDouble($('#pricetoon').text())
					stock.priceperunit = Double.parseDouble($('#priceperunit').text())
					stock.pricebig = Double.parseDouble($('#priecbig').text())
					stock.countproduct =  Integer.parseInt($('#countproduct').text())
					stock.nameunit = $('#txtnameunit').text()
					stock.productimport = $('#dateimport').getValue() 
					stock.productexport = $('#extdat').getValue() 
					stock.detail = $('#detail').text()
					stock.save()
					redirect(uri: "/newmodestock.zul")
        		}
        		
        	}
        })
		$('#listmode2').on('select',{	
			if(checkprint == true){
				$('#listproduct > rows > row').detach()
				checkprint = false
			}		
			for(Newstock m : Newstock.findAll()){
				if(m.modeproduct == $('#listmode2').text()){
					$('#listproduct > rows').append{
						row(style:"font-size:14px;font-weight:bold;color:black;background:pink;"){
							label(value:m.barcode,style:"font-size:14px;font-weight:bold;color:black")
							label(value:m.nameproduct,style:"font-size:14px;font-weight:bold;color:black")
							label(value:m.modeproduct,style:"font-size:14px;font-weight:bold;color:black")
							label(value:m.pricetoon,style:"font-size:14px;font-weight:bold;color:black")
							label(value:"${m.priceperunit}",style:"font-size:14px;font-weight:bold;color:black")
							label(value:"${m.pricebig}",style:"font-size:14px;font-weight:bold;color:black")
							label(value:"${m.countproduct}",style:"font-size:14px;font-weight:bold;color:black")
							datebox(value:m.productimport,zclass:"mydb",disabled:"true",style:"font-size:12px;font-weight:bold;color:black")
							datebox(value:m.productexport,zclass:"mydb",disabled:"true",style:"font-size:12px;font-weight:bold;color:black")
							if(checkedit == false){
								hbox{
									button(orient:"vertical",mold:"trendy",label:"แก้ไข",image:"/ext/images/icon/edit.png",id:"e${m.barcode}",style:"font-size:12px;font-weight:bold;color:black")
									button(orient:"vertical",mold:"trendy",label:"ลบ",image:"/ext/images/icon/delete.png",id:m.barcode,style:"font-size:12px;font-weight:bold;color:black")
									checkprintbeforchecedit = true
								}
							}
							else{
								hbox{
									button(orient:"vertical",mold:"trendy",label:"แก้ไข",image:"/ext/images/icon/edit.png",disabled:"true",style:"font-size:12px;font-weight:bold;color:black")
									button(orient:"vertical",mold:"trendy",label:"ลบ",image:"/ext/images/icon/delete.png",disabled:"true",style:"font-size:12px;font-weight:bold;color:black")
								}							
							}
						}
					}
					$('button[label="แก้ไข"]').on('click',{
						int oldcount = 0
						int sumcount = 0
						String search = it.target.id.substring(1)
						def searchbarcode = Newstock.findByBarcode(search)
			        	if(searchbarcode != null){
			        		$('#barcod').val(searchbarcode.barcode)
			        		$('#barcod').setDisabled(true)
							$('#nameproduct').val(searchbarcode.nameproduct)
							$('#listmode').val(searchbarcode.modeproduct)
							$('#pricetoon').val(searchbarcode.pricetoon)
							$('#priceperunit').val(searchbarcode.priceperunit)
							$('#priecbig').val(searchbarcode.pricebig)
							$('#countproduct').val(searchbarcode.countproduct)
							oldcount = searchbarcode.countproduct
							$('#countproduct').setDisabled(true)							
							$('#dateimport').val(searchbarcode.productimport)
							$('#extdat').val(searchbarcode.productexport)
							$('#detail').val(searchbarcode.detail)
							if(checkprintbuttonadd == 1){
								$('#addcount > button').detach()
								checkprintbuttonadd = 0
							}
							if(checkprintbuttonadd == 0){
								$('#addcount').append{
									button(label:"เพิ่ม")
									button(label:"ลด")
									checkprintbuttonadd = 1
								}
								$('button[label="เพิ่ม"]').on('click',{
									$d("#windowpop").setVisible(true)
					                $d("#windowpop").setLeft("30%")
					                $d("#windowpop").setTop("30%")
					                $d("#windowpop").setSizable(true)
					                $d("#windowpop").doPopup() 
					                $d("#newcountpro").focus() 
					                $d("#savenewcount").on('click',{
							           	int newcount = Integer.parseInt($d("#newcountpro").text())
							           	sumcount = oldcount+newcount
					                	if(newcount < 0){
					                		alert("ขออภัยค่ะท่านสามารถเพิ่มสินค้าได้ เป็นจำนวน เต็ม บวกเท่านั้นค่ะ")
					                		$d("#windowpop").setVisible(false)
					                	}
					                	else if(sumcount < 0){
					                		alert("ขออภัย ค่ะ ไม่สามารถทำให้จำนวนสินค้าน้อยกว่า 0 ได้ค่ะ")
					                		$d("#windowpop").setVisible(false)
					                	}else{
					                		sentnewcount = newcount
					                		$('#countproduct').val(sumcount)
					                		$d("#windowpop").setVisible(false)
					                		checkchangecount = true
				                		}
							        })
								})
								$('button[label="ลด"]').on('click',{
									$d("#discount").setVisible(true)
					                $d("#discount").setLeft("30%")
					                $d("#discount").setTop("30%")
					                $d("#discount").setSizable(true)
					                $d("#discount").doPopup() 
					                $d("#disnewcount").focus() 
					                $d("#savedisnew").on('click',{
							           	int newcount = Integer.parseInt($d("#disnewcount").text())
							           	sumcount = oldcount+newcount
					                	if(newcount > 0){
					                		alert("กรุณาใส่เครื่องหมาย ลบ (\"-\") ด้านหน้าตัวเลขด้วยค่ะ")
					                		$d("#discount").setVisible(false)
					                	}
					                	else if(sumcount < 0){
					                		alert("ขออภัย ค่ะ ไม่สามารถทำให้จำนวนสินค้าน้อยกว่า 0 ได้ค่ะ")
					                		$d("#discount").setVisible(false)
					                	}else{
					                		sentnewcount = newcount
					                		$('#countproduct').val(sumcount)
					                		$d("#discount").setVisible(false)
					                		checkchangecount = true
				                		}
							        })
								})
							}
						}
					})
					$('button[label="ลบ"]').on('click',{
						def finddelete = Newstock.findByBarcode(it.target.id)
						if(finddelete != null){
							finddelete.delete()
						}
						redirect(uri: "/newmodestock.zul")
					})
					checkprint = true
				}
			}
		})
    }
    public void addsumbuy(int count){
    	def addbuy = new Buy()
    	addbuy.barcode  = $('#barcod').text()
		addbuy.useradd = session.user
		addbuy.nameproduct = $('#nameproduct').text()
		addbuy.modeproduct = $('#listmode').text()
		addbuy.pricetoon = Double.parseDouble($('#pricetoon').text())
		addbuy.priceperunit = Double.parseDouble($('#priceperunit').text())
		addbuy.pricebig = Double.parseDouble($('#priecbig').text())
		addbuy.countproduct =  count
		addbuy.nameunit = $('#txtnameunit').text()
		addbuy.productimport = $('#dateimport').text()
		addbuy.productexport = $('#extdat').text()
		addbuy.detail = $('#detail').text()
		addbuy.save()
    }
}
