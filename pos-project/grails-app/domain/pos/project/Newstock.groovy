package pos.project

class Newstock {

    String barcode
	String nameproduct
	String modeproduct
	Double pricetoon
	Double priceperunit
	Double pricebig
	int countproduct
	String nameunit
	Date productimport
	Date productexport
	String detail

    static constraints = {
    	barcode unique: true
    }
}
