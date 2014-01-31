package pos.project

class Groupemployee {

    String namegroup
	String l1 //ขายได้
	String l2 //เพิ่มหมวดและสินค้าได้
	String l3 //เพิ่มหมวดและพนักงานได้
	String l4 //ดูยอดและใบเสร็จย้อนหลังได้
	String l5 //สร้างบัตรส่วนลดได้

    static constraints = {
    	namegroup unique: true
    }
}
