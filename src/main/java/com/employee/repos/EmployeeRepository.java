package com.employee.repos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.employee.entity.Employee;
import com.employee.exceptions.EmployeeNotFoundException;
import com.employee.ifaces.EmployeeCrudRepository;


public class EmployeeRepository implements EmployeeCrudRepository {
	
	private Connection connection;
	private List<Employee> employeeList;

	public EmployeeRepository(Connection connection) {
		super();
		this.employeeList=new ArrayList<>();
		this.connection = connection;
	}

	@Override
	public boolean save(Employee employee){
		String query="INSERT INTO employee_table values(?,?,?,?,?,?,?)";
		int rowAdded=0;
		try(PreparedStatement statement=connection.prepareStatement(query)){
			statement.setString(1,employee.getFirstName());
			statement.setString(2,employee.getLastName());
			statement.setString(3,employee.getAddress());
			statement.setString(4,employee.getEmailAddress());
			statement.setLong(5, employee.getPhoneNumber());
			statement.setDate(6, Date.valueOf(employee.getDateOfBirth()));
			if(employee.getWeddingDate()!=null) {
				statement.setDate(7,Date.valueOf(employee.getWeddingDate()));
			}
			else {
				statement.setDate(7,null);
			}
			rowAdded=statement.executeUpdate();
		}
		catch (Exception e) {
			System.err.println("Employee with the same mail id is already found");
		}
		if(rowAdded==1) {
			try {
				this.employeeList=findAll();
			} catch (EmployeeNotFoundException e) {
				System.err.println("No employee is found");
			}
			return true;
		}
		return false;
	}

	@Override
	public List<Employee> getByName(String name) throws EmployeeNotFoundException {
		List<Employee> list=this.employeeList.stream().filter(e->e.getFirstName().equals(name)).collect(Collectors.toList());
		if(list.isEmpty()) {
			throw new EmployeeNotFoundException("ERR-102", "Employee Not Found with the given name : "+name);
		}
		else {
			return list;
		}
	}

	@Override
	public List<String> getNameAndPhoneNumber() throws EmployeeNotFoundException {
		List<String> employeesList = new ArrayList<>();
        Employee employee=null;
        String query = "SELECT first_name,phone_number FROM employee_table";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                employee = mapRowToObject(resultSet,"NameAndPhoneNumber");
                employeesList.add("Employee Name : "+employee.getFirstName()+" Employee Phone number : "+employee.getPhoneNumber());
                while (resultSet.next()) {
                    employee = mapRowToObject(resultSet,"NameAndPhoneNumber");
                    employeesList.add("Employee Name : "+employee.getFirstName()+" Employee Phone number : "+employee.getPhoneNumber());
                }
            } else {
                throw new EmployeeNotFoundException("ERR-103", "No Employees Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeesList;
	}

	@Override
	public boolean updateEmailAndPhoneNumber(String initialMail, String changedMail, long phoneNumber) throws EmployeeNotFoundException {
		String query="UPDATE employee_table SET email_address=?,phone_number=? WHERE email_address=?";
		int rowUpdated=0;
		try(PreparedStatement statement=connection.prepareStatement(query)){
			statement.setString(1,changedMail);
			statement.setLong(2,phoneNumber);
			statement.setString(3,initialMail);
			rowUpdated=statement.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		if(rowUpdated==1) {
			try {
				this.employeeList=findAll();
			} catch (EmployeeNotFoundException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteByName(String name,String email) throws EmployeeNotFoundException {
		String query="DELETE FROM employee_table WHERE first_name=? AND email_address=?";
		int rowDeleted=0;
		try(PreparedStatement statement=connection.prepareStatement(query)){
			statement.setString(1,name);
			statement.setString(2, email);
			rowDeleted=statement.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		if(rowDeleted==1) {
			try {
				this.employeeList=findAll();
			} catch (EmployeeNotFoundException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	@Override
	public List<String> getNameAndEmail(LocalDate date) throws EmployeeNotFoundException {
		List<String> employeesList = new ArrayList<>();
        Employee employee=null;
        String query = "SELECT first_name,email_address FROM employee_table WHERE MONTH(date_of_birth)=? AND DAY(date_of_birth)=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            Date dateOfBirth = Date.valueOf(date);
            statement.setInt(1, dateOfBirth.getMonth()+1);
            statement.setInt(2, dateOfBirth.getDate());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                employee = mapRowToObject(resultSet,"NameAndEmail");
                employeesList.add("Employee Name : "+employee.getFirstName()+" Employee Email : "+employee.getEmailAddress());
                while (resultSet.next()) {
                    employee = mapRowToObject(resultSet,"NameAndEmail");
                    employeesList.add("Employee Name : "+employee.getFirstName()+" Employee Email : "+employee.getEmailAddress());
                }
            } else {
                throw new EmployeeNotFoundException("ERR-106","No Employees have been found with the given date of birth : " + dateOfBirth);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeesList;
	}

	@Override
	public List<String> getNameAndPhoneNumber(LocalDate date) throws EmployeeNotFoundException {
		List<String> employeesList = new ArrayList<>();
        Employee employee=null;
        String sql = "select first_name,phone_number from employee_table where MONTH(wedding_date)=? AND DAY(wedding_date)=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            Date weddingDate = Date.valueOf(date);
            statement.setInt(1, weddingDate.getMonth()+1);
            statement.setInt(2, weddingDate.getDate());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                employee = mapRowToObject(resultSet,"NameAndPhoneNumber");
                employeesList.add("Employee Name : "+employee.getFirstName()+" Employee Phone number : "+employee.getPhoneNumber());
                while (resultSet.next()) {
                	employee = mapRowToObject(resultSet,"NameAndPhoneNumber");
                	employeesList.add("Employee Name : "+employee.getFirstName()+" Employee Phone number : "+employee.getPhoneNumber());
                }
            } else {
                throw new EmployeeNotFoundException("ERR-107","No Employees have been found with the given date of birth: " + weddingDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeesList;
	}
	
	private Employee mapRowToObject(ResultSet resultSet,String type) throws SQLException{
		if(type.equals("all")) {
			String firstName=resultSet.getString("first_name");
			String lastName=resultSet.getString("last_name");
			String address=resultSet.getString("address");
			String emailAddress=resultSet.getString("email_address");
			long phoneNumber=resultSet.getLong("phone_number");
			LocalDate dateOfBirth=resultSet.getDate("date_of_birth").toLocalDate();
			LocalDate weddingDate=null;
			if(resultSet.getDate("wedding_date")!=null) {
				weddingDate=resultSet.getDate("wedding_date").toLocalDate();
			}
			return new Employee(firstName, lastName, address, emailAddress, phoneNumber, dateOfBirth, weddingDate);
		}
		else if(type.equals("NameAndPhoneNumber")) {
			String firstName=resultSet.getString("first_name");
			long phoneNumber=resultSet.getLong("phone_number");
			return new Employee(firstName, phoneNumber);
		}
		else{
			String firstName=resultSet.getString("first_name");
			String emailAddress=resultSet.getString("email_address");
			return new Employee(firstName, emailAddress);
		}
	}
	
	public List<Employee> findAll() throws EmployeeNotFoundException{
		this.employeeList.clear();
		String query="SELECT * FROM employee_table";
		Employee employee=null;
		try(PreparedStatement statement=connection.prepareStatement(query)){
			ResultSet resultSet=statement.executeQuery();
			if(resultSet.next()) {
				employee=mapRowToObject(resultSet,"all");
				this.employeeList.add(employee);
				while(resultSet.next()) {
					employee=mapRowToObject(resultSet,"all");
					this.employeeList.add(employee);
				}
			}
			else {
				throw new EmployeeNotFoundException("ERR-103", "No Employee is found");
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return this.employeeList;
	}

}
