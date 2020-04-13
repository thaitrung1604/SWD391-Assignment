    create table account (
       id bigint not null auto_increment,
        is_enabled bit not null,
        password varchar(255),
        username varchar(255),
        user_id bigint not null,
        primary key (id)
    ) engine=InnoDB;
    
    create table asset (
       id bigint not null auto_increment,
        description varchar(255),
        expiry_warranty_date datetime(6),
        name varchar(255),
        next_warranty_date datetime(6),
        price double precision not null,
        purchase_date datetime(6),
        department_id bigint,
        status_id bigint,
        store_id bigint,
        supplier_id bigint,
        type_id bigint,
        user_id bigint,
        primary key (id)
    ) engine=InnoDB;
    
    create table department (
       id bigint not null auto_increment,
        email varchar(255),
        name varchar(255),
        phone varchar(255),
        primary key (id)
    ) engine=InnoDB;
    
    create table history (
       id bigint not null auto_increment,
        create_by bigint,
        date datetime(6),
        department_id bigint,
        expiry_warranty_date datetime(6),
        next_warranty_date datetime(6),
        status_id bigint,
        store_id bigint,
        user_id bigint,
        asset_id bigint,
        primary key (id)
    ) engine=InnoDB;
    
    create table minute_of_handover (
       id bigint not null auto_increment,
        create_by bigint,
        date datetime(6),
        asset_id bigint,
        current_user_id bigint,
        previous_user_id bigint,
        primary key (id)
    ) engine=InnoDB;
    
    create table role (
       id bigint not null auto_increment,
        name varchar(255),
        primary key (id)
    ) engine=InnoDB;
    
    create table status (
       id bigint not null auto_increment,
        name varchar(255),
        primary key (id)
    ) engine=InnoDB;
    
    create table store (
       id bigint not null auto_increment,
        address varchar(255),
        name varchar(255),
        phone varchar(255),
        primary key (id)
    ) engine=InnoDB;
    
    create table supplier (
       id bigint not null auto_increment,
        email varchar(255),
        name varchar(255),
        phone varchar(255),
        primary key (id)
    ) engine=InnoDB;
    
    create table type (
       id bigint not null auto_increment,
        name varchar(255),
        primary key (id)
    ) engine=InnoDB;
    
    create table user (
       id bigint not null auto_increment,
        email varchar(255),
        name varchar(255),
        phone varchar(255),
        role_id bigint,
        store_id bigint,
        primary key (id)
    ) engine=InnoDB;
    
    alter table account 
       add constraint UK_h6dr47em6vg85yuwt4e2roca4 unique (user_id);
    
    alter table account 
       add constraint UK_gex1lmaqpg0ir5g1f5eftyaa1 unique (username);
    
    alter table department 
       add constraint UK_3x9ey8sma05yo5jaip7xwpnnr unique (email);
    
    alter table department 
       add constraint UK_1t68827l97cwyxo9r1u6t4p7d unique (name);
    
    alter table department 
       add constraint UK_ot9v7cs92wnxmg2cmrepttnoy unique (phone);
    
    alter table store 
       add constraint UK_6449ovkajl8faucijt8aw1xq6 unique (address);
    
    alter table store 
       add constraint UK_d0p5ly1cv6guij7sq1mbnr8ec unique (name);
    
    alter table store 
       add constraint UK_t628hr0thqcknea2eb14y58jp unique (phone);
    
    alter table supplier 
       add constraint UK_g7qiwwu4vpciysmeeyme9gg1d unique (email);
    
    alter table supplier 
       add constraint UK_c3fclhmodftxk4d0judiafwi3 unique (name);
    
    alter table supplier 
       add constraint UK_odw8hcb1lettg4mqax263yyb5 unique (phone);
    
    alter table user 
       add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email);
    
    alter table user 
       add constraint UK_589idila9li6a4arw1t8ht1gx unique (phone);
    
    alter table account 
       add constraint FK7m8ru44m93ukyb61dfxw0apf6 
       foreign key (user_id) 
       references user (id);
    
    alter table asset 
       add constraint FKnpe7x7oel7k7arlms8bb94rnx 
       foreign key (department_id) 
       references department (id);
    
    alter table asset 
       add constraint FKh0hr0ht1icl555gkicoa2fhr6 
       foreign key (status_id) 
       references status (id);
    
    alter table asset 
       add constraint FK944e1t9793i8lh361slsvpwxx 
       foreign key (store_id) 
       references store (id);
    
    alter table asset 
       add constraint FKtkl1m8s9s42gsl23jkbrsvg26 
       foreign key (supplier_id) 
       references supplier (id);
    
    alter table asset 
       add constraint FKk5srvv6dv4d9arp4tke6yc5wu 
       foreign key (type_id) 
       references type (id);
    
    alter table asset 
       add constraint FKi2t8rfq8blfbh1rpvbxqrmgvd 
       foreign key (user_id) 
       references user (id);
    
    alter table history 
       add constraint FK3hbwl5qddnrr14871xe7xbgen 
       foreign key (asset_id) 
       references asset (id);
    
    alter table minute_of_handover 
       add constraint FKbw9iac824k69g42sh31m0b6fv 
       foreign key (asset_id) 
       references asset (id);
    
    alter table minute_of_handover 
       add constraint FKcxl9vybn5pn1gciau7cx1nhrt 
       foreign key (current_user_id) 
       references user (id);
    
    alter table minute_of_handover 
       add constraint FKok6tf4d5r16kcum7rd99t1yis 
       foreign key (previous_user_id) 
       references user (id);
    
    alter table user 
       add constraint FKn82ha3ccdebhokx3a8fgdqeyy 
       foreign key (role_id) 
       references role (id);
    
    alter table user 
       add constraint FK1k3x2kt9pxcmon8jhquv31qe4 
       foreign key (store_id) 
       references store (id);