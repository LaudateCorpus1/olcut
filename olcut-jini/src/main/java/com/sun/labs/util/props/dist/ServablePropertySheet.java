package com.sun.labs.util.props.dist;

import com.sun.labs.util.props.ComponentListener;
import com.sun.labs.util.props.Configurable;
import com.sun.labs.util.props.ConfigurationManager;
import com.sun.labs.util.props.PropertyException;
import com.sun.labs.util.props.PropertySheet;
import com.sun.labs.util.props.RawPropertyData;
import net.jini.core.lease.Lease;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A property sheet which defines a collection of properties for a single
 * component in the system.
 */
public class ServablePropertySheet<T extends Configurable> extends PropertySheet<T> {
    private static final Logger logger = Logger.getLogger(ServablePropertySheet.class.getName());

    private boolean implementsRemote;

    /**
     * The time to lease this object.
     */
    private long leaseTime = Lease.ANY;

    /**
     * The name of the component containing a list of configuration entries.
     */
    private String entriesName;

    /**
     * The configuration entries to use for service registration or matching.
     */
    private ConfigurationEntry[] entries;

    private JiniConfigurationManager jcm;

    public ServablePropertySheet(T configurable, String name,
                                 JiniConfigurationManager cm, RawPropertyData rpd) {
        this((Class<T>) configurable.getClass(), name, cm, rpd);
        owner = configurable;
        jcm = cm;
    }

    public ServablePropertySheet(Class<T> confClass, String name,
                                 JiniConfigurationManager cm, RawPropertyData rpd) {
        super(confClass,name,cm,rpd);
        jcm = cm;

        //
        // Does this class implement remote?
        for (Class iface : ownerClass.getInterfaces()) {
            if (iface.equals(java.rmi.Remote.class)) {
                implementsRemote = true;
            }
        }

        leaseTime = rpd.getLeaseTime();
        entriesName = rpd.getEntriesName();

        //
        // If we're supposed to have configuration entries, then get them now.
        if (entriesName != null) {
            ConfigurationEntries ce
                    = (ConfigurationEntries) cm.lookup(entriesName);
            if (ce == null) {
                throw new PropertyException(instanceName, "entries",
                        "Cannot find entries component " + entriesName);
            }
            entries = ce.getEntries();
        }
    }

    public boolean implementsRemote() {
        return implementsRemote;
    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }

    public long getLeaseTime() {
        return leaseTime;
    }

    public ConfigurationEntry[] getEntries() {
        return entries;
    }

    @Override
    public synchronized T getOwner(ComponentListener<T> cl, boolean reuseComponent) {
        // TODO: remove this once the Jini tests pass.
        logger.setLevel(Level.ALL);

        if (!isInstantiated() || !reuseComponent) {

            ComponentRegistry registry = jcm.getComponentRegistry();
            //
            // See if we should do a lookup in a service registry.
            if (registry != null
                    && !isExportable()
                    && ((size() == 0 && implementsRemote) || isImportable())) {
                    logger.finer(String.format("Looking up instance %s in registry",
                            getInstanceName()));
                owner = (T) registry.lookup(this, cl);
                if (owner != null) {
                    return owner;
                } else if (size() == 0 && isImportable()) {
                    //
                    // We needed to look something up and no success,
                    // so return null.
                    return null;
                }
            }

            // Failed to do service lookup, instantiate directly
            super.getOwner(cl, reuseComponent);

            if (registry != null && isExportable()) {
                registry.register(owner, this);
            }
        }

        return owner;
    }

    /**
     * Gets the owning property manager
     *
     * @return the property manager
     */
    @Override
    public JiniConfigurationManager getConfigurationManager() {
        return jcm;
    }

    @Override
    public void setCM(ConfigurationManager cm) {
        if (cm instanceof JiniConfigurationManager) {
            this.cm = cm;
            this.jcm = (JiniConfigurationManager) cm;
        } else {
            throw new IllegalArgumentException("Must pass a JiniConfigurationManager to a ServablePropertySheet");
        }
    }

}
