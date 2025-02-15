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
package org.unitime.colleague.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.unitime.localization.impl.Localization;
import org.unitime.localization.messages.ColleagueMessages;
import org.unitime.timetable.model.CourseOffering;
import org.unitime.timetable.util.DynamicList;
import org.unitime.timetable.util.DynamicListObjectFactory;

/**
 * 
 * @author says
 *
 */
public class ColleagueOfferingDetailForm extends ActionForm {

	protected final static ColleagueMessages MSG = Localization.create(ColleagueMessages.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -5161466018324037153L;
	// --------------------------------------------------------- Instance Variables

    private String op;   
    private Long subjectAreaId;
    private Long crsOfferingId;
    private Long instrOfferingId;
    private Long courseOfferingId;
    private Long ctrlCrsOfferingId;
    private Integer projectedDemand;
    private Integer enrollment;
    private Integer demand;
    private Integer limit;
    private Boolean unlimited;
    private Boolean notOffered;
    private Boolean isEditable;
    private Boolean isFullyEditable;
    private Boolean isManager;
    private String instrOfferingName;
    private String instrOfferingNameNoTitle;
    private List courseOfferings;
    private String subjectAreaAbbr;
    private String courseNbr;
    private String consentType;
    private String creditText;
    private String nextId;
    private String previousId;
    private String catalogLinkLabel;
    private String catalogLinkLocation;

    // --------------------------------------------------------- Classes

    /** Factory to create dynamic list element for Course Offerings */
    protected DynamicListObjectFactory factoryCourseOfferings = new DynamicListObjectFactory() {
        public Object create() {
            return new String("");
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

        throw new UnsupportedOperationException(
            "Generated method 'validate(...)' not implemented.");
    }

    /** 
     * Method reset
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        op = "view";    
        subjectAreaId = null;
        subjectAreaAbbr = null;
        courseNbr = null;
        crsOfferingId = null;
        instrOfferingId = null;
        courseOfferingId = null;
        ctrlCrsOfferingId = null;
        enrollment = null;
        demand = null;
        projectedDemand = null;
        limit = null;
        unlimited = Boolean.valueOf(false);
        notOffered = null;
        instrOfferingName = "";
        instrOfferingNameNoTitle = "";
        isEditable = null;
        isFullyEditable = null; 
        isManager = null;
        courseOfferings = DynamicList.getInstance(new ArrayList(), factoryCourseOfferings);
        nextId = previousId = null;
        creditText = "";
        catalogLinkLabel = null;
        catalogLinkLocation = null;
    }
    
    public List getCourseOfferings() {
        return courseOfferings;
    }
    public String getCourseOfferings(int key) {
        return courseOfferings.get(key).toString();
    }
    public void setCourseOfferings(int key, Object value) {
        this.courseOfferings.set(key, value);
    }
    public void setCourseOfferings(List courseOfferings) {
        this.courseOfferings = courseOfferings;
    }
    
    public Long getSubjectAreaId() {
        return subjectAreaId;
    }
    public void setSubjectAreaId(Long subjectAreaId) {
        this.subjectAreaId = subjectAreaId;
    }
    
    public Long getCrsOfferingId() {
        return crsOfferingId;
    }
    public void setCrsOfferingId(Long crsOfferingId) {
        this.crsOfferingId = crsOfferingId;
    }
    
    public Long getCtrlCrsOfferingId() {
        return ctrlCrsOfferingId;
    }
    public void setCtrlCrsOfferingId(Long ctrlCrsOfferingId) {
        this.ctrlCrsOfferingId = ctrlCrsOfferingId;
    }
    
    public Integer getDemand() {
        return demand;
    }
    public void setDemand(Integer demand) {
        this.demand = demand;
    }
    
    public Integer getProjectedDemand() {
        return projectedDemand;
    }
    public void setProjectedDemand(Integer projectedDemand) {
        this.projectedDemand = projectedDemand;
    }

    public Long getInstrOfferingId() {
        return instrOfferingId;
    }
    public void setInstrOfferingId(Long instrOfferingId) {
        this.instrOfferingId = instrOfferingId;
    }
    
    public Integer getLimit() {
        return limit;
    }    
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    public Boolean getUnlimited() {
        return unlimited;
    }    
    public void setUnlimited(Boolean unlimited) {
        this.unlimited = unlimited;
    }

    public Boolean getNotOffered() {
        return notOffered;
    }
    public void setNotOffered(Boolean notOffered) {
        this.notOffered = notOffered;
    }
    
    public Boolean getIsEditable() {
        return isEditable;
    }
    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }
    
    public Boolean getIsFullyEditable() {
        return isFullyEditable;
    }
    public void setIsFullyEditable(Boolean isFullyEditable) {
        this.isFullyEditable = isFullyEditable;
    }

    public String getOp() {
        return op;
    }
    public void setOp(String op) {
        this.op = op;
    }
        
    public String getSubjectAreaAbbr() {
        return subjectAreaAbbr;
    }
    public void setSubjectAreaAbbr(String subjectAreaAbbr) {
        this.subjectAreaAbbr = subjectAreaAbbr;
    }
    
    public String getCourseNbr() {
        return courseNbr;
    }
    public void setCourseNbr(String courseNbr) {
        this.courseNbr = courseNbr;
    }
    
    public String getInstrOfferingName() {
        return instrOfferingName;
    }
    public void setInstrOfferingName(String instrOfferingName) {
        this.instrOfferingName = instrOfferingName;
    }    
    public String getInstrOfferingNameNoTitle() {
        return instrOfferingNameNoTitle;
    }
    public void setInstrOfferingNameNoTitle(String instrOfferingNameNoTitle) {
        this.instrOfferingNameNoTitle = instrOfferingNameNoTitle;
    }    
    
    public Boolean getIsManager() {
        return isManager;
    }
    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }
        
    public String getConsentType() {
        return consentType;
    }
    public void setConsentType(String consentType) {
        this.consentType = consentType;
    }
        
    public String getCatalogLinkLabel() {
		return catalogLinkLabel;
	}

	public void setCatalogLinkLabel(String catalogLinkLabel) {
		this.catalogLinkLabel = catalogLinkLabel;
	}

	public String getCatalogLinkLocation() {
		return catalogLinkLocation;
	}

	public void setCatalogLinkLocation(String catalogLinkLocation) {
		this.catalogLinkLocation = catalogLinkLocation;
	}

	/**
     * Add a course offering to the existing list
     * @param co Course Offering
     */
    public void addToCourseOfferings(CourseOffering co) {
        this.courseOfferings.add(co);
    }

    /**
     * @return No. of course offerings in the instr offering
     */
    public Integer getCourseOfferingCount() {
        return Integer.valueOf(this.courseOfferings.size());
    }
    
    public String getNextId() { return nextId; }
    public void setNextId(String nextId) { this.nextId = nextId; }
    public String getPreviousId() { return previousId; }
    public void setPreviousId(String previousId) { this.previousId = previousId; }
    public boolean getHasDemandOfferings() {
    	if (courseOfferings==null || courseOfferings.isEmpty()) return false;
    	for (Iterator i=courseOfferings.iterator();i.hasNext();)
    		if (((CourseOffering)i.next()).getDemandOffering()!=null) return true;
    	return false;
    }

	public String getCreditText() {
		return creditText;
	}

	public void setCreditText(String creditText) {
		this.creditText = creditText;
	}

	public Integer getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(Integer enrollment) {
		this.enrollment = enrollment;
	}

	public Long getCourseOfferingId() {
		return courseOfferingId;
	}

	public void setCourseOfferingId(Long courseOfferingId) {
		this.courseOfferingId = courseOfferingId;
	}
}
