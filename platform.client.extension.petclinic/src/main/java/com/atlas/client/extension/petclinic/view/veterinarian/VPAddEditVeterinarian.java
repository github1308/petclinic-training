package com.atlas.client.extension.petclinic.view.veterinarian;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Executions.Configs;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ButtonGroup;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Form;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Radio;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.atlas.client.extension.petclinic.core.Veterinarian;
import com.atlas.client.extension.petclinic.core.CodeValueTypes.VetSpeciality;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@MapsTo.Type(Veterinarian.class)
@Getter @Setter
public class VPAddEditVeterinarian {
	
	@Tile(imgSrc="resources/icons/autocase.svg#Layer_1", size=Tile.Size.Large) 
	private VTAddEditVeterinarian vtAddEditVeterinarian;
	
	
	@Model
	@Getter @Setter
	public static class VTAddEditVeterinarian {
		
		@Section
		private VSAddEditVeterinarian vsAddEditVeterinarian;
	}

	
	@MapsTo.Type(Veterinarian.class)
	@Getter @Setter
	public static class VSAddEditVeterinarian {
		
		@Form(cssClass="twoColumn")
		private VFAddEditVeterinarian vfAddEditVeterinarian;
	}
	
	
	@MapsTo.Type(Veterinarian.class)
	@Getter @Setter
	public static class VFAddEditVeterinarian {
		
		@ButtonGroup
		private VBGAddEditVeterinarian buttonGroup;
		
		@TextBox @MapsTo.Path
		@Label("First Name")
		private String firstName;
		
		@TextBox @MapsTo.Path
		@Label("Last Name")
		private String lastName;
		
		@Radio(postEventOnChange=true)
		@Path
		@Values(value = VetSpeciality.class)
		@Label("Speciality")
		private String speciality;
		
		@TextBox(hidden=true) @MapsTo.Path
		private String fullName;
	}
	
	
	@Model
	@Getter @Setter
	public static class VBGAddEditVeterinarian {
		
		@Config(url="/p/veterinarianview/vpVeterenarians/vtVeterinarians/vsVeterinarians/_nav?pageId=vpVeterenarians")
		@Button
		private String cancel;
		
		@Configs({
			@Config(url="/vpAddEditVeterenarian/vtAddEditVeterinarian/vsAddEditVeterinarian/vfAddEditVeterinarian/_update"),
			@Config(url="/p/veterinarianview/vpVeterenarians/vtVeterinarians/vsVeterinarians/_nav?pageId=vpVeterenarians")
		})
		@Button(style=Button.Style.PRIMARY, type = Button.Type.submit)
		private String Submit;
	}
}
