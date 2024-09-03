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


