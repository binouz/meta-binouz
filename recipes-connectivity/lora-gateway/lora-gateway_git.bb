DESCRIPTION = "Lora gateway library for concentrators based on semtech chips"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a2bdef95625509f821ba00460e3ae0eb"

DEPENDS = ""
RDEPENDS = ""

SRC_URI = "git://git@github.com/Lora-net/lora_gateway.git;protocol=ssh"

S = "${WORKDIR}/git"
SRCREV_pn-${PN} = "${AUTOREV}"

do_compile() {
    oe_runmake -C libloragw
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}/lora
    install -m 0755 ${S}/libloragw/libloragw.a ${D}${libdir}/libloragw.a
    install -m 0644 ${S}/libloragw/inc/* ${D}${includedir}/lora
}

FILES_${PN} = "${libdir}/libloragw.a ${includedir}/lora"
