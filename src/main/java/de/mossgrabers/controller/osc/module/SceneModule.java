// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.osc.module;

import de.mossgrabers.controller.osc.exception.IllegalParameterException;
import de.mossgrabers.controller.osc.exception.MissingCommandException;
import de.mossgrabers.controller.osc.exception.UnknownCommandException;
import de.mossgrabers.framework.daw.IHost;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.ISceneBank;
import de.mossgrabers.framework.daw.data.IScene;
import de.mossgrabers.framework.osc.IOpenSoundControlWriter;

import java.util.LinkedList;


/**
 * All global related commands.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class SceneModule extends AbstractModule
{
    /**
     * Constructor.
     *
     * @param host The host
     * @param model The model
     * @param writer The writer
     */
    public SceneModule (final IHost host, final IModel model, final IOpenSoundControlWriter writer)
    {
        super (host, model, writer);
    }


    /** {@inheritDoc} */
    @Override
    public String [] getSupportedCommands ()
    {
        return new String []
        {
            "scene"
        };
    }


    /** {@inheritDoc} */
    @Override
    public void execute (final String command, final LinkedList<String> path, final Object value) throws IllegalParameterException, UnknownCommandException, MissingCommandException
    {
        switch (command)
        {
            case "scene":
                final String sceneCommand = getSubCommand (path);
                final ISceneBank sceneBank = this.model.getCurrentTrackBank ().getSceneBank ();
                switch (sceneCommand)
                {
                    case "bank":
                        final String subCommand2 = getSubCommand (path);
                        switch (subCommand2)
                        {
                            case "+":
                                if (isTrigger (value))
                                    sceneBank.selectNextPage ();
                                break;
                            case "-":
                                if (isTrigger (value))
                                    sceneBank.selectPreviousPage ();
                                break;
                            default:
                                throw new UnknownCommandException (subCommand2);
                        }
                        break;

                    case "+":
                        if (isTrigger (value))
                            sceneBank.scrollForwards ();
                        break;

                    case "-":
                        if (isTrigger (value))
                            sceneBank.scrollBackwards ();
                        break;

                    case "create":
                        if (isTrigger (value))
                            this.model.getProject ().createSceneFromPlayingLauncherClips ();
                        break;

                    default:
                        final int scene = Integer.parseInt (sceneCommand);
                        final String sceneCommand2 = getSubCommand (path);
                        switch (sceneCommand2)
                        {
                            case "launch":
                                sceneBank.getItem (scene - 1).launch ();
                                break;
                            default:
                                throw new UnknownCommandException (sceneCommand2);
                        }
                        break;
                }
                break;

            default:
                throw new UnknownCommandException (command);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void flush (final boolean dump)
    {
        final ISceneBank sceneBank = this.model.getSceneBank ();
        for (int i = 0; i < sceneBank.getPageSize (); i++)
        {
            final IScene scene = sceneBank.getItem (i);
            final String sceneAddress = "/scene/" + (i + 1) + "/";
            this.writer.sendOSC (sceneAddress + "exists", scene.doesExist (), dump);
            this.writer.sendOSC (sceneAddress + "name", scene.getName (), dump);
            this.writer.sendOSC (sceneAddress + "selected", scene.isSelected (), dump);
        }
    }
}
