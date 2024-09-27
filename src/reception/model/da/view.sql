create view doctor_shift_emp_view as
select DOCTOR.DOCTOR_ID as DOCTOR_ID,
       WORK_SHIFT.SHIFT_DATE as SHIFT_DATE
    from DOCTOR
join WORK_SHIFT on DOCTOR.DOCTOR_ID = WORK_SHIFT.SHIFT_DOCTOR_ID
join EMPLOYEE on EMPLOYEE.EMPLOYEE_ID = WORK_SHIFT.SHIFT_EMPLOYEE_ID;



select * from VISIT_TIME
where VISIT_DATE_TIME between to_date('2024-09-15 16:00:00') and to_date('2024-09-15 16:30:00');

create view patient_view as
    select PATIENT.PATIENT_ID as patientId
    from PATIENT
join VISIT_TIME on patient.patient_id = visit_time.VISIT_PATIENT_ID;

SELECT
    P.PATIENT_ID ,
    P.NAME,
    P.FAMILY,
    P.DISEASE,
    V.VISIT_TIME_ID,
    VISIT_DATE_TIME,
    VISIT_DURATION
FROM
    PATIENT P
        JOIN VISIT_TIME V
             ON P.PATIENT_ID=V.VISIT_PATIENT_ID;

create view doctor_shift_emp_view as
SELECT
    D.DOCTOR_ID,
    D.NAME,
    D.FAMILY,
    D.SKILL,
    W.WORK_SHIFT_ID,
    W.SHIFT_DATE

FROM
    DOCTOR D
        JOIN WORK_SHIFT W
             ON D.DOCTOR_ID=W.SHIFT_DOCTOR_ID;


create view doctor_visit_emp_view as
SELECT *
FROM
    doctor_shift_emp_view DSh
        JOIN VISIT_TIME V
             ON DSh.WORK_SHIFT_ID=V.VISIT_WORK_SHIFT_ID;

