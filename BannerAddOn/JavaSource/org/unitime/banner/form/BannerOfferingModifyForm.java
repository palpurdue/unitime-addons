/*
 * Licensed to The Apereo Foundation under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * The Apereo Foundation licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
*/
package org.unitime.banner.form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.unitime.banner.model.BannerCourse;
import org.unitime.banner.model.BannerSection;
import org.unitime.banner.model.BannerSession;
import org.unitime.banner.model.dao.BannerCourseDAO;
import org.unitime.timetable.ApplicationProperties;
import org.unitime.timetable.model.Class_;
import org.unitime.timetable.model.CourseOffering;
import org.unitime.timetable.model.Preference;
import org.unitime.timetable.solver.ClassAssignmentProxy;
import org.unitime.timetable.util.DynamicList;
import org.unitime.timetable.util.DynamicListObjectFactory;


/**
 * 
 * @author says
 *
 */
public class BannerOfferingModifyForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5412595518174343486L;
	
    // --------------------------------------------------------- Instance Variables
	private String op;
    private Integer subjectAreaId;
    private Long bannerCourseOfferingId;
    private Long bannerConfigId;
	private Long instrOfferingId;
    private String instrOfferingName;
	private Integer itypeId;
	private Float labHours;
	private Long instrOffrConfigId;
	private String origSubparts;
	private Boolean configIsEditable;
	private Boolean showLimitOverride;
	private Boolean showLabHours;
	
	private List bannerSectionIds;
	private List itypes;
	private List bannerSectionSectionIds;
	private List bannerSectionOriginalSectionIds;
	private List consents;
	private List datePatterns;
	private List times;
	private List readOnlyClasses;
	private List bannerSectionLabels;
	private List bannerSectionLabelIndents;
	private List rooms;
	private List instructors;
	private List readOnlySubparts;
	private List limitOverrides;
	private List courseCredits;
	private List courseCreditOverrides;
	private List classLimits;
	private List defaultCampus;
	private List campusOverrides;
	private List campuses;
	private List classHasErrors;
	private List bannerCampusOverrides;
	
	/*
	private static String BANNER_SECTION_IDS_TOKEN = "bannerSectionIds";
	private static String BANNER_SECTION_SECTION_IDS_TOKEN = "bannerSectionSectionIds";
	private static String BANNER_SECTION_ORIGINAL_SECTION_IDS_TOKEN = "bannerSectionOriginalSectionIds";
	private static String ITYPES_TOKEN = "itypes";
	private static String CONSENT_TOKEN = "consents";
	private static String READ_ONLY_CLASSES_TOKEN = "readOnlyClasses";
	private static String BANNER_SECTION_LABELS_TOKEN = "bannerSectionLabels";
	private static String BANNER_SECTION_LABEL_INDENTS_TOKEN = "bannerSectionLabelIndents";
	private static String DATE_PATTERNS_TOKEN = "datePatterns";
	private static String TIMES_TOKEN = "times";
	private static String ROOMS_TOKEN = "rooms";
	private static String INSTRUCTORS_TOKEN = "instructors";
	*/

    // --------------------------------------------------------- Classes

    /** Factory to create dynamic list element for Course Offerings */
    protected DynamicListObjectFactory factoryClasses = new DynamicListObjectFactory() {
        public Object create() {
            return new String(Preference.BLANK_PREF_VALUE);
        }
    };
		
    // --------------------------------------------------------- Methods
    /** 
     * Method validate
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    public ActionErrors validate(
        ActionMapping mapping,
        HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();

        // Get Message Resources
        MessageResources rsc = 
            (MessageResources) super.getServlet()
            	.getServletContext().getAttribute(Globals.MESSAGES_KEY);
        
        if (op.equals(rsc.getMessage("button.update"))) {
	        // Check Instructional Offering Config
	        if (this.instrOffrConfigId==null || this.instrOffrConfigId.intValue()<=0) {
	            errors.add("instrOffrConfigId", new ActionMessage("errors.required", "Instructional Offering Config"));            
	        }
	        // Validate class limits provide space that is >= limit for the instructional offering config
	        initClassHasErrorsToFalse();
	        validateBannerSectionData(errors);
        }
        
        return errors;
    }
    
    private void validateBannerSectionData(ActionErrors errors){
    	HashSet<String> idSet = new HashSet<String>();
    	int prevSize = idSet.size();
    	BannerCourseDAO bcDao = BannerCourseDAO.getInstance();
    	Session hibSession = bcDao.getSession();
    	BannerCourse bc = bcDao.get(getBannerCourseOfferingId());
    	CourseOffering co = bc.getCourseOffering(hibSession);
		String sectionIdRegex = "^[0-9A-Z]{1,3}$";
    	Pattern pattern = Pattern.compile(sectionIdRegex);
    	for(int index = 0 ; index < this.getBannerSectionSectionIds().size(); index++){
    		String sectionId = (String)this.getBannerSectionSectionIds().get(index);
    		if (sectionId == null || sectionId.trim().length() == 0){
    			errors.add("uniqueSectionId", new ActionMessage("errors.generic", "Section Index cannot be null: " + (String) this.getBannerSectionLabels().get(index)));
    			this.getClassHasErrors().set(index, Boolean.valueOf(true));    			
    		}
    		if (sectionId != null){
	    		sectionId = sectionId.toUpperCase();
	    		try { 
			    	Matcher matcher = pattern.matcher(sectionId);
			    	if (!matcher.find()) {
		    			errors.add("invalid section id", new ActionMessage("errors.generic", "Section Index can only consist of numbers or alpha characters: " + (String) this.getBannerSectionLabels().get(index)));
		    			this.getClassHasErrors().set(index, Boolean.valueOf(true));    			
			    	}
		    	}
		    	catch (Exception e) {
			        errors.add("courseNbr", new ActionMessage("errors.generic", "Section Index cannot be matched to regular expression: " + sectionIdRegex + ". Reason: " + e.getMessage()));
	    			this.getClassHasErrors().set(index, Boolean.valueOf(true));    			
		    	}
    		}
    		idSet.add(sectionId);
    		if (idSet.size() != (prevSize + 1)){
    			errors.add("uniqueSectionId", new ActionMessage("errors.generic", "Section Index must be unique for: " + (String) this.getBannerSectionLabels().get(index)));
    			this.getClassHasErrors().set(index, Boolean.valueOf(true)); 
    		} else {
    			prevSize++;
    		}
    		if (sectionId.equals("999")) {
    			errors.add("maxSectionId", new ActionMessage("errors.generic", "Section Index must be less than 999: " + (String) this.getBannerSectionLabels().get(index)));
    			this.getClassHasErrors().set(index, Boolean.valueOf(true));     			
    		}
    		if (!this.getBannerSectionOriginalSectionIds().contains((String)this.getBannerSectionSectionIds().get(index))){
    			if (!BannerSection.isSectionIndexUniqueForCourse(co.getInstructionalOffering().getSession(), co, hibSession, (String)this.getBannerSectionSectionIds().get(index))){
        			errors.add("uniqueSectionId", new ActionMessage("errors.generic", "New Section Index must be unique for: " + (String) this.getBannerSectionLabels().get(index)));
        			this.getClassHasErrors().set(index, Boolean.valueOf(true));   				
    			}
    		}
    		if (this.showLimitOverride.booleanValue()){
    			String limitStr = (String)this.getLimitOverrides().get(index);
				Integer limit = null;
				if (limitStr != null && limitStr.trim().length() > 0){
					try {
						limit = Integer.valueOf(limitStr);
					} catch (Exception e) {
	        			errors.add("limitOverride", new ActionMessage("errors.generic", "The limit override must be an integer number: " + (String) this.getBannerSectionLabels().get(index)));
	        			this.getClassHasErrors().set(index, Boolean.valueOf(true));   				
					}
					
				}
				String classLimitStr = (String)this.getClassLimits().get(index);
    			Integer classLimit = Integer.valueOf(classLimitStr);
    			if (limit != null && limit.intValue() > classLimit.intValue()){
        			errors.add("limitOverride", new ActionMessage("errors.generic", "The limit override cannot get greater than the class limit: " + (String) this.getBannerSectionLabels().get(index)));
        			this.getClassHasErrors().set(index, Boolean.valueOf(true));   				
    			}
    			if (limit != null && limit.intValue() < 0){
    				errors.add("limitOverride", new ActionMessage("errors.generic", "The limit override must be greater than or equal to 0: " + (String) this.getBannerSectionLabels().get(index)));
        			this.getClassHasErrors().set(index, Boolean.valueOf(true));   	
    			}
    		}
    		String creditOverrideStr = (String)this.getCourseCreditOverrides().get(index);
    		Float creditOverride = null;
    		if (creditOverrideStr != null && creditOverrideStr.trim().length() > 0){
    			try {
					creditOverride = Float.valueOf(creditOverrideStr);
				} catch (Exception e) {
        			errors.add("creditOverride", new ActionMessage("errors.generic", "The course credit override must be a number: " + (String) this.getBannerSectionLabels().get(index)));
        			this.getClassHasErrors().set(index, Boolean.valueOf(true));   				
				}
				if (creditOverride != null && creditOverride < 0){
    				errors.add("creditOverride", new ActionMessage("errors.generic", "The course credit override must be greater than or equal to 0: " + (String) this.getBannerSectionLabels().get(index)));
        			this.getClassHasErrors().set(index, Boolean.valueOf(true));   						
				}
    		}
    	}
    }
              
    private void initClassHasErrorsToFalse(){
		this.setClassHasErrors(DynamicList.getInstance(new ArrayList(), factoryClasses));
		for(Iterator it = this.getBannerSectionIds().iterator(); it.hasNext();){
			this.getClassHasErrors().add(Boolean.valueOf(false));
			it.next();
		}
    }
     
    /** 
     * Method reset
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    	bannerCourseOfferingId = null;
    	bannerConfigId = null;
    	instrOfferingId = null;
    	itypeId = null;
    	labHours = null;
    	instrOffrConfigId = null;
    	instrOfferingName = "";
    	origSubparts = "";
    	configIsEditable = Boolean.valueOf(false);
    	showLimitOverride = Boolean.valueOf(false);
    	showLabHours = Boolean.valueOf(false);
    	resetLists();
    }
    
    private void resetLists(){
    	bannerSectionIds = DynamicList.getInstance(new ArrayList(), factoryClasses);
    	bannerSectionSectionIds = DynamicList.getInstance(new ArrayList(), factoryClasses);
    	bannerSectionOriginalSectionIds = DynamicList.getInstance(new ArrayList(), factoryClasses);
    	itypes = DynamicList.getInstance(new ArrayList(), factoryClasses);
    	consents = DynamicList.getInstance(new ArrayList(), factoryClasses);
    	times = DynamicList.getInstance(new ArrayList(), factoryClasses);
    	readOnlyClasses = DynamicList.getInstance(new ArrayList(), factoryClasses);
       	classHasErrors = DynamicList.getInstance(new ArrayList(), factoryClasses);
       	bannerSectionLabels = DynamicList.getInstance(new ArrayList(), factoryClasses);
       	bannerSectionLabelIndents = DynamicList.getInstance(new ArrayList(), factoryClasses);
       	rooms = DynamicList.getInstance(new ArrayList(), factoryClasses);
       	instructors = DynamicList.getInstance(new ArrayList(), factoryClasses);
    	datePatterns = DynamicList.getInstance(new ArrayList(), factoryClasses);
    	readOnlySubparts = DynamicList.getInstance(new ArrayList(), factoryClasses);
       	limitOverrides = DynamicList.getInstance(new ArrayList(), factoryClasses);
       	classLimits = DynamicList.getInstance(new ArrayList(), factoryClasses);
       	courseCredits = DynamicList.getInstance(new ArrayList(), factoryClasses);
       	courseCreditOverrides = DynamicList.getInstance(new ArrayList(), factoryClasses);
       	defaultCampus = DynamicList.getInstance(new ArrayList(), factoryClasses);
       	campusOverrides = DynamicList.getInstance(new ArrayList(), factoryClasses);
       	campuses = DynamicList.getInstance(new ArrayList(), factoryClasses);
       	bannerCampusOverrides = DynamicList.getInstance(new ArrayList(), factoryClasses);
    }
     
	public List getBannerSectionIds() {
		return bannerSectionIds;
	}
	public void setBannerSectionIds(List bannerSectionIds) {
		this.bannerSectionIds = bannerSectionIds;
	}
	public List getClassHasErrors() {
		return classHasErrors;
	}
	public void setClassHasErrors(List classHasErrors) {
		this.classHasErrors = classHasErrors;
	}
	public List getBannerSectionLabels() {
		return bannerSectionLabels;
	}
	public void setBannerSectionLabels(List bannerSectionLabels) {
		this.bannerSectionLabels = bannerSectionLabels;
	}
	public List getInstructors() {
		return instructors;
	}
	public void setInstructors(List instructors) {
		this.instructors = instructors;
	}
	public List getDatePatterns() {
		return datePatterns;
	}
	public void setDatePatterns(List datePatterns) {
		this.datePatterns = datePatterns;
	}
	public Long getInstrOffrConfigId() {
		return instrOffrConfigId;
	}
	public void setInstrOffrConfigId(Long instrOffrConfigId) {
		this.instrOffrConfigId = instrOffrConfigId;
	}
	public Integer getItypeId() {
		return itypeId;
	}
	public void setItypeId(Integer itypeId) {
		this.itypeId = itypeId;
	}
	public Float getLabHours() {
		return labHours;
	}
	public void setLabHours(Float labHours) {
		this.labHours = labHours;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public List getTimes() {
		return times;
	}
	public void setTimes(List times) {
		this.times = times;
	}
	public List getBannerSectionSectionIds() {
		return bannerSectionSectionIds;
	}
	public void setBannerSectionSectionIds(List bannerSectionSectionIds) {
		this.bannerSectionSectionIds = bannerSectionSectionIds;
	}
	public List getReadOnlyClasses() {
		return readOnlyClasses;
	}
	public void setReadOnlyClasses(List readOnlyClasses) {
		this.readOnlyClasses = readOnlyClasses;
	}	
	public Long getInstrOfferingId() {
		return instrOfferingId;
	}
	public void setInstrOfferingId(Long instrOfferingId) {
		this.instrOfferingId = instrOfferingId;
	}

	    
	public void addToBannerSections(BannerSession bsess, BannerSection bs, Class_ cls, ClassAssignmentProxy classAssignmentProxy, Boolean isReadOnly, String indent, Boolean canShowLimitOverridesIfNeeded){
		
		this.showLimitOverride = Boolean.valueOf(canShowLimitOverridesIfNeeded?bs.isCrossListedSection(null):false);
		this.bannerSectionLabels.add(bs.bannerSectionLabel());
		this.bannerSectionLabelIndents.add(indent);
		this.bannerSectionIds.add(bs.getUniqueId().toString());
		this.bannerSectionSectionIds.add(bs.getSectionIndex());
		this.bannerSectionOriginalSectionIds.add(bs.getSectionIndex());
		this.itypes.add(cls.getSchedulingSubpart().getItype().getSis_ref());
		this.consents.add(bs.effectiveConsentType()==null?null:bs.effectiveConsentType().getUniqueId());
		this.readOnlyClasses.add(isReadOnly.toString());
		this.classHasErrors.add(Boolean.valueOf(false).toString());	
		this.datePatterns.add(bs.buildDatePatternHtml(classAssignmentProxy));
		this.times.add(bs.buildAssignedTimeHtml(classAssignmentProxy));
		this.rooms.add(bs.buildAssignedRoomHtml(classAssignmentProxy));
		this.instructors.add(bs.buildInstructorHtml());
		if (showLimitOverride.booleanValue()){
			this.limitOverrides.add(bs.getOverrideLimit()==null?"":bs.getOverrideLimit());
			this.classLimits.add(bs.maxEnrollBasedOnClasses(null));
		}
		this.courseCreditOverrides.add(bs.getOverrideCourseCredit()==null?"":bs.getOverrideCourseCredit());
		this.courseCredits.add(bs.courseCreditStringBasedOnClass(cls));
		this.defaultCampus.add(bs.getDefaultCampusCode(bsess, cls));
		this.campusOverrides.add(bs.getBannerCampusOverride()==null?null:bs.getBannerCampusOverride().getUniqueId());
		this.campuses.add(bs.getCampusCode(bsess, cls));
	}
	
	public Integer getSubjectAreaId() {
		return subjectAreaId;
	}

	public void setSubjectAreaId(Integer subjectAreaId) {
		this.subjectAreaId = subjectAreaId;
	}

	public String getInstrOfferingName() {
		return instrOfferingName;
	}

	public void setInstrOfferingName(String instrOfferingName) {
		this.instrOfferingName = instrOfferingName;
	}

	public List getBannerSectionLabelIndents() {
		return bannerSectionLabelIndents;
	}

	public void setBannerSectionLabelIndents(List bannerSectionLabelIndents) {
		this.bannerSectionLabelIndents = bannerSectionLabelIndents;
	}

	public List getItypes() {
		return itypes;
	}

	public void setItypes(List itypes) {
		this.itypes = itypes;
	}

	public List getConsents() {
		return consents;
	}

	public void setConsents(List consents) {
		this.consents = consents;
	}

	public String getOrigSubparts() {
		return origSubparts;
	}

	public void setOrigSubparts(String origSubparts) {
		this.origSubparts = origSubparts;
	}


	public List getReadOnlySubparts() {
		return readOnlySubparts;
	}

	public void setReadOnlySubparts(List readOnlySubparts) {
		this.readOnlySubparts = readOnlySubparts;
	}

	public List getRooms() {
		return rooms;
	}

	public void setRooms(List rooms) {
		this.rooms = rooms;
	}

	public Long getBannerCourseOfferingId() {
		return bannerCourseOfferingId;
	}

	public void setBannerCourseOfferingId(Long bannerCourseOfferingId) {
		this.bannerCourseOfferingId = bannerCourseOfferingId;
	}

	public List getBannerSectionOriginalSectionIds() {
		return bannerSectionOriginalSectionIds;
	}

	public void setBannerSectionOriginalSectionIds(
			List bannerSectionOriginalSectionIds) {
		this.bannerSectionOriginalSectionIds = bannerSectionOriginalSectionIds;
	}

	public Long getBannerConfigId() {
		return bannerConfigId;
	}

	public void setBannerConfigId(Long bannerConfigId) {
		this.bannerConfigId = bannerConfigId;
	}

	public Boolean getConfigIsEditable() {
		return configIsEditable;
	}

	public void setConfigIsEditable(Boolean configIsEditable) {
		this.configIsEditable = configIsEditable;
	}

	public Boolean getShowLabHours() {
		return showLabHours;
	}

	public void setShowLabHours(Boolean showLabHours) {
		this.showLabHours = showLabHours;
	}

	public Boolean getShowLimitOverride() {
		return showLimitOverride;
	}

	public void setShowLimitOverride(Boolean showLimitOverride) {
		if (ApplicationProperties.getProperty("tmtbl.banner.section.limit.overrides_allowed", "true").equalsIgnoreCase("true")) {
			this.showLimitOverride = showLimitOverride;
		} else {
			this.showLimitOverride = Boolean.valueOf(false);	
		}
	}

	public List getLimitOverrides() {
		return limitOverrides;
	}

	public void setLimitOverrides(List limitOverrides) {
		this.limitOverrides = limitOverrides;
	}

	public List getCourseCreditOverrides() {
		return courseCreditOverrides;
	}

	public void setCourseCreditOverrides(List courseCreditOverrides) {
		this.courseCreditOverrides = courseCreditOverrides;
	}

	public List getClassLimits() {
		return classLimits;
	}

	public void setClassLimits(List classLimits) {
		this.classLimits = classLimits;
	}

	public List getCourseCredits() {
		return courseCredits;
	}

	public void setCourseCredits(List courseCredits) {
		this.courseCredits = courseCredits;
	}

	public List getDefaultCampus() {
		return defaultCampus;
	}

	public void setDefaultCampus(List defaultCampus) {
		this.defaultCampus = defaultCampus;
	}

	public List getCampusOverrides() {
		return campusOverrides;
	}

	public void setCampusOverrides(List campusOverrides) {
		this.campusOverrides = campusOverrides;
	}

	public List getCampuses() {
		return campuses;
	}

	public void setCampuses(List campuses) {
		this.campuses = campuses;
	}

	public List getBannerCampusOverrides() {
		return bannerCampusOverrides;
	}

	public void setBannerCampusOverrides(List bannerCampusOverrides) {
		this.bannerCampusOverrides = bannerCampusOverrides;
	}

}
