package reception.model.bl;

import reception.controllers.Exception.UserNotFoundException;
import reception.model.da.PatientDa;
import reception.model.entity.Patient;

import java.util.Optional;

public class PatientBl {
    public static void remove(int id)throws Exception{
        try(PatientDa patientDa = new PatientDa()){
            patientDa.remove(id);
        }
    }

    public static Patient findByUsernameAndPassword(String username, String password)throws Exception{
        try(PatientDa patientDa = new PatientDa()){
            Optional<Patient> patient = patientDa.findByUsernameAndPassword(username, password);

            if(patient.isPresent()){
                return patient.get();
            }else{
                throw new UserNotFoundException();
            }
        }
    }
}
