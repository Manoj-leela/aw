--#V1.1b
-- 1.11
alter table asset_class_allocation change column `detail` `detail` varchar(1000) DEFAULT NULL;
alter table asset_class_allocation change column `description` `description` longtext DEFAULT NULL;
alter table asset_class_allocation change column `color` `color` varchar(20) default null;
alter table asset_class_allocation change column `type` `instrument_type` tinyint(2) DEFAULT NULL after display_name;

alter table cp_fund_account change column `fund` `cp_fund` tinyint(2) DEFAULT NULL;

alter table cp_fund_account_transacton change column `fund` `cp_fund` tinyint(2) DEFAULT NULL;
alter table cp_fund_account_transacton change column `transaction_type` `fund_transaction_type` tinyint(2) DEFAULT NULL;

alter table external_fund change column `version` `version` int(5) NOT NULL after `updated_on`;
alter table external_fund drop column etc;
--alter table external_fund_price change column `fund_id` `external_fund_id` varchar(32) NOT NULL after `id`;
ALTER TABLE `external_fund_price` DROP FOREIGN KEY `external_fund_price_ibfk_1`;
ALTER TABLE `external_fund_price` CHANGE COLUMN `fund_id` `external_fund_id` VARCHAR(32) NOT NULL ;
ALTER TABLE `external_fund_price` ADD CONSTRAINT `external_fund_price_ibfk_1` FOREIGN KEY (`external_fund_id`)  REFERENCES `external_fund` (`id`) ON DELETE CASCADE;

alter table external_fund_price change column `dealing` `dealing_price` tinyint(1) NOT NULL;

alter table instrument drop column instrument_id, drop column asset_type, drop column description;
alter table instrument change column `current_rate` `current_price` decimal(15,5) DEFAULT NULL;
alter table instrument change column `fees` `fees_per_trade_leg` decimal(5,5) DEFAULT NULL;
alter table instrument change column type instrument_type tinyint(2) not null;

alter table portfolio change column description description longtext default null;
alter table portfolio change column risk_profiling risk_profile tinyint(2) not null;
alter table portfolio change column assignment_category assignment_category tinyint(2) not null;
alter table portfolio change column status status tinyint(2) not null;

alter table portfolio_instrument change column position trade_position tinyint(2) not null;
alter table portfolio_instrument drop column display_weightage,drop column display_name;

alter table redemption change column `redemption_status` redemption_status tinyint(2);
alter table redemption change column `fees` `amount_received_fees` decimal(15,5) DEFAULT NULL after amount_from_broker;
alter table redemption change column remarks remarks longtext default null;
alter table redemption add column `net_redemption_amount` decimal(15,5) DEFAULT NULL after amount_received_fees;
alter table redemption change column amount_from_broker amount_requested_from_broker decimal(15,5) DEFAULT NULL;
alter table redemption add column `amount_received_from_broker` decimal(15,5) DEFAULT NULL after amount_requested_from_broker;

alter table remittance change column investor_remittance `investor_remittance_status` tinyint(2) DEFAULT NULL after remittance_slip_file_name;
alter table remittance change column remittance_amount `investor_remittance_remitted_amount` decimal(15,5) DEFAULT NULL after investor_remittance_status;
alter table remittance change column received_amount `investor_remittance_received_amount` decimal(15,5) DEFAULT NULL after investor_remittance_remitted_amount;
alter table remittance change column fees `investor_remittance_fees` decimal(15,5) DEFAULT NULL after investor_remittance_received_amount;
alter table remittance change column funding_status `broker_funding_status` tinyint(1) DEFAULT NULL after investor_remittance_fees;
alter table remittance change column broker_batch `broker_batch` date DEFAULT NULL after broker_funding_status;
alter table remittance add column  `broker_funding_received_amount` decimal(15,5) DEFAULT NULL after broker_batch;
alter table remittance add column  `broker_funding_fees` decimal(15,5) DEFAULT NULL after broker_funding_received_amount;
alter table remittance add column  `net_investment_amount` decimal(15,5) DEFAULT NULL after broker_funding_fees;
alter table remittance change column remarks remarks longtext default null;
alter table remittance drop column remittance_date;

alter table user change phone_number `mobile_number` varchar(50) DEFAULT NULL;
alter table user change progress_status `progress_status` tinyint(2) DEFAULT NULL after currency;
alter table user drop column `user_question_1`, drop column `user_question_2`, drop column `user_question_3`, drop column user_question_4, drop column goal, drop column customer_id, drop column joined_date;
alter table user change broker_api_key `broker_saxo_api_key` varchar(256) DEFAULT NULL;
alter table user change broker_api_secret `broker_saxo_api_secret` varchar(1000) DEFAULT NULL;
alter table user change column account_status account_status tinyint(2) default null;
alter table user change column agent_otp_status agent_otp_status tinyint(2) default null;
alter table user change column kyc_status kyc_status tinyint(2) default null;
alter table user change column bank_detail_status bank_details_status tinyint(2) default null;
alter table user change column agent_otp `agent_otp` varchar(10) DEFAULT NULL after agent_id;
alter table user change column bank_name `bank_details_bank_name` varchar(255) DEFAULT NULL;
alter table user change column bank_address `bank_details_bank_address` varchar(1000) DEFAULT NULL;
alter table user change column aba `bank_details_aba` varchar(100) DEFAULT NULL;
alter table user change column chips `bank_details_chips` varchar(100) DEFAULT NULL;
alter table user change column swift_number `bank_details_swift_number` varchar(100) DEFAULT NULL;
alter table user change column account_name `bank_details_account_name` varchar(100) DEFAULT NULL;
alter table user change column account_number `bank_details_account_number` varchar(100) DEFAULT NULL;
alter table user change column reference `bank_details_reference` varchar(100) DEFAULT NULL;
alter table user change column ac_credit_investor `declarations_ai` tinyint(2) DEFAULT NULL;
alter table user change column pep `declarations_pep` tinyint(2) DEFAULT NULL;
alter table user change column crc `declarations_crc` tinyint(2) DEFAULT NULL;
alter table user change column tax_crime `declarations_tax_crime` tinyint(2) DEFAULT NULL;
alter table user change column source_of_income `declarations_source_of_income` varchar(255) DEFAULT NULL;
alter table user change column signature_file_name declarations_signature_file_name varchar(255) DEFAULT NULL after declarations_source_of_income;
alter table user change column agree agreement_user_agreement tinyint(2) default null;
alter table user drop column charges;
alter table user change column residence_country `residence_country` int(5) DEFAULT NULL after mobile_number;
alter table user change verified `mobile_verified` tinyint(2) DEFAULT NULL after mobile_number;
alter table user change verified_timestamp `mobile_verified_timestamp` datetime DEFAULT NULL after mobile_verified;
alter table user change column annual_income `annual_income` decimal(15,5) DEFAULT NULL after residence_country;
alter table user change column currency currency tinyint(2) default null after residence_country;
alter table user change column has_acknowledged `agreement_user_agreement_acknowledged` tinyint(2) DEFAULT NULL after agreement_user_agreement;
alter table user change column `us_citizen` `declarations_us_citizen` tinyint(2) DEFAULT NULL after declarations_source_of_income;
alter table user change column kyc_remarks kyc_remarks longtext default null after kyc_status;
alter table user change column bank_detail_remarks bank_details_remarks longtext default null after bank_details_status;
alter table user change column bank_declaration_email_sent `bank_details_declarations_email_sent` datetime DEFAULT NULL;
alter table user change column bank_status_completed_email_sent `bank_details_declarations_status_completed_email_sent` datetime DEFAULT NULL;
alter table user change column isadmin `isadmin` tinyint(1) NOT NULL;
alter table user change column kyc1_file_name `kyc1_file_name` varchar(255) DEFAULT NULL after kyc_remarks;
alter table user change column kyc2_file_name `kyc2_file_name` varchar(255) DEFAULT NULL after kyc1_file_name;
alter table user change column kyc3_file_name `kyc3_file_name` varchar(255) DEFAULT NULL after kyc1_file_name;
alter table user change column declaration_status declarations_status tinyint(2) default null after bank_details_reference;
alter table user change column agent_id `agent_id` varchar(32) DEFAULT NULL after mobile_verified_timestamp;
alter table user change column agent_otp `agent_otp` varchar(10) DEFAULT NULL after agent_id;
alter table user change column agent_otp_status `agent_otp_status` tinyint(2) DEFAULT NULL after agent_otp;
alter table user change column hash_salt `hash_salt` varchar(64) DEFAULT NULL after password;
alter table user change column dob `date_of_birth` datetime DEFAULT NULL after annual_income;
alter table user change column last_logged_in_ip_address `last_logged_in_ip_address` varchar(64) DEFAULT NULL after hash_salt;
alter table user change column social_id `social_id` varchar(256) DEFAULT NULL after email;

ALTER TABLE `remittance` 
ADD COLUMN `investor_remittance_issue_email_sent` DATETIME NULL DEFAULT NULL AFTER `net_investment_amount`;
alter table remittance change column `funded_email_sent` broker_funded_email_sent datetime DEFAULT NULL;

ALTER TABLE `redemption` ADD COLUMN `redemption_completed_email_sent` DATETIME NULL DEFAULT NULL AFTER `net_redemption_amount`;

alter table user_messages change column receiver_user_id `receiver_user_id` varchar(32) NOT NULL after sender_user_id;

alter table user_portfolio drop column computed_profile, drop column final_profile;
alter table user_portfolio drop column user_profiling_question_1;
alter table user_portfolio drop column user_profiling_question_2;
alter table user_portfolio drop column user_profiling_question_3;
alter table user_portfolio drop column user_profiling_question_4;
alter table user_portfolio drop column user_profiling_question_5;
alter table user_portfolio drop column user_profiling_question_6;
alter table user_portfolio drop column user_profiling_question_7;
alter table user_portfolio drop column user_profiling_question_8;
alter table user_portfolio drop column user_profiling_answer_1;
alter table user_portfolio drop column user_profiling_answer_2;
alter table user_portfolio drop column user_profiling_answer_3;
alter table user_portfolio drop column user_profiling_answer_4;
alter table user_portfolio drop column user_profiling_answer_5;
alter table user_portfolio drop column user_profiling_answer_6;
alter table user_portfolio drop column user_profiling_answer_7;
alter table user_portfolio drop column user_profiling_answer_8;
alter table user_portfolio drop column target_date_withdrawal;
alter table user_portfolio change column reassign_portfolio_id `rebalance_target_portfolio_id` varchar(32) DEFAULT NULL;
alter table user_portfolio change column total_amount_invested `net_investment_amount` decimal(15,5) DEFAULT NULL after portfolio_id;
alter table user_portfolio change column net_uninvested_amount `total_uninvested_amount` decimal(15,5) DEFAULT NULL after net_investment_amount;
alter table user_portfolio change column realised_pnl `realised_pnl` decimal(15,5) default null after total_uninvested_amount;
alter table user_portfolio change column unrealised_pnl `unrealised_pnl` decimal(15,5) default null after realised_pnl;
alter table user_portfolio change column net_asset_value `net_asset_value` decimal(15,5) default null after unrealised_pnl;
alter table user_portfolio drop column fees;
alter table user_portfolio change column goal `goal` tinyint(2) DEFAULT NULL after portfolio_id;
alter table user_portfolio change column funding_status `portfolio_funding_status` varchar(255) DEFAULT NULL after goal;
alter table user_portfolio change column status `execution_status` tinyint(2) NOT NULL after portfolio_funding_status;

rename table user_portfolio_ques_ans to user_portfolio_question_and_answer;

alter table user_portfolio_transaction change column `fund_amount` amount decimal(15,5) DEFAULT NULL;
alter table user_portfolio_transaction change column status `status` tinyint(2) DEFAULT NULL;

alter table user_trade change column net_invested_amount `allocated_amount` decimal(15,5) DEFAULT NULL after portfolio_instrument_id;
alter table user_trade change column net_closed_amount `closed_amount` decimal(15,5) DEFAULT NULL after allocated_amount;
alter table user_trade change column status `execution_status` tinyint(2) NOT NULL;
alter table user_trade change column retry_count `retry_count` tinyint(1) NOT NULL;
alter table user_trade change column position `trade_position` tinyint(2) NOT NULL after closed_amount;
alter table user_trade change column close_price `closed_price` decimal(15,5) DEFAULT NULL after current_price;
alter table user_trade change column units `entered_units` decimal(10,2) DEFAULT NULL after entered_price;
alter table user_trade change column fees fees_both_trade_legs decimal(15,5) DEFAULT NULL after closed_price;
alter table user_trade change column `version` `version` int(5) NOT NULL after `updated_on`;
alter table user_trade change column current_price `current_price` decimal(15,5) DEFAULT NULL after trade_position;

alter table user_trade_transaction change column status status tinyint(2) default null;
alter table user_trade_transaction change column description description longtext not null;

alter table user_submission change column `version` `version` int(5) NOT NULL after `updated_on`;

-- 1.12
ALTER TABLE `user_trade` CHANGE COLUMN `entered_price` `entered_price` DECIMAL(15,5) NULL DEFAULT NULL ;
ALTER TABLE `user_trade` CHANGE COLUMN `entered_units` `entered_units` DECIMAL(10,5) NULL DEFAULT NULL ;
ALTER TABLE `user_trade` CHANGE COLUMN `trade_position` `trade_position` TINYINT(2) NOT NULL AFTER `allocated_amount`;
ALTER TABLE `user_trade` CHANGE COLUMN `entered_units` `entered_units` DECIMAL(10,5) NULL DEFAULT NULL AFTER `current_price`;
ALTER TABLE `user_trade` ADD COLUMN `entered_amount_before_fees` DECIMAL(15,5) NULL AFTER `entered_price`;
ALTER TABLE `user_trade` ADD COLUMN `entered_fees` DECIMAL(15,5) NULL AFTER `entered_amount_before_fees`;
ALTER TABLE `user_trade` ADD COLUMN `entered_amount` DECIMAL(15,5) NULL AFTER `entered_fees`;
ALTER TABLE `user_trade` ADD COLUMN `closed_amount_before_fees` DECIMAL(15,5) NULL AFTER `closed_price`;
ALTER TABLE `user_trade` ADD COLUMN `closed_fees` DECIMAL(15,5) NULL AFTER `closed_amount_before_fees`;
ALTER TABLE `user_trade` CHANGE COLUMN `closed_amount` `closed_amount` DECIMAL(15,5) NULL DEFAULT NULL AFTER `closed_fees`;

-- 1.13
ALTER TABLE `instrument` 
ADD COLUMN `saxo_asset_type` VARCHAR(32) NULL AFTER `current_price`,
ADD COLUMN `saxo_instrument_id` VARCHAR(32) NULL AFTER `saxo_asset_type`;

-- 1.14
update user_portfolio set portfolio_funding_status='2' where portfolio_funding_status='Completed';
update user_portfolio set portfolio_funding_status='1' where portfolio_funding_status='Funded';
update user_portfolio set portfolio_funding_status='0' where portfolio_funding_status='Not Funded Yet';

ALTER TABLE `user_portfolio` CHANGE COLUMN `portfolio_funding_status` `portfolio_funding_status` TINYINT(2) NULL DEFAULT 0 ;

-- 1.15
update user_portfolio set portfolio_funding_status = 0 where portfolio_funding_status is null;

--1.16
update user_portfolio set net_investment_amount=0 where net_investment_amount is null;