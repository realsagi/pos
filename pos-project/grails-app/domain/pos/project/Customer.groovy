package pos.project

class Customer {

	String cusid
	String passwords
	String namecus
	String cuscard
	String groupcus
	String address
	String tel
	String other

    static constraints = {
    	cusid unique: true
    }
}
