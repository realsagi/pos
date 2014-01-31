package pos.project

class Billsell {

    // data static part1
	Double numbill
	String cusid
	String namecus
	String emid
	String nameem
	Date datesell
	String giftcard
	
	//data dinamic
	String nameproduct
	int count
	Double priceperunit
	Double sum

	// data static part2
	Double pricegiftcard
	Double sumall
	

    static constraints = {
    	numbill unique: true
    }
}
