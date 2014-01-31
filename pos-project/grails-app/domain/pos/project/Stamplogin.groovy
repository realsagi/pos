package pos.project

class Stamplogin {

    String user
	String groupname
	String timelogin

    static constraints = {
    	user unique: true
    }
}
