--
-- Copyright (c) 2017 SUSE LLC
--
-- This software is licensed to you under the GNU General Public License,
-- version 2 (GPLv2). There is NO WARRANTY for this software, express or
-- implied, including the implied warranties of MERCHANTABILITY or FITNESS
-- FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
-- along with this software; if not, see
-- http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
--
-- Red Hat trademarks are not licensed under GPLv2. No permission is
-- granted to use or replicate Red Hat trademarks that are incorporated
-- in this software or its documentation.
--

CREATE TABLE suseImageInfo
(
    id             NUMBER NOT NULL
                     CONSTRAINT suse_imginfo_imgid_pk PRIMARY KEY,
    name           VARCHAR2(128) NOT NULL,
    version        VARCHAR2(128) NOT NULL,
    checksum       VARCHAR2(128),
    image_arch_id  NUMBER NOT NULL
                       CONSTRAINT rhn_imginfo_said_fk
                           REFERENCES rhnServerArch (id),
    org_id         NUMBER NOT NULL
                     CONSTRAINT suse_imginfo_oid_fk
                       REFERENCES web_customer (id)
                       ON DELETE CASCADE,
    action_id      NUMBER,
    profile_id     NUMBER
                     CONSTRAINT suse_imginfo_pid_fk
                       REFERENCES suseImageProfile (profile_id)
                       ON DELETE SET NULL,
    store_id       NUMBER NOT NULL
                      CONSTRAINT suse_imginfo_sid_fk
                         REFERENCES suseImageStore (id)
                         ON DELETE SET NULL,
    build_server_id  NUMBER
                      CONSTRAINT suse_imginfo_bsid_fk
                         REFERENCES suseMinionInfo (server_id)
                         ON DELETE SET NULL,
    created        timestamp with local time zone
                     DEFAULT (current_timestamp) NOT NULL,
    modified       timestamp with local time zone
                     DEFAULT (current_timestamp) NOT NULL,
    CONSTRAINT suse_imginfo_aid_fk FOREIGN KEY (action_id, build_server_id)
        REFERENCES rhnServerAction (action_id, server_id) ON DELETE SET NULL
)
ENABLE ROW MOVEMENT
;

CREATE SEQUENCE suse_imginfo_imgid_seq;