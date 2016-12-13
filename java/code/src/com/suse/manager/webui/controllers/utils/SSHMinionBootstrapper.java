/**
 * Copyright (c) 2016 SUSE LLC
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

package com.suse.manager.webui.controllers.utils;

import com.redhat.rhn.domain.server.ContactMethod;
import com.redhat.rhn.domain.server.ServerFactory;
import com.redhat.rhn.domain.token.ActivationKeyFactory;
import com.redhat.rhn.domain.user.User;
import com.redhat.rhn.manager.token.ActivationKeyManager;
import com.suse.manager.reactor.messaging.ApplyStatesEventMessage;
import com.suse.manager.reactor.messaging.RegisterMinionEventMessageAction;
import com.suse.manager.webui.services.impl.SSHMinionsPendingRegistrationService;
import com.suse.manager.webui.services.impl.SaltService;
import com.suse.manager.webui.utils.InputValidator;
import com.suse.manager.webui.utils.gson.BootstrapParameters;
import com.suse.manager.webui.utils.gson.JSONBootstrapHosts;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.suse.manager.webui.services.impl.SaltSSHService.SSH_PUSH_PORT;
import static com.suse.manager.webui.services.impl.SaltSSHService.getSSHUser;

/**
 * Code for bootstrapping salt-ssh systems using salt-ssh.
 */
public class SSHMinionBootstrapper extends AbstractMinionBootstrapper {

    private static SSHMinionBootstrapper instance;
    private static final Logger LOG = Logger.getLogger(SSHMinionBootstrapper.class);

    /**
     * Standard constructor. For testing only - to obtain instance of this class, use
     * getInstance.
     * @param saltService salt service to use
     */
    public SSHMinionBootstrapper(SaltService saltService) {
        super(saltService);
    }

    /**
     * Get instance of the SSHMinionBootstrapper
     * @return the instance of the SSHMinionBootstrapper
     */
    public static synchronized SSHMinionBootstrapper getInstance() {
        if (instance == null) {
            instance = new SSHMinionBootstrapper(SaltService.INSTANCE);
        }
        return instance;
    }

    @Override
    protected List<String> validateJsonInput(JSONBootstrapHosts input) {
        return InputValidator.INSTANCE.validateBootstrapSSHManagedInput(input);
    }

    @Override
    protected List<String> getBootstrapMods() {
        return Arrays.asList(
                ApplyStatesEventMessage.CERTIFICATE,
                "ssh_bootstrap");
    }

    @Override
    protected Map<String, Object> createPillarData(User user, BootstrapParameters input) {
        Map<String, Object> pillar = super.createPillarData(user, input);
        pillar.put("contact_method",
            ActivationKeyManager.getInstance().findAll(user)
                    .stream()
                    .filter(ak -> input.getActivationKeys().contains(ak.getKey()))
                    .findFirst()
                    .map(ak -> ak.getContactMethod())
                    .map(method -> method.getLabel())
                    .orElse("ssh-push"));
        return pillar;
    }

    @Override
    protected Optional<String> validateContactMethod(ContactMethod desiredContactMethod) {
        boolean isIncompatible = Stream.of(
                ServerFactory.findContactMethodByLabel("ssh-push"),
                ServerFactory.findContactMethodByLabel("ssh-push-tunnel")
        ).noneMatch(cm -> cm.getId().equals(desiredContactMethod.getId()));

        if (isIncompatible) {
            return Optional.of("Selected activation key cannot be used as its contact" +
                    " method is not compatible with the salt-ssh systems.");
        }

        return Optional.empty();
    }

    @Override
    protected BootstrapResult bootstrapInternal(BootstrapParameters params, User user) {
        BootstrapResult result = super.bootstrapInternal(params, user);
        LOG.info("salt-ssh system bootstrap success: " + result.isSuccess() +
                ", proceeding with registration.");
        String minionId = params.getHost();
        String contactMethod = params.getFirstActivationKey()
                .map(ActivationKeyFactory::lookupByKey)
                .map(key -> key.getContactMethod())
                .map(method -> method.getLabel())
                .orElse("ssh-push");
        SSHMinionsPendingRegistrationService.addMinion(minionId, contactMethod);
        try {
            if (result.isSuccess()) {
                getRegisterAction().registerSSHMinion(
                        minionId,
                        params.getFirstActivationKey());
            }
        }
        finally {
            SSHMinionsPendingRegistrationService.removeMinion(minionId);
        }
        return result;
    }

    // we want to override this in tests
    protected RegisterMinionEventMessageAction getRegisterAction() {
        return new RegisterMinionEventMessageAction();
    }

    /**
     * Create the bootstrap parameters specific for salt-ssh push minions.
     * @param input json input
     * @return the bootstrap parameters
     */
    @Override
    protected BootstrapParameters createBootstrapParams(JSONBootstrapHosts input) {
        return new BootstrapParameters(input.getHost(),
                Optional.of(SSH_PUSH_PORT), getSSHUser(), input.maybeGetPassword(),
                input.getActivationKeys(), input.getIgnoreHostKeys());
    }
}