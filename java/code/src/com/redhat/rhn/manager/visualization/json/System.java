/**
 * Copyright (c) 2017 SUSE LLC
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */

package com.redhat.rhn.manager.visualization.json;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * POJO representing system to be displayed in visualization.
 */
public class System {

    private String id;
    private String parentId;
    private String name;
    @SerializedName("contact_method")
    private String contactMethod;
    @SerializedName("base_channel")
    private String baseChannel;
    @SerializedName("base_entitlement")
    private String baseEntitlement;
    private Long checkin;
    private String type = "system";

    /**
     * Standard constructor
     */
    public System() {
    }

    /**
     * Standard constructor
     * @param idIn idIn
     * @param parentIdIn idIn of parent
     * @param nameIn nameIn
     * @param contactMethodIn contact method
     * @param baseChannelIn base channel
     * @param baseEntitlementIn base entitlement
     * @param checkinIn check-in
     */
    public System(Long idIn, Long parentIdIn, String nameIn, String contactMethodIn,
            String baseChannelIn, String baseEntitlementIn, Date checkinIn) {
        if (idIn != null) {
            this.id = idIn.toString();
        }
        if (parentIdIn != null) {
            this.parentId = parentIdIn.toString();
        }
        this.name = nameIn;
        this.contactMethod = contactMethodIn;
        this.baseChannel = baseChannelIn;
        this.baseEntitlement = baseEntitlementIn;
        if (checkinIn != null) {
            this.checkin = checkinIn.getTime();
        }
    }

    /**
     * Standard constructor
     * @param idIn idIn
     * @param nameIn nameIn
     * @param contactMethodIn contact method
     * @param baseChannelIn base channel
     * @param baseEntitlementIn base entitlement
     * @param checkinIn check-in
     */
    public System(Long idIn, String nameIn, String contactMethodIn, String baseChannelIn,
            String baseEntitlementIn, Date checkinIn) {
        if (idIn != null) {
            this.id = idIn.toString();
        }
        this.name = nameIn;
        this.contactMethod = contactMethodIn;
        this.baseChannel = baseChannelIn;
        this.baseEntitlement = baseEntitlementIn;
        if (checkinIn != null) {
            this.checkin = checkinIn.getTime();
        }
    }

    /**
     * Gets the type.
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the id.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param idIn - the id
     * @return this
     */
    public System setId(String idIn) {
        id = idIn;
        return this;
    }

    /**
     * Gets the parentId.
     *
     * @return parentId
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Sets the parentId.
     *
     * @param parentIdIn - the parentId
     * @return this
     */
    public System setParentId(String parentIdIn) {
        parentId = parentIdIn;
        return this;
    }

    /**
     * Gets the name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param nameIn - the name
     * @return this
     */
    public System setName(String nameIn) {
        name = nameIn;
        return this;
    }

    /**
     * Gets the baseChannel.
     *
     * @return baseChannel
     */
    public String getBaseChannel() {
        return baseChannel;
    }

    /**
     * Sets the baseChannel.
     *
     * @param baseChannelIn - the baseChannel
     * @return this
     */
    public System setBaseChannel(String baseChannelIn) {
        baseChannel = baseChannelIn;
        return this;
    }

    /**
     * Gets the baseEntitlement.
     *
     * @return baseEntitlement
     */
    public String getBaseEntitlement() {
        return baseEntitlement;
    }

    /**
     * Sets the baseEntitlement.
     *
     * @param baseEntitlementIn - the baseEntitlement
     * @return this
     */
    public System setBaseEntitlement(String baseEntitlementIn) {
        baseEntitlement = baseEntitlementIn;
        return this;
    }

    /**
     * Gets the checkin.
     *
     * @return checkin
     */
    public Long getCheckin() {
        return checkin;
    }

    /**
     * Sets the checkin.
     *
     * @param checkinIn - the checkin
     * @return this
     */
    public System setCheckin(Long checkinIn) {
        checkin = checkinIn;
        return this;
    }

    /**
     * Gets the contactMethod.
     *
     * @return contactMethod
     */
    public String getContactMethod() {
        return contactMethod;
    }

    /**
     * Sets the contactMethod.
     *
     * @param contactMethodIn - the contactMethod
     * @return this
     */
    public System setContactMethod(String contactMethodIn) {
        contactMethod = contactMethodIn;
        return this;
    }
}