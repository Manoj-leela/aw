package sg.activewealth.roboadvisor.infra.helper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

@Component
public class PropertiesHelper {

    public SimpleDateFormat DATE_FORMAT_FOR_FORM_OBJECT =
            new SimpleDateFormat(DATE_FORMAT_FOR_FORM);

    public static final String DATE_FORMAT_FOR_FORM = "dd/MM/yyyy";

    public static final String DATE_FORMAT_FOR_PRINT = "dd MMM yyyy";

    public static final String DATE_FORMAT_FOR_GRAPH = "dd MMM";

    public static final String DATE_TIME_FORMAT_FOR_PRINT = "dd MMM yyyy, HH:mm'h'";

    public static final String DOUBLE_FORMAT_FOR_PRINT = "#0.00";

    public static final String INTEGER_FORMAT_FOR_PRINT = "#0";

    public static final String INTEGER_FORMAT_WITH_SIGNS_FOR_PRINT = "'+'#0;'-'#0";

    public static final String ROLENAME_COUNTRY_MARKETING_MANAGER = "Country Marketing Manager";

    public static final String ROLENAME_MEMBER = "Member";

    public static final String ROLENAME_FINANCE_HQ = "Finance HQ";

    public static final String ROLENAME_FINANCE = "Finance";

    public static final String COUNTRY_ALL = "All";

    public static final int CONTENT_MAX_CHARS = 2000;
    
    public static final String BROKER_TYPE_IB = "IB";
    
    public static final String BROKER_TYPE_SAXO = "SAXO";
    
    public static final int SCALE = 5;

    // ############ APP SETTINGS #####################
    @Value("${conf.app.isProduction}")
    public Boolean appIsProduction;

    @Value("${conf.app.isSendEmail}")
    public Boolean appIsSendEmail;

    @Value("${conf.app.domain.name}")
    public String appDomainName;

    @Value("${conf.app.domain.url}")
    public String appDomainUrl;

    @Value("${conf.app.domain.url.noscheme}")
    public String appDomainUrlNoScheme;

    @Value("${conf.app.web.url}")
    public String appWebUrl;

    @Value("${conf.app.web.url.noscheme}")
    public String appWebUrlNoScheme;

    @Value("${conf.app.context.name.relative}")
    public String appContextNameRelative;

    @Value("${conf.app.context.name}")
    public String appContextName;

    @Value("${conf.app.context.name.root}")
    public String appContextNameRoot;

    @Value("${conf.app.deploy}")
    public String appDeploy;

    @Value("${conf.app.uploads}")
    public String appUploads;
    
    @Value("${conf.app.encodedfile.upload}")
    public String encodedFileUpload;

    //Nexmo SMS properties
    @Value("${conf.app.isSendNotification}")
    public Boolean appIsSendNotification;

    @Value("${nexmo.key}")
    public String nexmoKey;

    @Value("${nexmo.secret}")
    public String nexmoSecret;

    @Value("${nexmo.smsFromNumber}")
    public String smsFromNumber;

    @Value("${conf.app.name.stylized.short}")
    public String appNameStylizedShort;

    //Broker integration
    @Value("${conf.app.fireExternalCallToBroker}")
    public Boolean fireExternalCallToBroker;

    @Value("${conf.app.systemUserEmailId}")
    public String systemUserEmailId;

    @Value("${conf.app.adminUserEmailId}")
    public String adminUserEmailId;

    // ############ SECURITY #########################
    @Value("${conf.security.webNoAuthUrls}")
    public String securityWebNoAuthUrls;

    @Value("${conf.security.logAccess}")
    public Boolean securityLogAccess;

    @Value("${conf.security.neverExpireSession}")
    public Boolean securityNeverExpireSession;

    @Value("${conf.security.enableRateLimit}")
    public Boolean securityEnableRateLimit;

    @Value("${conf.security.standardHashSalt}")
    public String securityStandardHashSalt;

    @Value("${conf.security.blockedIps}")
    public String securityBlockedIps;

    @Value("${conf.portfolio.percentageLoss.threshold}")
    public int portfolioLossPercentageThreshold;

    private List<String> securityBlockedIpsList = new ArrayList<String>();

    public List<String> getSecurityBlockedIpsList() {
        if (securityBlockedIpsList.isEmpty()
                && !ValidationUtils.getInstance().isEmptyString(securityBlockedIps)) {
            securityBlockedIpsList = Arrays.asList(securityBlockedIps.split(","));
        }
        return securityBlockedIpsList;
    }

    @Value("${conf.notification.systemErrors}")
    public String notificationSystemErrors;

    @Value("${conf.notification.systemScheduled}")
    public String notificationSystemScheduled;

    // ############## MAIL #################
    @Value("${mail.host}")
    public String mailHost;

    @Value("${mail.port}")
    public String mailPort;

    @Value("${mail.username}")
    public String mailUsername;

    @Value("${mail.password}")
    public String mailPassword;

    @Value("${mail.from}")
    public String mailFrom;

    @Value("${mail.bounce}")
    public String mailBounce;

    @Value("${mail.fromname}")
    public String mailFromName;

    @Value("${mail.forgotpassword.subject}")
    public String mailForgotPasswordSubject;

    @Value("${mail.resetpassword.template}")
    public String mailResetpasswordTemplate;

    @Value("${mail.verify.template}")
    public String mailVerifyTemplate;

    @Value("${mail.reminder.template}")
    public String mailReminderTemplate;

    @Value("${mail.reminder.hours}")
    public int mailReminderHours;

    // ############### Facebook Api ##################

    @Value("${facebook.graph.url}")
    public String facebookGraphUrl;

    // ############## S3 SETTINGS #############
    // @Value("${aws.s3.access_key}")
    public String awsAccesskey;

    // @Value("${aws.s3.secret_key}")
    public String awsSecretkey;

    // @Value("${aws.s3.bucket_name}")
    public String awsBucketName;

    @Value("${conf.home.rowPerpage}")
    public Integer homeRowPerPage;

    public PropertiesHelper() {
        DATE_FORMAT_FOR_FORM_OBJECT.setLenient(false);
    }

    // ######################ENQUIRY#################
    @Value("${mail.enquiry.user}")
    public String mailEnquiryUser;

    public String getAppDomainUrl() {
        return appDomainUrl;
    }
   
    // #####################QUESTIONS################
    @Value("${risk.profile.savingReason.question}")
    public String savingReasonQuestion;
   
    @Value("${risk.profile.investingFamiliarity.question}")
    public String investingFamiliarityQuestion;
    
    @Value("${risk.profile.investmentStyle.question}")
    public String investmentStyleQuestion;
    
    @Value("${risk.profile.lossTolerance.question}")
    public String lossesToleranceQuestion;
    
    @Value("${risk.profile.investmentDuration.question}")
    public String investmentDurationQuestion;
    
    @Value("${user.profile.annualIncome.question}")
    public String annualIncomeQuestion;
    
    @Value("${user.profile.investingPeriod.question}")
    public String investingPeriodQuestion;
    
    @Value("${user.profile.currentAge.question}")
    public String currentAgeQuestion;
    
    //########################ANSWERS################
    @Value("${risk.profile.savingReason.answer.1}")
    public String savingReasonAnswer1;
    
    @Value("${risk.profile.savingReason.answer.2}")
    public String savingReasonAnswer2;
    
    @Value("${risk.profile.savingReason.answer.3}")
    public String savingReasonAnswer3;
    
    @Value("${risk.profile.savingReason.answer.4}")
    public String savingReasonAnswer4;
    
    @Value("${risk.profile.savingReason.answer.5}")
    public String savingReasonAnswer5;
    
    @Value("${risk.profile.investingFamiliarity.answer.1}")
    public String investingFamiliarityAnswer1;
    
    @Value("${risk.profile.investingFamiliarity.answer.2}")
    public String investingFamiliarityAnswer2;
    
    @Value("${risk.profile.investingFamiliarity.answer.3}")
    public String investingFamiliarityAnswer3;
    
    @Value("${risk.profile.investingFamiliarity.answer.4}")
    public String investingFamiliarityAnswer4;
    
    @Value("${risk.profile.investmentStyle.answer.1}")
    public String investmentStyleAnswer1;
    
    @Value("${risk.profile.investmentStyle.answer.2}")
    public String investmentStyleAnswer2;
    
    @Value("${risk.profile.investmentStyle.answer.3}")
    public String investmentStyleAnswer3;
    
    @Value("${risk.profile.investmentStyle.answer.4}")
    public String investmentStyleAnswer4;
    
    @Value("${risk.profile.investmentStyle.answer.5}")
    public String investmentStyleAnswer5;
    
    @Value("${risk.profile.lossTolerance.answer.1}")
    public String lossesToleranceAnswer1;
    
    @Value("${risk.profile.lossTolerance.answer.2}")
    public String lossesToleranceAnswer2;
    
    @Value("${risk.profile.lossTolerance.answer.3}")
    public String lossesToleranceAnswer3;
    
    @Value("${risk.profile.lossTolerance.answer.4}")
    public String lossesToleranceAnswer4;
    
    @Value("${risk.profile.lossTolerance.answer.5}")
    public String lossesToleranceAnswer5;
    
    @Value("${risk.profile.investmentDuration.answer.1}")
    public String investmentDurationAnswer1;
    
    @Value("${risk.profile.investmentDuration.answer.2}")
    public String investmentDurationAnswer2;
    
    @Value("${risk.profile.investmentDuration.answer.3}")
    public String investmentDurationAnswer3;
    
    @Value("${risk.profile.investmentDuration.answer.4}")
    public String investmentDurationAnswer4;
    
    @Value("${user.profile.annualIncome.answer.1}")
    public String annualIncomeAnswer1;
    
    @Value("${user.profile.annualIncome.answer.2}")
    public String annualIncomeAnswer2;
    
    @Value("${user.profile.annualIncome.answer.3}")
    public String annualIncomeAnswer3;
    
    @Value("${user.profile.annualIncome.answer.4}")
    public String annualIncomeAnswer4;
    
    @Value("${user.profile.investingPeriod.answer.1}")
    public String investingPeriodAnswer1;
    
    @Value("${user.profile.investingPeriod.answer.2}")
    public String investingPeriodAnswer2;
    
    @Value("${user.profile.investingPeriod.answer.3}")
    public String investingPeriodAnswer3;
    
    @Value("${user.profile.investingPeriod.answer.4}")
    public String investingPeriodAnswer4;
    
    @Value("${user.profile.currentAge.answer.1}")
    public String currentAgeAnswer1;
    
    @Value("${user.profile.currentAge.answer.2}")
    public String currentAgeAnswer2;
    
    @Value("${user.profile.currentAge.answer.3}")
    public String currentAgeAnswer3;
    
    @Value("${user.profile.currentAge.answer.4}")
    public String currentAgeAnswer4;
    
    @Value("${user.profile.currentAge.answer.5}")
    public String currentAgeAnswer5;
    
	@Value("${annualincome_question}")
	public String userannualincomequestion;
	
	@Value("${investmentobjective_question}")
	public String investmentObjectiveQuestion;
	
	@Value("${riskreturnexpectations_question}")
	public String riskReturnExpectationsQuestion;
	
	@Value("${volatilitytolerance_question}")
	public String volatilityToleranceQuestion;
	
	@Value("${losstolerance_question}")
	public String lossToleranceQuestion;
	
	@Value("${personalityandbehavior_question}")
	public String personalityAndBehaviorQuestion;
	
	@Value("${timehorizon_question}")
	public String timeHorizonQuestion;
	
	@Value("${liquidityconsideration_1_question}")
	public String liquidityConsiderationQuestion1;
	
	@Value("${liquidityconsideration_2_question}")
	public String liquidityConsiderationQuestion2;
	
	@Value("${investmentobjective_answer_1}")
	public String investmentObjectiveAnswer1;
	
	@Value("${investmentobjective_answer_2}")
	public String investmentObjectiveAnswer2;
	
	@Value("${investmentobjective_answer_3}")
	public String investmentObjectiveAnswer3;
	
	@Value("${investmentobjective_answer_4}")
	public String investmentObjectiveAnswer4;
	
	@Value("${investmentobjective_answer_5}")
	public String investmentObjectiveAnswer5;
	
	@Value("${riskreturnexpectations_answer_1}")
	public String riskReturnExpectationsAnswer1;
	
	@Value("${riskreturnexpectations_answer_2}")
	public String riskReturnExpectationsAnswer2;
	
	@Value("${riskreturnexpectations_answer_3}")
	public String riskReturnExpectationsAnswer3;
	
	@Value("${riskreturnexpectations_answer_4}")
	public String riskReturnExpectationsAnswer4;
	
	@Value("${riskreturnexpectations_answer_5}")
	public String riskReturnExpectationsAnswer5;
	
	@Value("${volatilitytolerance_answer_1}")
	public String volatilityToleranceAnswer1;
	
	@Value("${volatilitytolerance_answer_2}")
	public String volatilityToleranceAnswer2;
	
	@Value("${volatilitytolerance_answer_3}")
	public String volatilityToleranceAnswer3;
	
	@Value("${volatilitytolerance_answer_4}")
	public String volatilityToleranceAnswer4;
	
	@Value("${volatilitytolerance_answer_5}")
	public String volatilityToleranceAnswer5;
	
	@Value("${losstolerance_answer_1}")
	public String lossToleranceAnswer1;
	
	@Value("${losstolerance_answer_2}")
	public String lossToleranceAnswer2;
	
	@Value("${losstolerance_answer_3}")
	public String lossToleranceAnswer3;
	
	@Value("${losstolerance_answer_4}")
	public String lossToleranceAnswer4;
	
	@Value("${losstolerance_answer_5}")
	public String lossToleranceAnswer5;
	
	@Value("${personalityandbehavior_answer_1}")
	public String personalityAndBehaviorAnswer1;
	
	@Value("${personalityandbehavior_answer_2}")
	public String personalityAndBehaviorAnswer2;
	
	@Value("${personalityandbehavior_answer_3}")
	public String personalityAndBehaviorAnswer3;
	
	@Value("${personalityandbehavior_answer_4}")
	public String personalityAndBehaviorAnswer4;
	
	@Value("${timehorizon_answer_1}")
	public String timeHorizonAnswer1;
	
	@Value("${timehorizon_answer_2}")
	public String timeHorizonAnswer2;
	
	@Value("${timehorizon_answer_3}")
	public String timeHorizonAnswer3;
	
	@Value("${timehorizon_answer_4}")
	public String timeHorizonAnswer4;
	
	@Value("${liquidityconsideration_1_answer_1}")
	public String liquidityConsideration1Answer1;
	
	@Value("${liquidityconsideration_1_answer_2}")
	public String liquidityConsideration1Answer2;
	
	@Value("${liquidityconsideration_1_answer_3}")
	public String liquidityConsideration1Answer3;
	
	@Value("${liquidityconsideration_1_answer_4}")
	public String liquidityConsideration1Answer4;
	
	@Value("${liquidityconsideration_1_answer_5}")
	public String liquidityConsideration1Answer5;
	
	@Value("${liquidityconsideration_2_answer_1}")
	public String liquidityConsideration2Answer1;
	
	@Value("${liquidityconsideration_2_answer_2}")
	public String liquidityConsideration2Answer2;
	
	@Value("${liquidityconsideration_2_answer_3}")
	public String liquidityConsideration2Answer3;
	
	@Value("${investmentgoal_name_1}")
	public String investmentGoal1;
	
	@Value("${investmentgoal_name_2}")
	public String investmentGoal2;
	
	@Value("${investmentgoal_name_3}")
	public String investmentGoal3;
	
	@Value("${investmentgoal_name_4}")
	public String investmentGoal4;
	
	@Value("${investmentgoal_name_5}")
	public String investmentGoal5;
	
	@Value("${investmentgoal_name_6}")
	public String investmentGoal6;
	
	@Value("${investmentgoal_name_7}")
	public String investmentGoal7;
	
	@Value("${investmentgoal_name_8}")
	public String investmentGoal8;

	@Value("${investmentgoal_basic_question}")
	public String investmentGoalBasicQuestion;
	
	@Value("${investmentgoal_selectgoal_question}")
	public String investmentGoalSelectQuestion;
	
	@Value("${investmentgoal_basic_answer_1}")
	public String investmentGoalBasicAnswer1;
	
	@Value("${investmentgoal_basic_answer_2}")
	public String investmentGoalBasicAnswer2;

	@Value("${investmentgoal_car_goal_desc}")
	public String carGoalDesc;
	
	@Value("${investmentgoal_health_goal_desc}")
	public String healthGoalDesc;
	
	@Value("${investmentgoal_education_goal_desc}")
	public String educationGoalDesc;
	
	@Value("${investmentgoal_retirement_goal_desc}")
	public String retirementGoalDesc;
	
	@Value("${investmentgoal_wedding_goal_desc}")
	public String weddingGoalDesc;
	
	@Value("${investmentgoal_depositforhouse_goal_desc}")
	public String depositforhouseGoalDesc;
	
	@Value("${investmentgoal_childaccount_goal_desc}")
	public String childaccountGoalDesc;
	
	@Value("${investmentgoal_generalinvesting_goal_desc}")
	public String generalinvestingGoalDesc;

	@Value("${investmentgoal_car_goal_cost}")
	public String carGoalCost;
	
	@Value("${investmentgoal_health_goal_cost}")
	public String healthGoalCost;
	
	@Value("${investmentgoal_education_goal_cost}")
	public String educationGoalCost;
	
	@Value("${investmentgoal_retirement_goal_cost}")
	public String retirementGoalCost;
	
	@Value("${investmentgoal_wedding_goal_cost}")
	public String weddingGoalCost;
	
	@Value("${investmentgoal_depositforhouse_goal_cost}")
	public String depositforhomeGoalCost;
	
	@Value("${investmentgoal_childaccount_goal_cost}")
	public String childaccountGoalCost;
	
	@Value("${investmentgoal_generalinvesting_goal_cost}")
	public String generalinvestingGoalCost;
	
	@Value("${investmentobjective_title}")
	public String investmentObjectiveTitle;
	
	@Value("${riskreturnexpectations_title}")
	public String riskReturnExpectationsTitle;
	
	@Value("${volatilitytolerance_title}")
	public String volatilityToleranceTitle;
	
	@Value("${losstolerance_title}")
	public String lossToleranceTitle;
	
	@Value("${personalityandbehavior_title}")
	public String personalityAndBehaviorTitle;
	
	@Value("${timehorizon_title}")
	public String timeHorizonTitle;
	
	@Value("${liquidityconsideration_1_title}")
	public String liquidityConsideration1Title;
	
	@Value("${liquidityconsideration_2_title}")
	public String liquidityConsideration2Title;
	
	@Value("${investmentobjective_description}")
	public String investmentObjectiveDescription;
	
	@Value("${riskreturnexpectations_description}")
	public String riskReturnExpectationsDescription;
	
	@Value("${volatilitytolerance_description}")
	public String volatilityToleranceDescription;
	
	@Value("${losstolerance_description}")
	public String lossToleranceDescription;
	
	@Value("${personalityandbehavior_description}")
	public String personalityAndBehaviorDescription;
	
	@Value("${timehorizon_description}")
	public String timeHorizonDescription;
	
	@Value("${liquidityconsideration_1_description}")
	public String liquidityConsideration1Description;
	
	@Value("${liquidityconsideration_2_description}")
	public String liquidityConsideration2Description;
	
	@Value("${investment.car.goal.image}")
	public String carGoalImageUrl;
	
	@Value("${investment.childaccount.goal.image}")
	public String childAccountGoalImageUrl;
	
	@Value("${investment.depositforhouse.goal.image}")
	public String depositForHouseGoalImageUrl;
	
	@Value("${investment.education.goal.image}")
	public String educationGoalImageUrl;
	
	@Value("${investment.health.goal.image}")
	public String healthGoalImageUrl;
	
	@Value("${investment.retirement.goal.image}")
	public String retirementGoalImageUrl;
	
	@Value("${investment.wedding.goal.image}")
	public String weddingGoalImageUrl;
	
	@Value("${investment.generalinvestment.goal.image}")
	public String generalInvestmentGoalImageUrl;
	
	@Value("${investment.specific.goal.image}")
	public String specificGoalImageUrl;
	
	@Value("${conf.tradeCreationBatchSize}")
	public Integer tradeCreationBatchSize;
	
	@Value("${conf.portfolioFundReleaseBatchSize}")
	public Integer portfolioFundReleaseBatchSize;
	
	@Value("${conf.fundPortfolioBatchSize}")
	public Integer fundPortfolioBatchSize;
	
	@Value("${saxo.api.findinstrumenturl}")
	public String saxoApiFindInstrumentUrl;
	
	@Value("${saxo.api.createtradeurl}")
	public String saxoApiCreateTradeUrl;
	
	@Value("${saxo.api.updaterateurl}")
	public String saxoApiUpdateRateUrl;
	
	@Value("${conf.api.iosapp.url}")
	public String iOSAppUrl;
	
	@Value("${conf.api.androidapp.url}")
	public String androidAppUrl;

	@Value("${ios.pushNotification.certification}")
	public String iOSPushNotificationCertification;

    @Value("${ios.pushNotification.password}")
    public String iOSPushNotificationPassword;
    
    @Value("${broker.fee.percentage}")
    public String brokerFee;
    
    @Value("${broker.exchange}")
    public String brokerExchange;
    
    @Value("${broker.tws.hostname}")
    public String ibSgaweInstanceHostName;
    
    @Value("${broker.tws.port}")
    public Integer ibSgaweInstancePort;

    @Value("${broker.type}")
    public String brokerType;


    @Value("${conf.api.version}")
    public String apiVersion;
    @Value("${ib.demo.instance.hostname}")
    public String ibDemoInstanceHostName;
    
    @Value("${ib.demo.instance.port}")
    public Integer ibDemoInstancePort;
    
    
}
