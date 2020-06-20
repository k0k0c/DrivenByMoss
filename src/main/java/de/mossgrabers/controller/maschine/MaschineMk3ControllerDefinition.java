// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.maschine;

import de.mossgrabers.framework.controller.DefaultControllerDefinition;
import de.mossgrabers.framework.utils.OperatingSystem;
import de.mossgrabers.framework.utils.Pair;

import java.util.List;
import java.util.UUID;


/**
 * Definition class for the NI Maschine Mk3 controller extension.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class MaschineMk3ControllerDefinition extends DefaultControllerDefinition
{
    private static final UUID EXTENSION_ID = UUID.fromString ("9055C36B-0A41-48AD-8675-4D3F133E53AC");


    /**
     * Constructor.
     */
    public MaschineMk3ControllerDefinition ()
    {
        super (EXTENSION_ID, "Maschine Mk3", "Native Instruments", 1, 1);
    }


    /** {@inheritDoc} */
    @Override
    public List<Pair<String [], String []>> getMidiDiscoveryPairs (final OperatingSystem os)
    {
        return this.createDeviceDiscoveryPairs ("Maschine MK3 Ctrl MIDI");
    }
}