package reception.model.utils;

import java.util.regex.Pattern;

public class Validation {

    public  String NameValidator(String name) throws Exception {
        if(Pattern.matches("^[a-zA-Z\\s]{2,30}$", name)){
            return name;
        }else{
            throw new Exception("Invalid Name");
        }
    }

    public  String familyValidator(String family) throws Exception {
        if(Pattern.matches("^[a-zA-Z\\s]{2,30}$", family)){
            return family;
        }else{
            throw new Exception("Invalid family");
        }
    }
    public  String nationalIdValidator(String nationalId) throws Exception {
        if(Pattern.matches("^[0-9]{10}$", nationalId)){
            return nationalId;
        }else{
            throw new Exception("Invalid national ID");
        }
    }
    public  String phoneNumberValidator(String phoneNumber) throws Exception {
        if(Pattern.matches("^[0-9]{11}$", phoneNumber)){
            return phoneNumber;
        }else{
            throw new Exception("Invalid phone number");
        }
    }

    public  String diseaseValidator(String disease) throws Exception {
        if(Pattern.matches("^[a-zA-Z\\s]{2,30}$", disease)){
            return disease;
        }else{
            throw new Exception("Invalid disease");
        }
    }

    public String paymentAmountValidator(String amount) throws Exception {
        if(Pattern.matches("^[0-9]*\\.?[0-9]+$", amount)){
            return amount;
        }else{
            throw new Exception("Invalid Amount");
        }
    }

    public  String DateAndTimeValidator(String time) throws Exception {
        if(Pattern.matches("^[0-9]{4}-[0-9]{2}-[0-9]-{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$", time)){
            return time;
        }else{
            throw new Exception("Invalid Date and Time");
        }
    }
}