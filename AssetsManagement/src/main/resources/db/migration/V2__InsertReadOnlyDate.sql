-- Read Only Data

Insert into status values (null,"Good"),(null,"Bad"),(null,"Reparing");
Insert into type values (null,"Electronic"),(null,"Water"),(null,"Furniture");
Insert into role values (null,"ROLE_ADMIN"),(null,"ROLE_MANAGER");

INSERT INTO `assetmanagement`.`user`
(`id`,
`email`,
`name`,
`phone`,
`role_id`,
`store_id`)
VALUES
(null,
"adminbreak@gmail.com",
"Break",
"0909111111",
1,
null);

INSERT INTO `assetmanagement`.`account`
(`id`,
`is_enabled`,
`password`,
`username`,
`user_id`)
VALUES
(null,
1,
"$2a$10$GNbjopOpDvY7N4F4FMOtWeTRPrLAuMK7lu56Yq6BaMEGwfloElbCq",
"admin",
1);