package pos.project

class Runbill {

    Double numbill
	String user

    static constraints = {
    	numbill unique: true
    }
}
