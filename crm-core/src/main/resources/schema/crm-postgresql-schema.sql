/****** Object: Table crm_user ******/
CREATE TABLE crm_user (
	id SERIAL,
	username varchar(32) NOT NULL,
	password varchar(32) NOT NULL,
	first_name varchar(32) NULL,
	last_name varchar(32) NULL,
	email_address varchar(64) NOT NULL,
	administrator boolean NOT NULL DEFAULT false,
	CONSTRAINT pk_crm_user PRIMARY KEY (id),
	CONSTRAINT ix_crm_user_username UNIQUE (username)
);

/****** Object: Table crm_object ******/
CREATE TABLE crm_object (
	id SERIAL,
	object_name varchar(64) NOT NULL,
	table_name varchar(64) NOT NULL,
	CONSTRAINT pk_crm_object PRIMARY KEY (id),
	CONSTRAINT ix_crm_object_object_name UNIQUE (object_name)
);

/****** Object: Table crm_field ******/
CREATE TABLE crm_field (
	id SERIAL,
	object_id integer NOT NULL,
	field_name varchar(64) NOT NULL,
	column_name varchar(64) NOT NULL,
	data_type smallint NOT NULL,
	data_type_ext integer NULL,
	required boolean NOT NULL DEFAULT false,
	list_index smallint NULL,
	view_index smallint NULL,
	CONSTRAINT pk_crm_field PRIMARY KEY (id),
	CONSTRAINT ix_crm_field_object_id_field_name UNIQUE (object_id, field_name),
	CONSTRAINT fk_crm_field_crm_object FOREIGN KEY (object_id) REFERENCES crm_object (id) ON DELETE CASCADE
);

/****** Object: Table crm_application ******/
CREATE TABLE crm_application (
	id SERIAL,
	application_name varchar(64) NOT NULL,
	view_index smallint NULL,
	CONSTRAINT pk_crm_application PRIMARY KEY (id),
	CONSTRAINT ix_crm_application_application_name UNIQUE (application_name)
);

/****** Object: Table crm_application_object ******/
CREATE TABLE crm_application_object (
	application_id integer NOT NULL,
	object_id integer NOT NULL,
	view_index smallint NULL,
	CONSTRAINT pk_crm_application_object PRIMARY KEY (application_id, object_id),
	CONSTRAINT fk_crm_application_object_crm_application FOREIGN KEY (application_id) REFERENCES crm_application (id) ON DELETE CASCADE,
	CONSTRAINT fk_crm_application_object_crm_object FOREIGN KEY (object_id) REFERENCES crm_object (id) ON DELETE CASCADE
);

/****** Object: Table crm_relationship ******/
CREATE TABLE crm_relationship (
	id SERIAL,
	parent_object_id integer NOT NULL,
	child_object_id integer NOT NULL,
	view_index smallint NULL,
	table_name varchar(64) NOT NULL,
	CONSTRAINT pk_crm_relationship PRIMARY KEY (id),
	CONSTRAINT fk_crm_relationship_crm_object_1 FOREIGN KEY (parent_object_id) REFERENCES crm_object (id),
	CONSTRAINT fk_crm_relationship_crm_object_2 FOREIGN KEY (child_object_id) REFERENCES crm_object (id)
);

/****** Object: Table crm_option_list ******/
CREATE TABLE crm_option_list(
	id SERIAL,
	option_list_name varchar(64) NOT NULL,
	CONSTRAINT pk_crm_option_list PRIMARY KEY (id) 
);

/****** Object: Table crm_option_list_item ******/
CREATE TABLE crm_option_list_item (
	id SERIAL,
	option_list_id integer NOT NULL,
	item_value varchar(64) NOT NULL,
	view_index smallint NULL,
	CONSTRAINT pk_crm_option_list_item PRIMARY KEY (id),
	CONSTRAINT fk_crm_option_list_item_crm_option_list FOREIGN KEY (option_list_id) REFERENCES crm_option_list (id) ON DELETE CASCADE
);

/****** Object: Table crm_file ******/
CREATE TABLE crm_file (
	id SERIAL,
	file_name varchar(255) NOT NULL,
	file_size integer NOT NULL,
	mime_type varchar(255) NOT NULL,
	content bytea,
	CONSTRAINT pk_crm_file PRIMARY KEY (id)
);

/****** Data: default user 'admin' ******/
INSERT INTO crm_user (username, password, first_name, email_address, administrator)
VALUES ('admin', 'admin', 'Administrator', 'admin@localhost', true);
