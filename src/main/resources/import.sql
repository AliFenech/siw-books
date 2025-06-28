

insert into book (id, title, year, imagePath) values(nextval('book_seq'), '1984', 1949, '1984.jpg');








insert into users(id, name, surname,password, email) values(1,'alice','fenech','alice','ali.fenech@stud.uniroma3.it');
insert into credentials(id, password, role, username, user_id) values(1,'$2a$10$BNQlmU069TTM2rYzOh83Y.x4MrTX/gMv77zAGCarxSSv0X4a13HdC','ADMIN','alice',1);