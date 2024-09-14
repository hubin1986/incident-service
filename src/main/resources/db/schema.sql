create table if not exists `incident` (
	`id`       int  primary key auto_increment not null,  -- int is enough for 0 ,2 power 32 -1 ，h2 not support unsigned
	`title` char (50) not null,
	`description`  char(150) ,
	`create_time` datetime not null,
	`update_time` datetime
);



