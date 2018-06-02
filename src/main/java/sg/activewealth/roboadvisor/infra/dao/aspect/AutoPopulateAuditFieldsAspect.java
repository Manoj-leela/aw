package sg.activewealth.roboadvisor.infra.dao.aspect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Transient;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.infra.dto.UserSessionDto;
import sg.activewealth.roboadvisor.infra.model.AbstractModel;
import sg.activewealth.roboadvisor.infra.service.UserSessionService;
import sg.activewealth.roboadvisor.infra.utils.SystemUtils;
import sg.activewealth.roboadvisor.portfolio.model.Portfolio;

@Component
@Aspect
public class AutoPopulateAuditFieldsAspect {
	
	@Autowired
	private UserSessionService userSessionService;

	@SuppressWarnings("rawtypes")
	@Around("execution(* sg.activewealth.roboadvisor.*.dao.*Dao.save(..))")
	public Object aspectMethod(ProceedingJoinPoint call) throws Throwable {
		Object param = call.getArgs()[0];
		Integer AUDIT_FIELD_POPULATION_MAX_RECURSE_DEPTH = 1;
		
		if (param instanceof AbstractModel) {
			//for create/update, 1st param definitely baseentity
            AbstractModel abstractModel = (AbstractModel) param;
            buildMandatoryFields(abstractModel, userSessionService.get(), null, AUDIT_FIELD_POPULATION_MAX_RECURSE_DEPTH);
		} else if (param instanceof List) {
			List list = (List) param;
			for (Object obj : list) {
				if (obj instanceof AbstractModel) {
					AbstractModel abstractModel = (AbstractModel) obj;

					buildMandatoryFields(abstractModel, userSessionService.get(), 1, AUDIT_FIELD_POPULATION_MAX_RECURSE_DEPTH); //do not recurse in
				}
			}
		}
		
		return call.proceed();
	}

	private static final String AUDIT_FIELD_NAME_DATE_CREATED = "createdOn";
	private static final String AUDIT_FIELD_NAME_DATE_UPDATED = "updatedOn";
	private static final String AUDIT_FIELD_NAME_CREATED_BY = "createdBy";
	private static final String AUDIT_FIELD_NAME_UPDATED_BY = "updatedBy";
	public static final String AUDIT_FIELD_MODEL_PACKAGE_NAME = ".model.";
	public static final String AUDIT_FIELD_COLLECTION_TYPE_NAME = "java.util.List";
	
	@SuppressWarnings("rawtypes")
	private AbstractModel buildMandatoryFields(AbstractModel baseEntity, UserSessionDto userSession, Integer depth, final Integer maxRecurseDepth) {
		if (depth == null) depth = 0;
		
		List<String> methodNames = new ArrayList<String>();
		methodNames.add(AUDIT_FIELD_NAME_DATE_CREATED);
		methodNames.add(AUDIT_FIELD_NAME_DATE_UPDATED);
		methodNames.add(AUDIT_FIELD_NAME_CREATED_BY);
		methodNames.add(AUDIT_FIELD_NAME_UPDATED_BY);		

		for (int i = 0; i < methodNames.size(); i++) {
			String methodName = methodNames.get(i).toString();

			Method method = null;
			try {
				//invoke only if field is empty
				if (methodName.equals(AUDIT_FIELD_NAME_DATE_CREATED) || methodName.equals(AUDIT_FIELD_NAME_DATE_UPDATED)) {
					boolean proceedToSetDateValue = true;
					//if is date created, check if there is an existing value. always perform for UPDATE
					if (methodName.equals(AUDIT_FIELD_NAME_DATE_CREATED)) {
						//try and check if createdOn have been set
						Object existingDateCreated = baseEntity.getClass().getMethod(SystemUtils.getInstance().buildFieldGetter(methodName), null).invoke(baseEntity, null);
						if (existingDateCreated != null) 
							proceedToSetDateValue = false;
					}

					if (proceedToSetDateValue) {
						LocalDateTime dateToSet = LocalDateTime.now();						
						method = baseEntity.getClass().getMethod(SystemUtils.getInstance().buildFieldSetter(methodName), new Class[]{LocalDateTime.class});
						method.invoke(baseEntity, new Object[]{dateToSet});
					}
				}

				else {
					if (userSession != null) {
						method = baseEntity.getClass().getMethod(SystemUtils.getInstance().buildFieldSetter(methodName), new Class[]{String.class});
						if (methodName.equals(AUDIT_FIELD_NAME_CREATED_BY) || methodName.equals(AUDIT_FIELD_NAME_UPDATED_BY))						
							method.invoke(baseEntity, new Object[]{userSession.getUserId()});
					}
				}
			}
			catch (NoSuchMethodException e) { } //ignore if no such method.
			catch (IllegalAccessException e) { }
			catch (InvocationTargetException e) { }
		}

		depth++;

		//do for composite and List properties
		if (depth <= maxRecurseDepth) {
			Field[]fields = baseEntity.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				Class fieldType = field.getType();
				//if is a composite property recurse
				if (fieldType.getName().contains(AUDIT_FIELD_MODEL_PACKAGE_NAME)) {
					try {
						AbstractModel compositeObject = (AbstractModel) baseEntity.getClass().getMethod(SystemUtils.getInstance().buildFieldGetter(field.getName()), null).invoke(baseEntity, null);
						if (compositeObject != null && Hibernate.isInitialized(compositeObject)) 
							this.buildMandatoryFields(compositeObject, userSession, depth, maxRecurseDepth);
					}
					catch (NoSuchMethodException e) { } //ignore if no such method.
					catch (IllegalAccessException e) { }
					catch (InvocationTargetException e) { }
				}
				//if is a Set property, iterate and recurse
				if (fieldType.getName().equals(AUDIT_FIELD_COLLECTION_TYPE_NAME) &&
						!field.isAnnotationPresent(Transient.class)) {
					try {
						Collection<AbstractModel> collectionValue = (Collection<AbstractModel>) baseEntity.getClass().getMethod(SystemUtils.getInstance().buildFieldGetter(field.getName()), null).invoke(baseEntity, null);
						if (collectionValue != null && Hibernate.isInitialized(collectionValue)) {
							for (Iterator<AbstractModel> itr = collectionValue.iterator(); itr.hasNext(); ) {
								AbstractModel setObject = itr.next();
								if (setObject != null) 
									this.buildMandatoryFields(setObject, userSession, depth, maxRecurseDepth);
							}
						}
					}
					catch (NoSuchMethodException e) { } //ignore if no such method.
					catch (IllegalAccessException e) { }
					catch (InvocationTargetException e) { }
				}
			}
		}	
		
		return baseEntity;
	}
	
}
