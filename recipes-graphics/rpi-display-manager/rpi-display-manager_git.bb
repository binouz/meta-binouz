DESCRIPTION = "Display manager with DBUS interface for raspberry pi"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit pkgconfig

DEPENDS = "userland dbus glib-2.0"
RDEPENDS_${PN} = "userland dbus glib-2.0"

SRC_URI = "git://git@github.com/binouz/rpi-display-manager.git;protocol=ssh"

S = "${WORKDIR}/git"
SRCREV_pn-${PN} = "${AUTOREV}"

do_compile() {
    oe_runmake
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/rpi-display-manager ${D}${bindir}/rpi-display-manager

    install -d ${D}${sysconfdir}/init.d/
    install -m 0755 ${S}/rpi-display-manager-init.sh ${D}${sysconfdir}/init.d/rpi-display-manager

    install -d ${D}${sysconfdir}/dbus-1/system.d/
    install -m 0644 ${S}/rpi-display-manager.conf ${D}${sysconfdir}/dbus-1/system.d/rpi-display-manager.conf
}

FILES_${PN} = "${bindir}/rpi-display-manager ${sysconfdir}/init.d/rpi-display-manager ${sysconfdir}/dbus-1/system.d/rpi-display-manager.conf"

inherit update-rc.d

INITSCRIPT_NAME = "rpi-display-manager"
INITSCRIPT_PARAMS = "start 99 5 2 ."
