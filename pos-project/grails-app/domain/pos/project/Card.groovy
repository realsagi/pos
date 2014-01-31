package pos.project

class Card {

    String idcard
	String nameadd
	String impday
	String extday
	int bath

    static constraints = {
    	idcard unique: true
    }
}
