package com.employee.services;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.*;


import com.employee.entity.Employee;
import com.employee.exceptions.EmployeeNotFoundException;
import com.employee.ifaces.EmployeeCrudRepository;
import com.employee.repos.EmployeeRepository;

public class EmployeeServices {

	private EmployeeCrudRepository repository;
	private Connection connection;
	private static final Logger logger=LogManager.getRootLogger();
	
	public EmployeeServices(Connection connection) {
		super();
		this.connection=connection;
		this.repository=new EmployeeRepository(this.connection);
	}
	
	public void addEmployee(Employee employee){
		boolean isAdded=false;
		isAdded = this.repository.save(employee);
		if(isAdded) {
			logger.info("Employee Added : "+isAdded);
		}
		else {
			logger.error("Employee Added : "+isAdded);
		}
	}
	
	public void findByName(String firstName){
		List<Employee> employeesList=new ArrayList<>();
		try {
			employeesList=this.repository.getByName(firstName);
			employeesList.forEach(e->logger.info(e));
		} catch (EmployeeNotFoundException e1) {
			logger.error("Employee with the first name "+firstName+" is not found");
		}
		
	}
	
	public void displayNameAndPhoneNumber(){
		List<String> employeesNameAndPhoneNumber=new ArrayList<>();
		try {
			employeesNameAndPhoneNumber = this.repository.getNameAndPhoneNumber();
			employeesNameAndPhoneNumber.forEach(System.out::println);
		} catch (EmployeeNotFoundException e) {
			logger.error("No employee is found");
		}
		
	}
	
	public void updateEmailAndPhoneNumber(String initialEmail,String changedEmail,long phoneNumber){
		boolean isUpdated=false;
		try {
			isUpdated = this.repository.updateEmailAndPhoneNumber(initialEmail, changedEmail, phoneNumber);
		} catch (EmployeeNotFoundException e) {
			logger.error("Employee not found with the mail "+initialEmail);
		}
		if(isUpdated) {
			logger.info("Employee Updated : "+isUpdated);
		}
		else {
			logger.error("Employee Updated : "+isUpdated);
		}
	}
	
	public void deleteEmployeeByFirstName(String firstName,String email){
		boolean isDeleted=false;
		try {
			isDeleted = this.repository.deleteByName(firstName,email);
		} catch (EmployeeNotFoundException e) {
			logger.error("Employee not found with the mail id "+email);
		}
		if(isDeleted) {
			logger.info("Employee deleted : "+isDeleted);
		}
		else {
			logger.error("Employee deleted : "+isDeleted);
		}
	}
	
	public void displayNameAndMail(LocalDate date){
		List<String> nameAndMail=new ArrayList<>();
		try {
			nameAndMail = this.repository.getNameAndEmail(date);
			nameAndMail.forEach(System.out::println);
		} catch (EmployeeNotFoundException e) {
			logger.error("No Employee is found with the birthday date "+date);
		}
		
	}
	
	public void displayNameAndPhoneNumber(LocalDate date){
		List<String> nameAndPhoneNumber=new ArrayList<>();
		try {
			nameAndPhoneNumber = this.repository.getNameAndPhoneNumber(date);
			nameAndPhoneNumber.forEach(System.out::println);
		} catch (EmployeeNotFoundException e) {
			logger.error("No Employee is found with the anniversary date "+date);
		}
		
	}
	
}
