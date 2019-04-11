DESCRIPTION = "Browser launcher for raspberry pi using chromium-egl-content"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit pkgconfig

DEPENDS = "libdbus-c++ libdbus-c++-native chromium-egl-content userland"
RDEPENDS_${PN} = ""

S = "${WORKDIR}/git"

SRC_URI = "git://git@github.com/binouz/rpi-chromium-egl-content-launcher.git;protocol=ssh"

SRCREV = "${AUTOREV}"

do_compile() {
  oe_runmake rpi-launcher
}

do_install() {
  install -d ${D}${bindir}
  install -d ${D}/etc/dbus-1/
  install -m 0755 rpi-launcher ${D}${bindir}/rpi-launcher
  install -m 0644 rpi-launcher.conf ${D}/etc/dbus-1/rpi-launcher.conf
}

FILES_${PN} = "${bindir}/rpi-launcher /etc/dbus-1/rpi-launcher.conf"
