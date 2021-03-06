package de.mossgrabers.controller.maschine.command.continuous;

import de.mossgrabers.controller.maschine.MaschineConfiguration;
import de.mossgrabers.controller.maschine.controller.MaschineControlSurface;
import de.mossgrabers.framework.command.continuous.KnobRowModeCommand;
import de.mossgrabers.framework.controller.ButtonID;
import de.mossgrabers.framework.daw.IBank;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.data.ITrack;


/**
 * Command to delegate the moves of a knob/fader row to the active mode. Special handling for button
 * combinations of Scene, Pattern, Select, ...
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class MainKnobRowModeCommand extends KnobRowModeCommand<MaschineControlSurface, MaschineConfiguration>
{
    /**
     * Constructor.
     *
     * @param model The model
     * @param surface The surface
     */
    public MainKnobRowModeCommand (final IModel model, final MaschineControlSurface surface)
    {
        super (-1, model, surface);
    }


    /** {@inheritDoc} */
    @Override
    public void execute (final int value)
    {
        if (this.surface.isPressed (ButtonID.SCENE1))
        {
            this.switchPage (ButtonID.SCENE1, this.model.getSceneBank (), value);
            return;
        }

        if (this.surface.isPressed (ButtonID.CLIP))
        {
            final ITrack selectedTrack = this.model.getCurrentTrackBank ().getSelectedItem ();
            if (selectedTrack != null)
                this.switchPage (ButtonID.CLIP, selectedTrack.getSlotBank (), value);
            return;
        }

        if (this.surface.isPressed (ButtonID.TRACK))
        {
            this.switchPage (ButtonID.TRACK, this.model.getCurrentTrackBank (), value);
            return;
        }

        if (this.surface.isPressed (ButtonID.SOLO))
        {
            this.switchPage (ButtonID.SOLO, this.model.getCurrentTrackBank (), value);
            return;
        }

        if (this.surface.isPressed (ButtonID.MUTE))
        {
            this.switchPage (ButtonID.MUTE, this.model.getCurrentTrackBank (), value);
            return;
        }

        if (this.surface.isPressed (ButtonID.ACCENT))
        {
            final int speed = (int) this.model.getValueChanger ().calcKnobSpeed (value, 1);
            this.surface.setTriggerConsumed (ButtonID.ACCENT);

            final MaschineConfiguration configuration = this.surface.getConfiguration ();
            final int v = Math.min (Math.max (1, configuration.getFixedAccentValue () + speed), 127);
            configuration.setFixedAccentValue (v);
            this.surface.getDisplay ().notify ("Fixed Accent: " + v);
            return;
        }

        super.execute (value);
    }


    private void switchPage (final ButtonID buttonID, final IBank<?> bank, final int value)
    {
        final int speed = (int) this.model.getValueChanger ().calcKnobSpeed (value, 1);
        this.surface.setTriggerConsumed (buttonID);
        if (speed < 0)
            bank.selectPreviousPage ();
        else
            bank.selectNextPage ();
    }
}
