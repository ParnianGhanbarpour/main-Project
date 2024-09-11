create table PERSON(
                       username nvarchar2(20) primary key,
                       password nvarchar2(20) not null,
                       nationalId nvarchar2(20) not null ,
                       name nvarchar2(20) not null,
                       family nvarchar2(20) not null,
                       phoneNumber nvarchar2(20) not null ,
                       active number(1),
                       access_level char(4) default '0000'
);
CREATE SEQUENCE PERSON_SEQ START WITH 1 INCREMENT BY 1;

create table PATIENT(

                       username nvarchar2(20) primary key,
                       password nvarchar2(20) not null,
                       nationalId nvarchar2(20) not null ,
                       name nvarchar2(20) not null,
                       family nvarchar2(20) not null,
                       phoneNumber nvarchar2(20) not null ,
                       disease nvarchar2(20),
                       active number(1),
                       access_level char(4) default '0000'
);
CREATE SEQUENCE PATIENT_SEQ START WITH 1 INCREMENT BY 1;

create table DOCTOR(
                       username nvarchar2(20) primary key,
                       password nvarchar2(20) not null,
                       nationalId nvarchar2(20) not null ,
                       name nvarchar2(20) not null,
                       family nvarchar2(20) not null,
                       phoneNumber nvarchar2(20) not null ,
                       skill nvarchar2(20),
                       active number(1),
                       access_level char(4) default '0000'
);
CREATE SEQUENCE DOCTOR_SEQ START WITH 1 INCREMENT BY 1;

create table EMPLOYEE(
                       username nvarchar2(20) primary key,
                       password nvarchar2(20) not null,
                       nationalId nvarchar2(20) not null ,
                       name nvarchar2(20) not null,
                       family nvarchar2(20) not null,
                       phoneNumber nvarchar2(20) not null ,
                       workDepartment nvarchar2(20),
                       active number(1),
                       access_level char(4) default '0000'
);
CREATE SEQUENCE EMPLOYEE_SEQ START WITH 1 INCREMENT BY 1;

create table PAYMENT(
                       id number primary key,
                       payment_method nvarchar2(20),
                       payment_time date,
                       payment_amount number
);
CREATE SEQUENCE PAYMENT_SEQ START WITH 1 INCREMENT BY 1;

create table PRESCRIPTION(
                       id number primary key,
                       medicine_name nvarchar2(20),
                       drug_dose nvarchar2(20),
                       duration nvarchar2(20),
                       explanation nvarchar2(50)
);
CREATE SEQUENCE PRESCRIPTION_SEQ START WITH 1 INCREMENT BY 1;

create table ROOMS(
                       room_number number primary key,
                       room_location nvarchar2(20),
                       equipments nvarchar2(20)
);
CREATE SEQUENCE ROOMS_SEQ START WITH 1 INCREMENT BY 1;

create TABLE WORK_SHIFT(
                           Work_Shift_Id number primary key ,
                           Shift_Doctor_Id number unique ,
                           Shift_Employee_Id number unique ,
                           Shift_Date timestamp ,
                           Shift_Starting_Time timestamp,
                           Shift_Finishing_Time timestamp
);
CREATE SEQUENCE Work_Shift_SEQ START WITH 1 INCREMENT BY 1;

create TABLE Visit_Time(
                           Visit_Time_Id number primary key ,
                           Visit_Work_Shift_Id number not null ,
                           Visit_Patient_Id number not null,
                           Visit_Payment_Id number not null,
                           Visit_Room_Number number not null,
                           Visit_Prescription_Id number not null,
                           Visit_Date_Time timestamp,
                           Visit_Duration nvarchar2(20) not null
);

CREATE SEQUENCE Visit_Time_SEQ START WITH 1 INCREMENT BY 1;