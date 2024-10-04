
select * from VISIT_TIME
where VISIT_DATE between to_date('2024-09-15 16:00:00'  , 'YYYY-MM-DD HH24:MI:SS') and to_date('2024-09-15 16:30:00' , 'YYYY-MM-DD HH24:MI:SS');

create view doctor_shift_emp_view as
SELECT
    W.WORK_SHIFT_ID,
    D.DOCTOR_ID,
    D.NAME,
    D.FAMILY,
    D.EXPERTISE,
    W.SHIFT_DATE ,
    W.SHIFT_STARTING_TIME AS FINISHING_TIME,
    W.SHIFT_FINISHING_TIME AS STARTARTING_TIME

FROM
    DOCTOR D
        JOIN WORK_SHIFT W
             ON D.DOCTOR_ID=W.SHIFT_DOCTOR_ID;



CREATE OR REPLACE VIEW doctor_visit_emp_view AS
SELECT
    V.VISIT_TIME_ID,
    V.VISIT_WORK_SHIFT_ID,
    V.VISIT_PATIENT_ID,
    V.VISIT_PAYMENT_ID,
    V.VISIT_ROOM_NUMBER,
    V.VISIT_PRESCRIPTION_ID,
    V.VISIT_DATE,
    V.HOUR,
    V.MINUTE,
    V.VISIT_DURATION,
    V.ACTIVE,
    V.ACCESS_LEVEL,
    V.DOCTOR_ID AS VISIT_DOCTOR_ID,
    DSh.DOCTOR_ID AS SHIFT_DOCTOR_ID,
    DSh.NAME AS DOCTOR_NAME,
    DSh.FAMILY AS DOCTOR_FAMILY,
    DSh.EXPERTISE AS DOCTOR_EXPERTISE,
    DSh.SHIFT_DATE,
    DSh.FINISHING_TIME,
    DSh.STARTARTING_TIME
FROM
    doctor_shift_emp_view DSh
        LEFT JOIN VISIT_TIME V
                  ON DSh.WORK_SHIFT_ID = V.VISIT_WORK_SHIFT_ID;



CREATE VIEW PATIENT_PRESCRIPTION_EMP_VIEW AS
SELECT
    PR.*,
    P.NAME AS PATIENT_NAME,
    P.FAMILY AS PATINT_FAMILY,
    P.DISEASE AS PATIENT_DISEASE
FROM
    PATIENT P
        JOIN PRESCRIPTION PR
             ON P.PATIENT_ID=PR.PATIENT_ID;

CREATE VIEW DOCTOR_PRESCRIPTION_EMP_VIEW AS
SELECT
    PR.*,
    D.NAME AS DOCTOR_NAME,
    D.FAMILY DOCTOR_FAMILY,
    D.EXPERTISE AS DOCTOR_SKILL

FROM
    DOCTOR D
        JOIN PRESCRIPTION PR
             ON D.DOCTOR_ID=PR.DOCTOR_ID;

CREATE VIEW PATIENT_VISIT_EMP_VIEW AS
SELECT
    V.*,
    P.NAME AS PATIENT_NAME,
    P.FAMILY AS PATIENT_FAMILY,
    P.DISEASE AS PATIENT_DISEASE
FROM
    PATIENT P
        JOIN VISIT_TIME V
             ON P.PATIENT_ID=V.VISIT_PATIENT_ID;


