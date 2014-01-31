package pos.project

class Employee {

    String emid
	String passwords
	String nameem
	String numpeople
	String groupname
	String address
	String tel
	String detail

    static constraints = {
    	emid unique: true
    }
}
