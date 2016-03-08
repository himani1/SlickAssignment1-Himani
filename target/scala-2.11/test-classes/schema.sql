
CREATE TABLE student(sname varchar(100),sid int PRIMARY KEY auto_increment);
CREATE TABLE subject(subname varchar(100),subid int PRIMARY KEY auto_increment);
CREATE TABLE studentsub(sid int,subid int);