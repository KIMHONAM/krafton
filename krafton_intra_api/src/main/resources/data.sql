INSERT INTO department  (department_code, department_name, create_user)  values( 'DEPT000001','인프라플랫폼팀',1);

INSERT INTO employee_role (role_code,role_name,sort_order,create_user) values('ROLE00001','본부장',1,1);
INSERT INTO employee_role (role_code,role_name,sort_order,create_user) values('ROLE00002','팀장',2,1);
INSERT INTO employee_role (role_code,role_name,sort_order,create_user) values('ROLE00003','팀원',3,1);

INSERT INTO employee (employee_number,department_code,name,position_id, hire_date, password, create_user) values ('K0001','DEPT000001','홍길동',3, '2021-01-01 00:00:00',1,1);

INSERT INTO employee_pto_summary (employee_id, occur_pto, use_pto, unused_pto, create_user) values (1,15.0,3.5,11.5,1);

INSERT INTO employee_pto_history (employee_id, start_date, end_date, reason, status, pto_type, pto_days, applicate_date, applicant, candidate, approver, approve_date, create_user) values(1,'2021-06-04 00:00:00','2021-06-05 00:00:00','개인 사유', 'PTOS000001', 'PTOT000001', 2, '2021-06-02 00:00:00', 1,1,1,'2021-06-02 00:00:00',1);

INSERT INTO common_code (code,code_name,sort_order,create_user) values ('PTOS000001','신청',1,1);
INSERT INTO common_code (code,code_name,sort_order,create_user) values ('PTOS000002','취소',2,1);
INSERT INTO common_code (code,code_name,sort_order,create_user) values ('PTOT000001','연차',1,1);
INSERT INTO common_code (code,code_name,sort_order,create_user) values ('PTOT000002','오전반차',2,1);
INSERT INTO common_code (code,code_name,sort_order,create_user) values ('PTOT000003','오후반차',3,1);

