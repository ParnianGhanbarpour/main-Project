create table PERSON(
                       username nvarchar2(20) primary key,
                       password nvarchar2(20) not null ,
                       nationalId nvarchar2(20) unique ,
                       name nvarchar2(20) not null,
                       family nvarchar2(20) not null,
                       phoneNumber nvarchar2(20) unique ,
                       active number(1),
                       access_level char(4) default '0000'
);
CREATE SEQUENCE PERSON_SEQ START WITH 1 INCREMENT BY 1;

create table PATIENT(
                       patient_id number primary key,
                       username nvarchar2(20) unique ,
                       password nvarchar2(20) not null,
                       national_id nvarchar2(20) unique ,
                       name nvarchar2(20) not null,
                       family nvarchar2(20) not null,
                       phone_number nvarchar2(20) unique ,
                       disease nvarchar2(20),
                       active number(1),
                       access_level char(4) default '0000'
);
CREATE SEQUENCE PATIENT_SEQ START WITH 1 INCREMENT BY 1;

create table DOCTOR(
                       doctor_id number primary key,
                       username nvarchar2(20) unique ,
                       password nvarchar2(20) not null,
                       national_id nvarchar2(20) unique ,
                       name nvarchar2(20) not null,
                       family nvarchar2(20) not null,
                       phone_number nvarchar2(20) unique ,
                       expertise nvarchar2(20),
                       active number(1),
                       access_level char(4) default '0000'
);

CREATE SEQUENCE DOCTOR_SEQ START WITH 1 INCREMENT BY 1;

create table EMPLOYEE(
                       employee_id number primary key,
                       username nvarchar2(20) unique ,
                       password nvarchar2(20) not null,
                       national_id nvarchar2(20) unique ,
                       name nvarchar2(20) not null,
                       family nvarchar2(20) not null,
                       phone_number nvarchar2(20) unique ,
                       active number(1),
                       access_level char(4) default '0000'
);
CREATE SEQUENCE EMPLOYEE_SEQ START WITH 1 INCREMENT BY 1;

create table PAYMENT(
                       payment_id number primary key,
                       payment_method nvarchar2(20),
                       payment_time nvarchar2(40),
                       payment_amount number,
                       active number(1),
                       access_level char(4) default '0000'
);
CREATE SEQUENCE PAYMENT_SEQ START WITH 1 INCREMENT BY 1;

create table PRESCRIPTION1(
                             prescription_id number primary key,
                             medicine_name nvarchar2(20),
                             drug_dose nvarchar2(20),
                             drug_duration nvarchar2(20),
                             explanation nvarchar2(50),
                             doctor_id number ,
                             patient_id number,
                             active number(1),
                             access_level char(12) default '000000000000'


);
CREATE SEQUENCE PRESCRIPTION_SEQ START WITH 1 INCREMENT BY 1;

create table ROOMS(
                       room_number number primary key,
                       room_location nvarchar2(20),
                       equipments nvarchar2(20)
);
CREATE SEQUENCE ROOMS_SEQ START WITH 1 INCREMENT BY 1;

create table WORK_SHIFT(
                           Work_Shift_Id number primary key ,
                           Shift_Doctor_Id number unique ,
                           Shift_Employee_Id number unique ,
                           Shift_Date date ,
                           Shift_Starting_Time nvarchar2(20),
                           Shift_Finishing_Time nvarchar2(20)
);
CREATE SEQUENCE WORK_SHIFT_SEQ START WITH 1 INCREMENT BY 1;

create table VISIT_TIME(
                           Visit_Time_Id number primary key ,
                           Visit_Work_Shift_Id number unique ,
                           Visit_Patient_Id number unique ,
                           Visit_Payment_Id number unique ,
                           Visit_Room_Number number unique ,
                           Visit_Prescription_Id number unique ,
                           Visit_Date date ,
                           hour number,
                           minute number,
                           Visit_Duration nvarchar2(20) not null,
                           active number(1),
                           access_level char(14) default '00000000000000'
);

CREATE SEQUENCE VISIT_TIME_SEQ START WITH 1 INCREMENT BY 1;
