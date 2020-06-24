package model;

public class Employee {

	private String employeeID;
	private String eName;
	private String ePhone;
	private String eEmail;
	private String eWage;
	
	public Employee(String employeeID, String eName, String ePhone, String eEmail, String eWage) {
		this.employeeID = employeeID;
		this.eName = eName;
		this.ePhone = ePhone;
		this.eEmail = eEmail;
		this.eWage = eWage;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

	public String getePhone() {
		return ePhone;
	}

	public void setePhone(String ePhone) {
		this.ePhone = ePhone;
	}

	public String geteEmail() {
		return eEmail;
	}

	public void seteEmail(String eEmail) {
		this.eEmail = eEmail;
	}

	public String geteWage() {
		return eWage;
	}

	public void seteWage(String eWage) {
		this.eWage = eWage;
	}
	
	
	
		
}
