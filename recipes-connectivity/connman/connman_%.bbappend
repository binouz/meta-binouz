FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PACKAGECONFIG_append_pn-connman = " wifi "

SRC_URI += " \
	file://main.conf \
	file://settings \
"

do_install_append() {
        install -d -m 0755 ${D}${sysconfdir}/connman
        install -m 0644 ${WORKDIR}/main.conf ${D}${sysconfdir}/connman/main.conf

        install -d ${D}/var/lib/connman/
        install -m 0644 ${WORKDIR}/settings ${D}/var/lib/connman/settings
}

FILES_${PN} += " /var/lib/connman/ "
