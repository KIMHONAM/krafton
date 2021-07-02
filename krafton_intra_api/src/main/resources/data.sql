INSERT INTO department  (department_code, department_name, create_user)  values( 'DEPT000001','인프라플랫폼팀',1);

INSERT INTO employee_role (role_code,role_name,sort_order,create_user) values('ROLE00001','본부장',1,1);
INSERT INTO employee_role (role_code,role_name,sort_order,create_user) values('ROLE00002','팀장',2,1);
INSERT INTO employee_role (role_code,role_name,sort_order,create_user) values('ROLE00003','팀원',3,1);

INSERT INTO employee (employee_number,department_code,name,position_id, hire_date, password, create_user) values ('K0001','DEPT000001','홍길동',3, '2021-01-01 00:00:00',1,1);

INSERT INTO employee_pto_summary (employee_id, occur_days, use_days, unused_days, create_user) values (1,15.0,3.5,11.5,1);

INSERT INTO employee_pto_history (employee_id, start_date, end_date, reason, status, pto_type, pto_days, applicate_date, applicant, approver, approve_date, create_user) values(1,'2021-06-04 00:00:00','2021-06-05 00:00:00','개인 사유', 'PTOS000001', 'PTOT000001', 2, '2021-06-02 00:00:00', 1,1,'2021-06-02 00:00:00',1);

INSERT INTO common_code (code,code_name,sort_order,create_user) values ('PTOS000001','신청',1,1);
INSERT INTO common_code (code,code_name,sort_order,create_user) values ('PTOS000002','취소',2,1);
INSERT INTO common_code (code,code_name,sort_order,create_user) values ('PTOT000001','연차',1,1);
INSERT INTO common_code (code,code_name,sort_order,create_user) values ('PTOT000002','오전반차',2,1);
INSERT INTO common_code (code,code_name,sort_order,create_user) values ('PTOT000003','오후반차',3,1);

INSERT INTO holiday_info (holiday,holiday_detail)values ('20210101', '신정');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20210211', '설날');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20210212', '설날');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20210213', '설날');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20210301', '삼일절');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20210505', '어린이날');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20210519', '부처님오신날');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20210606', '현충일');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20210815', '광복절');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20210920', '추석');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20210921', '추석');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20210922', '추석');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20211003', '개천절');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20211009', '한글날');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20211225', '성탄절');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220101', '신정');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220131', '설날');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220201', '설날');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220202', '설날');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220301', '삼일절');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220309', '대통령선거일');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220505', '어린이날');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220508', '부처님오신날');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220601', '지방선거');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220606', '현충일');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220815', '광복절');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220909', '추석');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220910', '추석');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220911', '추석');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20220912', '대체공휴일');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20221003', '개천절');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20221009', '한글날');
INSERT INTO holiday_info (holiday,holiday_detail)values ('20221225', '성탄절');