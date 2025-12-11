-- 查询当前数据库所支持的存储引擎
show engines;

-- 创建MyISAM存储引擎的表
create table engine_myisam(
    id int,
    name varchar(10)
) engine = MyISAM;

show create table engine_myisam;

-- 创建MEMORY存储引擎的表
create table engine_memory(
    id int,
    name varchar(10)
) engine = MEMORY;

show create table engine_memory;

-- 创建INNODB存储引擎的表
create table engine_innodb(
    id int,
    name varchar(10)
) engine = InnoDB;

show create table engine_innodb;
