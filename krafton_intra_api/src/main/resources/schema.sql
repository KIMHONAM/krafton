CREATE TABLE IF NOT EXISTS department (

	department_code  VARCHAR(10) NOT NULL COMMENT '부서 아이디',
	department_name  VARCHAR(100) NOT NULL COMMENT '부서명',
	create_date DATETIME NOT NULL DEFAULT NOW() COMMENT '데이터생성일',
    create_user INT(10) NOT NULL COMMENT '생성자',
    update_date DATETIME NULL COMMENT '수정일',
    update_user INT(10) NULL COMMENT '수정자',

	PRIMARY KEY (department_code)
)DEFAULT CHARSET=utf8 COMMENT='부서 정보 테이블';


CREATE TABLE IF NOT EXISTS employee_role (

	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '직책 아이디',
	role_code  VARCHAR(10) NOT NULL COMMENT '직책코드',
	role_name  VARCHAR(100) NOT NULL COMMENT '직책명',
	sort_order SMALLINT(5) UNSIGNED  NULL DEFAULT NULL COMMENT '정렬순서',
	create_date DATETIME NOT NULL DEFAULT NOW() COMMENT '데이터생성일',
    create_user INT(10) NOT NULL COMMENT '생성자',
    update_date DATETIME NULL COMMENT '수정일',
    update_user INT(10) NULL COMMENT '수정자',

	PRIMARY KEY (id)
)AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='직책 정보 테이블';

CREATE TABLE IF NOT EXISTS employee (

	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '아이디',
	employee_number VARCHAR(20) NOT NULL COMMENT '사번',
	department_code  VARCHAR(10)  NOT NULL COMMENT '부서코드',
	name VARCHAR(50) NOT NULL COMMENT '이름',
	position_id INT(10) NOT NULL COMMENT '직책코드',
	hire_date DATETIME NOT NULL COMMENT '입사일',
	password  VARCHAR(200) NOT NULL COMMENT '패스워드',
	create_date DATETIME NOT NULL DEFAULT NOW() COMMENT '데이터생성일',
    create_user INT(10) NOT NULL COMMENT '생성자',
    update_date DATETIME NULL COMMENT '수정일',
    update_user INT(10) NULL COMMENT '수정자',

	PRIMARY KEY (id)
) AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='사원 정보 테이블';
CREATE UNIQUE INDEX EMPLOYEE_UNIQUE_IDX ON employee (employee_number);

CREATE TABLE IF NOT EXISTS employee_pto_summary (

	employee_id INT(10) NOT NULL COMMENT '휴가자',
	occur_days FLOAT(4) NOT NULL COMMENT '발생연차',
	use_days FLOAT(4) NOT NULL COMMENT '사용연차',
	unused_days FLOAT(4) NOT NULL COMMENT '잔여연차',
	create_date DATETIME NOT NULL DEFAULT NOW() COMMENT '데이터생성일',
    create_user INT(10) NOT NULL COMMENT '생성자',
    update_date DATETIME NULL COMMENT '수정일',
    update_user INT(10) NULL COMMENT '수정자',

	PRIMARY KEY (employee_id)
) DEFAULT CHARSET=utf8 COMMENT='휴가 현황 테이블';

CREATE TABLE IF NOT EXISTS employee_pto_history (

	id VARCHAR(30) COMMENT '신청내역 키',
	employee_id INT(10) UNSIGNED NOT NULL COMMENT '휴가자 아이디',
	start_date DATETIME NOT NULL COMMENT '시작일',
	end_date DATETIME NOT NULL COMMENT '종료일',
	reason VARCHAR(200) NULL COMMENT '휴가 사유',
	status VARCHAR(10) NOT NULL COMMENT '상태 - 승인/취소',
	cancel_yn VARCHAR(1) NOT NULL DEFAULT 'N' COMMENT '취소여부',
	pto_type VARCHAR(10) NOT NULL COMMENT '반차/연차',
	pto_days FLOAT(4) NOT NULL COMMENT '신청 일수',
	deduct_yn varchar(1) NOT NULL DEFAULT 'Y' COMMENT '공제 여부',
	applicate_date DATETIME NOT NULL COMMENT '신청일',
	applicant INT(10) NOT NULL COMMENT '신청자',
	approver INT(10) NULL COMMENT '최종 승인자',
	approve_date DATETIME NULL COMMENT '승인일자',
	reject_date DATETIME NULL COMMENT '반려일자',
	reject_reason VARCHAR(200) NULL COMMENT '반려사유',
	rejector INT(10) NULL COMMENT '휴가 반려인',
	cancel_date DATETIME NULL COMMENT '취소일자',
	cancel_reason VARCHAR(200) NULL COMMENT '취소 사유',
	retractor INT(10) NULL COMMENT '취소인',
	create_date DATETIME NOT NULL DEFAULT NOW() COMMENT '데이터생성일',
	create_user INT(10) NOT NULL COMMENT '생성자',
	update_date DATETIME NULL COMMENT '수정일',
	update_user INT(10) NULL COMMENT '수정자',

	PRIMARY KEY (id)
) DEFAULT CHARSET=utf8 COMMENT='휴가 신청 내역 테이블';

CREATE TABLE IF NOT EXISTS employee_pto_items (

	employee_id INT(10) UNSIGNED NOT NULL COMMENT '휴가자 아이디',
	pto_history_id VARCHAR(30) COMMENT '신청내역 키',
	pto_date DATE NOT NULL COMMENT '휴가일',
	pto_type VARCHAR(10) NOT NULL COMMENT '오전,오후반차/연차',
	create_date DATETIME NOT NULL DEFAULT NOW() COMMENT '데이터생성일',
	create_user INT(10) NOT NULL COMMENT '생성자',
	update_date DATETIME NULL COMMENT '수정일',
	update_user INT(10) NULL COMMENT '수정자',

	PRIMARY KEY (employee_id, pto_date, pto_type)
) DEFAULT CHARSET=utf8 COMMENT='휴가 신청 개별 내역 테이블';

CREATE TABLE IF NOT EXISTS common_code(

	code VARCHAR(10) NOT NULL COMMENT '코드',
	code_name VARCHAR(100) NOT NULL COMMENT '코드명',
	sort_order TINYINT(4) UNSIGNED DEFAULT NULL COMMENT '정렬 순서',
	create_date DATETIME NOT NULL DEFAULT NOW() COMMENT '데이터생성일',
    create_user INT(10) NOT NULL COMMENT '생성자',
    update_date DATETIME NULL COMMENT '수정일',
    update_user INT(10) NULL COMMENT '수정자',

	PRIMARY KEY(code)
)DEFAULT CHARSET=utf8 COMMENT='공통코드 테이블';


CREATE TABLE IF NOT EXISTS holiday_info(
    holiday DATE NOT NULL COMMENT '휴무일',
    holiday_detail VARCHAR(50) NOT NULL COMMENT '휴무일 설명',
    PRIMARY KEY(holiday)
)DEFAULT CHARSET=utf8 COMMENT='공휴일 정보';