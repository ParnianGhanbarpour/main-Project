package reception.model.tools;

import reception.model.entity.Doctor;
import reception.model.entity.Employee;
import reception.model.entity.Patient;
import reception.model.entity.Person;

public class LoginManager {

        public Person authenticate(String username, String password) throws Exception {

            if ("doctorUser".equals(username) && "doctorPass".equals(password)) {
                return new Doctor();
            } else if ("patientUser".equals(username) && "patientPass".equals(password)) {
                return new Patient();
            } else if ("employeeUser".equals(username) && "employeePass".equals(password)) {
                return new Employee();
            } else {
                throw new Exception("Invalid username or password.");
            }
        }


}
