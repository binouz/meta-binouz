DESCRIPTION = "C++ websocket client/server library"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://COPYING;md5=4d168d763c111f4ffc62249870e4e0ea"

inherit pkgconfig cmake

DEPENDS = "boost"
RDEPENDS_${PN} = "boost"

SRC_URI = "git://git@github.com/zaphoyd/websocketpp.git;protocol=ssh"

S = "${WORKDIR}/git"
SRCREV_pn-${PN} = "${AUTOREV}"
