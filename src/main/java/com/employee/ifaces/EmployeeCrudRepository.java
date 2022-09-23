package com.employee.ifaces;

import java.time.LocalDate;
import java.util.List;

import com.employee.entity.Employee;
import com.employee.exceptions.EmployeeNotFoundException;

public interface EmployeeCrudRepository extends CrudRespository<Employee> {
	public List<Employee> getByName(String name) throws EmployeeNotFoundException;
	public List<String> getNameAndPhoneNumber() throws EmployeeNotFoundException;
	public boolean updateEmailAndPhoneNumber(String initialMail,String changedMail,long phoneNumber) throws EmployeeNotFoundException;
	public boolean deleteByName(String name,String email) throws EmployeeNotFoundException;
	public List<String> getNameAndEmail(LocalDate date)throws EmployeeNotFoundException;
	public List<String> getNameAndPhoneNumber(LocalDate date)throws EmployeeNotFoundException;
}
