/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.atlas.client.extension.petclinic.view.pet;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Executions.Configs;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Type;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ButtonGroup;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Calendar;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ComboBox;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Form;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Modal;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.PickList;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.PickListSelected;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextArea;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.defn.extension.EnableConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.LabelConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.VisibleConditional;
import com.atlas.client.extension.petclinic.core.CodeValueTypes.AllCategory;
import com.atlas.client.extension.petclinic.core.CodeValueTypes.CatCategory;
import com.atlas.client.extension.petclinic.core.CodeValueTypes.DogCategory;
import com.atlas.client.extension.petclinic.core.CodeValueTypes.petType;
import com.atlas.client.extension.petclinic.core.Pet;
import com.atlas.client.extension.petclinic.core.Veterinarian;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tony Lopez
 *
 */
@MapsTo.Type(Pet.class)
@Getter @Setter
public class VPAddEditPet {
	
	 @Tile(size = Tile.Size.Large)
	 private VTAddEditPet vtAddEditPet;
	 
	 @Path(linked=false)
	 private List<Veterinarian> vets;

	@Model
	@Getter @Setter
	public static class VTAddEditPet {
		
		@Label("Meal Instruction")
		@Modal(closable = true)
		private VMMealInstruction vmMealInstruction;
	    
		@Section
	    private VSAddEditPet vsAddEditPet;
	    
	}
	
	@Model
	@Getter @Setter
	public static class VSAddEditPet {
		
		@Form(cssClass="twoColumn")
		private VFAddEditPet vfAddEditPet;
		
	}
	
	@Type(Pet.class)
	@Getter @Setter
	public static class VFAddEditPet {
		
		@ButtonGroup(cssClass="text-sm-right pt-2 pb-2")
		private VBGAddPetButtonGrp vbgAddPetButtonGrp;
		
		// TODO id is not sent with UI payload using @ParamContext(enabled = false, visible = true)
	    // Should we offer a way of supporting this in favor of deprecating readOnly?
	    @Label("Pet ID")
	    @TextBox(readOnly = true)
	    @Path
	    private Long id;
		
		@Label("Pet's Name")
		@TextBox(postEventOnChange=true)
		@NotNull
		@Path
		private String name;
		
		@Label("Date of Birth")
		@Calendar(postEventOnChange=true)
		@Path 
		private LocalDate dob;
		
		@Label("Type")
		@ComboBox(postEventOnChange=true)
		@Path
		@Values(value = petType.class)
		@ValuesConditional(targetPath="../category", condition= {
			@ValuesConditional.Condition(when="state == 'Dog'", then = @Values(value=DogCategory.class)),
			@ValuesConditional.Condition(when="state == 'Cat'", then = @Values(value=CatCategory.class))
		})
		@VisibleConditional(targetPath = { "../category" }, when = "state != 'Horse'")
		@EnableConditional(targetPath = { "../category" }, when = "state != 'Parrot'")
		@LabelConditional(targetPath = "/../name", condition = {
			@LabelConditional.Condition(when = "state != null", then = @Label("<!/!>'s Name"))
		})
		private String type;
		
		@Label("Category")
		@PickList(sourceHeader="Available Category", targetHeader="Selected Category")
		private PicklistType category; 
		
		@TextArea
		@Max(value=500)
		private String notes;
		
		@Label("Add Meal Instruction")
		@Button
		@Config(url = "/vpAddEditPet/vtAddEditPet/vmMealInstruction/section/form/_get?fn=param&expr=unassignMapsTo()")
		@Config(url = "/vpAddEditPet/vtAddEditPet/vmMealInstruction/section/form/_process?fn=_setByRule&rule=rules/common/setId")
		@Config(url = "/vpAddEditPet/vtAddEditPet/vmMealInstruction/_process?fn=_setByRule&rule=togglemodal")
		private String addMealInstruction;
		
		@Label("Meal Instructions")
		@Grid(onLoad = true, expandableRows = true)
		@Path
		private List<MealInstructionLineItem> mealInstructions;
	}
	
	@Type(Pet.class)
	@Getter @Setter
	public static class PicklistType {		
		
		@Values(value=AllCategory.class)
		@Path("category")
		@PickListSelected(postEventOnChange=true)
		@NotNull
		private String[] selected;
	}
	
	@Model
	@Getter @Setter
	public static class VBGAddPetButtonGrp {
		
		@Label("Submit")
		@Button(style = Button.Style.PRIMARY,type=Button.Type.submit)
		@Configs({
			@Config(url="/vpAddEditPet/vtAddEditPet/vsAddEditPet/vfAddEditPet/_update"),
			@Config(url="/vpAddEditPet/vets/_process?fn=_set&url=/p/veterinarian/_search?fn=query"),
			@Config(url="/vpAddEditPet/.m/_process?fn=_setByRule&rule=assignpet&associatedParam=/vpAddEditPet/vets/_get"),
		//	@Config(url="/p/veterinarian:<!/.m/vetId!>/assignedPets/_process?fn=_add&value=<!/.m/id!>"),
			@Config( when="findStateByPath('/.m/vetId') != null", url="/p/veterinarian:<!/.m/vetId!>/assignedPets/_process?fn=_add&value=<!/.m/id!>"),
			@Config(url="/p/ownerview:<!/.m/ownerId!>/_nav?pageId=vpOwnerInfo")
		})
		private String submit;
	
		@Label("Cancel")
		@Button(style = Button.Style.PRIMARY, type = Button.Type.reset, browserBack = true)
		private String cancel;
	}
}