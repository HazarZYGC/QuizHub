package FactoryPattern;


public abstract class Person{

	private String name;
	private String surname;
	private String gender;
	private String birthday;
	private String email;
	private String password;
	
	public Person(String name, String surname, String gender, String birthday, String email,String password) {

		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.birthday = birthday;
		this.email = email;
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", surname=" + surname + ", gender=" + gender + ", birthday=" + birthday
				+ ", email=" + email +", password=" + password + "]";
	}



}
