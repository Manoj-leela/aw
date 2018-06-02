--#V1.1a
-- 1.11
ALTER TABLE `redemption` 
CHANGE COLUMN `redemption_amount` `redemption_amount` DECIMAL(15,5) NULL DEFAULT NULL ,
CHANGE COLUMN `fees` `fees` DECIMAL(15,5) NULL DEFAULT NULL ;

delete from QRTZ_CRON_TRIGGERS where TRIGGER_NAME = 'closeOrderRequestJobTrigger';
delete from QRTZ_CRON_TRIGGERS where TRIGGER_NAME = 'placeOrderRequestJobTrigger';

delete from QRTZ_TRIGGERS where TRIGGER_NAME = 'closeOrderRequestJobTrigger';
delete from QRTZ_TRIGGERS where TRIGGER_NAME = 'placeOrderRequestJobTrigger';

ALTER TABLE `remittance` 
CHANGE COLUMN `remittance_amount` `remittance_amount` DECIMAL(15,5) NULL DEFAULT NULL ;

update instrument set current_rate = 1 where code = 'CASH';

ALTER TABLE `instrument` 
CHANGE COLUMN `current_rate` `current_rate` DECIMAL(15,5) NULL DEFAULT NULL ;

ALTER TABLE `remittance` 
CHANGE COLUMN `fees` `fees` DECIMAL(15,5) NULL DEFAULT NULL ;

ALTER TABLE `user_portfolio` 
CHANGE COLUMN `fees` `fees` DECIMAL(15,5) NULL DEFAULT NULL ;

ALTER TABLE `user_portfolio_transaction` 
CHANGE COLUMN `fund_amount` `fund_amount` DECIMAL(15,5) NULL DEFAULT NULL ;

ALTER TABLE `user_trade` 
CHANGE COLUMN `entered_price` `entered_price` DECIMAL(15,5) NULL DEFAULT NULL ,
CHANGE COLUMN `current_price` `current_price` DECIMAL(15,5) NULL DEFAULT NULL ,
CHANGE COLUMN `close_price` `close_price` DECIMAL(15,5) NULL DEFAULT NULL ,
CHANGE COLUMN `fees` `fees` DECIMAL(15,5) NULL DEFAULT NULL ;

ALTER TABLE `remittance` 
ADD COLUMN `investor_remittance_issue_email_sent` DATETIME NULL DEFAULT NULL AFTER `remarks`;

ALTER TABLE `redemption` ADD COLUMN `redemption_completed_email_sent` DATETIME NULL DEFAULT NULL AFTER `remarks`;

-- 1.12
ALTER TABLE `user` CHANGE COLUMN `residence_country` `residence_country` int(5) DEFAULT NULL;



