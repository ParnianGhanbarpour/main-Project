package reception.model.bl;

import reception.controllers.Exception.UserNotFoundException;
import reception.model.da.EmployeeDa;
import reception.model.entity.Employee;

import java.util.Optional;

public class EmployeeBl {

    public static void remove(int id)throws Exception{
        try(EmployeeDa employeeDa = new EmployeeDa()){
            employeeDa.remove(id);
        }
    }

    public static Employee findByUsernameAndPassword(String username, String password)throws Exception{
        try(EmployeeDa employeeDa = new EmployeeDa()){
            Optional<Employee> employee = employeeDa.findByUsernameAndPassword(username, password);

            if(employee.isPresent()){
                return employee.get();
            }else{
                throw new UserNotFoundException();
            }
        }
    }

}