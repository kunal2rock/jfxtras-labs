package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public class DescriptiveVJournalVBox extends DescriptiveVBox<VJournal>
{
    public DescriptiveVJournalVBox()
    {
        super();
        // remove unavailable elements
        endLabel.setVisible(false);
        endLabel = null;
        locationLabel.setVisible(false);
        locationLabel = null;
        locationTextField.setVisible(false);
        locationTextField = null;
    }
    
    @Override
    public void setupData(
//            Appointment appointment,
            VJournal vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<AppointmentGroup> appointmentGroups)
    {
        super.setupData(vComponent, startRecurrence, endRecurrence, appointmentGroups);

        // Journal supports multiple descriptions, but this control only supports one description
        if (vComponent.getDescriptions() == null)
        {
            vComponent.withDescriptions("");
        }
        descriptionTextArea.textProperty().bindBidirectional(vComponent.getDescriptions().get(0).valueProperty());
    }
}
