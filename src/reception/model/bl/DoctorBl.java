package reception.model.bl;

import reception.controllers.Exception.UserNotFoundException;
import reception.model.da.DoctorDa;
import reception.model.entity.Doctor;

import java.util.Optional;

public class DoctorBl extends Exception{
    public static void remove(int id)throws Exception{
        try(DoctorDa doctorDa = new DoctorDa()){
            doctorDa.remove(id);
        }
    }

    public static Doctor findByUsernameAndPassword(String username, String password)throws Exception{
        try(DoctorDa doctorDa = new DoctorDa()){
            Optional<Doctor> doctor = doctorDa.findByUsernameAndPassword(username, password);

            if(doctor.isPresent()){
                return doctor.get();
            }else{
                throw new UserNotFoundException();
            }
        }
    }
}
