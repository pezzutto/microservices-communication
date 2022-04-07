insert into CATEGORY (id, name) values (nextval('category_seq'), 'Comic Books');
insert into CATEGORY (id, name) values (nextval('category_seq'), 'Movies');
insert into CATEGORY (id, name) values (nextval('category_seq'), 'Books');

insert into SUPPLIER (id, name) values (nextval('supplier_seq'), 'Panini Comics');
insert into SUPPLIER (id, name) values (nextval('supplier_seq'), 'Amazon');

insert into PRODUCT (id, name, category_id, supplier_id, available_qty, created_at) values (nextval('product_seq'), 'Crises nas infinitas terras', 1, 1, 10, current_timestamp);
insert into PRODUCT (id, name, category_id, supplier_id, available_qty, created_at) values (nextval('product_seq'), 'Interestelar', 2, 2, 5, current_timestamp);
insert into PRODUCT (id, name, category_id, supplier_id, available_qty, created_at) values (nextval('product_seq'), 'Harry Potter e a pedra filosofal', 3, 2, 3, current_timestamp);



