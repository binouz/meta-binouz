DESCRIPTION = "Optimized fork of nRF24L01 for Arduino & Raspberry Pi/Linux Devices"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a23a74b3f4caf9616230789d94217acb"

DEPENDS = "wiringpi"
RDEPENDS_${PN} = ""

SRC_URI = "git://git@github.com/nRF24/RF24.git;protocol=ssh"

S = "${WORKDIR}/git"
SRCREV_pn-${PN} = "${AUTOREV}"

do_compile() {
    oe_runmake
}

do_install() {
    install -d ${D}${libdir}
    install -m 0755 librf24.so* ${D}${libdir}/librf24.so

    sed -i 's,^HEADER_DIR=.*$,HEADER_DIR=${D}${includedir}/RF24,' Makefile.inc
    oe_runmake install-headers
}

INSANE_SKIP_${PN} = "ldflags"
INSANE_SKIP_${PN}-dev = "ldflags"

FILES_${PN} = "${libdir} ${includedir}"
FILES_${PN}-dev = ""
